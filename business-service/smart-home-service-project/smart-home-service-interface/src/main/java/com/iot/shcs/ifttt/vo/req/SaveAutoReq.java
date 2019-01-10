package com.iot.shcs.ifttt.vo.req;

import lombok.Data;

import java.io.Serializable;

/**
 * 描述：新增联动请求
 * 创建人： LaiGuiMing
 * 创建时间： 2018/4/23 15:19
 */
@Data
public class SaveAutoReq implements Serializable {
    private Long id;
    private String name;
    private Long userId;
    private Integer status; //启用状态 0=stop 1=running
    private String icon;
    private String triggerType; //场景类型 0=dev 1=timer
    private Integer isMulti; //是否跨网关 0=否 1=是
    private String directId;
    private String timeJson; //时间参数json字符串 时间范围/定时/天文定时
    private Long spaceId;
    private Long tenantId;
    private Long appletId;
    private Integer devSceneId;
    private Integer devTimerId;
    private Integer visiable; //是否可见
}
