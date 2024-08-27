package com.student.servie;

import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.student.common.Result;
import com.student.dao.entity.StClass;
import com.student.dao.entity.StDorm;
import com.student.dao.entity.StMajor;
import com.student.dao.entity.StUser;
import com.student.dao.service.IStClassService;
import com.student.dao.service.IStDormService;
import com.student.dao.service.IStMajorService;
import com.student.dao.service.IStUserService;
import com.student.vo.VoElement;
import com.student.vo.VoUser;
import org.apache.commons.lang3.text.StrTokenizer;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @author 李123
 */
@Service
public class StudentService {
    //头像图片上传地址
    @Value("${stuImgFile}")
    private String imgFile;

    @Autowired
    private IStMajorService majorService;
    @Autowired
    private IStClassService classService;
    @Autowired
    private IStDormService dormService;
    @Autowired
    private IStUserService userService;
    @Autowired
    private AdminService adminService;


    /**
     * 构造全部专业班级数据结构
     */
    public Result getMajorClass() {
        LinkedList<VoElement<Long>> resList = new LinkedList<>();
        List<StMajor> majorList = majorService.list();
        for (StMajor major : majorList) {
            VoElement<Long> tem = new VoElement<>();
            LinkedList<VoElement<Long>> temList = new LinkedList<>();
            List<StClass> classList = classService.list(new QueryWrapper<StClass>().eq("major_id", major.getId()));
            for (StClass stClass : classList) {
                VoElement<Long> temVo = new VoElement<>();
                temVo.setLabel(stClass.getName());
                temVo.setValue(stClass.getId());
                temVo.setChildren(null);
                temList.add(temVo);
            }
            tem.setChildren(temList);
            tem.setValue(major.getId());
            tem.setLabel(major.getName());
            resList.add(tem);
        }
        return Result.succ(resList);
    }

    /**
     * 构造全部宿舍数据
     */
    public Result getDorm() {
        LinkedList<VoElement<Long>> resList = new LinkedList<>();
        List<StDorm> dormList = dormService.list();
        Map<String, LinkedList<VoElement<Long>>> map = new HashMap<>();
        for (StDorm dorm : dormList) {
            if (!map.containsKey(dorm.getDormAddress())) {
                map.put(dorm.getDormAddress(), new LinkedList<>());
            } else {
                VoElement<Long> tem = new VoElement<>(dorm.getId(), dorm.getDormNum(), null);
                map.get(dorm.getDormAddress()).add(tem);
            }
        }
        for(String key:map.keySet()) {
            resList.add(new VoElement<>(0L, key, map.get(key)));
        }
        return Result.succ(resList);
    }

    /**
     * 更新学生信息
     */
    public Result updateStu(VoUser voUser){
        return adminService.updateStu(voUser);
//        StUser stUser = new StUser();
//        //对象转换
//        BeanUtils.copyProperties(voUser,stUser);
//        stUser.setMajorClassId(Long.valueOf(voUser.getMajorClass()[1].toString()));
//        stUser.setMajorId((Long.valueOf(voUser.getMajorClass()[0].toString())));
//        stUser.setDormId((Long.valueOf(voUser.getDorm()[1].toString())));
//        //更新数据库
//        if(userService.updateById(stUser)){
//            return Result.succ(null);
//        }else {
//            return Result.fail("完善信息失败");
//        }
    }

    /**
     * 更改用户头像
     */
    public Result upAvatar(MultipartFile file){
        StUser user= (StUser) SecurityUtils.getSubject().getPrincipal();
        String oriFilename= file.getOriginalFilename();
        //用uuid构造唯一文件名
        String newFilename= UUID.randomUUID()+"-"+user.getUsername()+"-"+oriFilename;
        //文件路径
        File filePath = new File(imgFile);
        if(!filePath.exists()){
            if(!filePath.mkdirs()) {
                return Result.fail("文件路径创建失败");
            }
        }
        //删除旧文件
        String oldFilename=user.getAvatar();
        if(!"userImg.png".equals(oldFilename)){
            File oldFile=new File(imgFile+user.getAvatar());
            if(!oldFile.exists()||!oldFile.delete()){
                return Result.fail("原有图片移除失败");
            }
        }
        //上传新文件
        try {
            file.transferTo(new File(imgFile+newFilename));
        } catch (IOException e) {
            return Result.succ("图片上传失败");
        }
        //更新数据库
        StUser stUser=new StUser();
        stUser.setAvatar(newFilename);
        stUser.setId(user.getId());
        if(userService.updateById(stUser)){
            return Result.succ(newFilename);
        }else {
            return Result.fail("图片上传失败");
        }

    }
}
