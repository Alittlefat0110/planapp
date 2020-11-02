package com.schedule.getmail.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;


import cn.hutool.core.util.StrUtil;
import com.schedule.getmail.contentSimilarity.similarity.text.CosineSimilarity;
import com.schedule.getmail.contentSimilarity.similarity.text.TextSimilarity;
import com.schedule.getmail.contentSimilarity.tokenizer.Tokenizer;
import com.schedule.getmail.contentSimilarity.tokenizer.Word;
import com.schedule.getmail.entity.*;
import com.schedule.getmail.mapper.ConferenceDataMapper;
import com.schedule.getmail.mapper.EmailConfigMapper;
import com.schedule.getmail.mapper.PlanDataMapper;
import com.schedule.getmail.mapper.TitleFrequencyMapper;
import com.schedule.getmail.service.IConferenceDataService;
import com.schedule.getmail.util.*;
import lombok.extern.slf4j.Slf4j;
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
import org.springframework.beans.BeanUtils;
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
@Slf4j(topic = "ConferenceDataLogger")
public class ConferenceDataServiceImpl extends ServiceImpl<ConferenceDataMapper, ConferenceData> implements IConferenceDataService {

    @Resource
    private ConferenceDataMapper conferenceDataMapper;
    @Resource
    private EmailConfigMapper emailConfigMapper;
    @Resource
    private PlanDataMapper planDataMapper;
    @Resource
    private TitleFrequencyMapper titleFrequencyMapper;


