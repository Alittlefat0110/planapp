package com.schedule.getmail.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.schedule.getmail.bean.request.AddEmailConfigRequest;
import com.schedule.getmail.entity.EmailConfig;
import com.schedule.getmail.mapper.EmailConfigMapper;
import com.schedule.getmail.service.IEmailConfigService;
import com.schedule.getmail.util.CheckUtil;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.sql.Timestamp;

/**
 * <p>
 * 邮箱配置表 服务实现类
 * </p>
 * @author StrTom
 * @since 2020-10-28
 */
@Service
public class EmailConfigServiceImpl extends ServiceImpl<EmailConfigMapper, EmailConfig> implements IEmailConfigService {

    @Resource
    private EmailConfigMapper emailConfigMapper;

    @Override
    public boolean saveOrUpdate(AddEmailConfigRequest request) {
        int flag  = 0;
        //根据userName判断是否已经绑定
        String username = request.getUsername();
        //获取当前时间
        Timestamp time = new Timestamp(System.currentTimeMillis());
        EmailConfig emailConfig1 = emailConfigMapper.selectOne(new QueryWrapper<EmailConfig>().lambda()
                .eq(!StringUtils.isEmpty(username), EmailConfig::getUsername,username)
        );
        if(CheckUtil.isEmpty(emailConfig1)){
            EmailConfig emailConfig = new EmailConfig();
            //添加用户名
            emailConfig.setUsername(request.getUsername());
            //添加邮箱
            emailConfig.setEmail(request.getEmail());
            //添加密码
            emailConfig.setPassword(request.getPassword());
            //添加邮箱同步开始时间
            emailConfig.setStartTime(request.getStartTime());
            //添加过滤关键词
            emailConfig.setKeyWord(request.getKeyWord());
            //添加过滤邮箱
            emailConfig.setKeyEmail(request.getKeyEmail());
            //创建时间
            emailConfig.setCreatetime(time);
            //更新时间
            emailConfig.setUpdatetime(time);
            //密码加密方式
            emailConfig.setEncrypt("1");
            //状态
            emailConfig.setFlag("1");
           flag =  emailConfigMapper.insert(emailConfig);
        }else {
            //添加邮箱
            emailConfig1.setEmail(request.getEmail());
            //添加密码
            emailConfig1.setPassword(request.getPassword());
            //添加邮箱同步开始时间
            emailConfig1.setStartTime(request.getStartTime());
            //添加过滤关键词
            emailConfig1.setKeyWord(request.getKeyWord());
            //添加过滤邮箱
            emailConfig1.setKeyEmail(request.getKeyEmail());
            //更新时间
            emailConfig1.setUpdatetime(time);
            //密码加密方式
            emailConfig1.setEncrypt("1");
            //状态
            emailConfig1.setFlag("1");
            flag =  emailConfigMapper.updateById(emailConfig1);
        }
        return flag > 0;
    }
}
