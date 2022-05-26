package com.wsf.springbootdemo.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wsf.springbootdemo.pojo.Role;
import com.wsf.springbootdemo.service.RoleService;
import com.wsf.springbootdemo.mapper.RoleMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author weijin
* @description 针对表【sys_role(角色表)】的数据库操作Service实现
* @createDate 2022-05-21 21:37:37
*/
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role>
    implements RoleService{

    @Autowired
    private RoleMapper roleMapper;

    @Override
    public List<Long> getMenuIdsByRoleId(Long roleID) {
        return roleMapper.getMenuIdsByRoleId(roleID);
    }
}




