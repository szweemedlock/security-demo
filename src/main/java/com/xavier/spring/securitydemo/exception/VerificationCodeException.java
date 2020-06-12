package com.xavier.spring.securitydemo.exception;

import org.springframework.security.core.AuthenticationException;

/**
 * @author Karl Xavier
 */
public class VerificationCodeException extends AuthenticationException {

    public VerificationCodeException() {
        super("图形验证码校验失败");
    }
}
