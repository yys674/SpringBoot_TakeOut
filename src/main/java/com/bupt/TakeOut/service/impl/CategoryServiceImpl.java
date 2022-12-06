package com.bupt.TakeOut.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bupt.TakeOut.common.CustomException;
import com.bupt.TakeOut.entity.Category;
import com.bupt.TakeOut.entity.Dish;
import com.bupt.TakeOut.entity.Setmeal;
import com.bupt.TakeOut.mapper.CategoryMapper;
import com.bupt.TakeOut.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper,Category> implements CategoryService {

    @Autowired
    private DishServiceImpl dishService;

    @Autowired
    private SetmealServiceImpl setmealService;

    @Override
    public void remove(Long ids) {

        LambdaQueryWrapper<Dish> dishLambdaQueryWrapper = new LambdaQueryWrapper<>();

        //即为select XXX where ids = ?
        dishLambdaQueryWrapper.eq(Dish::getCategoryId,ids);
        //记录查到的数目
        int DishCount = dishService.count(dishLambdaQueryWrapper);
        //若数目不为零说明有关联
        if(DishCount > 0){
            //抛出自定义异常
            throw new CustomException("有关联菜品！无法删除！");
        }

        //另一个同理
        LambdaQueryWrapper<Setmeal> setmealLambdaQueryWrapper = new LambdaQueryWrapper<>();

        setmealLambdaQueryWrapper.eq(Setmeal::getCategoryId,ids);
        int setmealCount = setmealService.count(setmealLambdaQueryWrapper);

        if(setmealCount>0){
            throw new CustomException("有关联套餐！无法删除！");
        }

        //正常情况下删除
        super.removeById(ids);

    }
}
