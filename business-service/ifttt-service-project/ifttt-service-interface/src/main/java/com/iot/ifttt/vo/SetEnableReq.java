package com.iot.ifttt.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * 描述：
 * 创建人： LaiGuiMing
 * 创建时间： 2018/9/27 11:30
 */
@Data
public class SetEnableReq implements Serializable {
    private Long id;
    private String status;
}
