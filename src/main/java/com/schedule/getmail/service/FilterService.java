package com.schedule.getmail.service;

import com.schedule.getmail.entity.EmailFilter;

import java.util.List;

public interface FilterService {
    int addFilter( List<EmailFilter> list); //设置过滤条件
    List<EmailFilter> selectFilter( String username);//查询过滤条件
}
