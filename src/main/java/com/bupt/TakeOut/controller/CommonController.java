package com.bupt.TakeOut.controller;


import com.bupt.TakeOut.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Slf4j//日志
@RestController//返回Json
@RequestMapping("/common")//前置路径/common
public class CommonController {

    @PostMapping("/upload")
    public R<String> upload(MultipartFile file){
        /*
         - method="post" 　　　　　　　　　　　采用post方式提交数据
         - enctype="multipart/form-data" 　　　　采用multipart格式上传文件
         - type="file"　　　　　　　　　　　　 　使用input的file控件上传
        * */
        //此时处理该请求方法中的file为一个临时文件，方法结束后就删除了，因此需要将该文件转存到指定位置
        log.info("file:{}",file.toString());
        return null;
    }
}
