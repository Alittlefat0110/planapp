package com.example.getmail.entity;

import java.math.BigDecimal;
import java.util.Date;

public class MailBean {
    private int id;
    private String title;
    private String fromPeople;
    private Date receiveTime;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFromPeople() {
        return fromPeople;
    }

    public void setFromPeople(String fromPeople) {
        this.fromPeople = fromPeople;
    }

    public Date getReceiveTime() {
        return receiveTime;
    }

    public void setReceiveTime(Date receiveTime) {
        this.receiveTime = receiveTime;
    }

    @Override
    public String toString() {
        return "MailBean{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", fromPeople='" + fromPeople + '\'' +
                ", receiveTime=" + receiveTime +
                '}';
    }

}
