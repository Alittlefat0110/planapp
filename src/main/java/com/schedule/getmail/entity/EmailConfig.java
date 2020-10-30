package com.schedule.getmail.entity;

import com.baomidou.mybatisplus.annotation.IdType;
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
 * @since 2020-10-28
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
    private String username;

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
    private String department;

    /**
     * 岗位
     */
    private String station;

    /**
     * 邮箱同步开始时间
     */
    private Timestamp startTime;

    /**
     * 过滤关键词/角色标签
     */
    private String keyWordS;

    /**
     * 过滤关键词/通用标签
     */
    private String keyWordT;

    /**
     * 过滤邮箱
     */
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
     * 状态
     */
    private String flag;


}
