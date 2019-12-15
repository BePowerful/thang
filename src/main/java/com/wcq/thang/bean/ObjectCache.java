package com.wcq.thang.bean;

import lombok.Data;
import org.springframework.stereotype.Component;

/**
 * @author wcq
 * @date 2019/12/14 17:27
 */
@Component
@Data
public class ObjectCache {
    private Object data;
    private Integer doId;
    private String tag;
}
