package com.iot.shcs.ota.service.impl;

import com.alibaba.fastjson.JSON;
import com.iot.common.exception.BusinessException;
import com.iot.common.helper.Page;
import com.iot.control.device.vo.resp.ListUserDeviceInfoRespVo;
import com.iot.device.api.OTAServiceApi;
import com.iot.device.enums.DevelopStatusEnum;
import com.iot.device.enums.ota.*;
import com.iot.device.util.RedisKeyUtil;
import com.iot.device.vo.req.ota.*;
import com.iot.device.vo.rsp.device.GetProductInfoRespVo;
import com.iot.device.vo.rsp.ota.*;
import com.iot.file.api.FileApi;
import com.iot.redis.RedisCacheUtil;
import com.iot.shcs.contants.ModuleConstants;
import com.iot.shcs.device.service.impl.DeviceCoreService;
import com.iot.shcs.ota.exception.OtaExceptionEnum;
import com.iot.shcs.ota.service.IOtaService;
import com.iot.shcs.ota.vo.StrategyInfoVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 项目名称：IOT云平台
 * 模块名称：ota
 * 功能描述：ota
 * 创建人： nongchongwei
 * 创建时间：2018年07月24日 16:58
 * 修改人： nongchongwei
 * 修改时间：2018年07月24日 16:58
 */
@Service
@Transactional
public class OtaServiceImpl implements IOtaService {

    public static final Logger LOGGER = LoggerFactory.getLogger(OtaServiceImpl.class);

    @Autowired
    private OTAServiceApi otaServiceApi;
    @Autowired
    private DeviceCoreService deviceCoreService;
    @Autowired
    private FileApi fileApi;



    /**
     * 描述：启动或暂停计划
     * @author nongchongwei
     * @date 2018/7/24 20:00
     * @param  upgradePlanOperateReq
     * @return
     */
    @Override
    public void operatePlan(UpgradePlanOperateReq upgradePlanOperateReq) {
        LOGGER.info("####ota#### proId->{},operatePlan upgradePlanOperateReq {}", upgradePlanOperateReq.getProductId(),JSON.toJSONString(upgradePlanOperateReq));
        Long productId = upgradePlanOperateReq.getProductId();
        UpgradePlanResp upgradePlanResp =  otaServiceApi.getUpgradePlanByProductId(productId);
        /**更新计划状态*/
        int count = otaServiceApi.updatePlanStatus(upgradePlanOperateReq.getPlanId(),upgradePlanOperateReq.getPlanStatus());
        if(0 == count){
            LOGGER.info("####ota####-->updatePlanStatus failed count->{}",count);
            throw new BusinessException(OtaExceptionEnum.UPDATE_PLAN_STATUS_ERROR);
        }
        /**状态已修改，操作缓存**/
        handleCache(upgradePlanOperateReq,upgradePlanResp);
        /**删除容灾报告**/
        if(PlanStatusEnum.Start.getValue().equals(upgradePlanOperateReq.getPlanStatus())){
            otaServiceApi.deleteStrategyReportByPlanId(upgradePlanResp.getId());
        }
        /***分配任务*/
        distributeTask(upgradePlanOperateReq,upgradePlanResp);
        /***操作日志*/
        this.otaServiceApi.recordUpgradePlanLog(upgradePlanOperateReq);
    }

    /**
     * 描述：启动或暂停计划 异常重置成暂停状态
     * @author nongchongwei
     * @date 2018/7/24 20:00
     * @param  upgradePlanOperateReq
     * @return
     */
    @Override
    public void resetPlan(UpgradePlanOperateReq upgradePlanOperateReq) {
        LOGGER.info("####ota#### proId->{},operatePlan upgradePlanOperateReq {}", upgradePlanOperateReq.getProductId(),JSON.toJSONString(upgradePlanOperateReq));
        Long productId = upgradePlanOperateReq.getProductId();
        UpgradePlanResp upgradePlanResp =  otaServiceApi.getUpgradePlanByProductId(productId);
        /**更新计划状态为暂停*/
        otaServiceApi.updatePlanStatus(upgradePlanOperateReq.getPlanId(),PlanStatusEnum.Pause.getValue());
        /**状态已修改，操作缓存**/
        handleCache(upgradePlanOperateReq,upgradePlanResp);
    }

