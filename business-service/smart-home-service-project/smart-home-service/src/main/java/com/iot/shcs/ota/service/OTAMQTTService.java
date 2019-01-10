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
import com.iot.device.vo.req.ota.*;
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
import com.iot.mqttsdk.common.MqttMsg;
import com.iot.mqttsdk.common.MqttMsgAck;
import com.iot.redis.RedisCacheUtil;
import com.iot.shcs.common.util.MQTTUtils;
import com.iot.shcs.contants.ModuleConstants;
import com.iot.shcs.device.service.impl.DeviceCoreService;
import com.iot.shcs.helper.DispatcherRouteHelper;
import com.iot.shcs.listener.MQTTClientListener;
import com.iot.shcs.ota.exception.OtaExceptionEnum;
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
@Service("ota")
public class OTAMQTTService implements CallBackProcessor {

    public static final Logger LOGGER = LoggerFactory.getLogger(OTAMQTTService.class);

    public static final int QOS = 1;
    @Autowired
    private MqttSdkService mqttSdkService;

    @Autowired
    private OTAServiceApi otaServiceApi;

    @Autowired
    private DeviceApi deviceApi;

    @Autowired
    private DeviceCoreApi deviceCoreApi;

    @Autowired
    private DeviceCoreService deviceCoreService;

    @Autowired
    private FileApi fileApi;

    @Autowired
    private UserApi userApi;

    @Autowired
    private OTAService otaService;

