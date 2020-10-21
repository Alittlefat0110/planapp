package com.example.getmail.controller;

import com.example.getmail.bean.AddEmailConfigResponse;
import com.example.getmail.constant.ErrorCode;
import com.example.getmail.service.GetMailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.annotation.Resource;


@RestController
@Slf4j(topic = "getDataFromEmail")
public class EwsExchangeController {
    @Resource
    private GetMailService getMailService;

    //1.拉取邮箱数据
    @Scheduled(cron = "0 0 23 * * ? ")//每天晚上十一点执行点
    @PostMapping(value = "/emailData/transferFromEmail", produces = "application/json;charset=utf-8")
    public AddEmailConfigResponse insertMail(){
        AddEmailConfigResponse response=new AddEmailConfigResponse();
        try {
            getMailService.transferFromEmail();
            response.setErrorCode(ErrorCode.SUCCESS);
        }catch(Exception e){
            log.error("",e);
            response.setErrorCode(ErrorCode.DB_ERROR);
        }
        return response;
    }
    //拉取会议数据
    @Scheduled(cron = "0 0 0 * * ? ")//每天零点执行
    @PostMapping(value = "/conferenceData/transferFromCalendar", produces = "application/json;charset=utf-8")
    public AddEmailConfigResponse insertCalendar(){
        AddEmailConfigResponse response=new AddEmailConfigResponse();
        try {
            getMailService.transferFromCalendar();
            response.setErrorCode(ErrorCode.SUCCESS);
        }catch(Exception e){
            log.error("",e);
            response.setErrorCode(ErrorCode.DB_ERROR);
        }
        return response;
    }
}
