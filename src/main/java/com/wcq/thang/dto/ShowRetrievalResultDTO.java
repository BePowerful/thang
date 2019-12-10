package com.wcq.thang.dto;

import com.wcq.thang.model.User;
import lombok.Data;

import java.util.Date;

/**
 * @author wcq
 * @date 2019/12/7 11:56
 */
@Data
public class ShowRetrievalResultDTO {
    /**
     * 主要显示的内容
     */
    private String showContent;
    /**
     * 语料相关用户
     */
    private User user;
    /**
     * 语料上传时间
     */
    private Date date;
    /**
     * 语料标签
     */
    private String tags;
    /**
     * 粗预料还是细语料
     */
    private String classType;
    /**
     * 对应语料的id
     */
    private Integer id;
    /**
     * 语料来源
     */
    private String source;
}
