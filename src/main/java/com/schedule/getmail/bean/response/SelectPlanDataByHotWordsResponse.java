package com.schedule.getmail.bean.response;

import com.schedule.getmail.entity.vo.HotWordsPlanDataVo;
import lombok.Data;

import java.util.List;

/**
 * PlanData 结果返回
 */
@Data
public class SelectPlanDataByHotWordsResponse extends BaseResponse {
    private List<HotWordsPlanDataVo> data;
}
