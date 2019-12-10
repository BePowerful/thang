package com.wcq.thang.bean;

import com.wcq.thang.config.Constant;

import java.io.*;

/**
 * @author wcq
 * @date 2019/11/7 10:58
 */
public class Utils {
    /**
     * 数据清洗方法
     * @param original
     * @return
     */
    public static String cleanFunction(String original){
        //清洗方法
        //....
        //返回清洗结果
        return "这是清洗结果";
    }

    /**
     * 读取文件
     * @param txtFilePath
     * @return
     */
    public static String readTxtFile(String txtFilePath){
        if(txtFilePath !=null){
            File file = new File(txtFilePath);
            if(file.exists()){
                Long size = file.length();
                byte[] fileContent = new byte[size.intValue()];
                try {
                    FileInputStream fileInputStream = new FileInputStream(file);
                    fileInputStream.read(fileContent);
                    fileInputStream.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    return new String(fileContent,Constant.ENCODING);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                    return null;
                }
            }else{
                System.out.println("读取文件发生错误，文件内容为空！");
                return null;
            }
        }else{
            System.out.println("读取文件发生错误，路径为空！");
            return null;
        }
    }

    /**
     * 更新或创建存储文件
     * @param content
     * @param url
     * @return
     */
    public static boolean uploadFile(String content,String url){
        BufferedWriter bufferedWriter = null;
        try {
            bufferedWriter = new BufferedWriter(new FileWriter(url));
            bufferedWriter.write(content);
            bufferedWriter.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }finally {
            if(bufferedWriter!=null){
                try {
                    bufferedWriter.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    /**
     * 写文件公共代码抽取
     * @param txtPath
     * @param buffer
     * @return
     */
    public static boolean writerTxtFile(String txtPath,String buffer) {
        FileOutputStream fileOutputStream = null;
        OutputStreamWriter outputStreamWriter = null;
        try {
            //打开文件输出流
            fileOutputStream = new FileOutputStream(txtPath);
            //打开文件写
            outputStreamWriter = new OutputStreamWriter(fileOutputStream, Constant.ENCODING);
            //写文件
            outputStreamWriter.write(buffer);
            //关闭文件写
            outputStreamWriter.close();
            //关闭文件输出流
            fileOutputStream.close();
            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } finally {
            if (outputStreamWriter != null) {
                try {
                    outputStreamWriter.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (fileOutputStream != null) {
                try {
                    fileOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
