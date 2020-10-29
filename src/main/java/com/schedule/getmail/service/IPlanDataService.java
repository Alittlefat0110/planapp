package com.schedule.getmail.service;

import com.baomidou.mybatisplus.extension.service.IService;
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
     * 查询当前周日程信息
     * @param username
     * @param pageIndex
     * @return
     */
    List<PlanData> selectByTimeRange(String username, int pageIndex);

}
