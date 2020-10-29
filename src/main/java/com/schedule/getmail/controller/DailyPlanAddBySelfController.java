//package com.example.getmail.controller;
//
//import com.example.getmail.bean.*;
//import com.example.getmail.constant.ErrorCode;
//import com.example.getmail.entity.PlanData;
//import com.example.getmail.service.DailyPlanConfigBySelfService;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RestController;
//
//import javax.annotation.Resource;
//import java.sql.Timestamp;
//import java.util.ArrayList;
//import java.util.List;
//
//@Slf4j(topic = "DailyPlanConfig")
//@RestController
//public class DailyPlanAddBySelfController {
//    @Resource
//    private DailyPlanConfigBySelfService dailyPlanAddBySelfService;
//
//    //1.手动添加日程
//    @PostMapping(value = "/dailyPlan/add", produces = "application/json;charset=utf-8")
//    public AddDailyPlanResponse dailyPlanAddBySelf(@RequestBody AddDailyPlanRequest request){
//        AddDailyPlanResponse response=new AddDailyPlanResponse();
//        List<PlanData> list = new ArrayList<>();
//        Timestamp time = new Timestamp(System.currentTimeMillis()); //获取当前时间
//        PlanData planData = new PlanData();
//        planData.setUsername(request.getUsername());   //插入用户名
//        planData.setTitle(request.getTitle());         //插入主题
//        planData.setContent(request.getContent());     //插入会议内容
//        planData.setPosition(request.getPosition());   //插入会议位置
//        planData.setStarttime(request.getStarttime()); //插入会议开始时间
//        planData.setEndtime(request.getEndtime());     //插入会议结束时间
//        planData.setPlantime(request.getPlantime());   //插入待办时间
//        planData.setCreatetime(time);                  //插入创建时间
//        planData.setUpdatetime(time);                  //插入初始更新时间=创建时间
//        planData.setFlag("1");                         //插入日程状态  1-正常，0-禁用 -1,已删除
//        planData.setSource("0");                       //插入数据来源  0：手动添加 1:邮件同步
//        list.add(planData);
//        try {
//            dailyPlanAddBySelfService.dailyPlanAddBySelf(list);
//            response.setErrorCode(ErrorCode.SUCCESS);
//        }catch(Exception e){
//            log.error("",e);
//            response.setErrorCode(ErrorCode.DB_ERROR);
//        }
//        return response;
//    }
//
//    //2.删除日程
//    @PostMapping(value = "/dailyPlan/delete", produces = "application/json;charset=utf-8")
//    public DeleteDailyPlanReponse dailyPlanDeleteById(@RequestBody DeleteDailyPlanRequest request ){
//        DeleteDailyPlanReponse response=new  DeleteDailyPlanReponse();
//        try {
//
//                dailyPlanAddBySelfService.dailyPlanDeleteById(request.getUsername(),request.getPlan_id());
//                response.setErrorCode(ErrorCode.SUCCESS);
//        }catch(Exception e){
//            log.error("",e);
//            response.setErrorCode(ErrorCode.DB_ERROR);
//        }
//        return response;
//    }
//    //3.更新日程
//    @PostMapping(value = "/dailyPlan/update", produces = "application/json;charset=utf-8")
//    public UpdateDailyPlanResponse dailyPlanUpdateById(@RequestBody UpdateDailyPlanRequest request){
//        UpdateDailyPlanResponse response=new UpdateDailyPlanResponse();
//        Timestamp time = new Timestamp(System.currentTimeMillis()); //获取当前时间
//        PlanData planData = new PlanData();
//        planData.setPlan_id(request.getPlan_id());     //选定id
//        planData.setUsername(request.getUsername());   //插入用户名
//        planData.setTitle(request.getTitle());         //插入主题
//        planData.setContent(request.getContent());     //插入会议内容
//        planData.setPosition(request.getPosition());   //插入会议位置
//        planData.setStarttime(request.getStarttime()); //插入会议开始时间
//        planData.setEndtime(request.getEndtime());     //插入会议结束时间
//        planData.setPlantime(request.getPlantime());   //插入待办时间
//        //planData.setCreatetime(time);                //插入创建时间
//        planData.setUpdatetime(time);                  //插入更新时间!=创建时间
//        planData.setFlag("1");                         //插入日程状态  1-正常，0-禁用 -1,已删除
//        planData.setSource("0");                       //插入数据来源  0：手动添加 1:邮件同步
//        try {
//            dailyPlanAddBySelfService.dailyPlanUpdateById(planData);
//            response.setErrorCode(ErrorCode.SUCCESS);
//        }catch(Exception e){
//            log.error("",e);
//            response.setErrorCode(ErrorCode.DB_ERROR);
//        }
//        return response;
//    }
//}
