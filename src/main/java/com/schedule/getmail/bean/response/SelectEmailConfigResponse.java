package com.schedule.getmail.bean.response;

import com.schedule.getmail.entity.vo.EmailConfigVo;
import lombok.Data;

/**
 * 查询绑定邮箱,时间设置,关键词、发件人过滤接口 response
 */
@Data
public class SelectEmailConfigResponse extends BaseResponse {
    private EmailConfigVo data;
}
