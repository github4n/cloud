package com.iot.ifttt.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * 描述：校验程序请求
 * 创建人： LaiGuiMing
 * 创建时间： 2018/9/27 11:07
 */
@Data
public class CheckAppletReq implements Serializable {
    private String type; // timer/dev
    private String msg;
}
