package com.schedule.getmail.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.schedule.getmail.entity.PlanData;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 日程数据表 Mapper 接口
 * </p>
 *
 * @author StrTom
 * @since 2020-10-28
 */
public interface PlanDataMapper extends BaseMapper<PlanData> {

    /**
     * 查询当前周日程
     * @param username
     * @param startTime
     * @param endTime
     * @return
     */
    List<PlanData> selectByTimeRange(@Param("username")String username, @Param("startTime") Date startTime, @Param("endTime")Date endTime);

}
