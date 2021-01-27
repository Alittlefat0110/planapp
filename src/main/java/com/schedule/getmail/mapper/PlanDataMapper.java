package com.schedule.getmail.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.schedule.getmail.entity.PlanData;
import com.schedule.getmail.entity.vo.HotWordsVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 * 日程数据表 Mapper 接口
 * </p>
 *
 * @author StrTom
 * @since 2020-10-28
 */
@Mapper
public interface PlanDataMapper extends BaseMapper<PlanData> {

    /**
     * 根据热词查询
     *
     * @param words
     * @return
     */
    //根据热词比对主题模糊查询用户名，按用户名分组统计包含该热词（主题）的数量，按出现次数降序排列
    @Select("SELECT t.user_name, count( t.user_name ) AS titleCount  FROM plan_data t  WHERE t.title LIKE '%${words}%' GROUP BY t.user_name  ORDER BY titleCount DESC")
    List<HotWordsVo> selectHotWordsByGroupByUserName(@Param("words") String words);

}
