package com.bupt.TakeOut.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bupt.TakeOut.entity.Setmeal;
import com.bupt.TakeOut.entity.SetmealDish;
import com.bupt.TakeOut.mapper.SetmealDishMapper;
import com.bupt.TakeOut.mapper.SetmealMapper;
import com.bupt.TakeOut.service.SetmealDishService;
import com.bupt.TakeOut.service.SetmealService;
import org.springframework.stereotype.Service;

@Service
public class SetmealDishServiceImpl extends ServiceImpl<SetmealDishMapper, SetmealDish> implements SetmealDishService {
}
