package com.schedule.getmail.service.impl;

import com.schedule.getmail.entity.EmailFilter;
import com.schedule.getmail.mapper.FilterMapper;
import com.schedule.getmail.service.FilterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service(value = "FilterService")//表示给当前类命名一个别名，方便注入到其他需要用到的类中；不加的话，默认别名就是当前类名，但是首字母小写
public class FilterServiceImpl  implements FilterService {
    @Autowired
    private FilterMapper filterMapper;

    @Override
    //新增过滤条件
    public  int addFilter( List<EmailFilter> list){ return filterMapper.addFilter(list);
    }
    @Override
    //查询过滤条件
    public List<EmailFilter> selectFilter( String username){
        return filterMapper.selectFilter(username);
    }
}
