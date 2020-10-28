package com.example.getmail.entity;

import lombok.Data;

import java.sql.Timestamp;
import java.util.Date;

@Data
public class ConferenceData {
    private Integer conference_id;
    private String calendar_id;
    private String sender;
    private Date receivetime;
    private String title;
    private String content;
    private String position;
    private Date starttime;
    private Date endtime;
    private Timestamp createtime;

}
