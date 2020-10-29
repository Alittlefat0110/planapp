package com.schedule.getmail.mapper;

import com.planapp.getmail.entity.*;
import com.schedule.getmail.entity.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;


import java.util.Date;
import java.util.List;
@Mapper
@Component(value ="getMailMapper")
public interface GetMailMapper {
    int mailInsert(@Param("list") List<EmailConfig> list); //添加邮箱列表
    List<EmailConfig> mailSelect(@Param("username") String username);//查询邮箱列表
    int mailDelete(@Param("email_id") List<Integer> email_id);//删除指定邮件
    int transferFromEmail(@Param("list") List<EmailData> list);//从邮箱同步插入邮箱数据到邮箱基础数据表
    int transferFromCalendar(@Param("list")List<ConferenceData> list);//从邮箱同步会议数据到会议基础数据表
    List<String> maildatacontent(@Param("owner") String owner);//从email_data获得邮件内容
    List<PlanData> selectByTimeRange(@Param("username")String username, @Param("startTime")Date startTime, @Param("endTime")Date endTime);//查询当前周日程
    PlanData selectByTitle(@Param("title") String title);//根据主题查询日程
    List<PlanData> selectByWord(@Param("title") String title);//根据主题模糊查询
    Date latestReceiveTime(); //获取上次拉取邮件数据的最新收件时间
    Date oldestReceiveTime();//拉取最早收件时间 post
    Date latestCalendarReceiveTime();//会议的最新（晚）收取时间
    List<ConferenceData> selectConferenceData();//获取会议数据表的信息 用于插入日程表
    void  dailyPlanGetFromConference(@Param("list") List<PlanData> list);//以会议数据生成日程表
    List<String> selectFilterKeyFromFilter(@Param("filter_name") String filter_name);//查询过滤条件 关键词/邮箱
    List<String> selectTitleFromPlanData(@Param("username") String username);//获取日程表中的主题内容，用于相似度比较
    int addWordsFrequency(@Param("list") List<TitleFrequency> list);//存储日程主题名词出现频数
    String selectHottestWords();//查询频率最高的词
}
