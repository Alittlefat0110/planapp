package com.schedule.getmail.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.schedule.getmail.bean.request.AddEmailConfigRequest;
import com.schedule.getmail.entity.EmailConfig;

/**
 * <p>
 * 邮箱配置表 服务类
 * </p>
 *
 * @author StrTom
 * @since 2020-10-28
 */
public interface IEmailConfigService extends IService<EmailConfig> {

    boolean saveOrUpdate(AddEmailConfigRequest request);

}
