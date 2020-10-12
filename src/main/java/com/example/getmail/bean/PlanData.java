package com.example.getmail.bean;

import java.math.BigInteger;
import java.util.Date;
import java.sql.Timestamp;

public class PlanData {
    private BigInteger plan_id;
    private String owner;
    private String title;
    private String content;
    private String tag;
    private Date plantime;
    private int seq;
    private Timestamp createtime;
    private Timestamp updatetime;
    private String flag;
    private String source;

    public BigInteger getPlan_id() {
        return plan_id;
    }

    public void setPlan_id(BigInteger plan_id) {
        this.plan_id = plan_id;
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public Date getPlantime() {
        return plantime;
    }

    public void setPlantime(Date plantime) {
        this.plantime = plantime;
    }

    public int getSeq() {
        return seq;
    }

    public void setSeq(int seq) {
        this.seq = seq;
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

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    @Override
    public String toString() {
        return "PlanData{" +
                "plan_id=" + plan_id +
                ", owner='" + owner + '\'' +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", tag='" + tag + '\'' +
                ", plantime=" + plantime +
                ", seq=" + seq +
                ", createtime=" + createtime +
                ", updatetime=" + updatetime +
                ", flag='" + flag + '\'' +
                ", source='" + source + '\'' +
                '}';
    }
}
