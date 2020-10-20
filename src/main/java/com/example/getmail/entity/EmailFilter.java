package com.example.getmail.entity;

import lombok.Data;

import java.math.BigInteger;
import java.sql.Timestamp;
@Data
public class EmailFilter {
    private Integer filter_id;
    private String username;
    private String filter_name;
    private String filter_key;
    private String type;
    private Timestamp createtime;
    private Timestamp updatetime;
    private Integer flag;

}
