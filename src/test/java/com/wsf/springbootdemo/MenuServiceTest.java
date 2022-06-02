package com.wsf.springbootdemo;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wsf.springbootdemo.pojo.Menu;
import com.wsf.springbootdemo.pojo.RoleMenu;
import com.wsf.springbootdemo.service.MenuService;
import com.wsf.springbootdemo.service.RoleMenuService;
import com.wsf.springbootdemo.service.RoleService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

/**
 * @author wsfstart
 * @create 2022-05-23 0:35
 */
@SpringBootTest
public class MenuServiceTest {

    @Autowired
    private RoleMenuService roleMenuService;

    @Autowired
    private MenuService menuService;

    @Autowired
    private RoleService roleService;

    @Test
    public void testMenusTree(){
        List<Menu> menuTree = menuService.getMenuTree();
        menuTree.forEach(menu -> {
            System.out.println(menu.getMenuName());
            menu.getMenus().forEach(System.out::println);
        });
    }

    @Test
    public void testMyMenuTree(){
//        List<RoleMenu> role_id = roleMenuService.list(new QueryWrapper<RoleMenu>().eq("role_id", 2));
        List<Menu> myMenuTree = menuService.getMyMenuTree(3L);
        myMenuTree.forEach(System.out::println);
//        menuService.list(new QueryWrapper<Menu>().in())
    }
}
