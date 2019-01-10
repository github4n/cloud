package com.iot.device.controller;

import com.alibaba.fastjson.JSON;
import com.iot.common.exception.BusinessException;
import com.iot.common.helper.Page;
import com.iot.common.util.CommonUtil;
import com.iot.common.util.StringUtil;
import com.iot.device.api.OTAServiceApi;
import com.iot.device.core.service.ProductServiceCoreUtils;
import com.iot.device.enums.ota.PlanStatusEnum;
import com.iot.device.enums.ota.UpgradeScopeEnum;
import com.iot.device.exception.OtaExceptionEnum;
import com.iot.device.exception.ProductExceptionEnum;
import com.iot.device.model.Product;
import com.iot.device.model.ota.StrategyConfig;
import com.iot.device.model.ota.UpgradeDeviceVersion;
import com.iot.device.service.IOtaService;
import com.iot.device.util.RedisKeyUtil;
import com.iot.device.vo.req.OtaPageReq;
import com.iot.device.vo.req.ota.*;
import com.iot.device.vo.req.ota.FirmwareVersionDto;
import com.iot.device.vo.rsp.ota.*;
import com.iot.redis.RedisCacheUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

/**
 * @Author: xfz
 * @Descrpiton:
 * @Date: 9:21 2018/5/2
 * @Modify by:
 */

@RestController
public class OTAController implements OTAServiceApi {

    public static final Logger LOGGER = LoggerFactory.getLogger(OTAController.class);

    @Autowired
    private IOtaService otaService;

    /**
     * 描述：查询升级日志
     * @author nongchongwei
     * @date 2018/7/24 20:00
     * @param  upgradeLogReq
     * @return
     */
    @ResponseBody
    @Override
    public Page<UpgradeLogResp> getUpgradeLog(@RequestBody UpgradeLogReq upgradeLogReq) {
        this.checkProductId(upgradeLogReq.getProductId());
        try{
            return otaService.getUpgradeLog(upgradeLogReq);
        }catch (Exception e) {
            LOGGER.error("getUpgradeLog error->",e);
            throw new BusinessException(OtaExceptionEnum.GET_UPGRADE_LOG_ERROR, e);
        }

    }
    /**
     * 描述：计划操作记录分页查询
     * @author nongchongwei
     * @date 2018/7/24 20:00
     * @param  upgradePlanReq
     * @return
     */
    @ResponseBody
    @Override
    public Page<UpgradePlanLogResp> getUpgradePlanLog(@RequestBody UpgradePlanReq upgradePlanReq) {
        this.checkTenantId(upgradePlanReq.getTenantId());
        this.checkProductId(upgradePlanReq.getProductId());
        try{
            return otaService.getUpgradePlanLog(upgradePlanReq);
        }catch (Exception e) {
            LOGGER.error("getUpgradePlanLog error->",e);
            throw new BusinessException(OtaExceptionEnum.GET_UPGRADE_PLAN_LOG_ERROR, e);
        }
    }

    /**
     * 描述：查询OTA版本合法性（唯一性）
     * @author maochengyuan
     * @created 2018/7/25 11:17
     * @param tenantId 租户id
     * @param prodId 产品id
     * @param otaVersion ota版本
     * @return com.iot.device.vo.rsp.ota.CheckVersionResp
     */
    @ResponseBody
    @Override
    public Boolean checkVersionLegality(@RequestParam(value = "tenantId") Long tenantId, @RequestParam(value = "prodId") Long prodId, @RequestParam(value = "otaVersion") String otaVersion) {
        this.checkTenantId(tenantId);
        this.checkProductId(prodId);
        if(StringUtil.isEmpty(otaVersion)){
            throw new BusinessException(OtaExceptionEnum.PARAM_IS_ERROR, "ota version is empty");
        }
        return this.otaService.checkVersionLegality(tenantId, prodId, otaVersion);
    }

    /**
     * 描述：依据产品ID查询升级版本列表
     * @author maochengyuan
     * @created 2018/7/25 11:17
     * @param dto ota版本查询参数
     * @return com.iot.common.helper.Page<com.iot.device.vo.rsp.ota.FirmwareVersionResp>
     */
    @ResponseBody
    @Override
    public Page<FirmwareVersionResp> getFirmwareVersionListByProductId(@RequestBody FirmwareVersionSearchReqDto dto) {
        this.checkTenantId(dto.getTenantId());
        this.checkProductId(dto.getProductId());
        return this.otaService.getFirmwareVersionListByProductId(dto);
    }

    /**
     * 描述：新增OTA升级版本信息
     * @author maochengyuan
     * @created 2018/7/25 11:17
     * @param dto 版本对象
     * @return int
     */
    @ResponseBody
    @Override
    public int createFirmwareVersion(@RequestBody FirmwareVersionReqDto dto) {
        if(StringUtil.isEmpty(dto.getOtaType())){
            throw new BusinessException(OtaExceptionEnum.PARAM_IS_ERROR, "ota type is empty");
        }
        if(StringUtil.isEmpty(dto.getOtaFileId())){
            throw new BusinessException(OtaExceptionEnum.PARAM_IS_ERROR, "ota file id is empty");
        }
        if(StringUtil.isEmpty(dto.getOtaMd5())){
            throw new BusinessException(OtaExceptionEnum.PARAM_IS_ERROR, "ota md5 is empty");
        }
        if(CommonUtil.isEmpty(dto.getFwType())){
            throw new BusinessException(OtaExceptionEnum.PARAM_IS_ERROR, "fw type is empty");
        }
        /**判断此租户此款产品OTA版本唯一性*/
        Boolean isLegality = this.checkVersionLegality(dto.getTenantId(), dto.getProductId(), dto.getOtaVersion());
        if(!isLegality){
            throw new BusinessException(OtaExceptionEnum.VERSION_EXIST);
        }
        try {
            dto.setCreateTime(new Date());
            dto.setUpdateTime(new Date());
            dto.setOtaMd5(dto.getOtaMd5().trim());
            return this.otaService.createFirmwareVersion(dto);
        } catch (Exception e) {
            throw new BusinessException(OtaExceptionEnum.OTA_VERSION_CREATE_FAILED, e);
        }
    }

