package com.example.getmail.controller;

import com.example.getmail.bean.*;
import com.example.getmail.entity.EmailConfig;
import com.example.getmail.constant.ErrorCode;
import com.example.getmail.entity.PlanData;
import com.example.getmail.mapper.GetMailMapper;
import com.example.getmail.service.GetMailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Slf4j(topic = "emailConfig")
@RestController
@Configuration
public class ReadExchangeMail  {
    @Resource
    private GetMailService getMailService;
    private GetMailMapper getMailMapper ;

    //1.添加同步邮箱
    @PostMapping(value = "/emailConfig/add", produces = "application/json;charset=utf-8")
    public AddEmailConfigResponse insertMail(@RequestBody AddEmailConfigRequest request){
        AddEmailConfigResponse response=new AddEmailConfigResponse();
        List<EmailConfig> list = new ArrayList<>();
        //获取当前时间
        Timestamp time = new Timestamp(System.currentTimeMillis());
        //配置邮箱列表
        EmailConfig mail = new EmailConfig();
        mail.setUsername(request.getUsername());//添加用户名
        mail.setEmail(request.getEmail()); //添加邮箱
        mail.setPassword(request.getPassword());//添加密码
        mail.setCreatetime(time);//创建时间
        mail.setUpdatetime(time);//更新时间
        mail.setEncrypt("1"); //密码加密方式
        mail.setFlag("1");//状态
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

    //2.查询同步邮箱列表
    @PostMapping(value = "/emailConfig/list", produces = "application/json;charset=utf-8")
    public SelectEmailConfigResponse mailSelect(@RequestBody SelectEmailConfigRequest request){
        SelectEmailConfigResponse response=new SelectEmailConfigResponse();
        try {
             List<EmailConfig> list =getMailService.mailSelect(request.getUsername());
             response.setData(list);
            response.setErrorCode(ErrorCode.SUCCESS);
        }catch(Exception e){
            log.error("",e);
            response.setErrorCode(ErrorCode.DB_ERROR);
        }
        return response;
    }
    //3.从会议数据表中拉取数据生成日程表
    @PostMapping(value = "/dailyPlan/getFromConference", produces = "application/json;charset=utf-8")
    public AddDailyPlanFromConferenceResponse dailyPlanGetFromConference(){
        AddDailyPlanFromConferenceResponse response=new AddDailyPlanFromConferenceResponse();
        try {
           getMailService.dailyPlanGetFromConference();
            response.setErrorCode(ErrorCode.SUCCESS);
        }catch(Exception e){
            log.error("",e);
            response.setErrorCode(ErrorCode.DB_ERROR);
        }
        return response;
    }

    //4.从日程表中查询指定时间段（当前周）内的日程信息
    @PostMapping(value = "/dailyPlan/listByTimeRange", produces = "application/json;charset=utf-8")
    public SelectDailyPlanByTimeRangeResponse selectByTimeRange (@RequestBody SelectDailyPlanByTimeRangeRequest request) {
        SelectDailyPlanByTimeRangeResponse response=new SelectDailyPlanByTimeRangeResponse();
        try {
            List<PlanData> list=getMailService.selectByTimeRange(request.getUsername(),request.getPageIndex());
            response.setData(list);
            response.setErrorCode(ErrorCode.SUCCESS);
            return response;
        }catch(Exception e){
            log.error("",e);
            response.setErrorCode(ErrorCode.DB_ERROR);
        }
        return response;
    }
    @PostMapping(value = "/DailyPlanTitle/getHottestWord",produces ="application/json;charset=utf-8" )
    public GetHottestWordResponse getHotWord(){
        GetHottestWordResponse response=new GetHottestWordResponse();
        try {
            String word=getMailService.getHotWord();
            response.setData(word);
            response.setErrorCode(ErrorCode.SUCCESS);
            return response;
        }catch (Exception e){
            log.error("",e);
            response.setErrorCode(ErrorCode.DB_ERROR);
        }
        return response;
    }

    @PostMapping(value = "/DailyPlan/SelectByHottestWord",produces ="application/json;charset=utf-8" )
    public SelectDailyPlanByHottestWordResponse SelectByHottestWord(){
        SelectDailyPlanByHottestWordResponse response=new SelectDailyPlanByHottestWordResponse();
        try{
            List<PlanData> list=getMailService.selectByTitle();
            response.setData(list);
            response.setErrorCode(ErrorCode.SUCCESS);
            return response;
        }catch (Exception e){
            log.error("",e);
            response.setErrorCode(ErrorCode.DB_ERROR);
        }
        return response;
    }





    //post 删除同步邮箱邮箱
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
}