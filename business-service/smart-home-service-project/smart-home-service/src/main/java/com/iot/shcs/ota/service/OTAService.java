package com.iot.shcs.ota.service;

import com.alibaba.fastjson.JSON;
import com.iot.common.util.CommonUtil;
import com.iot.common.util.StringUtil;
import com.iot.control.device.api.UserDeviceCoreApi;
import com.iot.device.api.CentralControlDeviceApi;
import com.iot.device.api.DeviceApi;
import com.iot.device.api.OTAServiceApi;
import com.iot.device.api.ProductApi;
import com.iot.device.enums.ota.*;
import com.iot.device.util.RedisKeyUtil;
import com.iot.device.vo.req.CommDeviceFwTypeVersionReq;
import com.iot.device.vo.req.CommDeviceInfoReq;
import com.iot.device.vo.req.DeviceReq;
import com.iot.device.vo.req.ota.UpgradeDeviceVersionReq;
import com.iot.device.vo.req.ota.UpgradeLogAddBatchReq;
import com.iot.device.vo.req.ota.UpgradeLogAddReq;
import com.iot.device.vo.req.ota.UpgradePlanReq;
import com.iot.device.vo.rsp.OtaVersionInfoResp;
import com.iot.device.vo.rsp.ProductResp;
import com.iot.device.vo.rsp.UserDeviceInfoResp;
import com.iot.device.vo.rsp.device.GetProductInfoRespVo;
import com.iot.device.vo.rsp.ota.*;
import com.iot.file.api.FileApi;
import com.iot.file.dto.FileDto;
import com.iot.redis.RedisCacheUtil;
import com.iot.shcs.contants.ModuleConstants;
import com.iot.shcs.device.service.impl.DeviceCoreService;
import com.iot.shcs.ota.service.impl.OtaServiceImpl;
import com.iot.shcs.ota.vo.StrategyInfoVo;
import com.iot.user.api.UserApi;
import com.iot.user.vo.FetchUserResp;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @Descrpiton: ota 具体服务操作
 * @Date: 16:37 2018/5/2
 * @Modify by:
 */

@Slf4j
@Service
public class OTAService {
    public static final Logger LOGGER = LoggerFactory.getLogger(OTAService.class);
    
    private static ExecutorService executor = Executors.newCachedThreadPool();

