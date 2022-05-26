package com.wsf.springbootdemo.service;

import com.wsf.springbootdemo.pojo.RoleMenu;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author weijin
* @description 针对表【sys_role_menu】的数据库操作Service
* @createDate 2022-05-23 21:28:02
*/
public interface RoleMenuService extends IService<RoleMenu> {
    boolean addRoleMenu(List<RoleMenu> roleMenus);
}
