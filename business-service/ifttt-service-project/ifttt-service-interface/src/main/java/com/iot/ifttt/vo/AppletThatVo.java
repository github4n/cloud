package com.iot.ifttt.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 描述：
 * 创建人： LaiGuiMing
 * 创建时间： 2018/9/28 14:43
 */
@Data
public class AppletThatVo implements Serializable {

    private Long id;
    /**
     * 程序主键
     */
    private Long appletId;
    /**
     * 服务标识
     */
    private String serviceCode;
    /**
     * 触发码
     */
    private String code;

    private List<AppletItemVo> items;
}
