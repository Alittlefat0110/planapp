package com.example.getmail;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.StrUtil;
import com.example.getmail.contentSimilarity.similarity.text.CosineSimilarity;
import com.example.getmail.contentSimilarity.similarity.text.TextSimilarity;
import com.example.getmail.entity.ConferenceData;
import com.example.getmail.entity.EmailData;
import com.example.getmail.entity.EmailFilter;
import com.example.getmail.entity.PlanData;
import com.example.getmail.mapper.DailyPlanAddBySelfMapper;
import com.example.getmail.mapper.GetMailMapper;
import com.example.getmail.service.DailyPlanConfigBySelfService;
import com.example.getmail.service.GetMailService;
import com.example.getmail.util.HtmlUtil;
import microsoft.exchange.webservices.data.core.EwsServiceXmlReader;
import microsoft.exchange.webservices.data.core.ExchangeService;
import microsoft.exchange.webservices.data.core.PropertySet;
import microsoft.exchange.webservices.data.core.enumeration.availability.AvailabilityData;
import microsoft.exchange.webservices.data.core.enumeration.availability.MeetingAttendeeType;
import microsoft.exchange.webservices.data.core.enumeration.misc.ExchangeVersion;
import microsoft.exchange.webservices.data.core.enumeration.property.WellKnownFolderName;
import microsoft.exchange.webservices.data.core.enumeration.search.SortDirection;
import microsoft.exchange.webservices.data.core.response.AttendeeAvailability;
import microsoft.exchange.webservices.data.core.service.folder.CalendarFolder;
import microsoft.exchange.webservices.data.core.service.folder.Folder;
import microsoft.exchange.webservices.data.core.service.item.Appointment;
import microsoft.exchange.webservices.data.core.service.item.EmailMessage;
import microsoft.exchange.webservices.data.core.service.item.Item;
import microsoft.exchange.webservices.data.core.service.schema.AppointmentSchema;
import microsoft.exchange.webservices.data.core.service.schema.ItemSchema;
import microsoft.exchange.webservices.data.credential.ExchangeCredentials;
import microsoft.exchange.webservices.data.credential.WebCredentials;
import microsoft.exchange.webservices.data.misc.availability.AttendeeInfo;
import microsoft.exchange.webservices.data.misc.availability.GetUserAvailabilityResults;
import microsoft.exchange.webservices.data.misc.availability.TimeWindow;
import microsoft.exchange.webservices.data.property.complex.Attachment;
import microsoft.exchange.webservices.data.property.complex.Attendee;
import microsoft.exchange.webservices.data.property.complex.FolderId;
import microsoft.exchange.webservices.data.property.complex.Mailbox;
import microsoft.exchange.webservices.data.property.complex.availability.CalendarEvent;
import microsoft.exchange.webservices.data.property.complex.availability.CalendarEventDetails;
import microsoft.exchange.webservices.data.search.CalendarView;
import microsoft.exchange.webservices.data.search.FindItemsResults;
import microsoft.exchange.webservices.data.search.ItemView;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.joda.time.DateTime;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.io.Console;
import java.math.BigInteger;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Timestamp;
import java.util.*;

@RestController
@Component
@EnableScheduling
@SpringBootTest
class GetmailApplicationTests {
	EmailData emailData = new EmailData();
	HtmlUtil a;
	@Resource
	private GetMailMapper getMailMapper;
	private GetMailService getMailService;
	private DailyPlanAddBySelfMapper dailyPlanAddBySelfMapper;
	private DailyPlanConfigBySelfService dailyPlanConfigBySelfService;


	@Test
	public void gettime() {
		//long l = System.currentTimeMillis();
		//Date time1=new Date(l);
		//SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		//System.out.println(sdf.format(time1));
		//Timestamp time = new Timestamp(System.currentTimeMillis());
		//System.out.println(time);

		List<EmailData> li = new ArrayList<>();
		for (int i = 0; i < li.size(); i++) {
			EmailData a = li.get(i);
			String X = a.getTitle();
			System.out.println(X);
		}
		//getMailMapper.plandataInsert(list);

		//System.out.println(getMailMapper.maildatacontent("mine"));
	}