    /**
     * 同步邮件数据
     * @throws Exception
     */
    @Override
    public  void transferEmail()  throws Exception {
        //设置TLS版本
        System.setProperty("https.protocols", "TLSv1,TLSv1.1,TLSv1.2,SSLv3");
        //设置exchange邮件管理系统版本
        ExchangeService service = new ExchangeService(ExchangeVersion.Exchange2010_SP1);
        //查询邮箱配置信息
        List<EmailConfig> configList = emailConfigMapper.selectList(new QueryWrapper<EmailConfig>());
        log.info("emailConfigMapper.selectList {} ",configList);
        //获取当前时间
        Timestamp time = new Timestamp(System.currentTimeMillis());
        for (int i = 0; i < configList.size(); i++) {
            EmailConfig emailConfig = configList.get(i);
            log.info("configList.get(i) {} ",emailConfig);
            //查询当前用户名对应的日程主题
            List<PlanData> planDataList = planDataMapper.selectList(new QueryWrapper<PlanData>()
            .eq("user_name" , emailConfig.getUserName())
            );
            log.info("planDataMapper.selectList {} ",planDataList);
            double score1pkx=0;
            //创建相似度分析方法对象
            TextSimilarity similarity =new CosineSimilarity();
            ExchangeCredentials credentials = new WebCredentials(emailConfig.getEmail(), emailConfig.getPassword(), "outlook.com");
            log.info("WebCredentials {} ",credentials);
            service.setCredentials(credentials);
            service.setUrl(new URI("https://s.outlook.com/EWS/Exchange.asmx"));
            Folder inbox = Folder.bind(service, WellKnownFolderName.Inbox);
            //邮件所处文件夹（客户端）
            String EmailBox=inbox.getDisplayName();
            System.out.println(EmailBox+"ok");
            log.info("inbox.getDisplayName {} ",EmailBox);
            //邮件遍历数目
            ItemView itemView = new ItemView(100);
            //按接收时间从晚到早遍历邮件
            itemView.getOrderBy().add(ItemSchema.DateTimeReceived, SortDirection.Descending);
            FindItemsResults<Item> findResults = service.findItems(inbox.getId(), itemView);
            log.info("service.findItems {} ",findResults);
            ArrayList<Item> items = findResults.getItems();
            log.info("findResults.getItems {} ",items);
            //若没有设置开始时间在，则以当年第一天为开始时间
            if(CheckUtil.isEmpty(emailConfig.getStartTime())){
                emailConfig.setStartTime(new Timestamp(DateUtil.getFirstDay().getTime()));
                log.info("emailConfig.getStartTime {} ",DateUtil.getFirstDay().getTime());
            }
            //查询过滤关键词（角色标签/通用标签）
            String keyWords =SplitUtil.splitString(emailConfig.getKeyWordS(),emailConfig.getKeyWordT());
            //合并两类关键词
            String[] keyWordAll= TokenUtil.tokenString(keyWords);
            //查询过滤邮箱
            String[] keyEmails= TokenUtil.tokenString(emailConfig.getKeyEmail());
            log.info("SplitUtil.splitString {},TokenUtil.tokenString {} {}",keyWords,keyWordAll,keyEmails);
            for (int j = 0; j < items.size(); j++) {
                Item item = items.get(j);
                System.out.println("邮件主题"+item.getSubject());
                EmailMessage message = EmailMessage.bind(service, item.getId());
                log.info("EmailMessage.bind {} ",message);
                message.load();
                //获取邮件主题
                String subject= item.getSubject();
                //获取发件人邮箱
                String sender = message.getSender().toString();
                log.info("item.getSubject {} message.getSender {}",subject,sender);
                //判断邮件主题是否含有过滤关键词
                boolean statusTitle = StrUtil.containsAny(subject, keyWordAll);
                log.info("StrUtil.containsAny {} ",statusTitle);
                //判断是否是黑名单邮箱
                boolean statusSender=StrUtil.equalsAny(sender,keyEmails);
                log.info("StrUtil.equalsAny {} ",statusSender);
                //若主题中含有过滤关键词，或者发件人为黑名单邮箱则跳过
                if (statusTitle||statusSender) {
                    continue;
                }
                //若邮件为会议类型则跳过
                if(item.getXmlElementName().equals("MeetingRequest")){
                    continue;
                }
                //获取邮件接收时间
                Date compareTime = item.getDateTimeReceived();
                log.info("item.getDateTimeReceived {} ",compareTime);
                //若设置的时间大于（晚于）该邮件接收时间则跳过
                if(new Date(emailConfig.getStartTime().getTime()).compareTo(compareTime) >=0){
                    continue;
                }
                ConferenceData cd =  conferenceDataMapper.selectOne(new QueryWrapper<ConferenceData>().lambda()
                        .eq(!StringUtils.isEmpty(item.getId().toString()), ConferenceData::getCalendarId,item.getId().toString())
                );
                log.info("conferenceDataMapper.selectOne {} ",cd);
                if(!CheckUtil.isEmpty(cd)){
                    continue;
                }
                int flag  = 0;
                ConferenceData conferenceData = new ConferenceData();
                //1.插入参考ID
                conferenceData.setCalendarId(item.getId().toString());
                //2.插入发件人
                conferenceData.setSender(sender);
                //3.插入收件人
                conferenceData.setReceiver(emailConfig.getEmail());
                //4.插入收件时间
                conferenceData.setReceiveTime(item.getDateTimeReceived());
                //5.插入主题
                conferenceData.setTitle(subject);
                //获取html文档中body的文本内容（邮件内容）
                String html_body = message.getBody().toString();
                String body = HtmlUtil.getContentFromHtml(html_body);
                log.info("HtmlUtil.getContentFromHtml {} ",body);
                //6.插入邮件内容
                conferenceData.setContent(body);
                //插入开始时间（暂以收件时间代替）
                conferenceData.setStartTime(item.getDateTimeReceived());
                //7.插入创建时间
                conferenceData.setCreateTime(time);
                //8.插入邮件类型
                conferenceData.setType(item.getXmlElementName());
                //9.插入用户名
                conferenceData.setUserName(emailConfig.getUserName());
                flag = conferenceDataMapper.insert(conferenceData);
                log.info("conferenceDataMapper.insert {} ",flag);

                //主题相似度排查
                for(int p=0;p<planDataList.size();p++) {
                    System.out.println("对比主题列表长度为"+planDataList.size());
                     score1pkx = similarity.getSimilarity(subject, planDataList.get(p).getTitle());
                     System.out.println("《"+subject+"--和--"+planDataList.get(p).getTitle()+"》的相似度为："+score1pkx);
                    log.info("similarity.getSimilarity {} ",score1pkx);
                }
                if (score1pkx>0.8){
                    System.out.println(subject+"-->相似度高于80%不予入库");
                    continue;
                }
                PlanData planData = new PlanData();
                BeanUtils.copyProperties(conferenceData,planData);
                planData.setSource("1");
                flag = planDataMapper.insert(planData);
                log.info("planDataMapper.insert {} ",flag);
                planDataList.add(planData);
                log.info("planDataList.add {}",planDataList);
                //1.根据title分词得到分词list
                List<Word> seg = Tokenizer.segment(conferenceData.getTitle());
                log.info("Tokenizer.segment {} ",seg);
                //2.根据分好的词查库
                for (Word w: seg) {
                    if("n".equals(w.getPos())){
                        TitleFrequency titleFrequency = titleFrequencyMapper.selectOne(new QueryWrapper<TitleFrequency>().lambda()
                                .eq(!StringUtils.isEmpty(w.getName()), TitleFrequency::getWords, w.getName())
                        );
                        log.info("titleFrequencyMapper.selectOne {} ",titleFrequency);
                        if(CheckUtil.isEmpty(titleFrequency)){
                            TitleFrequency t = new TitleFrequency();
                            t.setWords(w.getName());
                            t.setFrequency(1);
                            flag = titleFrequencyMapper.insert(t);
                            log.info("titleFrequencyMapper.insert {} ",flag);
                        }else {
                            titleFrequency.setFrequency(titleFrequency.getFrequency()+1);
                            flag = titleFrequencyMapper.updateById(titleFrequency);
                            log.info("titleFrequencyMapper.updateById {} ",flag);
                        }
                    }
                }
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
        //查询邮箱配置信息
        List<EmailConfig> configList = emailConfigMapper.selectList(new QueryWrapper<EmailConfig>());
        log.info("emailConfigMapper.selectList {} ",configList);
        //获取当前时间
        Timestamp time = new Timestamp(System.currentTimeMillis());
        Date now = new Date();
        for (int i = 0; i < configList.size(); i++) {
            EmailConfig emailConfig = configList.get(i);
            log.info("emailConfigMapper.selectList {} ",configList);
            //查询当前用户名对应的日程主题
            List<PlanData> planDataList = planDataMapper.selectList(new QueryWrapper<PlanData>()
                    .eq("user_name" , emailConfig.getUserName())
            );
            log.info("planDataMapper.selectList {} ",planDataList);
            double score1pkx=0;
            //创建相似度分析方法对象
            TextSimilarity similarity =new CosineSimilarity();
            //绑定邮箱
            ExchangeCredentials credentials = new WebCredentials(emailConfig.getEmail(), emailConfig.getPassword(), "outlook.com");
            log.info("WebCredentials {} ",credentials);
            service.setCredentials(credentials);
            service.setUrl(new URI("https://outlook.Office365.com/EWS/Exchange.asmx"));
            service.setCredentials(credentials);
            service.setTraceEnabled(true);
            //指定获取类型为Calendar
            Folder inbox = Folder.bind(service, WellKnownFolderName.Calendar);
            //所在文件夹（客户端）
            String EmailBox = inbox.getDisplayName();
            System.out.println(inbox.getDisplayName());
            log.info("inbox.getDisplayName {} ",EmailBox);
            //设置截止时间
            Date end = DateUtils.addDays(now, +30);
            //若指定时间段为空则令起始获取时间为当年1月1日
            if(CheckUtil.isEmpty(emailConfig.getStartTime())){
                emailConfig.setStartTime(new Timestamp(DateUtil.getFirstDay().getTime()));
                log.info("emailConfig.getStartTime() {} ",DateUtil.getFirstDay().getTime());
            }
            CalendarView cView = new CalendarView(new Date(emailConfig.getStartTime().getTime()),end);
            //指定要查看的邮箱
            FolderId folderId = new FolderId(WellKnownFolderName.Calendar, new Mailbox(emailConfig.getEmail()));
            CalendarFolder calendar = CalendarFolder.bind(service, folderId);
            FindItemsResults<Appointment> findResults = calendar.findAppointments(cView);
            log.info("calendar.findAppointments {} ",findResults);
            try {
                findResults = service.findAppointments(folderId, cView);
            } catch (Exception e) {
                e.printStackTrace();
            }
            //查询过滤关键词（角色标签/通用标签）
            String keyWords =SplitUtil.splitString(emailConfig.getKeyWordS(),emailConfig.getKeyWordT());
            //合并两类关键词
            String[] keyWordAll= TokenUtil.tokenString(keyWords);
            //查询过滤邮箱
            String[] keyEmails= TokenUtil.tokenString(emailConfig.getKeyEmail());
            log.info("SplitUtil.splitString {},TokenUtil.tokenString {} {}",keyWords,keyWordAll,keyEmails);
            ArrayList<Appointment> appointmentItems = findResults==null?null:findResults.getItems();
            for(Appointment ap:appointmentItems) {
                ap.load();
                //若库中有已存在该会议的参考ID则跳过
                ConferenceData cd =  conferenceDataMapper.selectOne(new QueryWrapper<ConferenceData>().lambda()
                        .eq(!StringUtils.isEmpty(ap.getId().toString()), ConferenceData::getCalendarId,ap.getId().toString()));
                log.info("conferenceDataMapper.selectOne {} ",cd);
                if(!CheckUtil.isEmpty(cd)){
                    continue;
                }
                //获取会议主题
                String subject = ap.getSubject();
                //获取会议组织者
                String sender =ap.getOrganizer().toString();
                log.info("ap.getSubject {} ap.getOrganizer {}",subject,sender);
                //todo 过滤关键字 发件人
                //判断是否含有过滤关键字 todo
                boolean statusTitle = StrUtil.containsAny(subject, keyWordAll);
                log.info("StrUtil.containsAny {} ",statusTitle);
                //判断是否为黑名单邮箱 todo
                boolean statusSender=StrUtil.equalsAny(sender,keyEmails);
                log.info("StrUtil.equalsAny {} ",statusSender);
                //若邮箱主题包含过滤关键词或发件人在黑名单中，则过滤该会议
                if (statusTitle||statusSender) {
                    continue;
                } else {
                    int flag = 0;
                    ConferenceData conferenceData = new ConferenceData();
                    //插入参考ID
                    conferenceData.setCalendarId(ap.getId().toString());
                    //插入会议组织者（发件人）
                    conferenceData.setSender(ap.getOrganizer().toString());
                    //参加会议的员工
                    List<Attendee> RequiredAttendees = ap.getRequiredAttendees().getItems();
                    List<Attendee> OptionalAttendees = ap.getOptionalAttendees().getItems();
                    String receiver=SplitUtil.splitUtil(RequiredAttendees,OptionalAttendees);
                    log.info("SplitUtil.splitUtil {} ",receiver);
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
                    conferenceData.setUserName(emailConfig.getUserName());
                    flag = conferenceDataMapper.insert(conferenceData);
                    log.info("conferenceDataMapper.insert {} ",flag);
                    //主题相似度排查
                    for(int p=0;p<planDataList.size();p++) {
                        System.out.println("对比主题列表长度为："+planDataList.size());
                        score1pkx = similarity.getSimilarity(subject, planDataList.get(p).getTitle());
                        System.out.println("《"+subject+"--和--"+planDataList.get(p).getTitle()+"》的相似度为："+score1pkx);
                        log.info("similarity.getSimilarity {} ",score1pkx);
                    }
                    if (score1pkx>0.8){
                        System.out.println(subject+"-->相似度高于80%不予入库");
                        continue;
                    }
                    PlanData planData = new PlanData();
                    BeanUtils.copyProperties(conferenceData,planData);
                    planData.setSource("2");
                    flag = planDataMapper.insert(planData);
                    log.info(" planDataMapper.insert {} ",flag);
                    planDataList.add(planData);
                    //1.根据title分词得到分词list
                    List<Word> seg = Tokenizer.segment(conferenceData.getTitle());
                    log.info(" Tokenizer.segment {} ",seg);
                    //2.根据分好的词查库
                    for (Word w: seg) {
                        if ("n".equals(w.getPos())) {
                            TitleFrequency titleFrequency = titleFrequencyMapper.selectOne(new QueryWrapper<TitleFrequency>().lambda()
                                    .eq(!StringUtils.isEmpty(w.getName()), TitleFrequency::getWords, w.getName())
                            );
                            log.info(" titleFrequencyMapper.selectOne {} ",titleFrequency);
                            //热词入库
                            if (CheckUtil.isEmpty(titleFrequency)) {
                                TitleFrequency t = new TitleFrequency();
                                t.setWords(w.getName());
                                t.setFrequency(1);
                                flag = titleFrequencyMapper.insert(t);
                                log.info(" titleFrequencyMapper.insert {} ",flag);
                            } else {
                                titleFrequency.setFrequency(titleFrequency.getFrequency() + 1);
                                flag = titleFrequencyMapper.updateById(titleFrequency);
                                log.info(" titleFrequencyMapper.updateById {} ",flag);
                            }
                        }
                    }
                }
            }
        }
    }
}
