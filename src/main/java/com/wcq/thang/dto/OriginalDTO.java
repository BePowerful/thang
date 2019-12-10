package com.wcq.thang.dto;
import com.wcq.thang.model.User;
import lombok.Data;

import java.util.Date;

/**
 * @author wcq
 * @date 2019/11/9 13:52
 */
@Data
public class OriginalDTO {
    private Integer originalId;
    private String title;
    private String url;
    private Integer uploader;
    private Date date;
    private String source;
    private String originalFormat;
    private Boolean formatChanged;
    private String txtPath;
    private Boolean cleaned;
    private String cleanedPath;
    private User user;
}
