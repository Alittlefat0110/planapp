package com.example.getmail.service.impl;

import com.example.getmail.entity.EmailFilter;
import com.example.getmail.mapper.FilterMapper;
import com.example.getmail.service.FilterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service(value = "FilterService")
public class FilterServiceImpl  implements FilterService {
    @Autowired
    private FilterMapper filterMapper;

    @Override
    //设置过滤条件
    public  int addFilter( List<EmailFilter> list){
        return filterMapper.addFilter(list);
    }
    @Override
    //查询过滤条件
    public List<EmailFilter> selectFilter( String username){
        return filterMapper.selectFilter(username);
    }
}
