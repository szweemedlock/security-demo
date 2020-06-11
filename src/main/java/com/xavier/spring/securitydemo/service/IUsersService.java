package com.xavier.spring.securitydemo.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xavier.spring.securitydemo.entity.Users;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author xavier
 * @since 2020-06-11
 */
public interface IUsersService extends IService<Users> {

    /**
     * 注册用户
     * @param u 用户对象
     * @return 操作结果
     */
    boolean register(Users u);

}
