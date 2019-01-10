package com.iot.device.model.ota;

import lombok.Data;
import lombok.ToString;

import java.util.Date;

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
public class UpgradePlanLog {
    /**
     * 日志id
     */
    private Long id;

    /**
     * 升级计划id
     */
    private Long planId;

    /**
     * 租户id
     */
    private Long tenantId;

    /**
     * 操作类型
     */
    private String operationType;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 创建人
     */
    private Long createBy;


}