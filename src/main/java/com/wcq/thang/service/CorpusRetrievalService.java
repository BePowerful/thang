package com.wcq.thang.service;

import com.wcq.thang.bean.Utils;
import com.wcq.thang.dto.OriginalDTO;
import com.wcq.thang.dto.ShowRetrievalResultDTO;
import com.wcq.thang.mapper.MatureMapper;
import com.wcq.thang.mapper.OriginalMapper;
import com.wcq.thang.mapper.UserMapper;
import com.wcq.thang.model.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 语料检索接口
 *
 * @author wcq
 * @date 2019/12/7 9:00
 */
@Service
@Transactional
public class CorpusRetrievalService {
    @Autowired
    private OriginalMapper originalMapper;
    @Autowired
    private MatureMapper matureMapper;
    @Autowired
    private UserMapper userMapper;

    /**
     * 粗语料模糊查询
     * @param content
     * @return
     */
    public List<ShowRetrievalResultDTO> searchMature(String content){
        List<ShowRetrievalResultDTO> showRetrievalResultDTOS = new ArrayList<>();
        String parameter = "%" + content + "%";
        MatureExample matureExample = new MatureExample();
        MatureExample.Criteria matureExampleCriteria = matureExample.createCriteria();
        matureExampleCriteria.andContentLike(parameter);
        List<Mature> matures = matureMapper.selectByExample(matureExample);
        if (matures.size() > 0 && matures.get(0) != null) {
            for (Mature mature : matures) {
                ShowRetrievalResultDTO dto = new ShowRetrievalResultDTO();
                //设置显示内容
                dto.setShowContent(mature.getContent());
                //设置对应id
                dto.setId(mature.getMatureId());
                //设置对应作者
                User user = userMapper.selectByPrimaryKey(mature.getUploader());
                dto.setUser(user);
                //设置时间
                dto.setDate(mature.getDate());
                //设置标签
                dto.setTags(mature.getTags());
                //设置类对应的class
                dto.setClassType("Mature");
                //设置来源
                dto.setSource(mature.getSource());

                showRetrievalResultDTOS.add(dto);
            }
        }
        return showRetrievalResultDTOS;
    }

    /**
     * 模糊查询粗语料
     * @param content
     * @return
     */
    public List<ShowRetrievalResultDTO> searchOriginal(String content){
        String parameter = "%" + content + "%";
        List<ShowRetrievalResultDTO> showRetrievalResultDTOS = new ArrayList<>();
        OriginalExample originalExample = new OriginalExample();
        OriginalExample.Criteria originalExampleCriteria = originalExample.createCriteria();
        originalExampleCriteria.andTitleLike(parameter);
        List<Original> originals = originalMapper.selectByExample(originalExample);
        if (originals.size() > 0 && originals.get(0) != null) {
            for (Original original : originals) {
                ShowRetrievalResultDTO dto = new ShowRetrievalResultDTO();
                //设置显示内容
                dto.setShowContent(original.getTitle());
                //设置对应id
                dto.setId(original.getOriginalId());
                //设置对应作者
                User user = userMapper.selectByPrimaryKey(original.getUploader());
                dto.setUser(user);
                //设置时间
                dto.setDate(original.getDate());
                //设置标签
                dto.setTags("原始语料");
                //设置类对应的class
                dto.setClassType("Original");
                //设置来源
                dto.setSource(original.getSource());

                showRetrievalResultDTOS.add(dto);
            }
        }
        return showRetrievalResultDTOS;
    }
    /**
     * 模糊查询全部
     * @param content
     * @return
     */
    public List<ShowRetrievalResultDTO> searchAll(String content) {
        String parameter = "%" + content + "%";
        List<ShowRetrievalResultDTO> showRetrievalResultDTOS = new ArrayList<>();
        //首先搜索细语料
        showRetrievalResultDTOS = searchMature(parameter);
        //搜索粗语料,合并
        showRetrievalResultDTOS.addAll(searchOriginal(content));
        return showRetrievalResultDTOS;
    }

    /**
     * 查询粗语料显示在预览页面
     * @param id
     * @return
     */
    public String getOriginalDTOByIdForPreview(Integer id){
        //根据id查询到原始语料
        Original original = originalMapper.selectByPrimaryKey(id);
        //清洗过就显示清洗后的
        if(original.getCleaned()){
            return Utils.makeStringToHTML(Utils.readTxtFile(original.getCleanedPath()));
        }else{//否则就显示转换过格式的
            return Utils.makeStringToHTML(Utils.readTxtFile(original.getTxtPath()));
        }

    }
}