    private static ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1);

    /** 线程池 */
    private static ExecutorService executor = Executors.newCachedThreadPool();

    @Override
    public void onMessage(MqttMsg mqttMsg) {
        DispatcherRouteHelper.dispatch(this, mqttMsg);
    }

    /**
     * 功能描述:8.7执行OTA请求  iot/v1/s/[userId]/ota/excOtaReq
     * @param: [msg, reqTopic]
     * @return: void
     * @auther: nongchongwei
     * @date: 2018/11/28 17:49
     */
    public void excOtaReq(MqttMsg msg, String reqTopic) {
        LOGGER.info("###otalog### OTAMQTTService excOtaReq msg{};reqTopic: {}", msg, reqTopic);
        OtaExcOtaReq payload = JSON.parseObject(msg.getPayload().toString(), OtaExcOtaReq.class);
        List<String> deviceIdList = payload.getDevId();

        if (CollectionUtils.isEmpty(deviceIdList)) {
            LOGGER.info("###otalog### OTAMQTTService excOtaReq deviceIdList:{}", deviceIdList);
            return;
        }
        List<DeviceResp> deviceRespList = deviceApi.getVersionByDeviceIdList(deviceIdList);
        LOGGER.info("###otalog### OTAMQTTService excOtaReq deviceRespList:{}", JSON.toJSONString(deviceRespList));
        if (!CollectionUtils.isEmpty(deviceRespList)) {
            /** key:productId:version value: 直连设备设备IdList*/
            Map<String, List<String>> directDeviceMap = new HashMap<>();
            /** productId:version */
            List<String> productVersionList = new ArrayList<>();
            /** key:productId:version value: map ：key：二级设备的父设备Id  val：子设备的设备IdList*/
            Map<String, Map<String, List<String>>> subDeviceMap = new HashMap<>();
            /**组装升级数据*/
            buildUpgradeData(deviceRespList,productVersionList,directDeviceMap,subDeviceMap);
            /**设备升级*/
            deviceUpgrade(productVersionList,directDeviceMap,subDeviceMap);
        }
    }
    /**
     * 功能描述:8.7执行OTA请求  设备升级
     * @param:
     * @return: void
     * @auther: nongchongwei
     * @date: 2018/11/28 17:49
     */
    private void deviceUpgrade(List<String> productVersionList, Map<String, List<String>> directDeviceMap, Map<String, Map<String, List<String>>> subDeviceMap) {
        LOGGER.info("###otalog### OTAMQTTService excOtaReq productVersionList:{}", JSON.toJSONString(productVersionList));
        Map<String, UpgradeInfoResp> productVersionUpgradeInfoRespMap = otaServiceApi.getNextVersionMap(productVersionList);
        LOGGER.info("###otalog### OTAMQTTService excOtaReq productVersionUpgradeInfoRespMap:{}", JSON.toJSONString(productVersionUpgradeInfoRespMap));
        LOGGER.info("###otalog### OTAMQTTService excOtaReq directDeviceMap:{}", JSON.toJSONString(directDeviceMap));
        /**直连设备升级*/
        if (null != directDeviceMap && !directDeviceMap.isEmpty()) {
            for (String key : directDeviceMap.keySet()) {
                UpgradeInfoResp upgradeInfoResp = productVersionUpgradeInfoRespMap.get(key);
                List<String> deviceIdsList = directDeviceMap.get(key);
                Long productId = Long.parseLong(key.split(":")[0]);
                String version = key.split(":")[1];
                /**升级*/
                for (String deviceId : deviceIdsList) {
                    doNoticeDevice(upgradeInfoResp, version, deviceId, null, productId, OtaUpgradeCheckEnum.FP.getType());
                }
            }
        }
        /**子设备升级*/
        LOGGER.info("###otalog### OTAMQTTService excOtaReq subDeviceMap:{}", JSON.toJSONString(subDeviceMap));
        if (null != subDeviceMap && !subDeviceMap.isEmpty()) {
            for (String key : subDeviceMap.keySet()) {
                UpgradeInfoResp upgradeInfoResp = productVersionUpgradeInfoRespMap.get(key);
                Long productId = Long.parseLong(key.split(":")[0]);
                String version = key.split(":")[1];
                /**升级*/
                Map<String, List<String>> subMap = subDeviceMap.get(key);
                for (String dictDevId : subMap.keySet()) {
                    doNoticeDevice(upgradeInfoResp, version, dictDevId, subMap.get(dictDevId), productId, OtaUpgradeCheckEnum.FP.getType());
                }
            }
        }
    }

    /**
     * 功能描述:8.7执行OTA请求  组装升级数据
     * @param:
     * @return: void
     * @auther: nongchongwei
     * @date: 2018/11/28 17:49
     */
    private void buildUpgradeData(List<DeviceResp> deviceRespList, List<String> productVersionList, Map<String, List<String>> directDeviceMap, Map<String, Map<String, List<String>>> subDeviceMap) {
        deviceRespList.forEach(
                deviceResp -> {
                    String key = deviceResp.getProductId() + ":" + deviceResp.getVersion();
                    productVersionList.add(key);
                    if (DeviceResp.IS_DIRECT_DEVICE.equals(deviceResp.getIsDirectDevice())) {
                        List<String> directDeviceList = directDeviceMap.get(key);
                        if (CollectionUtils.isEmpty(directDeviceList)) {
                            directDeviceList = new ArrayList<>();
                            directDeviceMap.put(key, directDeviceList);
                        }
                        directDeviceList.add(deviceResp.getDeviceId());
                    } else if (DeviceResp.IS_NOT_DIRECT_DEVICE.equals(deviceResp.getIsDirectDevice())) {
                        Map<String, List<String>> subDeviceIdMap = subDeviceMap.get(key);
                        if (null == subDeviceIdMap) {
                            subDeviceIdMap = new HashMap<>();
                            subDeviceMap.put(key, subDeviceIdMap);
                        }
                        List<String> subDeviceIdList = subDeviceIdMap.get(deviceResp.getParentId());
                        if (CollectionUtils.isEmpty(subDeviceIdList)) {
                            subDeviceIdList = new ArrayList<>();
                            subDeviceIdMap.put(deviceResp.getParentId(), subDeviceIdList);
                        }
                        subDeviceIdList.add(deviceResp.getDeviceId());
                    }
                }
        );
    }

    /**
     * 功能描述:8.1执行OTA通知   云--> 网关
     * @param: [fwType, directDeviceId, deviceIds, version, url, mode, md5, seq, srcAddr]
     * @return: void
     * @auther: nongchongwei
     * @date: 2018/11/28 17:53
     */
    protected void excOtaNotif(Integer fwType,String directDeviceId,List<String> deviceIds,
                               String version,String url,Integer mode,String md5, String seq, String srcAddr) {
        LOGGER.info("###otalog### fwType->{},directDeviceId->{},deviceIds->{},version->{},url->{},mode->{},md5->{}",
                fwType, directDeviceId,deviceIds,version,url,mode,md5);
        String respTopic = "iot/v1/c/" + directDeviceId + "/ota/excOtaNotif";
        MqttMsg msp = new MqttMsg();
        msp.setService("ota");
        msp.setMethod("excOtaNotif");
        if (StringUtils.isEmpty(seq)) {
            seq = StringUtil.getRandomString(6);
        }
        msp.setSeq(seq);
        if (!StringUtils.isEmpty(srcAddr)) {
            msp.setSrcAddr(srcAddr);
        } else {
            msp.setSrcAddr(MQTTUtils.DEFAULT_CLOUD_SOURCE_ADDR);
        }
        if (CollectionUtils.isEmpty(deviceIds)) {
            //发送给直连设备自己本身升级
            deviceIds = new ArrayList<>();
            deviceIds.add(directDeviceId);
        }

        List<OtaNotifResp> otaList = Lists.newArrayList();
        OtaNotifResp ota = new OtaNotifResp(deviceIds, fwType, version, md5, url, mode);
        otaList.add(ota);
        OtaNotifPayloadResp payload = new OtaNotifPayloadResp(otaList);

        MqttMsgAck ack = MqttMsgAck.successAck();
        msp.setAck(ack);
        msp.setPayload(payload);
        msp.setTopic(respTopic);

        LOGGER.info("###otalog### excOtaNotif-发送MQTT到直连设备({}, {})",JSON.toJSONString(msp), respTopic);
        mqttSdkService.sendMessage(MQTTClientListener.getMqttClientId(), msp, QOS);
    }
    /**
     * 描述：8.5获取版本列表请求   app --->cloud   reqTopic iot/v1/s/" + userId + "/ota/getVerListReq
     * @author nongchongwei
     * @date 2018/10/23 10:46
     * @param
     * @return
     */
    public void getVerListReq(MqttMsg msg, String reqTopic) {
        LOGGER.info("getVerListReq({}, {})", JSON.toJSONString(msg), reqTopic);
        if(CommonUtil.isEmpty(msg.getPayload())){
            LOGGER.info("####ota#### getVerListReq Payload is null");
            return;
        }
        OtaVersionReq otaVersionReq = JSON.parseObject(msg.getPayload().toString(), OtaVersionReq.class);
        List<OtaVersionResp> otaDevList = new ArrayList<>();
        MqttMsgAck ack = MqttMsgAck.successAck();
        try {
            /**参数校验**/
            FetchUserResp user = this.checkParam(otaVersionReq,reqTopic);
            /**获取相应设备信息*/
            List<ListDeviceByParamsRespVo> deviceParamList = this.getDeviceList(user,otaVersionReq);
            /**获取设备id和版本*/
            Long productId = deviceParamList.get(0).getProductId();
            List<String> deviceIdList = new ArrayList<>();
            List<String> versionList = new ArrayList<>();
            deviceParamList.forEach(
                    deviceResp -> {
                        deviceIdList.add(deviceResp.getUuid());
                    }
            );
            if (CollectionUtils.isEmpty(deviceIdList)) {
                logger.info("####ota#### getVerListReq deviceIdList is null");
                return;
            }
            List<DeviceResp> deviceList = deviceApi.getVersionByDeviceIdList(deviceIdList);
            deviceList.forEach(
                    device -> {
                        versionList.add(device.getVersion());
                    }
            );
            /**策略升级 uuid升级 批次升级时需要过滤 begin**/
            List<String> filterDevId = new ArrayList<>();
            /**查询计划**/
            UpgradePlanResp upgradePlanResp = otaServiceApi.getUpgradePlanByProductId(productId);
            Integer currentGroupVal = -1;
            if(null != upgradePlanResp && PlanStatusEnum.Start.getValue().equals(upgradePlanResp.getPlanStatus())){
                currentGroupVal = filterByStrategy(upgradePlanResp,false,filterDevId);
            }
            /**策略升级 uuid升级 批次升级时需要过滤 end**/
            /**组装返回信息**/
            buildOtaDevList(productId,versionList,currentGroupVal,deviceList,otaDevList,filterDevId,upgradePlanResp);
        } catch (RemoteCallBusinessException e) {
            LOGGER.error("business error", e);
            ack = MqttMsgAck.failureAck(400, e.getMessage());
        } catch (Exception e) {
            LOGGER.error("system error", e);
            ack = MqttMsgAck.failureAck(500, "system error");
        } finally {
            OtaVersionListPayloadResp payloadResp = new OtaVersionListPayloadResp(otaDevList);
            //2.8.6做出回应  cloud -->app
            msg.setPayload(payloadResp);
            msg.setAck(ack);
            getVerListResp(msg, reqTopic);
        }

    }
    /**
     * 描述：8.5获取版本列表请求   组装返回数据
     * @author nongchongwei
     * @date 2018/10/23 10:46
     * @param
     * @return
     */
    private void buildOtaDevList(Long productId, List<String> versionList, Integer currentGroupVal, List<DeviceResp> deviceList, List<OtaVersionResp> otaDevList, List<String> filterDevId, UpgradePlanResp upgradePlanResp ) {
        if (null != versionList && versionList.size() > 0) {
            final List<String> filterDevIdList = filterDevId;
            Map<String, UpgradeInfoResp> upgradeInfoRespMap = otaServiceApi.getUpgradeInfoRespMap(productId, versionList);
            final Integer currentGroup = currentGroupVal;
            deviceList.forEach(
                    device -> {
                        String oldVersion = device.getVersion();
                        String newVersion = oldVersion;
                        if (StringUtil.isNotBlank(oldVersion)) {
                            UpgradeInfoResp upgradeInfoResp = upgradeInfoRespMap.get(oldVersion);
                            if (null == upgradeInfoResp || PlanStatusEnum.Pause.getValue().equals(upgradeInfoResp.getPlanStatus())) {
                                //计划暂停 旧版本就是最新的
                                OtaVersionResp otaVersionResp = new OtaVersionResp(oldVersion, oldVersion, device.getDeviceId(), 0, 0, 0, "");
                                otaDevList.add(otaVersionResp);
                            }else if( ((StrategySwitchEnum.USE.getValue().equals(upgradePlanResp.getStrategySwitch()) && UpgradeScopeEnum.FULL.getValue().equals(upgradePlanResp.getUpgradeScope())
                                    && !ModuleConstants.OTA_UPGRADE_STRATEGY_COMPLETE_GROUP.equals(currentGroup)  && !currentGroup.equals(-1))
                                    || (StrategySwitchEnum.UNUSE.getValue().equals(upgradePlanResp.getStrategySwitch()) && UpgradeScopeEnum.UUID.getValue().equals(upgradePlanResp.getUpgradeScope()) )
                                    || (StrategySwitchEnum.UNUSE.getValue().equals(upgradePlanResp.getStrategySwitch()) && UpgradeScopeEnum.BATCH.getValue().equals(upgradePlanResp.getUpgradeScope())))
                                    &&  (null != filterDevIdList &&  !filterDevIdList.contains(device.getDeviceId())) ){
                                /**使用策略升级  策略还没完成 设备id不在策略明细范围内 不升级*/
                                OtaVersionResp otaVersionResp = new OtaVersionResp(oldVersion, oldVersion, device.getDeviceId(), 0, 0, 0, "");
                                otaDevList.add(otaVersionResp);
                            }else {
                                if (null != upgradeInfoResp && StringUtil.isNotBlank(upgradeInfoResp.getNextVersion())) {
                                    newVersion = upgradeInfoResp.getNextVersion();
                                }
                                Integer stage = 0;
                                Integer percent = 0;
                                String key = RedisKeyUtil.getUpgradeDeviceStatusKey(device.getDeviceId());
                                String stageStr = RedisCacheUtil.hashGetString(key, ModuleConstants.OTA_UPGRADE_STATUS_STAGE);
                                String msgStr = RedisCacheUtil.hashGetString(key, ModuleConstants.OTA_UPGRADE_STATUS_MSG);
                                String percentStr = RedisCacheUtil.hashGetString(key, ModuleConstants.OTA_UPGRADE_STATUS_PERCENT);
                                if (StringUtil.isNotBlank(stageStr)) {
                                    stage = Integer.parseInt(stageStr);
                                }
                                if (StringUtil.isNotBlank(percentStr)) {
                                    percent = Integer.parseInt(percentStr);
                                }
                                OtaVersionResp otaVersionResp = new OtaVersionResp(oldVersion, newVersion, device.getDeviceId(), stage, percent, 0, msgStr);
                                otaDevList.add(otaVersionResp);
                            }
                        }
                    }
            );
        }
    }
    /**
     * 描述：8.5获取版本列表请求   获取设备信息
     * @author nongchongwei
     * @date 2018/10/23 10:46
     * @param
     * @return
     */
    private List<ListDeviceByParamsRespVo> getDeviceList(FetchUserResp user, OtaVersionReq otaVersionReq) {
        List<ListDeviceByParamsRespVo> deviceParamList = new ArrayList<>();
        List<String> childDevList = otaVersionReq.getDevId();
        Long userId = user.getId();
        Long tenantId = user.getTenantId();
        Long proId =  Long.parseLong(otaVersionReq.getProductId());
        Set devIdSet = new HashSet();
        devIdSet.addAll(childDevList);
        /**校验 过滤 设备id 是否与userId绑定  是否是proId下的设备  begin**/
        List<ListUserDeviceInfoRespVo> userDeviceInfoList = deviceCoreService.listUserDevicesByDeviceIds(tenantId, userId, Lists.newArrayList(devIdSet));
        if (CollectionUtils.isEmpty(userDeviceInfoList)) {
            logger.debug("getVerListReq（获取不到用户设备信息.{},{},{}）", tenantId, userId, JSON.toJSONString(devIdSet));
            throw new BusinessException(OtaExceptionEnum.GET_USER_DEVICE_ERROR);
        }
        List<String> targetDeviceIds = Lists.newArrayList();
        userDeviceInfoList.forEach(userDevice -> {
            targetDeviceIds.add(userDevice.getDeviceId());
        });
        deviceParamList = deviceCoreApi.listDeviceByParams(ListDeviceByParamsReq.builder()
                .deviceIds(targetDeviceIds)
                .productId(proId).deviceType(otaVersionReq.getDevType()).build());
        if (CollectionUtils.isEmpty(deviceParamList)) {
            logger.debug("getVerListReq（获取不到过滤设备信息.{},{},{}）", tenantId, userId, JSON.toJSONString(targetDeviceIds));
            throw new BusinessException(OtaExceptionEnum.GET_DEVICE_ERROR);
        }
        /**校验 过滤 设备id 是否与userId绑定  是否是proId下的设备  end**/
        return deviceParamList;
    }
    /**
     * 描述：8.5获取版本列表请求   参数校验
     * @author nongchongwei
     * @date 2018/10/23 10:46
     * @param
     * @return
     */
    private FetchUserResp checkParam(OtaVersionReq otaVersionReq, String reqTopic) {
        List<String> childDevList = otaVersionReq.getDevId();
        FetchUserResp user = null;
        //1.对请求的数据进行处理
        if (CollectionUtils.isEmpty(childDevList)) {
            LOGGER.info("####ota#### getVerListReq childDevList->{}",childDevList);
            throw new BusinessException(UserExceptionEnum.PARAM_IS_ERROR);
        }
        if(StringUtil.isBlank(otaVersionReq.getProductId())){
            LOGGER.info("####ota#### ProductId is null, ProductId->{}",otaVersionReq.getProductId());
            throw new BusinessException(UserExceptionEnum.PARAM_IS_ERROR);
        }
        String userUuId = MQTTUtils.parseReqTopic(reqTopic);
        user = userApi.getUserByUuid(userUuId);
        LOGGER.info("####ota#### getVerListReq userUuId->{},user->{}",userUuId,user);
        if (user == null) {
            LOGGER.info("####ota#### getVerListReq user is not exist, user == null userUuId->{},user->{}",userUuId,user);
            throw new BusinessException(UserExceptionEnum.USER_IS_NOT_EXIST);
        }
        return user;
    }
    /**
     * 描述：获取当前策略组策略明细、指定uuid升级明细、指定批次升级明细 返回currentGroupVal
     * @author nongchongwei
     * @date 2018/10/23 10:46
     * @param
     * @return
     */
    public Integer filterByStrategy(UpgradePlanResp upgradePlanResp,Boolean justStrategy,List<String> retDevList) {
        List<String> filterDevId = new ArrayList<>();
        Integer currentGroupVal = -1;
        if (null == retDevList) return currentGroupVal;
         /**策略开关与升级范围**/
        Integer strategySwitch = upgradePlanResp.getStrategySwitch();
        Integer upgradeScope = upgradePlanResp.getUpgradeScope();
        /**获取策略明细**/
        if(null != strategySwitch && null != upgradeScope ){
            if(StrategySwitchEnum.USE.getValue().equals(strategySwitch) && UpgradeScopeEnum.FULL.getValue().equals(upgradeScope)){
                String currentGroupKey = RedisKeyUtil.getUpgradeStrategyCurrentGroupKey(upgradePlanResp.getId());
                StrategyInfoVo strategyInfoVo = RedisCacheUtil.valueObjGet(currentGroupKey, StrategyInfoVo.class);
                currentGroupVal = strategyInfoVo.getCurrentGroup();
                if(null != currentGroupVal && !ModuleConstants.OTA_UPGRADE_STRATEGY_COMPLETE_GROUP.equals(currentGroupVal)){
                    filterDevId = otaServiceApi.getStrategyDetailWithGroup(upgradePlanResp.getId(),currentGroupVal);
                }
            }else if(StrategySwitchEnum.UNUSE.getValue().equals(strategySwitch) && UpgradeScopeEnum.UUID.getValue().equals(upgradeScope) && !justStrategy){
                Set<String> filterDevIdSet = otaServiceApi.getStrategyDetailUuid(upgradePlanResp.getId());
                filterDevId = new ArrayList<>(filterDevIdSet);
            }else if(StrategySwitchEnum.UNUSE.getValue().equals(strategySwitch) && UpgradeScopeEnum.BATCH.getValue().equals(upgradeScope)  && !justStrategy){
                Set<String> filterDevIdSet = otaServiceApi.getStrategyDetailWithBatchNum(upgradePlanResp.getId());
                filterDevId = new ArrayList<>(filterDevIdSet);
            }
        }
        retDevList.addAll(filterDevId);
        return currentGroupVal;
    }
    /**
     * 描述：8.6获取版本列表响应
     * @author nongchongwei
     * @date 2018/10/23 10:46
     * @param
     * @return
     */
    private void getVerListResp(MqttMsg msg, String reqTopic) {
        String userId = MQTTUtils.parseReqTopic(reqTopic);
        String rspTopic = "iot/v1/c/" + userId + "/ota/getVerListResp";
        msg.setService("ota");
        msg.setMethod("getVerListResp");
        msg.setTopic(rspTopic);
        LOGGER.info("getVerListResp({}, {})",JSON.toJSONString(msg), reqTopic);
        mqttSdkService.sendMessage(MQTTClientListener.getMqttClientId(), msg, QOS);
    }
    /**
     * 描述：8.4上报版本信息  直连设备上报--->云端  iot/v1/s/[devId]/ota/updateVerInfo
     * @author nongchongwei
     * @date 2018/10/23 10:46
     * @param
     * @return
     */
    public void updateVerInfo(MqttMsg msg, String reqTopic) {
        LOGGER.info("###otalog### updateVerInfo({}, {})", msg, reqTopic);
        /**网关设备Id*/
        String directDeviceId = MQTTUtils.parseReqTopic(reqTopic);
        /**key:deviceId value:version*/
        Map<String, String> deviceVersionMap = new HashMap<>();
        /**key:deviceId value:productId*/
        Map<String, Long> deviceProductMap = null;
        /**key:productId，version 用于唯一确定升级版本信息  value:子设备deviceIdList  使用该升级版本的设备集合 */
        Map<String, List<String>> productVersionDeviceIdMap = new HashMap<>();
        List<String> deviceIds = new ArrayList<>();
        if (msg.getPayload() != null) {
            BatchIUpgradeDeviceVersion batchIUpgradeDeviceVersion = new BatchIUpgradeDeviceVersion();
            List<UpgradeDeviceVersionReq> upgradeDeviceVersionReqList = new ArrayList<>();
            LOGGER.info("###otalog### updateVerInfo({}, {})", msg.getPayload().toString(), reqTopic);
            OtaUpdateVersionInfoListReq versionInfoListReq = JSON.parseObject(msg.getPayload().toString(), OtaUpdateVersionInfoListReq.class);
            List<OtaUpdateVersionInfoReq> payloadList = versionInfoListReq.getVerInfo();
            if (!CollectionUtils.isEmpty(payloadList)) { // 直连设备下子设备列表版本信息
                /**过滤 只升级租户id跟网关租户id一样的子设备*/
                payloadList = filterDifferentTenantIdSubDev(payloadList,directDeviceId);
                for (OtaUpdateVersionInfoReq payload : payloadList) {
                    UpgradeDeviceVersionReq upgradeDeviceVersionReq = new UpgradeDeviceVersionReq();
                    String deviceId = payload.getDevId();
                    String version = payload.getVersion();
                    deviceVersionMap.put(deviceId, version);
                    deviceIds.add(payload.getDevId());

                    upgradeDeviceVersionReq.setDeviceId(deviceId);
                    upgradeDeviceVersionReq.setFwType(payload.getFwType());
                    upgradeDeviceVersionReq.setVersion(version);
                    upgradeDeviceVersionReqList.add(upgradeDeviceVersionReq);
                }
                /**设备版本对应关系记录*/
                batchIUpgradeDeviceVersion.setUpgradeDeviceVersionReqList(upgradeDeviceVersionReqList);
                otaServiceApi.batchInsertUpgradeDeviceVersion(batchIUpgradeDeviceVersion);
                deviceProductMap = deviceApi.findUuidProductIdMap(deviceIds);
                /**处理升级日志 处理上报升级状态最后没有上报5 6的情况，记录升级是否成功*/
                handleUpgradeLog(deviceVersionMap);
                /**过滤deviceIds 每个设备id 所属产品的升级计划 是否在策略升级 如果是，是否在当前策略组内 不在则不升级*/
                deviceIds = filterDeviceIds(deviceProductMap);
                onLineFirmware(deviceVersionMap,deviceProductMap);
                /**直连设备上报的情况*/
                if (payloadList.size() == 1 && directDeviceId.equals(deviceIds.get(0))) {
                    LOGGER.info("###otalog### directDeviceId->{}",  directDeviceId);
                    UpgradeInfoResp upgradeInfoResp = otaServiceApi.getNextVersion(deviceProductMap.get(directDeviceId), deviceVersionMap.get(directDeviceId));
                    doNoticeDevice(upgradeInfoResp, deviceVersionMap.get(directDeviceId), directDeviceId, null, deviceProductMap.get(directDeviceId), OtaUpgradeCheckEnum.F.getType());
                } else {
                    /**二级设备*/
                    subDevUpgrade(deviceIds,deviceVersionMap,deviceProductMap,productVersionDeviceIdMap,directDeviceId);
                }
            }
        }
    }

    private void onLineFirmware(Map<String, String> deviceVersionMap, Map<String, Long> deviceProductMap) {
        if(null == deviceVersionMap || deviceVersionMap.isEmpty() || null == deviceProductMap || deviceProductMap.isEmpty()){
            return;
        }
        Map<Long, List<String>> productVersionMap = new HashMap<>();
        for (Map.Entry<String, Long> entry : deviceProductMap.entrySet()) {
            Long productId = entry.getValue();
            List<String> versionList = productVersionMap.get(productId);
            if(null == versionList){
                versionList = new ArrayList<>();
                productVersionMap.put(productId,versionList);
            }
            String version = deviceVersionMap.get(entry.getKey());
            if(!versionList.contains(version)){
                versionList.add(version);
            }
        }
        for (Map.Entry<Long, List<String>> entry : productVersionMap.entrySet()) {
            Long productId = entry.getKey();
            String notOnlineFirmwareKey = RedisKeyUtil.getOtaUpgradeNotOnlineFirmwareKey(productId);
            List<String> versionList = entry.getValue();
            if(null == versionList || versionList.isEmpty()) continue;
            FirmwareVersionUpdateVersionDto dto = new FirmwareVersionUpdateVersionDto();
            dto.setProductId(productId);
            dto.setVersionOnlineTime(new Date());
            Set verSet = new HashSet();
            for(String version : versionList){
                String ver =  RedisCacheUtil.hashGetString(notOnlineFirmwareKey,version);
                LOGGER.info("onLineFirmware ver->{}",ver);
                if(StringUtil.isNotEmpty(ver)){
                    verSet.add(version);
                    RedisCacheUtil.hashRemove(notOnlineFirmwareKey,version);
                }
            }
            if(verSet.size() > 0 && !verSet.isEmpty()){
                dto.setOtaVersion(verSet);
                otaServiceApi.updateVersionOnlineTimeByProductId(dto);
            }
        }
    }

    /***
     * 描述：子设备上报版本升级
     * @author nongchongwei
     * @date 2018/10/23 10:46
     * @param deviceIds
     * @param deviceVersionMap
     * @param deviceProductMap
     * @param productVersionDeviceIdMap
     * @param directDeviceId
     */
    private void subDevUpgrade(List<String> deviceIds, Map<String, String> deviceVersionMap, Map<String, Long> deviceProductMap, Map<String, List<String>> productVersionDeviceIdMap,String directDeviceId) {
        List<String> productIdVersionKeyList = new ArrayList<>();
        List<String> deviceIdListTemp = null;
        LOGGER.info("###otalog### deviceIds->{}",  deviceIds);
        for (String deviceId : deviceIds) {
            /**组装productIdVersionKeyList key参数 内容：productId+Version*/
            String version = deviceVersionMap.get(deviceId);
            Long productId = deviceProductMap.get(deviceId);
            LOGGER.info("###otalog### deviceId->{},productId->{}",  deviceId,productId);
            if (productId == null || StringUtil.isBlank(version)) {
                continue;
            }
            String productIdVersionKey = productId + ":" + version;
            /**组装productVersionDeviceIdMap  productId+Version->子设备idList*/
            deviceIdListTemp = productVersionDeviceIdMap.get(productIdVersionKey);
            if (null == deviceIdListTemp) {
                deviceIdListTemp = new ArrayList<>();
                productVersionDeviceIdMap.put(productIdVersionKey, deviceIdListTemp);
            }
            deviceIdListTemp.add(deviceId);
            /**组装productIdVersionKeyList  productId+Version 放入List，用于查询下一个升级版本信息*/
            if (!productIdVersionKeyList.contains(productIdVersionKey)) {
                productIdVersionKeyList.add(productIdVersionKey);
            }
        }
        /** product+Version作为key 下一个升级信息作为value */
        /**key:productId，version 用于唯一确定升级版本信息  value:UpgradeInfoResp升级版本信息*/
        Map<String, UpgradeInfoResp> productVersionUpgradeInfoRespMap = otaServiceApi.getNextVersionMap(productIdVersionKeyList);
        LOGGER.info("###otalog### productIdVersionKeyList->{}",  productIdVersionKeyList);
        /**根据需求，延迟30s下发*/
        executorService.schedule(new Runnable() {
            @Override
            public void run() {
                for (String productIdVersionKeyTemp : productIdVersionKeyList) {
                    /**product+Version->子设备idList 即同一个产品，使用同一个版本的设备集合*/
                    List<String> deviceIdList = productVersionDeviceIdMap.get(productIdVersionKeyTemp);
                    LOGGER.info("###otalog### deviceIdList->{}",  deviceIdList);
                    /**product+Version->下一个升级版本信息*/
                    UpgradeInfoResp upgradeInfoResp = productVersionUpgradeInfoRespMap.get(productIdVersionKeyTemp);
                    LOGGER.info("###otalog### upgradeInfoResp->{}",  upgradeInfoResp);
                    Long productId = Long.parseLong(productIdVersionKeyTemp.split(":")[0]);
                    String version = productIdVersionKeyTemp.split(":")[1];
                    /**升级*/
                    doNoticeDevice(upgradeInfoResp, version, directDeviceId, deviceIdList, productId, OtaUpgradeCheckEnum.F.getType());
                }
            }
        },30, TimeUnit.SECONDS);
    }
    /**
     * 功能描述:过滤deviceIds 每个设备id 所属产品的升级计划 是否在策略升级 如果是，是否在当前策略组内 不在则不升级
     * @param: [deviceIds, deviceProductMap]
     * @return: java.util.List<java.lang.String>
     * @auther: nongchongwei
     * @date: 2018/11/26 15:28
     */
    private List<String> filterDeviceIds(Map<String, Long> deviceProductMap) {
        List<String> retList = new ArrayList<>();
        Map<Long,List<String>> productIdMapDevIdList = new HashMap<>();

        List<String> devIdTemp = null;
        for (Map.Entry<String, Long> entry : deviceProductMap.entrySet()) {
            devIdTemp = productIdMapDevIdList.get(entry.getValue());
            if(null == devIdTemp){
                productIdMapDevIdList.put(entry.getValue(),new ArrayList<>());
                devIdTemp = productIdMapDevIdList.get(entry.getValue());
            }
            devIdTemp.add(entry.getKey());
        }
        List<Long> productIdList = new ArrayList<>(productIdMapDevIdList.keySet());
        LOGGER.info("productIdMapDevIdList->{}",JSON.toJSONString(productIdMapDevIdList));
        LOGGER.info("productIdList->{}",JSON.toJSONString(productIdList));
        Map<Long,UpgradePlanResp> upgradePlanMap = otaServiceApi.getUpgradePlanByBathProductId(productIdList);
        LOGGER.info("productIdList->{}",JSON.toJSONString(upgradePlanMap));
        for (Map.Entry<Long, List<String>> entry : productIdMapDevIdList.entrySet()) {
            UpgradePlanResp upgradePlanResp = upgradePlanMap.get(entry.getKey());
            if(PlanStatusEnum.Start.getValue().equals(upgradePlanResp.getPlanStatus())){
                Integer strategySwitch = upgradePlanResp.getStrategySwitch();
                Integer upgradeScope = upgradePlanResp.getUpgradeScope();
                /**获取策略明细  处理选择了uuid或批次升级但是不填uuid或批次号的情况**/
                if(null != strategySwitch && null != upgradeScope
                        && ((StrategySwitchEnum.USE.getValue().equals(strategySwitch) && UpgradeScopeEnum.FULL.getValue().equals(upgradeScope))
                             || (StrategySwitchEnum.UNUSE.getValue().equals(strategySwitch) && UpgradeScopeEnum.UUID.getValue().equals(upgradeScope))
                             || (StrategySwitchEnum.UNUSE.getValue().equals(strategySwitch) && UpgradeScopeEnum.BATCH.getValue().equals(upgradeScope))
                       ) ){
                    List<String> groupDevList =  new ArrayList<>();
                    Integer currentGroupVal  = this.filterByStrategy(upgradePlanResp,false,groupDevList);
                    if(ModuleConstants.OTA_UPGRADE_STRATEGY_COMPLETE_GROUP.equals(currentGroupVal) && !currentGroupVal.equals(-1)){
                        /**已经通过容灾策略**/
                        retList.addAll(entry.getValue());
                    }else {
                        List<String> tempList = listFilter(groupDevList,entry.getValue());
                        retList.addAll(tempList);
                    }
                }else {
                    retList.addAll(entry.getValue());
                }
            }
        }
        LOGGER.info("retList->{}",JSON.toJSONString(retList));
        return retList;
    }
    /**
     * 功能描述:过滤list
     * @param: [groupDevList, valueList]
     * @return: java.util.List<java.lang.String>
     * @auther: nongchongwei
     * @date: 2018/11/26 15:28
     */
    private List<String> listFilter(List<String> groupDevList, List<String> valueList) {
        List<String> retList = new ArrayList<>();
        if(null == valueList || valueList.isEmpty()){
            return retList;
        }
        if(null == groupDevList || groupDevList.isEmpty()){
            return retList;
        }
        for(String value : valueList){
            if(groupDevList.contains(value)){
                retList.add(value);
            }
        }
        return retList;
    }

    /**
     * 描述：只升级租户id跟网关的租户id一样的子设备
     * @author nongchongwei
     * @date 2018/11/1 16:13
     * @param
     * @return
     */
    private List<OtaUpdateVersionInfoReq> filterDifferentTenantIdSubDev(List<OtaUpdateVersionInfoReq> payloadList, String directDeviceId) {
        LOGGER.info("###otalog### payloadList size->{},directDeviceId->{}",  payloadList.size(),directDeviceId);
        List<OtaUpdateVersionInfoReq> payloadListRet = new ArrayList<>();
        if(null == payloadList || payloadList.size() == 0){
            LOGGER.info("###otalog### null == payloadList || payloadList.size() == 0");
            return payloadList;
        }
        List<String> subDevIdList = new ArrayList<>();
        for (OtaUpdateVersionInfoReq payload : payloadList) {
            LOGGER.info("###otalog### payload.getDevId()->{}",payload.getDevId());
            subDevIdList.add(payload.getDevId());
        }
        subDevIdList.add(directDeviceId);
        Map<String, Long> devidTenantIdMap = deviceApi.findUuidTenantIdMap(subDevIdList);

        if(null ==devidTenantIdMap || devidTenantIdMap.isEmpty()){
            LOGGER.info("null ==devidTenantIdMap || devidTenantIdMap.isEmpty() directDeviceId->{}",directDeviceId);
            return payloadList;
        }
        Long directTenantId = devidTenantIdMap.get(directDeviceId);
        if(null == directTenantId){
            LOGGER.info("null == diretTenantId directDeviceId->{}",directDeviceId);
            return payloadList;
        }
        for(OtaUpdateVersionInfoReq otaUpdateVersionInfoReq : payloadList){
            String uuid = otaUpdateVersionInfoReq.getDevId();
            Long tenantId =  devidTenantIdMap.get(uuid);
            LOGGER.info("###otalog### tenantId->{},diretTenantId->{}，diretTenantId.equals(tenantId)->{}",tenantId,directTenantId,directTenantId.equals(tenantId));
            if(directTenantId.equals(tenantId) ){
                payloadListRet.add(otaUpdateVersionInfoReq);
            }
        }
        return payloadListRet;
    }
    /**
     * 功能描述:处理升级日志和策略报告 处理上报升级状态最后没有上报5 6的情况，记录升级是否成功
     * @param: [deviceVersionMap]
     * @return: void
     * @auther: nongchongwei
     * @date: 2018/11/28 17:36
     */
    private void handleUpgradeLog(Map<String, String> deviceVersionMap) {
        LOGGER.info("handleUpgradeLog deviceVersionMap->{}",JSON.toJSONString(deviceVersionMap));
        if (null == deviceVersionMap || deviceVersionMap.isEmpty()) {
            return;
        }
        for (String deviceId : deviceVersionMap.keySet()) {
            String version = deviceVersionMap.get(deviceId);
            UpgradeLogAddReq upgradeLogAddReq = otaServiceApi.getCacheUpgradeLog(deviceId);
            LOGGER.info("handleUpgradeLog upgradeLogAddReq->{}",JSON.toJSONString(upgradeLogAddReq));
            if (null == upgradeLogAddReq) continue;
            String key = RedisKeyUtil.getUpgradeDeviceStatusKey(deviceId);
            String stage = RedisCacheUtil.hashGetString(key, ModuleConstants.OTA_UPGRADE_STATUS_STAGE);
            /**upgradeLogAddReq.getTargetVersion()是下一个版本，不是最终版本**/
            /**stage为空表示没有升级过，或设备已经上报成功或失败**/
            if(StringUtil.isNotBlank(stage)){
                if (version.equals(upgradeLogAddReq.getTargetVersion())) {
                    /**处理策略升级的情况**/
                    handleStrategyUpgradeRecord(deviceId,OtaStageEnum.SUCCESS.getValue(),"success");
                    /**处理升级日志*/
                    handleUpgradeLog(deviceId,OtaStageEnum.SUCCESS.getValue(),key,version,"success");
                } else {
                    /**处理策略升级的情况**/
                    handleStrategyUpgradeRecord(deviceId,OtaStageEnum.FAILED.getValue(),"failed");
                    /**处理升级日志*/
                    handleUpgradeLog(deviceId,OtaStageEnum.FAILED.getValue(),key,version,"failed");
                }
            }

        }
    }
    /**
     * 功能描述:下发升级日志
     * @param: [upgradeInfoResp, version, directDeviceId, deviceIdList, productId, upgradeType]
     * @return: void
     * @auther: nongchongwei
     * @date: 2018/11/28 17:35
     */
    private void doNoticeDevice(UpgradeInfoResp upgradeInfoResp, String version, String directDeviceId, List<String> deviceIdList, Long productId, String upgradeType) {
        if (!CommonUtil.isEmpty(upgradeInfoResp) && !StringUtil.isEmpty(upgradeInfoResp.getNextVersion())
                && !StringUtil.isEmpty(upgradeInfoResp.getUpgradeType()) && PlanStatusEnum.Start.getValue().equals(upgradeInfoResp.getPlanStatus())
                && !upgradeInfoResp.getNextVersion().equals(version)) {
            /**升级 执行OTA通知*/
            LOGGER.info("###otalog### upgradeInfoResp->{},version->{},directDeviceId->{},deviceIdList->{},productId->{},upgradeType->{}",
                    upgradeInfoResp,version,directDeviceId,deviceIdList,productId,upgradeType);
            if ((UpgradeTypeEnum.Force.getValue().equals(upgradeInfoResp.getUpgradeType()) && OtaUpgradeCheckEnum.F.getType().equals(upgradeType))
                    || OtaUpgradeCheckEnum.FP.getType().equals(upgradeType)) {
                FileDto fileDto = fileApi.getGetUrl(upgradeInfoResp.getOtaFileId());
                if (null == fileDto || StringUtil.isBlank(fileDto.getPresignedUrl())) {
                    LOGGER.error("****************doNoticeDevice ota url is null :{}", JSON.toJSONString(fileDto));
                    return;
                }
                LOGGER.info("###otalog### url->{}",fileDto.getPresignedUrl());
                excOtaNotif(upgradeInfoResp.getFwType(), directDeviceId, deviceIdList, upgradeInfoResp.getNextVersion(), fileDto.getPresignedUrl(),
                        ModuleConstants.OTA_UPGRADE_MODE_SILENT, upgradeInfoResp.getOtaMd5(), StringUtil.getRandomNumber(10), "cloud");
                if (null == deviceIdList) {
                    deviceIdList = new ArrayList<>();
                    deviceIdList.add(directDeviceId);
                }
                /***使用策略、并且当前策略组不是已经完成的时，缓存容灾报告*/
                GetProductInfoRespVo getProductInfoRespVo = deviceCoreService.getProductById(productId);
                String model = getProductInfoRespVo.getModel();
                String currentGroupKey = RedisKeyUtil.getUpgradeStrategyCurrentGroupKey(upgradeInfoResp.getId());
                StrategyInfoVo strategyInfoVo = RedisCacheUtil.valueObjGet(currentGroupKey, StrategyInfoVo.class);
                Integer currentGroupVal = ModuleConstants.OTA_UPGRADE_STRATEGY_COMPLETE_GROUP;
                if(null != strategyInfoVo && null != strategyInfoVo.getCurrentGroup()){
                    currentGroupVal = strategyInfoVo.getCurrentGroup();
                }
                /***缓存指令信息 很重要*/
                UpgradeLogAddBatchReq upgradeLogAddReq = new UpgradeLogAddBatchReq(upgradeInfoResp.getId(),
                        deviceIdList, upgradeInfoResp.getUpgradeType(), upgradeInfoResp.getNextVersion(),
                        version,model,currentGroupVal,productId);
                otaServiceApi.cacheBatchUpgradeLog(upgradeLogAddReq);
            }
        } else {
            LOGGER.info("deviceIdList: {},productId:{},version:{} no need to upgrade", deviceIdList, productId, version);
        }
    }
    /**
     * 功能描述:8.3更新版本通知   云--> APP
     * @param: [userId, deviceId, deviceName, version]
     * @return: void
     * @auther: nongchongwei
     * @date: 2018/11/28 17:34
     */
    protected void updateVerNotif(String userId, String deviceId, String deviceName, String version) {
        String rspTopic = "iot/v1/c/" + userId + "/ota/updateVerNotif";
        MqttMsg msg = new MqttMsg();
        msg.setService("ota");
        msg.setMethod("updateVerNotif");
        msg.setSeq(StringUtil.getRandomString(6));
        //云发出的srcAddr
        msg.setSrcAddr(MQTTUtils.DEFAULT_CLOUD_SOURCE_ADDR);

        OtaUpdateVerNotifPayloadResp payloadResp = new OtaUpdateVerNotifPayloadResp(deviceId, version, deviceName);
        msg.setPayload(payloadResp);
        MqttMsgAck ack = MqttMsgAck.successAck();
        msg.setAck(ack);
        msg.setTopic(rspTopic);
        LOGGER.info("updateVerNotif,响应给app({}, {})", msg, rspTopic);
        mqttSdkService.sendMessage(MQTTClientListener.getMqttClientId(), msg, QOS);
    }
    /**
     * 功能描述:8.2上报OTA状态 网关/APP-->云  iot/v1/cb/[devId]/ota/ updateOtaStautsNotif
     * @param: [msg, reqTopic]
     * @return: void
     * @auther: nongchongwei
     * @date: 2018/11/28 15:42
     */
    public void updateOtaStautsNotif(MqttMsg msg, String reqTopic) {
        LOGGER.info("updateOtaStautsNotif({}, {})", msg, reqTopic);
        OtaUpdateStatusReq statusReq = JSON.parseObject(msg.getPayload().toString(), OtaUpdateStatusReq.class);
        String devId = statusReq.getDevId();
        Integer stage = statusReq.getStage();
        String otaMsg = statusReq.getMsg();
        Integer percent = statusReq.getPercent();
        String version = statusReq.getVersion();
        String key = RedisKeyUtil.getUpgradeDeviceStatusKey(devId);
        /***缓存升级状态*/
        handleUpgradeDeviceStatusCache(key,stage,percent,otaMsg);
        /**处理策略升级的情况*/
        handleStrategyUpgradeRecord(devId,stage,otaMsg);
        /**处理升级日志**/
        handleUpgradeLog(devId,stage,key,version,otaMsg);
    }
    /***
     * 功能描述:缓存升级状态
     * @param: [key, stage, percent, otaMsg]
     * @return: void
     * @auther: nongchongwei
     * @date: 2018/11/28 15:41
     */
    private void handleUpgradeDeviceStatusCache(String key, Integer stage, Integer percent, String otaMsg) {
        if (OtaStageEnum.DOWNLOADING.getValue().equals(stage) || OtaStageEnum.DOWNLOADED.getValue().equals(stage) ||
                OtaStageEnum.UPGRADE.getValue().equals(stage) || OtaStageEnum.INSTALLING.getValue().equals(stage) ||
                OtaStageEnum.BUSY.getValue().equals(stage)) {
            if (null != stage) {
                RedisCacheUtil.hashPut(key, ModuleConstants.OTA_UPGRADE_STATUS_STAGE, stage, false);
            }
            if (StringUtil.isNotBlank(otaMsg)) {
                RedisCacheUtil.hashPut(key, ModuleConstants.OTA_UPGRADE_STATUS_MSG, otaMsg, false);
            }

            if (null != percent) {
                RedisCacheUtil.hashPut(key, ModuleConstants.OTA_UPGRADE_STATUS_PERCENT, percent, false);
            }
        }
    }

    /***
     * 功能描述:处理升级日志
     * @param: [devId, stage, key]
     * @return: void
     * @auther: nongchongwei
     * @date: 2018/11/28 15:39
     */
    public void handleUpgradeLog(String devId, Integer stage, String key,String version,String otaMsg) {
        if (OtaStageEnum.SUCCESS.getValue().equals(stage)) {
            /**固件上传后，成功升级一台设备就把固件上线*/
            checkNotOnLineFirmware(devId,version);
            LOGGER.info("updateOtaStautsNotif devId->{},stage->{},time->{}", devId, stage, System.currentTimeMillis());
            if(StringUtil.isEmpty(otaMsg)) otaMsg = "success";
            otaServiceApi.updateUpgradeLog(devId, UpgradeResultEnum.Success.getValue(),otaMsg);
            RedisCacheUtil.delete(key);
            /**这里可以更新一下版本信息 避免设备成功后没有上报版本或上报后收不到的情况**/
            List<UpgradeDeviceVersionReq> upgradeDeviceVersionReqList = new ArrayList<>();
            BatchIUpgradeDeviceVersion batchIUpgradeDeviceVersion = new BatchIUpgradeDeviceVersion();
            UpgradeDeviceVersionReq upgradeDeviceVersionReq = new UpgradeDeviceVersionReq();
            upgradeDeviceVersionReq.setDeviceId(devId);
            upgradeDeviceVersionReq.setFwType(0);
            upgradeDeviceVersionReq.setVersion(version);
            upgradeDeviceVersionReqList.add(upgradeDeviceVersionReq);
            /**设备版本对应关系记录*/
            batchIUpgradeDeviceVersion.setUpgradeDeviceVersionReqList(upgradeDeviceVersionReqList);
            otaServiceApi.batchInsertUpgradeDeviceVersion(batchIUpgradeDeviceVersion);
        } else if (OtaStageEnum.FAILED.getValue().equals(stage)) {
            LOGGER.info("updateOtaStautsNotif devId->{},stage->{},time->{}", devId, stage, System.currentTimeMillis());
            boolean realFailed = checkRealFailed(otaMsg);
            if(realFailed){
                if(StringUtil.isEmpty(otaMsg)) otaMsg = "failed";
                otaServiceApi.updateUpgradeLog(devId, UpgradeResultEnum.Failed.getValue(),otaMsg);
                RedisCacheUtil.delete(key);
            }else {
                LOGGER.info("updateOtaStautsNotif device sleep devId->{},stage->{},otaMsg->{}", devId, stage,otaMsg);
            }
        }else if( OtaStageEnum.IDLE.getValue().equals(stage)){
            RedisCacheUtil.delete(key);
        }else if(OtaStageEnum.DOWNLOADED.getValue().equals(stage)){
            /**发送消息到rabbitmq  死信  等待超时**/
            OtaDeadInfo otaDeadInfo = new OtaDeadInfo(devId,version);
            String otaDeadInfoStr = JSON.toJSONString(otaDeadInfo);
            mqttSdkService.basicPublish("ota-dead",otaDeadInfoStr);
        }
    }

    /***
     * 功能描述:判断是否是真的失败，目前只有子设备失眠是假失败 true为真失败 false是休眠
     * 休眠："msg":"failed|-33302|ota device sleep"
     * @param: otaMsg
     * @return: void
     * @auther: nongchongwei
     * @date: 2018/11/28 15:39
     */
    private boolean checkRealFailed(String otaMsg) {
        boolean realFailed = true;
        if (StringUtil.isNotBlank(otaMsg) && otaMsg.contains("|")){
            String[] msgArray = otaMsg.split("\\|");
            if(null != msgArray && msgArray.length == 3){
                try{
                    Integer code = Integer.parseInt( msgArray[1]);
                    String msg = msgArray[2];
                    if(OtaStatusEnum.SLEEP.getValue().equals(code) && OtaStatusEnum.SLEEP.getDesc().equals(msg)){
                        realFailed = false;
                    }
                }catch (Exception e){
                    logger.error("checkRealFailed error->",e);
                }
            }
        }
        return realFailed;
    }
    /**
     * 功能描述:查询未上线固件缓存  有上线  删缓存
     prodid:ver
     * @param: []
     * @return: void
     * @auther: nongchongwei
     * @date: 2018/11/30 11:11
     */
    private void checkNotOnLineFirmware(String devId,String version) {
        String devKey = RedisKeyUtil.OTA_UPGRADE_INFO_DETAIL + devId;
        UpgradeLogAddReq req = RedisCacheUtil.valueObjGet(devKey, UpgradeLogAddReq.class);
        if(null != req){
            Long productId = req.getProductId();
            String notOnlineFirmwareKey = RedisKeyUtil.getOtaUpgradeNotOnlineFirmwareKey(productId);
            String ver =  RedisCacheUtil.hashGetString(notOnlineFirmwareKey,version);
            LOGGER.info("checkNotOnLineFirmware ver->{}",ver);
            if(StringUtil.isNotEmpty(ver)){
                FirmwareVersionUpdateVersionDto dto = new FirmwareVersionUpdateVersionDto();
                dto.setProductId(productId);
                dto.setVersionOnlineTime(new Date());
                Set verSet = new HashSet();
                verSet.add(version);
                dto.setOtaVersion(verSet);
                otaServiceApi.updateVersionOnlineTimeByProductId(dto);
                RedisCacheUtil.hashRemove(notOnlineFirmwareKey,version);
            }
        }
    }

    /**
     * 功能描述:处理策略升级的情况
     * @param: [devId, stage]
     * @return: void
     * @auther: nongchongwei
     * @date: 2018/11/28 11:26
     */
    public void handleStrategyUpgradeRecord(String devId, Integer stage,String reason) {
        LOGGER.info("####ota####   handleStrategyUpgradeRecord devId->{},stage->{},reason->{}", devId, stage, reason);
        if(OtaStageEnum.SUCCESS.getValue().equals(stage) || OtaStageEnum.FAILED.getValue().equals(stage)) {
            if(OtaStageEnum.FAILED.getValue().equals(stage)){
                /**设备休眠判断*/
                boolean realFailed = checkRealFailed(reason);
                if(!realFailed){
                    LOGGER.info("handleStrategyUpgradeRecord device sleep devId->{},stage->{},otaMsg->{}", devId, stage,reason);
                    return ;
                }
            }
            String devKey = RedisKeyUtil.OTA_UPGRADE_INFO_DETAIL + devId;
            UpgradeLogAddReq req = RedisCacheUtil.valueObjGet(devKey, UpgradeLogAddReq.class);
            LOGGER.info("####ota####   handleStrategyUpgradeRecord req->{}", JSON.toJSONString(req));
            if(null != req){
                /**redis锁，同一个设备，几秒之内，如果上报多次成功失败或broker在qos=1同一消息发送多次，只处理一次 抛异常，不然执行后面的日志记录会清理掉UpgradeLogAddReq**/
                boolean flag=RedisCacheUtil.setNx(RedisKeyUtil.OTA_UPGRADE_LOCK+devId,"Y",5);
                if (!flag){
                    LOGGER.info("handleStrategyUpgradeRecord lock  devId->{},stage->{},otaMsg->{}", devId, stage,reason);
                    throw new BusinessException(OtaExceptionEnum.DUPLICATE_MESSAGES_ERROR);
                }
                Long planId = req.getPlanId();
                Long productId = req.getProductId();
                UpgradePlanResp upgradePlanResp = otaServiceApi.getUpgradePlanByProductId(productId);
                LOGGER.info("####ota####   handleStrategyUpgradeRecord req->{}", JSON.toJSONString(upgradePlanResp));
                List<String> devList = new ArrayList<>();
                Integer currentGroupVal = filterByStrategy(upgradePlanResp,true,devList);
                LOGGER.info("####ota####   handleStrategyUpgradeRecord devList->{},currentGroupVal->{}", JSON.toJSONString(devList),currentGroupVal);
                if(null != devList && devList.size() != 0  && devList.contains(devId)
                        && PlanStatusEnum.Start.getValue().equals(upgradePlanResp.getPlanStatus())
                        && !ModuleConstants.OTA_UPGRADE_STRATEGY_COMPLETE_GROUP.equals(currentGroupVal)
                        && -1 != currentGroupVal){
                    this.switchStrategyGroup(planId ,devId, stage,productId,upgradePlanResp.getTenantId() );
                }
                /**记录容灾报告**/
                this.updateStrategyReport(upgradePlanResp,devId,stage,reason);
            }else {
                LOGGER.info("####ota####  upgradeLogAddReq is null devId-{},stage->{},reason->{}",devId,stage,reason);
            }
        }
    }
    /**
     * 功能描述:记录容灾报告
     * @param: [upgradePlanResp, devId, stage]
     * @return: void
     * @auther: nongchongwei
     * @date: 2018/11/28 13:40
     */
    private void updateStrategyReport(UpgradePlanResp upgradePlanResp, String devId, Integer stage,String reason) {
        LOGGER.info("####ota####  updateStrategyReport upgradePlanResp->{}, devId->{},stage->{},reason->{}",JSON.toJSONString(upgradePlanResp), devId, stage, reason);
        Long planId = upgradePlanResp.getId();
        /**根据计划id查策略下的所有明细devId**/
        List<String> strategyAllDevIdList = this.otaServiceApi.getStrategyDetailWithPlanId(planId);

        LOGGER.info("####ota####  updateStrategyReport strategyAllDevIdList->{}",JSON.toJSONString(strategyAllDevIdList));
        if(strategyAllDevIdList.contains(devId)){
            if (OtaStageEnum.SUCCESS.getValue().equals(stage)) {
                LOGGER.info("updateOtaStautsNotif devId->{},stage->{},time->{}", devId, stage, System.currentTimeMillis());
                if(StringUtil.isEmpty(reason)) reason = "success";
                otaServiceApi.updateStrategyReport(devId,UpgradeResultEnum.Success.getValue(),reason);
            } else if (OtaStageEnum.FAILED.getValue().equals(stage)) {
                if(StringUtil.isEmpty(reason)) reason = "failed";
                LOGGER.info("updateOtaStautsNotif devId->{},stage->{},time->{}", devId, stage, System.currentTimeMillis());
                otaServiceApi.updateStrategyReport(devId,UpgradeResultEnum.Failed.getValue(),reason);
            }
        }
    }

    /***
     * 功能描述:根据升级情况 暂停计划和切换策略
     * @param: [planId, devId, stage, productId, tenantId]
     * @return: void
     * @auther: nongchongwei
     * @date: 2018/11/28 11:21
     */
    private void switchStrategyGroup(Long planId ,String devId, Integer stage,Long productId ,Long tenantId){
        logger.info("####ota#### switch strategy group planId->{},devId->{},stage->{},productId->{},tenantId->{}",planId,devId,stage,productId,tenantId);
        String currentGroupKey = RedisKeyUtil.getUpgradeStrategyCurrentGroupKey(planId);
        StrategyInfoVo strategyInfoVo = RedisCacheUtil.valueObjGet(currentGroupKey, StrategyInfoVo.class);
        logger.info("####ota####  currentGroup strategyInfoVo->{}",JSON.toJSONString(strategyInfoVo));
        if(null == strategyInfoVo){
            logger.info("####ota#### switchStrategyGroup  null == strategyInfoVo");
            return;
        }
        Integer currentGroupVal = strategyInfoVo.getCurrentGroup();
        Integer total = strategyInfoVo.getUpgradeTotal();
        Integer threshold = strategyInfoVo.getThreshold();
        String successKey = RedisKeyUtil.getUpgradeStrategyGroupSuccessKey(planId,currentGroupVal);
        String failKey = RedisKeyUtil.getUpgradeStrategyGroupFailKey(planId,currentGroupVal);
        Long failNum = 0l;
        Long successNum = 0l;
        if(OtaStageEnum.SUCCESS.getValue().equals(stage)){
            failNum = RedisCacheUtil.setDelAndGetSize(failKey,Arrays.asList(devId));
            successNum = RedisCacheUtil.setAddAndGetSize(successKey,Arrays.asList(devId));
        }else {
            successNum = RedisCacheUtil.setDelAndGetSize(successKey,Arrays.asList(devId));
            failNum = RedisCacheUtil.setAddAndGetSize( failKey,Arrays.asList(devId));
        }

        logger.info("####ota#### switchStrategyGroup successNum->{},failNum->{},planId->{},currentGroupVal->{}",successNum,failNum,planId,currentGroupVal);
        /***达到阀值，暂停计划 清除相关缓存  确保所有缓存 在计划重新编辑后 能获取到新的值*/
        if(null != threshold && null != failNum && (threshold.intValue() == failNum.intValue() || threshold.intValue() < failNum.intValue())){
            /**还要缓存这个已经触发阀值这个情况 以便计划页面线条变红*/
            logger.info("####ota#### switchStrategyGroup trigger threshold and pause plan threshold->{},failNum->{},planId->{}",threshold,failNum,planId);
            String strategyTriggerThresholdKey = RedisKeyUtil.getUpgradeStrategyTriggerThresholdKey(planId);
            RedisCacheUtil.valueSet(strategyTriggerThresholdKey,StrategyTriggerEnum.Trigger.getValue());
            pausePlan(productId,planId,tenantId);
            return;
        }
        /**策略执行完毕 成功才切换 切换到下一个策略*/
        int sum = failNum.intValue() + successNum.intValue();
        if(null != total && (total.intValue() == sum && failNum.intValue() < threshold.intValue() )){
            logger.info("####ota#### updateOtaStautsNotif handleStrategyUpgrade error total->{},failNum+successNum->{},planId->{}",total,(failNum+successNum),planId);
            String strategyGroupSequenceKey = RedisKeyUtil.getUpgradeStrategyGroupSequenceKey(planId);
            StrategyInfoVo nextStrategyInfoVo = RedisCacheUtil.hashGet(strategyGroupSequenceKey,""+currentGroupVal,StrategyInfoVo.class);
            logger.info("####ota#### updateOtaStautsNotif handleStrategyUpgrade nextStrategyInfoVo-> {}", JSON.toJSONString(nextStrategyInfoVo));
            String strategyCurrentGroupKey = RedisKeyUtil.getUpgradeStrategyCurrentGroupKey(planId);
            StrategyInfoVo strategyCurrentGroupInfoVo = RedisCacheUtil.valueObjGet(currentGroupKey, StrategyInfoVo.class);
            logger.info("####ota#### updateOtaStautsNotif handleStrategyUpgrade strategyCurrentGroupInfoVo-> {}", JSON.toJSONString(strategyCurrentGroupInfoVo));
            RedisCacheUtil.valueObjSet(strategyCurrentGroupKey,nextStrategyInfoVo);
            logger.info("####ota####  begin start next StrategyGroup  planId->{},devId->{},stage->{},productId->{},tenantId->{}",planId,devId,stage,productId,tenantId);
            /**调用OTAService 进行升级下一个策略组*/
            executor.submit(new Runnable() {
                public void run() {
                    try {
                        otaService.noticeDevice(productId);
                    } catch (Exception e) {
                        logger.error("", e);
                    }
                }
            });
        }else {
            logger.info("####ota#### no switch  total->{},failNum+successNum->{},planId->{},result->{}",total,(failNum+successNum),planId,(null != total && (total.equals(failNum+successNum) || total < (failNum+successNum))));
        }
    }

    /**
     * 功能描述:  暂停计划
     * @param: [productId, planId, tenantId]
     * @return: void
     * @auther: nongchongwei
     * @date: 2018/11/28 11:25
     */
    private void pausePlan(Long productId, Long planId, Long tenantId) {
        logger.info("####ota#### pausePlan productId->{},planId->{}",productId,planId);
        otaServiceApi.updatePlanStatus(planId,PlanStatusEnum.Pause.getValue());
        /**删除计划缓存*/
        String key = RedisKeyUtil.getUpgradePlanKey(productId);
        RedisCacheUtil.delete(key);
        /**升级计划明细路缓存删除*/
        String pathKey = RedisKeyUtil.getUpgradePlanPathKey(productId);
        RedisCacheUtil.delete(pathKey);
        /**删除任务缓存*/
        String taskKey = RedisKeyUtil.getUpgradePlanTaskKey(productId);
        RedisCacheUtil.delete(taskKey);
        /**清除每个策略组成功失败记录**/
        List<StrategyConfigResp> strategyConfigRespList = this.otaServiceApi.getStrategyConfig(planId,tenantId);
        if(null != strategyConfigRespList && !strategyConfigRespList.isEmpty()){
            for(StrategyConfigResp strategyConfigResp :  strategyConfigRespList){
                String failKey = RedisKeyUtil.getUpgradeStrategyGroupFailKey(planId,strategyConfigResp.getStrategyGroup());
                String successKey = RedisKeyUtil.getUpgradeStrategyGroupSuccessKey(planId,strategyConfigResp.getStrategyGroup());
                String strategyTaskKey = RedisKeyUtil.getUpgradeStrategyTaskKey(planId,strategyConfigResp.getStrategyGroup());
                RedisCacheUtil.delete(successKey);
                RedisCacheUtil.delete(failKey);
                RedisCacheUtil.delete(strategyTaskKey);
            }
        }
    }


}
