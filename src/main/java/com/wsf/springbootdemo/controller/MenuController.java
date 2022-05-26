package com.wsf.springbootdemo.controller;

import com.auth0.jwt.interfaces.Claim;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wsf.springbootdemo.pojo.Menu;
import com.wsf.springbootdemo.pojo.ResponseResult;
import com.wsf.springbootdemo.pojo.User;
import com.wsf.springbootdemo.service.MenuService;
import com.wsf.springbootdemo.utils.JWTUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author wsfstart
 * @create 2022-05-21 19:14
 */
@Slf4j
@RestController
@RequestMapping("/menu")
public class MenuController {

    @Autowired
    private MenuService menuService;

    @PostMapping("list")
    @PreAuthorize("hasAuthority('system:Menu:list')")
    public ResponseResult list(@RequestParam(required = false,defaultValue = "1") Integer page,
                               @RequestParam(required = false,defaultValue = "10") Integer limit,
                               String menuName,String perms){
        log.info("页码:"+page+"条数:"+limit);
        Page<Menu> menuPage = new Page<>(page, limit);
        QueryWrapper<Menu> wrapper = new QueryWrapper<>();
        wrapper.like(StringUtils.isNotBlank(menuName),"menu_name",menuName)
                .like(StringUtils.isNotBlank(perms),"perms",perms);
        menuService.page(menuPage, wrapper);
        return new ResponseResult(200, "请求权限列表成功", menuPage);
    }

    @PostMapping("status")
    @PreAuthorize("hasAuthority('system:Menu:save')")
    @Transactional
    public ResponseResult status(Long id,String status){
        Menu menu = new Menu();
        menu.setId(id);
        menu.setStatus(status);
        if(menuService.updateById(menu)){
            return new ResponseResult(200,"修改状态成功");
        }else{
            return new ResponseResult(500,"修改状态失败");
        }
    }

    @PostMapping("delete")
    @PreAuthorize("hasAuthority('system:Menu:delete')")
    @Transactional
    public ResponseResult delete(Long id){
        if(menuService.removeById(id)){
            return new ResponseResult(200,"删除成功");
        }else{
            return new ResponseResult(500,"删除失败");
        }
    }

    @PostMapping("save")
    @PreAuthorize("hasAuthority('system:Menu:save')")
    @Transactional
    public ResponseResult save(Menu menu, HttpServletRequest request){
        String token = request.getHeader("token");
        Map<String, Claim> payloadFromToken = JWTUtil.getPayloadFromToken(token);
        Claim claim = payloadFromToken.get("id");
        String id = claim.asString();
        if(Objects.isNull(menu.getId())){
            log.info("添加---------------"+menu);
            menu.setCreateTime(new Date());
            menu.setCreateBy(Long.parseLong(id));
            boolean save = menuService.save(menu);
            if(save){
                return new ResponseResult(200,"权限管理添加成功");
            }else{
                return new ResponseResult(500,"权限管理添加失败");
            }
        }else{
            menu.setUpdateBy(Long.parseLong(id));
            menu.setUpdateTime(new Date());
            log.info("修改---------------"+menu);
            boolean update = menuService.updateById(menu);
            if(update){
                return new ResponseResult(200,"权限管理修改成功");
            }else{
                return new ResponseResult(500,"权限管理修改成功");
            }
        }

    }

    @PostMapping("tree")
    @PreAuthorize("hasAuthority('system:Menu:list')")
    public ResponseResult tree(){
        List<Menu> menuTree = menuService.getMenuTree();
        return new ResponseResult(200,"获取权限列表成功",menuTree);
    }



}
