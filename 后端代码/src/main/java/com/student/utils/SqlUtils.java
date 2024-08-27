package com.student.utils;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.crypto.digest.DigestUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.student.dao.entity.*;
import com.student.dao.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

@Service
public class SqlUtils {

    @Autowired
    private IStUserService userService;
    @Autowired
    private IStGradeStuService gradeStuService;
    @Autowired
    private IStGradeTeaService gradeTeaService;
    @Autowired
    private IStClassRoomService classRoomService;
    @Autowired
    private IStReGradeStuService reGradeStuService;
    private static final Random random = new Random();

    // 生成一个由两个或三个中文汉字组成的随机姓名
    public String generate() {
        StringBuilder sb = new StringBuilder();
        // 第一个汉字
        sb.append(getRandomChineseChar());
        // 第二个汉字
        sb.append(getRandomChineseChar());
        // 有一半的概率添加第三个汉字
        if (random.nextBoolean()) {
            sb.append(getRandomChineseChar());
        }
        return sb.toString();
    }

    // 生成一个随机的中文汉字
    private char getRandomChineseChar() {
        char[] chars = new char[]{'赵', '徐', '李', '凯', '明', '鑫', '伟', '航', '京', '斯', '思', '河', '琪', '睿', '杨', '浩', '鹏', '叶', '苏', '舒'};
        // 生成一个Unicode码，该码在中文汉字的范围内
        return chars[RandomUtil.randomInt(chars.length)];
    }

    public void test1() {
        for(long i=96;i<=125;i++){
            for(long j=64;j<=68;j+=2){
                StGradeStu stu = new StGradeStu();
                stu.setStuId(i);
                stu.setRoomId(j);
                stu.setGrade(RandomUtil.randomInt(57,97));
                stu.setPoint(stu.getGrade()>=60? stu.getGrade()/10.0-5:0);
                gradeStuService.save(stu);
            }
        }
    }
    public void test2(){
        long num=49;
        for(int i=35;i<=48;i++){
            StClassRoom classRoom = classRoomService.getById(i);
            classRoom.setClassDesc("软件19级2班");
            String classKey= RandomUtil.randomString(5).toUpperCase();
            classRoom.setClassKey(classKey);
            classRoom.setId(num++);
            classRoomService.save(classRoom);
            Long id = classRoom.getId();
            List<StGradeTea> list = gradeTeaService.list(new QueryWrapper<StGradeTea>().eq("class_room_id", i));
            Long teaId = list.get(0).getTeaId();
            StGradeTea stGradeTea = new StGradeTea();
            stGradeTea.setTeaId(teaId);
            stGradeTea.setCouId(list.get(0).getCouId());
            stGradeTea.setClassRoomId(id);
            gradeTeaService.save(stGradeTea);
        }
    }

    public void test3(){
        List<StGradeStu> list = gradeStuService.list(new QueryWrapper<StGradeStu>().lt("grade", 60));
        Map<Long,Long>map=new HashMap<>();
        long id=69;
        for(StGradeStu gradeStu:list){
            StGradeTea stGradeTea = gradeTeaService.getOne(new QueryWrapper<StGradeTea>().eq("class_room_id", gradeStu.getRoomId()));
            StClassRoom classRoom = classRoomService.getById(gradeStu.getRoomId());
            long couId=stGradeTea.getCouId();
            long teaId= stGradeTea.getTeaId();
            long stuId=gradeStu.getStuId();
            if(map.containsKey(couId)){
                StReGradeStu stu = new StReGradeStu();
                stu.setRoomId(map.get(couId));
                stu.setStuId(stuId);
                stu.setGrade(RandomUtil.randomInt(78,92));
                stu.setPoint(stu.getGrade()>=60? stu.getGrade()/10.0-5:0);
                reGradeStuService.save(stu);
            }else {
                map.put(couId,id);
                classRoom.setId(id);
                classRoom.setClassDesc("软件20级");
                String classKey= RandomUtil.randomString(5).toUpperCase();
                classRoom.setClassKey(classKey);
                String[] split = classRoom.getStuYear().split(",");
                String year=String.valueOf(Integer.parseInt(split[0])+1);
                classRoom.setStuYear(year+","+split[1]);
                classRoomService.save(classRoom);

                StGradeTea stGradeTeaNew = new StGradeTea();
                stGradeTeaNew.setTeaId(teaId);
                stGradeTeaNew.setCouId(couId);
                stGradeTeaNew.setClassRoomId(id);
                gradeTeaService.save(stGradeTeaNew);
                StGradeTea stGradeTeaBing = new StGradeTea();
                stGradeTeaBing.setTeaId(teaId);
                stGradeTeaBing.setCouId(couId);
                gradeTeaService.save(stGradeTeaBing);

                StReGradeStu stu = new StReGradeStu();
                stu.setRoomId(id);
                stu.setStuId(stuId);
                stu.setGrade(RandomUtil.randomInt(78,92));
                stu.setPoint(stu.getGrade()>=60? stu.getGrade()/10.0-5:0);
                reGradeStuService.save(stu);
            }
            id++;
        }
    }

    public void test4(){
        List<StGradeStu> list = gradeStuService.list();
        for(StGradeStu gradeStu :list){
            Long couId = gradeTeaService.getOne(new QueryWrapper<StGradeTea>().eq("class_room_id", gradeStu.getRoomId())).getCouId();
            gradeStu.setCouId(couId);
            gradeStuService.update(gradeStu,new QueryWrapper<StGradeStu>().eq("stu_id",gradeStu.getStuId()).eq("room_id",gradeStu.getRoomId()));
        }

        List<StReGradeStu> reList = reGradeStuService.list();
        for(StReGradeStu gradeStu :reList){
            Long couId = gradeTeaService.getOne(new QueryWrapper<StGradeTea>().eq("class_room_id", gradeStu.getRoomId())).getCouId();
            gradeStu.setCouId(couId);
            reGradeStuService.update(gradeStu,new QueryWrapper<StReGradeStu>().eq("stu_id",gradeStu.getStuId()).eq("room_id",gradeStu.getRoomId()));
        }

    }

    public void test() {
        String[] sex = new String[]{"男", "女"};
        int num = 410;
        for (long i = 96; i < 126; i++) {
            StUser user = userService.getById(67);
            user.setId(i);
            user.setUsername("311909203" + num++);
            user.setIdCard("4115282001" + RandomUtil.randomNumbers(8));
            user.setPhoneNum("158" + RandomUtil.randomNumbers(8));
            String generate = "";
            int sum = -1;
            while (true) {
                generate = generate();
                sum = userService.list(new QueryWrapper<StUser>().eq("real_name", generate)).size();
                if (sum == 0) {
                    break;
                }
            }
            user.setRealName(generate);
            user.setEmail("3235" + RandomUtil.randomNumbers(6) + "@qq.com");
            String salt = RandomUtil.randomString(5);
            String password = DigestUtil.md5Hex(user.getIdCard().substring(user.getIdCard().length() - 6) + salt);
            user.setPassword(password);
            user.setMajorClassId(2L);
            user.setSalt(salt);
            user.setSex(sex[RandomUtil.randomInt(0, 1)]);
            user.setDormId(RandomUtil.randomLong(1, 9));
            userService.save(user);
        }


    }
}
