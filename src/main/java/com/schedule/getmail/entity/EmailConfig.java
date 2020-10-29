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
     * 密码加密方式，1：明文，2：*
     */
    private String encrypt;

    /**
     * 创建时间
     */
    private Timestamp createtime;

    /**
     * 更新时间
     */
    private Timestamp updatetime;

    /**
     * 状态
     */
    private String flag;


}
