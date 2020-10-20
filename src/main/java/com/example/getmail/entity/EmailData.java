package com.example.getmail.entity;
import lombok.Data;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.Date;

@Data
public class EmailData {
    private BigInteger email_data_id;//邮件ID
    private String email_ref_id;//message ID
    private String sender; //发送人邮件地址
    private String title; //主题
    private String content; //邮件内容
    private Date receivetime; //收件时间
    private String owner; //同步用户
    private Timestamp createtime;
    private Timestamp updatetime;
}
