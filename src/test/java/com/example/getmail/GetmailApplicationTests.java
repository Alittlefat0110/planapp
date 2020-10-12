package com.example.getmail;

import com.example.getmail.entity.EmailData;
import com.example.getmail.mapper.GetMailMapper;
import com.example.getmail.service.GetMailService;
import com.example.getmail.service.impl.util;
import microsoft.exchange.webservices.data.core.ExchangeService;
import microsoft.exchange.webservices.data.core.enumeration.misc.ExchangeVersion;
import microsoft.exchange.webservices.data.core.enumeration.property.WellKnownFolderName;
import microsoft.exchange.webservices.data.core.enumeration.search.SortDirection;
import microsoft.exchange.webservices.data.core.service.folder.Folder;
import microsoft.exchange.webservices.data.core.service.item.EmailMessage;
import microsoft.exchange.webservices.data.core.service.item.Item;
import microsoft.exchange.webservices.data.core.service.schema.ItemSchema;
import microsoft.exchange.webservices.data.credential.ExchangeCredentials;
import microsoft.exchange.webservices.data.credential.WebCredentials;
import microsoft.exchange.webservices.data.property.complex.Attachment;
import microsoft.exchange.webservices.data.search.FindItemsResults;
import microsoft.exchange.webservices.data.search.ItemView;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.net.URI;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
@RestController
@Component
@EnableScheduling
@SpringBootTest
class GetmailApplicationTests {
	EmailData emailData =new EmailData();
	util a;
    @Resource
	private GetMailMapper getMailMapper ;
	private GetMailService getMailService;
	@Test
	public void  getUserUnReadMail() throws Exception {

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
	public void  gettime(){
		//long l = System.currentTimeMillis();
		//Date time1=new Date(l);
		//SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		//System.out.println(sdf.format(time1));
		//Timestamp time = new Timestamp(System.currentTimeMillis());
		//System.out.println(time);

		List<EmailData> li = new ArrayList<>();
		for(int i=0;i<li.size();i++) {
			EmailData a = li.get(i);
			String X=a.getTitle();
			System.out.println(X);
		}
		  //getMailMapper.plandataInsert(list);

		//System.out.println(getMailMapper.maildatacontent("mine"));
	}

	@Test
	public void makepemail()  throws Exception {

		System.setProperty("https.protocols", "TLSv1,TLSv1.1,TLSv1.2,SSLv3"); //设置TLS版本
		//使用exchange服务工具类创建服务
		//ExchangeMailUtil exchangeMailUtil = new ExchangeMailUtil(mailServer, user, password, readUrlPrefix);
		//ExchangeService service = exchangeMailUtil.getExchangeService();
		//创建exchange服务 ExchangeVersion.Exchange2010_SP1    (服务版本号)
		ExchangeService service = new ExchangeService(ExchangeVersion.Exchange2010_SP1);
		ExchangeCredentials credentials = new WebCredentials("xgwfat@outlook.com", "giyoyo9420", "outlook.com");
		service.setCredentials(credentials);
		service.setUrl(new URI("https://s.outlook.com/EWS/Exchange.asmx"));
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
			h.setTag("1");
			Timestamp time = new Timestamp(System.currentTimeMillis());
			h.setCreatetime(time);
			h.setUpdatetime(time);
			list.add(h);
			getMailMapper.transferfromemail(list);
		}
/**
		//getMailMapper.transferfromemail(list);
		List<PlanData> list1= new ArrayList<>();
		PlanData p = new PlanData();
		p.setOwner("me");
		p.setTitle(emailData.getTitle());
		p.setContent(emailData.getContent());
		p.setTag("1");
		p.setPlantime(emailData.getReceivetime());
		p.setSeq(0);
		Timestamp time = new Timestamp(System.currentTimeMillis());
		p.setCreatetime(time);
		p.setUpdatetime(time);
		p.setFlag("1");
		p.setSource("username");
		list1.add(p);
		 getMailMapper.plandataInsert(list1);
		System.out.println("操作成功");
 */
	}


	@Test
	public void  plantable(){
		getMailMapper.plantableInsert();
		System.out.println("插入成功");

	}
	@Test
	@Scheduled(cron = "0/3 * * * * ?")
	//@ResponseBody
	public void contextLoads() {
		System.out.println(getMailMapper.selectThisWeek());
	}
}

