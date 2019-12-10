package com.wcq.thang.config;

import org.springframework.beans.factory.annotation.Value;

/**
 * @author wcq
 * @date 2019/11/8 11:43
 */
public class Constant {
    /**
     * 语料存储位置
     */
    public static final String FILE_URL="D:/ThangFiles";


    /**
     * 操作成功状态码
     */
    public static final int DO_SUCCESS = 200;

    /**
     * 操作失败状态码
     */
    public static final int DO_FAIL = 250;

    /**
     * 语料类型
     */
    public static final String DOC ="doc";
    public static final String DOCX ="docx";
    public static final String XLS ="xls";
    public static final String XLSX ="xlsx";
    public static final String PDF ="pdf";
    public static final String TXT ="txt";

    /**
     * 未操作标志
     */
    public static final Boolean NO_DO =false;
    /**
     * 已操作标志
     */
    public static final Boolean DONE = true;

    /**
     * 编码
     */
    public static final String ENCODING = "UTF-8";

    /**
     * 下载细语料
     */
    public static final int DOWNLOAD_MATURE = 0;
    /**
     * 下载原始语料
     */
    public static final int DOWNLOAD_ORIGINAL = 1;
    /**
     * 下载清洗后的语料
     */
    public static final int DOWNLOAD_CLEANED = 2;

}
