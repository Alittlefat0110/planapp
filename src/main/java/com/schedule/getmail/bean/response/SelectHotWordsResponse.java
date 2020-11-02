package com.schedule.getmail.bean.response;

import com.schedule.getmail.entity.TitleFrequency;
import lombok.Data;

import java.util.List;

@Data
public class SelectHotWordsResponse extends BaseResponse{
    private List<TitleFrequency> data;
}