    /**
     * 描述：新增OTA升级版本信息,一创建便上线，仅用于发布产品时使用
     * @author nongchongwei
     * @date 2018/9/27 13:53
     * @param
     * @return
     */
    @ResponseBody
    @Override
    public void initFirmwareVersion(@RequestBody FirmwareVersionDto dto) {
        if(StringUtil.isEmpty(dto.getOtaType())){
            LOGGER.info("ota type is empty");
            throw new BusinessException(OtaExceptionEnum.PARAM_IS_ERROR, "ota type is empty");
        }
        if(StringUtil.isEmpty(dto.getOtaFileId())){
            LOGGER.info("ota file id is empty");
            throw new BusinessException(OtaExceptionEnum.PARAM_IS_ERROR, "ota file id is empty");
        }
        if(StringUtil.isEmpty(dto.getOtaMd5())){
            LOGGER.info("ota md5 is empty");
            throw new BusinessException(OtaExceptionEnum.PARAM_IS_ERROR, "ota md5 is empty");
        }
        if(CommonUtil.isEmpty(dto.getFwType())){
            LOGGER.info("fw type is empty");
            throw new BusinessException(OtaExceptionEnum.PARAM_IS_ERROR, "fw type is empty");
        }
        dto.setCreateTime(new Date());
        dto.setUpdateTime(new Date());
        dto.setVersionOnlineTime(new Date());
        dto.setOtaMd5(dto.getOtaMd5().trim());
        this.otaService.initFirmwareVersion(dto);

    }

    /**
     * 描述：查询计划
     * @author nongchongwei
     * @date 2018/7/24 20:00
     * @param  upgradePlanReq
     * @return
     */
    @ResponseBody
    @Override
    public UpgradePlanResp getUpgradePlan(@RequestBody UpgradePlanReq upgradePlanReq) {
        if(CommonUtil.isEmpty(upgradePlanReq.getProductId())){
            throw new BusinessException(OtaExceptionEnum.PARAM_IS_ERROR, "product id is empty");
        }
        if(CommonUtil.isEmpty(upgradePlanReq.getTenantId())){
            throw new BusinessException(OtaExceptionEnum.PARAM_IS_ERROR, "tenant id is empty");
        }
        try{
            return this.otaService.getUpgradePlan(upgradePlanReq);
        }catch (Exception e) {
            throw new BusinessException(OtaExceptionEnum.GET_UPGRADE_PLAN__ERROR, e);
        }

    }
    @ResponseBody
    @Override
    public UpgradePlanResp getUpgradePlanByProductId(@RequestParam(value = "productId") Long productId) {
        if(CommonUtil.isEmpty(productId)){
            throw new BusinessException(OtaExceptionEnum.PARAM_IS_ERROR, "product id is empty");
        }
        try{
            return this.otaService.getUpgradePlan(productId);
        }catch (Exception e) {
            throw new BusinessException(OtaExceptionEnum.GET_UPGRADE_PLAN__ERROR, e);
        }
    }
    @ResponseBody
    @Override
    public Map<Long, UpgradePlanResp> getUpgradePlanByBathProductId(@RequestParam(value = "productIdList") List<Long> productIdList) {
        if(CommonUtil.isEmpty(productIdList)){
            throw new BusinessException(OtaExceptionEnum.PARAM_IS_ERROR, "product id is empty");
        }
        try{
            return this.otaService.getUpgradePlan(productIdList);
        }catch (Exception e) {
            throw new BusinessException(OtaExceptionEnum.GET_UPGRADE_PLAN__ERROR, e);
        }
    }

    /**
     * 描述：更新计划状态
     * @author nongchongwei
     * @date 2018/10/22 15:53
     * @param
     * @return
     */
    @ResponseBody
    @Override
    public int updatePlanStatus(@RequestParam(value = "id") Long id, @RequestParam(value = "planStatus") String planStatus) {
        return this.otaService.updatePlanStatus(id,planStatus);
    }

    @Override
    public int updatePlanStartTime(Long id, Date startTime) {
        return 0;
    }

    /**
     * 描述：查询子设备信息
     * @author nongchongwei
     * @date 2018/10/22 15:54
     * @param
     * @return
     */
    @ResponseBody
    @Override
    public List<SubOtaDeviceInfo> getSubOtaDeviceInfo(@RequestParam(value = "productId") Long productId, @RequestParam(value = "versionList") List<String> versionList) {
        return this.otaService.getSubOtaDeviceInfo(productId,versionList);
    }

    /**
     * 描述：查询直连设备
     * @author nongchongwei
     * @date 2018/10/22 16:07
     * @param
     * @return
     */
    @ResponseBody
    @Override
    public List<ForceOtaDevInfo> getDirectForceOta(@RequestParam(value = "productId") Long productId, @RequestParam(value = "versionList") List<String> versionList) {
        return this.otaService.getDirectForceOta(productId,versionList);
    }

    /**
     * 描述：根据产品id和版本列表查询设备信息
     * @author nongchongwei
     * @date 2018/10/22 17:35
     * @param
     * @return
     */
    @ResponseBody
    @Override
    public List<OtaDeviceInfo> getOtaDeviceInfo(@RequestParam(value = "productId")Long productId, @RequestParam(value = "versionList") List<String> versionList) {
        return this.otaService.getOtaDeviceInfo(productId,versionList);
    }

    /**
     * 描述：更新上线时间
     * @author nongchongwei
     * @date 2018/10/22 17:48
     * @param
     * @return
     */
    @ResponseBody
    @Override
    public int updateVersionOnlineTime(@RequestBody FirmwareVersionUpdateVersionDto dto) {
        return this.otaService.updateVersionOnlineTime(dto);
    }

    /**
     * 描述：更新上线时间 产品审核通过时
     * @author nongchongwei
     * @date 2018/10/22 17:48
     * @param
     * @return
     */
    @ResponseBody
    @Override
    public int updateVersionOnlineTimeNoVersion(@RequestBody FirmwareVersionUpdateVersionDto dto) {
        return this.otaService.updateVersionOnlineTimeNoVersion(dto);
    }

    /**
     * 描述：更新上线时间
     * @author nongchongwei
     * @date 2018/10/22 17:48
     * @param
     * @return
     */
    @ResponseBody
    @Override
    public int updateVersionOnlineTimeByProductId(@RequestBody FirmwareVersionUpdateVersionDto dto) {
        return this.otaService.updateVersionOnlineTimeByProductId(dto);
    }

    /**
     * 描述：固件查询
     * @author nongchongwei
     * @date 2018/10/22 20:10
     * @param
     * @return
     */
    @ResponseBody
    @Override
    public List<FirmwareVersionQueryResp> getAllOTAVersionInfoListByProductId(@RequestParam(value = "tenantId")Long tenantId, @RequestParam(value = "productId") Long productId) {
        return this.otaService.getAllOTAVersionInfoListByProductId(tenantId,productId);
    }

    /**
     * 描述：初始固件查询
     * @author nongchongwei
     * @date 2018/10/22 20:10
     * @param
     * @return
     */
    @ResponseBody
    @Override
    public List<FirmwareVersionQueryResp> getInitOTAVersionInfoListByProductId(@RequestParam(value = "tenantId")Long tenantId, @RequestParam(value = "productId") Long productId) {
        return this.otaService.getInitOTAVersionInfoListByProductId(tenantId,productId);
    }

