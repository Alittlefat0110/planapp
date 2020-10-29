//package com.schedule.getmail.controller;
//
//import com.schedule.getmail.bean.response.AddEmailConfigResponse;
//import com.schedule.getmail.constant.ErrorCode;
//import com.schedule.getmail.service.GetMailService;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RestController;
//import javax.annotation.Resource;
//
//
///**
// * 同步邮箱数据
// */
//@RestController
//@Slf4j(topic = "getDataFromEmail")
//public class EmailDataController {
//    @Resource
//    private GetMailService getMailService;
//
//    /**
//     * 邮件同步
//     * @return
//     */
//    @Scheduled(cron = "0 0 23 * * ? ")//每天晚上十一点执行点
//    @PostMapping(value = "/emailData/transferFromEmail", produces = "application/json;charset=utf-8")
//    public AddEmailConfigResponse insertMail(){
//        AddEmailConfigResponse response=new AddEmailConfigResponse();
//        try {
//            getMailService.transferFromEmail();
//            response.setErrorCode(ErrorCode.SUCCESS);
//        }catch(Exception e){
//            log.error("",e);
//            response.setErrorCode(ErrorCode.DB_ERROR);
//        }
//        return response;
//    }
//
//    /**
//     * 会议同步
//     * @return
//     */
//    @Scheduled(cron = "0 0 0 * * ? ")//每天零点执行
//    @PostMapping(value = "/conferenceData/transferFromCalendar", produces = "application/json;charset=utf-8")
//    public AddEmailConfigResponse insertCalendar(){
//        AddEmailConfigResponse response=new AddEmailConfigResponse();
//        try {
//            getMailService.transferFromCalendar();
//            response.setErrorCode(ErrorCode.SUCCESS);
//        }catch(Exception e){
//            log.error("",e);
//            response.setErrorCode(ErrorCode.DB_ERROR);
//        }
//        return response;
//    }
//}
