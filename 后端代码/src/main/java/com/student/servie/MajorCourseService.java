package com.student.servie;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.student.common.Result;
import com.student.dao.entity.StCourse;
import com.student.dao.entity.StGradeStu;
import com.student.dao.entity.StMajor;
import com.student.dao.entity.StReGradeStu;
import com.student.dao.service.IStCourseService;
import com.student.dao.service.IStGradeStuService;
import com.student.dao.service.IStMajorService;
import com.student.dao.service.IStReGradeStuService;
import com.student.vo.VoCourse;
import com.student.vo.VoReview;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Service
public class MajorCourseService {

    @Autowired
    private IStCourseService courseService;
    @Autowired
    private IStMajorService majorService;
    @Autowired
    private IStGradeStuService gradeStuService;
    @Autowired
    private IStReGradeStuService reGradeStuService;

    public Result getCourseByMajor(String majorName) {
        List<StCourse> stCourseList = courseService.list(new QueryWrapper<StCourse>().eq("major", majorName));
        List<VoCourse> resultList=new LinkedList<>();
        for(StCourse course:stCourseList){
            VoCourse voCourse = new VoCourse();
            BeanUtils.copyProperties(course,voCourse);
            if(course.getCouType()!=null){
                voCourse.setCouType(course.getCouType().getCouType());
            }
            resultList.add(voCourse);
        }
        return Result.succ(resultList);
    }

    public Result addMajor(String majorName) {
        List<StMajor> majorList = majorService.list(new QueryWrapper<StMajor>().eq("name", majorName));
        if (majorList.size() == 0) {
            majorService.save(StMajor.builder().name(majorName).build());
            return Result.succ(majorService.list());
        } else {
            return Result.fail("专业名称已存在");
        }
    }

    public Result delMajor(String majorName) {
        if(courseService.list(new QueryWrapper<StCourse>().eq("major",majorName)).size()!=0){
            return Result.fail("删除失败，该专业下仍有绑定课程");
        }else {
            boolean removeMajor = majorService.remove(new QueryWrapper<StMajor>().eq("name", majorName));
            if(removeMajor){
                return Result.succ(majorService.list());
            }else return Result.fail("删除失败，请检查专业名称");
        }
    }

    public Result delCourse(int couId){
        boolean isExist=gradeStuService.list(new QueryWrapper<StGradeStu>().eq("cou_id",couId)).size()==0;
        boolean isReExist=reGradeStuService.list(new QueryWrapper<StReGradeStu>().eq("cou_id",couId)).size()==0;
        if(isExist&&isReExist){
            if(courseService.removeById(couId)){
                return Result.succ(null);
            }else return Result.fail("删除失败");
        }else {
            return Result.fail("该课程下仍有绑定的学生");
        }
    }

    public Result getCourseById(int couId){
        return Result.succ(courseService.getById(couId));
    }

    public Result addCourse(StCourse stCourse){
        QueryWrapper<StCourse> queryWrapper=new QueryWrapper<StCourse>()
                .eq("major",stCourse.getMajor())
                .eq("cou_name",stCourse.getCouName());
        if(stCourse.getId()!=null){
            queryWrapper.ne("id",stCourse.getId());
        }
        if(courseService.list(queryWrapper).size()!=0){
            return Result.fail("该专业中已存在同名课程");
        }
        if(courseService.saveOrUpdate(stCourse)){
            return Result.succ(null);
        }else return Result.fail("操作失败");
    }
}
