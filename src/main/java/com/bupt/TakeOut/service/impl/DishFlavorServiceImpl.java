package com.bupt.TakeOut.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bupt.TakeOut.entity.Dish;
import com.bupt.TakeOut.entity.DishFlavor;
import com.bupt.TakeOut.mapper.DishFlavorMapper;
import com.bupt.TakeOut.mapper.DishMapper;
import com.bupt.TakeOut.service.DishFlavorService;
import com.bupt.TakeOut.service.DishService;
import org.springframework.stereotype.Service;

@Service
public class DishFlavorServiceImpl extends ServiceImpl<DishFlavorMapper, DishFlavor> implements DishFlavorService {
}
