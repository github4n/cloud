package com.iot.device.model.ota;

import lombok.Data;
import lombok.ToString;

/**
 * 项目名称：立达信IOT云平台
 * 模块名称：ota
 * 功能描述：ota
 * 创建人： nongchongwei
 * 创建时间：2018年07月24日 16:58
 * 修改人： nongchongwei
 * 修改时间：2018年07月24日 16:58
 */
@Data
@ToString
public class UpgradePlanDetail {
    /**
     * 明细id
     */
    private Long id;

    /**
     * 升级计划id
     */
    private Long planId;

    /**
     * 是否有过渡版本 0/1
     */
    private Integer hasTransition;

    /**
     * 租户id
     */
    private Long tenantId;
}