package com.iot.device.vo.rsp.ota;

import com.iot.device.vo.req.ota.StrategyReportResp;

import java.util.Date;
import java.util.List;

public class StrategyReportVo {

    /**
     * 计划id
     */
    private Long planId;

    /**
     * 策略组
     */
    private Integer strategyGroup;
    /**
     * 总数
     */
    private Integer upgradeTotal;
    /**
     * 阀值
     */
    private Integer threshold;
    /**
     * 升级成功数量
     */
    private Integer success;
    /**
     * 升级失败数量
     */
    private Integer fail;

    /**
     * 升级范围
     */
    private Integer upgradeScope;
    /**
     * 策略组是否成功
     */
    private String strategyStatus;
    /**
     * 全部完成时间
     */
    private Date allCompleteTime;
    /**
     * 升级范围
     */
    private List<StrategyReportResp> strategyReportRespList;


}