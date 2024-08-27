package com.student.dao.mapper;

import com.student.dao.entity.StGradeStu;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.student.dao.entity.StUser;
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
public interface StGradeStuMapper extends BaseMapper<StGradeStu> {

    /**
     * 获取选择当前课堂的学生信息
     * @param roomId 课堂id
     * @return list
     */
    List<StUser> getStuList(Long roomId);
}
