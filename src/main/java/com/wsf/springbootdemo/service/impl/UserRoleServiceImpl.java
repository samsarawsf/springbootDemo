package com.wsf.springbootdemo.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wsf.springbootdemo.pojo.UserRole;
import com.wsf.springbootdemo.service.UserRoleService;
import com.wsf.springbootdemo.mapper.UserRoleMapper;
import org.springframework.stereotype.Service;

/**
* @author weijin
* @description 针对表【sys_user_role】的数据库操作Service实现
* @createDate 2022-05-25 21:55:10
*/
@Service
public class UserRoleServiceImpl extends ServiceImpl<UserRoleMapper, UserRole>
    implements UserRoleService{

}




