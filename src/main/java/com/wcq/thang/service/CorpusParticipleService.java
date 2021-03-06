package com.wcq.thang.service;

import com.wcq.thang.bean.ObjectCache;
import com.wcq.thang.bean.ParticipleUtil;
import com.wcq.thang.bean.Result;
import com.wcq.thang.bean.Utils;
import com.wcq.thang.config.Constant;
import com.wcq.thang.dto.ShowRetrievalResultDTO;
import com.wcq.thang.mapper.MatureMapper;
import com.wcq.thang.mapper.OriginalMapper;
import com.wcq.thang.mapper.TcorpusMapper;
import com.wcq.thang.mapper.UserMapper;
import com.wcq.thang.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 语料分词接口
 * @author wcq
 * @date 2019/12/7 8:58
 */
@Service
@Transactional
public class CorpusParticipleService {
    @Autowired
    private MatureMapper matureMapper;
    @Autowired
    private OriginalMapper originalMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private TcorpusMapper tcorpusMapper;
    @Autowired
    private ObjectCache objectCache;

    /**
     * 获取可分词的语料列表
     * 用于分词导入语料时使用显示在loading页面
     * @return
     */
    public List<ShowRetrievalResultDTO> getCanParticipleCorpus(){
        List<ShowRetrievalResultDTO> showRetrievalResultDTOS = new ArrayList<>();

        //所有的细语料应该都是可以分词的
        MatureExample matureExample = new MatureExample();
        List<Mature> matures = matureMapper.selectByExample(matureExample);
        //获取所有的已经清洗过的原始语料
        OriginalExample originalExample = new OriginalExample();
        OriginalExample.Criteria criteria = originalExample.createCriteria();
        criteria.andCleanedEqualTo(true);
        List<Original> originals = originalMapper.selectByExample(originalExample);
        //合并
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

                dto.setSource(original.getSource());
                showRetrievalResultDTOS.add(dto);
            }
        }
        return showRetrievalResultDTOS;
    }

    /**
     * 用户离开loading页面时
     * 语料内容导入
     * 用于分词
     * @param id
     * @param classType
     * @return
     */
    public String getParticipleContent(Integer id,String classType){
        if(classType.equals("Mature")){
            return matureMapper.selectByPrimaryKey(id).getContent();
        }else{
            return Utils.readTxtFile(originalMapper.selectByPrimaryKey(id).getCleanedPath());
        }
    }

    /**
     * 分词入口
     * @param content 分词内容
     * @param funcSelect 分词方法
     * @param doId 分词对应的预料id
     * @param classType id来自m还是o
     * @return
     */
    public Result participleMain(String content, Integer funcSelect,Integer doId,String classType) {
        Result result = new Result();
        ParticipleUtil participleUtil = new ParticipleUtil();
        System.out.println("*************"+funcSelect);
        String participleResult = "";
        switch (funcSelect){
            case 1:participleResult=participleUtil.jieBa(content);break;
            case 2:participleResult=participleUtil.standard(content);break;
            case 3:participleResult=participleUtil.NLP(content);break;
            case 4:participleResult=participleUtil.indexPar(content);break;
            case 5:participleResult=participleUtil.crf(content);break;
            case 6:participleResult=participleUtil.NShort(content);break;
            case 7:participleResult=participleUtil.analysis(content);break;
            default:System.out.println("选择分词方法时出错！");break;
        }
        if(doId>0){
            //导入分词
            objectCache.setData(participleResult);
            objectCache.setDoId(doId);
            objectCache.setTag(classType);
        }else{
            //输入分词
            objectCache.setData(participleResult);
            objectCache.setDoId(-1);
        }
        result.setMsg("分词成功!");
        result.setCode(Constant.DO_SUCCESS);
        result.setData(participleResult);
        return result;
    }

    public Result saveParticipleRes() {
        Result result = new Result();
        Tcorpus tcorpus = new Tcorpus();
        if(objectCache.getDoId()>0){
        }
        return null;
    }
}
