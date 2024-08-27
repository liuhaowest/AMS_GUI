package com.student.shiro;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.ThreadContext;
import org.apache.shiro.web.filter.authc.AuthenticatingFilter;
import org.apache.shiro.web.subject.WebSubject;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

/**
 * @author 李123
 * JwtFilter
 */
@Component
public class JwtFilter extends AuthenticatingFilter {
    /**
     * 过滤器拦截请求的入口方法,所有请求都会进入该方法
     * 返回true则允许访问
     */
    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        boolean allowed = false;
        try{
            //检测header里的JWT Token内容是否正确，尝试使用token进行登录
            allowed = this.executeLogin(request,response);
        } catch (IllegalStateException e){ //未找到token
            System.out.println("未找到token");
        } catch (Exception e){
            System.out.println("token检验出错");
        }
        return allowed || super.isPermissive(mappedValue);
    }


    // 登录方法重写
    @Override
    protected boolean executeLogin(ServletRequest request, ServletResponse response) throws Exception {
        AuthenticationToken token = this.createToken(request, response);
        if (token == null) {
            throw new IllegalStateException("无法创建token");
        } else {
            try {
                Subject subject = new WebSubject.Builder(request, response).buildSubject();
                subject.login(token);
                ThreadContext.bind(subject);
                return this.onLoginSuccess(token, subject, request, response);
            } catch (AuthenticationException var5) {
                return this.onLoginFailure(token, var5, request, response);
            }
        }
    }
    /**
     * onLoginFailure方法返回false,会进入该方法，表示拒绝访问
     */
    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        HttpServletResponse httpServletResponse = WebUtils.toHttp(response);
        httpServletResponse.setCharacterEncoding("UTF-8");
        httpServletResponse.setContentType("application/json;charset=UTF-8");
        PrintWriter writer = httpServletResponse.getWriter();
        writer.write("{\"code\":402,\"msg\":\"拒绝访问,请先登录" + "\"}");
        return false;
    }
    /**
     * 利用 JWT token 登录失败，会进入该方法
     */
    @Override
    protected boolean onLoginFailure(AuthenticationToken token, AuthenticationException e, ServletRequest request, ServletResponse response) {
        //直接返回false,交给后面的 onAccessDenied()方法处理
        return false;
    }

    // 创建JwtToken
    @Override
    protected AuthenticationToken createToken(ServletRequest servletRequest, ServletResponse servletResponse) {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        String jwt = request.getHeader("Token");
        if (jwt == null) {
            return null;
        }
        return new JwtToken(jwt);
    }


    /**
     * 对跨域提供支持
     */
    @Override
    protected boolean preHandle(ServletRequest request, ServletResponse response) throws Exception {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        httpServletResponse.setHeader("Access-control-Allow-Origin", httpServletRequest.getHeader("Origin"));
        httpServletResponse.setHeader("Access-Control-Allow-Methods", "GET,POST,OPTIONS,PUT,DELETE");
        httpServletResponse.setHeader("Access-Control-Allow-Headers", httpServletRequest.getHeader("Access-Control-Request-Headers"));
        // 跨域时会首先发送一个option请求，这里我们给option请求直接返回正常状态
        if (httpServletRequest.getMethod().equals(RequestMethod.OPTIONS.name())) {
            httpServletResponse.setStatus(HttpStatus.OK.value());
            return false;
        }
        return super.preHandle(request, response);
    }
}

