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
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;

@Component  //配合Scheduled定时器使用
@Service(value = "GetMailService")
public class GetMailServiceImpl implements GetMailService {
    EmailData emailData;
    HtmlUtil a;
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
        Date currYearFirst = calendar.getTime();
        for (int i = 0; i < items.size(); i++) {
            EmailMessage message = EmailMessage.bind(service, items.get(i).getId());
            message.load();
            //将同步邮箱数据插入mysql数据库
            if (latestTime == null) {
                latestTime = currYearFirst;
                Date compareTime = items.get(i).getDateTimeReceived();
                if (latestTime.compareTo(compareTime) >= 0)
                    continue;
                //System.out.println(items.get(i).getSubject());
                EmailData h = new EmailData();
                h.setEmail_ref_id(items.get(i).getId().toString()); //message ID
                h.setSender(message.getSender().toString()); //发件人
                h.setTitle(items.get(i).getSubject()); //主题
                String html_body = message.getBody().toString();
                String body = a.getContentFromHtml(html_body);
                h.setContent(body); //邮件内容
                h.setReceivetime(items.get(i).getDateTimeReceived()); //收件时间
                h.setOwner("mine");
                Timestamp time = new Timestamp(System.currentTimeMillis());
                h.setCreatetime(time);
                h.setUpdatetime(time);
                list.add(h);
            } else if (latestTime != null) {
                Date compareTime = items.get(i).getDateTimeReceived();
                if (latestTime.compareTo(compareTime) >= 0)
                    continue;
                System.out.println(items.get(i).getSubject());
                EmailData h = new EmailData();
                h.setEmail_ref_id(items.get(i).getId().toString()); //message ID
                h.setSender(message.getSender().toString()); //发件人
                h.setTitle(items.get(i).getSubject()); //主题
                String html_body = message.getBody().toString();
                String body = a.getContentFromHtml(html_body);
                h.setContent(body); //邮件内容
                h.setReceivetime(items.get(i).getDateTimeReceived()); //收件时间
                h.setOwner("mine");
                Timestamp time = new Timestamp(System.currentTimeMillis());
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
        ExchangeService service = new ExchangeService(ExchangeVersion.Exchange2010_SP1);
        ExchangeCredentials credentials = new WebCredentials("xgwfat@outlook.com", "giyoyo9420", "outlook.com");
        service.setCredentials(credentials);
        service.setUrl(new URI("https://outlook.Office365.com/EWS/Exchange.asmx"));
        service.setCredentials(credentials);
        service.setTraceEnabled(true);
        // Bind to the Calendar.
        Folder inbox = Folder.bind(service, WellKnownFolderName.Calendar);
        System.out.println(inbox.getDisplayName());
        //Calendar start = Calendar.getInstance();
        //start.set(2020,10,19);
        Calendar end = Calendar.getInstance();
        end.set(2020,10,30);
        Date start = new Date();
        //Date end = new Date(start.getTime() + 1000*3600*24);
        CalendarView cView = new CalendarView(start,end.getTime());
        //指定要查看的邮箱
        FolderId folderId = new FolderId(WellKnownFolderName.Calendar, new Mailbox("xgwfat@outlook.com"));
        CalendarFolder alendar = CalendarFolder.bind(service, folderId);
        FindItemsResults<Appointment> findResults = alendar.findAppointments(cView);
        List<ConferenceData> list =new ArrayList<>();
        List<EmailFilter> listTitle=getMailMapper.selectFilterKeyFromFilter("title");
        try {
            findResults = service.findAppointments(folderId, cView);
        } catch (Exception e) {
            e.printStackTrace();
        }
        ArrayList<Appointment> appointmentItems = findResults==null?null:findResults.getItems();
        for(Appointment ap:appointmentItems) {
            ap.load();
            String subject = ap.getSubject();
            boolean status = StrUtil.containsAny(subject, listTitle.get(0).getFilter_key());
            //如邮箱主题包含过滤关键词的某一个，则过滤该会议
            if (status) {
                continue;
            } else {
                ConferenceData c = new ConferenceData();
                c.setCalendar_id(ap.getId().toString());//message ID
                c.setSender(ap.getOrganizer().toString()); //会议组织者
                c.setReceivetime(ap.getDateTimeReceived()); //接收时间
                c.setTitle(ap.getSubject());//会议主题
                String html_body = ap.getBody().toString();
                String body = a.getContentFromHtml(html_body);
                c.setContent(body);//会议内容
                c.setPosition(ap.getLocation());//会议位置
                c.setStarttime(ap.getStart());//会议开始时间
                c.setEndtime(ap.getEnd());//会议结束时间
                Timestamp time = new Timestamp(System.currentTimeMillis());
                c.setCreatetime(time);//数据条创建时间
                list.add(c);
            }
        }

        if(CollectionUtil.isNotEmpty(list)){
            getMailMapper.transferFromCalendar(list);
        }

    }
    @Override
    //以会议数据生成日程表
    public void dailyPlanGetFromConference(){
        List<ConferenceData> data =getMailMapper.selectConferenceData();
        List<PlanData> list=new ArrayList<>();
        PlanData planData = new PlanData();
        for (int i = 0; i < data.size(); i++) {
            System.out.println(data.get(i).getTitle());
            planData.setTitle(data.get(i).getTitle());//插入主题
            planData.setContent(data.get(i).getContent()); //插入会议内容
            planData.setPosition(data.get(i).getPosition()); //插入会议位置
            planData.setStarttime(data.get(i).getStarttime());//插入会议开始时间
            planData.setEndtime(data.get(i).getEndtime());    //插入会议结束时间
            Timestamp time = new Timestamp(System.currentTimeMillis()); //获取当前时间
            planData.setUsername("yourself");                  //插入用户名
            planData.setPlantime(data.get(i).getStarttime());   //插入待办时间
            planData.setCreatetime(time);                  //插入创建时间
            planData.setUpdatetime(time);                  //插入初始更新时间=创建时间
            planData.setFlag("1");                         //插入日程状态  1-正常，0-禁用 -1,已删除
            planData.setSource("1");                       //插入数据来源  0：手动添加 1:邮件同步
            list.add(planData);
            getMailMapper.dailyPlanGetFromConference(list);
            list.clear();
        }
    }

    @Override
    //获取当前周日程，分页展示
    public List<PlanData> selectByTimeRange(String username, int pageIndex) {
        Date now=DateUtil.date();
        Date d=DateUtil.offsetWeek(now,pageIndex);
        Date startTime=DateUtil.beginOfWeek(d);
        Date endTime=DateUtil.endOfWeek(d,true);
        return getMailMapper.selectByTimeRange(username,startTime,endTime);
    }
}
