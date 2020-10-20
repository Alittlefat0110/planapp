package com.example.getmail.controller;

import com.example.getmail.bean.*;
import com.example.getmail.constant.ErrorCode;
import com.example.getmail.entity.EmailFilter;
import com.example.getmail.service.FilterService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Slf4j(topic= "FilterConfig")
@RestController
public class FilterController {
    @Autowired
    FilterService filterService;
    //设置过滤条件
    @PostMapping(value = "/dailyPlan/filterSetting",produces = "application/json;charset=utf-8")
    public AddFilterResponse addFilter(@RequestBody AddFilterRequest request){
        AddFilterResponse response=new AddFilterResponse();
        Timestamp time = new Timestamp(System.currentTimeMillis()); //获取当前时间
        List<EmailFilter> list = new ArrayList<>();
        EmailFilter f =new EmailFilter();
        f.setUsername(request.getUsername());
        f.setFilter_name(request.getFilter_name());
        f.setFilter_key(request.getFilter_key());
        f.setType(request.getType());
        f.setCreatetime(time);
        f.setUpdatetime(time);
        f.setFlag(1);
        list.add(f);
        try {
            filterService.addFilter(list);
            response.setErrorCode(ErrorCode.SUCCESS);
        }catch(Exception e){
            log.error("",e);
            response.setErrorCode(ErrorCode.DB_ERROR);
        }
        return response;
    }
    //查询过滤条件
    @PostMapping(value = "/dailyPlan/filterList",produces = "application/json;charset=utf-8")
    public SelectFilterResponse selectFilter(@RequestBody SelectFilterRequest request){
        SelectFilterResponse response=new SelectFilterResponse();

        try {
            List<EmailFilter> list= filterService.selectFilter(request.getUsername());
            response.setData(list);
            response.setErrorCode(ErrorCode.SUCCESS);
        }catch(Exception e){
            log.error("",e);
            response.setErrorCode(ErrorCode.DB_ERROR);
        }
        return response;
    }


}
