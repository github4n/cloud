package com.iot.shcs.device.queue.utils;

/**
 * @Author: lucky
 * @Descrpiton:
 * @Date: 17:54 2018/10/29
 * @Modify by:
 */
public interface EventConstants {

    //倒计时功能事件
    String COUNT_DOWN_EXEC_EVENT = "CountDownExecEvent";

    //运行时间报表功能事件
    String UPDATE_RUNTIME_EVENT = "UpdateRuntimeEvent";

    //电量报表功能事件
    String UPDATE_ENERGY_EVENT = "UpdateEnergyEvent";

    String TAMPER_EVENT = "tamper";
}
