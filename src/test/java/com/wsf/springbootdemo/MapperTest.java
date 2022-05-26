package com.wsf.springbootdemo;

import com.auth0.jwt.interfaces.Claim;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wsf.springbootdemo.mapper.MenuMapper;
import com.wsf.springbootdemo.mapper.UserMapper;
import com.wsf.springbootdemo.pojo.Menu;
import com.wsf.springbootdemo.pojo.User;
import com.wsf.springbootdemo.utils.JWTUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author wsf
 */
@SpringBootTest
public class MapperTest {
    @Autowired
    private MenuMapper menuMapper;

     @Autowired
    private UserMapper userMapper;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Test
    public void testUserMapper(){
        List<User> users = userMapper.selectList(null);
        System.out.println(users);
    }

    @Test
    public void testBCryptPasswordEncoder(){
//        String encode1 = bCryptPasswordEncoder.encode("123");
//        String encode2 = bCryptPasswordEncoder.encode("123");
//        System.out.println(encode1+"  "+encode2);
        System.out.println(bCryptPasswordEncoder.matches("123", "$2a$10$QoMJW2BeVYB3KEUPSZr5NuOY5ebAL5fqB9w2KwIQY8fGLdOejhOfm"));
//
    }

    @Test
    public void testParseToken(){
        Map<String, Claim> payloadFromToken = JWTUtil.getPayloadFromToken("eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpZCI6IjIiLCJleHAiOjE2NTI5NjcwMjl9.q3LUyflLOpmCjy8brZBvtPISSQmJ1-JeiUk7iG2MkvI");
        payloadFromToken.forEach(((s, claim) -> {
            System.out.println(s+":"+claim.asString());
        }));
    }

    @Test
    public void testMenuMapper(){
        List<String> list = menuMapper.selectPermsByUserId(2L);
        System.out.println(list);
    }

    @Test
    public void testUserList(){
        Page<User> userPage = new Page<>(1, 10);
        userMapper.getUserList(userPage);
        System.out.println(userPage.getRecords());
    }

    @Test
    public void testMenuList(){
        List<Menu> menus = menuMapper.selectList(null);
        Map<String ,List<Menu>> map = new HashMap<>();
        map.put("用户管理",new ArrayList<>());
        map.put("角色管理",new ArrayList<>());
        map.put("权限管理",new ArrayList<>());
        menus.forEach(menu -> {
           if( menu.getMenuName().substring(5,7).equals("用户")){
               map.get("用户管理").add(menu);
           }else if ( menu.getMenuName().substring(5,7).equals("角色")){
               map.get("角色管理").add(menu);
           }else {
               map.get("权限管理").add(menu);
           }
        });
        map.forEach( (s, menus1) -> {
            System.out.println(s);
            menus1.forEach(System.out::println);
        });

    }

    @Test
    public void testInsertList(){

    }

}
