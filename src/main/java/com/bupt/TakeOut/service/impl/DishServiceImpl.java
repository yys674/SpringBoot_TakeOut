package com.bupt.TakeOut.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bupt.TakeOut.dto.DishDto;
import com.bupt.TakeOut.entity.Category;
import com.bupt.TakeOut.entity.Dish;
import com.bupt.TakeOut.entity.DishFlavor;
import com.bupt.TakeOut.mapper.CategoryMapper;
import com.bupt.TakeOut.mapper.DishMapper;
import com.bupt.TakeOut.service.CategoryService;
import com.bupt.TakeOut.service.DishService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService {

    @Autowired
    private DishFlavorServiceImpl dishFlavorService;

    @Override
    @Transactional//开启事务------原因？？
    public void saveWithFlavor(DishDto dishDto) {
        //保存前端传来的菜品数据
        this.save(dishDto);

        Long dishId = dishDto.getId();

        //得到口味
        List<DishFlavor> flavors = dishDto.getFlavors();

        //将口味和dish一一对应
        /*!!!!!!
        内联？？or?
        Returns a stream consisting of the results of applying the given function to the elements of this stream.
        * */
        flavors.stream().map(
                (item)->{item.setDishId(dishId);
                           return item;
                            }
        ).collect(Collectors.toList());

        //保存口味到菜品数据表dish_flavor;saveBatch 批量保存
        dishFlavorService.saveBatch(flavors);
    }

    //修改菜品(dish附带口味flavor
    @Override
//    @Transactional
//    public void getByIdWithFlavor(Long id) {
    //查询完应返回DishDto，将结果传给前端
    public DishDto getByIdWithFlavor(Long id) {
        //查询菜品信息
        Dish dish = this.getById(id);

        DishDto dishDto = new DishDto();
        //将dish属性复制到dishDto
        BeanUtils.copyProperties(dish, dishDto);

        //查询菜品flavor信息
        //用MP的构造器查询
        LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DishFlavor::getDishId,id);
        //查询完放入列表
        List<DishFlavor> list = dishFlavorService.list(queryWrapper);

        dishDto.setFlavors(list);
        return dishDto;
    }

    //更新dish+flavor
    @Override
//    @Transactional
    public void updateWithFlavor(DishDto dishDto) {
        //dishService中，先更新dish表,更新用update！！！！！！get只进行了查询，因此只有口味修改了，其他信息没变
//        this.getById(dishDto);
        this.updateById(dishDto);

        //把dish_flavor的老内容删除
        LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DishFlavor::getDishId,dishDto.getId());
        dishFlavorService.remove(queryWrapper);

        //更新dish-flavor新内容
//        List<DishFlavor> flavorList = dishDto.getFlavors();
        List<DishFlavor> flavors = dishDto.getFlavors();

        /*
        * !!!学习下面的方法！！
        * */
        flavors = flavors.stream().map(
                (item)->{
                    item.setDishId(dishDto.getId());
                    return item;
                }
        ).collect(Collectors.toList());

        //批量处理
        dishFlavorService.saveBatch(flavors);


    }

    /*//删除dish的同时把flavor也删了
    //报错400
    @Override
    public void removeWithFlavor(DishDto dishDto) {
        //得到当前的dishId
        Dish dish = this.getById(dishDto.getId());
        Long dishId = dish.getId();

        List<DishFlavor> flavors = dishDto.getFlavors();

        //将口味和DishId一一对应
        flavors = flavors.stream().map(
                (item)->{
                    item.setDishId(dishId);
                    return item;
                }
        ).collect(Collectors.toList());

        //删除各个flavor
        for (DishFlavor flavor : flavors){
            dishFlavorService.removeById(flavor.getId());
        }

    }*/
}