    /**
     * 描述：分配任务
     * @author nongchongwei
     * @date 2018/11/19 10:55
     * @param
     * @return
     */
    private void distributeTask(UpgradePlanOperateReq upgradePlanOperateReq,  UpgradePlanResp upgradePlanResp) {

        /**启动计划 */
        if(PlanStatusEnum.Start.getValue().equals(upgradePlanOperateReq.getPlanStatus())){
            Long productId = upgradePlanOperateReq.getProductId();
            /**策略开关**/
            Integer strategySwitch = upgradePlanResp.getStrategySwitch();
            /**升级范围**/
            Integer upgradeScope = upgradePlanResp.getUpgradeScope();
            /**计划ID**/
            Long planId = upgradePlanResp.getId();
            /**该计划下的所有版本*/
            List<String> versionList = otaServiceApi.getVersionByProductId(productId);
            LOGGER.info("####ota####  versionList->{}",versionList);
            if(null == versionList || versionList.size() == 0){
                LOGGER.info("####ota####-->versionList is null versionList->{}",versionList);
                return;
            }
            /**指定uuid和批次升级时，获取需要升级的设备id**/
            Set<String> devIdSet = null;
            if(UpgradeScopeEnum.UUID.getValue().equals(upgradeScope)){
                devIdSet = this.otaServiceApi.getStrategyDetailUuid(planId);
                LOGGER.info("####ota####-->devIdSet->{}",devIdSet);
                if(null == devIdSet || devIdSet.isEmpty()){
                    return;
                }
            }else if(UpgradeScopeEnum.BATCH.getValue().equals(upgradeScope)){
                devIdSet = this.otaServiceApi.getStrategyDetailWithBatchNum(planId);
                LOGGER.info("####ota####-->devIdSet->{}",devIdSet);
                if(null == devIdSet || devIdSet.isEmpty()){
                    return;
                }
            }

            /**确定当前产品是直连、非直连*/
            GetProductInfoRespVo getProductInfoRespVo = deviceCoreService.getProductById(productId);
            Integer isDirectDevice = getProductInfoRespVo.getIsDirectDevice();

            /**强制升级 分配任务 信息：设备、版本 、父设备id */
            if(UpgradeTypeEnum.Force.getValue().equals(upgradePlanResp.getUpgradeType())){
                if(OtaDevTypeEnum.DIRECT.getValue().equals(isDirectDevice) ){
                    cacheForceDirectUpgrade(upgradePlanOperateReq,versionList,strategySwitch,upgradeScope,planId,devIdSet);
                }else if(OtaDevTypeEnum.SUB.getValue().equals(isDirectDevice)){
                    cacheForceSubUpgrade(upgradePlanOperateReq, versionList,strategySwitch,upgradeScope,planId,devIdSet);
                }
            }/**推送升级 分配任务 信息：设备、版本 、用户id*/
            else if(UpgradeTypeEnum.Push.getValue().equals(upgradePlanResp.getUpgradeType())){
                cachePushUpgrade(upgradePlanOperateReq,versionList,strategySwitch,upgradeScope,planId,devIdSet);
            }
            /**启动计划设置非目标版本上线*/
            setFirmwareVersionOnline(versionList,upgradePlanOperateReq);
        }else if(PlanStatusEnum.Pause.getValue().equals(upgradePlanOperateReq.getPlanStatus())){
            String taskKey = RedisKeyUtil.getUpgradePlanTaskKey(upgradePlanOperateReq.getProductId());
            RedisCacheUtil.delete(taskKey);
        }
    }


    /**
     * 描述：更新版本上线时间
     * @author nongchongwei
     * @date 2018/9/11 16:02
     * @param
     * @return
     */
    private void setFirmwareVersionOnline(List<String> allVersionList, UpgradePlanOperateReq upgradePlanOperateReq) {
        if(null == allVersionList || allVersionList.size() < 1){
            return;
        }
        List<FirmwareVersionQueryResp> firmwareVersionList = this.otaServiceApi.getAllOTAVersionInfoListByProductId(upgradePlanOperateReq.getTenantId(),
                upgradePlanOperateReq.getProductId());
        List<String> firmwareVersion = new ArrayList<>();
        firmwareVersionList.forEach(firmware -> {
            if(null == firmware.getVersionOnlineTime()){
                firmwareVersion.add(firmware.getOtaVersion());
            }
        });
        List<String> result = allVersionList.stream().filter((String s) -> firmwareVersion.contains(s)).collect(Collectors.toList());
        if(null != result && result.size() > 0 ){
            FirmwareVersionUpdateVersionDto firmwareVersionUpdateVersionDto = new FirmwareVersionUpdateVersionDto();
            firmwareVersionUpdateVersionDto.setOtaVersion(new HashSet<>(result));
            firmwareVersionUpdateVersionDto.setProductId(upgradePlanOperateReq.getProductId());
            firmwareVersionUpdateVersionDto.setTenantId(upgradePlanOperateReq.getTenantId());
            firmwareVersionUpdateVersionDto.setVersionOnlineTime(new Date());
            this.otaServiceApi.updateVersionOnlineTime(firmwareVersionUpdateVersionDto);
        }
    }

    /**
     * 描述：推送升级
     * @author nongchongwei
     * @date 2018/8/7 11:04
     * @param
     * @return
     */
    private void cachePushUpgrade(UpgradePlanOperateReq upgradePlanOperateReq, List<String> versionList, Integer strategySwitch, Integer upgradeScope,Long planId,Set<String> devIdSet) {
        /**根据产品id和版本列表查已激活设备信息*/
        List<OtaDeviceInfo> deviceTempList = otaServiceApi.getOtaDeviceInfo(upgradePlanOperateReq.getProductId(),versionList);
        if(CollectionUtils.isEmpty(deviceTempList)){
            return;
        }
        /**查询deviceTempList中每台设备对应的用户id**/
        List<OtaDeviceInfo> deviceList = queryDeviceUserId(deviceTempList);
        deviceTempList.clear();

        if(StrategySwitchEnum.UNUSE.getValue().equals(strategySwitch) &&
                (UpgradeScopeEnum.UUID.getValue().equals(upgradeScope) || UpgradeScopeEnum.BATCH.getValue().equals(upgradeScope))){
            /**推送升级 指定uuid或批次升级，过滤uuid**/
            deviceList = filterPushOtaList(devIdSet,deviceList);
        }else if(StrategySwitchEnum.USE.getValue().equals(strategySwitch) && UpgradeScopeEnum.FULL.getValue().equals(upgradeScope)){
            /**推送升级 策略升级 任务分组**/
            /**打乱顺序，随机取样**/
            Collections.shuffle(deviceTempList);
            pushStrategyUpgradeGrouping(planId,upgradePlanOperateReq,deviceList,upgradeScope);
        }else if(StrategySwitchEnum.USE.getValue().equals(strategySwitch) && (UpgradeScopeEnum.UUID.getValue().equals(upgradeScope) || UpgradeScopeEnum.BATCH.getValue().equals(upgradeScope))){
            /**出现不应该出现的第三个值 抛异常*/
            LOGGER.info("####ota#### operatePlan config error,cacheForceSubUpgrade,strategySwitch->{},upgradeScope->",strategySwitch,upgradeScope);
            throw new BusinessException(OtaExceptionEnum.CONFIG_ERROR);
        }
        /**推送升级 普通升级 任务分组**/
        pushUpgradeGrouping(upgradePlanOperateReq,deviceList);
    }
    /**
     * 描述：推送升级 普通升级 任务分组
     * @author nongchongwei
     * @date 2018/11/19 9:49
     * @param
     * @return
     */
    private void pushUpgradeGrouping(UpgradePlanOperateReq upgradePlanOperateReq,List<OtaDeviceInfo> deviceList) {
        String taskKey = RedisKeyUtil.getUpgradePlanTaskKey(upgradePlanOperateReq.getProductId());
        if(!CollectionUtils.isEmpty(deviceList)){
            Map<Long, List<PushOtaDevInfo>> pushOtaMap = new HashMap<>();
            for(OtaDeviceInfo otaDeviceInfo : deviceList){
                if(!pushOtaMap.containsKey(otaDeviceInfo.getUserId())){
                    pushOtaMap.put(otaDeviceInfo.getUserId(), new ArrayList<>());
                }
                PushOtaDevInfo pushOtaDevInfo = new PushOtaDevInfo(otaDeviceInfo.getDevId(), otaDeviceInfo.getDevName(), otaDeviceInfo.getVer());
                pushOtaMap.get(otaDeviceInfo.getUserId()).add(pushOtaDevInfo);
            }
            Map<String,PushOta> pushOtaInfoMap = new HashMap<>();
            int indexKey = 0;
            for (Long pushOtaKey : pushOtaMap.keySet()) {
                pushOtaInfoMap.put(""+indexKey++, new PushOta(pushOtaKey, pushOtaMap.get(pushOtaKey)));
            }
            RedisCacheUtil.hashPutBatch(taskKey, pushOtaInfoMap, null,true);
        }
    }

