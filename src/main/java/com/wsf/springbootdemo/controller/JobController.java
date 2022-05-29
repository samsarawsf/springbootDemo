package com.wsf.springbootdemo.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wsf.springbootdemo.pojo.Dept;
import com.wsf.springbootdemo.pojo.Job;
import com.wsf.springbootdemo.pojo.ResponseResult;
import com.wsf.springbootdemo.service.JobService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.Objects;

/**
 * @author wsfstart
 * @create 2022-05-29 21:02
 */
@Slf4j
@RestController
@RequestMapping("/job")
public class JobController {

    @Autowired
    private JobService jobService;

    @PostMapping("list")
    @PreAuthorize("hasAuthority('system:Job:list')")
    public ResponseResult list(@RequestParam(required = false,defaultValue = "1") Integer page,
                               @RequestParam(required = false,defaultValue = "10") Integer limit,
                               String name){
        log.info("页码:"+page+"条数:"+limit);
        Page<Job> jobPage = new Page<>(page, limit);
        QueryWrapper<Job> wrapper = new QueryWrapper<>();
        wrapper.like(StringUtils.isNotBlank(name),"name",name);
        jobService.page(jobPage,wrapper);
        return new ResponseResult(200,"请求职位列表成功",jobPage);
    }


    @PostMapping("save")
    @PreAuthorize("hasAuthority('system:Job:save')")
    @Transactional
    public ResponseResult save(Job job){
        if(Objects.isNull(job.getId())){
            //如果为空就是添加操作
            job.setCreateTime(new Date());
            boolean save = jobService.save(job);
            if(save){
                return new ResponseResult(200,"部门管理添加成功");
            }else{
                return new ResponseResult(500,"部门管理添加失败");
            }
        }else{
            //修改
            boolean update = jobService.updateById(job);
            if(update){
                return new ResponseResult(200,"部门管理修改成功");
            }else{
                return new ResponseResult(500,"部门管理修改失败");
            }
        }
    }

    @PostMapping("delete")
    @PreAuthorize("hasAuthority('system:Job:delete')")
    @Transactional
    public ResponseResult delete(Long id){
        if(jobService.removeById(id)){
            return new ResponseResult(200,"删除成功");
        }else{
            return new ResponseResult(500,"删除失败");
        }
    }

    @PostMapping("status")
    @PreAuthorize("hasAuthority('system:Job:save')")
    @Transactional
    public ResponseResult status(Long id,String status){
        Job job = new Job();
        job.setId(id);
        job.setStatus(status);
        if(jobService.updateById(job)){
            return new ResponseResult(200,"修改状态成功");
        }
        else return  new ResponseResult(500,"修改状态失败");
    }
}
