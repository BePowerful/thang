package com.wcq.thang.bean;

import lombok.Data;

import java.io.Serializable;

/**
 * @author wcq
 * @date 2019/11/6 19:45
 */
@Data
public class Result implements Serializable {
    /**
     * 操作状态
     * */
    private int code;
    /**
     * 操作结果提示信息
     */
    private String msg;
    /**
     * 操作对象
     */
    private Object data;
}
