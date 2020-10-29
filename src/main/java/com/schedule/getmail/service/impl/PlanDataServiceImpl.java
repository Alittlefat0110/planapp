package com.schedule.getmail.service.impl;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.schedule.getmail.entity.PlanData;
import com.schedule.getmail.mapper.PlanDataMapper;
import com.schedule.getmail.service.IPlanDataService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 日程数据表 服务实现类
 * </p>
 * @author StrTom
 * @since 2020-10-28
 */
@Service
public class PlanDataServiceImpl extends ServiceImpl<PlanDataMapper, PlanData> implements IPlanDataService {

    @Resource
    private PlanDataMapper planDataMapper;

    @Override
    public List<PlanData> selectByTimeRange(String username, int pageIndex) {
        //获取当前时间
        Date now= DateUtil.date();
        //当前时间对应的当前周（pageIndex=0）及偏移（pageIndex=-1：上一周 / pageIndex=1：下一周）
        Date d=DateUtil.offsetWeek(now,pageIndex);
        //所在周的开始时间
        Date startTime=DateUtil.beginOfWeek(d);
        //所在周的结束时间，以星期天作为一周的最后一天
        Date endTime=DateUtil.endOfWeek(d,true);
        //从日程表查询时间段内的日程数据
        return planDataMapper.selectByTimeRange(username,startTime,endTime);
    }
}
