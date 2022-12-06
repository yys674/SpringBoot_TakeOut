package com.bupt.TakeOut.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bupt.TakeOut.entity.Category;
import com.bupt.TakeOut.entity.Dish;
import com.bupt.TakeOut.mapper.CategoryMapper;
import com.bupt.TakeOut.mapper.DishMapper;
import com.bupt.TakeOut.service.CategoryService;
import com.bupt.TakeOut.service.DishService;
import org.springframework.stereotype.Service;

@Service
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService {
}