    /**  
     * 描述：推送升级 策略升级 任务分组
     * @author nongchongwei  
     * @date 2018/11/16 18:02
     * @param   
     * @return   
     */ 
    private void pushStrategyUpgradeGrouping(Long planId, UpgradePlanOperateReq upgradePlanOperateReq, List<OtaDeviceInfo> deviceList, Integer upgradeScope) {
        /**策略开关开启，根据策略配置进行设备分组**/
        List<StrategyConfigResp> strategyConfigRespList = this.otaServiceApi.getStrategyConfig(planId,upgradePlanOperateReq.getTenantId());
        /**校验策略配置中 全部配置组升级总量之和 要是比该产品下的设备数多 抛异常 **/
        checkDevSum(strategyConfigRespList,deviceList);
        /**初始化策略升级进度和策略组顺序**/
        initStrategyProgress(planId,strategyConfigRespList);
        /**遍历策略 给每个策略组进行分配要升级的设备**/
        Map<Integer,List<StrategyDetailReq>> strategyDetailReqMap = new HashMap<>();
        List<StrategyDetailReq> strategyDetailReqList = null;
        /**开始组装每个策略组的升级任务缓存模型**/
        for(StrategyConfigResp strategyConfig : strategyConfigRespList){
            /**策略组升级总量**/
            Integer upgradeTotal = strategyConfig.getUpgradeTotal();
            if(null == upgradeTotal || 0 == upgradeTotal){
                continue;
            }
            int count = 0;
            Integer group = strategyConfig.getStrategyGroup();
            /**策略组任务key**/
            String strategyKey = RedisKeyUtil.getUpgradeStrategyTaskKey(planId,group);
            strategyDetailReqList = new ArrayList<>();
            Iterator<OtaDeviceInfo> it = deviceList.iterator();
            Map<Long, List<PushOtaDevInfo>> pushOtaMap = new HashMap<>();
            while(it.hasNext()){
                OtaDeviceInfo otaDeviceInfo = it.next();
                count++;

                if(!pushOtaMap.containsKey(otaDeviceInfo.getUserId())){
                    pushOtaMap.put(otaDeviceInfo.getUserId(), new ArrayList<>());
                }
                PushOtaDevInfo pushOtaDevInfo = new PushOtaDevInfo(otaDeviceInfo.getDevId(), otaDeviceInfo.getDevName(), otaDeviceInfo.getVer());
                pushOtaMap.get(otaDeviceInfo.getUserId()).add(pushOtaDevInfo);

                /**组装策略明细**/
                StrategyDetailReq strategyDetailReq = new StrategyDetailReq();
                strategyDetailReq.setDeviceUuid(otaDeviceInfo.getDevId());
                strategyDetailReq.setPlanId(planId);
                strategyDetailReq.setStrategyGroup(group);
                strategyDetailReqList.add(strategyDetailReq);
                /**放入策略组，从列表删除**/
                it.remove();
                if(count == upgradeTotal){
                    break;
                }
            }
            Map<String,PushOta> pushOtaInfoMap = new HashMap<>();
            int indexKey = 0;
            for (Long pushOtaKey : pushOtaMap.keySet()) {
                pushOtaInfoMap.put(""+indexKey++, new PushOta(pushOtaKey, pushOtaMap.get(pushOtaKey)));
            }
            /**记录每个组的任务**/
            RedisCacheUtil.hashPutBatch(strategyKey, pushOtaInfoMap, null,true);
            /**记录每个组的策略明细**/
            strategyDetailReqMap.put(group,strategyDetailReqList);
        }
        /**策略明细入库**/
        StrategyDetailDto strategyDetailDto = new StrategyDetailDto();
        strategyDetailDto.setStrategyDetailReqMap(strategyDetailReqMap);
        strategyDetailDto.setUpgradeScope(upgradeScope);
        this.otaServiceApi.saveStrategyDetail(strategyDetailDto);
    }
    /**
     * 描述：初始化策略升级进度和策略组顺序
     * @author nongchongwei
     * @date 2018/11/19 14:30
     * @param
     * @return
     */
    private void initStrategyProgress(Long planId,List<StrategyConfigResp> strategyConfigRespList) {
        StrategyConfigResp strategyConfigResp = strategyConfigRespList.get(0);
        StrategyInfoVo strategyInfoVo = new StrategyInfoVo(strategyConfigResp.getStrategyGroup(),strategyConfigResp.getUpgradeTotal(),strategyConfigResp.getThreshold());
        /**缓存当前进行升级的策略组**/
        String strategyFirstGroupKey = RedisKeyUtil.getUpgradeStrategyCurrentGroupKey(planId);
        RedisCacheUtil.valueObjSet(strategyFirstGroupKey,strategyInfoVo);
        /**策略组顺序**/
        Map<String,StrategyInfoVo> groupSequenceMap = new HashMap<>();
        for(int i = 0; i < strategyConfigRespList.size()-1 ; i++){
            StrategyConfigResp strategyConfig = strategyConfigRespList.get(i+1);
            groupSequenceMap.put(""+strategyConfigRespList.get(i).getStrategyGroup(),new StrategyInfoVo(strategyConfig.getStrategyGroup(),strategyConfig.getUpgradeTotal(),strategyConfigResp.getThreshold()));
        }
        groupSequenceMap.put(""+strategyConfigRespList.get(strategyConfigRespList.size()-1).getStrategyGroup(),
                new StrategyInfoVo(ModuleConstants.OTA_UPGRADE_STRATEGY_COMPLETE_GROUP,0,0) );

        String strategyGroupSequenceKey = RedisKeyUtil.getUpgradeStrategyGroupSequenceKey(planId);
        RedisCacheUtil.hashPutAll(strategyGroupSequenceKey,groupSequenceMap,true);

    }


