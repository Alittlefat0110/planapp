package com.example.getmail.entity;

import java.math.BigInteger;
import java.sql.Timestamp;

public class PlanTable {
    private String username;
    private String title;
    private String content;
    private Timestamp createtime;


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Timestamp getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Timestamp createtime) {
        this.createtime = createtime;
    }

    @Override
    public String toString() {
        return "PlanTable{" +
                ", username='" + username + '\'' +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", createtime=" + createtime +
                '}';
    }
}


