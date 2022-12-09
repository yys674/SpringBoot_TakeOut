package com.bupt.TakeOut.dto;

import com.bupt.TakeOut.entity.Dish;
import com.bupt.TakeOut.entity.DishFlavor;
import lombok.Data;
import java.util.ArrayList;
import java.util.List;

@Data
public class DishDto extends Dish {

    private List<DishFlavor> flavors = new ArrayList<>();

    private String categoryName;

    private Integer copies;
}
