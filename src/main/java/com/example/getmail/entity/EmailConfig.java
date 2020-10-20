package com.example.getmail.entity;

import lombok.Data;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.Date;
@Data
public class EmailConfig {
    private Integer email_id;
    private String username;
    private String email;
    private String password;
    private String encrypt; //密码加密方式
    private Timestamp createtime;
    private Timestamp updatetime;
    private String flag; //状态

    public Integer getEmail_id() {
        return email_id;
    }

    public void setEmail_id(Integer email_id) {
        this.email_id = email_id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEncrypt() {
        return encrypt;
    }

    public void setEncrypt(String encrypt) {
        this.encrypt = encrypt;
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

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    @Override
    public String toString() {
        return "EmailConfig{" +
                "email_id=" + email_id +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", encrypt='" + encrypt + '\'' +
                ", createtime=" + createtime +
                ", updatetime=" + updatetime +
                ", flag='" + flag + '\'' +
                '}';
    }
}
