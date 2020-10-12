package com.example.getmail.service;

import com.example.getmail.bean.EmailConfig;
import com.example.getmail.bean.EmailData;
import com.example.getmail.bean.MailBean;
import com.example.getmail.bean.PlanData;

import java.math.BigInteger;
import java.util.List;

public interface GetMailService {
    int mailInsert(List<EmailConfig> list);//添加邮箱列表
    List<EmailConfig> mailSelect(String username);//查询邮箱列表
    int mailDelete(List<Integer> email_id);//删除指定邮件

    void transferfromemail(String username) throws Exception;//同步插入邮箱数据
    int plandataInsert(List<PlanData> list); //生成日程表
    //int makeplan(String username) throws Exception;//生成日程信息
}