    @Autowired
    private OTAServiceApi otaServiceApi;
    @Autowired
    @Qualifier("ota")
    private OTAMQTTService otamqttService;
    @Autowired
    private UserApi userApi;
    @Autowired
    private FileApi fileApi;
    @Autowired
    private ProductApi productApi;
    @Autowired
    private DeviceCoreService deviceCoreService;
    /**
     * 功能描述:下发升级指令
     * @param: [productId]
     * @return: void
     * @auther: nongchongwei
     * @date: 2018/11/28 14:24
     */
    public void noticeDevice(Long productId) {
        LOGGER.info("###otalog### noticeDevice begin productId:{}", productId);

        UpgradePlanResp upgradePlanResp = otaServiceApi.getUpgradePlanByProductId(productId);
        LOGGER.info("###otalog### noticeDevice upgradePlanResp:{}", JSON.toJSONString(upgradePlanResp));
        if (null == upgradePlanResp ) {
            LOGGER.info("###otalog### noticeDevice upgradePlanResp is null,no upgrade}");
            return;
        }
        if(PlanStatusEnum.Pause.getValue().equals(upgradePlanResp.getPlanStatus())){
            LOGGER.info("###otalog### noticeDevice upgradePlanResp is pause,no upgrade}");
            return;
        }

        List<String> versionList = otaServiceApi.getVersionByProductId(productId);
        LOGGER.info("###otalog### noticeDevice versionList:{}", JSON.toJSONString(versionList));

        if (CollectionUtils.isEmpty(versionList)) {
            LOGGER.info("###otalog### noticeDevice versionList is null,no upgrade}");
            return;
        }

        ProductResp productResp = productApi.getSomeProductPropertyById(productId);
        LOGGER.info("###otalog### noticeDevice productResp:{}", JSON.toJSONString(productResp));

        if (null == productResp ) {
            LOGGER.info("###otalog### noticeDevice productResp is null,no upgrade}");
            return;
        }

        String taskTypeVal = getTaskTypeVal(upgradePlanResp,productResp);
        LOGGER.info("###otalog### noticeDevice taskTypeVal:{}", taskTypeVal);
        if(StringUtil.isBlank(taskTypeVal)){
            return;
        }
        boolean strategyUpgrade = getStrategyUpgrade(upgradePlanResp);
        LOGGER.info("###otalog### noticeDevice strategyUpgrade:{}", strategyUpgrade);
        Map<String, UpgradeInfoResp> upgradeInfoRespMap = otaServiceApi.getUpgradeInfoRespMap(productId, versionList);
        LOGGER.info("###otalog### noticeDevice upgradeInfoRespMap:{}", JSON.toJSONString(upgradeInfoRespMap));
        Map<String, String> fileUrlMap = new HashMap<>();
        noticeDeviceUpgrade(productId, upgradeInfoRespMap, taskTypeVal, fileUrlMap, versionList, strategyUpgrade,upgradePlanResp.getId());
    }
    /***
     * 功能描述:判断是否策略升级
     * @param: [upgradePlanResp]
     * @return: boolean
     * @auther: nongchongwei
     * @date: 2018/11/28 14:16
     */
    private boolean getStrategyUpgrade(UpgradePlanResp upgradePlanResp) {
        boolean strategyUpgrade = false;
        Integer strategySwitch = upgradePlanResp.getStrategySwitch();
        Integer upgradeScope = upgradePlanResp.getUpgradeScope();
        if(null != strategySwitch && null != upgradeScope && StrategySwitchEnum.USE.getValue().equals(strategySwitch) && UpgradeScopeEnum.FULL.getValue().equals(upgradeScope)){
            /**直连设备强制升级 策略升级 任务分组**/
            strategyUpgrade = true;
        }
        return strategyUpgrade;
    }
    /***
     * 功能描述:判断升级类型
     * @param: [upgradePlanResp, productResp]
     * @return: java.lang.String
     * @auther: nongchongwei
     * @date: 2018/11/28 14:26
     */
    private String getTaskTypeVal(UpgradePlanResp upgradePlanResp,ProductResp productResp) {
        String taskTypeVal = null;
        if (UpgradeTypeEnum.Force.getValue().equals(upgradePlanResp.getUpgradeType())) {
            if (OtaDevTypeEnum.DIRECT.getValue().equals(productResp.getIsDirectDevice())) {
                taskTypeVal = TaskTypeEnum.DIRECTFORCE.getValue();
            } else if (OtaDevTypeEnum.SUB.getValue().equals(productResp.getIsDirectDevice())) {
                taskTypeVal = TaskTypeEnum.SUBFORCE.getValue();
            } else {
                return null;
            }
        } else if (UpgradeTypeEnum.Push.getValue().equals(upgradePlanResp.getUpgradeType())) {
            taskTypeVal = TaskTypeEnum.PUSH.getValue();
        } else {
            return null;
        }
        return taskTypeVal;
    }

    private void noticeDeviceUpgrade(Long productId, Map<String, UpgradeInfoResp> upgradeInfoRespMap,
                                     String taskTypeVal, Map<String, String> fileUrlMap, List<String> versionList,
                                     boolean strategyUpgrade,Long planId) {
        executor.submit(new Runnable() {
            public void run() {
                if (StringUtil.isBlank(taskTypeVal)) {
                    return;
                }
                if (TaskTypeEnum.DIRECTFORCE.getValue().equals(taskTypeVal)) {
                    directForceUpgrade(productId, upgradeInfoRespMap, fileUrlMap, versionList,strategyUpgrade,planId);
                } else if (TaskTypeEnum.SUBFORCE.getValue().equals(taskTypeVal)) {
                    subForceUpgrade(productId, upgradeInfoRespMap, fileUrlMap, versionList,strategyUpgrade,planId);
                } else if (TaskTypeEnum.PUSH.getValue().equals(taskTypeVal)) {
                    pushUpgrade(upgradeInfoRespMap, productId,strategyUpgrade,planId);
                } else {
                    return;
                }
            }
        });
    }

