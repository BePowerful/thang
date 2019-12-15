package com.wcq.thang.controller;

import com.alibaba.fastjson.JSONObject;
import com.wcq.thang.bean.Result;
import com.wcq.thang.service.CorpusParticipleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author wcq
 * @date 2019/12/7 9:04
 */
@RestController
public class CorpusParticipleController {
    @Autowired
    private CorpusParticipleService participleService;

    @GetMapping("/participle")
    public ModelAndView toParticiple(){return new ModelAndView("participle");}

    /**
     * 从分词专栏点击导入时调用，查询出所有matures和经过清洗的original到loading页面
     * @return
     */
    @GetMapping("/loadingForParticiple")
    public ModelAndView toLoadingParticiple(){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("doWhat","participle");
        modelAndView.addObject("objects",participleService.getCanParticipleCorpus());
        modelAndView.setViewName("loading");
        return modelAndView;
    }

    //加载语料内容的controller在CommonController中
//    @PostMapping("/participle")
//    public Result participleMain(
//            @RequestParam(name = "content")String content,
//            @RequestParam(name = "funcSelect") Integer funcSelect){
//        return participleService.participleMain(content,funcSelect);
//    }
    @PostMapping("/participle")
    public Result participleMain(@RequestBody JSONObject object){
        String content = object.getString("participleContent");//获取带分词内容
        Integer doId = object.getInteger("doId");//获取带分词语料id，可能为空（输入的时候），id需要结合classType确定
        Integer funcSelect = object.getInteger("participleFunction");//分词方法选择
        String classType = object.getString("classType");//带分词语料来自mature还是Original
        if(doId == null){
            doId = -1;
            classType="nullObject";
        }
        return participleService.participleMain(content,funcSelect,doId,classType);
    }
    @GetMapping("/participle/saveResult")
    public Result saveParticipleResult(){
        return participleService.saveParticipleRes();
    }

}