    /**
     * 描述：推送升级 指定uuid或批次升级时 过滤
     * @author nongchongwei
     * @date 2018/11/16 17:18
     * @param
     * @return
     */
    private List<OtaDeviceInfo> filterPushOtaList(Set<String> devIdSet, List<OtaDeviceInfo> deviceList) {
        List<OtaDeviceInfo> deviceListRet = new ArrayList<>();
        deviceList.forEach(
             otaDeviceInfo->{
                if(devIdSet.contains(otaDeviceInfo.getDevId())){
                        deviceListRet.add(otaDeviceInfo);
                    }
                }
        );
        return deviceListRet;
    }

    /**
     * 描述： 查询deviceTempList中每台设备对应的用户id
     * @author nongchongwei
     * @date 2018/11/16 15:57
     * @param
     * @return
     */
    private List<OtaDeviceInfo> queryDeviceUserId(List<OtaDeviceInfo> deviceTempList) {
        List<OtaDeviceInfo> deviceList = new ArrayList<>();
        Map<String,Long> devIdAndUserIdMap = new HashMap<>();
        /**收集参数：设备ID和租户id**/
        OtaDeviceInfo otaDevice = deviceTempList.get(0);
        List<String> devIdList = new ArrayList<>();
        for(OtaDeviceInfo otaDeviceInfoTemp : deviceTempList){
            devIdList.add(otaDeviceInfoTemp.getDevId());
        }
        /**查询每台设备对应的用户id*/
        List<ListUserDeviceInfoRespVo> listUserDeviceInfoRespVoList =  deviceCoreService.listBatchUserDevices(otaDevice.getTenantId(),devIdList);
        for(ListUserDeviceInfoRespVo listUserDeviceInfoRespVo : listUserDeviceInfoRespVoList){
            devIdAndUserIdMap.put(listUserDeviceInfoRespVo.getDeviceId(),listUserDeviceInfoRespVo.getUserId());
        }
        /**过滤  可能deviceTempList中的设备并没有绑定用户  devIdAndUserid中包含的就是绑定了用户  同时设置用户id到OtaDeviceInfo
         * devIdList可能存在没有对应userid的 所以过滤一下
         * */
        for(OtaDeviceInfo otaDeviceInfoTemp : deviceTempList){
            Long userId = devIdAndUserIdMap.get(otaDeviceInfoTemp.getDevId());
            if(null != userId){
                otaDeviceInfoTemp.setUserId(userId);
                deviceList.add(otaDeviceInfoTemp);
            }
        }
        return deviceList;
    }

