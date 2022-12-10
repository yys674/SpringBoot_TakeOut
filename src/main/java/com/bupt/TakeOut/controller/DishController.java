package com.bupt.TakeOut.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bupt.TakeOut.common.R;
import com.bupt.TakeOut.dto.DishDto;
import com.bupt.TakeOut.entity.Category;
import com.bupt.TakeOut.entity.Dish;
import com.bupt.TakeOut.entity.DishFlavor;
import com.bupt.TakeOut.service.impl.CategoryServiceImpl;
import com.bupt.TakeOut.service.impl.DishFlavorServiceImpl;
import com.bupt.TakeOut.service.impl.DishServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/dish")
public class DishController {

    /*
    * 范例里为Service的自动装配？为啥不是impl?？
    * */
    //属性自动装配
    @Autowired
    private DishServiceImpl dishService;

    @Autowired
    private DishFlavorServiceImpl dishFlavorService;

    @Autowired
    private CategoryServiceImpl categoryService;

    @PostMapping
    public R<String> save(@RequestBody DishDto dishDto){
        dishService.saveWithFlavor(dishDto);
        return R.success("菜品添加成功！");
    }

    @GetMapping("/page")
    public R<Page> page(int page,int pageSize,String name){
        //分页构造器---每页存放Dish
        Page<Dish> pageInfo = new Page<>(page, pageSize);
        //加入flavor后的显示
        Page<DishDto> dishDtoPage = new Page<>();
        //条件构造器
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        //过滤条件---name非空时，按nam相似查询
        queryWrapper.like(StringUtils.isNotBlank(name),Dish::getName,name);
        //排序---按照创建时间
        queryWrapper.orderByDesc(Dish::getUpdateTime);
        //开始分页查询
        dishService.page(pageInfo,queryWrapper);

        //对象拷贝--将前者拷贝到后者，忽略属性records(用于记录的列表？
        BeanUtils.copyProperties(pageInfo,dishDtoPage,"records");

        List<Dish> records = pageInfo.getRecords();

        /*
        *
        * ?????
        * */
        List<DishDto> list = records.stream().map(
                (item)->{
                    DishDto dishDto = new DishDto();
                    BeanUtils.copyProperties(item,dishDto);

                    Long categoryId = item.getCategoryId();
                    /*
                     * ！！！
                     * 根据id查分类对象
                     * */
                    Category category = categoryService.getById(categoryId);
                    if (category != null) {
                        String categoryName = category.getName();
                        //!!!
                        dishDto.setCategoryName(categoryName);
                    }
                    return dishDto;
                }
        ).collect(Collectors.toList());

        dishDtoPage.setRecords(list);

        return R.success(dishDtoPage);
    }

    //调用自定义的方法修改菜品dish
    @GetMapping("/{id}")
    public R<DishDto> getById(@PathVariable Long id){
        DishDto dishDto = dishService.getByIdWithFlavor(id);
        return R.success(dishDto);
    }
    //保存修改菜品dish
    //标注请求体？？？？----从请求中获取参数？？
//    public R<DishDto> update(@RequestBody DishDto dishDto) {
    //返回的R作为弹出标签显示
    @PutMapping
    public R<String> update(@RequestBody DishDto dishDto) {
        dishService.updateWithFlavor(dishDto);
        return R.success("菜品dish修改成功！");
    }

    //启售停售
    @PostMapping("/status/{status}")
    public R<String> sale(@PathVariable int status,String[] ids){
        for (String id:
             ids) {
            //只需要改status，因此没必要获取flavor
            Dish dish = dishService.getById(id);
            //修改status
            dish.setStatus(status);
            //将修改操作存入数据库
            dishService.updateById(dish);
        }
        return R.success("修改成功！");
    }

    //删除操作
    //没有把对应的flavor删除！！！
    @DeleteMapping
    public R<String> delete(String[] ids) {
        for (String id:
                ids) {
            Dish dish = dishService.getById(id);
            dishService.removeById(dish);
//            dishFlavorService.removeById()
        }
        return R.success("删除成功！");
    }

/*    @DeleteMapping
    public R<String> delete(String[] ids,@RequestBody DishDto dishDto) {
        for (String id:
                ids) {
            dishService.removeWithFlavor(dishDto);
//            dishService.removeById(dish);
//            dishFlavorService.removeById()
        }
        return R.success("删除成功！");
    }*/

   /* @DeleteMapping
//    public R<String> delete(String[] ids) {
    public R<String> delete(String[] ids,DishDto dishDto) {
        for (String id:
             ids) {
            Dish dish = dishService.getById(id);
            //报错cannot be cast to.....  DishDto are in unnamed module of loader 'app')
//            DishDto dishDto =(DishDto) dishService.getById(id);
            List<DishFlavor> flavors = dishDto.getFlavors();
            flavors = flavors.stream().map(
                    (item)->{
                        item.setDishId(dish.getId());
                        return item;
                    }
            ).collect(Collectors.toList());
            for (DishFlavor dishFlavor:
                 flavors) {
                dishFlavorService.removeById(dishFlavor.getId());
            }
            dishService.removeById(dish);
        }
        return R.success("删除成功！");
    }*/

}
