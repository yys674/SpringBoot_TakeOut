package com.bupt.TakeOut.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bupt.TakeOut.common.R;
import com.bupt.TakeOut.entity.Category;
import com.bupt.TakeOut.entity.Employee;
import com.bupt.TakeOut.service.impl.CategoryServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j//日志
@RestController//返回响应体
@RequestMapping("/category")
public class CategoryController {
    @Autowired//自动装配
    private CategoryServiceImpl categoryService;

    //实现新增套餐分类功能
    @PostMapping
    public R<String> save(@RequestBody Category category){

        //打印分类信息
        log.info(category.toString());

        categoryService.save(category);

        return R.success("新增分类成功..！！");//返回类作为参数显示到弹窗上
    }

    //分页查询,类比员工分页查询
    @GetMapping("/page")
    public R<Page> page(int page,int pageSize){
        log.info("page={},pageSize={},name={}",page,pageSize);
        //有参构造
        Page pageInfo = new Page(page, pageSize);
        //启用MP中的wrapper----包装sql语句的功能的类
        //创建条件构造器
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper();

        //过滤后进行排序，按照order排序
        queryWrapper.orderByAsc(Category::getSort);

        //查询---service调用mapper操作数据库
//        categoryService.page(pageInfo);
        //需要传入queryWrapper,才能显示排序后的查询结果，否则还是添加的顺序
        categoryService.page(pageInfo,queryWrapper);

        return R.success(pageInfo);
    }


    //删除分类
    @DeleteMapping
    public R<String> delete(Long ids){
        log.info("删除分类：{}",ids);

        //根据id删除分类
//        categoryService.removeById(ids);

        //为了调用自定义的remove，应在controller中使用
        categoryService.remove(ids);

        return R.success("分类信息删除成功..！");

    }

    //修改分类
    @PutMapping
    public R<String> update(@RequestBody Category category){//获取前端传入的json数据

        categoryService.updateById(category);

        return R.success("分类修改成功！");
    }

    //菜品添加中下拉框内容的实现
    @GetMapping("/list")
    public R<List<Category>> list(Category category){

        //MP的条件构造器
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();

        //添加元素的条件的设置
        queryWrapper.eq(category.getType()!=null,Category::getType,category.getType());
        //排序条件---按次序排序，一样的话则按修改时间排序
        queryWrapper.orderByAsc(Category::getSort).orderByAsc(Category::getUpdateTime);

        //处理完成后通过service层的功能将处理过的数据放入list
        List<Category> list = categoryService.list(queryWrapper);
        return R.success(list);
    }
}
