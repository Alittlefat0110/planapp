package com.schedule.getmail.entity;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.schedule.getmail.util.CheckUtil;
import com.schedule.getmail.util.DateUtil;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * <p>
 * 日程数据表
 * </p>
 *
 * @author StrTom
 * @since 2020-10-30
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class PlanData implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 日程ID
     */
    @TableId(value = "plan_id", type = IdType.AUTO)
    private Long planId;

    /**
     * 所属用户
     */
    private String userName;

    /**
     * 发件人
     */
    @TableField(strategy = FieldStrategy.IGNORED)
    private String sender;

    /**
     * 收件人
     */
    @TableField(strategy = FieldStrategy.IGNORED)
    private String receiver;

    /**
     * 主题
     */
    private String title;

    /**
     * 详情
     */
    @TableField(strategy = FieldStrategy.IGNORED)
    private String content;

    /**
     * 会议位置
     */
    @TableField(strategy = FieldStrategy.IGNORED)
    private String position;

    /**
     * 会议开始时间
     */
    @TableField(strategy = FieldStrategy.IGNORED)
    private Date startTime;

    /**
     * 会议结束时间
     */
    @TableField(strategy = FieldStrategy.IGNORED)
    private Date endTime;

    /**
     * 接收时间
     */
    @TableField(strategy = FieldStrategy.IGNORED)
    private Date receiveTime;

    /**
     * 创建时间
     */
    private Timestamp createTime;

    /**
     * 更新时间
     */
    @TableField(strategy = FieldStrategy.IGNORED)
    private Timestamp updateTime;

    /**
     * 数据来源 0：手动添加 1:邮件同步
     */
    private String source;


    @TableField(exist=false)
    private String time;

    public String getTime(){
        if(!CheckUtil.isEmpty(startTime)){
            return DateUtil.formatHHMMSS(startTime);
        }
        return "";
    }



}
