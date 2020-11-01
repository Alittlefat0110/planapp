package com.schedule.getmail.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.schedule.getmail.bean.request.AddEmailConfigRequest;
import com.schedule.getmail.contentSimilarity.tokenizer.Tokenizer;
import com.schedule.getmail.contentSimilarity.tokenizer.Word;
import com.schedule.getmail.entity.EmailConfig;
import com.schedule.getmail.entity.PlanData;
import com.schedule.getmail.entity.TitleFrequency;
import com.schedule.getmail.mapper.ConferenceDataMapper;
import com.schedule.getmail.mapper.EmailConfigMapper;
import com.schedule.getmail.mapper.PlanDataMapper;
import com.schedule.getmail.mapper.TitleFrequencyMapper;
import com.schedule.getmail.service.ITitleFrequencyService;
import com.schedule.getmail.util.CheckUtil;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.*;

/**
 * <p>
 * 日程主题名词词频统计表 服务实现类
 * </p>
 *
 * @author StrTom
 * @since 2020-10-28
 */
@Service
public class TitleFrequencyServiceImpl extends ServiceImpl<TitleFrequencyMapper, TitleFrequency> implements ITitleFrequencyService {
    @Resource
    private TitleFrequencyMapper titleFrequencyMapper;
    @Resource
    private PlanDataMapper planDataMapper;

    @Override
    public void saveOrUpdate() {
		//建立查询
		List<PlanData> planDataList = planDataMapper.selectList(new QueryWrapper<PlanData>());
		Map<Word, Integer> tm =new HashMap<>();
		//对每个主题中的名词进行分词，并统计出现频数，封装到一个map中
		for (int i=0;i<planDataList.size();i++){
			//对主题进行分词
			List<Word> seg = Tokenizer.segment(planDataList.get(i).getTitle());
			//将分词封装到map中，并累计各词出现频数
			for (int x = 0; x < seg.size(); x++) {
				//若词性为名词则纳入累计
				if(seg.get(x).toString().endsWith("/n")) {
					if (!tm.containsKey(seg.get(x))) {
						tm.put(seg.get(x), 1);
					} else {
						//统计词频
						int count = tm.get(seg.get(x)) + 1;
						tm.put(seg.get(x), count);
					}
				}
			}
		}
		//将带词性后缀的中文分词取出纯中文
		List<Map.Entry<Word,Integer>> list = new ArrayList(tm.entrySet());
		Collections.sort(list, (o1, o2) -> (o1.getValue() - o2.getValue()));
		String reg = "[^\u4e00-\u9fa5]";

		//将词频统计插入表中
		TitleFrequency titleFrequency=new TitleFrequency();
		for(int k=0;k<tm.size();k++) {
			//去除热词带有的空字符
			String word = list.get(k).getKey().toString().replaceAll(reg,"");
			Integer frequency=list.get(k).getValue();
			TitleFrequency titleFrequency1 =  titleFrequencyMapper.selectOne(new QueryWrapper<TitleFrequency>().lambda()
					.eq(!StringUtils.isEmpty(word), TitleFrequency::getWords,word));

			if(CheckUtil.isEmpty(titleFrequency1)){
				//插入热词
				titleFrequency.setWords(word);
				//插入热词词频
				titleFrequency.setFrequency(frequency);
				titleFrequencyMapper.insert(titleFrequency);
			}else {
				//若热词已存在，则更新热词词频
				titleFrequencyMapper.updateById(titleFrequency1);
				UpdateWrapper<TitleFrequency> updateWrapper = new UpdateWrapper<>();
				//以热词作为条件跟新
				updateWrapper.eq("words",word);
				//更新热词词频
				titleFrequency.setFrequency(frequency);
				titleFrequencyMapper.update(titleFrequency, updateWrapper);
			}
		}
	}


}
