package com.iot.shcs.ifttt.vo.res;

import lombok.Data;

import java.util.Date;

/**
 * 描述：联动列表应答
 * 创建人： LaiGuiMing
 * 创建时间： 2018/9/30 14:46
 */
@Data
public class AutoListResp {
    private Long id;
    private String name;
    private Integer status; //启用状态 0=stop 1=running
    private String icon;
    private String type;
    private String timeJson; //时间参数json字符串 时间范围/定时/天文定时
    private Date createTime;
    private Integer devSceneId;
    private Integer devTimerId;
}
