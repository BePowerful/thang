package com.wcq.thang.controller;

import com.alibaba.fastjson.JSONObject;
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
    @GetMapping("/loadingForClean")
    public ModelAndView formCleanToLoading() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("doWhat","clean");
        modelAndView.addObject("objects", cleanService.getNoCleanAndFormatChangedOriginal());
        modelAndView.setViewName("loading");
        return modelAndView;
    }
    //导入语料内容在CommonController中

//    /**
//     * * 在clean页面点击清洗后执行
//     *      * 携带清洗的内容
//     *      * 返回Result对象，清洗后的语料存放在result.data中
//     * @param content
//     * @param obj
//     * @return
//     */
//    @PostMapping("/clean")
//    public Result clean(
//            /*@RequestBody String param*/
//            @RequestParam(name = "param")String content,
//            @RequestParam(name = "paramt") String  obj){
////        JSONObject jsonObject = new JSONObject();
////        JSONArray objects = JSONArray.parseArray(param);
//
////        return cleanService.cleanOriginalOrOtherInput(content,ftj,"yes");
//        return new Result();
//    }
    /**
     * 在clean页面点击清洗后执行
     * 携带清洗的内容和清洗语料的id
     * 返回Result对象，清洗后的语料存放在result.data中
     * @param object
     * @return
     */
    @PostMapping("/clean")
    public Result clean(@RequestBody JSONObject object){
        String ftj = object.getString("ftj");
        String math = object.getString("math");
        String content = object.getString("originalContent");
        Integer doId = object.getInteger("doId");
        if(ftj==null){
            ftj="off";
        }
        if(math==null){
            math = "off";
        }
        if(doId == null){
            doId = -1;
        }
        return cleanService.cleanOriginalOrOtherInput(content,ftj,math,doId);
    }
    /*
    @PostMapping("/tryLayui")
    public Result tryLayui2(
            //这其中如果有一个为空就会400
            //所以采用RequestBody
            @RequestParam(name = "originalContent")String con,
            @RequestParam(name="ftj") String ftj,
            @RequestParam(name="math") String math
    ){
        if(ftj == null)
            ftj="null";
        if(math == null)
            math ="null";
        System.out.println(ftj);
        System.out.println(math);
        System.out.println(con);

        Result result = new Result();
        result.setCode(200);
        result.setMsg("ok");
        return result;
    }*/
//    @PostMapping("/tryLayui")
//    public Result tryLayui(@RequestBody JSONObject object){
//        String ftj = object.getString("ftj");
//        String math = object.getString("math");
//        String con = object.getString("originalContent");
//        if(ftj == null)
//            ftj="null";
//        if(math == null)
//            math ="null";
//        System.out.println(ftj);
//        System.out.println(math);
//        System.out.println(con);
//
//        Result result = new Result();
//        result.setCode(200);
//        result.setMsg("ok了");
//        return result;
//    }

    /**
     * 在清洗页面点击更新系统存储后执行，
     * 它传入原始语料的id和清洗后的内容
     * 结果就是将清洗结果写入cleanedPath.txt中
     * @return
     */
    @GetMapping("/clean/updateFile")
    public Result saveCleanResult(){
        return cleanService.seaveCleanResult();
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
