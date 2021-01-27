package com.schedule.getmail.entity.vo;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * <p>
 * 邮箱配置表
 * 与前端对接
 * </p>
 *
 * @author StrTom
 * @since 2020-10-30
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class EmailConfigVo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 邮箱配置ID
     * “value”：设置数据库字段值
     * “type”：设置主键类型、如果数据库主键设置了自增建议使用“AUTO”
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
    //字段验证，传入null值时允许更新，忽略null值的判断（mybatis-plus默认传入null值不更新）
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
    private String startTime;

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
    @JsonFormat(pattern="yyyy-MM-dd hh:mm:ss")
    private Timestamp createTime;

    /**
     * 更新时间
     */
    @JsonFormat(pattern="yyyy-MM-dd hh:mm:ss")
    private Timestamp updateTime;

    /**
     * 是否只接收chinamoney邮件  1：是/ 0：不是
     */
    private String flag;


}
