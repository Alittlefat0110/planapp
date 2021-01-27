package com.schedule.getmail.service.impl;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.schedule.getmail.bean.request.AddDailyPlanRequest;
import com.schedule.getmail.contentSimilarity.tokenizer.Tokenizer;
import com.schedule.getmail.contentSimilarity.tokenizer.Word;
import com.schedule.getmail.entity.PlanData;
import com.schedule.getmail.entity.TitleFrequency;
import com.schedule.getmail.entity.vo.HotWordsVo;
import com.schedule.getmail.entity.vo.HotWordsPlanDataVo;
import com.schedule.getmail.entity.vo.TimeAxisPlanDataVo;
import com.schedule.getmail.mapper.PlanDataMapper;
import com.schedule.getmail.mapper.TitleFrequencyMapper;
import com.schedule.getmail.service.IPlanDataService;
import com.schedule.getmail.util.CheckUtil;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 日程数据表 服务实现类
 * </p>
 *
 * @author StrTom
 * @since 2020-10-28
 */
@Slf4j(topic = "planDataLogger")
@Service
public class PlanDataServiceImpl extends ServiceImpl<PlanDataMapper, PlanData> implements IPlanDataService {

    @Resource
    private PlanDataMapper planDataMapper;
    @Resource
    private TitleFrequencyMapper titleFrequencyMapper;

    /**
     * 按周查询
     *
     * @param username
     * @param pageIndex
     * @return
     */
    @Override
    public List<TimeAxisPlanDataVo> selectByTimeRange(String username, int pageIndex) {
        log.info("planData selectByTimeRange start! userName {},pageIndex {}", username, pageIndex);
        // 返回的日期集合
        List<String> days = new ArrayList<String>();
        //获取当前时间
        Date now = DateUtil.date();
        //当前时间对应的当前周（pageIndex=0）及偏移（pageIndex=-1：上一周 / pageIndex=1：下一周）
        Date d = DateUtil.offsetWeek(now, pageIndex);
        //所在周的开始时间
        Date startTime = DateUtil.beginOfWeek(d);
        //所在周的结束时间，以星期天作为一周的最后一天
        Date endTime = DateUtil.endOfWeek(d, true);
        Calendar tempStart = Calendar.getInstance();
        tempStart.setTime(startTime);
        Calendar tempEnd = Calendar.getInstance();
        tempEnd.setTime(endTime);
        //获取当前周是哪7天，放入days列表中
        while (tempStart.before(tempEnd)) {
            //当前周第一天开始,时间小于当前周最后一天的(结束）时间(转换格式后)放入days集合
            days.add(com.schedule.getmail.util.DateUtil.format(tempStart.getTime()));
            //日期+1
            tempStart.add(Calendar.DAY_OF_YEAR, 1);
        }
        List<TimeAxisPlanDataVo> timeAxisPlanDataVos = new ArrayList<>();
        for (String s : days) {
            TimeAxisPlanDataVo timeAxisPlanData = new TimeAxisPlanDataVo();
            List<PlanData> list = planDataMapper.selectList(new QueryWrapper<PlanData>().lambda()
                    //用户名条件
                    .eq(!StringUtils.isEmpty(username), PlanData::getUserName, username)
                    //开始时间条件
                    .apply("date_format (start_Time,'%Y-%m-%d') = date_format('" + s + "','%Y-%m-%d')")
                    //按开始时间升序排列
                    .orderByAsc(PlanData::getStartTime)
            );
            //返回单日日期
            timeAxisPlanData.setTime(s);
            //返回某天对应数据
            timeAxisPlanData.setData(list);
            //将日期与对应数据封装（循环结束时对应7天内的数据）
            timeAxisPlanDataVos.add(timeAxisPlanData);
        }
        log.info("planData selectByTimeRange startTime {},endTime {}", startTime, endTime);
//        List<PlanData> list = planDataMapper.selectByTimeRange(username,startTime,endTime);
//        List<PlanData> list = planDataMapper.selectList(new QueryWrapper<PlanData>().lambda()
//                .eq(!StringUtils.isEmpty(username), PlanData::getUserName,username)
//                .apply("date_format (start_Time,'%Y-%m-%d') >= date_format('" + startTime + "','%Y-%m-%d')")
//                .apply("date_format (start_Time,'%Y-%m-%d') <= date_format('" + endTime + "','%Y-%m-%d')")
//                .orderByAsc(PlanData::getStartTime)
//        );
        log.info("planData selectByTimeRange end! list.size {}", timeAxisPlanDataVos.size());
        return timeAxisPlanDataVos;
    }

    /**
     * 按月查询
     *
     * @param username
     * @return
     */
    @Override
    public List<PlanData> selectByMonthRange(String username) {
        log.info("planData selectByMonthRange start! userName {}", username);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        c.add(Calendar.MONTH, 0);
        c.set(Calendar.DAY_OF_MONTH, 1);
        String first = format.format(c.getTime());
        System.out.println("===============本月first day:" + first);
        //获取当前月最后一天
        Calendar ca = Calendar.getInstance();
        ca.set(Calendar.DAY_OF_MONTH, ca.getActualMaximum(Calendar.DAY_OF_MONTH));
        String last = format.format(ca.getTime());
        System.out.println("===============本月last day:" + last);
        log.info("planData selectByMonthRange first day {} last day {}", first, last);
        List<PlanData> list = planDataMapper.selectList(new QueryWrapper<PlanData>().lambda()
                .eq(!StringUtils.isEmpty(username), PlanData::getUserName, username)
                .ne(PlanData::getSource, "Message")
                .apply("date_format (start_Time,'%Y-%m-%d') >= date_format('" + first + "','%Y-%m-%d')")
                .apply("date_format (start_Time,'%Y-%m-%d') <= date_format('" + last + "','%Y-%m-%d')")
                .orderByAsc(PlanData::getStartTime)
        );
        log.info("planData selectByMonthRange end! list.size {}", list.size());
        return list;
    }

