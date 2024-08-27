package com.student.dao.mapper;

import com.student.dao.entity.StCourse;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author liHeWei
 * @since 2022-04-29
 */
@Mapper
public interface StCourseMapper extends BaseMapper<StCourse> {
//    /**
//     * 根据学年，专业，查询学生未选择的课
//     * @param year 学年
//     * @param major 专业
//     * @param userId 用户id
//     * @return list<StCourse>
//     */
//    List<StCourse> getCourse(String year,String major,Long userId);

    /**
     * 查询课堂所属的课程信息
     * @param roomId 课堂id
     * @return stCourse
     */
    StCourse getCourse(Long roomId);
}
