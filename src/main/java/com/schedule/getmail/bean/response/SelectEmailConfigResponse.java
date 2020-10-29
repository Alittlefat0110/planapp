package com.schedule.getmail.bean.response;

import com.schedule.getmail.entity.EmailConfig;
import lombok.Data;

@Data
public class SelectEmailConfigResponse extends BaseResponse{
    private EmailConfig data;
}
