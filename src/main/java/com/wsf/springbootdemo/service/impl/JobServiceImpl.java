package com.wsf.springbootdemo.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wsf.springbootdemo.pojo.Job;
import com.wsf.springbootdemo.service.JobService;
import com.wsf.springbootdemo.mapper.JobMapper;
import org.springframework.stereotype.Service;

/**
* @author weijin
* @description 针对表【sys_job】的数据库操作Service实现
* @createDate 2022-05-29 17:56:29
*/
@Service
public class JobServiceImpl extends ServiceImpl<JobMapper, Job>
    implements JobService{

}




