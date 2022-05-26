package com.wsf.springbootdemo;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wsf.springbootdemo.pojo.User;
import com.wsf.springbootdemo.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

/**
 * @author wsfstart
 * @create 2022-05-20 22:30
 */
@SpringBootTest
public class UserServiceTest {
    @Autowired
    private UserService userService;

    @Test
    public void testGetUserList(){
        Page<User> userPage = new Page<>(1, 5);
        userService.page(userPage,null);
        System.out.println(userPage.getRecords().size());
        System.out.println(userPage.getTotal());
        System.out.println();
    }

    @Test
    public void testUserList(){
        Page<User> userPage = new Page<>(2, 5);

         userService.getUserList(userPage);
         userPage.getRecords().forEach(System.out::println);
    }
}
