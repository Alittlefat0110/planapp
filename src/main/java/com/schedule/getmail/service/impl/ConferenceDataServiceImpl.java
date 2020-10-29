package com.schedule.getmail.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;


import cn.hutool.core.util.StrUtil;
import com.schedule.getmail.entity.*;
import com.schedule.getmail.mapper.ConferenceDataMapper;
import com.schedule.getmail.mapper.GetMailMapper;
import com.schedule.getmail.service.IConferenceDataService;
import com.schedule.getmail.util.HtmlUtil;
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
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.net.URI;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 普通邮件/会议数据表 服务实现类
 * </p>
 *
 * @author StrTom
 * @since 2020-10-28
 */
@Service
public class ConferenceDataServiceImpl extends ServiceImpl<ConferenceDataMapper, ConferenceData> implements IConferenceDataService {
    //创建提取html的body内容的工具对象
    HtmlUtil htmlUtil;
    @Resource
    //@Autowired
    private GetMailMapper getMailMapper;
    private ConferenceDataMapper conferenceDataMapper;
    @Override
    //同步邮箱数据
    public  void transferEmail()  throws Exception {
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
        List<ConferenceData> list = new ArrayList<>();
        Date latestTime = getMailMapper.latestReceiveTime(); //数据库中拉取最新收件时间
        //获取当年第一天的时间
        Calendar date = Calendar.getInstance();
        int year = date.get(Calendar.YEAR);
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(Calendar.YEAR, year);
        //当年第一天
        Date currYearFirst = calendar.getTime();
        for (int i = 0; i < items.size(); i++) {
            EmailMessage message = EmailMessage.bind(service, items.get(i).getId());
            message.load();

            //若邮件为会议则跳过
            if(items.get(i).getXmlElementName().equals("MeetingMessage")){
                continue;
            }

            //若数据库为空则令最新收件时间为当年1月1日
            if (latestTime == null) {
                latestTime = currYearFirst;
                Date compareTime = items.get(i).getDateTimeReceived();
                //根据接收时间判断当前邮件是否存在于数据库中
                if (latestTime.compareTo(compareTime) >= 0)
                    continue;
                ConferenceData conferenceData = new ConferenceData();
                //1.获取参考ID
                conferenceData.setCalendarId(items.get(i).getId().toString());
                //2.获取发件人
                conferenceData.setSender(message.getSender().toString());
                //3.收件时间
                conferenceData.setReceiveTime(items.get(i).getDateTimeReceived());
                //4.获取主题
                conferenceData.setTitle(items.get(i).getSubject());
                //获取html文档中body的文本内容（邮件内容）
                String html_body = message.getBody().toString();
                String body = htmlUtil.getContentFromHtml(html_body);
                //5.邮件内容
                conferenceData.setContent(body);
                //获取当前时间
                Timestamp time = new Timestamp(System.currentTimeMillis());
                //6.添加创建时间
                conferenceData.setCreateTime(time);
                //7.邮件类型
                conferenceData.setType(items.get(i).getXmlElementName());
                conferenceDataMapper.insert(conferenceData);

            } else if (latestTime != null) {
                Date compareTime = items.get(i).getDateTimeReceived();
                //根据接收时间判断当前邮件是否存在于数据库中
                if (latestTime.compareTo(compareTime) >= 0)
                    continue;
                ConferenceData conferenceData = new ConferenceData();
                //1.获取参考ID
                conferenceData.setCalendarId(items.get(i).getId().toString());
                //2.获取发件人
                conferenceData.setSender(message.getSender().toString());
                //3.收件时间
                conferenceData.setReceiveTime(items.get(i).getDateTimeReceived());
                //4.获取主题
                conferenceData.setTitle(items.get(i).getSubject());
                //获取html文档中body的文本内容（邮件内容）
                String html_body = message.getBody().toString();
                String body = htmlUtil.getContentFromHtml(html_body);
                //5.邮件内容
                conferenceData.setContent(body);
                //获取当前时间
                Timestamp time = new Timestamp(System.currentTimeMillis());
                //6.添加创建时间
                conferenceData.setCreateTime(time);
                //7.邮件类型
                conferenceData.setType(items.get(i).getXmlElementName());
                conferenceDataMapper.insert(conferenceData);
            }
        }
    }

