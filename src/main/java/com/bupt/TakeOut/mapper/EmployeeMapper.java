package com.bupt.TakeOut.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bupt.TakeOut.entity.Employee;
import org.apache.ibatis.annotations.Mapper;

@Mapper
//继承mybatisPlus中的BaseMapper(封装了操作sql的各种方法
public interface EmployeeMapper extends BaseMapper<Employee> {
}
