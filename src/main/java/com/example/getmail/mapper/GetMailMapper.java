package com.example.getmail.mapper;

import com.example.getmail.bean.*;
import com.example.getmail.entity.EmailConfig;
import com.example.getmail.entity.EmailData;
import com.example.getmail.entity.PlanData;
import com.example.getmail.entity.PlanTable;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;



import java.util.List;
@Mapper
@Component(value ="getMailMapper")
public interface GetMailMapper {
    int mailInsert(List<EmailConfig> list); //添加邮箱列表
    List<EmailConfig> mailSelect(String username);//查询邮箱列表
    int mailDelete(List<Integer> email_id);//删除指定邮件

    int transferfromemail(List<EmailData> list);//同步插入邮箱数据
    int plandataInsert(List<PlanData> list); //post 生成日程表
    int plantableInsert(); //生成日程表
    List<EmailData> maildatacontent(String owner);//从email_data获得邮件内容
    List<PlanTable> selectThisWeek();//查询当前周
    List<CronConfig> getCron();
}
