package com.schedule.getmail.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.schedule.getmail.bean.request.AddEmailConfigRequest;
import com.schedule.getmail.entity.EmailConfig;
import com.schedule.getmail.mapper.EmailConfigMapper;
import com.schedule.getmail.service.IEmailConfigService;
import com.schedule.getmail.util.CheckUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.sql.Timestamp;

/**
 * <p>
 * 邮箱配置表 服务实现类
 * </p>
 *
 * @author StrTom
 * @since 2020-10-28
 */
@Service
@Slf4j(topic = "emailConfigLogger")
public class EmailConfigServiceImpl extends ServiceImpl<EmailConfigMapper, EmailConfig> implements IEmailConfigService {

    @Resource
    private EmailConfigMapper emailConfigMapper;

    @Override
    public boolean saveOrUpdate(AddEmailConfigRequest request) {
        log.info("emailConfig saveOrUpdate start! {}", request);
        int flag = 0;
        //根据userName判断是否已经绑定
        String username = request.getUserName();
        //获取当前时间
        Timestamp time = new Timestamp(System.currentTimeMillis());
        //根据username查库（先判空）
        EmailConfig emailConfig1 = emailConfigMapper.selectOne(new QueryWrapper<EmailConfig>()
                .lambda()
                .eq(!StringUtils.isEmpty(username), EmailConfig::getUserName, username)
        );
        log.info("emailConfigMapper.selectOne {} , request.getUserName {} ", emailConfig1, username);
        //若根据传入的username查库为空，则直接插入
        if (CheckUtil.isEmpty(emailConfig1)) {
            EmailConfig emailConfig = new EmailConfig();
            BeanUtils.copyProperties(request, emailConfig);
            //添加邮箱同步开始时间
            String startTime = request.getStartTime();
            Timestamp startTimeTimestamp = null;
            if (!CheckUtil.isEmpty(startTime)) {
                startTimeTimestamp = Timestamp.valueOf(request.getStartTime());
            }
            emailConfig.setStartTime(startTimeTimestamp);
            //创建时间
            emailConfig.setCreateTime(time);
            //更新时间
            emailConfig.setUpdateTime(time);
            //密码加密方式
            emailConfig.setEncrypt("1");
            flag = emailConfigMapper.insert(emailConfig);
            log.info("emailConfigMapper.insert  {} ", flag);
        } else {
            //添加邮箱同步开始时间
            String startTime = request.getStartTime();
            Timestamp startTimeTimestamp = null;
            if (!CheckUtil.isEmpty(startTime)) {
                startTimeTimestamp = Timestamp.valueOf(request.getStartTime());
            }
            emailConfig1.setStartTime(startTimeTimestamp);
            //添加过滤关键词
            BeanUtils.copyProperties(request, emailConfig1);
            //更新时间
            emailConfig1.setUpdateTime(time);
            //密码加密方式
            emailConfig1.setEncrypt("1");
            //若传入的开始时间与此前不同，则将新时间插入NewStartTime字段
            if (emailConfig1.getStartTime().getTime() != startTimeTimestamp.getTime()) {
                emailConfig1.setNewStartTime(startTimeTimestamp);
            }
            flag = emailConfigMapper.updateById(emailConfig1);
            log.info("emailConfigMapper.updateById  {} ", flag);
        }
        log.info("emailConfig saveOrUpdate end! {}", flag > 0);
        return flag > 0;
    }
}
