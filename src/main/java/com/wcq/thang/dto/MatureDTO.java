package com.wcq.thang.dto;

import com.wcq.thang.model.User;
import lombok.Data;

import java.util.Date;

/**
 * @author wcq
 * @date 2019/12/6 13:34
 */
@Data
public class MatureDTO {
    private Integer matureId;
    private String tags;
    private String source;
    private Integer uploader;
    private Date date;
    private String content;
    private User user;
}
