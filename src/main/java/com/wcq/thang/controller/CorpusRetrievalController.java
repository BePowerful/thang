package com.wcq.thang.controller;

import com.wcq.thang.dto.ShowRetrievalResultDTO;
import com.wcq.thang.service.CorpusRetrievalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
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
    public ModelAndView searchAll(@RequestParam(name="searchContent")String content){
        List<ShowRetrievalResultDTO> showRetrievalResultDTOS = retrievalService.searchAll(content);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("ResultDTOS",showRetrievalResultDTOS);
        modelAndView.setViewName("/corpus");
        return modelAndView;
    }
}