    @ResponseBody
    @Override
    public void recordUpgradePlanLog(@RequestBody UpgradePlanOperateReq upgradePlanOperateReq) {
        this.otaService.recordUpgradePlanLog(upgradePlanOperateReq);
    }

    /**
     * 描述：升级计划修改
     * @author nongchongwei
     * @date 2018/7/24 20:00
     * @param  upgradePlanEditReq
     * @return
     */
    @ResponseBody
    @Override
    public int editUpgradePlan(@RequestBody UpgradePlanEditReq upgradePlanEditReq) {
        if(CommonUtil.isEmpty(upgradePlanEditReq.getId())){
            throw new BusinessException(OtaExceptionEnum.PARAM_IS_ERROR, "plan id is empty");
        }
        if(CommonUtil.isEmpty(upgradePlanEditReq.getTenantId())){
            throw new BusinessException(OtaExceptionEnum.PARAM_IS_ERROR, "tenant id is empty");
        }
        if(CommonUtil.isEmpty(upgradePlanEditReq.getUserId())){
            throw new BusinessException(OtaExceptionEnum.PARAM_IS_ERROR, "user id is empty");
        }
        if(StringUtil.isEmpty(upgradePlanEditReq.getPlanStatus())){
            throw new BusinessException(OtaExceptionEnum.PARAM_IS_ERROR, "operate type is empty");
        }
        if(!PlanStatusEnum.Start.getValue().equals(upgradePlanEditReq.getPlanStatus()) &&
                !PlanStatusEnum.Pause.getValue().equals(upgradePlanEditReq.getPlanStatus())){
            throw new BusinessException(OtaExceptionEnum.PARAM_IS_ERROR, "operate type is empty");
        }

        checkVersionDuplication(upgradePlanEditReq);

        return  this.otaService.editUpgradePlan(upgradePlanEditReq);

    }
    /**
     * 描述：获取下一个版本
     * @author nongchongwei
     * @date 2018/7/24 20:00
     * @param  productId
     * @param  currentVersion
     * @return
     */
    @ResponseBody
    @Override
    public UpgradeInfoResp getNextVersion(@RequestParam(value = "productId") Long productId, @RequestParam(value = "currentVersion") String currentVersion) {
        if(StringUtil.isEmpty(currentVersion)){
            throw new BusinessException(OtaExceptionEnum.PARAM_IS_ERROR, "currentVersion is empty");
        }
        if(CommonUtil.isEmpty(productId)){
            throw new BusinessException(OtaExceptionEnum.PARAM_IS_ERROR, "product id is empty");
        }
        UpgradeInfoResp upgradeInfoResp = this.otaService.getNextUpgradeInfoResp(productId, currentVersion);

        return upgradeInfoResp;
    }

    @ResponseBody
    @Override
    public Map<String, UpgradeInfoResp> getNextVersionMap(@RequestParam(value = "productIdVersionList") List<String> productIdVersionList) {
        Map<String, UpgradeInfoResp> upgradeInfoRespMap = new HashMap<>();
        for(String productIdVersion : productIdVersionList){
            String[] param = productIdVersion.split(":");
            if(null == param || param.length<2){
                continue;
            }
            Long productId = Long.parseLong(param[0]);
            String currentVersion = param[1];
            UpgradeInfoResp upgradeInfoResp = this.otaService.getNextUpgradeInfoResp(productId,currentVersion);
            upgradeInfoRespMap.put(productIdVersion,upgradeInfoResp);
        }
        return upgradeInfoRespMap;
    }

    @ResponseBody
    @Override
    public Map<String, UpgradeInfoResp> getUpgradeInfoRespMap(@RequestParam(value = "productId") Long productId, @RequestParam(value = "versionList") List<String> versionList) {
        Map<String, UpgradeInfoResp> upgradeInfoRespMap = new HashMap<>();
        for(String version : versionList){
            UpgradeInfoResp upgradeInfoResp = this.otaService.getNextUpgradeInfoResp(productId,version);
            upgradeInfoRespMap.put(version,upgradeInfoResp);
        }
        return upgradeInfoRespMap;
    }
    /**
     * 描述： 批量插入设备版本信息
     * @author nongchongwei
     * @date 2018/11/20 13:55
     * @param
     * @return
     */
    @ResponseBody
    @Override
    public void batchInsertUpgradeDeviceVersion(@RequestBody BatchIUpgradeDeviceVersion batchIUpgradeDeviceVersion) {
        List<UpgradeDeviceVersionReq> upgradeDeviceVersionReqList = batchIUpgradeDeviceVersion.getUpgradeDeviceVersionReqList();
        List<UpgradeDeviceVersion> upgradeDeviceVersionList = new ArrayList<>();
        List<String> deviceIdList = new ArrayList<>();
        for(UpgradeDeviceVersionReq upgradeDeviceVersionReq : upgradeDeviceVersionReqList){
            UpgradeDeviceVersion upgradeDeviceVersion = new UpgradeDeviceVersion();
            upgradeDeviceVersion.setCreateTime(new Date());
            upgradeDeviceVersion.setUpdateTime(new Date());
            upgradeDeviceVersion.setDeviceId(upgradeDeviceVersionReq.getDeviceId());
            upgradeDeviceVersion.setFwType(upgradeDeviceVersionReq.getFwType());
            upgradeDeviceVersion.setVersion(upgradeDeviceVersionReq.getVersion());
            upgradeDeviceVersionList.add(upgradeDeviceVersion);
            deviceIdList.add(upgradeDeviceVersionReq.getDeviceId());
        }
        this.otaService.batchInsertUpgradeDeviceVersion(upgradeDeviceVersionList,deviceIdList);
    }

    /**
     * 描述：依据产品ID获取升级版本列表
     * @author maochengyuan
     * @created 2018/7/26 20:11
     * @param tenantId 租户id
     * @param productId 产品id
     * @return java.util.List<java.lang.String>
     */
    @ResponseBody
    @Override
    public List<String> getOTAVersionListByProductId(@RequestParam(value = "tenantId") Long tenantId, @RequestParam(value = "productId") Long productId){
        this.checkTenantId(tenantId);
        this.checkProductId(productId);
        return this.otaService.getOTAVersionListByProductId(tenantId, productId);
    }

    /**
     * 描述：依据产品ID获取版分页查询
     * @author maochengyuan
     * @created 2018/7/26 20:11
     * @param req 查询参数
     * @return com.github.pagehelper.PageInfo<com.iot.device.vo.rsp.ota.FirmwareVersionPageResp>
     */
    @ResponseBody
    @Override
    public Page<FirmwareVersionPageResp> getOTAVersionPageByProductId(@RequestBody FirmwareVersionSearchReqDto req){
        return this.otaService.getOTAVersionPageByProductId(req);
    }