    /**
     * 描述：推送升级
     *
     * @param
     * @return
     * @author nongchongwei
     * @date 2018/8/7 15:24
     */
    private void pushUpgrade(Map<String, UpgradeInfoResp> upgradeInfoRespMap, Long productId,boolean strategyUpgrade,Long planId) {
        int indexKey = 0;
        while (true) {
            List<PushOta> pushOtaList = null;
            if(strategyUpgrade){
                String currentGroupKey = RedisKeyUtil.getUpgradeStrategyCurrentGroupKey(planId);
                StrategyInfoVo strategyInfoVo = RedisCacheUtil.valueObjGet(currentGroupKey, StrategyInfoVo.class);
                Integer currentGroupVal = strategyInfoVo.getCurrentGroup();
                if(!CommonUtil.isEmpty(currentGroupVal)){
                    if(ModuleConstants.OTA_UPGRADE_STRATEGY_COMPLETE_GROUP.equals(currentGroupVal)){
                        pushOtaList = otaServiceApi.getPushOtaList(productId, indexKey);
                    }else {
                        pushOtaList = otaServiceApi.getPushStrategyOtaList(planId,currentGroupVal,indexKey);
                    }
                }
            }else {
                pushOtaList = otaServiceApi.getPushOtaList(productId, indexKey);
            }

            indexKey += 100;
            if (CollectionUtils.isEmpty(pushOtaList)) {
                break;
            }
            List<Long> userIdList = new ArrayList<>();
            for (PushOta pushOta : pushOtaList) {
                userIdList.add(pushOta.getUserId());
            }
            Map<Long, String> userIdMap = userApi.getBathUuid(userIdList);
            for (PushOta pushOta : pushOtaList) {
                Long userId = pushOta.getUserId();
                List<PushOtaDevInfo> pushOtaDevInfoList = pushOta.getDevList();
                if (CollectionUtils.isEmpty(pushOtaDevInfoList)) {
                    continue;
                }
                for (PushOtaDevInfo pushOtaDevInfo : pushOtaDevInfoList) {
                    String deviceId = pushOtaDevInfo.getDevId();
                    String deviceName = pushOtaDevInfo.getDevName();
                    UpgradeInfoResp upgradeInfoResp = upgradeInfoRespMap.get(pushOtaDevInfo.getVer());
                    if (null == upgradeInfoResp) {
                        continue;
                    }
                    String version = upgradeInfoResp.getNextVersion();
                    try {
                        otamqttService.updateVerNotif(userIdMap.get(userId), deviceId, deviceName, version);
                    } catch (Exception e) {
                        log.error("pushUpgrade userId->" + userIdMap.get(userId) + " deviceId->" + deviceId, e);
                    }

                }
            }
            if (pushOtaList.size() < 100) {
                break;
            }
        }
    }

