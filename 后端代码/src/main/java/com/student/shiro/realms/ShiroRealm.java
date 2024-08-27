package com.student.shiro.realms;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.student.utils.JwtUtils;
import com.student.dao.entity.StRole;
import com.student.dao.entity.StUser;
import com.student.dao.service.IStRoleService;
import com.student.dao.service.IStUserService;
import com.student.shiro.JwtToken;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * @author 李123
 */
@Service
public class ShiroRealm extends AuthorizingRealm {

    @Autowired
    private IStRoleService roleService;
    @Autowired
    private IStUserService userService;


    //验证token是否为jwtToken
    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof JwtToken;
    }

    //权限认证
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        //获取用户信息
        StUser user = (StUser) principalCollection.getPrimaryPrincipal();
        Long roleId = user.getRoleId();
        //查询权限信息
        StRole role = roleService.getById(roleId);
        //添加权限
        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
        simpleAuthorizationInfo.addRole(role.getRole());
        return simpleAuthorizationInfo;
    }

    //身份认证
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
       //获取用户信息
        String token = (String) authenticationToken.getCredentials();
        //获取用户名
        String username = JwtUtils.getString(token, "username");
        //查询用户
        StUser user= userService.getOne(new QueryWrapper<StUser>().eq("username", username));
        if (username == null||user == null) {
            throw new AuthenticationException("用户不存在");
        }
        //验证token
        if (!JwtUtils.verify(token)) {
            throw new AuthenticationException("签名验证失败");
        }
        //将用户信息放入shiro，方便业务类获取当前登录信息
        return new SimpleAuthenticationInfo(user, user, getName());
    }
}
