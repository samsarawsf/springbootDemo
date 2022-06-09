package com.wsf.springbootdemo.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wsf.springbootdemo.pojo.*;
import com.wsf.springbootdemo.service.DeptService;
import com.wsf.springbootdemo.service.EmployeeService;
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

import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * @author wsfstart
 * @create 2022-05-29 18:23
 */
@Slf4j
@RestController
@RequestMapping("/dept")
@Api(tags = "部门信息处理")
public class DeptController {

    @Autowired
    private DeptService deptService;

    @Autowired
    private EmployeeService employeeService;

    @PostMapping("list")
    @PreAuthorize("hasAuthority('system:Dept:list')")
    @Operation(summary = "查询职位列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "当前页",defaultValue = "1",dataTypeClass = Integer.class),
            @ApiImplicitParam(name = "limit", value = "每页条数",defaultValue = "10",dataTypeClass = Integer.class),
            @ApiImplicitParam(name = "name", value = "部门名称",dataTypeClass = String.class)
    })
    public ResponseResult list(@RequestParam(required = false,defaultValue = "1") Integer page,
                               @RequestParam(required = false,defaultValue = "10") Integer limit,
                               String name){
        log.info("页码:"+page+"条数:"+limit);
        Page<Dept> deptPage = new Page<>(page, limit);
        QueryWrapper<Dept> wrapper = new QueryWrapper<>();
        wrapper.like(StringUtils.isNotBlank(name),"name",name);
        deptService.page(deptPage,wrapper);
        return new ResponseResult(200,"请求部门列表成功",deptPage);
    }


    @PostMapping("save")
    @PreAuthorize("hasAuthority('system:Dept:save')")
    @Transactional(rollbackFor=Exception.class)
    @Operation(summary = "修改部门信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "dept", value = "部门对象",dataTypeClass = Job.class)
    })
    public ResponseResult save(Dept dept){
        if(Objects.isNull(dept.getId())){
            //如果为空就是添加操作
            dept.setCreateTime(new Date());
            boolean save = deptService.save(dept);
            if(save){
                return new ResponseResult(200,"部门管理添加成功");
            }else{
                return new ResponseResult(500,"部门管理添加失败");
            }
        }else{
            //修改
            boolean update = deptService.updateById(dept);
            if(update){
                return new ResponseResult(200,"部门管理修改成功");
            }else{
                return new ResponseResult(500,"部门管理修改失败");
            }
        }
    }

    @PostMapping("delete")
    @PreAuthorize("hasAuthority('system:Dept:delete')")
    @Transactional(rollbackFor=Exception.class)
    @Operation(summary = "根据ID删除部门")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "部门ID",dataTypeClass = Long.class)
    })
    public ResponseResult delete(Long id){
        if(deptService.removeById(id)){
            return new ResponseResult(200,"删除成功");
        }else{
            return new ResponseResult(500,"删除失败");
        }
    }

    @PostMapping("status")
    @PreAuthorize("hasAuthority('system:Dept:save')")
    @Transactional(rollbackFor=Exception.class)
    @Operation(summary = "修改状态")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "部门ID",dataTypeClass = Long.class),
            @ApiImplicitParam(name = "status", value = "状态",dataTypeClass = String.class),
    })
    public ResponseResult status(Long id,String status){
        Dept dept = new Dept();
        dept.setId(id);
        dept.setStatus(status);
        if(deptService.updateById(dept)){
            return new ResponseResult(200,"修改状态成功");
        }
         return  new ResponseResult(500,"修改状态失败");
    }

    @PostMapping("listEmp")
    @PreAuthorize("hasAuthority('system:Dept:list')")
    @Operation(summary = "根据部门ID查询员工列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "部门ID",dataTypeClass = Long.class)
    })
    public ResponseResult listEmp(Long id){
        List<Employee> employeeList = employeeService.list(new QueryWrapper<Employee>().eq(id != null, "dept_id", id));
        return new ResponseResult(200,"请求员工列表成功",employeeList);
    }
}
