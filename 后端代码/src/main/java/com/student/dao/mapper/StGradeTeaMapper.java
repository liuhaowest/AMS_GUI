package com.student.dao.mapper;

import com.student.dao.entity.StCourse;
import com.student.dao.entity.StGradeTea;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author liHeWei
 * @since 2022-05-03
 */
@Mapper
public interface StGradeTeaMapper extends BaseMapper<StGradeTea> {
    /**
     * 查询教师已选的该专业的课程id
     * @param teaId 教师id
     * @param major 专业名称
     * @return List<StGradeTea>
     */
    List<StGradeTea> getCourseByMajor(Long teaId,String major);

    List<StCourse> getRebuildCourse(Long stuId);
 }
