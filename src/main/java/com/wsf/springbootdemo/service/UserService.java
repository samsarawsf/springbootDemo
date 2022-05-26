package com.wsf.springbootdemo.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wsf.springbootdemo.pojo.ResponseResult;
import com.wsf.springbootdemo.pojo.User;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author weijin
* @description 针对表【sys_user(用户表)】的数据库操作Service
* @createDate 2022-05-20 22:13:20
*/
public interface UserService extends IService<User> {
    ResponseResult login(User user);

    ResponseResult logout();

    IPage getUserList(Page<User> page);
}
