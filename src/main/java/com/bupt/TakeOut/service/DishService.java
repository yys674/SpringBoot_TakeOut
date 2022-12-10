package com.bupt.TakeOut.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bupt.TakeOut.dto.DishDto;
import com.bupt.TakeOut.entity.Dish;

public interface DishService extends IService<Dish> {

    public void saveWithFlavor(DishDto dishDto);

    public DishDto getByIdWithFlavor(Long id);

    public void updateWithFlavor(DishDto dishDto);



}
