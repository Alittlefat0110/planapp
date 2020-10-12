package com.example.getmail.controller;

import com.example.getmail.bean.EmailConfig;
import com.example.getmail.bean.MailBean;
import com.example.getmail.service.GetMailService;
import microsoft.exchange.webservices.data.core.ExchangeService;
import microsoft.exchange.webservices.data.core.enumeration.misc.ExchangeVersion;
import microsoft.exchange.webservices.data.core.enumeration.property.WellKnownFolderName;
import microsoft.exchange.webservices.data.core.service.item.EmailMessage;

import microsoft.exchange.webservices.data.core.service.item.Item;
import microsoft.exchange.webservices.data.credential.ExchangeCredentials;
import microsoft.exchange.webservices.data.credential.WebCredentials;

import microsoft.exchange.webservices.data.search.FindItemsResults;
import microsoft.exchange.webservices.data.search.ItemView;
import microsoft.exchange.webservices.data.core.service.folder.Folder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import javax.annotation.Resource;
import java.math.BigInteger;
import java.net.URI;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
public class ReadExchangeMail {
    @Resource
    private GetMailService getMailService;

    @ResponseBody
    @RequestMapping(value = "/emailconfig/add", produces = "application/json;charset=utf-8")
    public String insertMail(String username,String email,String password){
        List<EmailConfig> list = new ArrayList<>();
        //获取当前时间
        Timestamp time = new Timestamp(System.currentTimeMillis());
        //配置邮箱列表
        EmailConfig mail = new EmailConfig();
        mail.setUsername(username);
        mail.setEmail(email);
        mail.setPassword(password);
        mail.setCreatetime(time);
        mail.setUpdatetime(time);
        mail.setEncrypt("1");
        mail.setFlag("0");
        list.add(mail);
        getMailService.mailInsert(list);
        return "添加成功";
    }

    @ResponseBody
    @RequestMapping(value = "/emailconfig/list", produces = "application/json;charset=utf-8")
    public List<EmailConfig> mailSelect(String username){
        return getMailService.mailSelect(username);
    }

    @ResponseBody
    @RequestMapping(value = "/emailconfig/delete", produces = "application/json;charset=utf-8")
    public String mailDelete(){
        Integer[] email_id={8,9,10};
        List<Integer> list = new ArrayList<>();
        for (Integer i=0;i< email_id.length;i++){
            Integer a = email_id[i];
            list.add(a);
        }
        getMailService.mailDelete(list);
        return "删除成功";
    }

    @ResponseBody
    @RequestMapping(value = "/dailyplan/transferfromemail", produces = "application/json;charset=utf-8")
    public String makeplan(String username) throws Exception {
        getMailService.transferfromemail(username);
        return "操作成功";
    }
}