package com.example.getmail.service.impl;

import com.example.getmail.service.GetMailService;

import javax.annotation.Resource;
import java.util.Date;

public class Say implements Runnable{
    @Resource
    private GetMailService getMailService;
    @Override
    public void run(){

        System.out.println("" + new Date() + " hello");

    }
}
