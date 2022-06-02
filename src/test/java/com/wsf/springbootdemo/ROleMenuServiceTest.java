package com.wsf.springbootdemo;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wsf.springbootdemo.pojo.RoleMenu;
import com.wsf.springbootdemo.service.RoleMenuService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author wsfstart
 * @create 2022-05-23 21:30
 */
@SpringBootTest
public class ROleMenuServiceTest {

    @Autowired
    private RoleMenuService roleMenuService;

    @Test
    public void testInsertList(){
        Long [] menuIds = {2L,3L,4L};
        System.out.println(Arrays.toString(menuIds));
        Long id =2L;
        List<RoleMenu> roleMenus = new ArrayList<>();
        Arrays.stream(menuIds).forEach(menuId ->{
            RoleMenu roleMenu = new RoleMenu(id, menuId);
            System.out.println(roleMenu);
            roleMenus.add(roleMenu);
        });
        roleMenuService.remove(new QueryWrapper<RoleMenu>().eq("role_id",id));
        roleMenuService.addRoleMenu(roleMenus);
    }

    @Test
    public void testIds(){
        List<Long> ids = roleMenuService.getMenuIdsByRoleId(2L);
        ids.forEach(System.out::println);
    }
}
