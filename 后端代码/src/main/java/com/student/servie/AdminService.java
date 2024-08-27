package com.student.servie;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.crypto.digest.DigestUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.student.common.Result;
import com.student.dao.entity.*;
import com.student.dao.service.*;
import com.student.vo.VoUser;
import com.sun.org.apache.bcel.internal.generic.RET;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class AdminService {

    @Autowired
    private IStUserService userService;
    @Autowired
    private IStDormService dormService;
    @Autowired
    private IStRoleService roleService;
    @Autowired
    private IStGradeTeaService gradeTeaService;
    @Autowired
    private IStGradeStuService gradeStuService;
    @Autowired
    private LoginService loginService;
    @Autowired
    private IStBingCourseService bingCourseService;

    //统一页条数
    private static final int pageSize=10;


    public Result getUserList(int current,long userType,StUser stUser){
        IPage<StUser> page=new Page<>(current,pageSize);
        IPage<StUser> temPage = null;
        if(stUser!=null){
            stUser.setRoleId(userType);
            temPage = userService.page(page, new QueryWrapper<>(stUser));
        }else {
            temPage = userService.page(page, new QueryWrapper<StUser>().eq("role_id", userType));
        }
        if(userType==1&&!CollectionUtils.isEmpty(temPage.getRecords())){
            return Result.succ(convertToVoUser(temPage));
        }
        return Result.succ(temPage);
    }

    //voUser转换
    public  IPage<VoUser> convertToVoUser(IPage<StUser> page){
        IPage<VoUser> resultPage=new Page<>(page.getCurrent(),pageSize);
        resultPage.setTotal(page.getTotal());
        List<VoUser> voUserList = page.getRecords().stream().map(item -> loginService.convertUser(item)).collect(Collectors.toList());
        resultPage.setRecords(voUserList);
        return resultPage;
    }



    public Result getDormList(int current, StDorm stDorm){
        IPage<StDorm> page=new Page<>(current,pageSize);
        if(stDorm!=null){
            return Result.succ(dormService.page(page,new QueryWrapper<>(stDorm)));
        }else {
            return Result.succ(dormService.page(page));
        }
    }

    public Result delDorm(long dormId){
        if(userService.list(new QueryWrapper<StUser>().eq("dorm_id",dormId)).size()==0){
            if(dormService.removeById(dormId)){
                return Result.succ(null);
            }else return Result.fail("删除失败");
        }else return Result.fail("任有学生属于该宿舍");
    }

    public Result delTea(long teaId){
        if(gradeTeaService.list(new QueryWrapper<StGradeTea>().eq("tea_id",teaId)).size()==0){
            userService.removeById(teaId);
            return Result.succ(null);
        }else {
            return Result.fail("删除失败，该教师任有绑定课程");
        }
    }

    public Result addBingCourse(StBingCourse stBingCourse){
        //校验
        List<StBingCourse> stBingCourseList = bingCourseService.list(new QueryWrapper<StBingCourse>().eq("stu_year", stBingCourse.getStuYear()));
        if(stBingCourseList.size()>0&&(stBingCourse.getType()==0||stBingCourse.getType()==1)){
            return Result.fail("同一学年的普通和重修选课任务在系统中只能同时存在一个");
        }
        if(bingCourseService.save(stBingCourse)){
            return Result.succ(null);
        }else {
            return Result.fail("选课任务开启失败");
        }
    }

    public Result delBingCourse(StBingCourse stBingCourse){
        if(bingCourseService.removeById(stBingCourse.getId())){
            return Result.succ(null);
        }else {
            return Result.fail("删除失败");
        }
    }

    public Result getBingCourseList(){
        return Result.succ(bingCourseService.list());
    }

    public Result delStu(long stuId){
        if(gradeStuService.list(new QueryWrapper<StGradeStu>().eq("stu_id",stuId)).size()==0){
            userService.removeById(stuId);
            return Result.succ(null);
        }else {
            return Result.fail("删除失败，该学生任有绑定课程");
        }
    }

    public Result updateTea(StUser stUser) {
        if(isRepeat(stUser)){
            if(stUser.getId()==null){
                initPassword(stUser);
            }
            //赋予教师角色
            Long roleId= roleService.getOne(new QueryWrapper<StRole>().eq("role","teacher")).getId();
            stUser.setRoleId(roleId);
            userService.saveOrUpdate(stUser);
            return Result.succ(null);
        }else {
            return Result.fail("操作失败，存在重复信息");
        }
    }

    public Result updateStu(VoUser voUser){
        StUser stUser = new StUser();
        //对象转换
        BeanUtils.copyProperties(voUser,stUser);
        if(voUser.getMajorClass()!=null&&voUser.getMajorClass()[0]!=null){
            stUser.setMajorClassId(Long.valueOf(voUser.getMajorClass()[1].toString()));
            stUser.setMajorId((Long.valueOf(voUser.getMajorClass()[0].toString())));
        }
        if(voUser.getDorm()!=null&&!voUser.getDorm()[1].equals("待完善")){
            stUser.setDormId((Long.valueOf(voUser.getDorm()[1].toString())));
        }
        if(isRepeat(stUser)){
            if(stUser.getId()==null){
                initPassword(stUser);
            }
            //赋予学生角色
            Long roleId= roleService.getOne(new QueryWrapper<StRole>().eq("role","student")).getId();
            stUser.setRoleId(roleId);
            userService.saveOrUpdate(stUser);
            return Result.succ(null);
        }else {
            return Result.fail("操作失败，存在重复信息");
        }
    }

    //信息避重
    public boolean isRepeat(StUser stUser){
        QueryWrapper<StUser> query = new QueryWrapper<>();
        query.or(wrapper->{
            wrapper.eq("username", stUser.getUsername());
            if(stUser.getEmail()!=null&&!stUser.getEmail().equals("待完善")){
                wrapper.or().eq("email", stUser.getEmail());
            }
            if(stUser.getIdCard()!=null&&!stUser.getIdCard().equals("待完善")){
                wrapper.or().eq("email", stUser.getEmail());
            }
            if(stUser.getPhoneNum()!=null&&!stUser.getPhoneNum().equals("待完善")){
                wrapper.or().eq("email", stUser.getEmail());
            }
        });
        if(stUser.getId()!=null){
            query.ne("id",stUser.getId());
        }
        return userService.list(query).size()==0;
    }

    //新用户初始密码,默认为身份证后六位
    public void initPassword(StUser stUser){
        String salt= RandomUtil.randomString(5);
        String password = DigestUtil.md5Hex(stUser.getIdCard().substring(stUser.getIdCard().length()-6)+salt);
        stUser.setPassword(password);
        stUser.setSalt(salt);
    }

    public Result updateDorm(StDorm stDorm){
        Map<String,String> map =new HashMap<>(2);
        map.put("dorm_num",stDorm.getDormNum());
        map.put("dorm_address",stDorm.getDormAddress());
        if(dormService.list(new QueryWrapper<StDorm>().allEq(map)).size()==0){
            dormService.saveOrUpdate(stDorm);
            return Result.succ(null);
        }else return Result.fail("已存在"+stDorm.getDormAddress()+ stDorm.getDormNum());
    }
}