    /**
     * 描述：获取一级设备强制升级设备列表
     * @author maochengyuan
     * @created 2018/8/7 16:34
     * @param productId 产品id
     * @param sta 开始下标
     * @return java.util.List<com.iot.device.vo.rsp.ota.ForceOtaDevInfo>
     */
    @ResponseBody
    @Override
    public List<ForceOtaDevInfo> getDirForceOtaList(@RequestParam(value = "productId") Long productId, @RequestParam(value = "sta") int sta){
        this.checkProductId(productId);
        return this.otaService.getOtaListFromCache(productId, sta, ForceOtaDevInfo.class);
    }

    /**
     * 描述：获取二级设备强制升级设备列表
     * @author maochengyuan
     * @created 2018/8/7 16:34
     * @param productId 产品id
     * @param sta 开始下标
     * @return java.util.List<com.iot.device.vo.rsp.ota.SubForceOta>
     */
    @ResponseBody
    @Override
    public List<SubForceOta> getSubForceOtaList(@RequestParam(value = "productId") Long productId, @RequestParam(value = "sta") int sta){
        this.checkProductId(productId);
        return this.otaService.getOtaListFromCache(productId, sta, SubForceOta.class);
    }

    /**
     * 描述：获取推送升级设备列表
     * @author maochengyuan
     * @created 2018/8/7 16:34
     * @param productId 产品id
     * @param sta 开始下标
     * @return java.util.List<com.iot.device.vo.rsp.ota.PushOta>
     */
    @ResponseBody
    @Override
    public List<PushOta> getPushOtaList(@RequestParam(value = "productId") Long productId, @RequestParam(value = "sta") int sta) {
        this.checkProductId(productId);
        return this.otaService.getOtaListFromCache(productId, sta, PushOta.class);
    }
    /**
     * 描述：获取直连设备策略升级设备列表
     * @author nongchongwei
     * @date 2018/11/19 15:17
     * @param
     * @return
     */
    @Override
    public List<ForceOtaDevInfo> getDirForceStrategyOtaList(@RequestParam(value = "planId") Long planId, @RequestParam(value = "group") Integer group, @RequestParam(value = "sta") int sta) {
        if(CommonUtil.isEmpty(planId)){
            throw new BusinessException(OtaExceptionEnum.PARAM_IS_ERROR, "planId is empty");
        }
        if(CommonUtil.isEmpty(group)){
            throw new BusinessException(OtaExceptionEnum.PARAM_IS_ERROR, "group is empty");
        }
        return this.otaService.getStrategyOtaListFromCache(planId,group, sta, ForceOtaDevInfo.class);
    }
    /**
     * 描述：获取二级策略升级设备列表
     * @author nongchongwei
     * @date 2018/11/19 15:17
     * @param
     * @return
     */
    @Override
    public List<SubForceOta> getSubForceStrategyOtaList(@RequestParam(value = "planId") Long planId, @RequestParam(value = "group") Integer group, @RequestParam(value = "sta") int sta) {
        if(CommonUtil.isEmpty(planId)){
            throw new BusinessException(OtaExceptionEnum.PARAM_IS_ERROR, "planId is empty");
        }
        if(CommonUtil.isEmpty(group)){
            throw new BusinessException(OtaExceptionEnum.PARAM_IS_ERROR, "group is empty");
        }
        return this.otaService.getStrategyOtaListFromCache(planId,group, sta, SubForceOta.class);
    }

    /**
     * 描述：获取策略升级设备列表
     * @author nongchongwei
     * @date 2018/11/19 15:17
     * @param
     * @return
     */
    @Override
    public List<PushOta> getPushStrategyOtaList(@RequestParam(value = "planId") Long planId, @RequestParam(value = "group") Integer group, @RequestParam(value = "sta") int sta) {
        if(CommonUtil.isEmpty(planId)){
            throw new BusinessException(OtaExceptionEnum.PARAM_IS_ERROR, "planId is empty");
        }
        if(CommonUtil.isEmpty(group)){
            throw new BusinessException(OtaExceptionEnum.PARAM_IS_ERROR, "group is empty");
        }
        return this.otaService.getStrategyOtaListFromCache(planId,group, sta, PushOta.class);
    }

    /**
     * 描述：缓存升级信息
     * @author maochengyuan
     * @created 2018/8/9 10:35
     * @param req 版本信息
     * @return void
     */
    @ResponseBody
    @Override
    public void cacheUpgradeLog(@RequestBody UpgradeLogAddReq req) {
        try {
            if(StringUtil.isEmpty(req.getDeviceUuid())){
                throw new BusinessException(OtaExceptionEnum.PARAM_IS_ERROR, "DeviceId is null");
            }
            if(StringUtil.isEmpty(req.getOriginalVersion())){
                throw new BusinessException(OtaExceptionEnum.PARAM_IS_ERROR, "OriginalVersion is null");
            }
            if(StringUtil.isEmpty(req.getTargetVersion())){
                throw new BusinessException(OtaExceptionEnum.PARAM_IS_ERROR, "TargetVersion is null");
            }
            if(StringUtil.isEmpty(req.getUpgradeType())){
                throw new BusinessException(OtaExceptionEnum.PARAM_IS_ERROR, "UpgradeType is null");
            }
            if(CommonUtil.isEmpty(req.getPlanId())){
                throw new BusinessException(OtaExceptionEnum.PARAM_IS_ERROR, "PlanId is null");
            }
            this.otaService.cacheUpgradeLog(req);
        } catch (Exception e) {
            LOGGER.error("update upgrade log failed: ", e);
        }
    }

    @ResponseBody
    @Override
    public UpgradeLogAddReq getCacheUpgradeLog(@RequestParam(value = "deviceId") String deviceId) {
        UpgradeLogAddReq req = null;
        try {
            if(StringUtil.isEmpty(deviceId)){
                throw new BusinessException(OtaExceptionEnum.PARAM_IS_ERROR, "DeviceId is null");
            }
            String key = RedisKeyUtil.OTA_UPGRADE_INFO_DETAIL+deviceId;
            req = RedisCacheUtil.valueObjGet(key, UpgradeLogAddReq.class);
        } catch (Exception e) {
            LOGGER.error("get upgrade log cache failed: ", e);
        }
        return req;
    }

