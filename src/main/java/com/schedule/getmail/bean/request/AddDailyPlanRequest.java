package com.schedule.getmail.bean.request;

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
