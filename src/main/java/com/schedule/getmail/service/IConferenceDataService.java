package com.schedule.getmail.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.schedule.getmail.entity.ConferenceData;
import com.schedule.getmail.entity.EmailConfig;

import java.util.List;

/**
 * <p>
 * 会议数据表 服务类
 * </p>
 *
 * @author StrTom
 * @since 2020-10-28
 */
public interface IConferenceDataService extends IService<ConferenceData> {
    //从邮箱拉取会议信息到会议数据表
    void transferEmail(List<EmailConfig> list)  throws Exception;
    void transferConference(List<EmailConfig> list)  throws Exception;
}
