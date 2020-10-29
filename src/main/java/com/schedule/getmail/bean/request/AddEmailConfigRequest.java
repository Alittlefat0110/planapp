package com.schedule.getmail.bean.request;


import lombok.Data;

@Data
public class AddEmailConfigRequest {

    String username;
    String email;
    String password;
}
