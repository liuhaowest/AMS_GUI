package com.student.servie;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.student.common.Result;
import com.student.dao.entity.*;
import com.student.dao.mapper.StGradeTeaMapper;
import com.student.dao.service.IGradeService;
import com.student.dao.service.IStuAvgPointService;
import com.student.dao.service.IStuPointService;
import com.student.utils.MainUtils;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;

@Service
public class ScoreAnalysisService {

    @Autowired
    private IStuAvgPointService avgPointService;
    @Autowired
    private IGradeService gradeService;
    @Autowired
    private IStuPointService pointService;
    @Autowired
    private StGradeTeaMapper gradeTeaMapper;


    /**
     * 获取学分和绩点
     */
    public Result getRankAndGpa(){
        Map<String, Object> resultMap = new HashMap<>(4);
        StUser user = (StUser) SecurityUtils.getSubject().getPrincipal();
        StuAvgPoint stuAvgPoint = avgPointService.getOne(new QueryWrapper<StuAvgPoint>().eq("stu_id", user.getId()));
        long rankSum = avgPointService.count();
        double sumGrade=0.0;
        List<Grade> list = gradeService.list(new QueryWrapper<Grade>().eq("stu_id", user.getId()));
        for(Grade grade:list) {
            sumGrade += grade.getCouGrade();
        }
        if(stuAvgPoint==null){
            resultMap.put("gpa","暂无数据");
            resultMap.put("rank","暂无数据");
        }else {
            resultMap.put("gpa",stuAvgPoint.getAvgGpa());
            resultMap.put("sumGpa",5);
            resultMap.put("rank",stuAvgPoint.getRanking());
            resultMap.put("rankSum",rankSum);
            resultMap.put("sumGrade",sumGrade);
        }
        return Result.succ(resultMap);
    }

    /**
     * 获取在校成绩区间
     */
    public Result getGradeInterval(){
        int [] resultArr=new int[5];
        StUser user = (StUser) SecurityUtils.getSubject().getPrincipal();
        List<Grade> list = gradeService.list(new QueryWrapper<Grade>().eq("stu_id", user.getId()));
        for(Grade grade:list) {
           int temGrade=grade.getGrade();
           if(temGrade<60){
                resultArr[0]++;
           } else if (temGrade < 70) {
               resultArr[1]++;
           } else if (temGrade < 80) {
               resultArr[2]++;
           } else if (temGrade < 90) {
               resultArr[3]++;
           }else {
               resultArr[4]++;
           }
        }
        return Result.succ(resultArr);

    }

    /**
     * 获取在校总成绩分析
     */
    public Result getScoreAnalysis(){
        StUser user = (StUser) SecurityUtils.getSubject().getPrincipal();
        List<String> maxCourse=new LinkedList<>();
        List<String> minCourse=new LinkedList<>();
        Map<String,Object> resultMap=new HashMap<>(6);
        resultMap.put("couData",gradeTeaMapper.getRebuildCourse(user.getId()));
        List<Grade> gradeList = gradeService.list(new QueryWrapper<Grade>()
                .eq("stu_id", user.getId())
                .orderByAsc("grade"));
        if(gradeList.size()==0){
            return Result.succ(resultMap);
        }
        int min=gradeList.get(0).getGrade();
        int max=gradeList.get(gradeList.size()-1).getGrade();
        int sumGrade=0;
        for(Grade grade:gradeList){
            if(grade.getGrade()==max){
                maxCourse.add(grade.getCouName());
            }
            if(grade.getGrade()==min){
                minCourse.add(grade.getCouName());
            }
            sumGrade+=grade.getGrade();
        }
        resultMap.put("maxCourse",maxCourse);
        resultMap.put("minCourse",minCourse);
        resultMap.put("maxGrade",max);
        resultMap.put("minGrade",min);
        resultMap.put("avgGrade",sumGrade/gradeList.size());
        return Result.succ(resultMap);
    }

    /**
     * 获取学期学分绩点排名
     */
    public Result getRankAndGpaByYear(){
        List<Map<String,Object>> resultList=new LinkedList<>();
        StUser user = (StUser) SecurityUtils.getSubject().getPrincipal();
        List<String> list = MainUtils.initStuYearNum();
        List<String> listYear = MainUtils.initStuYear();
        Collections.reverse(listYear);
        int num=0;
        for(String stuYear:list){
            Map<String,Object> map=new HashMap<>(3);
            map.put("avgGpa", Optional.ofNullable(getPoint(stuYear))
                    .map(StuPoint::getAvgGpa)
                    .orElse(0.0));
            map.put("rank",Optional.ofNullable(getPoint(stuYear))
                    .map(StuPoint::getRanking)
                    .orElse(0L));
            double sumGrade = gradeService.list(new QueryWrapper<Grade>()
                            .eq("stu_id", user.getId())
                            .eq("stu_year", stuYear)).stream()
                    .mapToDouble(Grade::getCouGrade)
                    .sum();
            map.put("sumGrade",sumGrade);
            map.put("stuYear",listYear.get(num++));
            resultList.add(map);
        }
        return Result.succ(resultList);
    }

    /**
     *获取指定类别的课程成绩
     */
    public Result getCourseByType(int type){
        Map<String,Object> map=new HashMap<>(5);
        StUser user = (StUser) SecurityUtils.getSubject().getPrincipal();
        List<Grade> gradeList = gradeService.list(new QueryWrapper<Grade>().eq("stu_id", user.getId()).eq("cou_type", type).orderByAsc("grade"));
        if(gradeList.size()==0){
            return Result.succ(map);
        }
        map.put("couData",gradeList);
        Grade minCourse = gradeList.get(0);
        Grade maxCourse = gradeList.get(gradeList.size()-1);
        map.put("minCourse",minCourse);
        map.put("maxCourse",maxCourse);
        map.put("minAvg",getAvgCouGrade(minCourse.getCouId(), minCourse.getStuYear()));
        map.put("maxAvg",getAvgCouGrade(maxCourse.getCouId(), maxCourse.getStuYear()));
        return Result.succ(map);
    }

    /**
     *获取指定课程的平均分
     */
    public double getAvgCouGrade(long couId,String stuYear){
        List<Grade> gradeList = gradeService.list(new QueryWrapper<Grade>().eq("cou_id", couId).eq("stu_year", stuYear));
        return gradeList.stream()
                .mapToInt(Grade::getGrade)
                .average()
                .orElse(0.0);
    }

    public StuPoint getPoint(String stuYear){
        StUser user = (StUser) SecurityUtils.getSubject().getPrincipal();
        return pointService.getOne(new QueryWrapper<StuPoint>().eq("stu_id",user.getId()).eq("stu_year",stuYear));
    }
}
