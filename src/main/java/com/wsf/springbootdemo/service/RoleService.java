package com.wsf.springbootdemo.service;

import com.wsf.springbootdemo.pojo.Role;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author weijin
* @description 针对表【sys_role(角色表)】的数据库操作Service
* @createDate 2022-05-21 21:37:37
*/
public interface RoleService extends IService<Role> {
    List<Long> getMenuIdsByRoleId(Long roleID);
}
