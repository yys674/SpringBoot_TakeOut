package com.bupt.TakeOut.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bupt.TakeOut.entity.Category;

public interface CategoryService extends IService<Category> {
    //自定义关联删除
    public void remove(Long ids);

}
