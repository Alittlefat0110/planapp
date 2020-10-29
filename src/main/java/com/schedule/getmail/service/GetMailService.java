package com.schedule.getmail.service;

import com.schedule.getmail.entity.EmailConfig;
import com.schedule.getmail.entity.PlanData;

import java.util.List;

public interface GetMailService {
    int mailInsert(List<EmailConfig> list);//添加邮箱列表
    List<EmailConfig> mailSelect(String username);//查询邮箱列表
    int mailDelete(List<Integer> email_id);//删除指定邮件
    void transferFromEmail() throws Exception;//同步插入邮箱数据
    void transferFromCalendar() throws Exception;//从邮箱拉取会议信息到会议数据表
    List<PlanData> selectByTimeRange(String username, int pageIndex);//查询当前周日程信息
    void  dailyPlanGetFromConference();//以会议数据生成日程表
    String getHotWord();//查看日程主题中的热搜名词（频数最大）
    List<PlanData> selectByTitle();//根据主题查询日程
}
