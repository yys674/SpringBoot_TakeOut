package com.bupt.TakeOut.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bupt.TakeOut.common.CustomException;
import com.bupt.TakeOut.common.R;
import com.bupt.TakeOut.dto.SetmealDto;
import com.bupt.TakeOut.entity.Category;
import com.bupt.TakeOut.entity.Setmeal;
import com.bupt.TakeOut.service.SetmealService;
import com.bupt.TakeOut.service.impl.CategoryServiceImpl;
import com.bupt.TakeOut.service.impl.SetmealDishServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/setmeal")
public class SetmealController {
    @Autowired
    private SetmealService setmealService;
    @Autowired
    private SetmealDishServiceImpl setmealDishService;
    @Autowired
    private CategoryServiceImpl categoryService;

    @PostMapping
    //接收前端传入的数据
//    public R<String> save(SetmealDto setmealDto){
    //加上注解@RequestBody才可获取请求的数据，传入形参中
    public R<String> save(@RequestBody SetmealDto setmealDto){
        //显示一下传过来的数据
        log.info("setmeal:{}",setmealDto.toString());
        //调用自定义方法保存
        setmealService.saveWithDish(setmealDto);
        return R.success("新增套餐成功..！");
    }

    //分页查询
    @GetMapping("/page")
    public R<Page> page(int page,int pageSize,String name){
        //分页构造器
        Page<Setmeal> pageInfo = new Page<>(page, pageSize);
        //显示内容包含菜品
        Page<SetmealDto> pageDtoInfo = new Page<>(page, pageSize);
        //条件构造器
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        //根据name模糊查询---前提是有name键入
        queryWrapper.like(StringUtils.isNotBlank(name),Setmeal::getName,name);
        //排序，根据sort排序
//        queryWrapper.orderByAsc(Setmeal::get
        queryWrapper.orderByDesc(Setmeal::getUpdateTime);
        //分页查询
        setmealService.page(pageInfo,queryWrapper);
        //将pageInfo对象拷贝到pageDtoInfo
        BeanUtils.copyProperties(pageInfo, pageDtoInfo,"records");

        List<Setmeal> records = pageInfo.getRecords();
        //根据id查询分类对象

        List<SetmealDto>list = records.stream().map(
                (item)->{
                    SetmealDto setmealDto = new SetmealDto();
                    BeanUtils.copyProperties(item,setmealDto);
                    Long categoryId = item.getCategoryId();
                    Category category = categoryService.getById(categoryId);
                    //设置分类名
                    if (categoryId != null) {
                        String categoryName = category.getName();
                        setmealDto.setCategoryName(categoryName);
                    }
                    return setmealDto;
                }
        ).collect(Collectors.toList());

        //存放到信息全的pageDtoInfo
        pageDtoInfo.setRecords(list);
        return R.success(pageDtoInfo);
    }

    //批量启售、停售
    @PostMapping("/status/{status}")
    public R<String> sale(@PathVariable int status,Long[] ids){
        for (Long id:
             ids) {
            Setmeal setmeal = setmealService.getById(id);
            //设置启售状态
            setmeal.setStatus(status);
            //更新数据库
            setmealService.updateById(setmeal);
        }
        return R.success("启售状态修改成功！");
    }

    //删除套餐
    @DeleteMapping
    public R<String> delete(Long[] ids){
        int idx = 0;
        for (Long id : ids) {
            Setmeal setmeal = setmealService.getById(id);
            if(setmeal.getStatus()!=1){
                setmealService.removeById(setmeal);
            }else {
                idx++;
            }
        }
        if (idx>0&&idx==ids.length) {
            throw new CustomException("均为启售状态，无法删除！");
//            return R.error("删除失败，在售");
}

        return R.success("删除成功！");
    }


    //修改套餐，回显套餐信息
    @GetMapping("/{id}")
    public R<SetmealDto> getById(@PathVariable Long id){
        SetmealDto setmealDto = setmealService.getByIdWithDish(id);
        return R.success(setmealDto);
    }

    //保存修改
    @PutMapping
    public R<String> update(@RequestBody SetmealDto setmealDto){
        /*
        * //调错方法了--这个saveWithDish方法是新增菜品的保存方法，调用该方法会报错 Duplicate entry '1601943854635773954' for key 'PRIMARY'
        * 而updateWithDish则事先删除了老的setmealDish信息
        * */
//        setmealService.saveWithDish(setmealDto);

        setmealService.updateWithDish(setmealDto);
        return R.success("套餐信息修改成功！");
    }

}
