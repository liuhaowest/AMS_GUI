package com.student.servie;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.crypto.digest.DigestUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.student.dao.entity.*;
import com.student.dao.service.*;
import com.student.utils.JwtUtils;
import com.student.common.Result;
import com.student.vo.VoUser;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * 处理用户登录注册
 * @author liHeWei
 * @since 2022-04-16
 */
@Service
public class LoginService {

    @Autowired
    private IStUserService userService;
    @Autowired
    private IStRoleService roleService;
    @Autowired
    private IStDormService dormService;
    @Autowired
    private IStClassService classService;
    @Autowired
    private IStMajorService majorService;

    @Resource
    private RedisTemplate<String, Object> redisTemplate ;

    //用户注册
    public Result register(VoUser voUser){
        StUser user=new StUser();
        if(voUser==null) {
            return Result.fail("注册信息未填写完整");
        }
        String salt= RandomUtil.randomString(5);
        String password = DigestUtil.md5Hex(voUser.getPassword()+salt);
        //效验账户唯一性
        QueryWrapper<StUser> query=new QueryWrapper<>();
        query.eq("username", voUser.getUsername())
                .or()
                .eq("email", voUser.getEmail())
                .or()
                .eq("phone_num", voUser.getPhoneNum());
        List<StUser> list = userService.list(query);
        if(!list.isEmpty()) {
            return Result.fail("邮箱或用户名或手机号已被注册");
        }
        //效验验证码,用前端传来的邮箱去redis中查询验证码
        String confirm=(String) redisTemplate.opsForValue().get(voUser.getEmail());
        if(confirm==null) {
            return Result.fail("验证码已失效,请重新发送");
        } else {
            if(!confirm.equals(voUser.getConfirm())) {
                return Result.fail("验证码不正确");
            }
        }
        //效验完毕,写入数据库
        String role=voUser.getIsTeacher()==1?"teacher":"student";
        Long roleId= roleService.getOne(new QueryWrapper<StRole>().eq("role",role)).getId();
        user.setUsername(voUser.getUsername());
        user.setRealName(voUser.getRealName());
        user.setEmail(voUser.getEmail());
        user.setPhoneNum(voUser.getPhoneNum());
        user.setSalt(salt);
        user.setPassword(password);
        user.setRoleId(roleId);
        userService.save(user);
        return Result.succ("注册成功");
    }

    //获取用户信息
    public Result getInfo(){
        StUser user = (StUser) SecurityUtils.getSubject().getPrincipal();
        return Result.succ(convertUser(user));
    }

    //用户类转换
    public VoUser convertUser(StUser user){
        //保护敏感信息
        user.setPassword(null);
        //获取权限信息
        LinkedList<String> list=new LinkedList<>();
        StRole role=roleService.getById(user.getRoleId());
        list.add(role.getRole());
        VoUser voUser=new VoUser();
        //类转换
        BeanUtils.copyProperties(user,voUser);
        if(user.getDormId()==0){
            voUser.setDorm(new Object[]{"待完善"});
        }else {
            StDorm dorm = dormService.getById(user.getDormId());
            voUser.setDorm(new Object[]{dorm.getId(),dorm.getDormAddress(),dorm.getDormNum()});
        }
        if(user.getMajorId()==0){
            voUser.setMajor(new String[]{"待完善"});
        }else {
            StMajor major = majorService.getById(user.getMajorId());
            voUser.setMajor(new Object[]{major.getName(),major.getId()});
        }
        if(user.getMajorClassId()==0){
            voUser.setMajorClass(new String[]{"待完善"});
        }else {
            StClass majorClass = classService.getById(user.getMajorClassId());
            voUser.setMajorClass(new Object[]{majorClass.getName(),majorClass.getId()});
        }
        voUser.setRoles(list);
        return voUser;
    }

    //用户登录
    public Result login(String account,String viewPassword){
        //查询信息
        StUser user=userService.getOne(new QueryWrapper<StUser>().eq("username", account)
        .or()
        .eq("email",account));
        //验证密码
        if(user==null) {
            return Result.fail("用户信息未注册");
        }
        String password=DigestUtil.md5Hex(viewPassword+user.getSalt());
        if(!user.getPassword().equals(password)) {
            return Result.fail("用户名或密码错误");
        } else {
            //验证成功，生成token传回去
            HashMap<String, String> map = new HashMap<>(1);
            Map<String,Object> res=new HashMap<>(2);
            map.put("username", user.getUsername());
            String jwt = JwtUtils.getToken(map);
            res.put("token",jwt);
            res.put("roles",roleService.getById(user.getRoleId()).getRole());
            return Result.succ(res);
        }
    }
}
