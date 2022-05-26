package com.wsf.springbootdemo.mapper;

import com.wsf.springbootdemo.pojo.Menu;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
* @author weijin
* @description 针对表【sys_menu(菜单表)】的数据库操作Mapper
* @createDate 2022-05-20 22:18:35
* @Entity com.wsf.springbootdemo.pojo.Menu
*/
@Repository
@Mapper
public interface MenuMapper extends BaseMapper<Menu> {
    List<String> selectPermsByUserId(Long id);

}




