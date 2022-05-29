package com.wsf.springbootdemo.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wsf.springbootdemo.pojo.Employee;
import com.wsf.springbootdemo.service.EmployeeService;
import com.wsf.springbootdemo.mapper.EmployeeMapper;
import org.springframework.stereotype.Service;

/**
* @author weijin
* @description 针对表【sys_employee】的数据库操作Service实现
* @createDate 2022-05-29 17:55:12
*/
@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee>
    implements EmployeeService{

}




