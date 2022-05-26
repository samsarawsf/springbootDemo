package com.wsf.springbootdemo.controller;

import com.alibaba.fastjson.JSON;
import com.auth0.jwt.interfaces.Claim;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wsf.springbootdemo.mapper.RoleMapper;
import com.wsf.springbootdemo.pojo.*;
import com.wsf.springbootdemo.service.MenuService;
import com.wsf.springbootdemo.service.RoleMenuService;
import com.wsf.springbootdemo.service.RoleService;
import com.wsf.springbootdemo.utils.JWTUtil;
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
public class RoleController {

    @Autowired
    private RoleService roleService;

    @Autowired
    private RoleMenuService roleMenuService;


    /**
     * 根据权限查询角色数据
     * @param page 当前页
     * @param limit 条数
     * @return
     */
    @PostMapping("list")
    @PreAuthorize("hasAuthority('system:Role:list')")
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
    @PreAuthorize("hasAuthority('system:Menu:list')")
    public ResponseResult menu(Long id){
        List<Long> menuIds = roleService.getMenuIdsByRoleId(id);
        return new ResponseResult(200,"请求权限成功",menuIds);
    }

    @PostMapping("changemenu")
    @PreAuthorize("hasAuthority('system:Role:save')")
    @Transactional
    public ResponseResult changeMenu(Long id, Long[] menuIds){
//        List<Long> menuIdss = JSON.parseArray(menuIds, Long.class);
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
    @Transactional
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
    @Transactional
    public ResponseResult delete(Long id){
        if( roleService.removeById(id)){
            return new ResponseResult(200,"删除成功");
        }else{
            return new ResponseResult(500,"删除失败");
        }
    }
}
