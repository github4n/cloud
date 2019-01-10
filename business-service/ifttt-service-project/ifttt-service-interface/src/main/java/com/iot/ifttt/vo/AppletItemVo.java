package com.iot.ifttt.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * 描述：程序子项类
 * 创建人： LaiGuiMing
 * 创建时间： 2018/9/27 10:38
 */
@Data
public class AppletItemVo implements Serializable {

    private Long id;
    /**
     * 事件主键，this_id/that_id
     */
    private Long eventId;
    /**
     * 规则体
     */
    private String json;
}
