package com.wsf.springbootdemo.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wsf.springbootdemo.pojo.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
* @author weijin
* @description 针对表【sys_user(用户表)】的数据库操作Mapper
* @createDate 2022-05-20 22:13:20
* @Entity com.wsf.springbootdemo.pojo.User
*/
@Repository
@Mapper
public interface UserMapper extends BaseMapper<User> {
    /**
     * 自定义分页
     * @param page
     * @return
     */
    IPage<User> getUserList(@Param("page") Page<User> page);
}




