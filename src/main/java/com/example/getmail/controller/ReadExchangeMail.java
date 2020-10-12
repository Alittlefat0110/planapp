package com.example.getmail.controller;

import com.example.getmail.bean.AddEmailConfigRequest;
import com.example.getmail.bean.AddEmailConfigResponse;
import com.example.getmail.entity.EmailConfig;
import com.example.getmail.constant.ErrorCode;
import com.example.getmail.mapper.GetMailMapper;
import com.example.getmail.service.GetMailService;
import lombok.extern.slf4j.Slf4j;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Slf4j(topic = "emailConfig")
@RestController
@Configuration      //1.主要用于标记配置类，兼备Component的效果。
@EnableScheduling
@Component
public class ReadExchangeMail  {
    @Resource
    private GetMailService getMailService;
    private GetMailMapper getMailMapper ;

    //添加邮箱
    @PostMapping(value = "/emailConfig/add", produces = "application/json;charset=utf-8")
    public AddEmailConfigResponse insertMail(@RequestBody AddEmailConfigRequest request){
        AddEmailConfigResponse response=new AddEmailConfigResponse();
        List<EmailConfig> list = new ArrayList<>();
        //获取当前时间
        Timestamp time = new Timestamp(System.currentTimeMillis());
        //配置邮箱列表
        EmailConfig mail = new EmailConfig();
        mail.setUsername(request.getUsername());
        mail.setEmail(request.getEmail());
        mail.setPassword(request.getPassword());
        mail.setCreatetime(time);
        mail.setUpdatetime(time);
        mail.setEncrypt("1");
        mail.setFlag("1");
        list.add(mail);
        try {
            getMailService.mailInsert(list);
            response.setErrorCode(ErrorCode.SUCCESS);
        }catch(Exception e){
            log.error("",e);
            response.setErrorCode(ErrorCode.DB_ERROR);
        }
        return response;
    }

    //查询邮箱列表
    @RequestMapping(value = "/emailConfig/list", produces = "application/json;charset=utf-8")
    public List<EmailConfig> mailSelect(String username){
        return getMailService.mailSelect(username);
    }

    //post 删除邮箱
    @RequestMapping(value = "/emailConfig/delete", produces = "application/json;charset=utf-8")
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

    //@Scheduled(cron = "0 0 24 * * * ?")//每晚12点同步一次
    //拉取邮箱数据
    @RequestMapping(value = "/dailyPlan/getMessageFromEmail", produces = "application/json;charset=utf-8")
    public void makeplan() throws Exception {
         getMailService.transferfromemail();
         System.out.println("操作成功");

    }
    //生成日程表
    @RequestMapping(value = "/dailyPlan/transferFromEmail", produces = "application/json;charset=utf-8")
    public int plantableInsert()  {
        return getMailService.plantableInsert();

    }
    //查询当前周
    //@Scheduled(cron = "0 0 24 * * * ?")//每晚12点同步一次
    @RequestMapping(value = "/dailyplan/listcurrentweek", produces = "application/json;charset=utf-8")
    public void selectThisWeek () {
        System.out.println("操作成功");
        getMailService.selectThisWeek();
    }


}