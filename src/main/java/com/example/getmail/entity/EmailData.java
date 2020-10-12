package com.example.getmail.entity;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.Date;


public class EmailData {
    private BigInteger email_data_id;//邮件ID
    private String email_ref_id;//message ID
    private String sender; //发送人邮件地址
    private String title; //主题
    private String content; //邮件内容
    private Date receivetime; //收件时间
    private String tag; //会议标签
    private String owner; //同步用户
    private Timestamp createtime;
    private Timestamp updatetime;

    public BigInteger getEmail_data_id() {
        return email_data_id;
    }

    public void setEmail_data_id(BigInteger email_data_id) {
        this.email_data_id = email_data_id;
    }

    public String getEmail_ref_id() {
        return email_ref_id;
    }

    public void setEmail_ref_id(String email_ref_id) {
        this.email_ref_id = email_ref_id;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
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

    public Date getReceivetime() {
        return receivetime;
    }

    public void setReceivetime(Date receivetime) {
        this.receivetime = receivetime;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public Timestamp getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Timestamp createtime) {
        this.createtime = createtime;
    }

    public Timestamp getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(Timestamp updatetime) {
        this.updatetime = updatetime;
    }

    @Override
    public String toString() {
        return "EmailData{" +
                "email_data_id=" + email_data_id +
                ", email_ref_id=" + email_ref_id +
                ", sender='" + sender + '\'' +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", receivetime='" + receivetime + '\'' +
                ", tag='" + tag + '\'' +
                ", owner='" + owner + '\'' +
                ", createtime='" + createtime + '\'' +
                ", updatetime='" + updatetime + '\'' +
                '}';
    }
}
