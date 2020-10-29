package com.schedule.getmail.bean.request;


import lombok.Data;

import java.sql.Timestamp;

@Data
public class AddEmailConfigRequest {

    String username;
    String email;
    String password;
    Timestamp startTime;
    String keyWord;
    String keyEmail;
}
