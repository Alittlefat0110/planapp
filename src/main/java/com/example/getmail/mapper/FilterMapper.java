package com.example.getmail.mapper;

import com.example.getmail.entity.EmailFilter;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface FilterMapper {
    int addFilter(@Param("list") List<EmailFilter> list); //设置过滤条件
    List<EmailFilter> selectFilter(@Param("username") String username);//查询过滤条件
}
