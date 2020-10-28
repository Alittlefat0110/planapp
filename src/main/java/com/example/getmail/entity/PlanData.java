package com.example.getmail.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.sql.Timestamp;
@Data
public class PlanData  {
    private Integer plan_id;
    private String username;
    private String title;
    private String content;
    private String position;
    private Date starttime;
    private Date endtime;
    private Date plantime;
    private Timestamp createtime;
    private Timestamp updatetime;
    private String flag;
    private String source;



}
