package com.wcq.thang.bean;

import com.wcq.thang.config.Constant;

import java.io.*;
import java.util.List;

/**
 * @author wcq
 * @date 2019/11/7 10:58
 */
public class Utils {
    /**
     * 清洗
     * @param original
     * @param math 是否清洗数学运输符号
     * @return
     */
    public static String cleanFunction(String original,String math){
        char[] chars = original.toCharArray();
        String res = "";
        for (char ch : chars) {
            if(math.equals("on")){
                switch (ch) {
                    case ' ':
                    case '\b':
                    case '\n':
                    case '\r':
                    case '\t':
                    case '\\':
                    case '+':
                    case '-':
                    case '*':
                    case '/':
                    case '^':
                    case '<':
                    case '>':
                    case '&': break;
                    default:{
                        res += String.valueOf(ch);
                        break;
                    }
                }
            }else{
                switch (ch){
                    case ' ':
                    case '\b':
                    case '\n':
                    case '\r':
                    case '\t':
                    case '\\':break;
                    default:{
                        res += String.valueOf(ch);
                        break;
                    }
                }
            }
        }
        return res;
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

    /**
     * 把字符串解析成HTML可以显示的样式
     * @param str
     * @return
     */
    public static String makeStringToHTML(String str){
        char[] chars = str.toCharArray();
        String document = "&nbsp;&nbsp;&nbsp;&nbsp;";
        for (char ch:chars){
            if(ch == '\n'){
                document +="<br/>";
            }else if(ch == '\t'){
                document +="&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;";
            }else{
                document += String.valueOf(ch);
            }
        }
        return document;
    }
}
