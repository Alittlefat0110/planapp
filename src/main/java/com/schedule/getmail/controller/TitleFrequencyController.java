package com.schedule.getmail.controller;


import com.schedule.getmail.bean.response.GetHottestWordResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 日程主题名词词频统计表 前端控制器
 * </p>
 * @author StrTom
 * @since 2020-10-28
 */
@Slf4j(topic = "titleFrequency")
@RestController
public class TitleFrequencyController {

    /**
     * 获取前十热词
     * @return
     */
    @PostMapping(value = "/DailyPlanTitle/getHottestWord",produces ="application/json;charset=utf-8" )
    public GetHottestWordResponse getHotWord(){
        GetHottestWordResponse response=new GetHottestWordResponse();
//        try {
//            String word=getMailService.getHotWord();
//            response.setData(word);
//            response.setErrorCode(ErrorCode.SUCCESS);
//            return response;
//        }catch (Exception e){
//            log.error("",e);
//            response.setErrorCode(ErrorCode.DB_ERROR);
//        }
        return response;
    }

    /**
     * 根据热词查询所有和热词相关
     * @return
     */
//    @PostMapping(value = "/DailyPlan/SelectByHottestWord",produces ="application/json;charset=utf-8" )
//    public SelectDailyPlanByHottestWordResponse SelectByHottestWord(){
//        SelectDailyPlanByHottestWordResponse response=new SelectDailyPlanByHottestWordResponse();
//        try{
//            List<PlanData> list=getMailService.selectByTitle();
//            response.setData(list);
//            response.setErrorCode(ErrorCode.SUCCESS);
//            return response;
//        }catch (Exception e){
//            log.error("",e);
//            response.setErrorCode(ErrorCode.DB_ERROR);
//        }
//        return response;
//    }

}
