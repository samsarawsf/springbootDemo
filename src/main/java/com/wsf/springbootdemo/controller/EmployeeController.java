package com.wsf.springbootdemo.controller;

import com.auth0.jwt.interfaces.Claim;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wsf.springbootdemo.pojo.Employee;
import com.wsf.springbootdemo.pojo.ResponseResult;
import com.wsf.springbootdemo.pojo.User;
import com.wsf.springbootdemo.pojo.UserRole;
import com.wsf.springbootdemo.service.DeptService;
import com.wsf.springbootdemo.service.EmployeeService;
import com.wsf.springbootdemo.service.JobService;
import com.wsf.springbootdemo.utils.DateUtil;
import com.wsf.springbootdemo.utils.JWTUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Map;
import java.util.Objects;

/**
 * @author wsfstart
 * @create 2022-05-29 21:25
 */
@Slf4j
@RestController
@RequestMapping("/emp")
public class EmployeeController {


    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private DeptService deptService;

    @Autowired
    private JobService jobService;

    @PostMapping("list")
    @PreAuthorize("hasAuthority('system:Emp:list')")
    public ResponseResult list(@RequestParam(required = false,defaultValue = "1") Integer page,
                               @RequestParam(required = false,defaultValue = "10") Integer limit,
                               String name,String phoneNumber,Integer isLock){
        log.info("页码:"+page+"条数:"+limit);
        Page<Employee> employeePage = new Page<>(page, limit);
        QueryWrapper<Employee> wrapper = new QueryWrapper<>();
        wrapper.like(StringUtils.isNotBlank(name),"name",name)
                .like(StringUtils.isNotBlank(phoneNumber),"phone_number",phoneNumber)
                .eq(!Objects.isNull(isLock),"status",isLock);
        employeeService.page(employeePage,wrapper);
        employeePage.getRecords().forEach(employee -> {
            employee.setDept(deptService.getById(employee.getDeptId()));
            employee.setJob(jobService.getById(employee.getJobId()));
        });
        return new ResponseResult(200, "请求员工列表成功", employeePage);
    }

    @PostMapping("status")
    @PreAuthorize("hasAuthority('system:Emp:save')")
    @Transactional
    public ResponseResult status(Long id,String status){
        Employee employee = new Employee();
        employee.setId(id);
        employee.setStatus(status);
        if(employeeService.updateById(employee)){
            return new ResponseResult(200,"修改状态成功");
        }
        else return  new ResponseResult(500,"修改状态失败");
    }

    @PostMapping("save")
    @PreAuthorize("hasAuthority('system:Emp:save')")
    @Transactional
    public ResponseResult save(Employee employee,String ibirthday){
        log.info("生日{}",ibirthday);
        employee.setBirthday(DateUtil.StringtoDate(ibirthday));
        if(Objects.isNull(employee.getId())){
            log.info("添加------------"+employee);
            boolean save = employeeService.save(employee);
            if(save){
                return new ResponseResult(200,"员工管理添加成功");
            }else{
                return new ResponseResult(500,"员工管理添加失败");
            }
        }else{
            log.info("修改------------"+employee);
            boolean update = employeeService.updateById(employee);
            if(update){
                return new ResponseResult(200,"员工管理修改成功");
            }else{
                return new ResponseResult(500,"员工管理修改失败");
            }
        }
    }

    @PostMapping("delete")
    @PreAuthorize("hasAuthority('system:Emp:delete')")
    @Transactional
    public ResponseResult delete(Long id){
        if( employeeService.removeById(id)){
            return new ResponseResult(200,"删除成功");
        }else{
            return new ResponseResult(500,"删除失败");
        }
    }


    @PostMapping("changeDept")
    @PreAuthorize("hasAuthority('system:Emp:dept')")
    @Transactional
    public ResponseResult changeRole(Long id,Long deptId){
        boolean deptUpdate = employeeService.update(new UpdateWrapper<Employee>().eq("id", id).set("dept_id", deptId));
        if(deptUpdate){
            return new ResponseResult(200,"部门分配成功");
        }
        return new ResponseResult(200,"部门分配失败");
    }

    @PostMapping("changeJob")
    @PreAuthorize("hasAuthority('system:Emp:job')")
    @Transactional
    public ResponseResult changeJob(Long id,Long jobId){
        boolean deptUpdate = employeeService.update(new UpdateWrapper<Employee>().eq("id", id).set("job_id", jobId));
        if(deptUpdate){
            return new ResponseResult(200,"岗位分配成功");
        }
        return new ResponseResult(200,"岗位分配失败");
    }
}
