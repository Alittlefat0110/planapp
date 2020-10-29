package com.schedule.getmail.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.schedule.getmail.entity.EmailData;
import com.schedule.getmail.mapper.EmailDataMapper;
import com.schedule.getmail.service.IEmailDataService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 同步邮箱数据 服务实现类
 * </p>
 * @author StrTom
 * @since 2020-10-28
 */
@Service
public class EmailDataServiceImpl extends ServiceImpl<EmailDataMapper, EmailData> implements IEmailDataService {

}