    /**
     * 描述：直连设备 强制升级
     * @author nongchongwei
     * @date 2018/8/7 11:05
     * @param
     * @return
     */
    private void cacheForceDirectUpgrade(UpgradePlanOperateReq upgradePlanOperateReq, List<String> versionList, Integer strategySwitch, Integer upgradeScope,Long planId,Set<String> devIdSet) {
        String taskKey = RedisKeyUtil.getUpgradePlanTaskKey(upgradePlanOperateReq.getProductId());
        /**查已经激活设备*/
        List<ForceOtaDevInfo> directForceOtaList = otaServiceApi.getDirectForceOta(upgradePlanOperateReq.getProductId(),versionList);
        LOGGER.info("####ota#### operatePlan cacheForceDirectUpgrade {}", JSON.toJSONString(directForceOtaList));
        if(CollectionUtils.isEmpty(directForceOtaList)){
            return;
        }
        if(StrategySwitchEnum.UNUSE.getValue().equals(strategySwitch) &&
                (UpgradeScopeEnum.UUID.getValue().equals(upgradeScope) || UpgradeScopeEnum.BATCH.getValue().equals(upgradeScope))){
            /**直连设备 强制升级 指定uuid升级或批次升级 过滤uuid**/
            directForceOtaList = this.filterDirectForceOtaList(devIdSet,directForceOtaList);
        }else if(StrategySwitchEnum.USE.getValue().equals(strategySwitch) && UpgradeScopeEnum.FULL.getValue().equals(upgradeScope)){
            /**直连设备强制升级 策略升级 任务分组**/
            /**打乱设备顺序，随机取样**/
            Collections.shuffle(directForceOtaList);
            forceDirectStrategyUpgradeGrouping(planId,upgradePlanOperateReq,directForceOtaList,upgradeScope);
        }else if(StrategySwitchEnum.USE.getValue().equals(strategySwitch) && (UpgradeScopeEnum.UUID.getValue().equals(upgradeScope) || UpgradeScopeEnum.BATCH.getValue().equals(upgradeScope))){
            /**出现不应该出现的第三个值 抛异常*/
            LOGGER.info("####ota#### operatePlan config error,cacheForceDirectUpgrade,strategySwitch->{},upgradeScope->",strategySwitch,upgradeScope);
            throw new BusinessException(OtaExceptionEnum.CONFIG_ERROR);
        }
        /**直连设备强制升级 普通升级 任务分组**/
        Map<String,ForceOtaDevInfo> directForceOtaMap = new HashMap<>();
        for (int i = 0; i < directForceOtaList.size(); i++) {
            directForceOtaMap.put("" + i, directForceOtaList.get(i));
        }
        RedisCacheUtil.hashPutBatch(taskKey, directForceOtaMap,null,true);
    }
    /**
     * 描述：直连设备强制升级 策略升级 任务分组
     * @author nongchongwei
     * @date 2018/11/16 16:46
     * @param
     * @return
     */
    private void forceDirectStrategyUpgradeGrouping(Long planId, UpgradePlanOperateReq upgradePlanOperateReq, List<ForceOtaDevInfo> directForceOtaList,Integer upgradeScope) {
        /**策略开关开启，根据策略配置进行设备分组**/
        List<StrategyConfigResp> strategyConfigRespList = this.otaServiceApi.getStrategyConfig(planId,upgradePlanOperateReq.getTenantId());
        /**校验策略配置中 全部配置组升级总量之和 要是比该产品下的设备数多 抛异常 **/
        checkDevSum(strategyConfigRespList,directForceOtaList);
        /**初始化策略升级进度和策略组顺序**/
        initStrategyProgress(planId,strategyConfigRespList);
        /**遍历策略配置 给每个策略组进行分配要升级的设备**/
        Map<Integer,List<StrategyDetailReq>> strategyDetailReqMap = new HashMap<>();
        List<StrategyDetailReq> strategyDetailReqList = null;
        for(StrategyConfigResp strategyConfig : strategyConfigRespList){
            /**策略组升级总量**/
            Integer upgradeTotal = strategyConfig.getUpgradeTotal();
            if(null == upgradeTotal || 0 == upgradeTotal){
                continue;
            }
            int count = 0;
            Integer group = strategyConfig.getStrategyGroup();
            /**任务组key**/
            String strategyKey = RedisKeyUtil.getUpgradeStrategyTaskKey(planId,group);
            Map<String,ForceOtaDevInfo> directForceOtaStrategyMap = new HashMap<>();
            strategyDetailReqList = new ArrayList<>();
            Iterator<ForceOtaDevInfo> it = directForceOtaList.iterator();
            while(it.hasNext()){
                ForceOtaDevInfo forceOtaDevInfoTemp = it.next();
                count++;

                directForceOtaStrategyMap.put("" + count, forceOtaDevInfoTemp);

                /***组装策略明细*/
                StrategyDetailReq strategyDetailReq = new StrategyDetailReq();
                strategyDetailReq.setDeviceUuid(forceOtaDevInfoTemp.getDevId());
                strategyDetailReq.setPlanId(planId);
                strategyDetailReq.setStrategyGroup(group);
                strategyDetailReqList.add(strategyDetailReq);

                it.remove();
                if(count == upgradeTotal){
                    break;
                }
            }
            /**记录每个组的任务**/
            RedisCacheUtil.hashPutBatch(strategyKey, directForceOtaStrategyMap, null,true);
            /**记录每个组的策略明细**/
            strategyDetailReqMap.put(group,strategyDetailReqList);
        }
        /**策略明细入库**/
        StrategyDetailDto strategyDetailDto = new StrategyDetailDto();
        strategyDetailDto.setStrategyDetailReqMap(strategyDetailReqMap);
        strategyDetailDto.setUpgradeScope(upgradeScope);
        this.otaServiceApi.delStrategyDetail(planId);
        this.otaServiceApi.saveStrategyDetail(strategyDetailDto);
    }