	@Test
	//@Scheduled(cron = "0/3 * * * * ?")
	public void makepemail() throws Exception {

		System.setProperty("https.protocols", "TLSv1,TLSv1.1,TLSv1.2,SSLv3"); //设置TLS版本
		//使用exchange服务工具类创建服务
		//ExchangeMailUtil exchangeMailUtil = new ExchangeMailUtil(mailServer, user, password, readUrlPrefix);
		//ExchangeService service = exchangeMailUtil.getExchangeService();
		//创建exchange服务 ExchangeVersion.Exchange2010_SP1    (服务版本号)
		ExchangeService service = new ExchangeService(ExchangeVersion.Exchange2010_SP1);
		ExchangeCredentials credentials = new WebCredentials("xgwfat@outlook.com", "giyoyo9420", "outlook.com");
		service.setCredentials(credentials);
		service.setUrl(new URI("https://outlook.Office365.com/EWS/Exchange.asmx"));
		// Bind to the Inbox.
		Folder inbox = Folder.bind(service, WellKnownFolderName.Inbox);
		System.out.println(inbox.getDisplayName());
		ItemView itemView = new ItemView(10000);
		itemView.getOrderBy().add(ItemSchema.DateTimeReceived, SortDirection.Descending);//按时间获取

		// 查询，插入数据
		FindItemsResults<Item> findResults = service.findItems(inbox.getId(), itemView);
		ArrayList<Item> items = findResults.getItems();
		List<EmailData> list = new ArrayList<>();
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
			Timestamp time = new Timestamp(System.currentTimeMillis());
			h.setCreatetime(time);
			h.setUpdatetime(time);
			list.add(h);
			//getMailMapper.transferfromemail(list);
		}
		getMailMapper.transferFromEmail(list);

	}


	@Test
	public void plantable() {
		//getMailMapper.plantableInsert();
		//System.out.println("插入成功");
		//System.out.println(getMailMapper.latestReceiveTime());


		Timestamp time = new Timestamp(System.currentTimeMillis()); //获取当前时间
		PlanData planData = new PlanData();
		planData.setPlan_id(1);
		planData.setUsername("myself");   //插入用户名
		planData.setTitle("新标题");         //插入主题
		planData.setContent("新主题");     //插入会议内容
		planData.setPosition("203");   //插入会议位置
		planData.setStarttime(time); //插入会议开始时间
		planData.setEndtime(time);     //插入会议结束时间
		planData.setPlantime(time);                    //插入待办时间
		//planData.setCreatetime(time);                  //插入创建时间
		planData.setUpdatetime(time);                  //插入更新时间!=创建时间
		planData.setFlag("1");                         //插入日程状态  1-正常，0-禁用 -1,已删除
		planData.setSource("0");                       //插入数据来源  0：手动添加 1:邮件同步
		dailyPlanAddBySelfMapper.dailyPlanUpdateById(planData);

	}

	@Test
	//@Scheduled(cron = "0/3 * * * * ?")
	//@ResponseBody
	public void contextLoads() {
		//PlanData u = new PlanData();
		//System.out.println(getMailMapper.selectThisWeek("yourself"));
		//Integer a =5;
		//for (int i = 0; i < getMailMapper.selectThisWeek("yourself").size();i++) {
		//	System.out.println(getMailMapper.selectThisWeek("yourself").get(i).getTitle());
		//}

		//dailyPlanConfigBySelfService.dailyPlanDeleteById("yourself",3);
		 //dailyPlanAddBySelfMapper.selectConferenceData();
		List<ConferenceData> data =getMailMapper.selectConferenceData();
		List<PlanData> list =new ArrayList<>();
		PlanData planData = new PlanData();
		for (int i = 0; i < data.size(); i++) {
			System.out.println(data.get(i).getTitle());
			planData.setTitle(data.get(i).getTitle());//插入主题
			planData.setContent(data.get(i).getContent()); //插入会议内容
			planData.setPosition(data.get(i).getPosition()); //插入会议位置
			planData.setStarttime(data.get(i).getStarttime());//插入会议开始时间
			planData.setEndtime(data.get(i).getEndtime());//插入会议结束时间
			Timestamp time = new Timestamp(System.currentTimeMillis()); //获取当前时间
			planData.setUsername("yourself");   //插入用户名
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


	@Test
	public  void getAppoinement() throws Exception{
		System.setProperty("https.protocols", "TLSv1,TLSv1.1,TLSv1.2,SSLv3"); //设置TLS版本
		//使用exchange服务工具类创建服务
		//ExchangeMailUtil exchangeMailUtil = new ExchangeMailUtil(mailServer, user, password, readUrlPrefix);
		//ExchangeService service = exchangeMailUtil.getExchangeService();
		//创建exchange服务 ExchangeVersion.Exchange2010_SP1    (服务版本号)
		ExchangeService service = new ExchangeService(ExchangeVersion.Exchange2010_SP1);
		ExchangeCredentials credentials = new WebCredentials("xgwfat@outlook.com", "giyoyo9420", "outlook.com");
		service.setCredentials(credentials);
		service.setUrl(new URI("https://outlook.Office365.com/EWS/Exchange.asmx"));
		service.setCredentials(credentials);
		service.setTraceEnabled(true);
		// Bind to the Inbox.
		Folder inbox = Folder.bind(service, WellKnownFolderName.Calendar);
		System.out.println(inbox.getDisplayName());
		//Calendar start = Calendar.getInstance();
		//start.set(2020,10,19);
		//Calendar end = Calendar.getInstance();
		//end.set(2020,10,24);
		//Date start = new Date();
		//Date end = new Date(start.getTime() + 1000*3600*24);
		Date now = new Date();//获取当前时间
		 Date start = DateUtils.addDays(now,-30);//设置开始时间为当前时间的前30天
		Date end = DateUtils.addDays(now, +30);      //设置截止时间为当前时间后30天
		CalendarView cView = new CalendarView(start,end);
		//指定要查看的邮箱
		FolderId folderId = new FolderId(WellKnownFolderName.Calendar, new Mailbox("xgwfat@outlook.com"));
		CalendarFolder alendar = CalendarFolder.bind(service, folderId);
		FindItemsResults<Appointment> findResults = alendar.findAppointments(cView);
		System.out.println("状态1");
		try {
			findResults = service.findAppointments(folderId, cView);
		} catch (Exception e) {
			e.printStackTrace();
		}
		ArrayList<Appointment> appointmentItems = findResults==null?null:findResults.getItems();
		System.out.println("状态2");
		List<String> listTitle=getMailMapper.selectFilterKeyFromFilter("title");
		List<String> listSender=getMailMapper.selectFilterKeyFromFilter("sender");
		String[] s=new String[listSender.size()];
		for(int i=0;i<listSender.size();i++){
			s[i]=listSender.get(i);
		}

		for(Appointment ap:appointmentItems){
			ap.load();
			String subject = ap.getSubject();
			String sender =ap.getOrganizer().toString();
			boolean status = StrUtil.containsAny(subject, listTitle.toString().toCharArray());
			boolean statusSender=StrUtil.equalsAny(sender,s);
				//如邮箱主题包含过滤关键词的某一个，则过滤该会议
				if (status||statusSender) {
					System.out.println("会议主题：" + ap.getSubject()+"-->被过滤了");
					System.out.println("会议组织者：" + ap.getOrganizer()+"--->被拒绝了");
					continue;
				} else {
					//得到HTML格式的内容，通过工具类提取body标签的内容
					System.out.println("X1");
					String html_body = ap.getBody().toString();
					String body = a.getContentFromHtml(html_body);
					System.out.println("会议主题：" + ap.getSubject());
					System.out.println("会议组织者：" + ap.getOrganizer());
					System.out.println("会议内容：" + body);
					//会议的开始和结束时间
					System.out.println("会议开始时间：" + ap.getStart());
					System.out.println("会议结束时间：" + ap.getEnd());
					System.out.println("是否会议：" + ap.getIsMeeting());
					System.out.println("会议位置：" + ap.getLocation());
					System.out.println("会议id：" + ap.getId());
					System.out.println("会议接收时间：" + ap.getDateTimeReceived());

					//参加会议的员工
					List<Attendee> RequiredAttendees = ap.getRequiredAttendees().getItems();//必须与会
					List<Attendee> OptionalAttendees = ap.getOptionalAttendees().getItems();//可选与会
					System.out.println("必须与会人员：" + RequiredAttendees);
					System.out.println("可选与会人员：" + OptionalAttendees);
				}


/**
			//会议使用的资源
			List<Attendee> resources = ap.getResources().getItems();
*/

		}


	}
	@Test
	public  void getAppoinementa() throws Exception {
		System.setProperty("https.protocols", "TLSv1,TLSv1.1,TLSv1.2,SSLv3"); //设置TLS版本
		//使用exchange服务工具类创建服务
		//ExchangeMailUtil exchangeMailUtil = new ExchangeMailUtil(mailServer, user, password, readUrlPrefix);
		//ExchangeService service = exchangeMailUtil.getExchangeService();
		//创建exchange服务 ExchangeVersion.Exchange2010_SP1    (服务版本号)
		ExchangeService service = new ExchangeService(ExchangeVersion.Exchange2010_SP1);
		ExchangeCredentials credentials = new WebCredentials("xgwfat@outlook.com", "giyoyo9420", "outlook.com");
		service.setCredentials(credentials);
		service.setUrl(new URI("https://outlook.Office365.com/EWS/Exchange.asmx"));
		service.setTraceEnabled(true);
		Calendar start = Calendar.getInstance();
		start.set(2020,10,1);
		Calendar end = Calendar.getInstance();
		end.set(2020,10,30);
		CalendarFolder calendar = CalendarFolder.bind(service, WellKnownFolderName.Calendar, new PropertySet());
		CalendarView cView = new CalendarView(start.getTime(), end.getTime());
		// 指定要查看的邮箱
		cView.setPropertySet(new PropertySet(AppointmentSchema.Subject, AppointmentSchema.Start, AppointmentSchema.End));
		// Retrieve a collection of appointments by using the calendar view.
		FindItemsResults<Appointment> appointments = calendar.findAppointments(cView);

		for (Appointment a : appointments)
		{
			// 获取各个会议信息
			List<AttendeeInfo> attendees = new ArrayList<>();
			attendees.add(new AttendeeInfo("xgwfat@outlook.com", MeetingAttendeeType.Room, true));
			GetUserAvailabilityResults results = service.getUserAvailability(attendees,
					// 设置当天时间
					new TimeWindow(DateTime.now().plusDays(0).toDate(), DateTime.now().plusDays(1).toDate()),
					AvailabilityData.FreeBusy);
			List<Map<String, Object>> list = new ArrayList<>();
			for (AttendeeAvailability availability : results.getAttendeesAvailability())
			{
				for (CalendarEvent calEvent : availability.getCalendarEvents())
				{
					Map<String, Object> map = new HashMap<>();
					// 开始时间和结束时间
					map.put("start", calEvent.getStartTime());
					map.put("end", calEvent.getEndTime());
					CalendarEventDetails details = calEvent.getDetails();
					if(details != null){
						// subject中包含发件人和主题
						String subject = details.getSubject();
						if(StringUtils.isNotBlank(subject)){
							// 按空格区分发件人和主题
							String[] strings = subject.split(" ");
							map.put("booker", strings[0]);
							map.put("meetingName", strings[1]);
						}
					}
					list.add(map);
					System.out.println(list);
				}

			}
		}
		System.out.println("ok");

	}

	@Test
	//获取当年第一天
	public void  getYearFirst() {
		Calendar date = Calendar.getInstance();
		int year = date.get(Calendar.YEAR);
		Calendar calendar = Calendar.getInstance();
		calendar.clear();
		calendar.set(Calendar.YEAR, year);
		Date currYearFirst = calendar.getTime();
		System.out.println(currYearFirst);
	}

	@Test
	//判断字符串中是否包含某字段（java.lang.String.contains() 方法）
	public void isInclude(){
		String str = "abc";
		boolean status = str.contains("a");
		if(status){
			System.out.println("包含");
		}else{
			System.out.println("不包含");
		}
		List<String> listTitle=getMailMapper.selectFilterKeyFromFilter("title");
		List<String> listSender=getMailMapper.selectFilterKeyFromFilter("sender");
		String[] s=new String[listSender.size()];
		for(int i=0;i<listSender.size();i++){
			s[i]=listSender.get(i);
			System.out.println(s[i]);
		}
		System.out.println(s);
	    //System.out.println(listSender.toString().toCharArray());
		System.out.println(listTitle.toString());
        String subject="过滤";
        String sender ="xiao xie <outlook_E8CA99C2584A3A73@outlook.com>";
        String str1="xgwfat@outlook.com";
		boolean sta = StrUtil.containsAny(subject, listTitle.toString().toCharArray());;
		boolean statusSender=StrUtil.equalsAny(sender,s);
		//如邮箱主题包含过滤关键词的某一个，则过滤该会议
		if (statusSender) {
			System.out.println("存在相等的邮箱");
		} else{
			System.out.println("不存在相等的邮箱");
		}
		if (sta) {
			System.out.println("包含关键字");
		} else{
			System.out.println("不包含关键字");
		}
	}

	@Test
	public void getSimilarityScore() throws Exception {
List<String> title = getMailMapper.selectTitleFromPlanData("yourself");
		TextSimilarity similarity = new CosineSimilarity();
for(int i=0;i<title.size();i++){
	//TextSimilarity similarity = new CosineSimilarity();
	String text5 = title.get(i);
	String text4 = "相似度测试";
	double score1pkx = similarity.getSimilarity(text4, text5);
	if(score1pkx>0.4) {
		System.out.println(text4 + " 和 " + text5 + " 的相似度分值：" + score1pkx);
	}
}


/**
		String text1 = title.get(0);
		String text2 = title.get(1);
		String text3 = title.get(7);
		TextSimilarity similarity = new CosineSimilarity();
		double score1pk2 = similarity.getSimilarity(text1, text2);
		double score1pk3 = similarity.getSimilarity(text1, text3);
		double score2pk2 = similarity.getSimilarity(text2, text2);
		double score2pk3 = similarity.getSimilarity(text2, text3);
		double score3pk3 = similarity.getSimilarity(text3, text3);
		System.out.println(text1 + " 和 " + text2 + " 的相似度分值：" + score1pk2);
		System.out.println(text1 + " 和 " + text3 + " 的相似度分值：" + score1pk3);
		System.out.println(text2 + " 和 " + text2 + " 的相似度分值：" + score2pk2);
		System.out.println(text2 + " 和 " + text3 + " 的相似度分值：" + score2pk3);
		System.out.println(text3 + " 和 " + text3 + " 的相似度分值：" + score3pk3);
 */

	}

	@Test
	//主题相似度排查测试
	public void dailyPlanFromConference(){
		List<String> title = getMailMapper.selectTitleFromPlanData("yourself");
		TextSimilarity similarity = new CosineSimilarity();
		List<ConferenceData> data =getMailMapper.selectConferenceData();
		List<PlanData> list=new ArrayList<>();
		PlanData planData = new PlanData();
		for(int i = 0; i < data.size(); i++) {
			String text4 = data.get(i).getTitle();//会议数据表（conference_data)中的主题
		     for(int j=0;j<title.size();j++) {
		    	String text5 = title.get(j);//日程表（plan_data中的主题）
			    double score1pkx = similarity.getSimilarity(text4, text5);//判断主题相似度
			    System.out.println(text4 + " 和 " + text5 + " 的相似度分值：" + score1pkx);
			    if (score1pkx<=0.8) {
					System.out.println("符合插入条件或已插入");
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
				}
		    }
		}
		if(CollectionUtil.isNotEmpty(list)) {
			getMailMapper.dailyPlanGetFromConference(list);
			list.clear();
		}
	}
}