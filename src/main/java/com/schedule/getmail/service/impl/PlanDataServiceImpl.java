package com.schedule.getmail.service.impl;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.schedule.getmail.bean.request.AddDailyPlanRequest;
import com.schedule.getmail.entity.PlanData;
import com.schedule.getmail.mapper.PlanDataMapper;
import com.schedule.getmail.service.IPlanDataService;
import com.schedule.getmail.util.CheckUtil;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 日程数据表 服务实现类
 * </p>
 * @author StrTom
 * @since 2020-10-28
 */
@Slf4j(topic = "planDataLogger")
@Service
public class PlanDataServiceImpl extends ServiceImpl<PlanDataMapper, PlanData> implements IPlanDataService {

    @Resource
    private PlanDataMapper planDataMapper;

    /**
     * 按周查询
     * @param username
     * @param pageIndex
     * @return
     */
    @Override
    public List<PlanData> selectByTimeRange(String username, int pageIndex) {
        log.info("planData selectByTimeRange start! userName {},pageIndex {}",username,pageIndex);
        //获取当前时间
        Date now= DateUtil.date();
        //当前时间对应的当前周（pageIndex=0）及偏移（pageIndex=-1：上一周 / pageIndex=1：下一周）
        Date d=DateUtil.offsetWeek(now,pageIndex);
        //所在周的开始时间
        Date startTime=DateUtil.beginOfWeek(d);
        //所在周的结束时间，以星期天作为一周的最后一天
        Date endTime= DateUtil.endOfWeek(d,true);
        //从日程表查询时间段内的日程数据
        log.info("planData selectByTimeRange startTime {},endTime {}",startTime,endTime);
//        List<PlanData> list = planDataMapper.selectByTimeRange(username,startTime,endTime);
        List<PlanData> list = planDataMapper.selectList(new QueryWrapper<PlanData>().lambda()
                .eq(!StringUtils.isEmpty(username), PlanData::getUserName,username)
                .apply("date_format (start_Time,'%Y-%m-%d') >= date_format('" + startTime + "','%Y-%m-%d')")
                .apply("date_format (start_Time,'%Y-%m-%d') <= date_format('" + endTime + "','%Y-%m-%d')")
                .orderByAsc(PlanData::getStartTime)
        );
        log.info("planData selectByTimeRange end! list.size {}",list.size());
        return list;
    }

    /**
     * 按月查询
     * @param username
     * @return
     */
    @Override
    public List<PlanData> selectByMonthRange(String username) {
        log.info("planData selectByMonthRange start! userName {}",username);
        SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        c.add(Calendar.MONTH, 0);
        c.set(Calendar.DAY_OF_MONTH,1);
        String first = format.format(c.getTime());
        System.out.println("===============本月first day:"+first);
        //获取当前月最后一天
        Calendar ca = Calendar.getInstance();
        ca.set(Calendar.DAY_OF_MONTH, ca.getActualMaximum(Calendar.DAY_OF_MONTH));
        String last = format.format(ca.getTime());
        System.out.println("===============本月last day:"+last);
        log.info("planData selectByMonthRange first day {} last day {}",first,last);
        List<PlanData> list = planDataMapper.selectList(new QueryWrapper<PlanData>().lambda()
                .eq(!StringUtils.isEmpty(username), PlanData::getUserName,username)
                .ne(PlanData::getSource,"Message")
                .apply("date_format (start_Time,'%Y-%m-%d') >= date_format('" + first + "','%Y-%m-%d')")
                .apply("date_format (start_Time,'%Y-%m-%d') <= date_format('" + last + "','%Y-%m-%d')")
                .orderByAsc(PlanData::getStartTime)
        );
        log.info("planData selectByMonthRange end! list.size {}",list.size());
        return list;
    }

    /**
     * 新增、修改
     * @param request
     * @return
     */
    @Override
    public boolean saveOrUpdate(AddDailyPlanRequest request) {
        log.info("planData saveOrUpdate start! {}",request);
        int flag  = 0;
        PlanData planData = new PlanData();
        log.info("planData saveOrUpdate planId {}",request.getPlanId());
        if(CheckUtil.isEmpty(request.getPlanId())){
            BeanUtils.copyProperties(request,planData);
            planData.setEndTime(com.schedule.getmail.util.DateUtil.fullParse(request.getEndTime()));
            planData.setStartTime(com.schedule.getmail.util.DateUtil.fullParse(request.getStartTime()));
            planData.setSource("0");
            planData.setCreateTime(new Timestamp(System.currentTimeMillis()));
            flag = planDataMapper.insert(planData);
            log.info("planDataMapper.insert flag {}",flag);
        }else {
            PlanData p = planDataMapper.selectById(request.getPlanId());
            log.info("planDataMapper.selectById PlanData {}",p);
            if(!CheckUtil.isEmpty(p)){
                BeanUtils.copyProperties(request,p);
                p.setEndTime(com.schedule.getmail.util.DateUtil.fullParse(request.getEndTime()));
                p.setStartTime(com.schedule.getmail.util.DateUtil.fullParse(request.getStartTime()));
                p.setUpdateTime(new Timestamp(System.currentTimeMillis()));
                flag = planDataMapper.updateById(p);
                log.info("planDataMapper.updateById flag {}",flag);
            }
        }
        log.info("planData saveOrUpdate end! flag {}",flag>0);
        return flag > 0;
    }
}
