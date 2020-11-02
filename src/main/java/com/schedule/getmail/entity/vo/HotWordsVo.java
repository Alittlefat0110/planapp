package com.schedule.getmail.entity.vo;

import lombok.Data;

/**
 * 热词分组统计
 */
@Data
public class HotWordsVo {
    /**
     * 用户名
     */
    private String userName;

    /**
     * 热词出现的次数
     */
    private int titleCount;
}
