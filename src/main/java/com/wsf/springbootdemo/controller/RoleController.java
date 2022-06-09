package com.wsf.springbootdemo.controller;


import com.auth0.jwt.interfaces.Claim;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wsf.springbootdemo.pojo.*;
import com.wsf.springbootdemo.service.RoleMenuService;
import com.wsf.springbootdemo.service.RoleService;
import com.wsf.springbootdemo.utils.JWTUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * @author wsfstart
 * @create 2022-05-22 16:01
 */
@Slf4j
@RestController
@RequestMapping("/role")
@Api(tags = "角色信息处理")
public class RoleController {

    @Autowired
    private RoleService roleService;

    @Autowired
    private RoleMenuService roleMenuService;


    /**
     * 根据权限查询角色数据
     * @param page 当前页
     * @param limit 条数
     * @return 响应体
     */
    @PostMapping("list")
    @PreAuthorize("hasAuthority('system:Role:list')")
    @Operation(summary = "查询角色列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "当前页",defaultValue = "1",dataTypeClass = Integer.class),
            @ApiImplicitParam(name = "limit", value = "每页条数",defaultValue = "10",dataTypeClass = Integer.class),
            @ApiImplicitParam(name = "roleName", value = "角色名称",dataTypeClass = String.class),
            @ApiImplicitParam(name = "roleKey", value = "角色代码",dataTypeClass = String.class)
    })
    public ResponseResult list(@RequestParam(required = false,defaultValue = "1") Integer page,
                               @RequestParam(required = false,defaultValue = "10") Integer limit,
                               String roleName, String roleKey){
        log.info("页码:"+page+"条数:"+limit);
        Page<Role> rolePage = new Page<>(page, limit);
        QueryWrapper<Role> wrapper = new QueryWrapper<>();
        wrapper.like(StringUtils.isNotBlank(roleName),"name",roleName)
                .like(StringUtils.isNotBlank(roleKey),"role_key",roleKey);
        roleService.page(rolePage,wrapper);
        return new ResponseResult(200, "请求角色列表成功", rolePage);
    }


    @PostMapping("menu")
    @PreAuthorize("hasAuthority('system:Menu:list')")@Transactional(rollbackFor=Exception.class)
    @Operation(summary = "根据ID查询角色权限")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "用户ID",dataTypeClass = Long.class)
    })
    public ResponseResult menu(Long id){
        List<Long> menuIds = roleService.getMenuIdsByRoleId(id);
        return new ResponseResult(200,"请求权限成功",menuIds);
    }

    @PostMapping("changemenu")
    @PreAuthorize("hasAuthority('system:Role:save')")
    @Transactional(rollbackFor=Exception.class)
    @Operation(summary = "修改角色权限")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "角色ID",dataTypeClass = Long.class),
            @ApiImplicitParam(name = "menuIds", value = "权限ID数组",dataTypeClass = Long.class)
    })
    public ResponseResult changeMenu(Long id, Long[] menuIds){
        System.out.println(Arrays.toString(menuIds));
        log.info("id:"+id);
        List<RoleMenu> roleMenus = new ArrayList<>();
        Arrays.stream(menuIds).forEach(menuId ->{
            RoleMenu roleMenu = new RoleMenu(id, menuId);
            System.out.println(roleMenu);
            roleMenus.add(roleMenu);
        });
        for (Long menuId:menuIds
             ) {
            RoleMenu roleMenu = new RoleMenu(id, menuId);
            roleMenus.add(roleMenu);
        }
        roleMenuService.remove(new QueryWrapper<RoleMenu>().eq("role_id",id));
        roleMenuService.addRoleMenu(roleMenus);
        return new ResponseResult(200,"权限配置成功");
    }

    @PostMapping("save")
    @PreAuthorize("hasAuthority('system:Role:save')")
    @Transactional(rollbackFor=Exception.class)
    @Operation(summary = "修改角色信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "role", value = "角色对象",dataTypeClass = Role.class)
    })
    public ResponseResult save(Role role, HttpServletRequest request){
        String token = request.getHeader("token");
        Map<String, Claim> payloadFromToken = JWTUtil.getPayloadFromToken(token);
        Claim claim = payloadFromToken.get("id");
        String id = claim.asString();
        if(Objects.isNull(role.getId())){
            role.setCreateBy(Long.parseLong(id));
            role.setCreateTime(new Date());
            boolean save = roleService.save(role);
            if(save){
                return new ResponseResult(200,"角色管理添加成功");
            }else{
                return new ResponseResult(500,"角色管理添加失败");
            }
        }else{
            role.setUpdateBy(Long.parseLong(id));
            role.setUpdateTime(new Date());
            boolean update = roleService.updateById(role);
            if(update){
                return new ResponseResult(200,"角色管理修改成功");
            }else{
                return new ResponseResult(500,"角色管理修改失败");
            }
        }
    }

    @PostMapping("delete")
    @PreAuthorize("hasAuthority('system:Role:delete')")
    @Transactional(rollbackFor=Exception.class)
    @Operation(summary = "根据ID删除角色")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "角色ID",dataTypeClass = Long.class)
    })
    public ResponseResult delete(Long id){
        if( roleService.removeById(id)){
            return new ResponseResult(200,"删除成功");
        }else{
            return new ResponseResult(500,"删除失败");
        }
    }
}
