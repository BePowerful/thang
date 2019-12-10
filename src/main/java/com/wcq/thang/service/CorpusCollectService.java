package com.wcq.thang.service;

import com.wcq.thang.bean.FormatConversion;
import com.wcq.thang.bean.Result;
import com.wcq.thang.config.Constant;
import com.wcq.thang.mapper.MatureMapper;
import com.wcq.thang.mapper.OriginalMapper;
import com.wcq.thang.model.Mature;
import com.wcq.thang.model.Original;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.UUID;

/**
 * 语料收集接口
 * 粗语的上传和格式转化
 * 细语料的收集
 * @author wcq
 * @date 2019/12/7 8:55
 */
@Service
@Transactional
public class CorpusCollectService {
    @Autowired
    private OriginalMapper originalMapper;
    @Autowired
    private MatureMapper matureMapper;

    /**
     * 原始(粗)语料上传接口
     * @param title
     * @param source
     * @param file
     * @return
     */
    public Result uploadOriginal(String title, String source, MultipartFile file){
        Result result = new Result();
        //语料上传者
        Integer uploader = 1;
        //获取语料文件,保存在服务器中并且获取保存地址
        //创建文件在服务器端的存放路径
        File filePath = new File(Constant.FILE_URL);
        if (!filePath.exists())//如果不存在就创建
            filePath.mkdirs();
        if(title==null || title.equals("")){//如果用户未提供文件名，title则使用上传上来的
            title = file.getOriginalFilename().substring(0, file.getOriginalFilename().indexOf("."));
        }
        //1.得到文件后缀(扩展名.txt/.pdf...)
        String fileSuffix = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
        //得到文件类型
        String originalFormat = fileSuffix.substring(1);
        //2.生成一个唯一的名字
        String fileName = UUID.randomUUID().toString() + fileSuffix;
        //3.封装一下文件
        //文件存储路劲
        String url = filePath + "\\" + fileName;
        File aimFile = new File(url);
        try {
            //4.上传
            file.transferTo(aimFile);
            //创建原始语料模型
            Original original = new Original();
            //设置语料标题
            original.setTitle(title);
            //设置上传日期
            original.setDate(new Date());
            //设置上传者
            original.setUploader(uploader);
            //设置来源
            original.setSource(source);
            //设置存储路径
            original.setUrl(url);
            //设置未清洗标志
            original.setCleaned(Constant.NO_DO);
            //设置语料格式
            original.setOriginalFormat(originalFormat);
            //如果上传的是txt类型的，就设置格式转换标志位为done,同时设置txtPath为原始语料存储地址
            if (original.getOriginalFormat().equals("txt")) {
                original.setFormatChanged(Constant.DONE);
                original.setTxtPath(original.getUrl());
            } else {//不是则需要进行格式转换
                String txtPath = null;
                switch (originalFormat){
                    case Constant.DOC:
                    case Constant.DOCX: {
                        //返回的txt文件路径
                        txtPath = FormatConversion.docOrDocxToTxt(original.getUrl());
                        if(txtPath!=null){
                            original.setTxtPath(txtPath);
                            original.setFormatChanged(Constant.DONE);
                        }else{
                            System.out.println("格式转换失败");
                        }
                        break;
                    }
                    case Constant.PDF: {
                        txtPath = FormatConversion.pdfToTxt(original.getUrl());
                        if(txtPath!=null){
                            original.setTxtPath(txtPath);
                            original.setFormatChanged(Constant.DONE);
                        }else{
                            System.out.println("格式转换失败");
                        }
                        break;
                    }
                    case Constant.XLS:
                    case Constant.XLSX: {
                        txtPath = FormatConversion.xlsOrXlsxToTxt(original.getUrl());
                        if(txtPath!=null){
                            original.setTxtPath(txtPath);
                            original.setFormatChanged(Constant.DONE);
                        }else{
                            System.out.println("格式转换失败");
                        }
                        break;
                    }
                    default: {
                        System.out.println("不支持此种格式");
                        break;
                    }
                }
            }
            //各种属性设置完成，保存到数据库
            originalMapper.insertSelective(original);
            result.setCode(Constant.DO_SUCCESS);
            result.setMsg("上传成功！");
            return result;
        } catch (IOException e) {
            e.printStackTrace();
            result.setCode(Constant.DO_FAIL);
            result.setMsg("服务器异常！");
            return result;
        }
    }

    /**
     * 细语料收录接口
     * @param tags
     * @param source
     * @param content
     * @return
     */
    public Result uploadMature(String tags, String source, String content){
        Mature mature = new Mature();
        //设置标签
        mature.setTags(tags);
        //设置来源
        mature.setSource(source);
        //设置上传者
        mature.setUploader(1);
        //设置时间
        mature.setDate(new Date());
        //设置内容
        mature.setContent(content);
        //保存到数据库
        Result result = new Result();
        if(matureMapper.insertSelective(mature)==1){
            result.setCode(Constant.DO_SUCCESS);
            result.setMsg("收录成功！");
        }else{
            result.setCode(Constant.DO_FAIL);
            result.setMsg("收录失败！");
        }
        return result;
    }
}
