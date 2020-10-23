package com.example.getmail.service.impl;

import java.net.URI;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.example.getmail.contentSimilarity.similarity.text.CosineSimilarity;
import com.example.getmail.contentSimilarity.similarity.text.TextSimilarity;
import com.example.getmail.entity.*;
import com.example.getmail.mapper.GetMailMapper;
import com.example.getmail.service.GetMailService;
import com.example.getmail.util.HtmlUtil;
import microsoft.exchange.webservices.data.core.ExchangeService;
import microsoft.exchange.webservices.data.core.enumeration.misc.ExchangeVersion;
import microsoft.exchange.webservices.data.core.enumeration.property.WellKnownFolderName;
import microsoft.exchange.webservices.data.core.enumeration.search.SortDirection;
import microsoft.exchange.webservices.data.core.service.folder.CalendarFolder;
import microsoft.exchange.webservices.data.core.service.folder.Folder;
import microsoft.exchange.webservices.data.core.service.item.Appointment;
import microsoft.exchange.webservices.data.core.service.item.EmailMessage;
import microsoft.exchange.webservices.data.core.service.item.Item;
import microsoft.exchange.webservices.data.core.service.schema.ItemSchema;
import microsoft.exchange.webservices.data.credential.ExchangeCredentials;
import microsoft.exchange.webservices.data.credential.WebCredentials;
import microsoft.exchange.webservices.data.property.complex.Attendee;
import microsoft.exchange.webservices.data.property.complex.FolderId;
import microsoft.exchange.webservices.data.property.complex.Mailbox;
import microsoft.exchange.webservices.data.search.CalendarView;
import microsoft.exchange.webservices.data.search.FindItemsResults;
import microsoft.exchange.webservices.data.search.ItemView;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;

@Component  //配合Scheduled定时器使用
@Service(value = "GetMailService")
public class GetMailServiceImpl implements GetMailService {
    //EmailData emailData;
    HtmlUtil a;         //创建提取html的body内容的工具对象
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
    //同步邮箱数据
    public  void transferFromEmail()  throws Exception {
        System.setProperty("https.protocols", "TLSv1,TLSv1.1,TLSv1.2,SSLv3"); //设置TLS版本
        ExchangeService service = new ExchangeService(ExchangeVersion.Exchange2010_SP1);
        ExchangeCredentials credentials = new WebCredentials("xgwfat@outlook.com", "giyoyo9420", "outlook.com");
        service.setCredentials(credentials);
        service.setUrl(new URI("https://s.outlook.com/EWS/Exchange.asmx"));
        Folder inbox = Folder.bind(service, WellKnownFolderName.Inbox);
        System.out.println(inbox.getDisplayName());
        ItemView itemView = new ItemView(10000);
        itemView.getOrderBy().add(ItemSchema.DateTimeReceived, SortDirection.Descending);//按时间获取
        // 查询，插入数据
        FindItemsResults<Item> findResults = service.findItems(inbox.getId(), itemView);
        ArrayList<Item> items = findResults.getItems();
        List<EmailData> list = new ArrayList<>();
        Date latestTime = getMailMapper.latestReceiveTime(); //数据库中拉取最新收件时间
        //获取当年第一天的时间
        Calendar date = Calendar.getInstance();
        int year = date.get(Calendar.YEAR);
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(Calendar.YEAR, year);
        Date currYearFirst = calendar.getTime();//当年第一天
        for (int i = 0; i < items.size(); i++) {
            EmailMessage message = EmailMessage.bind(service, items.get(i).getId());
            message.load();
            //将同步邮箱数据插入mysql数据库
            if (latestTime == null) {                                  //若数据库为空则令最新收件时间为当年1月1日
                latestTime = currYearFirst;
                Date compareTime = items.get(i).getDateTimeReceived();//当前邮件的收件时间
                if (latestTime.compareTo(compareTime) >= 0)           //根据接收时间判断当前邮件是否存在于数据库中
                    continue;
                //System.out.println(items.get(i).getSubject());
                EmailData h = new EmailData();
                h.setEmail_ref_id(items.get(i).getId().toString());        //message ID
                h.setSender(message.getSender().toString());               //发件人
                h.setTitle(items.get(i).getSubject());                     //邮件主题
                String html_body = message.getBody().toString();
                String body = a.getContentFromHtml(html_body);             //获取html文档中body的文本内容（邮件内容）
                h.setContent(body);                                        //邮件内容
                h.setReceivetime(items.get(i).getDateTimeReceived());      //收件时间
                h.setOwner("mine");                                        //拥有者
                Timestamp time = new Timestamp(System.currentTimeMillis());//获取当前时间
                h.setCreatetime(time);
                h.setUpdatetime(time);
                list.add(h);
            } else if (latestTime != null) {
                Date compareTime = items.get(i).getDateTimeReceived();
                if (latestTime.compareTo(compareTime) >= 0)         //根据接收时间判断当前邮件是否存在于数据库中
                    continue;
                //System.out.println(items.get(i).getSubject());
                EmailData h = new EmailData();
                h.setEmail_ref_id(items.get(i).getId().toString()); //message ID
                h.setSender(message.getSender().toString());        //发件人
                h.setTitle(items.get(i).getSubject());              //邮件主题
                String html_body = message.getBody().toString();
                String body = a.getContentFromHtml(html_body);       //获取html文档中body的文本内容（邮件内容）
                h.setContent(body);                                  //邮件内容
                h.setReceivetime(items.get(i).getDateTimeReceived()); //收件时间
                h.setOwner("mine");
                Timestamp time = new Timestamp(System.currentTimeMillis());//获取当前时间
                h.setCreatetime(time);
                h.setUpdatetime(time);
                list.add(h);
            }
        }
        if (CollectionUtil.isNotEmpty(list)) {
            //先判空
            getMailMapper.transferFromEmail(list);
        }
    }

