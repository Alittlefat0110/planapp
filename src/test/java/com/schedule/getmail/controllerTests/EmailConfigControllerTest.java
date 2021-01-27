package com.schedule.getmail.controllerTests;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.schedule.getmail.BootstrapperTest;
import com.schedule.getmail.bean.request.AddEmailConfigRequest;
import com.schedule.getmail.bean.request.SelectConferenceDataByToEmailRequest;
import com.schedule.getmail.bean.request.SelectEmailConfigRequest;
import com.schedule.getmail.bean.response.AddEmailConfigResponse;
import com.schedule.getmail.bean.response.SelectEmailConfigResponse;
import com.schedule.getmail.bean.response.SelectFromEmailResponse;
import com.schedule.getmail.constant.ErrorCode;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class EmailConfigControllerTest extends BootstrapperTest {

    @Autowired
    protected ObjectMapper objectMapper;

    private static final String insertOrUpdateMail = "/emailConfig/saveOrUpdate";
    private static final String mailSelect = "/emailConfig/getEmailConfig";
    private static final String getFromEmail = "/emailConfig/getFromEmail";

    @Value("classpath:data/querySaveOrUpdate.json")
    private Resource querySaveOrUpdate;
    @Value("classpath:data/queryGetEmailConfig.json")
    private Resource queryGetEmailConfig;
    @Value("classpath:data/queryGetFromEmail.json")
    private Resource queryGetFromEmail;

    @Test
    public void insertOrUpdateMail() throws Exception{
        AddEmailConfigRequest request = objectMapper.readValue(querySaveOrUpdate.getInputStream(),AddEmailConfigRequest.class);
        AddEmailConfigResponse response = restPost(insertOrUpdateMail,request,AddEmailConfigResponse.class);
        assertThat(response.getCode(),equalTo(ErrorCode.SUCCESS.getCode()));
    }

    @Test
    public void mailSelect() throws Exception{
        SelectEmailConfigRequest request = objectMapper.readValue(queryGetEmailConfig.getInputStream(),SelectEmailConfigRequest.class);
        SelectEmailConfigResponse response = restPost(mailSelect,request,SelectEmailConfigResponse.class);
        assertThat(response.getCode(),equalTo(ErrorCode.SUCCESS.getCode()));
    }

    @Test
    public void getFromEmail() throws Exception{
        SelectConferenceDataByToEmailRequest request = objectMapper.readValue(queryGetFromEmail.getInputStream(),SelectConferenceDataByToEmailRequest.class);
        SelectFromEmailResponse response = restPost(getFromEmail,request,SelectFromEmailResponse.class);
        assertThat(response.getCode(),equalTo(ErrorCode.SUCCESS.getCode()));
    }



}
