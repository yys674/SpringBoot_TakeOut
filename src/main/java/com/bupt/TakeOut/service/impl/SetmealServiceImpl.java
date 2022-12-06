package com.bupt.TakeOut.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bupt.TakeOut.entity.Dish;
import com.bupt.TakeOut.entity.Setmeal;
import com.bupt.TakeOut.mapper.DishMapper;
import com.bupt.TakeOut.mapper.SetmealMapper;
import com.bupt.TakeOut.service.DishService;
import com.bupt.TakeOut.service.SetmealService;
import org.springframework.stereotype.Service;

@Service
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal> implements SetmealService {
}
