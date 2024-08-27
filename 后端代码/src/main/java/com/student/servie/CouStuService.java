 package com.student.servie;

import com.student.common.Result;

import java.time.LocalDateTime;

/**
 * 学生-课堂
 */
public interface CouStuService{

    /**
     * 分页查询全部课堂(当前学生未绑定)
     * @param current 当前页
     * @param id 当前选课任务id
     * @return list
     */
    Result getRoomList(int current,int id);

    /**
     * 根据课堂码查询课堂
     * @param classKey 课堂码
     * @return StClassRoom
     */
    Result getRoomByKey(String classKey,int id);

    /**
     * 绑定课堂
     * @param roomId 课堂id
     * @param type 0:普通，1：重修
     * @return sc/fu
     */
    Result bingRoom(Long roomId,int type);

    /**
     * 初始化学生已选课程的菜单
     * @return list
     */
    Result menuInit();

    /**
     * 根据学年获取学生已选课堂
     * @param year 学年
     * @param couType 类型0：普通，1:重修
     * @return list
     */
    Result getRoomsByYear(String year,int couType);

    /**
     * 退出课堂
     * @param roomId 课堂id
     * @return sc/fu
     */
    Result exitRoom(Long roomId,int couType);

    boolean isBetweenBingCourse(String startTime, String endTime, LocalDateTime now);
}
