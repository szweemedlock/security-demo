package com.xavier.spring.securitydemo.provider;

import com.xavier.spring.securitydemo.config.CustomAuthenticationDetails;
import com.xavier.spring.securitydemo.exception.VerificationCodeException;
import com.xavier.spring.securitydemo.service.impl.UsersServiceImpl;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

/**
 * 自定义的认证器
 */
@Component
public class CustomAuthenticationProvider extends DaoAuthenticationProvider {

    public CustomAuthenticationProvider(UsersServiceImpl usersService) {
        this.setUserDetailsService(usersService);
    }

    @Override
    protected void additionalAuthenticationChecks(UserDetails userDetails, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
        // 实现图形验证码校验逻辑
        CustomAuthenticationDetails details = (CustomAuthenticationDetails) authentication.getDetails();
        if (!details.isImageCodeIsRight()) {
            throw new VerificationCodeException();
        }
        // 调用父类方法完成密码验证
        super.additionalAuthenticationChecks(userDetails, authentication);
    }
}
