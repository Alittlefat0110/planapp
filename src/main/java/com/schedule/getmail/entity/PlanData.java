package com.schedule.getmail.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

/**
 * <p>
 * 日程数据表
 * </p>
 *
 * @author StrTom
 * @since 2020-10-28
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
    private String username;

    /**
     * 主题
     */
    private String title;

    /**
     * 详情
     */
    private String content;

    /**
     * 会议位置
     */
    private String position;

    /**
     * 会议开始时间
     */
    private Date starttime;

    /**
     * 会议结束时间
     */
    private Date endtime;

    /**
     * 待办时间
     */
    private Date plantime;

    /**
     * 创建时间
     */
    private Timestamp createtime;

    /**
     * 更新时间
     */
    private Timestamp updatetime;

    /**
     * 状态 1-正常，0-禁用 -1,已删除
     */
    private String flag;

    /**
     * 数据来源 0：手动添加 1:邮件同步
     */
    private String source;


}