    /**
     * 描述：批量缓存升级信息
     * @author nongchognwei
     * @created 2018/8/9 10:35
     * @param req 版本信息
     * @return void
     */
    @ResponseBody
    @Override
    public void cacheBatchUpgradeLog(@RequestBody UpgradeLogAddBatchReq req) {
        try {
            if (CollectionUtils.isEmpty(req.getDeviceUuidList())) {
                throw new BusinessException(OtaExceptionEnum.PARAM_IS_ERROR, "DeviceId is null");
            }
            if(StringUtil.isEmpty(req.getOriginalVersion())){
                throw new BusinessException(OtaExceptionEnum.PARAM_IS_ERROR, "OriginalVersion is null");
            }
            if(StringUtil.isEmpty(req.getTargetVersion())){
                throw new BusinessException(OtaExceptionEnum.PARAM_IS_ERROR, "TargetVersion is null");
            }
            if(StringUtil.isEmpty(req.getUpgradeType())){
                throw new BusinessException(OtaExceptionEnum.PARAM_IS_ERROR, "UpgradeType is null");
            }
            if(CommonUtil.isEmpty(req.getPlanId())){
                throw new BusinessException(OtaExceptionEnum.PARAM_IS_ERROR, "PlanId is null");
            }
            req.getDeviceUuidList().forEach(
                    devId->{
                        UpgradeLogAddReq upgradeLogAddReq = new UpgradeLogAddReq();
                        BeanUtils.copyProperties(req, upgradeLogAddReq);
                        upgradeLogAddReq.setDeviceUuid(devId);
                        this.otaService.cacheUpgradeLog(upgradeLogAddReq);
                    });
        } catch (Exception e) {
            LOGGER.error("update upgrade log failed: ", e);
        }
    }


    /**
     * 描述：更新升级结果
     * @author maochengyuan
     * @created 2018/8/9 10:36
     * @param deviceId 设备id
     * @param upgradeResult 升级结果
     * @return void
     */
    @ResponseBody
    @Override
    public void updateUpgradeLog(@RequestParam(value = "deviceId") String deviceId, @RequestParam(value = "upgradeResult") String upgradeResult,@RequestParam(value = "upgradeMsg")String upgradeMsg) {
        try {
            if(StringUtil.isEmpty(deviceId)){
                throw new BusinessException(OtaExceptionEnum.PARAM_IS_ERROR, "deviceId is null");
            }
            if(StringUtil.isEmpty(upgradeResult)){
                throw new BusinessException(OtaExceptionEnum.PARAM_IS_ERROR, "upgrade result is null");
            }
            if(StringUtil.isEmpty(upgradeMsg)){
                throw new BusinessException(OtaExceptionEnum.PARAM_IS_ERROR, "upgrade upgradeMsg is null");
            }
            this.otaService.updateUpgradeLog(deviceId, upgradeResult,upgradeMsg);
        } catch (Exception e) {
            LOGGER.error("update upgrade log failed: ", e);
        }
    }


    @ResponseBody
    @Override
    public void updateStrategyReport(@RequestParam(value = "deviceId") String deviceId, @RequestParam(value = "upgradeResult") String upgradeResult, @RequestParam(value = "reason") String reason) {
        try {
            if(StringUtil.isEmpty(upgradeResult)){
                throw new BusinessException(OtaExceptionEnum.PARAM_IS_ERROR, "upgrade result is null");
            }
            if(StringUtil.isEmpty(deviceId)){
                throw new BusinessException(OtaExceptionEnum.PARAM_IS_ERROR, "deviceId is null");
            }
            if(StringUtil.isEmpty(reason)){
                throw new BusinessException(OtaExceptionEnum.PARAM_IS_ERROR, "reason is null");
            }
            this.otaService.updateStrategyReport(deviceId, upgradeResult,reason);
        } catch (Exception e) {
            LOGGER.error("update upgrade log failed: ", e);
        }
    }
    /**
     * 功能描述:查询策略升级 升级失败版本
     * @param: [deviceId, upgradeResult]
     * @return: void
     * @auther: nongchongwei
     * @date: 2018/11/27 11:32
     */
    @ResponseBody
    @Override
    public List<String> selectFailUpgradeVersion(@RequestParam(value = "planId") Long planId) {
        if(CommonUtil.isEmpty(planId)){
            throw new BusinessException(OtaExceptionEnum.PARAM_IS_ERROR, "PlanId is null");
        }
        return  this.otaService.selectFailUpgradeVersion(planId);
    }

    @ResponseBody
    @Override
    public int deleteStrategyReportByPlanId(@RequestParam(value = "planId") Long planId) {
        if(CommonUtil.isEmpty(planId)){
            throw new BusinessException(OtaExceptionEnum.PARAM_IS_ERROR, "PlanId is null");
        }
        return this.otaService.deleteStrategyReportByPlanId(planId);
    }

    @ResponseBody
    @Override
    public List<StrategyReportGroupVo> getStrategyReport( @RequestParam(value = "productId") Long productId) {
        if(CommonUtil.isEmpty(productId)){
            throw new BusinessException(OtaExceptionEnum.PARAM_IS_ERROR, "productId is null");
        }
        return this.otaService.selectStrategyReportByGroup(productId);
    }

    @Override
    public Page<StrategyReportResp> selectStrategyReportByGroupAsPage(@RequestBody StrategyReportSearchReqDto dto) {
        if(CommonUtil.isEmpty(dto.getPlanId())){
            throw new BusinessException(OtaExceptionEnum.PARAM_IS_ERROR, "planId is null");
        }
        if(CommonUtil.isEmpty(dto.getStrategyGroup())){
            throw new BusinessException(OtaExceptionEnum.PARAM_IS_ERROR, "strategyGroup is null");
        }
        return this.otaService.selectStrategyReportByGroupAsPage(dto);
    }

    /**
     * 描述：查询设备版本
     * @author nongchognwei
     * @created 2018/8/9 10:35
     * @param deviceId 设备Id
     * @return void
     */
    @ResponseBody
    @Override
    public String getOtaDeviceVersion(@RequestParam(value = "deviceId") String deviceId) {
        return this.otaService.getOtaDeviceVersion(deviceId);
    }

    /**
     * 描述：根据产品id和版本查询固件url
     * @author nongchongwei
     * @date 2018/9/6 15:41
     * @param
     * @return
     */
    @ResponseBody
    @Override
    public Map<String,String>  getFirmwareUrl(@RequestParam(value = "productId") Long productId, @RequestParam(value = "version") String version) {
        if(StringUtil.isEmpty(version)){
            throw new BusinessException(OtaExceptionEnum.PARAM_IS_ERROR, "version is null");
        }
        if(CommonUtil.isEmpty(productId)){
            throw new BusinessException(OtaExceptionEnum.PARAM_IS_ERROR, "productId is null");
        }
        return this.otaService.getFirmwareUrl(productId,version);
    }

