package com.wcq.thang.controller;

import com.wcq.thang.dto.ShowRetrievalResultDTO;
import com.wcq.thang.service.CorpusRetrievalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

/**
 * @author wcq
 * @date 2019/12/7 9:04
 */
@RestController
public class CorpusRetrievalController {
    @Autowired
    private CorpusRetrievalService retrievalService;
    /**
     * 此方法用于模糊查询全部类型语料，包括Original,Mature,
     * @param content
     * @return
     */
    @PostMapping("/searchAll")
    public ModelAndView searchAll(
            @RequestParam(name="searchContent")String content,
            @RequestParam(name = "searchKind")Integer kind){
        List<ShowRetrievalResultDTO> showRetrievalResultDTOS = null;
        if(kind == null){
            showRetrievalResultDTOS = retrievalService.searchAll(content);
        }else{
            switch (kind){
                case 1:showRetrievalResultDTOS=retrievalService.searchMature(content);break;
                case 2:showRetrievalResultDTOS=retrievalService.searchOriginal(content);break;
                default:System.out.println("搜索类型错误！");break;
            }
        }
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("ResultDTOS",showRetrievalResultDTOS);
        modelAndView.setViewName("/corpus");
        return modelAndView;
    }
    @GetMapping("/preview/{id}")
    public ModelAndView previewOriginal(@PathVariable(name = "id") Integer id){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("document",retrievalService.getOriginalDTOByIdForPreview(id));
        modelAndView.setViewName("/preview");
        return modelAndView;
    }
}
