package com.wsf.springbootdemo.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wsf.springbootdemo.pojo.RoleMenu;
import com.wsf.springbootdemo.service.RoleMenuService;
import com.wsf.springbootdemo.mapper.RoleMenuMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author weijin
* @description 针对表【sys_role_menu】的数据库操作Service实现
* @createDate 2022-05-23 21:28:02
*/
@Service
public class RoleMenuServiceImpl extends ServiceImpl<RoleMenuMapper, RoleMenu>
    implements RoleMenuService{

    @Autowired
    private RoleMenuMapper roleMenuMapper;

    @Override
    public boolean addRoleMenu(List<RoleMenu> roleMenus) {
        return roleMenuMapper.addRoleMenu(roleMenus);
    }

    @Override
    public List<Long> getMenuIdsByRoleId(Long roleId) {
        return roleMenuMapper.getMenuIdsByRoleId(roleId);
    }
}




