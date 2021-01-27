package com.schedule.getmail.controllerTests;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.schedule.getmail.BootstrapperTest;
import com.schedule.getmail.bean.response.SelectHotWordsResponse;
import com.schedule.getmail.constant.ErrorCode;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class TitleFrequencyControllerTest extends BootstrapperTest {


    private static final String listHotWord = "/dailyPlanTitle/getHotWord";

    @Test
    public void listHotWord() throws Exception{
        SelectHotWordsResponse response = restPost(listHotWord,"",SelectHotWordsResponse.class);
        assertThat(response.getCode(),equalTo(ErrorCode.SUCCESS.getCode()));
    }
}
