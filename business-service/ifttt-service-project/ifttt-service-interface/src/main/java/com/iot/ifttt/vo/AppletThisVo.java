package com.iot.ifttt.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 描述：
 * 创建人： LaiGuiMing
 * 创建时间： 2018/9/28 14:42
 */
@Data
public class AppletThisVo implements Serializable {
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
     * 逻辑 or/and
     */
    private String logic;
    /**
     * 传参
     */
    private String param;

    private List<AppletItemVo> items;
}
