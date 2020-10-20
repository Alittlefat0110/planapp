package com.example.getmail;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.StrUtil;
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
	public void getUserUnReadMail() throws Exception {

		/** try {
		 SSLContext ctx = SSLContext.getInstance("TLSv1.2");
		 ctx.init(null, null, null);
		 SSLContext.setDefault(ctx);  //将你所要使用的TLS版本设为默认
		 } catch (NoSuchAlgorithmException e1) {
		 e1.printStackTrace();
		 } catch (KeyManagementException e) {
		 e.printStackTrace();
		 }
		 System.setProperty("https.protocols", "TLSv1.2");*/
		System.setProperty("https.protocols", "TLSv1,TLSv1.1,TLSv1.2,SSLv3");
		//使用exchange服务工具类创建服务
		//ExchangeMailUtil exchangeMailUtil = new ExchangeMailUtil(mailServer, user, password, readUrlPrefix);
		//ExchangeService service = exchangeMailUtil.getExchangeService();
		//创建exchange服务 ExchangeVersion.Exchange2010_SP1    (服务版本号)
		ExchangeService service = new ExchangeService(ExchangeVersion.Exchange2010_SP1);
		ExchangeCredentials credentials = new WebCredentials("xgwfat@outlook.com", "giyoyo9420", "outlook.com");
		service.setCredentials(credentials);
		//service.autodiscoverUrl("xiaoxie0929@163.com");
		service.setUrl(new URI("https://s.outlook.com/EWS/Exchange.asmx"));

		// Bind to the Inbox.
		Folder inbox = Folder.bind(service, WellKnownFolderName.Inbox);
		System.out.println(inbox.getDisplayName());
		ItemView itemView = new ItemView(10);
		// 查询
		FindItemsResults<Item> findResults = service.findItems(inbox.getId(), itemView);
		ArrayList<Item> items = findResults.getItems();
		//for (int i = 0; i < items.size(); i++) {
		//	EmailMessage message = EmailMessage.bind(service, items.get(i).getId());
		//	message.load();
		//System.out.println(message.getSender());
		//System.out.println("邮件主题：" + items.get(i).getSubject());
		//System.out.println("接收方：" + message.getReceivedBy());
		//System.out.println("发送：" + message.getSender());
		//System.out.println("发送人：" + message.getFrom());
		//System.out.println("接收时间：" + items.get(i).getDateTimeReceived());
		//System.out.println("是否已读：" + message.getIsRead());
		//System.out.println("邮件ID：" + items.get(i).getId()); //Message ID
		//	System.out.println("邮件内容：" + message.getBody()); //Message ID


		//	}
		ItemView view = new ItemView(10);
		FindItemsResults<Item> findResult = service.findItems(inbox.getId(), view);
		for (Item item : findResult.getItems()) {
			EmailMessage message = EmailMessage.bind(service, item.getId());
			List<Attachment> attachs = message.getAttachments().getItems();//
			System.out.println("id-->" + message.getId());
			System.out.println("sender-->" + message.getSender());
			System.out.println("sub-->" + item.getSubject());
			System.out.println("body-->" + message.getUniqueBody());
		}
	}

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
		Calendar end = Calendar.getInstance();
		end.set(2020,10,24);
		Date start = new Date();
		//Date end = new Date(start.getTime() + 1000*3600*24);
		CalendarView cView = new CalendarView(start,end.getTime());
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
		List<EmailFilter> listTitle=getMailMapper.selectFilterKeyFromFilter("title");

		for(Appointment ap:appointmentItems){
			ap.load();
			String subject = ap.getSubject();
				boolean status = StrUtil.containsAny(subject, listTitle.get(1).getFilter_key());
				//如邮箱主题包含过滤关键词的某一个，则过滤该会议
				if (status) {
					continue;
				} else {
					//得到HTML格式的内容，通过工具类提取body标签的内容
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
		List<EmailFilter> listTitle=getMailMapper.selectFilterKeyFromFilter("title");
		System.out.println(listTitle.toString());
	}

}