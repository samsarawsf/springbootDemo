package com.wsf.springbootdemo.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wsf.springbootdemo.pojo.Dept;
import com.wsf.springbootdemo.service.DeptService;
import com.wsf.springbootdemo.mapper.DeptMapper;
import org.springframework.stereotype.Service;

/**
* @author weijin
* @description 针对表【sys_dept(部门表)】的数据库操作Service实现
* @createDate 2022-05-29 17:56:21
*/
@Service
public class DeptServiceImpl extends ServiceImpl<DeptMapper, Dept>
    implements DeptService{

}




