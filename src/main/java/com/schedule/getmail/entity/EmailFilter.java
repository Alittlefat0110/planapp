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
 * 同步过滤条件表
 * </p>
 *
 * @author StrTom
 * @since 2020-10-28
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class EmailFilter implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 过滤条件ID
     */
    @TableId(value = "filter_id", type = IdType.AUTO)
    private Long filterId;

    /**
     * 条件设置者
     */
    private String username;

    /**
     * sender:发件邮件，title：邮件主题
     */
    private String filterName;

    /**
     * 过滤内容
     */
    private String filterKey;

    /**
     * 过滤类型 1:黑名单 0：白名单
     */
    private String type;

    /**
     * 创建时间
     */
    private LocalDateTime createtime;

    /**
     * 更新时间
     */
    private LocalDateTime updatetime;

    /**
     * 状态 1：生效  0：禁用
     */
    private String flag;


}
