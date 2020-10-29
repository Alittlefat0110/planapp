package com.schedule.getmail.bean.response;

import com.schedule.getmail.entity.EmailFilter;
import lombok.Data;

import java.util.List;

@Data
public class SelectFilterResponse extends  BaseResponse{
    private List<EmailFilter> data;
}
