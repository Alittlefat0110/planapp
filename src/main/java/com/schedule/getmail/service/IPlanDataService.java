package com.schedule.getmail.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.schedule.getmail.bean.request.AddDailyPlanRequest;
import com.schedule.getmail.entity.PlanData;

import java.util.List;

/**
 * <p>
 * 日程数据表 服务类
 * </p>
 *
 * @author StrTom
 * @since 2020-10-28
 */
public interface IPlanDataService extends IService<PlanData> {

    /**
     * 查询时间轴
     * @param username
     * @param pageIndex
     * @return
     */
    List<PlanData> selectByTimeRange(String username, int pageIndex);

    /**
     *查询日历程
     * @param username
     * @return
     */
    List<PlanData> selectByMonthRange(String username);

    /**
     * 新增、修改
     * @param request
     * @return
     */
    boolean saveOrUpdate(AddDailyPlanRequest request);

}