    /**
     * 描述：获取版本百分比
     * @author nongchongwei
     * @date 2018/9/6 15:41
     * @param
     * @return
     */
    @ResponseBody
    @Override
    public Map<String, String> getVersionPercent(@RequestBody VersionPercentReq versionPercentReq) {
        if (CollectionUtils.isEmpty(versionPercentReq.getVersionList())) {
            throw new BusinessException(OtaExceptionEnum.PARAM_IS_ERROR, "version list is null");
        }
        if(CommonUtil.isEmpty(versionPercentReq.getProductId())){
            throw new BusinessException(OtaExceptionEnum.PARAM_IS_ERROR, "product id is null");
        }
        if(CommonUtil.isEmpty(versionPercentReq.getTenantId())){
            throw new BusinessException(OtaExceptionEnum.PARAM_IS_ERROR, "tenant id is null");
        }

        return this.otaService.getVersionPercent(versionPercentReq);
    }

    /**
     * 描述： 根据model获取升级计划
     * @author nongchongwei
     * @date 2018/9/6 15:41
     * @param
     * @return
     */
    @ResponseBody
    @Override
    public UpgradePlanResp getUpgradePlanByProductModel(@RequestParam(value = "model") String model) {
        UpgradePlanReq upgradePlanReq = new UpgradePlanReq();
        if (StringUtils.isEmpty(model)) {
            throw new BusinessException(ProductExceptionEnum.PRODUCT_ID_NOTNULL);
        }
        Product product = ProductServiceCoreUtils.getProductByProductModel(model);
        if (product == null) {
            throw new BusinessException(ProductExceptionEnum.PRODUCT_NOT_EXIST);
        }
        upgradePlanReq.setProductId(product.getId());
        upgradePlanReq.setTenantId(product.getTenantId());
        return this.otaService.getUpgradePlan(upgradePlanReq);
    }

    /**
     * 描述： 根据产品model获取固件下载url
     * @author nongchongwei
     * @date 2018/9/6 15:40
     * @param
     * @return
     */
    @ResponseBody
    @Override
    public Map<String, String> getFirmwareUrlByModel(@RequestParam(value = "model") String model, @RequestParam(value = "version") String version) {
        if(StringUtil.isEmpty(version)){
            throw new BusinessException(OtaExceptionEnum.PARAM_IS_ERROR, "version is null");
        }
        if(StringUtil.isEmpty(model)){
            throw new BusinessException(OtaExceptionEnum.PARAM_IS_ERROR, "model is null");
        }
        Product product = ProductServiceCoreUtils.getProductByProductModel(model);
        if (product == null) {
            throw new BusinessException(ProductExceptionEnum.PRODUCT_NOT_EXIST);
        }
        Long productId = product.getId();
        return this.otaService.getFirmwareUrl(productId,version);
    }

    /**
     * 描述：保存策略配置
     * @author nongchongwei
     * @date 2018/11/15 10:50
     * @param
     * @return
     */
    @ResponseBody
    @Override
    public int saveStrategyConfig(@RequestBody StrategyConfigDto strategyConfigDto) {
        checkParamStrategyConfigDto(strategyConfigDto);
        int retCode = 0;
        try {
            retCode = this.otaService.saveStrategyConfig(strategyConfigDto);
        } catch (Exception e) {
            LOGGER.error("saveStrategyConfig error->",e);
            e.printStackTrace();
            throw new BusinessException(OtaExceptionEnum.SAVE_STRATEGY_CONFIG_ERROR);
        }
        return retCode;
    }
    /**
     * 描述：StrategyConfigDto 参数校验
     * @author nongchongwei
     * @date 2018/11/16 10:44
     * @param
     * @return
     */
    void checkParamStrategyConfigDto(StrategyConfigDto strategyConfigDto){
        if(null == strategyConfigDto){
            LOGGER.info("##ota## param is error,strategyConfigDto is null");
            throw new BusinessException(OtaExceptionEnum.PARAM_IS_ERROR, "strategyConfigDto is null");
        }
        List<StrategyConfigReq> strategyConfigList = strategyConfigDto.getStrategyConfigList();
        if(null != strategyConfigList && strategyConfigList.size() > 0){
            for(StrategyConfigReq strategyConfigReq : strategyConfigList){
                LOGGER.info("##ota## saveStrategyConfig strategyConfigReq {}", JSON.toJSONString(strategyConfigReq));
                if(CommonUtil.isEmpty(strategyConfigReq.getStrategyGroup())){
                    LOGGER.info("##ota## param is error,strategyGroup is null");
                    throw new BusinessException(OtaExceptionEnum.PARAM_IS_ERROR, "strategyGroup is null");
                }
                if(CommonUtil.isEmpty(strategyConfigReq.getTenantId())){
                    LOGGER.info("##ota## param is error,tenantId is null");
                    throw new BusinessException(OtaExceptionEnum.PARAM_IS_ERROR, "tenantId is null");
                }
                if(CommonUtil.isEmpty(strategyConfigReq.getPlanId())){
                    LOGGER.info("##ota## param is error,planId is null");
                    throw new BusinessException(OtaExceptionEnum.PARAM_IS_ERROR, "planId is null");
                }
                if(CommonUtil.isEmpty(strategyConfigReq.getThreshold())){
                    LOGGER.info("##ota## param is error,threshold is null");
                    throw new BusinessException(OtaExceptionEnum.PARAM_IS_ERROR, "threshold is null");
                }
                if(CommonUtil.isEmpty(strategyConfigReq.getUpgradeTotal())){
                    LOGGER.info("##ota## param is error,upgradeTotal is null");
                    throw new BusinessException(OtaExceptionEnum.PARAM_IS_ERROR, "upgradeTotal is null");
                }
                if(CommonUtil.isEmpty(strategyConfigReq.getTriggerAction())){
                    LOGGER.info("##ota## param is error,triggerAction is null");
                    throw new BusinessException(OtaExceptionEnum.PARAM_IS_ERROR, "triggerAction is null");
                }
            }
        }
    }
    /**
     * 描述：查询策略配置
     * @author nongchongwei
     * @date 2018/11/15 14:25
     * @param
     * @return
     */
    @ResponseBody
    @Override
    public List<StrategyConfigResp> getStrategyConfig(@RequestParam(value = "planId") Long planId, @RequestParam(value = "tenantId") Long tenantId) {
        LOGGER.info("##ota## saveStrategyConfig planId->{},tenantId->{}", planId,tenantId);
        if(CommonUtil.isEmpty(tenantId)){
            LOGGER.info("##ota## param is error,tenantId is null");
            throw new BusinessException(OtaExceptionEnum.PARAM_IS_ERROR, "tenantId is null");
        }
        if(CommonUtil.isEmpty(planId)){
            LOGGER.info("##ota## param is error,planId is null");
            throw new BusinessException(OtaExceptionEnum.PARAM_IS_ERROR, "planId is null");
        }
        return this.otaService.getStrategyConfig(planId,tenantId);
    }

