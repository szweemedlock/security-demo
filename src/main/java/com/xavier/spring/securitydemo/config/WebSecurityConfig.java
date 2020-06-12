package com.xavier.spring.securitydemo.config;

import com.google.code.kaptcha.Producer;
import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.code.kaptcha.util.Config;
import com.xavier.spring.securitydemo.handler.CustomAuthenticationFailureHandler;
import com.xavier.spring.securitydemo.service.IUsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationDetailsSource;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Properties;

/**
 * @author Karl Xavier
 */
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final DataSource dataSource;

    private final IUsersService usersService;

    private final AuthenticationDetailsSource<HttpServletRequest, WebAuthenticationDetails> customAuthenticationDetailsSource;

    private final AuthenticationProvider authenticationProvider;

    public WebSecurityConfig(IUsersService usersService, AuthenticationDetailsSource<HttpServletRequest, WebAuthenticationDetails> customAuthenticationDetailsSource, AuthenticationProvider authenticationProvider, DataSource dataSource) {
        this.usersService = usersService;
        this.customAuthenticationDetailsSource = customAuthenticationDetailsSource;
        this.authenticationProvider = authenticationProvider;
        this.dataSource = dataSource;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // super.configure(http);
        http.authorizeRequests()
                .antMatchers("/admin/api/**").hasRole("ADMIN")
                .antMatchers("/user/api/**").hasRole("USER")
                // 放开验证码权限
                .antMatchers("/app/api/**", "/captcha.jpg").permitAll()
                .anyRequest().authenticated()
                .and()
                .csrf().disable()
                .formLogin()
                // 应用 authenticationDetailSource
                .authenticationDetailsSource(customAuthenticationDetailsSource)
                .loginPage("/login.html")
                // 制定处理登陆请求的路径
                .loginProcessingUrl("/auth/form").permitAll()
                //
                .successHandler((httpServletRequest, httpServletResponse, authentication) -> {
                    httpServletResponse.setContentType("application/json;charset=utf-8");
                    PrintWriter writer = httpServletResponse.getWriter();
                    writer.write("{\"message\":\"Hello\"}");
                })
                .failureHandler(new CustomAuthenticationFailureHandler())
                .and()
                .rememberMe().userDetailsService(usersService)
                .tokenRepository(repository())
                .and()
                .logout()
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login.html")
                .logoutSuccessHandler((request, response, authentication) -> System.out.println("我成功退出了"))
                // 使用户的session失效
                .invalidateHttpSession(true)
                // 删除指定的 cookie
                .deleteCookies("JSESSIONID", "remember-me")
                // 用于注销的处理句柄，允许自定义一些清理策略
                .addLogoutHandler((request, response, authentication) -> System.out.println("这里可以自定义清理策略"));
        // 将自定义的过滤器添加在用户认证之前
//        http.addFilterBefore(new VerificationCodeFilter(), UsernamePasswordAuthenticationFilter.class);
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(authenticationProvider);
    }

    /**
     * kaptcha实例
     * @return 实例对象
     */
    @Bean
    public Producer captcha() {
        Properties properties = new Properties();
        properties.setProperty("kaptcha.image.width", "150");
        properties.setProperty("kaptcha.image.height", "50");
        properties.setProperty("kaptcha.textproducer.char.string", "0123456789");
        properties.setProperty("kaptcha.textproducer.char.length", "4");
        Config config = new Config(properties);
        // 使用默认的图形验证码实现
        DefaultKaptcha defaultKaptcha = new DefaultKaptcha();
        defaultKaptcha.setConfig(config);
        return defaultKaptcha;
    }

    @Bean
    public PersistentTokenRepository repository() {
        JdbcTokenRepositoryImpl jdbcTokenRepository = new JdbcTokenRepositoryImpl();
        jdbcTokenRepository.setDataSource(dataSource);
        // 启动自动建表
//        jdbcTokenRepository.setCreateTableOnStartup(true);
        return jdbcTokenRepository;
    }
}
