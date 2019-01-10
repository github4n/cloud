package com.iot.shcs.security.queue.utils;

/**
 * @Author: lucky
 * @Descrpiton: device 相关队列
 * @Date: 10:37 2018/8/10
 * @Modify by:
 */
public interface SecurityQueueConstants {

    //getStatusResp
    String GET_STATUS_RESP_EXCHANGE="get-status-resp-exchange";
    String TWO_C_GET_STATUS_RESP_QUEUE = "get-status-resp-queue";
    String GET_STATUS_RESP_ROUTING="get-status-resp-routing";

    //setArmModeResp
    String SET_ARM_MODE_RESP_EXCHANGE="set-arm-mode-resp-exchange";
    String SET_ARM_MODE_RESP_ROUTING="set-arm-mode-resp-routing";
    String TWO_C_SET_ARM_MODE_RESP_QUEUE="set-arm-mode-resp-routing";
}
