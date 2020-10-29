package com.schedule.getmail.bean.request;

import lombok.Data;

import java.math.BigInteger;

@Data
public class AddFilterRequest {
    private String username;
    private String filter_name;
    private String filter_key;
    private String type;
}
