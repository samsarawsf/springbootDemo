package com.wsf.springbootdemo.mapper;

import com.wsf.springbootdemo.pojo.UserRole;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;

/**
* @author weijin
* @description 针对表【sys_user_role】的数据库操作Mapper
* @createDate 2022-05-25 21:55:10
* @Entity com.wsf.springbootdemo.pojo.UserRole
*/
@Repository
public interface UserRoleMapper extends BaseMapper<UserRole> {

}




