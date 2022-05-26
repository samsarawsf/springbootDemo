package com.wsf.springbootdemo.controller;

import com.auth0.jwt.interfaces.Claim;
import com.baomidou.kaptcha.Kaptcha;
import com.baomidou.kaptcha.exception.KaptchaIncorrectException;
import com.baomidou.kaptcha.exception.KaptchaNotFoundException;
import com.baomidou.kaptcha.exception.KaptchaTimeoutException;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wsf.springbootdemo.mapper.RoleMapper;
import com.wsf.springbootdemo.pojo.ResponseResult;
import com.wsf.springbootdemo.pojo.User;
import com.wsf.springbootdemo.pojo.UserRole;
import com.wsf.springbootdemo.service.RoleService;
import com.wsf.springbootdemo.service.UserRoleService;
import com.wsf.springbootdemo.service.UserService;
import com.wsf.springbootdemo.utils.JWTUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Map;
import java.util.Objects;

/**
 * @author wsfstart
 * @create 2022-05-19 15:41
 */
@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private Kaptcha kaptcha;

    @Autowired
    private UserService userService;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private UserRoleService userRoleService;

    @Autowired
    private RoleService roleService;


    /**
     * 登录接口
     * @param username
     * @param password
     * @param code
     * @return
     */
    @PostMapping("login")
    public ResponseResult login(String username,String password,String code) {
        log.info("user/login");
        log.info(username+" "+password+"  "+code);
        User user = new User(username, password);
        try {
             kaptcha.validate(code);

        } catch (Exception e) {
            e.printStackTrace();
            if (e instanceof KaptchaIncorrectException) {
                return new ResponseResult(410, "验证码不正确");
            } else if (e instanceof KaptchaNotFoundException) {
                return new ResponseResult(410, "验证码未找到");
            } else if (e instanceof KaptchaTimeoutException) {
                return new ResponseResult(410, "验证码过期");
            } else {
                return new ResponseResult(410, "验证码渲染失败");
            }

        }
        //登录
        return userService.login(user);
    }

    /**
     * 获取Kaptcha验证码
     */
    @GetMapping("getKaptchaImg")
    public void getKaptchaImg() {
        //默认900秒
        kaptcha.render();
    }


    @RequestMapping("logout")
    public ResponseResult logout() {
        return userService.logout();
    }

    /**
     * 根据权限查询用户数据
     * @param page 当前页
     * @param limit 条数
     * @return
     */
    @PostMapping("list")
    @PreAuthorize("hasAuthority('system:User:list')")
    public ResponseResult list(@RequestParam(required = false,defaultValue = "1") Integer page,
                               @RequestParam(required = false,defaultValue = "10") Integer limit,
                               String userName,String phonenumber,Integer isLock) {
        log.info("页码:"+page+"条数:"+limit);
        Page<User> userPage = new Page<>(page, limit);
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.like(StringUtils.isNotBlank(userName),"user_name",userName)
                .like(StringUtils.isNotBlank(phonenumber),"phonenumber",phonenumber)
                        .eq(!Objects.isNull(isLock),"status",isLock);
        userService.page(userPage,wrapper);
        userPage.getRecords().forEach(user -> {
            user.setRole(roleService.getById(user.getUserType()));
        });
        return new ResponseResult(200, "请求用户列表成功", userPage);
    }

    @PostMapping("status")
    @PreAuthorize("hasAuthority('system:User:save')")
    @Transactional
    public ResponseResult status(Long id,String status){
        User user = new User();
        user.setId(id);
        user.setStatus(status);
        user.setUpdateTime(new Date());
        if(userService.updateById(user)){
            return new ResponseResult(200,"修改状态成功");
        }
        else return  new ResponseResult(500,"修改状态失败");
    }

    @PostMapping("save")
    @PreAuthorize("hasAuthority('system:User:save')")
    @Transactional
    public ResponseResult save(User user, HttpServletRequest request){
        String token = request.getHeader("token");
        Map<String, Claim> payloadFromToken = JWTUtil.getPayloadFromToken(token);
        Claim claim = payloadFromToken.get("id");
        String id = claim.asString();
        if(Objects.isNull(user.getId())){
            user.setCreateTime(new Date());
            log.info("添加------------"+user);
            user.setCreateBy(Long.parseLong(id));
            boolean save = userService.save(user);
            if(save){
                return new ResponseResult(200,"用户管理添加成功");
            }else{
                return new ResponseResult(500,"用户管理添加失败");
            }
        }else{
            user.setUpdateTime(new Date());
            user.setUpdateBy(Long.parseLong(id));
            log.info("修改------------"+user);
            boolean update = userService.updateById(user);
            if(update){
                return new ResponseResult(200,"用户管理修改成功");
            }else{
                return new ResponseResult(500,"用户管理修改失败");
            }
        }
    }

    @PostMapping("checkUserNme")
    public ResponseResult checkUserName(String userName){
        User user = userService.getOne(new QueryWrapper<User>().eq(StringUtils.isNotBlank(userName), "user_name", userName));
        if(Objects.isNull(user)){
            return new ResponseResult(200,"用户名可用");
        }
        return new ResponseResult(500,"用户名已存在");
    }

    @PostMapping("delete")
    @PreAuthorize("hasAuthority('system:User:delete')")
    @Transactional
    public ResponseResult delete(Long id){
       if( userService.removeById(id)){
           return new ResponseResult(200,"删除成功");
       }else{
           return new ResponseResult(500,"删除失败");
       }
    }

    @PostMapping("resetpwd")
    @PreAuthorize("hasAuthority('system:User:save')")
    @Transactional
    public ResponseResult resetpwd(Long id){
        String encode = bCryptPasswordEncoder.encode("123456");
        User user = new User();
        user.setId(id);
        user.setPassword(encode);
        user.setUpdateTime(new Date());
        if(userService.updateById(user)){
            return new ResponseResult(200,"重设密码成功");
        }else{
            return new ResponseResult(500,"重设密码失败");
        }
    }

    @PostMapping("changeRole")
    @PreAuthorize("hasAuthority('system:User:role')")
    @Transactional
    public ResponseResult changeRole(Long id,Long roleId){

        boolean userUpdate = userService.update(new UpdateWrapper<User>().eq("id", id).set("user_type", roleId));
        boolean userRoleUpdate = userRoleService.updateById(new UserRole(id, roleId));
        if(userUpdate&&userRoleUpdate){
            return new ResponseResult(200,"角色分配成功");
        }
        return new ResponseResult(200,"角色分配失败");
    }

    @PostMapping("test")
    public ResponseResult test(@RequestHeader String token){
        System.out.println(token);
        return null;
    }
}
