package com.wsf.springbootdemo.mapper;

import com.wsf.springbootdemo.pojo.Role;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
* @author weijin
* @description 针对表【sys_role(角色表)】的数据库操作Mapper
* @createDate 2022-05-21 21:37:37
* @Entity com.wsf.springbootdemo.pojo.Role
*/
@Repository
@Mapper
public interface RoleMapper extends BaseMapper<Role> {
    List<Long> getMenuIdsByRoleId(Long roleID);
}




