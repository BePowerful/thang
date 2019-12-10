package com.wcq.thang.service;

import com.wcq.thang.config.Constant;
import com.wcq.thang.mapper.MatureMapper;
import com.wcq.thang.mapper.OriginalMapper;
import com.wcq.thang.mapper.UserMapper;
import com.wcq.thang.model.Original;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;

/**
 * 公共服务接口
 * @author wcq
 * @date 2019/12/7 9:01
 */
@Service
@Transactional
public class CommonService {
    @Autowired
    private OriginalMapper originalMapper;
    @Autowired
    private MatureMapper matureMapper;
    @Autowired
    private UserMapper userMapper;

    /**
     * 语料下载接口
     * @param response
     * @param id 语料id
     * @param type //语料下载类型
     */
    public void downloadFile(HttpServletResponse response, Integer id, int type) {
        String url = null;
        String title = null;
        if(type== Constant.DOWNLOAD_MATURE){
            //细语料下载暂不提供
//            Mature mature = matureMapper.selectByPrimaryKey(id);
//            title = "细语料"+id;

        }else if(type == Constant.DOWNLOAD_ORIGINAL){
            Original original = originalMapper.selectByPrimaryKey(id);
            url = original.getUrl();
            title = original.getTitle()+"."+original.getOriginalFormat();
        }else if(type == Constant.DOWNLOAD_CLEANED){
            Original original = originalMapper.selectByPrimaryKey(id);
            url = original.getCleanedPath();
            title = original.getTitle()+".txt";
        }else{
            System.out.println("下载类型错误");
            return;
        }
        File file = new File(url);
        if (file.exists()) {
            FileInputStream fis = null;
            BufferedInputStream bis = null;
            // 配置文件下载
            response.setHeader("content-type", "application/octet-stream");
            response.setContentType("application/octet-stream");
            //response.setContentType("application/force-download");// 设置强制下载不打开
            try {
                response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(url, "UTF-8"));
                //response.setHeader("Content-Disposition", "attachment;fileName=" + new String(title.getBytes("GB2312"), "ISO-8859-1"));
                byte[] buffer = new byte[1024];
                fis = new FileInputStream(file);
                bis = new BufferedInputStream(fis);
                OutputStream os = response.getOutputStream();
                int i = bis.read(buffer);
                while (i != -1) {
                    os.write(buffer, 0, i);
                    i = bis.read(buffer);
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (bis != null) {
                    try {
                        bis.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                if (fis != null) {
                    try {
                        fis.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }



}
