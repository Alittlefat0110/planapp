package com.schedule.getmail.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 同步邮箱数据
 * </p>
 *
 * @author StrTom
 * @since 2020-10-28
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class EmailData implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 邮件ID
     */
    @TableId(value = "email_data_id", type = IdType.AUTO)
    private Long emailDataId;

    /**
     * 邮件参考ID
     */
    private String emailRefId;

    /**
     * 发件人邮箱地址
     */
    private String sender;

    /**
     * 邮件主题
     */
    private String title;

    /**
     * 邮件内容
     */
    private String content;

    /**
     * 收件时间
     */
    private LocalDateTime receivetime;

    /**
     * 同步用户
     */
    private String owner;

    /**
     * 创建时间
     */
    private LocalDateTime createtime;

    /**
     * 更新时间
     */
    private LocalDateTime updatetime;


}