    /**
     * 描述：直连设备 强制升级 指定uuid升级或批次升级 过滤uuid
     * @author nongchongwei
     * @date 2018/11/15 20:17
     * @param
     * @return
     */
    private List<ForceOtaDevInfo> filterDirectForceOtaList(Set<String> devIdSet,List<ForceOtaDevInfo> directForceOtaList){
        List<ForceOtaDevInfo> directForceOtaListRet = new ArrayList<>();
        directForceOtaList.forEach(
                forceOtaDevInfo->{
                    if(devIdSet.contains(forceOtaDevInfo.getDevId())){
                        directForceOtaListRet.add(forceOtaDevInfo);
                    }
                }
        );
        return directForceOtaListRet;
    }
    /**
     * 描述：子设备 强制升级
     * @author nongchongwei
     * @date 2018/8/7 11:05
     * @param
     * @return
     */
    private void cacheForceSubUpgrade(UpgradePlanOperateReq upgradePlanOperateReq, List<String> versionList, Integer strategySwitch, Integer upgradeScope,Long planId,Set<String> devIdSet) {
        /**查设备*/
        List<SubOtaDeviceInfo> subOtaDeviceInfoList = otaServiceApi.getSubOtaDeviceInfo(upgradePlanOperateReq.getProductId(),versionList);
        if(CollectionUtils.isEmpty(subOtaDeviceInfoList)){
            return;
        }
        if(StrategySwitchEnum.UNUSE.getValue().equals(strategySwitch)
               && (UpgradeScopeEnum.UUID.getValue().equals(upgradeScope)
               || UpgradeScopeEnum.BATCH.getValue().equals(upgradeScope))){
            /**子设备升级 强制升级 指定uuid升级或批次升级 过滤uuid**/
            subOtaDeviceInfoList = this.filterSubForceOtaList(devIdSet,subOtaDeviceInfoList);
        }else if(StrategySwitchEnum.USE.getValue().equals(strategySwitch) && UpgradeScopeEnum.FULL.getValue().equals(upgradeScope)){
            /**子设备升级 策略升级 任务分组**/
            /**打乱顺序，随机取样**/
            Collections.shuffle(subOtaDeviceInfoList);
            forceSubStrategyUpgradeGrouping(planId,upgradePlanOperateReq,subOtaDeviceInfoList,upgradeScope);
        }else if(StrategySwitchEnum.USE.getValue().equals(strategySwitch) && (UpgradeScopeEnum.UUID.getValue().equals(upgradeScope) || UpgradeScopeEnum.BATCH.getValue().equals(upgradeScope))){
            /**出现不应该出现的第三个值 抛异常*/
            LOGGER.info("####ota#### operatePlan config error,cacheForceSubUpgrade,strategySwitch->{},upgradeScope->",strategySwitch,upgradeScope);
            throw new BusinessException(OtaExceptionEnum.CONFIG_ERROR);
        }
        /**子设备升级 普通升级 任务分组**/
        forceSubUpgradeGrouping(upgradePlanOperateReq,subOtaDeviceInfoList);
    }
    /**
     * 描述：设备升级 普通升级 任务分组
     * @author nongchongwei
     * @date 2018/11/19 9:39
     * @param
     * @return
     */
    private void forceSubUpgradeGrouping(UpgradePlanOperateReq upgradePlanOperateReq,List<SubOtaDeviceInfo> subOtaDeviceInfoList) {
        String taskKey = RedisKeyUtil.getUpgradePlanTaskKey(upgradePlanOperateReq.getProductId());
        Map<String, List<ForceOtaDevInfo>> subForceOtaMap = new HashMap<>();
        for(SubOtaDeviceInfo subOtaDeviceInfo : subOtaDeviceInfoList){
            if(!subForceOtaMap.containsKey(subOtaDeviceInfo.getPdevId())){
                subForceOtaMap.put(subOtaDeviceInfo.getPdevId(), new ArrayList<>());
            }
            ForceOtaDevInfo forceOtaDevInfo = new ForceOtaDevInfo(subOtaDeviceInfo.getDevId(), subOtaDeviceInfo.getVer());
            subForceOtaMap.get(subOtaDeviceInfo.getPdevId()).add(forceOtaDevInfo);
        }
        Map<String,SubForceOta> subForceMap = new HashMap<>();
        int indexKey = 0;
        for (String subForceOtaKey : subForceOtaMap.keySet()) {
            subForceMap.put(""+indexKey++, new SubForceOta(subForceOtaKey, subForceOtaMap.get(subForceOtaKey)));
        }
        RedisCacheUtil.hashPutBatch(taskKey, subForceMap, ModuleConstants.OTA_TASK_TIME,true);
    }

    /**
     * 描述：子设备升级 策略升级 任务分组
     * @author nongchongwei
     * @date 2018/11/19 9:36
     * @param
     * @return
     */
    private void forceSubStrategyUpgradeGrouping(Long planId, UpgradePlanOperateReq upgradePlanOperateReq, List<SubOtaDeviceInfo> subOtaDeviceInfoList, Integer upgradeScope) {
        /**策略开关开启，根据策略配置进行设备分组**/
        List<StrategyConfigResp> strategyConfigRespList = this.otaServiceApi.getStrategyConfig(planId,upgradePlanOperateReq.getTenantId());
        /**校验策略配置中 全部配置组升级总量之和 要是比该产品下的设备数多 抛异常 **/
        checkDevSum(strategyConfigRespList,subOtaDeviceInfoList);
        /**初始化策略升级进度和策略组顺序**/
        initStrategyProgress(planId,strategyConfigRespList);
        /**遍历策略 给每个策略组进行分配要升级的设备**/
        Map<Integer,List<StrategyDetailReq>> strategyDetailReqMap = new HashMap<>();
        List<StrategyDetailReq> strategyDetailReqList = null;

        /**开始组装每个策略组的升级任务缓存模型**/
        for(StrategyConfigResp strategyConfig : strategyConfigRespList){
            /**策略组升级总量**/
            Integer upgradeTotal = strategyConfig.getUpgradeTotal();
            if(null == upgradeTotal || 0 == upgradeTotal){
                continue;
            }
            int count = 0;
            Integer group = strategyConfig.getStrategyGroup();
            /**策略组任务key**/
            String strategyKey = RedisKeyUtil.getUpgradeStrategyTaskKey(planId,group);
            strategyDetailReqList = new ArrayList<>();
            Iterator<SubOtaDeviceInfo> it = subOtaDeviceInfoList.iterator();
            Map<String, List<ForceOtaDevInfo>> subForceOtaMap = new HashMap<>();
            while(it.hasNext()){
                count++;
                SubOtaDeviceInfo subOtaDeviceInfo = it.next();

                if(!subForceOtaMap.containsKey(subOtaDeviceInfo.getPdevId())){
                    subForceOtaMap.put(subOtaDeviceInfo.getPdevId(), new ArrayList<>());
                }
                ForceOtaDevInfo forceOtaDevInfo = new ForceOtaDevInfo(subOtaDeviceInfo.getDevId(), subOtaDeviceInfo.getVer());
                subForceOtaMap.get(subOtaDeviceInfo.getPdevId()).add(forceOtaDevInfo);

                /**策略明细**/
                StrategyDetailReq strategyDetailReq = new StrategyDetailReq();
                strategyDetailReq.setDeviceUuid(subOtaDeviceInfo.getDevId());
                strategyDetailReq.setPlanId(planId);
                strategyDetailReq.setStrategyGroup(group);
                strategyDetailReqList.add(strategyDetailReq);
                /**放入策略组，从列表删除**/
                it.remove();
                if(count == upgradeTotal){
                    break;
                }
            }
            Map<String,SubForceOta> subForceMap = new HashMap<>();
            int indexKey = 0;
            for (String subForceOtaKey : subForceOtaMap.keySet()) {
                subForceMap.put(""+indexKey++, new SubForceOta(subForceOtaKey, subForceOtaMap.get(subForceOtaKey)));
            }
            RedisCacheUtil.hashPutBatch(strategyKey, subForceMap, null,true);

            /**记录每个组的策略明细**/
            strategyDetailReqMap.put(group,strategyDetailReqList);
        }
        /**策略明细入库**/
        StrategyDetailDto strategyDetailDto = new StrategyDetailDto();
        strategyDetailDto.setStrategyDetailReqMap(strategyDetailReqMap);
        strategyDetailDto.setUpgradeScope(upgradeScope);
        this.otaServiceApi.saveStrategyDetail(strategyDetailDto);
    }