    @Override
    //同步会议（日历）数据
    public void transferFromCalendar() throws Exception{
        System.setProperty("https.protocols", "TLSv1,TLSv1.1,TLSv1.2,SSLv3"); //设置TLS版本
        ExchangeService service = new ExchangeService(ExchangeVersion.Exchange2010_SP1);//邮件管理系统版本
        ExchangeCredentials credentials = new WebCredentials("xgwfat@outlook.com", "giyoyo9420", "outlook.com");
        service.setCredentials(credentials);
        service.setUrl(new URI("https://outlook.Office365.com/EWS/Exchange.asmx"));
        service.setCredentials(credentials);
        service.setTraceEnabled(true);
        // Bind to the Calendar.
        Folder inbox = Folder.bind(service, WellKnownFolderName.Calendar); //确定拉取数据来源（类型）
        System.out.println(inbox.getDisplayName());
        Date now = new Date();//获取当前时间
        Date start = DateUtils.addDays(now,-30);//设置开始时间为当前时间的前30天
        Date end = DateUtils.addDays(now, +30);      //设置截止时间为当前时间后30天
        CalendarView cView = new CalendarView(start,end);      //指定获取时间段
        //指定要查看的邮箱
        FolderId folderId = new FolderId(WellKnownFolderName.Calendar, new Mailbox("xgwfat@outlook.com"));
        CalendarFolder calendar = CalendarFolder.bind(service, folderId);
        FindItemsResults<Appointment> findResults = calendar.findAppointments(cView);
        List<ConferenceData> list =new ArrayList<>();
        List<String> listTitle=getMailMapper.selectFilterKeyFromFilter("title");  //获取过滤关键字列表
        List<String> listSender=getMailMapper.selectFilterKeyFromFilter("sender");//获取过滤邮箱列表
        String[] s=new String[listSender.size()];
        for(int i=0;i<listSender.size();i++){
            s[i]=listSender.get(i);
            System.out.println(s[i]);
        }
        try {
            findResults = service.findAppointments(folderId, cView);
        } catch (Exception e) {
            e.printStackTrace();
        }
        ArrayList<Appointment> appointmentItems = findResults==null?null:findResults.getItems();
        for(Appointment ap:appointmentItems) {
            ap.load();
            String subject = ap.getSubject();
            String sender =ap.getOrganizer().toString();
            //是否含有过滤关键字
            boolean statusTitle = StrUtil.containsAny(subject, listTitle.toString().toCharArray());
            //是否是黑名单邮箱（数据表中只存储黑名单）
            boolean statusSender=StrUtil.equalsAny(sender,s);
            //如邮箱主题包含过滤关键词或发件人在黑名单中，则过滤该会议
            if (statusTitle||statusSender) {
                continue;
            } else {
                ConferenceData c = new ConferenceData();
                c.setCalendar_id(ap.getId().toString());    //message ID
                c.setSender(ap.getOrganizer().toString());  //会议组织者
                c.setReceivetime(ap.getDateTimeReceived()); //接收时间
                c.setTitle(ap.getSubject());                //会议主题
                String html_body = ap.getBody().toString();
                String body = a.getContentFromHtml(html_body); //去除html框架，获取body中的文本内容
                c.setContent(body);                            //会议内容
                c.setPosition(ap.getLocation());               //会议位置
                c.setStarttime(ap.getStart());                 //会议开始时间
                c.setEndtime(ap.getEnd());                     //会议结束时间
                Timestamp time = new Timestamp(System.currentTimeMillis());//获取当前时间作为入库时间
                c.setCreatetime(time);                                     //数据条创建时间
                list.add(c);
            }
        }

        if(CollectionUtil.isNotEmpty(list)){                 //判断list是否为空
            getMailMapper.transferFromCalendar(list);
        }

    }
    @Override
    //以会议数据生成日程表
    public void dailyPlanGetFromConference(){
        List<String> title = getMailMapper.selectTitleFromPlanData("yourself");//获取日程表的主题
        TextSimilarity similarity = new CosineSimilarity();                             //新建中文相似度判定工具对象
        List<ConferenceData> data =getMailMapper.selectConferenceData();                //获取会议数据表的信息
        List<PlanData> list=new ArrayList<>();                                          //新建PlanData（日程）的list对象
        PlanData planData = new PlanData();                                             //新建PlanData（日程）单体对象
        for(int i = 0; i < data.size(); i++) {
            String title1 = data.get(i).getTitle();                                    //会议数据表（conference_data)中的主题
            for(int j=0;j<title.size();j++) {
                String title2 = title.get(j);                                          //日程表（plan_data中的主题）
                double score1pkx = similarity.getSimilarity(title1, title2);           //判断主题相似度
                //System.out.println(title1 + " 和 " + title2 + " 的相似度分值：" + score1pkx);
                if (score1pkx<=0.8) {
                   // System.out.println("符合插入条件或已插入");
                    //System.out.println(data.get(i).getTitle());
                    planData.setTitle(data.get(i).getTitle());                        //插入主题
                    planData.setContent(data.get(i).getContent());                    //插入会议内容
                    planData.setPosition(data.get(i).getPosition());                  //插入会议位置
                    planData.setStarttime(data.get(i).getStarttime());                //插入会议开始时间
                    planData.setEndtime(data.get(i).getEndtime());                    //插入会议结束时间
                    Timestamp time = new Timestamp(System.currentTimeMillis());       //获取当前时间
                    planData.setUsername("yourself");                                 //插入用户名
                    planData.setPlantime(data.get(i).getStarttime());                 //插入待办时间
                    planData.setCreatetime(time);                                     //插入创建时间
                    planData.setUpdatetime(time);                                     //插入初始更新时间=创建时间
                    planData.setFlag("1");                                            //插入日程状态  1-正常，0-禁用 -1,已删除
                    planData.setSource("1");                                          //插入数据来源  0：手动添加 1:邮件同步
                    list.add(planData);
                }
            }
        }
        if(CollectionUtil.isNotEmpty(list)) {
            getMailMapper.dailyPlanGetFromConference(list);       //将list插入到日程白表中
            list.clear();                                         //清空list列表
        }
    }

    @Override
    //获取时间段（当前周）日程，分页展示
    public List<PlanData> selectByTimeRange(String username, int pageIndex) {
        Date now=DateUtil.date();                 //获取当前时间
        Date d=DateUtil.offsetWeek(now,pageIndex);//当前时间对应的当前周（pageIndex=0）及偏移（pageIndex=-1：上一周 / pageIndex=1：下一周）
        Date startTime=DateUtil.beginOfWeek(d);//所在周的开始时间
        Date endTime=DateUtil.endOfWeek(d,true);//所在周的结束时间，以星期天作为一周的最后一天
        return getMailMapper.selectByTimeRange(username,startTime,endTime); //从日程表查询时间段内的日程数据
    }
}
