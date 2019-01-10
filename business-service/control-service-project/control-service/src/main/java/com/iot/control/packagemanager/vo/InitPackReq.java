package com.iot.control.packagemanager.vo;

import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * 描述：
 * 创建人： LaiGuiMing
 * 创建时间： 2018/10/12 15:30
 */
@Data
@Builder
public class InitPackReq {
    private Long productId;
    private Long tenantId;
    private String userUuId;
    private Long spaceId;
    private String directDeviceId;
    private List<String> devIds;
}
