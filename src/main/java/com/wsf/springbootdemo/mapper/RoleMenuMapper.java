package com.wsf.springbootdemo.mapper;

import com.wsf.springbootdemo.pojo.RoleMenu;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
* @author weijin
* @description 针对表【sys_role_menu】的数据库操作Mapper
* @createDate 2022-05-23 21:28:02
* @Entity com.wsf.springbootdemo.pojo.RoleMenu
*/
@Repository
@Mapper
public interface RoleMenuMapper extends BaseMapper<RoleMenu> {
    boolean addRoleMenu(List<RoleMenu> roleMenus);
}




