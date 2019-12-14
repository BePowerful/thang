package com.wcq.thang.service;

import com.wcq.thang.bean.ObjectCache;
import com.wcq.thang.bean.ParticipleUtil;
import com.wcq.thang.bean.Result;
import com.wcq.thang.bean.Utils;
import com.wcq.thang.config.Constant;
import com.wcq.thang.dto.ShowRetrievalResultDTO;
import com.wcq.thang.mapper.MatureMapper;
import com.wcq.thang.mapper.OriginalMapper;
import com.wcq.thang.mapper.UserMapper;
import com.wcq.thang.model.Original;
import com.wcq.thang.model.OriginalExample;
import com.wcq.thang.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * 语料清洗接口
 * @author wcq
 * @date 2019/12/7 8:57
 */
@Service
@Transactional
public class CorpusCleanService {
    @Autowired
    private OriginalMapper originalMapper;
    @Autowired
    private MatureMapper matureMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private ObjectCache objectCache;

    /**
     * 获取未清洗语料列表
     * 用于clean页面导入时在loading页面显示
     * @return
     */
    public List<ShowRetrievalResultDTO> getNoCleanAndFormatChangedOriginal() {
        //创建一个传输对象OriginalDTO的List
        List<ShowRetrievalResultDTO> showRetrievalResultDTOS = new ArrayList<>();
        //查找所有未清洗的语料，即cleaned =0
        OriginalExample example = new OriginalExample();
        OriginalExample.Criteria criteria = example.createCriteria();
        //导入的语料必须是未清洗的
        criteria.andCleanedEqualTo(Constant.NO_DO);
        //还必须是已经转换未txt的
        criteria.andFormatChangedEqualTo(Constant.DONE);
        List<Original> originals = originalMapper.selectByExample(example);
        //遍历原始语料集合
        for (Original original : originals) {
            //根据uploader值找到user
            User user = userMapper.selectByPrimaryKey(original.getUploader());
            //创建一个OriginalDTO对象
            ShowRetrievalResultDTO dto = new ShowRetrievalResultDTO();
            dto.setShowContent(original.getTitle());
            dto.setClassType("Original");
            dto.setTags("原始语料");
            dto.setSource(original.getSource());
            dto.setDate(original.getDate());
            dto.setId(original.getOriginalId());
            //设置user对象
            dto.setUser(user);
            showRetrievalResultDTOS.add(dto);
        }
        return showRetrievalResultDTOS;
    }

    /**
     * 离开loading页面时使用
     * 语料内容导入
     * 用于清洗
     * 通过id获取
     * @param id
     * @return
     */
    public String getOriginalTxtById(Integer id) {
        //获取文件url
        Original original = originalMapper.selectByPrimaryKey(id);
        String txtFilePath = original.getTxtPath();
        String content = Utils.readTxtFile(txtFilePath);
        if(content!=null)
            return  content;
        else
            return " ";
    }

    /**
     * 语料清洗接口
     * @param content
     * @return
     */
    public Result cleanOriginalOrOtherInput(String content,String ftj,String math,Integer doId) {
        Result result = new Result();
        //调用清洗方法，返回清洗结果
        String cleanResult = Utils.cleanFunction(content,math);
        if (cleanResult != null) {//清洗成功，设置语料清洗标志位
            if(ftj.equals("on")){//需要繁体转简体就转一下
                cleanResult = ParticipleUtil.ftoJ(cleanResult);
            }
            if(doId>0){//导入的语料
                objectCache.setDoId(doId);
                objectCache.setData(cleanResult);
            }
            result.setCode(Constant.DO_SUCCESS);
            result.setMsg("清洗成功!");
            result.setData(cleanResult);
        } else {
            result.setCode(Constant.DO_FAIL);
            result.setMsg("数据清洗失败!");
            result.setData("清洗失败！");
        }
        return result;
    }

    /**
     * 清洗后更新语料存储
     * @return
     */
    public Result seaveCleanResult() {
        Result result = new Result();
        //通过id找到原始语料文件
        Original original = originalMapper.selectByPrimaryKey(objectCache.getDoId());

        //创建存储,统一存储为txt格式
        File filePath = new File(Constant.FILE_URL);
        if (!filePath.exists())//如果不存在就创建
            filePath.mkdirs();
        String fileName = UUID.randomUUID().toString() + ".txt";
        String cleanedPath = filePath + "\\" + fileName;
        //存储
        if (Utils.writerTxtFile(cleanedPath,objectCache.getData().toString())) {
            //设置清洗后的语料存储位置
            original.setCleanedPath(cleanedPath);
            //设置语料已经清洗过了
            original.setCleaned(Constant.DONE);
            //更新原始语料
            int i = originalMapper.updateByPrimaryKeySelective(original);
            if(i==1){
                result.setCode(Constant.DO_SUCCESS);
                result.setMsg("更新系统存储成功！");
            }else{
                System.out.println("更新存储清洗语料时发生错误！");
                result.setCode(Constant.DO_FAIL);
                result.setMsg("更新系统存储失败");
            }
        } else {
            result.setCode(Constant.DO_FAIL);
            result.setMsg("更新系统存储失败");
        }
        return result;
    }
}
