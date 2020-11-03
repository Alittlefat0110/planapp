package com.schedule.getmail.entity;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.sql.Timestamp;
import java.time.LocalDateTime;

/**
 * <p>
 * 邮箱配置表
 * </p>
 *
 * @author StrTom
 * @since 2020-10-30
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class EmailConfig implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 邮箱配置ID
     */
    @TableId(value = "email_id", type = IdType.AUTO)
    private Long emailId;

    /**
     * 所属用户
     */
    private String userName;

    /**
     * 邮箱地址
     */
    private String email;

    /**
     * 邮箱登陆密码
     */
    private String password;

    /**
     * 部门
     */
    @TableField(strategy = FieldStrategy.IGNORED)
    private String department;

    /**
     * 岗位
     */
    @TableField(strategy = FieldStrategy.IGNORED)
    private String station;

    /**
     * 设置邮箱同步开始时间
     */
    @TableField(strategy = FieldStrategy.IGNORED)
    private Timestamp startTime;

    /**
     * 过滤关键词/角色标签
     */
    @TableField(strategy = FieldStrategy.IGNORED)
    private String keyWordS;

    /**
     * 过滤关键词/通用标签
     */
    @TableField(strategy = FieldStrategy.IGNORED)
    private String keyWordT;

    /**
     * 过滤邮箱
     */
    @TableField(strategy = FieldStrategy.IGNORED)
    private String keyEmail;

    /**
     * 密码加密方式，1：明文，2：*
     */
    private String encrypt;

    /**
     * 创建时间
     */
    private Timestamp createTime;

    /**
     * 更新时间
     */
    private Timestamp updateTime;

    /**
     * 是否只接收chinamoney邮件  1：是/ 0：不是
     */
    private String flag;

    /**
     * 最新起始时间
     */
    private Timestamp newStartTime;

}
