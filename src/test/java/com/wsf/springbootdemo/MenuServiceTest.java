package com.wsf.springbootdemo;

import com.wsf.springbootdemo.pojo.Menu;
import com.wsf.springbootdemo.service.MenuService;
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
    private MenuService menuService;

    @Test
    public void testMenusTree(){
        List<Menu> menuTree = menuService.getMenuTree();
        menuTree.forEach(menu -> {
            System.out.println(menu.getMenuName());
            menu.getMenus().forEach(System.out::println);
        });
    }
}
