package com.example.getmail.service;

import com.example.getmail.entity.EmailFilter;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface FilterService {
    int addFilter( List<EmailFilter> list); //设置过滤条件
    List<EmailFilter> selectFilter( String username);//查询过滤条件
}
