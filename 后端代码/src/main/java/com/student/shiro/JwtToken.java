package com.student.shiro;

import org.apache.shiro.authc.AuthenticationToken;

/**
 * JwtToken
 * @author 李123
 */
public class JwtToken implements AuthenticationToken {
    /**
     * 密钥
     */
    private final String token;

    JwtToken(String token) {
        this.token = token;
    }

    @Override
    public Object getPrincipal() {
        return token;
    }

    @Override
    public Object getCredentials() {
        return token;
    }
}

