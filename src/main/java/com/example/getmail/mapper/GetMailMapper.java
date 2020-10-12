package com.example.getmail.mapper;

import com.example.getmail.bean.EmailConfig;
import com.example.getmail.bean.EmailData;
import com.example.getmail.bean.MailBean;
import com.example.getmail.bean.PlanData;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.util.List;
@Mapper
@Component(value ="getMailMapper")
public interface GetMailMapper {
    int mailInsert(List<EmailConfig> list); //添加邮箱列表
    List<EmailConfig> mailSelect(String username);//查询邮箱列表
    int mailDelete(List<Integer> email_id);//删除指定邮件

    int transferfromemail(List<EmailData> list);//同步插入邮箱数据
    int plandataInsert(List<PlanData> list); //生成日程表
}
