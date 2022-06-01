package com.wsf.springbootdemo.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wsf.springbootdemo.pojo.LoginUser;
import com.wsf.springbootdemo.pojo.ResponseResult;
import com.wsf.springbootdemo.pojo.User;
import com.wsf.springbootdemo.service.UserService;
import com.wsf.springbootdemo.mapper.UserMapper;
import com.wsf.springbootdemo.utils.JWTUtil;
import com.wsf.springbootdemo.utils.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
* @author weijin
* @description 针对表【sys_user(用户表)】的数据库操作Service实现
* @createDate 2022-05-20 22:13:20
*/
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService{
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Override
    public ResponseResult login(User user) {
        //AuthenticationManager的authenticate方法来进行用户认证
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user.getUserName(),user.getPassword());
        Authentication authenticate = authenticationManager.authenticate(authenticationToken);
        //如果认证没通过，给出对应的异常提示
        if(Objects.isNull(authenticate)){
            throw new RuntimeException("登陆失败");
        }
        //如果认证通过了，使用Userid生成jwt jwt存入ResponseResult返回
        LoginUser loginUser = (LoginUser) authenticate.getPrincipal();
        String userid = loginUser.getUser().getId().toString();
        System.out.println(loginUser.getUser());
//        String jwt = JwtUtil.createJWT(userid);
        HashMap<String, String> tokenMap = new HashMap<>();
        tokenMap.put("id",userid);
        String jwt = JWTUtil.getToken(tokenMap);
        Map<String ,String> map = new HashMap<>();
        map.put("token",jwt);
        map.put("nickName",loginUser.getUser().getNickName());
        map.put("id",loginUser.getUser().getId().toString());
        //把完整的用户信息存入redis userid作为Key
        redisCache.setCacheObject("login:"+userid,loginUser);
        return new ResponseResult(200,"登录成功",map);
    }

    @Override
    public ResponseResult logout() {
        //获取SecurityContextHolder中的用户id
        UsernamePasswordAuthenticationToken authentication = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        String id = loginUser.getUser().getId().toString();
        //删除redis中的值
        redisCache.deleteObject("login:"+id);
        return new ResponseResult(200,"注销成功");
    }

    @Override
    public IPage<User> getUserList(Page<User> page) {
        return userMapper.getUserList(page);
    }
}




