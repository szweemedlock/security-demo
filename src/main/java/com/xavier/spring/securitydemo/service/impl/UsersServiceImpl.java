package com.xavier.spring.securitydemo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xavier.spring.securitydemo.entity.Users;
import com.xavier.spring.securitydemo.mapper.UsersMapper;
import com.xavier.spring.securitydemo.service.IUsersService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author xavier
 * @since 2020-06-11
 */
@Service
@Transactional(rollbackFor = {Exception.class})
public class UsersServiceImpl extends ServiceImpl<UsersMapper, Users> implements IUsersService, UserDetailsService {

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        QueryWrapper<Users> qw = new QueryWrapper<>();
        qw.eq("username", s);
        Users u = getOne(qw);
        if (Objects.isNull(u)) {
            throw new UsernameNotFoundException("用户不存在");
        }
        // 用于讲逗号隔开的权限集字符串切割成可用的权限对象列表
        u.setAuthorities(AuthorityUtils.commaSeparatedStringToAuthorityList(u.getRoles()));
        return u;
    }

    /**
     * 自定义实现权限的转换
     * @param roles 角色集
     * @return 权限集
     */
    @SuppressWarnings("unused")
    private List<GrantedAuthority> generateAuthorities(String roles) {
        List<GrantedAuthority> authorities = new ArrayList<>();
        if (StringUtils.isNoneBlank(roles)) {
            String[] roleArray = roles.split(";");
            Arrays.stream(roleArray).forEach(n -> authorities.add(new SimpleGrantedAuthority(n)));
        }
        return authorities;
    }

    @Override
    public boolean register(Users u) {
        // 对密码进行加密处理
        u.setPassword(PasswordEncoderFactories.createDelegatingPasswordEncoder().encode(u.getPassword()));
        return save(u);
    }
}
