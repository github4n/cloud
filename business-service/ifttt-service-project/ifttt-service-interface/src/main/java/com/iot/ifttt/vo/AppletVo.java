package com.iot.ifttt.vo;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import lombok.Data;

/**
 * 描述：保存程序请求
 * 创建人： LaiGuiMing
 * 创建时间： 2018/9/27 10:33
 */
@Data
public class AppletVo implements Serializable {
    private Long id;
    /**
     * 名称
     */
    private String name;
    /**
     * 状态 0on  1off
     */
    private String status;
    /**
     * 创建者主键
     */
    private Long createBy;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 创建时间
     */
    private Long ruleId;

    private List<AppletThisVo> thisList;

    private List<AppletThatVo> thatList;
}