    /**
     * 描述：查询策略明细 指定uuid升级
     * @author nongchongwei
     * @date 2018/11/15 20:04
     * @param
     * @return
     */
    @ResponseBody
    @Override
    public Set<String>  getStrategyDetailUuid(@RequestParam(value = "planId") Long planId) {
        LOGGER.info("##ota## getStrategyDetailUuid planId->{}", planId);
        if(CommonUtil.isEmpty(planId)){
            LOGGER.info("##ota## param is error,planId is null");
            throw new BusinessException(OtaExceptionEnum.PARAM_IS_ERROR, "planId is null");
        }
        return this.otaService.getStrategyDetail(planId);
    }

    /**
     * 描述：查询计划编辑的批次号 指定批次升级
     * @author nongchongwei
     * @date 2018/11/15 20:04
     * @param
     * @return
     */
    @ResponseBody
    @Override
    public List<Long> selectBatchByPlanId(@RequestParam(value = "planId") Long planId) {
        LOGGER.info("##ota## selectBatchByPlanId planId->{}", planId);
        if(CommonUtil.isEmpty(planId)){
            LOGGER.info("##ota## param is error,planId is null");
            throw new BusinessException(OtaExceptionEnum.PARAM_IS_ERROR, "planId is null");
        }
        return this.otaService.selectBatchByPlanId(planId);
    }

    /**
     * 描述：查询策略明细 策略升级
     * @author nongchongwei
     * @date 2018/11/15 20:04
     * @param
     * @return
     */
    @ResponseBody
    @Override
    public List<String> getStrategyDetailWithGroup(@RequestParam(value = "planId") Long planId,@RequestParam(value = "group") Integer group) {
        LOGGER.info("##ota## getStrategyDetailWithGroup planId->{},group->{}", planId,group);
        if(CommonUtil.isEmpty(planId)){
            LOGGER.info("##ota## param is error,planId is null");
            throw new BusinessException(OtaExceptionEnum.PARAM_IS_ERROR, "planId is null");
        }
        if(CommonUtil.isEmpty(group)){
            LOGGER.info("##ota## param is error,group is null");
            throw new BusinessException(OtaExceptionEnum.PARAM_IS_ERROR, "group is null");
        }
        return this.otaService.getStrategyDetailWithGroup(planId,group);
    }

    /**
     * 描述：查询策略明细 策略升级 查计划下全部
     * @author nongchongwei
     * @date 2018/11/15 20:04
     * @param
     * @return
     */
    @ResponseBody
    @Override
    public List<String> getStrategyDetailWithPlanId(@RequestParam(value = "planId") Long planId) {
        LOGGER.info("##ota## getStrategyDetailWithPlanId planId->{}", planId);
        if(CommonUtil.isEmpty(planId)){
            LOGGER.info("##ota## param is error,planId is null");
            throw new BusinessException(OtaExceptionEnum.PARAM_IS_ERROR, "planId is null");
        }
        return this.otaService.getStrategyDetailWithPlanId(planId);
    }

    /**
     * 描述：删除策略明细
     * @author nongchongwei
     * @date 2018/11/15 20:04
     * @param
     * @return
     */
    @ResponseBody
    @Override
    public int delStrategyDetail(@RequestParam(value = "planId") Long planId) {
        LOGGER.info("##ota## delStrategyDetail planId->{}", planId);
        if(CommonUtil.isEmpty(planId)){
            LOGGER.info("##ota## param is error,planId is null");
            throw new BusinessException(OtaExceptionEnum.PARAM_IS_ERROR, "planId is null");
        }
        return this.otaService.delStrategyDetail(planId);
    }


    /**
     * 描述：查询策略明细 批次升级
     * @author nongchongwei
     * @date 2018/11/15 20:04
     * @param
     * @return
     */
    @ResponseBody
    @Override
    public Set<String>  getStrategyDetailWithBatchNum(@RequestParam(value = "planId")Long planId) {
        LOGGER.info("##ota## getStrategyDetailWithBatchNum planId->{}", planId);
        if(CommonUtil.isEmpty(planId)){
            LOGGER.info("##ota## param is error,planId is null");
            throw new BusinessException(OtaExceptionEnum.PARAM_IS_ERROR, "planId is null");
        }
        return this.otaService.getStrategyDetailWithBatchNum(planId);
    }

    /**
     * 描述： 保存策略明细
     * @author nongchongwei
     * @date 2018/11/16 10:50
     * @param
     * @return
     */
    @ResponseBody
    @Override
    public void saveStrategyDetail(@RequestBody StrategyDetailDto strategyDetailDto) {
        this.checkParamStrategyDetailDto(strategyDetailDto);
        this.otaService.saveStrategyDetail(strategyDetailDto);
    }

