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
public class UpgradePlanTransition {
    /**
     * id
     */
    private Long id;

    /**
     * 明细id
     */
    private Long detailId;

    /**
     * 过渡版本号
     */
    private String transitionVersion;

    /**
     * 租户id
     */
    private Long tenantId;
}