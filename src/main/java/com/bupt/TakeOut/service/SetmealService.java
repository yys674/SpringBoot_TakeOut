package com.bupt.TakeOut.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bupt.TakeOut.dto.SetmealDto;
import com.bupt.TakeOut.entity.Setmeal;

public interface SetmealService extends IService<Setmeal> {
    public void saveWithDish(SetmealDto setmealDto);
    public SetmealDto getByIdWithDish(Long id);
    public void updateWithDish(SetmealDto setmealDto);
}
