package com.schedule.getmail.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.schedule.getmail.bean.response.SelectHotWordsResponse;
import com.schedule.getmail.entity.TitleFrequency;
import com.schedule.getmail.service.ITitleFrequencyService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;

import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

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

    @Resource
    private ITitleFrequencyService titleFrequencyService;

    /**
     * 获取前十热词
     * @return
     */
    @ApiOperation(value = "查询前热词前10接口", notes="查询前热词前10接口")
    @PostMapping(value = "/dailyPlanTitle/getHotWord",produces ="application/json;charset=utf-8" )
    public SelectHotWordsResponse listHotWord(){
        SelectHotWordsResponse response=new SelectHotWordsResponse();
        List<TitleFrequency> list = titleFrequencyService.list(new QueryWrapper<TitleFrequency>().lambda()
                .orderByDesc(TitleFrequency::getFrequency)
                .last("limit 10")
        );
        response.setData(list);
        return response;
    }



}
