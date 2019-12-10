package com.wcq.thang.controller;

import com.wcq.thang.service.CorpusCleanService;
import com.wcq.thang.service.CorpusParticipleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author wcq
 * @date 2019/12/7 9:02
 */
@RestController
public class CommonController {
    @Autowired
    private CorpusCleanService cleanService;
    @Autowired
    private CorpusParticipleService participleService;

    @GetMapping("/test")
    public ModelAndView toTest(){ return new ModelAndView("test");}

    /**
     * 此方法用于离开loading页面时，携带者选中语料的id、类型和操作参数
     * 清洗只用id,和操作参数（清洗还是分词）
     * 分词需要用三个，类型为细语料还是粗语料
     * 说明，原因是清洗导入和分词导入用的是同一个loading页面模板
     *
     * @param id
     * @param classType
     * @param doThis
     * @return
     */
    @GetMapping("/leaveLoading")
    public ModelAndView loading(@RequestParam(name = "id") Integer id,
                                @RequestParam(name = "classType") String classType,
                                @RequestParam(name = "doThis") String doThis) {
        String content = null;
        ModelAndView modelAndView = new ModelAndView();
        if (doThis.equals("clean")) {
            content = cleanService.getOriginalTxtById(id);
            modelAndView.addObject("originalContent", content);
            modelAndView.addObject("doID", id);
            modelAndView.setViewName("clean");
        } else {
            content = participleService.getParticipleContent(id, classType);
            modelAndView.addObject("participleContent", content);
            modelAndView.addObject("doID", id);
            modelAndView.setViewName("participle");
        }
        return modelAndView;
    }
}
