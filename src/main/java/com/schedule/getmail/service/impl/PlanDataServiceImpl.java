package com.schedule.getmail.service.impl;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.schedule.getmail.bean.request.AddDailyPlanRequest;
import com.schedule.getmail.entity.PlanData;
import com.schedule.getmail.mapper.PlanDataMapper;
import com.schedule.getmail.service.IPlanDataService;
import com.schedule.getmail.util.CheckUtil;
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
@Service
public class PlanDataServiceImpl extends ServiceImpl<PlanDataMapper, PlanData> implements IPlanDataService {

    @Resource
    private PlanDataMapper planDataMapper;

    @Override
    public List<PlanData> selectByTimeRange(String username, int pageIndex) {
        //获取当前时间
        Date now= DateUtil.date();
        //当前时间对应的当前周（pageIndex=0）及偏移（pageIndex=-1：上一周 / pageIndex=1：下一周）
        Date d=DateUtil.offsetWeek(now,pageIndex);
        //所在周的开始时间
        Date startTime=DateUtil.beginOfWeek(d);
        //所在周的结束时间，以星期天作为一周的最后一天
        Date endTime=DateUtil.endOfWeek(d,true);
        //从日程表查询时间段内的日程数据
        return planDataMapper.selectByTimeRange(username,startTime,endTime);
    }

    @Override
    public List<PlanData> selectByMonthRange(String username) {
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
        return planDataMapper.selectList(new QueryWrapper<PlanData>().lambda()
                .eq(!StringUtils.isEmpty(username), PlanData::getUserName,username)
                .ne(PlanData::getSource,"Message")
                .apply("date_format (starttime,'%Y-%m-%d') >= date_format('" + first + "','%Y-%m-%d')")
                .apply("date_format (starttime,'%Y-%m-%d') <= date_format('" + last + "','%Y-%m-%d')")
                .orderByAsc(PlanData::getStartTime));
    }

    @Override
    public boolean saveOrUpdate(AddDailyPlanRequest request) {
        int flag  = 0;
        PlanData planData = new PlanData();
        if(CheckUtil.isEmpty(request.getPlanId())){
            BeanUtils.copyProperties(request,planData);
            planData.setSource("0");
            planData.setCreateTime(new Timestamp(System.currentTimeMillis()));
            flag = planDataMapper.insert(planData);
        }else {
            PlanData p = planDataMapper.selectById(request.getPlanId());
            if(CheckUtil.isEmpty(p)){
                p.setContent(request.getContent());
                p.setTitle(request.getTitle());
                p.setUpdateTime(new Timestamp(System.currentTimeMillis()));
                p.setPosition(request.getPosition());
                p.setStartTime(request.getStartTime());
                p.setEndTime(request.getEndTime());
                flag = planDataMapper.updateById(p);
            }
        }
        return flag > 0;
    }
}
