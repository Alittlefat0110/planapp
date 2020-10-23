package com.example.getmail.mapper;

import com.example.getmail.entity.*;
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
    int transferFromEmail(@Param("list") List<EmailData> list);//同步插入邮箱数据到邮箱基础数据表
    int transferFromCalendar(@Param("list")List<ConferenceData> list);//同步会议数据到会议基础数据表
    List<EmailData> maildatacontent(String owner);//从email_data获得邮件内容
    List<PlanData> selectByTimeRange(@Param("username")String username, @Param("startTime")Date startTime,@Param("endTime")Date endTime);//查询当前周
    Date latestReceiveTime(); //获取上次拉取数据的最新收件时间
    Date oldestReceiveTime();//拉取最早收件时间
    Date latestCalendarReceiveTime();//最新会议收取时间
    List<ConferenceData> selectConferenceData();//获取会议数据表的信息以插入日程表
    void  dailyPlanGetFromConference(@Param("list") List<PlanData> list);//以会议数据生成日程表
    //List<EmailFilter> selectTitleFromFilter();//查询过滤关键词
    List<EmailFilter> selectFilterKeyFromFilter(@Param("filter_name") String filter_name);//查询过滤邮箱
    List<String> selectTitleFromPlanData(@Param("username") String username);
}
