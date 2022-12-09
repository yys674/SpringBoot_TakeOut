package com.bupt.TakeOut.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bupt.TakeOut.entity.Dish;
import com.bupt.TakeOut.entity.DishFlavor;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface DishFlavorMapper extends BaseMapper<DishFlavor> {
}
