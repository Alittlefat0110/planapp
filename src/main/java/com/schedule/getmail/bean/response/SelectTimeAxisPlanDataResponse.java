package com.schedule.getmail.bean.response;

import com.schedule.getmail.entity.PlanData;
import com.schedule.getmail.entity.vo.TimeAxisPlanDataVo;
import lombok.Data;

import java.util.List;

/**
 * PlanData 结果返回
 */
@Data
public class SelectTimeAxisPlanDataResponse extends BaseResponse{
    private List<TimeAxisPlanDataVo> data;
}
