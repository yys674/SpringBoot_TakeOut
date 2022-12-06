package com.bupt.TakeOut.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.bupt.TakeOut.common.R;
import com.bupt.TakeOut.entity.Employee;
import com.bupt.TakeOut.service.impl.EmployeeServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@Slf4j//日志
@RestController//@Controller+@ResponseBody，直接在页面返回数据
@RequestMapping("/employee")//类前声明，前置路径/employee/
public class EmployeeController {
    @Autowired//自动装配，将实体类中的属性和数据库中的各个属性一一映射
    private EmployeeServiceImpl employeeService;

    @RequestMapping("/login")
//    public R<Employee> login(HttpServletRequest request, Employee employee) {//传入employee获取密码;HttpServletRequest
    public R<Employee> login(HttpServletRequest request,@RequestBody Employee employee) {//传入employee获取密码;HttpServletRequest

        //1、将页面提交的密码加密
        String password = employee.getPassword();
        password = DigestUtils.md5DigestAsHex(password.getBytes());//md5加密

        //2、根据提交的用户名查询数据库
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();//包装器作用？？
        //用法？？
        queryWrapper.eq(Employee::getUsername,employee.getUsername());
        Employee emp = employeeService.getOne(queryWrapper);

        //3、--6、各种情况
        if (emp == null) {
            return R.error("登录失败..");
        }
        if(!emp.getPassword().equals(password)){//equal和==区别
            return R.error("密码错误");
        }
        if (emp.getStatus()==0){
            return R.error("账号禁用");
        }
        //放入的参数意义？
        request.getSession().setAttribute("employee",emp.getId());//将成功的结果存入session中
        return R.success(emp);
    }

}
