package com.bupt.TakeOut.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bupt.TakeOut.dto.SetmealDto;
import com.bupt.TakeOut.entity.Dish;
import com.bupt.TakeOut.entity.Setmeal;
import com.bupt.TakeOut.entity.SetmealDish;
import com.bupt.TakeOut.mapper.DishMapper;
import com.bupt.TakeOut.mapper.SetmealMapper;
import com.bupt.TakeOut.service.DishService;
import com.bupt.TakeOut.service.SetmealService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal> implements SetmealService {

    @Autowired
    private SetmealDishServiceImpl setmealDishService;

    //存Dish相关
    @Override
    @Transactional//事务作用
    public void saveWithDish(SetmealDto setmealDto) {
        //先将共有属性保存
        this.save(setmealDto);
        //存下来菜品
        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();
        //设置列表各个元素中的套餐Id--setmealId
        setmealDishes = setmealDishes.stream().map(
                (item)->{
                    item.setSetmealId(setmealDto.getId());
                    return item;
                }
        ).collect(Collectors.toList());
        //保存套餐和菜品的关联信息,用setmealDish(套餐菜品)的业务层实现
        setmealDishService.saveBatch(setmealDishes);

    }

    //套餐信息回显
    @Override
    public SetmealDto getByIdWithDish(Long id) {
        Setmeal setmeal = this.getById(id);
        SetmealDto setmealDto = new SetmealDto();
        //共有信息复制给dto
        BeanUtils.copyProperties(setmeal, setmealDto);

        //菜品信息
        LambdaQueryWrapper<SetmealDish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SetmealDish::getSetmealId,setmeal.getId());
        //查询结果存到列表
        List<SetmealDish> list = setmealDishService.list(queryWrapper);

        //列表结果存到Dto
        setmealDto.setSetmealDishes(list);
        return setmealDto;
    }

    //保存套餐修改
    @Override
    public void updateWithDish(SetmealDto setmealDto) {
        //先保存共有信息（Setmeal和Dto都有的属性
        this.save(setmealDto);
        //把SetmealDish(对应修改页面黄色添加菜品框的信息)数据库表中的信息先删除
        LambdaQueryWrapper<SetmealDish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SetmealDish::getSetmealId,setmealDto.getId());
        //将筛选出来的把SetmealDish表中的信息删除
        setmealDishService.remove(queryWrapper);

        //把json传过来的新的信息插入把SetmealDish表中
        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();
        //将json传过来的把SetmealDish的setmealId和库中的setmeal对应
        setmealDishes = setmealDishes.stream().map(
            (item)->{
                item.setSetmealId(setmealDto.getId());
                return item;
            }
        ).collect(Collectors.toList());

        setmealDishService.saveBatch(setmealDishes);

    }
}
