package com.example.getmail.service.impl;

import java.net.URI;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.example.getmail.entity.EmailConfig;
import com.example.getmail.entity.EmailData;
import com.example.getmail.entity.PlanData;
import com.example.getmail.entity.PlanTable;
import com.example.getmail.mapper.GetMailMapper;
import com.example.getmail.service.GetMailService;
import microsoft.exchange.webservices.data.core.ExchangeService;
import microsoft.exchange.webservices.data.core.enumeration.misc.ExchangeVersion;
import microsoft.exchange.webservices.data.core.enumeration.property.WellKnownFolderName;
import microsoft.exchange.webservices.data.core.service.folder.Folder;
import microsoft.exchange.webservices.data.core.service.item.EmailMessage;
import microsoft.exchange.webservices.data.core.service.item.Item;
import microsoft.exchange.webservices.data.credential.ExchangeCredentials;
import microsoft.exchange.webservices.data.credential.WebCredentials;
import microsoft.exchange.webservices.data.search.FindItemsResults;
import microsoft.exchange.webservices.data.search.ItemView;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;

@Component  //配合Scheduled定时器使用
@Service(value = "GetMailService")
public class GetMailServiceImpl implements GetMailService {
    EmailData emailData;
    util a;
    @Resource
    //@Autowired
    private GetMailMapper getMailMapper ;
    @Override
    //插入邮箱配置信息
    public int mailInsert(List<EmailConfig> list) {
        return getMailMapper.mailInsert(list);
    }
    @Override
    //查询邮箱配置信息
    public List<EmailConfig> mailSelect(String username) {
        return getMailMapper.mailSelect(username);
    }

    @Override
    //post 删除指定邮件
    public int mailDelete(List<Integer> email_id) {
        return getMailMapper.mailDelete(email_id);
    }

    @Override
    //以同步邮箱数据生成日程表
    public  void transferfromemail()  throws Exception {
        System.setProperty("https.protocols", "TLSv1,TLSv1.1,TLSv1.2,SSLv3"); //设置TLS版本
        //使用exchange服务工具类创建服务
        //ExchangeMailUtil exchangeMailUtil = new ExchangeMailUtil(mailServer, user, password, readUrlPrefix);
        //ExchangeService service = exchangeMailUtil.getExchangeService();
        //创建exchange服务 ExchangeVersion.Exchange2010_SP1    (服务版本号)
        ExchangeService service = new ExchangeService(ExchangeVersion.Exchange2010_SP1);
        ExchangeCredentials credentials = new WebCredentials("", "giyoyo9420","outlook.com");
        service.setCredentials(credentials);
        service.setUrl(new URI("https://s.outlook.com/EWS/Exchange.asmx"));
        // Bind to the Inbox.
        Folder inbox = Folder.bind(service, WellKnownFolderName.Inbox);
        System.out.println(inbox.getDisplayName());
        ItemView itemView = new ItemView(10);

        // 查询，插入数据
        FindItemsResults<Item> findResults = service.findItems(inbox.getId(), itemView);
        ArrayList<Item> items = findResults.getItems();
        List<EmailData> list =new ArrayList<>();
        for (int i = 0; i < items.size(); i++) {
            EmailMessage message = EmailMessage.bind(service, items.get(i).getId());
            message.load();
            //将同步邮箱数据插入mysql数据库
            EmailData h = new EmailData();
            h.setEmail_ref_id(items.get(i).getId().toString()); //message ID
            h.setSender(message.getSender().toString()); //发件人
            h.setTitle(items.get(i).getSubject()); //主题
            String html_body = message.getBody().toString();
            String body = a.getContentFromHtml(html_body);
            h.setContent(body); //邮件内容
            h.setReceivetime(items.get(i).getDateTimeReceived()); //收件时间
            h.setOwner("mine");
            h.setTag("1");
            Timestamp time = new Timestamp(System.currentTimeMillis());
            h.setCreatetime(time);
            h.setUpdatetime(time);
            list.add(h);
            getMailMapper.transferfromemail(list);
        }

    }

    @Override
    //将email_data导入日程表
    public int plantableInsert(){
        return getMailMapper.plantableInsert();
    }
    @Override
    public List<PlanTable> selectThisWeek(){
        return getMailMapper.selectThisWeek();
    }

    @Override
    //post
    public int plandataInsert(List<PlanData> list){
        List<EmailData> li = new ArrayList<>();
        for(int i=0;i<li.size();i++){
            EmailData a = li.get(i);
            PlanData p = new PlanData();
            p.setOwner("me");
            p.setTitle(a.getTitle());
            p.setContent(a.getContent());
            p.setTag("1");
            p.setPlantime(emailData.getReceivetime());
            p.setSeq(0);
            Timestamp time = new Timestamp(System.currentTimeMillis());
            p.setCreatetime(time);
            p.setUpdatetime(time);
            p.setFlag("1");
            list.add(p);
        }
        return getMailMapper.plandataInsert(list);

    }
    /**
    @Scheduled(cron = "0/3 * * * * ?")
    @Override
    public void Scheduled () {
        System.out.println("111定时任务每三秒输出一次");
    }
    */
}
