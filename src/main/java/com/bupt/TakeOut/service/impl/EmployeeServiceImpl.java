package com.bupt.TakeOut.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bupt.TakeOut.entity.Employee;
import com.bupt.TakeOut.mapper.EmployeeMapper;
import com.bupt.TakeOut.service.EmployeeService;
import org.springframework.stereotype.Service;

@Service//在实现类中声明为service，不要在接口处声明
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements EmployeeService {

}
