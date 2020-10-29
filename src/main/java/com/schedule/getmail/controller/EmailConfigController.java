package com.schedule.getmail.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.schedule.getmail.bean.request.AddEmailConfigRequest;
import com.schedule.getmail.bean.response.AddEmailConfigResponse;
import com.schedule.getmail.bean.request.SelectEmailConfigRequest;
import com.schedule.getmail.bean.response.SelectEmailConfigResponse;
import com.schedule.getmail.constant.ErrorCode;
import com.schedule.getmail.entity.EmailConfig;
import com.schedule.getmail.service.IEmailConfigService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * <p>
 * 邮箱配置表 前端控制器
 * </p>
 * @author StrTom
 * @since 2020-10-28
 */
@Slf4j(topic = "emailConfig")
@RestController
public class EmailConfigController {

    @Resource
    private IEmailConfigService emailConfigService;

    /**
     * todo 配置全部放入到这里
     * 绑定邮箱
     * @param request
     * @return
     */
    @ApiOperation(value = "绑定邮箱,时间设置,关键词、发件人过滤接口", notes="绑定邮箱,时间设置,关键词、发件人过滤接口")
    @PostMapping(value = "/emailConfig/saveOrUpdate", produces = "application/json;charset=utf-8")
    public AddEmailConfigResponse insertOrUpdateMail(@RequestBody AddEmailConfigRequest request){
        AddEmailConfigResponse response=new AddEmailConfigResponse();
        try {
            boolean flag = emailConfigService.saveOrUpdate(request);
            if(flag){
                response.setErrorCode(ErrorCode.SUCCESS);
            }else {
                response.setErrorCode(ErrorCode.DB_ERROR);
            }
        }catch (Exception e){
            log.error("",e);
            response.setErrorCode(ErrorCode.DB_ERROR);
        }
        return response;
    }

    /**
     * 根据userName查询EmailConfig
     * @param request
     * @return
     */
    @ApiOperation(value = "查询绑定邮箱,时间设置,关键词、发件人过滤接口", notes="查询绑定邮箱,时间设置,关键词、发件人过滤接口")
    @PostMapping(value = "/emailConfig/getEmailConfig", produces = "application/json;charset=utf-8")
    public SelectEmailConfigResponse mailSelect(@RequestBody SelectEmailConfigRequest request){
        SelectEmailConfigResponse response=new SelectEmailConfigResponse();
        try {
            EmailConfig emailConfig = emailConfigService.getOne(new QueryWrapper<EmailConfig>().lambda()
                    .eq(!StringUtils.isEmpty(request.getUsername()), EmailConfig::getEmail,request.getUsername())
            );
            response.setData(emailConfig);
            response.setErrorCode(ErrorCode.SUCCESS);
        }catch(Exception e){
            log.error("",e);
            response.setErrorCode(ErrorCode.DB_ERROR);
        }
        return response;
    }

}
