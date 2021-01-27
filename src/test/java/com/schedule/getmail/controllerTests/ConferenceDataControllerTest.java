package com.schedule.getmail.controllerTests;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.schedule.getmail.BootstrapperTest;
import com.schedule.getmail.bean.response.TransferConferenceDataResponse;
import com.schedule.getmail.constant.ErrorCode;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class ConferenceDataControllerTest extends BootstrapperTest {

    @Autowired
    protected ObjectMapper objectMapper;

    private final static String transferEmail = "/conferenceData/transferEmail";
    private final static String transferConference = "/conferenceData/transferConference";

    @Test
    public void transferEmail() throws Exception{
        TransferConferenceDataResponse response = restPost(transferEmail,"",TransferConferenceDataResponse.class);
        assertThat(response.getCode(),equalTo(ErrorCode.SUCCESS.getCode()));
    }

    @Test
    public void transferConference() throws Exception{
        TransferConferenceDataResponse response = restPost(transferConference,"",TransferConferenceDataResponse.class);
        assertThat(response.getCode(),equalTo(ErrorCode.SUCCESS.getCode()));
    }

}
