package com.wcq.thang.controller;

import com.wcq.thang.bean.Result;
import com.wcq.thang.service.CorpusCollectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author wcq
 * @date 2019/12/7 9:03
 */
@RestController
public class CorpusCollectController {
    @Autowired
    private CorpusCollectService collectService;

    @GetMapping("/upload")
    public ModelAndView toUpload(){
        return new ModelAndView("upload");
    }

    @GetMapping("/collect")
    public ModelAndView toCollect(){ return new ModelAndView("collect");}

    /**
     * 用于粗预料上传
     * 语料上传页面点击上传按钮后执行
     * @param file
     * @param title
     * @param source
     * @return
     */
    @PostMapping(value = "/upload")
    public Result upload(
            @RequestParam("file") MultipartFile file,
            @RequestParam("title") String title,
            @RequestParam("source") String source){
        Result result = collectService.uploadOriginal(title,source,file);
        return result;
    }

    /**
     * 此方法用于细语料的收录，点击细语料后会收录
     * @param tags
     * @param source
     * @param content
     * @return
     */
    @PostMapping("/collect")
    public Result collect(
            @RequestParam(name = "tags") String tags,
            @RequestParam(name = "source")String source,
            @RequestParam(name = "content")String content){
        return collectService.uploadMature(tags,source,content);
    }
}
