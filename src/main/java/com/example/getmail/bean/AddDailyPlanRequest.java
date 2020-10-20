package com.example.getmail.bean;

import lombok.Data;

import java.util.Date;
@Data
public class AddDailyPlanRequest {

         String username;
         String title;
         String content;
         String position;
         Date starttime;
         Date endtime;
         Date plantime;

}
