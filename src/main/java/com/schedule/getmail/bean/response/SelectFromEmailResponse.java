package com.schedule.getmail.bean.response;

import lombok.Data;

import java.util.List;

/**
 * FromEmail 结果返回
 */
@Data
public class SelectFromEmailResponse extends BaseResponse {
    private List<String> data;
}
