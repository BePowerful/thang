package com.wcq.thang.controller;

import com.wcq.thang.bean.Result;
import com.wcq.thang.service.CorpusParticipleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
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
    @PostMapping("/participle")
    public Result participleMain(
            @RequestParam(name = "content")String content,
            @RequestParam(name = "funcSelect") Integer funcSelect){
        return participleService.participleMain(content,funcSelect);
    }

}
