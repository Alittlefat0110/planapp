package com.example.getmail.bean;

import com.example.getmail.entity.EmailFilter;
import lombok.Data;

import java.util.List;

@Data
public class SelectFilterResponse extends  BaseResponse{
    private List<EmailFilter> data;
}
