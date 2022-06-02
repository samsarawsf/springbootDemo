package com.wsf.springbootdemo.service;

import com.wsf.springbootdemo.pojo.Menu;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author weijin
* @description 针对表【sys_menu(菜单表)】的数据库操作Service
* @createDate 2022-05-20 22:18:35
*/
public interface MenuService extends IService<Menu> {
    List<Menu> getMenuTree();

    List<Menu> getMyMenuTree(Long id);
}
