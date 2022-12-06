package com.bupt.TakeOut.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bupt.TakeOut.common.R;
import com.bupt.TakeOut.entity.Employee;
import com.bupt.TakeOut.service.impl.EmployeeServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

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
        //用法？？--
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

    /*
    * 员工退出功能
    * */
    @RequestMapping("/logout")
    public R<String> logout(HttpServletRequest request){
        //清理session中保存的当前员工的登录id
        request.getSession().removeAttribute("employee");
        return R.success("退出成功");
    }

    /*
    * 增加员工功能
    * */
    @PostMapping//post方式提交
    public R<String> save(HttpServletRequest request,@RequestBody Employee employee){//@RequestBody，获取页面请求的信息
        //设置传入参数以外的内容，创建时间、创建人、密码等
        //首先，初始密码设置为123456，再用md5加密
        employee.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes()));
        //设置创建时间
        /*employee.setCreateTime(LocalDateTime.now());
        employee.setUpdateTime(LocalDateTime.now());
        //设置创建人
        //创建人id在数据库中的类型为bigInt，且employee中创建人传入参数类型为LONG，因此，此处设为Long
        Long empId = (Long) request.getSession().getAttribute("employee");
        employee.setCreateUser(empId);
        employee.setUpdateUser(empId);*/
        //最后调用employService继承的mybatisPlus的IService进行添加，连sql语句都不用写，通过调用方法实现类
        employeeService.save(employee);
        return R.success("员工添加成功");//返回的通用结果类是传入到前端代码作为参数，从而显示到页面上
    }

    //分页功能实现
    @GetMapping("/page")
    //根据前端代码的需要，确定R的模板类型
    public R<Page> page(int page,int pageSize,String name){
        log.info("page={},pageSize={},name={}",page,pageSize,name);
        //有参构造，name为可有可无，因此使用有参构造
        Page pageInfo = new Page(page, pageSize);
        //启用MP中的wrapper----包装sql语句的功能的类
        //创建条件构造器
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper();
        //过滤条件：名称和搜索内容相似(name不为空则启用过滤
        queryWrapper.like(!StringUtils.isEmpty(name),Employee::getName,name);
        //过滤后进行排序，按照秀嘎时间排序
        queryWrapper.orderByDesc(Employee::getUpdateTime);

        //查询---service调用mapper操作数据库
        employeeService.page(pageInfo);
        return R.success(pageInfo);
    }


    @PutMapping
    //启用禁用员工信息
    public R<String> update(HttpServletRequest request,@RequestBody Employee employee){
        log.info(employee.toString());

        //1、获取Ajax请求的数据,从request中获取，得到当前登录的用户id
        Long empId = (Long) request.getSession().getAttribute("employee");

        /*employee.setUpdateUser(empId);
        employee.setUpdateTime(LocalDateTime.now());*/
        //2、对数据进行更新
        //仍采用继承MP的方法
        employeeService.updateById(employee);
        //3、修改成功返回
        return R.success("修改成功");
    }

    //根据id查询员工信息,从而
    @GetMapping("/{id}")
    public R<Employee> getById(@PathVariable String id){
        log.info("根据id查询对象");
        Employee emp = employeeService.getById(id);
        if (emp == null) {
            return R.error("未找到该员工信息");
        }
        //找到之后，保存该员工信息，点击保存按钮后，转到添加员工请求处（post），完成修改
        return R.success(emp);
    }


}
