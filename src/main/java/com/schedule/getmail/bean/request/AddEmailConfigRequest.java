package com.schedule.getmail.bean.request;


import lombok.Data;

import java.sql.Timestamp;

/**
 * insert 绑定邮箱,时间设置,关键词、发件人过滤  request
 */
@Data
public class AddEmailConfigRequest {

    String userName;
    String email;
    String password;
    Timestamp startTime;
    String keyWord;
    String keyEmail;
}
