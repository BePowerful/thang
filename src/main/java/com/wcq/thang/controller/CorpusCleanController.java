package com.wcq.thang.controller;

import com.wcq.thang.bean.Result;
import com.wcq.thang.config.Constant;
import com.wcq.thang.service.CommonService;
import com.wcq.thang.service.CorpusCleanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;

/**
 * @author wcq
 * @date 2019/12/7 9:03
 */
@RestController
public class CorpusCleanController {
    @Autowired
    private CorpusCleanService cleanService;
    @Autowired
    private CommonService commonService;

    @GetMapping("/clean")
    public ModelAndView toClean() {
        return new ModelAndView("clean");
    }

    /**
     * 在清洗页面点击导入后使用，跳转到loading页面
     * 携带着未清洗并且已经转换过格式的原始语料
     *
     * @return
     */
    @GetMapping("/loading")
    public ModelAndView formCleanToLoading() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("doWhat","clean");
        modelAndView.addObject("objects", cleanService.getNoCleanAndFormatChangedOriginal());
        modelAndView.setViewName("loading");
        return modelAndView;
    }
    //导入语料内容在CommonController中

    /**
     * 在clean页面点击清洗后执行
     * 携带清洗的内容
     * 返回Result对象，清洗后的语料存放在result.data中
     * @param content
     * @return
     */
    @PostMapping("/clean")
    public Result clean(
            @RequestParam(name = "needContent")String content){
        return cleanService.cleanOriginalOrOtherInput(content);
    }

    /**
     * 在清洗页面点击更新系统存储后执行，
     * 它传入原始语料的id和清洗后的内容
     * 结果就是将清洗结果写入cleanedPath.txt中
     * @param content
     * @param id
     * @return
     */
    @PostMapping("/clean/updateFile")
    public Result uploadFile(
            @RequestParam(name="content")String content,
            @RequestParam(name = "IDid") Integer id
    ){
        return cleanService.updateOriginalCaseCleaned(content,id);
    }

    /**
     * 此功能在更新系统存储后出现，
     * 此方法对应导出原始语料Original
     * @param id
     * @param response
     */
    @GetMapping ("/clean/downloadClean/{id}")
    public void downloadOriginal(@PathVariable(name = "id") Integer id, HttpServletResponse response){
        commonService.downloadFile(response,id, Constant.DOWNLOAD_ORIGINAL);
    }

    /**
     * 此功能在更新系统存储后出现，
     * 此方法对应导出清洗后的语料
     * @param id
     * @param response
     */
    @GetMapping("/clean/downloadCleaned/{id}")
    public void downloadCleaned(@PathVariable(name = "id") Integer id,HttpServletResponse response){
        commonService.downloadFile(response,id, Constant.DOWNLOAD_CLEANED);
    }

}
