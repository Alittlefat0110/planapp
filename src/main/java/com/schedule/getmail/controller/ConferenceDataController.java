package com.schedule.getmail.controller;


import com.schedule.getmail.bean.response.TransferConferenceDataResponse;
import com.schedule.getmail.constant.ErrorCode;
import com.schedule.getmail.service.IConferenceDataService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.PostMapping;

import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * <p>
 * 会议数据表 前端控制器
 * </p>
 *
 * @author StrTom
 * @since 2020-10-28
 */
@RestController
@Slf4j(topic = "getDataFromEmail")
public class ConferenceDataController {
    @Resource
    private IConferenceDataService iConferenceDataService;

    //拉取邮箱数据
    @Scheduled(cron = "0 37 15 * * * ")
    @PostMapping(value = "/conferenceData/transferEmail", produces = "application/json;charset=utf-8")
    public TransferConferenceDataResponse transferEmail() {
        TransferConferenceDataResponse response = new TransferConferenceDataResponse();
        try {
            iConferenceDataService.transferEmail(null);
            response.setErrorCode(ErrorCode.SUCCESS);
        } catch (Exception e) {
            log.error("", e);
            response.setErrorCode(ErrorCode.DB_ERROR);
        }
        return response;
    }

    //拉取会议数据
    @Scheduled(cron = "0 37 15 * * * ")
    @PostMapping(value = "/conferenceData/transferConference", produces = "application/json;charset=utf-8")
    public TransferConferenceDataResponse transferConference() {
        TransferConferenceDataResponse response = new TransferConferenceDataResponse();
        try {
            iConferenceDataService.transferConference(null);
            response.setErrorCode(ErrorCode.SUCCESS);
        } catch (Exception e) {
            log.error("", e);
            response.setErrorCode(ErrorCode.DB_ERROR);
        }
        return response;
    }
}
