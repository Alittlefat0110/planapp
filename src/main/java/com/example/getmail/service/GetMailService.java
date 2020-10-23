package com.example.getmail.service;

import com.example.getmail.entity.EmailConfig;
import com.example.getmail.entity.PlanData;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface GetMailService {
    int mailInsert(List<EmailConfig> list);//添加邮箱列表
    List<EmailConfig> mailSelect(String username);//查询邮箱列表
    int mailDelete(List<Integer> email_id);//删除指定邮件
    void transferFromEmail() throws Exception;//同步插入邮箱数据
    void transferFromCalendar() throws Exception;//从邮箱拉取会议信息到会议数据表
    List<PlanData> selectByTimeRange(String username,int pageIndex);//查询当前周日程信息
    void  dailyPlanGetFromConference();//以会议数据生成日程表
}
