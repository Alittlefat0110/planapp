package com.example.getmail.entity;

import java.math.BigInteger;
import java.sql.Timestamp;

public class EmailFilter {
    private BigInteger filter_id;
    private String owner;
    private String title;
    private String sender;
    private String type;
    private String createTime;
    private String updateTime;
    private String flag;

    public BigInteger getFilter_id() {
        return filter_id;
    }

    public void setFilter_id(BigInteger filter_id) {
        this.filter_id = filter_id;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    @Override
    public String toString() {
        return "EmailFilter{" +
                "filter_id=" + filter_id +
                ", owner='" + owner + '\'' +
                ", title='" + title + '\'' +
                ", sender='" + sender + '\'' +
                ", type='" + type + '\'' +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", flag='" + flag + '\'' +
                '}';
    }
}
