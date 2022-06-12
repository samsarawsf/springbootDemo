package com.wsf.springbootdemo.controller;

import com.auth0.jwt.interfaces.Claim;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wsf.springbootdemo.pojo.Menu;
import com.wsf.springbootdemo.pojo.ResponseResult;
import com.wsf.springbootdemo.service.MenuService;
import com.wsf.springbootdemo.service.RoleMenuService;
import com.wsf.springbootdemo.utils.JWTUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.v3.oas.annotations.Operation;
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
@Api(tags = "权限（菜单）信息处理")
public class MenuController {

    @Autowired
    private MenuService menuService;

    @Autowired
    private RoleMenuService roleMenuService;

    @PostMapping("list")
    @PreAuthorize("hasAuthority('system:Menu:list')")
    @Operation(summary = "查询权限列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "当前页",defaultValue = "1",dataTypeClass = Integer.class),
            @ApiImplicitParam(name = "limit", value = "每页条数",defaultValue = "10",dataTypeClass = Integer.class),
            @ApiImplicitParam(name = "menuName", value = "权限名称",dataTypeClass = String.class),
            @ApiImplicitParam(name = "perms", value = "权限代码",dataTypeClass = String.class)
    })
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
    @Transactional(rollbackFor=Exception.class)
    @Operation(summary = "修改状态")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "权限ID",dataTypeClass = Long.class),
            @ApiImplicitParam(name = "status", value = "状态",dataTypeClass = String.class),
    })
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
    @Transactional(rollbackFor=Exception.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "权限ID",dataTypeClass = Long.class)
    })
    @Operation(summary = "根据ID删除权限")
    public ResponseResult delete(Long id){
        if(menuService.removeById(id)){
            return new ResponseResult(200,"删除成功");
        }else{
            return new ResponseResult(500,"删除失败");
        }
    }

    @PostMapping("save")
    @PreAuthorize("hasAuthority('system:Menu:save')")
    @Transactional(rollbackFor=Exception.class)
    @Operation(summary = "修改权限信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "menu", value = "权限对象",dataTypeClass = Menu.class)
    })
    public ResponseResult save(Menu menu, HttpServletRequest request){
        //从请求头中获取token
        String token = request.getHeader("token");
        //解析token
        Map<String, Claim> payloadFromToken = JWTUtil.getPayloadFromToken(token);
        //获取id
        Claim claim = payloadFromToken.get("id");
        String id = claim.asString();
        if(menu.getParentId()==null){
            menu.setParentId(0L);
        }
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
    @Operation(summary = "查询所有权限的树状结构")
    public ResponseResult tree(){
        List<Menu> menuTree = menuService.getMenuTree();
        return new ResponseResult(200,"获取权限列表成功",menuTree);
    }

    @PostMapping("myMenuTree")
    @Operation(summary = "根据ID查询权限的树状结构")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "用户ID",dataTypeClass = Long.class)
    })
    public ResponseResult myMenuTree(Long id){
        List<Menu> myMenuTree = menuService.getMyMenuTree(id);
        return new ResponseResult(200,"获取权限列表成功",myMenuTree);
    }

}
