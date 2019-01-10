package com.iot.shcs.ota.service;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.iot.common.exception.BusinessException;
import com.iot.common.exception.RemoteCallBusinessException;
import com.iot.common.util.CommonUtil;
import com.iot.common.util.StringUtil;
import com.iot.control.device.vo.resp.ListUserDeviceInfoRespVo;
import com.iot.device.api.DeviceApi;
import com.iot.device.api.DeviceCoreApi;
import com.iot.device.api.OTAServiceApi;
import com.iot.device.enums.ota.*;
import com.iot.device.util.RedisKeyUtil;
import com.iot.device.vo.req.device.ListDeviceByParamsReq;
import com.iot.device.vo.req.ota.BatchIUpgradeDeviceVersion;
import com.iot.device.vo.req.ota.UpgradeDeviceVersionReq;
import com.iot.device.vo.req.ota.UpgradeLogAddBatchReq;
import com.iot.device.vo.req.ota.UpgradeLogAddReq;
import com.iot.device.vo.rsp.DeviceResp;
import com.iot.device.vo.rsp.device.GetProductInfoRespVo;
import com.iot.device.vo.rsp.device.ListDeviceByParamsRespVo;
import com.iot.device.vo.rsp.ota.StrategyConfigResp;
import com.iot.device.vo.rsp.ota.UpgradeInfoResp;
import com.iot.device.vo.rsp.ota.UpgradePlanResp;
import com.iot.file.api.FileApi;
import com.iot.file.dto.FileDto;
import com.iot.mqttsdk.MqttSdkService;
import com.iot.mqttsdk.common.CallBackProcessor;
import com.iot.mqttsdk.common.DeadBackProcessor;
import com.iot.mqttsdk.common.MqttMsg;
import com.iot.mqttsdk.common.MqttMsgAck;
import com.iot.redis.RedisCacheUtil;
import com.iot.shcs.common.util.MQTTUtils;
import com.iot.shcs.contants.ModuleConstants;
import com.iot.shcs.device.service.impl.DeviceCoreService;
import com.iot.shcs.helper.DispatcherRouteHelper;
import com.iot.shcs.listener.MQTTClientListener;
import com.iot.shcs.ota.utils.OtaUpgradeCheckEnum;
import com.iot.shcs.ota.vo.OtaDeadInfo;
import com.iot.shcs.ota.vo.StrategyInfoVo;
import com.iot.shcs.ota.vo.req.*;
import com.iot.shcs.ota.vo.resp.*;
import com.iot.user.api.UserApi;
import com.iot.user.exception.UserExceptionEnum;
import com.iot.user.vo.FetchUserResp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 项目名称：IOT云平台
 * 模块名称：ota
 * 功能描述：ota
 * 创建人： nongchongwei
 * 创建时间：2018年07月24日 16:58
 * 修改人： nongchongwei
 * 修改时间：2018年07月24日 16:58
 */
@Service("otaDead")
public class OTADeadMQTTService implements DeadBackProcessor {

    public static final Logger LOGGER = LoggerFactory.getLogger(OTADeadMQTTService.class);

    public static final int QOS = 1;
    @Autowired
    @Qualifier("ota")
    private OTAMQTTService otaMQTTService;
    @Override
    public void onMessage(String message) {
        logger.error("message->{}",message);
        if(StringUtil.isNotBlank(message)){
            try {
                OtaDeadInfo otaDeadInfo =  JSON.parseObject(message, OtaDeadInfo.class);
                String devId = otaDeadInfo.getDevId();
                String key = RedisKeyUtil.getUpgradeDeviceStatusKey(devId);
                String stage = RedisCacheUtil.hashGetString(key, ModuleConstants.OTA_UPGRADE_STATUS_STAGE);
                /**stage为空表示没有升级过，或设备已经上报成功或失败**/
                if(StringUtil.isNotBlank(stage)){
                    /**处理策略升级的情况**/
                    otaMQTTService.handleStrategyUpgradeRecord(devId,OtaStageEnum.FAILED.getValue(),"TIMEOUT");
                    /**处理升级日志*/
                    otaMQTTService.handleUpgradeLog(devId,OtaStageEnum.FAILED.getValue(),key,otaDeadInfo.getVer(),"TIMEOUT");
                }
            } catch (Exception e) {
                logger.error("JSON parse error->", e);
            }
        }
    }




}