    @Override
    //同步会议（日历）数据
    public void transferConference() throws Exception{
        //设置TLS版本
        System.setProperty("https.protocols", "TLSv1,TLSv1.1,TLSv1.2,SSLv3");
        //邮件管理系统版本
        ExchangeService service = new ExchangeService(ExchangeVersion.Exchange2010_SP1);
        //绑定收件邮箱
        EmailConfig emailConfig =new EmailConfig();

        ExchangeCredentials credentials = new WebCredentials("xgwfat@outlook.com", "giyoyo9420", "outlook.com");
        service.setCredentials(credentials);
        service.setUrl(new URI("https://outlook.Office365.com/EWS/Exchange.asmx"));
        service.setCredentials(credentials);
        service.setTraceEnabled(true);
        //指定获取类型为Calendar
        Folder inbox = Folder.bind(service, WellKnownFolderName.Calendar); //确定拉取数据来源（类型）
        System.out.println(inbox.getDisplayName());
        //获取当前时间
        Date now = new Date();
        //设置开始时间
        Date start = DateUtils.addDays(now,-30);
        //设置截止时间
        Date end = DateUtils.addDays(now, +30);
        //指定获取时间段
        CalendarView cView = new CalendarView(start,end);
        //指定要查看的邮箱
        FolderId folderId = new FolderId(WellKnownFolderName.Calendar, new Mailbox("xgwfat@outlook.com"));
        CalendarFolder calendar = CalendarFolder.bind(service, folderId);
        FindItemsResults<Appointment> findResults = calendar.findAppointments(cView);
//        List<String> listTitle=getMailMapper.selectFilterKeyFromFilter("title");  //获取过滤关键字列表
//        List<String> listSender=getMailMapper.selectFilterKeyFromFilter("sender");//获取过滤邮箱列表
//        String[] s=new String[listSender.size()];
//        for(int i=0;i<listSender.size();i++){
//            s[i]=listSender.get(i);
//            System.out.println(s[i]);
//        }
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
            //是否含有过滤关键字 todo
            boolean statusTitle = StrUtil.containsAny(subject, "");
            //是否是黑名单邮箱（数据表中只存储黑名单）todo
            boolean statusSender=StrUtil.equalsAny(sender,"s");
            //若邮箱主题包含过滤关键词或发件人在黑名单中，则过滤该会议
            if (statusTitle||statusSender) {
                continue;
            } else {
                ConferenceData conferenceData = new ConferenceData();
                //插入参考ID
                conferenceData.setCalendarId(ap.getId().toString());    //message ID
                //插入会议组织者
                conferenceData.setSender(ap.getOrganizer().toString());  //会议组织者
                //插入接收时间
                conferenceData.setReceiveTime(ap.getDateTimeReceived()); //接收时间
                //插入会议主题
                conferenceData.setTitle(ap.getSubject());                //会议主题
                //去除html框架，获取body中的文本内容
                String html_body = ap.getBody().toString();
                String body = htmlUtil.getContentFromHtml(html_body);
                //插入会议内容
                conferenceData.setContent(body);
                //插入会议位置
                conferenceData.setPosition(ap.getLocation());
                //插入会议开始时间
                conferenceData.setStartTime(ap.getStart());
                //插入会议结束时间
                conferenceData.setEndTime(ap.getEnd());
                //获取当前时间
                Timestamp time = new Timestamp(System.currentTimeMillis());
                //插入创建时间
                conferenceData.setCreateTime(time);
                //插入类型
                conferenceData.setType(ap.getXmlElementName());
                conferenceDataMapper.insert(conferenceData);
            }
        }
    }
}