    /**
     * 描述：子设备升级
     * 每次对100个网关下的子设备进行升级
     *  同一个网关下的相同版本的子设备发一次升级命令
     * @param
     * @return
     * @author nongchongwei
     * @date 2018/8/7 15:24
     */
    private void subForceUpgrade(Long productId, Map<String, UpgradeInfoResp> upgradeInfoRespMap, Map<String, String> fileUrlMap, List<String> versionList,boolean strategyUpgrade,Long planId) {
        int indexKey = 0;
        while (true) {
            List<SubForceOta> subForceOtaList = null;

            if(strategyUpgrade){
                String currentGroupKey = RedisKeyUtil.getUpgradeStrategyCurrentGroupKey(planId);
                StrategyInfoVo strategyInfoVo = RedisCacheUtil.valueObjGet(currentGroupKey, StrategyInfoVo.class);
                Integer currentGroupVal = strategyInfoVo.getCurrentGroup();
                if(!CommonUtil.isEmpty(currentGroupVal)){
                    if(ModuleConstants.OTA_UPGRADE_STRATEGY_COMPLETE_GROUP.equals(currentGroupVal)){
                        subForceOtaList = otaServiceApi.getSubForceOtaList(productId, indexKey);
                    }else {
                        subForceOtaList = otaServiceApi.getSubForceStrategyOtaList( planId,currentGroupVal,indexKey);
                    }
                }
            }else {
                subForceOtaList = otaServiceApi.getSubForceOtaList(productId, indexKey);
            }

            indexKey += 100;
            if (CollectionUtils.isEmpty(subForceOtaList)) {
                break;
            }
            for (SubForceOta subForceOta : subForceOtaList) {
                List<ForceOtaDevInfo> forceOtaDevInfoList = subForceOta.getSubDevList();
                /**版本分组*/
                Map<String, List<String>> versionDeviceMap = new HashMap<>();
                if (!CollectionUtils.isEmpty(forceOtaDevInfoList)) {
                    for (ForceOtaDevInfo forceOtaDevInfo : forceOtaDevInfoList) {
                        List<String> deviceIdList = versionDeviceMap.get(forceOtaDevInfo.getVer());
                        if (null == deviceIdList) {
                            versionDeviceMap.put(forceOtaDevInfo.getVer(), new ArrayList<>());
                        }
                        versionDeviceMap.get(forceOtaDevInfo.getVer()).add(forceOtaDevInfo.getDevId());
                    }
                }
                for (String version : versionDeviceMap.keySet()) {
                    UpgradeInfoResp upgradeInfoResp = upgradeInfoRespMap.get(version);
                    try {
                        excOtaNotif(upgradeInfoResp, subForceOta.getPdevId(), versionDeviceMap.get(version), productId, version, fileUrlMap, versionList);
                    } catch (Exception e) {
                        e.printStackTrace();
                        log.error("pushUpgrade version->" + version + " pdeviceId->" + subForceOta.getPdevId(), e);
                    }
                }
            }
            if (subForceOtaList.size() < 100) {
                break;
            }
        }
    }

    /**
     * 描述：直连设备升级
     *
     * @param
     * @return
     * @author nongchongwei
     * @date 2018/8/7 15:24
     */
    private void directForceUpgrade(Long productId, Map<String, UpgradeInfoResp> upgradeInfoRespMap, Map<String, String> fileUrlMap, List<String> versionList,boolean strategyUpgrade,Long planId) {
        int indexKey = 0;
        while (true) {
            List<ForceOtaDevInfo> directForceOtaList = null;
            if(strategyUpgrade){
                String currentGroupKey = RedisKeyUtil.getUpgradeStrategyCurrentGroupKey(planId);
                StrategyInfoVo strategyInfoVo = RedisCacheUtil.valueObjGet(currentGroupKey, StrategyInfoVo.class);
                Integer currentGroupVal = strategyInfoVo.getCurrentGroup();
                if(!CommonUtil.isEmpty(currentGroupVal)){
                    if(ModuleConstants.OTA_UPGRADE_STRATEGY_COMPLETE_GROUP.equals(currentGroupVal)){
                        directForceOtaList = otaServiceApi.getDirForceOtaList(productId, indexKey);
                    }else {
                        directForceOtaList = otaServiceApi.getDirForceStrategyOtaList( planId,currentGroupVal,indexKey);
                    }
                }
            }else {
                directForceOtaList = otaServiceApi.getDirForceOtaList(productId, indexKey);
            }

            indexKey += 100;
            if (CollectionUtils.isEmpty(directForceOtaList)) {
                break;
            }
            for (ForceOtaDevInfo directForceOta : directForceOtaList) {
                UpgradeInfoResp upgradeInfoResp = upgradeInfoRespMap.get(directForceOta.getVer());
                try {
                    excOtaNotif(upgradeInfoResp, directForceOta.getDevId(), null, productId, directForceOta.getVer(), fileUrlMap, versionList);
                } catch (Exception e) {
                    log.error("pushUpgrade version->" + directForceOta.getVer() + " deviceId->" + directForceOta.getDevId(), e);
                }

            }
            if (directForceOtaList.size() < 100) {
                break;
            }
        }
    }


