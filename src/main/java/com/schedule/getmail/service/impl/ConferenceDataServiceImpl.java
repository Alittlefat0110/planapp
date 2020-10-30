package com.schedule.getmail.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;


import cn.hutool.core.util.StrUtil;
import com.schedule.getmail.entity.*;
import com.schedule.getmail.mapper.ConferenceDataMapper;
import com.schedule.getmail.mapper.EmailConfigMapper;
import com.schedule.getmail.service.IConferenceDataService;
import com.schedule.getmail.util.*;
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
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.net.URI;
import java.sql.Timestamp;
import java.sql.Wrapper;
import java.util.ArrayList;
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

    @Resource
    private ConferenceDataMapper conferenceDataMapper;
    @Resource
    private EmailConfigMapper emailConfigMapper;


    /**
     * 同步邮件数据
     * @throws Exception
     */
    @Override
    public  void transferEmail()  throws Exception {
        //设置TLS版本
        System.setProperty("https.protocols", "TLSv1,TLSv1.1,TLSv1.2,SSLv3");
        ExchangeService service = new ExchangeService(ExchangeVersion.Exchange2010_SP1);
        List<EmailConfig> configList = emailConfigMapper.selectList(new QueryWrapper<EmailConfig>());
        //获取当前时间
        Timestamp time = new Timestamp(System.currentTimeMillis());
        for (int i = 0; i < configList.size(); i++) {
            EmailConfig emailConfig = configList.get(i);
            ExchangeCredentials credentials = new WebCredentials(emailConfig.getEmail(), emailConfig.getPassword(), "outlook.com");
            service.setCredentials(credentials);
            service.setUrl(new URI("https://s.outlook.com/EWS/Exchange.asmx"));
            Folder inbox = Folder.bind(service, WellKnownFolderName.Inbox);
            System.out.println(inbox.getDisplayName());
            ItemView itemView = new ItemView(10000);
            //按时间获取
            itemView.getOrderBy().add(ItemSchema.DateTimeReceived, SortDirection.Descending);
            FindItemsResults<Item> findResults = service.findItems(inbox.getId(), itemView);
            ArrayList<Item> items = findResults.getItems();
            if(CheckUtil.isEmpty(emailConfig.getStartTime())){
                emailConfig.setStartTime(new Timestamp(DateUtil.getFirstDay().getTime()));
            }
            //查询过滤关键词/邮箱
            String keyWords =SplitUtil.splitString(emailConfig.getKeyWordS(),emailConfig.getKeyWordT());
            String[] keyWordAll= TokenUtil.tokenString(keyWords);
            String[] keyEmails= TokenUtil.tokenString(emailConfig.getKeyEmail());
            for (int j = 0; j < items.size(); j++) {
                Item item = items.get(j);
                EmailMessage message = EmailMessage.bind(service, item.getId());
                message.load();
                //获取邮件主题
                String subject= item.getSubject();
                //获取发件人邮箱
                String sender = message.getSender().toString();
                //todo 过滤 关键字，发件人
                //判断邮件主题是否含有过滤关键词
                boolean statusTitle = StrUtil.containsAny(subject, keyWordAll);
                //判断是否是黑名单邮箱
                boolean statusSender=StrUtil.equalsAny(sender,keyEmails);
                if (statusTitle||statusSender) {
                    continue;
                }
                //若邮件为会议类型则跳过
                if(item.getXmlElementName().equals("MeetingRequest")){
                    continue;
                }
                Date compareTime = item.getDateTimeReceived();
                if(new Date(emailConfig.getStartTime().getTime()).compareTo(compareTime) >=0){
                    continue;
                }
                ConferenceData cd =  conferenceDataMapper.selectOne(new QueryWrapper<ConferenceData>().lambda()
                        .eq(!StringUtils.isEmpty(item.getId().toString()), ConferenceData::getCalendarId,item.getId().toString()));
                if(!CheckUtil.isEmpty(cd)){
                    continue;
                }
                message.getReceivedBy();
                ConferenceData conferenceData = new ConferenceData();
                //1.插入参考ID
                conferenceData.setCalendarId(item.getId().toString());
                //2.插入发件人
                conferenceData.setSender(message.getSender().toString());
                //3.插入收件人
                conferenceData.setReceiver(message.getReceivedBy().toString());
                //3.插入收件时间
                conferenceData.setReceiveTime(item.getDateTimeReceived());
                //4.插入主题
                conferenceData.setTitle(item.getSubject());
                //获取html文档中body的文本内容（邮件内容）
                String html_body = message.getBody().toString();
                String body = HtmlUtil.getContentFromHtml(html_body);
                //5.插入邮件内容
                conferenceData.setContent(body);
                //6.插入创建时间
                conferenceData.setCreateTime(time);
                //7.插入邮件类型
                conferenceData.setType(item.getXmlElementName());
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
        List<EmailConfig> configList = emailConfigMapper.selectList(new QueryWrapper<EmailConfig>());
        //获取当前时间
        Timestamp time = new Timestamp(System.currentTimeMillis());
        Date now = new Date();
        for (int i = 0; i < configList.size(); i++) {
            EmailConfig emailConfig = configList.get(i);
            ExchangeCredentials credentials = new WebCredentials(emailConfig.getEmail(), emailConfig.getPassword(), "outlook.com");
            service.setCredentials(credentials);
            service.setUrl(new URI("https://outlook.Office365.com/EWS/Exchange.asmx"));
            service.setCredentials(credentials);
            service.setTraceEnabled(true);
            //指定获取类型为Calendar
            Folder inbox = Folder.bind(service, WellKnownFolderName.Calendar); //确定拉取数据来源（类型）
            System.out.println(inbox.getDisplayName());
            //设置截止时间
            Date end = DateUtils.addDays(now, +30);
            //指定获取时间段
            if(CheckUtil.isEmpty(emailConfig.getStartTime())){
                emailConfig.setStartTime(new Timestamp(DateUtil.getFirstDay().getTime()));
            }
            CalendarView cView = new CalendarView(new Date(emailConfig.getStartTime().getTime()),end);
            //指定要查看的邮箱
            FolderId folderId = new FolderId(WellKnownFolderName.Calendar, new Mailbox(emailConfig.getEmail()));
            CalendarFolder calendar = CalendarFolder.bind(service, folderId);
            FindItemsResults<Appointment> findResults = calendar.findAppointments(cView);
            try {
                findResults = service.findAppointments(folderId, cView);
            } catch (Exception e) {
                e.printStackTrace();
            }
            //查询过滤关键词/邮箱 todo
            String keyWords =SplitUtil.splitString(emailConfig.getKeyWordS(),emailConfig.getKeyWordT());
            String[] keyWordAll= TokenUtil.tokenString(keyWords);
            String[] keyEmails= TokenUtil.tokenString(emailConfig.getKeyEmail());

            ArrayList<Appointment> appointmentItems = findResults==null?null:findResults.getItems();
            for(Appointment ap:appointmentItems) {
                ap.load();
                //若库中有已存在的C
                ConferenceData cd =  conferenceDataMapper.selectOne(new QueryWrapper<ConferenceData>().lambda()
                        .eq(!StringUtils.isEmpty(ap.getId().toString()), ConferenceData::getCalendarId,ap.getId().toString()));
                if(!CheckUtil.isEmpty(cd)){
                    continue;
                }
                //获取会议主题
                String subject = ap.getSubject();
                //获取会议组织者
                String sender =ap.getOrganizer().toString();
                //todo 过滤关键字 发件人
                //判断是否含有过滤关键字 todo
                boolean statusTitle = StrUtil.containsAny(subject, keyWordAll);
                //判断是否为黑名单邮箱 todo
                boolean statusSender=StrUtil.equalsAny(sender,keyEmails);
                //若邮箱主题包含过滤关键词或发件人在黑名单中，则过滤该会议
                if (statusTitle||statusSender) {
                    continue;
                } else {
                    ConferenceData conferenceData = new ConferenceData();
                    //插入参考ID
                    conferenceData.setCalendarId(ap.getId().toString());
                    //插入会议组织者（发件人）
                    conferenceData.setSender(ap.getOrganizer().toString());
                    //参加会议的员工
                    List<Attendee> RequiredAttendees = ap.getRequiredAttendees().getItems();
                    List<Attendee> OptionalAttendees = ap.getOptionalAttendees().getItems();
                    String receiver=SplitUtil.splitUtil(RequiredAttendees,OptionalAttendees);
                    //插入会议员工（参与者）
                    conferenceData.setReceiver(receiver);
                    //插入接收时间
                    conferenceData.setReceiveTime(ap.getDateTimeReceived());
                    //插入会议主题
                    conferenceData.setTitle(ap.getSubject());
                    //去除html框架，获取body中的文本内容
                    String html_body = ap.getBody().toString();
                    String body = HtmlUtil.getContentFromHtml(html_body);
                    //插入会议内容
                    conferenceData.setContent(body);
                    //插入会议位置
                    conferenceData.setPosition(ap.getLocation());
                    //插入会议开始时间
                    conferenceData.setStartTime(ap.getStart());
                    //插入会议结束时间
                    conferenceData.setEndTime(ap.getEnd());
                    //插入创建时间
                    conferenceData.setCreateTime(time);
                    //插入类型
                    conferenceData.setType(ap.getXmlElementName());
                    conferenceDataMapper.insert(conferenceData);
                }
            }
        }
    }
}