    private void checkParamStrategyDetailDto(StrategyDetailDto strategyDetailDto) {
        if(null == strategyDetailDto){
            LOGGER.info("##ota## saveStrategyDetail, param is error,strategyDetailDto is null");
            throw new BusinessException(OtaExceptionEnum.PARAM_IS_ERROR, "strategyConfigDto is null");
        }
        if(null == strategyDetailDto.getUpgradeScope() || (
                !UpgradeScopeEnum.UUID.getValue().equals(strategyDetailDto.getUpgradeScope()) &&
                !UpgradeScopeEnum.BATCH.getValue().equals(strategyDetailDto.getUpgradeScope()) &&
                !UpgradeScopeEnum.FULL.getValue().equals(strategyDetailDto.getUpgradeScope())
        )
        ){
            LOGGER.info("##ota## saveStrategyDetail, param is error,upgradeScope ->{}",strategyDetailDto.getUpgradeScope());
            throw new BusinessException(OtaExceptionEnum.PARAM_IS_ERROR, "upgradeScope is error");
        }
        if(UpgradeScopeEnum.FULL.getValue().equals(strategyDetailDto.getUpgradeScope())){
            Map<Integer, List<StrategyDetailReq>> strategyDetailReqMap =  strategyDetailDto.getStrategyDetailReqMap();
            for (List<StrategyDetailReq> strategyDetailReqList : strategyDetailReqMap.values()) {
                if(null == strategyDetailReqList || strategyDetailReqList.isEmpty()) {
                    LOGGER.info("##ota## saveStrategyDetail param is error, full upgrade strategyDetailReqList is null");
                    throw new BusinessException(OtaExceptionEnum.PARAM_IS_ERROR, "strategyDetailReqList is null");
                }
                strategyDetailReqList.forEach(
                        strategyDetailReq->{
                            if(CommonUtil.isEmpty(strategyDetailReq.getPlanId())){
                                LOGGER.info("##ota## saveStrategyDetail param is error,planId is null");
                                throw new BusinessException(OtaExceptionEnum.PARAM_IS_ERROR, "planId is null");
                            }
                            if(CommonUtil.isEmpty(strategyDetailReq.getStrategyGroup())){
                                LOGGER.info("##ota## saveStrategyDetail param is error,strategyGroup is null");
                                throw new BusinessException(OtaExceptionEnum.PARAM_IS_ERROR, "strategyGroup is null");
                            }
                        }
                );
            }
        }else if(UpgradeScopeEnum.UUID.getValue().equals(strategyDetailDto.getUpgradeScope())){
            List<StrategyDetailReq> strategyDetailReqList = strategyDetailDto.getStrategyDetailReqList();
            if(null == strategyDetailReqList || strategyDetailReqList.isEmpty()){
                LOGGER.info("##ota## saveStrategyDetail param is error, uuid upgrade strategyDetailReqList is null");
                throw new BusinessException(OtaExceptionEnum.PARAM_IS_ERROR, "strategyDetailReqList is null");
            }
            strategyDetailReqList.forEach(
                    strategyDetailReq->{
                        if(CommonUtil.isEmpty(strategyDetailReq.getPlanId())){
                            LOGGER.info("##ota## saveStrategyDetail param is error, uuid upgrade planId is null");
                            throw new BusinessException(OtaExceptionEnum.PARAM_IS_ERROR, "planId is null");
                        }
                        if(StringUtil.isEmpty(strategyDetailReq.getDeviceUuid())){
                            LOGGER.info("##ota## saveStrategyDetail param is error, uuid upgrade devId is null");
                            throw new BusinessException(OtaExceptionEnum.PARAM_IS_ERROR, "devId is null");
                        }
                    }
            );
        } else if(UpgradeScopeEnum.BATCH.getValue().equals(strategyDetailDto.getUpgradeScope())){
            List<StrategyDetailReq> strategyDetailReqList = strategyDetailDto.getStrategyDetailReqList();
            if(null == strategyDetailReqList || strategyDetailReqList.isEmpty()){
                LOGGER.info("##ota## saveStrategyDetail param is error, batch upgrade strategyDetailReqList is null");
                throw new BusinessException(OtaExceptionEnum.PARAM_IS_ERROR, "strategyDetailReqList is null");
            }
            strategyDetailReqList.forEach(
                    strategyDetailReq->{
                        if(CommonUtil.isEmpty(strategyDetailReq.getPlanId())){
                            LOGGER.info("##ota## saveStrategyDetail param is error, uuid upgrade planId is null");
                            throw new BusinessException(OtaExceptionEnum.PARAM_IS_ERROR, "planId is null");
                        }
                        if(CommonUtil.isEmpty(strategyDetailReq.getBatchNum())){
                            LOGGER.info("##ota## saveStrategyDetail param is error, uuid upgrade batchNum is null");
                            throw new BusinessException(OtaExceptionEnum.PARAM_IS_ERROR, "batchNum is null");
                        }
                    }
            );
        }



    }

    /**
     * 描述：根据产品id查版本
     * @author nongchongwei
     * @date 2018/9/6 15:40
     * @param
     * @return
     */
    @ResponseBody
    @Override
    public List<String> getVersionByProductId(@RequestParam(value = "productId") Long productId){
        this.checkProductId(productId);
        return this.otaService.getVersionByProductId(productId);
    }

    /**
     * 描述：校验升级计划是否重复
     * @author nongchongwei
     * @date 2018/7/24 20:00
     * @param  upgradePlanEditReq
     * @return
     */
    private void checkVersionDuplication(UpgradePlanEditReq upgradePlanEditReq) {
        List<UpgradePlanDetailEditReq> upgradePlanDetailEditReqList = upgradePlanEditReq.getUpgradePlanDetailEditReqList();

        if(null != upgradePlanDetailEditReqList && upgradePlanDetailEditReqList.size() > 0){
            List<String> substituteVersionList = null;
            List<String> transitionVersionList = null;
            List<String> versionList = new ArrayList<>();
            for(UpgradePlanDetailEditReq upgradePlanDetailEditReq : upgradePlanDetailEditReqList){
                substituteVersionList = upgradePlanDetailEditReq.getSubstituteVersionList();
                transitionVersionList = upgradePlanDetailEditReq.getTransitionVersionList();
                if(null != substituteVersionList && substituteVersionList.size() > 0){
                    for(String substituteVersion : substituteVersionList){
                        if(!versionList.contains(substituteVersion)){
                            versionList.add(substituteVersion);
                        }else {
                            throw new BusinessException(OtaExceptionEnum.PARAM_IS_ERROR, "substituteVersion duplication");
                        }
                    }
                }
                if(null != transitionVersionList && transitionVersionList.size() > 0){
                    for(String transitionVersion : transitionVersionList){
                        if(versionList.contains(transitionVersion)){
                            throw new BusinessException(OtaExceptionEnum.PARAM_IS_ERROR, "transitionVersion duplication");
                        }else {
                            versionList.add(transitionVersion);
                        }
                    }
                }
            }
        }
    }

    /**
     * 描述：检查租户是合法
     * @author maochengyuan
     * @created 2018/7/25 14:57
     * @param tenantId
     * @return void
     */
    private void checkTenantId(Long tenantId){
        if(CommonUtil.isEmpty(tenantId)){
            throw new BusinessException(OtaExceptionEnum.PARAM_IS_ERROR, "tenantId is empty");
        }
    }

    /**
     * 描述：检查产品id是合法
     * @author maochengyuan
     * @created 2018/7/25 14:57
     * @param productId
     * @return void
     */
    private void checkProductId(Long productId){
        if(CommonUtil.isEmpty(productId)){
            throw new BusinessException(OtaExceptionEnum.PARAM_IS_ERROR, "productId is empty");
        }
    }
    
    @Override
    @ResponseBody
	public Page<OtaFileInfoResp> getOtaFileList(@RequestBody OtaPageReq pageReq) {
		return otaService.getOtaFileList(pageReq);
	}
	
	@Override
	@ResponseBody
	public int saveOtaFileInfo(@RequestBody OtaFileInfoReq otaFileInfoReq) {
		return otaService.saveOtaFileInfo(otaFileInfoReq);
	}
	
	@Override
	@ResponseBody
	public int updateOtaFileInfo(@RequestBody OtaFileInfoReq otaFileInfoReq) {
		return otaService.updateOtaFileInfo(otaFileInfoReq);
	}
	
	@Override
	@ResponseBody
	public OtaFileInfoResp findOtaFileInfoByProductId(@RequestBody OtaFileInfoReq otaFileInfoReq) {
		return otaService.findOtaFileInfoByProductId(otaFileInfoReq);
	}

    @ResponseBody
    @Override
    public int deleteByFirmwareId(@RequestParam(value = "id") Long id) {
        try{
            return  otaService.deleteByFirmwareId(id);
        }catch (Exception e) {
            throw new BusinessException(OtaExceptionEnum.DELETE_FIRMWARE_ERROR, e);
        }
    }

}
