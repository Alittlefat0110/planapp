package com.schedule.getmail.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.schedule.getmail.entity.ConferenceData;
import com.schedule.getmail.mapper.ConferenceDataMapper;
import com.schedule.getmail.service.IConferenceDataService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 会议数据表 服务实现类
 * </p>
 *
 * @author StrTom
 * @since 2020-10-28
 */
@Service
public class ConferenceDataServiceImpl extends ServiceImpl<ConferenceDataMapper, ConferenceData> implements IConferenceDataService {

}