    /**
     * 描述：校验策略配置中 设备数量是不是比该产品下的设备数少
     * @author nongchongwei
     * @date 2018/11/19 9:09
     * @param
     * @return
     */
    private int checkDevSum(List<StrategyConfigResp> strategyConfigRespList, List deviceInfoList) {
        if(null == strategyConfigRespList || strategyConfigRespList.size()==0){
            LOGGER.info("####ota#### operatePlan config error,checkDevSum,strategyConfigRespList is null");
            throw new BusinessException(OtaExceptionEnum.CONFIG_ERROR);
        }
        int total = 0;
        for(StrategyConfigResp strategyConfig : strategyConfigRespList){
            total += strategyConfig.getUpgradeTotal();
        }
        if(total > deviceInfoList.size()){
            LOGGER.info("####ota#### operatePlan config error,checkDevSum,strategy config upgrade num too large，total->{},deviceInfoList.size()->{}",total,deviceInfoList.size());
            throw new BusinessException(OtaExceptionEnum.STRATEGY_TOTAL_TOO_LARGE);
        }
        if(total < 0 || total== 0){
            LOGGER.info("####ota#### operatePlan config error,checkDevSum,strategy config upgrade num is zero，total->{},deviceInfoList.size()->{}",total,deviceInfoList.size());
            throw new BusinessException(OtaExceptionEnum.CONFIG_ERROR);
        }
        return total;
    }

    /**
     * 描述： 子设备 强制升级 指定uuid升级或批次升级 过滤uuid
     * @author nongchongwei
     * @date 2018/11/19 9:01
     * @param
     * @return
     */
    private List<SubOtaDeviceInfo> filterSubForceOtaList(Set<String> devIdSet, List<SubOtaDeviceInfo> subOtaDeviceInfoList) {
        List<SubOtaDeviceInfo> subOtaDeviceInfoListRet = new ArrayList<>();
        subOtaDeviceInfoList.forEach(
                subOtaDevice->{
                    if(devIdSet.contains(subOtaDevice.getDevId())){
                        subOtaDeviceInfoListRet.add(subOtaDevice);
                    }
                }
        );
        return subOtaDeviceInfoListRet;
    }

    /**
     * 描述：清除缓存
     * @author nongchongwei
     * @date 2018/8/7 11:08
     * @param
     * @return
     */
    private void handleCache(UpgradePlanOperateReq upgradePlanOperateReq,UpgradePlanResp upgradePlanResp) {
        Long productId = upgradePlanOperateReq.getProductId();

        /**计划ID**/
        Long planId = upgradePlanResp.getId();

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
        List<StrategyConfigResp> strategyConfigRespList = this.otaServiceApi.getStrategyConfig(planId,upgradePlanOperateReq.getTenantId());
        if(null != strategyConfigRespList && !strategyConfigRespList.isEmpty()){
            for(StrategyConfigResp strategyConfigResp :  strategyConfigRespList){
                String successKey = RedisKeyUtil.getUpgradeStrategyGroupSuccessKey(planId,strategyConfigResp.getStrategyGroup());
                String failKey = RedisKeyUtil.getUpgradeStrategyGroupFailKey(planId,strategyConfigResp.getStrategyGroup());
                String strategyTaskKey = RedisKeyUtil.getUpgradeStrategyTaskKey(planId,strategyConfigResp.getStrategyGroup());
                RedisCacheUtil.delete(successKey);
                RedisCacheUtil.delete(failKey);
                RedisCacheUtil.delete(strategyTaskKey);
            }
        }

        if(PlanStatusEnum.Start.getValue().equals(upgradePlanResp.getPlanStatus())){
            String strategyTriggerThresholdKey = RedisKeyUtil.getUpgradeStrategyTriggerThresholdKey(planId);
            RedisCacheUtil.valueSet(strategyTriggerThresholdKey,StrategyTriggerEnum.UnTrigger.getValue());

            String strategyFirstGroupKey = RedisKeyUtil.getUpgradeStrategyCurrentGroupKey(planId);
            String strategyGroupSequenceKey = RedisKeyUtil.getUpgradeStrategyGroupSequenceKey(planId);
            RedisCacheUtil.delete(strategyFirstGroupKey);
            RedisCacheUtil.delete(strategyGroupSequenceKey);
        }
    }


    /**
     * 描述：查询升级日志
     * @author nongchongwei
     * @date 2018/10/23 10:43
     * @param
     * @return
     */
    @Override
    public Page<UpgradeLogResp> getUpgradeLog(UpgradeLogReq upgradeLogReq) {
        return otaServiceApi.getUpgradeLog(upgradeLogReq);
    }
    /**
     * 描述：计划操作记录分页查询
     * @author nongchongwei
     * @date 2018/10/23 10:44
     * @param
     * @return
     */
    @Override
    public Page<UpgradePlanLogResp> getUpgradePlanLog(UpgradePlanReq upgradePlanReq) {
        return this.otaServiceApi.getUpgradePlanLog(upgradePlanReq);
    }
    /**
     * 描述：查询OTA版本合法性（唯一性）
     * @author nongchongwei
     * @date 2018/10/23 11:13
     * @param
     * @return
     */
    @Override
    public Boolean checkVersionLegality(Long tenantId, Long prodId, String otaVersion) {
        return this.otaServiceApi.checkVersionLegality(tenantId,prodId,otaVersion);
    }

