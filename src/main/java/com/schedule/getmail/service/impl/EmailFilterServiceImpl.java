package com.schedule.getmail.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.schedule.getmail.entity.EmailFilter;
import com.schedule.getmail.mapper.EmailFilterMapper;
import com.schedule.getmail.service.IEmailFilterService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 同步过滤条件表 服务实现类
 * </p>
 *
 * @author StrTom
 * @since 2020-10-28
 */
@Service
public class EmailFilterServiceImpl extends ServiceImpl<EmailFilterMapper, EmailFilter> implements IEmailFilterService {

}
