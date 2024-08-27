package com.student.servie;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.student.common.Result;
import com.student.dao.entity.Grade;
import com.student.dao.entity.StUser;
import com.student.dao.entity.StuAvgPoint;
import com.student.dao.entity.StuPoint;
import com.student.dao.service.IGradeService;
import com.student.dao.service.IStuAvgPointService;
import com.student.dao.service.IStuPointService;
import com.student.utils.MainUtils;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class GradeService {

    @Autowired
    private IGradeService gradeService;
    @Autowired
    private IStuPointService pointService;

    @Autowired
    private IStuAvgPointService avgPointService;

    //折现图x轴下标
    private static final  String [] XList={"大一上","大一下","大二上","大二下","大三上","大三下","大四上","大四下"};


    /**
     * 获取个人学年平均绩点
     * @return s/f
     */
    public StuPoint getPoint(String stuYear){
        StUser user = (StUser) SecurityUtils.getSubject().getPrincipal();
        return pointService.getOne(new QueryWrapper<StuPoint>().eq("stu_id",user.getId()).eq("stu_year",stuYear));
    }

    /**
     * 获取个人总平均绩点
     * @return s/f
     */
    public Result getPointInStuYear(){
        StUser user = (StUser) SecurityUtils.getSubject().getPrincipal();
        return Result.succ(avgPointService.getOne(new QueryWrapper<StuAvgPoint>().eq("stu_id",user.getId())));
    }

    /**
     * 获取专业总平均绩点前50
     * @return s/f
     */
    public Result getAvgPointLimit(String majorName){
        StUser user = (StUser) SecurityUtils.getSubject().getPrincipal();
        return Result.succ(avgPointService.list(new QueryWrapper<StuAvgPoint>()
                .eq("major_name", majorName)
                .eq("stu_day", user.getStuDay())
                .last("limit 50")));
    }

    /**
     * 按照学年获取个人成绩
     * @return s/f
     */
    public Result getGrade(String stuYear){
        StUser user = (StUser) SecurityUtils.getSubject().getPrincipal();
        return Result.succ(gradeService.list(new QueryWrapper<Grade>().eq("stu_id",user.getId()).eq("stu_year",stuYear)));
    }

    /**
     * 查询指定课程指定学年前50名
     * @param couId 课程id
     * @param stuYear 学年
     * @return s/f
     */
    public Result getGradePoint(int couId, String stuYear) {
        StUser user = (StUser) SecurityUtils.getSubject().getPrincipal();
        return Result.succ(gradeService.list(new QueryWrapper<Grade>().eq("cou_id", couId)
                .eq("stu_day", user.getStuDay())
                .eq("stu_year", stuYear).last("limit 50")));
    }

    /**
     * 获取学生总学分
     * @return s/f
     */
    public Result getSumGrade(){
        double sum=0.0;
        StUser user = (StUser) SecurityUtils.getSubject().getPrincipal();
        List<Grade> list = gradeService.list(new QueryWrapper<Grade>().eq("stu_id", user.getId()));
        for(Grade grade:list) {
            sum += grade.getCouGrade();
        }
        return Result.succ(sum);
    }

    /**
     * 构造主页绩点折线图数据
     * type:0：绩点，1：排名
     */
    public Result initLineChartPoint(int type){
        Map<String,Object> res=new HashMap<>(2);
        List<String> xData=new LinkedList<>();
        List<Object> yData=new LinkedList<>();
        List<String> list = MainUtils.initStuYearNum();
        int num=0;
        for(String stuYear:list){
            xData.add(XList[num++]);
            if(type==0){
                yData.add(Optional.ofNullable(getPoint(stuYear))
                        .map(StuPoint::getAvgGpa)
                        .orElse(0.0));
            }else {
                yData.add(Optional.ofNullable(getPoint(stuYear))
                        .map(StuPoint::getRanking)
                        .orElse(0L));
            }
        }
        res.put("xData",xData);
        res.put("yData",yData);
        return Result.succ(res);
    }


}