    /**
     * 描述：依据产品ID查询升级版本列表
     * @author nongchongwei
     * @date 2018/10/23 10:46
     * @param
     * @return
     */
    @Override
    public Page<FirmwareVersionResp> getFirmwareVersionListByProductId(FirmwareVersionSearchReqDto dto) {
        return this.otaServiceApi.getFirmwareVersionListByProductId(dto);
    }
    /**
     * 描述：新增OTA升级版本信息
     * @author nongchongwei
     * @date 2018/10/23 10:46
     * @param
     * @return
     */
    @Override
    public int createFirmwareVersion(FirmwareVersionReqDto dto) {
        return this.otaServiceApi.createFirmwareVersion(dto);
    }
    /**
     * 描述：新增OTA升级版本信息,一创建便上线，仅用于发布产品时使用
     * @author nongchongwei
     * @date 2018/10/23 10:46
     * @param
     * @return
     */
    @Override
    public void initFirmwareVersion(FirmwareVersionDto dto) {
        Long tenantId = dto.getTenantId();
        Long prodId = dto.getProductId();
        GetProductInfoRespVo getProductInfoRespVo = deviceCoreService.getProductById(prodId);
        if(null != getProductInfoRespVo && null != getProductInfoRespVo.getDevelopStatus() &&
                (DevelopStatusEnum.RELEASE.getValue() == getProductInfoRespVo.getDevelopStatus() || DevelopStatusEnum.OPERATED.getValue() == getProductInfoRespVo.getDevelopStatus())){
            throw new BusinessException(OtaExceptionEnum.VERSION_EXIST);
        }
        List<FirmwareVersionQueryResp>  firmwareVersionQueryRespList = this.otaServiceApi.getInitOTAVersionInfoListByProductId(tenantId,prodId);
        if(null != firmwareVersionQueryRespList && firmwareVersionQueryRespList.size() > 0){
            List<String> fileIdList = new ArrayList<>();
            for(FirmwareVersionQueryResp firmwareVersionQueryResp : firmwareVersionQueryRespList){
                fileIdList.add(firmwareVersionQueryResp.getOtaFileId());
            }
            fileApi.deleteObject(fileIdList);
        }
        this.otaServiceApi.initFirmwareVersion(dto);
    }
    /**
     * 描述：获取初始化版本
     * @author nongchongwei
     * @date 2018/10/23 10:47
     * @param
     * @return
     */
    @Override
    public FirmwareVersionQueryResp getInitOTAVersionInfoListByProductId(Long tenantId, Long prodId) {
        List<FirmwareVersionQueryResp>  firmwareVersionQueryRespList = this.otaServiceApi.getInitOTAVersionInfoListByProductId(tenantId,prodId);
        if(null == firmwareVersionQueryRespList || firmwareVersionQueryRespList.size() == 0){
            return new FirmwareVersionQueryResp();
        }
        return firmwareVersionQueryRespList.get(0);
    }

    /**
     * 描述：查询计划
     * @author nongchongwei
     * @date 2018/10/23 10:47
     * @param
     * @return
     */
    @Override
    public UpgradePlanResp getUpgradePlan(UpgradePlanReq upgradePlanReq) {
        return this.otaServiceApi.getUpgradePlan(upgradePlanReq);
    }

    /**
     * 描述：升级计划修改
     * @author nongchongwei
     * @date 2018/10/23 10:48
     * @param
     * @return
     */
    @Override
    public int editUpgradePlan(UpgradePlanEditReq upgradePlanEditReq) {
        return this.otaServiceApi.editUpgradePlan( upgradePlanEditReq);
    }
    /**
     * 描述：依据产品ID获取升级版本列表
     * @author nongchongwei
     * @date 2018/10/23 10:48
     * @param
     * @return
     */
    @Override
    public List<String> getOTAVersionListByProductId( Long tenantId, Long productId) {
        return this.otaServiceApi.getOTAVersionListByProductId(tenantId, productId);
    }
    /**
     * 描述：依据产品ID获取版分页查询
     * @author nongchongwei
     * @date 2018/10/23 10:49
     * @param
     * @return
     */
    @Override
    public Page<FirmwareVersionPageResp> getOTAVersionPageByProductId(FirmwareVersionSearchReqDto req) {
        return this.otaServiceApi.getOTAVersionPageByProductId(req);
    }
    /**
     * 描述：获取版本百分比
     * @author nongchongwei
     * @date 2018/10/23 10:50
     * @param
     * @return
     */
    @Override
    public Map<String, String> getVersionPercent(VersionPercentReq versionPercentReq) {
        return this.otaServiceApi.getVersionPercent(versionPercentReq);
    }
    /**
     * 描述：根据model获取升级计划
     * @author nongchongwei
     * @date 2018/10/23 10:50
     * @param
     * @return
     */
    @Override
    public UpgradePlanResp getUpgradePlanByProductModel(String model) {
        return this.otaServiceApi.getUpgradePlanByProductModel(model);
    }
    /**
     * 描述：根据产品model获取固件下载url
     * @author nongchongwei
     * @date 2018/10/23 10:50
     * @param
     * @return
     */
    @Override
    public Map<String, String> getFirmwareUrlByModel(String model, String version) {
        return this.otaServiceApi.getFirmwareUrlByModel(model, version);
    }
    /**
     * 描述：删除固件
     * @author nongchongwei
     * @date 2018/10/23 10:51
     * @param
     * @return
     */
    @Override
    public int deleteByFirmwareId( Long id) {
        return this.otaServiceApi.deleteByFirmwareId(id);
    }

}