    private void excOtaNotif(UpgradeInfoResp upgradeInfoResp, String directDeviceId, List<String> subDeviceIdList, Long productId, String version
            , Map<String, String> fileUrlMap, List<String> versionList) {
        if (!CommonUtil.isEmpty(upgradeInfoResp) && !StringUtil.isEmpty(upgradeInfoResp.getNextVersion())
                && !StringUtil.isEmpty(upgradeInfoResp.getUpgradeType()) && PlanStatusEnum.Start.getValue().equals(upgradeInfoResp.getPlanStatus())
                && !upgradeInfoResp.getNextVersion().equals(version) && !CollectionUtils.isEmpty(versionList) && versionList.contains(version)) {
            String url = fileUrlMap.get(upgradeInfoResp.getOtaFileId());
            if (StringUtil.isBlank(url)) {
                FileDto fileDto = fileApi.getGetUrl(upgradeInfoResp.getOtaFileId());
                if (null == fileDto || StringUtil.isBlank(fileDto.getPresignedUrl())) {
                    log.error("****************checkAndUpgrade ota url is null :{}", fileDto);
                    return;
                }
                url = fileDto.getPresignedUrl();
                fileUrlMap.put(upgradeInfoResp.getOtaFileId(), url);
            }
            otamqttService.excOtaNotif(upgradeInfoResp.getFwType(), directDeviceId, subDeviceIdList, upgradeInfoResp.getNextVersion(), url,
                    ModuleConstants.OTA_UPGRADE_MODE_SILENT, upgradeInfoResp.getOtaMd5(), StringUtil.getRandomNumber(10), "cloud");
            if (null == subDeviceIdList) {
                subDeviceIdList = new ArrayList<>();
                subDeviceIdList.add(directDeviceId);
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
            UpgradeLogAddBatchReq upgradeLogAddReq = new UpgradeLogAddBatchReq(upgradeInfoResp.getId(),
                    subDeviceIdList, upgradeInfoResp.getUpgradeType(), upgradeInfoResp.getNextVersion(),
                    version,model,currentGroupVal,productId);
            otaServiceApi.cacheBatchUpgradeLog(upgradeLogAddReq);
        } else {
            log.info("directDeviceId: {},productId:{},version:{} no need to upgrade", directDeviceId, productId, version);
        }
    }


    /**
     * 描述：直连设备上报基本信息时升级ota
     *
     * @param
     * @return
     * @author nongchongwei
     * @date 2018/7/31 11:38
     */
    public void checkAndUpgrade(Map<String, Object> payload, String deviceId, Long productId) {
        /**上报的ota信息 目前只上报一个ota信息*/
        List<Map<String, Object>> otaList = (List<Map<String, Object>>) payload.get("ota");
        if (!CollectionUtils.isEmpty(otaList)) {
            Map<String, Object> ota = otaList.get(0);
            Integer fwType = (Integer) ota.get("fwType");
            String version = (String) ota.get("version");
            if (null == fwType || fwType < 0) {
                return;
            }
            /**处理升级日志*/
            handleUpgradeLog(deviceId, version);
            /**使用策略升级  策略还没完成 设备id不在策略明细范围内 不升级*/
            boolean result = filterByStrategy(productId,deviceId);
            if(result){
                return;
            }
            /**获取升级版本信息*/
            UpgradeInfoResp upgradeInfoResp = otaServiceApi.getNextVersion(productId, version);
            /**计划状态校验*/
            if (!CommonUtil.isEmpty(upgradeInfoResp) && !StringUtil.isEmpty(upgradeInfoResp.getNextVersion())
                    && !StringUtil.isEmpty(upgradeInfoResp.getUpgradeType()) && PlanStatusEnum.Start.getValue().equals(upgradeInfoResp.getPlanStatus())
                    && !upgradeInfoResp.getNextVersion().equals(version)) {
                /**强制升级 执行OTA通知*/
                if (UpgradeTypeEnum.Force.getValue().equals(upgradeInfoResp.getUpgradeType())) {
                    FileDto fileDto = fileApi.getGetUrl(upgradeInfoResp.getOtaFileId());
                    if (null == fileDto || StringUtil.isBlank(fileDto.getPresignedUrl())) {
                        LOGGER.error("****************checkAndUpgrade ota url is null :{}",JSON.toJSONString(fileDto));
                        return;
                    }
                    otamqttService.excOtaNotif(fwType, deviceId, null, upgradeInfoResp.getNextVersion(), fileDto.getPresignedUrl(),
                            ModuleConstants.OTA_UPGRADE_MODE_SILENT, upgradeInfoResp.getOtaMd5(), StringUtil.getRandomNumber(10), "cloud");

                    /***使用策略、并且当前策略组不是已经完成的时，缓存容灾报告*/
                    GetProductInfoRespVo getProductInfoRespVo = deviceCoreService.getProductById(productId);
                    String model = getProductInfoRespVo.getModel();
                    String currentGroupKey = RedisKeyUtil.getUpgradeStrategyCurrentGroupKey(upgradeInfoResp.getId());
                    StrategyInfoVo strategyInfoVo = RedisCacheUtil.valueObjGet(currentGroupKey, StrategyInfoVo.class);
                    Integer currentGroupVal = ModuleConstants.OTA_UPGRADE_STRATEGY_COMPLETE_GROUP;
                    if(null != strategyInfoVo && null != strategyInfoVo.getCurrentGroup()){
                        currentGroupVal = strategyInfoVo.getCurrentGroup();
                    }
                    UpgradeLogAddReq upgradeLogAddReq = new UpgradeLogAddReq(upgradeInfoResp.getId(),
                            deviceId, upgradeInfoResp.getUpgradeType(), upgradeInfoResp.getTargetVersion(), version,model,currentGroupVal,productId);
                    otaServiceApi.cacheUpgradeLog(upgradeLogAddReq);
                }
            } else {
                log.info("deviceId: {},productId:{},version:{} no need to upgrade", deviceId, productId, version);
            }
        } else {
            log.error("****************checkAndUpgrade otaList is null :{}", otaList);
        }
    }
    /**
     * 描述：返回true则不升级
     * @param
     * @return
     * @author nongchongwei
     * @date 2018/7/31 11:38
     */
    private boolean filterByStrategy(Long productId,String devId) {
        boolean result = false;
        UpgradePlanResp upgradePlanResp = otaServiceApi.getUpgradePlanByProductId(productId);
        if(null == upgradePlanResp || PlanStatusEnum.Pause.getValue().equals(upgradePlanResp.getPlanStatus())) return true;
        List<String> filterDevId = new ArrayList<>();
        Integer currentGroupVal = -1;
        if(null != upgradePlanResp && PlanStatusEnum.Start.getValue().equals(upgradePlanResp.getPlanStatus())) {
            currentGroupVal = otamqttService.filterByStrategy(upgradePlanResp, false, filterDevId);
        }
        if( ((StrategySwitchEnum.USE.getValue().equals(upgradePlanResp.getStrategySwitch()) && UpgradeScopeEnum.FULL.getValue().equals(upgradePlanResp.getUpgradeScope())
                && !ModuleConstants.OTA_UPGRADE_STRATEGY_COMPLETE_GROUP.equals(currentGroupVal) && !currentGroupVal.equals(-1))
                || (StrategySwitchEnum.UNUSE.getValue().equals(upgradePlanResp.getStrategySwitch()) && UpgradeScopeEnum.UUID.getValue().equals(upgradePlanResp.getUpgradeScope()) )
                || (StrategySwitchEnum.UNUSE.getValue().equals(upgradePlanResp.getStrategySwitch()) && UpgradeScopeEnum.BATCH.getValue().equals(upgradePlanResp.getUpgradeScope())))
                &&  (null != filterDevId &&  !filterDevId.contains(devId)) ){
            /**使用策略升级  策略还没完成 设备id不在策略明细范围内 不升级*/
            result = true;
        }
        return result;
    }

    /**
     * 描述：升级上报状态异常时  处理升级日志和容灾报告
     *
     * @param
     * @return
     * @author nongchongwei
     * @date 2018/7/31 11:38
     */
    private void handleUpgradeLog(String deviceId, String version) {
        LOGGER.info("handleUpgradeLog deviceId->{},version->{}",deviceId,version);
        if (StringUtil.isBlank(deviceId) || StringUtil.isBlank(version)) {
            return;
        }
        UpgradeLogAddReq upgradeLogAddReq = otaServiceApi.getCacheUpgradeLog(deviceId);
        LOGGER.info("handleUpgradeLog upgradeLogAddReq->{}",JSON.toJSONString(upgradeLogAddReq));
        if (null == upgradeLogAddReq) return;
        String key = RedisKeyUtil.getUpgradeDeviceStatusKey(deviceId);
        String stage = RedisCacheUtil.hashGetString(key, ModuleConstants.OTA_UPGRADE_STATUS_STAGE);
        /**upgradeLogAddReq.getTargetVersion()是下一个版本，不是最终版本**/
        if(StringUtil.isNotBlank(stage)){
            if (!version.equals(upgradeLogAddReq.getTargetVersion())) {
                /**处理策略升级的情况**/
                otamqttService.handleStrategyUpgradeRecord(deviceId,OtaStageEnum.FAILED.getValue(),"failed");
                /**处理升级日志*/
                otamqttService.handleUpgradeLog(deviceId,OtaStageEnum.FAILED.getValue(),key,version,"failed");
            } else {
                /**处理策略升级的情况**/
                otamqttService.handleStrategyUpgradeRecord(deviceId,OtaStageEnum.SUCCESS.getValue(),"success");
                /**处理升级日志*/
                otamqttService.handleUpgradeLog(deviceId,OtaStageEnum.SUCCESS.getValue(),key,version,"success");
            }
        }
    }

    /**
     * 描述：构建用于更新设备信息的ota
     *
     * @param
     * @return
     * @author nongchongwei
     * @date 2018/7/31 14:13
     */
    public List<Map<String, Object>> buildOtaForUpdateDeviceInfo(String deviceId, Map<String, Object> payload, List<UpgradeDeviceVersionReq> upgradeDeviceVersionReqList) {
        List<Map<String, Object>> returnOta = new ArrayList<>();
        List<Map<String, Object>> otaList = (List<Map<String, Object>>) payload.get("ota");
        if (CollectionUtils.isEmpty(otaList)) {
            return returnOta;
        }
        Map<String, Object> ota = otaList.get(0);
        Integer fwType = (Integer) ota.get("fwType");
        String versionNum = (String) ota.get("version");

        Map<String, Object> deviceVersionResp = new HashMap<>();
        deviceVersionResp.put("version", versionNum);
        deviceVersionResp.put("fwType", fwType);
        returnOta.add(deviceVersionResp);

        UpgradeDeviceVersionReq upgradeDeviceVersionReq = new UpgradeDeviceVersionReq();
        upgradeDeviceVersionReq.setVersion(versionNum);
        upgradeDeviceVersionReq.setFwType(fwType);
        upgradeDeviceVersionReq.setDeviceId(deviceId);
        upgradeDeviceVersionReqList.add(upgradeDeviceVersionReq);
        return returnOta;
    }

}
