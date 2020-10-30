package com.schedule.getmail.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * <p>
 * 会议数据表
 * </p>
 *
 * @author StrTom
 * @since 2020-10-30
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class ConferenceData implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 会议ID
     */
    @TableId(value = "conference_id", type = IdType.AUTO)
    private Long conferenceId;

    /**
     * 每个会议的参考id
     */
    private String calendarId;

    /**
     * 发起人
     */
    private String sender;

    /**
     * 收件人
     */
    private String receiver;

    /**
     * 接收时间
     */
    private Date receiveTime;

    /**
     * 主题
     */
    private String title;

    /**
     * 内容
     */
    private String content;

    /**
     * 会议地点
     */
    private String position;

    /**
     * 会议开始时间
     */
    private Date startTime;

    /**
     * 会议结束时间
     */
    private Date endTime;

    /**
     * 创建时间
     */
    private Timestamp createTime;

    /**
     * 数据类型：普通邮件Message/ 会议MeetingRequest
     */
    private String type;

    /**
     * 用户名
     */
    private String userName;


}
