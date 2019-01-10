package com.iot.shcs.ifttt.vo.req;

import lombok.Data;

/**
 * 描述：联动列表请求
 * 创建人： LaiGuiMing
 * 创建时间： 2018/9/30 14:51
 */
@Data
public class AutoListReq {
    private Long userId;
    private Long spaceId;
    private Long tenantId;
}
