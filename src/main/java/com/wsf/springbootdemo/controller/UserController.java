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
import com.wsf.springbootdemo.pojo.ResponseResult;
import com.wsf.springbootdemo.pojo.User;
import com.wsf.springbootdemo.pojo.UserRole;
import com.wsf.springbootdemo.service.RoleService;
import com.wsf.springbootdemo.service.UserRoleService;
import com.wsf.springbootdemo.service.UserService;
import com.wsf.springbootdemo.utils.JWTUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.v3.oas.annotations.Operation;
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
@Api(tags = "用户信息处理")
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
    @Operation(summary = "登录接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username", value = "用户名",dataTypeClass = String.class),
            @ApiImplicitParam(name = "password", value = "密码",dataTypeClass = String.class),
            @ApiImplicitParam(name = "code", value = "验证码",dataTypeClass = String.class),
    })
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
    @Operation(summary = "获取验证码")
    @GetMapping("getKaptchaImg")
    public void getKaptchaImg() {
        //默认900秒
        kaptcha.render();
    }


    @PostMapping("logout")
    @Operation(summary = "退出登录")
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
    @Operation(summary = "查询用户列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "当前页",defaultValue = "1",dataTypeClass = Integer.class),
            @ApiImplicitParam(name = "limit", value = "每页条数",defaultValue = "10",dataTypeClass = Integer.class),
            @ApiImplicitParam(name = "userName", value = "用户名",dataTypeClass = String.class),
            @ApiImplicitParam(name = "phonenumber", value = "手机号",dataTypeClass = String.class),
            @ApiImplicitParam(name = "isLock", value = "是否锁定",dataTypeClass = Integer.class),
    })
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
    @Transactional(rollbackFor=Exception.class)
    @Operation(summary = "修改状态")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "用户ID",dataTypeClass = Long.class),
            @ApiImplicitParam(name = "status", value = "状态",dataTypeClass = String.class),
    })
    public ResponseResult status(Long id,String status){
        User user = new User();
        user.setId(id);
        user.setStatus(status);
        user.setUpdateTime(new Date());
        if(userService.updateById(user)){
            return new ResponseResult(200,"修改状态成功");
        }
        return  new ResponseResult(500,"修改状态失败");
    }

    @PostMapping("save")
    @PreAuthorize("hasAuthority('system:User:save')")
    @Transactional(rollbackFor=Exception.class)
    @Operation(summary = "修改用户信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "user", value = "用户对象",dataTypeClass = User.class)
    })
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

    @PostMapping("updateContent")
    @Transactional(rollbackFor=Exception.class)
    @Operation(summary = "修改个人信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "user", value = "用户对象",dataTypeClass = User.class)
    })
    public ResponseResult updateContent(User user){
        boolean update = userService.updateById(user);
        if(update){
            return new ResponseResult(200,"修改个人信息成功");
        }else{
            return new ResponseResult(500,"修改个人信息失败");
        }
    }

    @PostMapping("getUserDetail")
    @Operation(summary = "根据ID查询用户详细信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "用户ID",dataTypeClass = Long.class)
    })
    public ResponseResult getUserDetail(Long id){
        User user = userService.getOne(new QueryWrapper<User>().eq(id != null, "id", id));
        return new ResponseResult(200,user);
    }

    @PostMapping("checkUserNme")
    @Operation(summary = "检查用户名是否可用")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userName", value = "用户名",dataTypeClass = String.class)
    })
    public ResponseResult checkUserName(String userName){
        User user = userService.getOne(new QueryWrapper<User>().eq(StringUtils.isNotBlank(userName), "user_name", userName));
        if(Objects.isNull(user)){
            return new ResponseResult(200,"用户名可用");
        }
        return new ResponseResult(500,"用户名已存在");
    }

    @PostMapping("delete")
    @PreAuthorize("hasAuthority('system:User:delete')")
    @Transactional(rollbackFor=Exception.class)
    @Operation(summary = "根据ID删除用户")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "用户ID",dataTypeClass = Long.class)
    })
    public ResponseResult delete(Long id){
       if( userService.removeById(id)){
           return new ResponseResult(200,"删除成功");
       }else{
           return new ResponseResult(500,"删除失败");
       }
    }

    @PostMapping("resetpwd")
    @PreAuthorize("hasAuthority('system:User:save')")
    @Operation(summary = "根据ID重置用户密码")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "用户ID",dataTypeClass = Long.class)
    })
    @Transactional(rollbackFor=Exception.class)
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
    @Transactional(rollbackFor=Exception.class)
    @Operation(summary = "修改用户角色")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "用户ID",dataTypeClass = Long.class),
            @ApiImplicitParam(name = "roleId", value = "角色ID",dataTypeClass = Long.class)
    })
    public ResponseResult changeRole(Long id,Long roleId){

        boolean userUpdate = userService.update(new UpdateWrapper<User>().eq("id", id).set("user_type", roleId));
        //先删除user_role表里的该id的的数据
        userRoleService.remove(new QueryWrapper<UserRole>().eq(!Objects.isNull(id),"user_id",id));
        //再添加数据
        boolean save = userRoleService.save(new UserRole(id, roleId));
        if(userUpdate&&save){
            return new ResponseResult(200,"角色分配成功");
        }
        return new ResponseResult(200,"角色分配失败");
    }


    @PostMapping("updatePwd")
    @Transactional(rollbackFor=Exception.class)
    @Operation(summary = "修改个人密码")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "用户ID",dataTypeClass = Long.class),
            @ApiImplicitParam(name = "currentPass", value = "当前密码",dataTypeClass = String.class),
            @ApiImplicitParam(name = "password", value = "新密码",dataTypeClass = String.class)
    })
    public ResponseResult updatePwd(Long id,String currentPass,String password){
        User user = userService.getOne(new QueryWrapper<User>().eq(id != null, "id", id));
        if(user!=null){
            boolean matches = bCryptPasswordEncoder.matches(currentPass, user.getPassword());
            if(matches){
                String encode = bCryptPasswordEncoder.encode(password);
                user.setPassword(encode);
                boolean update = userService.updateById(user);
                if(update){
                    return new ResponseResult(200,"修改成功");
                }else{
                    return new ResponseResult(500,"修改失败");
                }
            }else{
                return new ResponseResult(500,"密码错误");
            }
        }else{
            return new ResponseResult(500,"没有该用户");
        }
    }
}
