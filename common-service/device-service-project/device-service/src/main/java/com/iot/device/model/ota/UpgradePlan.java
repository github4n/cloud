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
public class UpgradePlan {
    /**
     * 计划id
     */
    private Long id;

    /**
     * 租户id
     */
    private Long tenantId;

    /**
     * 产品id
     */
    private Long productId;

    /**
     * 启动Start 暂停Pause
     */
    private String planStatus;

    /**
     * 升级方式  推送升级Push(默认) 强制升级Force
     */
    private String upgradeType;

    /**
     * 升级版本号
     */
    private String targetVersion;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 创建人
     */
    private Long createBy;

    /**
     * 修改时间
     */
    private Date updateTime;

    /**
     * 修改人
     */
    private Long updateBy;

    /**
     * 编辑次数
     */
    private Integer editedTimes;

    /**
     * 策略开关
     */
    private Integer strategySwitch;

    /**
     * 升级范围
     */
    private Integer upgradeScope;

}