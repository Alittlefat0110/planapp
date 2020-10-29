package com.schedule.getmail.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 日程主题名词词频统计表
 * </p>
 *
 * @author StrTom
 * @since 2020-10-28
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class TitleFrequency implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主题中分离出的名词
     */
    private String words;

    /**
     * 出现频数
     */
    private Integer frequency;


}
