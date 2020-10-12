package com.example.getmail.service;

import com.example.getmail.entity.EmailConfig;
import com.example.getmail.entity.PlanData;
import com.example.getmail.entity.PlanTable;

import java.util.List;

public interface GetMailService {
    int mailInsert(List<EmailConfig> list);//添加邮箱列表
    List<EmailConfig> mailSelect(String username);//查询邮箱列表
    int mailDelete(List<Integer> email_id);//删除指定邮件

    void transferfromemail() throws Exception;//同步插入邮箱数据
    int plandataInsert(List<PlanData> list); //生成日程表
    int plantableInsert();
    //int makeplan(String username) throws Exception;//生成日程信息
    //void Scheduled ();
    List<PlanTable> selectThisWeek();//查询当前周
}
