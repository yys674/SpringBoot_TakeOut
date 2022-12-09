package com.bupt.TakeOut.controller;


import com.bupt.TakeOut.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.UUID;

@Slf4j//日志
@RestController//返回Json
@RequestMapping("/common")//前置路径/common
public class CommonController {

    //设置上传的服务端路径
    @Value("${SpringBoot_TakeOut.path}")
    private String basePath;


    @PostMapping("/upload")
    public R<String> upload(MultipartFile file){
        /*
         - method="post" 　　　　　　　　　　　采用post方式提交数据
         - enctype="multipart/form-data" 　　　　采用multipart格式上传文件
         - type="file"　　　　　　　　　　　　 　使用input的file控件上传
        * */
        //此时处理该请求方法中的file为一个临时文件，方法结束后就删除了，因此需要将该文件转存到指定位置
//        log.info("file:{}",file.toString());
        //获取原始文件名（xx.jpg）+后缀
        String originalFilename = file.getOriginalFilename();
        String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));
        //随机生成文件名
        String fileName = UUID.randomUUID().toString() + suffix;

        //创建目录
        //进入File看源码--->public File(String pathname) {
        File dir = new File(basePath);
        if (!dir.exists()) {
            dir.mkdirs();//不存在该路径则创建路径
        }

        //将tmp文件传入dir
        try {
            file.transferTo(new File(basePath + fileName));
        } catch (IOException e) {
            e.printStackTrace();
        }


        return R.success(fileName);
    }

    @GetMapping("/download")
//    public R<String> download(String name, HttpServletResponse response){
    public void download(String name, HttpServletResponse response){

        try {
            //文件输入流读取文件
            FileInputStream fileInputStream = new FileInputStream(new File(basePath + name));
            //输出流写到浏览器
            ServletOutputStream outputStream = response.getOutputStream();

            /*
            * 下面为写入操作的固定套路
            *
            * */

            //逐字节写入
            int len = 0;
            byte[] bytes = new byte[1024];
            //到末尾标识符之前一直写
//            while ((len = fileInputStream.read()) != -1) {//读取字节！！！
            while ((len = fileInputStream.read(bytes)) != -1) {
                outputStream.write(bytes,0,len);//传入参数依次为字节，偏置，长度（？
                outputStream.flush();//刷新
            }
            //结束后关闭流
            outputStream.close();
            fileInputStream.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return;

    }

}