    /**
     * 新增、修改（日程表，热词表）
     *
     * @param request
     * @return
     */
    @Override
    public boolean saveOrUpdate(AddDailyPlanRequest request) {
        log.info("planData saveOrUpdate start! {}", request);
        int flag = 0;
        PlanData planData = new PlanData();
        log.info("planData saveOrUpdate planId {}", request.getPlanId());
        if (CheckUtil.isEmpty(request.getPlanId())) {
            BeanUtils.copyProperties(request, planData);
            //传入的时间为非完整格式yyyy-MM-dd HH:mm，转换到完整格式yyyy-MM-dd HH:mm:ss
            planData.setEndTime(com.schedule.getmail.util.DateUtil.fullParse(request.getEndTime()));
            planData.setStartTime(com.schedule.getmail.util.DateUtil.fullParse(request.getStartTime()));
            planData.setSource("0");
            planData.setCreateTime(new Timestamp(System.currentTimeMillis()));
            flag = planDataMapper.insert(planData);
            log.info("planDataMapper.insert flag {}", flag);
        } else {
            PlanData p = planDataMapper.selectById(request.getPlanId());
            log.info("planDataMapper.selectById PlanData {}", p);
            if (!CheckUtil.isEmpty(p)) {
                BeanUtils.copyProperties(request, p);
                p.setEndTime(com.schedule.getmail.util.DateUtil.fullParse(request.getEndTime()));
                p.setStartTime(com.schedule.getmail.util.DateUtil.fullParse(request.getStartTime()));
                p.setUpdateTime(new Timestamp(System.currentTimeMillis()));
                flag = planDataMapper.updateById(p);
                log.info("planDataMapper.updateById flag {}", flag);
            }
        }
        //对传入的主题进行分词
        List<Word> seg = Tokenizer.segment(request.getTitle());
        log.info("Tokenizer.segment {} ", seg);
        for (Word w : seg) {
            //挑选出n，vn，nx几种词性的词准备入库
            if ("n".equals(w.getPos()) || "vn".equals(w.getPos()) || "nx".equals(w.getPos())) {
                //根据分好的词查库
                TitleFrequency titleFrequency = titleFrequencyMapper.selectOne(new QueryWrapper<TitleFrequency>().lambda()
                        .eq(!StringUtils.isEmpty(w.getName()), TitleFrequency::getWords, w.getName())
                );
                log.info("titleFrequencyMapper.selectOne {} ", titleFrequency);
                //如果当前分词在表中为空则直接插入，词频为1
                if (CheckUtil.isEmpty(titleFrequency)) {
                    TitleFrequency t = new TitleFrequency();
                    t.setWords(w.getName());
                    t.setFrequency(1);
                    int flag2 = titleFrequencyMapper.insert(t);
                    log.info("titleFrequencyMapper.insert {} ", flag2);
                    //否则（已存在）则按id更新，词频+1
                } else {
                    titleFrequency.setFrequency(titleFrequency.getFrequency() + 1);
                    int flag3 = titleFrequencyMapper.updateById(titleFrequency);
                    log.info("titleFrequencyMapper.updateById {} ", flag3);
                }
            }
        }

        log.info("planData saveOrUpdate end! flag {}", flag > 0);
        return flag > 0;
    }

    /**
     * 根据热词查询
     *
     * @param words
     * @return
     */
    @Override
    public List<HotWordsPlanDataVo> selectPlanDataByHotWords(String words) {
        log.info("planData selectPlanDataByHotWords start! words {}", words);
        List<HotWordsPlanDataVo> list = new ArrayList<>();
        //根据热词比对主题模糊查询用户名，按用户名分组统计包含该热词（主题）的数量，按出现次数降序排列
        List<HotWordsVo> wordsList = planDataMapper.selectHotWordsByGroupByUserName(words);
        log.info("planData planDataMapper.selectHotWordsByGroupByUserName wordsList {}", wordsList);
        //如根据热词没有查到数据直接返回
        if (CheckUtil.isEmpty(wordsList)) {
            return list;
        }
        //根据热词以及userName查询日程
        for (HotWordsVo h : wordsList) {
            HotWordsPlanDataVo hotWordsPlanData = new HotWordsPlanDataVo();
            //获取wordsList中存放的用户名
            hotWordsPlanData.setUserName(h.getUserName());
            //获取wordsList中存放的用户数
            hotWordsPlanData.setSum(h.getTitleCount());
            //按wordsList中的用户名，及热词模糊查询主题所对应的日程，并按开始时间降序排列
            List<PlanData> planData = planDataMapper.selectList(new QueryWrapper<PlanData>().lambda()
                    .eq(PlanData::getUserName, h.getUserName())
                    .like(PlanData::getTitle, words)
                    .orderByDesc(PlanData::getStartTime)
            );
            hotWordsPlanData.setData(planData);
            list.add(hotWordsPlanData);
        }
        log.info("planData selectPlanDataByHotWords end! list {}", list);
        return list;
    }
}
