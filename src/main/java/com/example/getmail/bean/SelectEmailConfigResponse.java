package com.example.getmail.bean;

import com.example.getmail.entity.EmailConfig;
import com.example.getmail.entity.PlanData;
import lombok.Data;

import java.util.List;

@Data
public class SelectEmailConfigResponse extends BaseResponse{
    private List<EmailConfig> data;
}
