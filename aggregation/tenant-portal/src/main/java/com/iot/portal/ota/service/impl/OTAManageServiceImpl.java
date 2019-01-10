package com.iot.portal.ota.service.impl;

import com.alibaba.fastjson.JSON;
import com.iot.common.beans.BeanUtil;
import com.iot.common.exception.BusinessException;
import com.iot.common.helper.Page;
import com.iot.common.util.CommonUtil;
import com.iot.common.util.StringUtil;
import com.iot.device.api.OTAServiceApi;
import com.iot.device.api.ProductApi;
import com.iot.device.enums.ota.*;
import com.iot.device.util.RedisKeyUtil;
import com.iot.device.vo.req.ota.*;
import com.iot.device.vo.rsp.ProductResp;
import com.iot.device.vo.rsp.ota.*;
import com.iot.file.api.FileApi;
import com.iot.portal.common.service.CommonServiceImpl;
import com.iot.portal.constant.TenantConstant;
import com.iot.portal.exception.OtaBusinessExceptionEnum;
import com.iot.portal.ota.service.OTAManageService;
import com.iot.portal.ota.vo.*;
import com.iot.redis.RedisCacheUtil;
import com.iot.saas.SaaSContextHolder;
import com.iot.shcs.ota.OtaSmartHomeApi;
import com.iot.user.api.UserApi;
import com.iot.user.vo.FetchUserResp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import sun.misc.BASE64Decoder;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 项目名称：IOT云平台
 * 模块名称：聚合层
 * 功能描述：OTA管理接口实现
 * 创建人： maochengyuan
 * 创建时间：2018/7/24 15:32
 * 修改人： maochengyuan
 * 修改时间：2018/7/24 15:32
 * 修改描述：
 */
@Service("OTAManageService")
public class OTAManageServiceImpl extends CommonServiceImpl implements OTAManageService {

    private final static Logger logger = LoggerFactory.getLogger(OTAManageServiceImpl.class);

    @Autowired
    private OtaSmartHomeApi otaServiceApi;

    @Autowired
    private UserApi userApi;

    @Autowired
    private ProductApi productApi;

    @Autowired
    private FileApi fileApi;

    @Autowired
    private OTAServiceApi serviceApi;


    /**产品id*/
    private static final String PRODUCT_ID = "productId";

    /**计划id*/
    private static final String PLAN_ID = "planId";

    /**计划id*/
    private static final String FIRMWARE_ID = "firmwareId";

    /**
     * 描述：查询升级日志
     * @author nongchongwei
     * @date 2018/7/24 20:00
     * @param  upgradeLogSearchReq
     * @return
     */
    @Override
    public Page<UpgradeLogVo> getUpgradeLog(UpgradeLogSearchReq upgradeLogSearchReq) {
        UpgradeLogReq searchReq = new UpgradeLogReq();
        BeanUtils.copyProperties(upgradeLogSearchReq, searchReq);
        searchReq.setProductId(super.decryptKey(upgradeLogSearchReq.getProductId(), PRODUCT_ID));
        Page upgradeLogPageInfo = otaServiceApi.getUpgradeLog(searchReq);
        Page<UpgradeLogVo> upgradeLogVoPage=new Page<>();
        BeanUtils.copyProperties(upgradeLogPageInfo,upgradeLogVoPage,"result");
        UpgradeLogVo upgradeLogVo = null;
        List<UpgradeLogVo> upgradeLogVoList = new ArrayList<>();

        List<UpgradeLogResp> upgradeLogRespList  = upgradeLogPageInfo.getResult();
        for(UpgradeLogResp upgradeLogResp : upgradeLogRespList){
            upgradeLogVo = new UpgradeLogVo();
            BeanUtils.copyProperties(upgradeLogResp, upgradeLogVo);
            upgradeLogVoList.add(upgradeLogVo);
        }
        upgradeLogVoPage.setResult(upgradeLogVoList);
        return upgradeLogVoPage;
    }

    /**
     * 描述：新增OTA升级版本信息
     * @author maochengyuan
     * @created 2018/7/24 19:44
     * @param firmwareVersionReq OTA升级版本
     * @return int
     */
    @Override
    public int createFirmwareVersion(FirmwareVersionReq firmwareVersionReq) {
        logger.info("createFirmwareVersion {}", JSON.toJSONString(firmwareVersionReq));
        if(StringUtil.isBlank(firmwareVersionReq.getOtaMd5())){
            throw new BusinessException(OtaBusinessExceptionEnum.INPUT_MD5_VALUE);
        }
        if(StringUtil.isBlank(firmwareVersionReq.getOtaFileId())){
            throw new BusinessException(OtaBusinessExceptionEnum.UPLOAD_FIRWARE_FIRST);
        }
        String md5 = fileApi.getObjectContentMD5(firmwareVersionReq.getOtaFileId());
        logger.info("md5->{}",md5);
        if(StringUtil.isBlank(md5)){
            fileApi.deleteObject(firmwareVersionReq.getOtaFileId());
            throw new BusinessException(OtaBusinessExceptionEnum.UPLOAD_FIRWARE_FIRST);
        }else {
            if(!firmwareVersionReq.getOtaMd5().equalsIgnoreCase(md5)){
                fileApi.deleteObject(firmwareVersionReq.getOtaFileId());
                throw new BusinessException(OtaBusinessExceptionEnum.MD5_VALUE_ERROR);
            }
        }
        FirmwareVersionReqDto dto = new FirmwareVersionReqDto();
        BeanUtils.copyProperties(firmwareVersionReq, dto);
        Long userId = SaaSContextHolder.getCurrentUserId();
        dto.setProductId(super.decryptKey(firmwareVersionReq.getProdId(), PRODUCT_ID));
        dto.setTenantId(SaaSContextHolder.currentTenantId());
        dto.setFwType(FwTypeEnum.ALL.getValue());//默认0,目前需求不明确
        dto.setCreateBy(userId);
        dto.setUpdateBy(userId);
        return this.otaServiceApi.createFirmwareVersion(dto);
    }



    @Override
    public void initFirmwareVersion(FirmwareVersionReq firmwareVersionReq) {
        logger.info("createFirmwareVersion {}", JSON.toJSONString(firmwareVersionReq));
        if(StringUtil.isBlank(firmwareVersionReq.getOtaMd5())){
            throw new BusinessException(OtaBusinessExceptionEnum.INPUT_MD5_VALUE);
        }
        if(StringUtil.isBlank(firmwareVersionReq.getOtaFileId())){
            throw new BusinessException(OtaBusinessExceptionEnum.UPLOAD_FIRWARE_FIRST);
        }
        String md5 = fileApi.getObjectContentMD5(firmwareVersionReq.getOtaFileId());
        logger.info("md5->{}",md5);
        if(StringUtil.isBlank(md5)){
            fileApi.deleteObject(firmwareVersionReq.getOtaFileId());
            throw new BusinessException(OtaBusinessExceptionEnum.UPLOAD_FIRWARE_FIRST);
        }else {
            if(!firmwareVersionReq.getOtaMd5().equalsIgnoreCase(md5)){
                fileApi.deleteObject(firmwareVersionReq.getOtaFileId());
                throw new BusinessException(OtaBusinessExceptionEnum.MD5_VALUE_ERROR);
            }
        }

        FirmwareVersionDto dto = new FirmwareVersionDto();
        BeanUtils.copyProperties(firmwareVersionReq, dto);
        Long userId = SaaSContextHolder.getCurrentUserId();
        /**前端拿到的id不是加密的*/
        dto.setProductId(Long.parseLong(firmwareVersionReq.getProdId()));
        dto.setTenantId(SaaSContextHolder.currentTenantId());
        dto.setFwType(FwTypeEnum.ALL.getValue());//默认0,目前需求不明确
        dto.setCreateBy(userId);
        dto.setUpdateBy(userId);
        this.otaServiceApi.initFirmwareVersion(dto);
    }

    @Override
    public FirmwareVersionQueryResp getInitOTAVersionInfoListByProductId(Long productId) {
        Long tenantId = SaaSContextHolder.currentTenantId();
        return this.otaServiceApi.getInitOTAVersionInfoListByProductId(tenantId,productId);
    }

    @Override
    public int saveStrategyConfig(StrategyConfigDto strategyConfigDto) {
        if(StringUtil.isEmpty(strategyConfigDto.getProductId())){
            return 0;
        }
        Long tenantId = SaaSContextHolder.currentTenantId();Long productId = super.decryptKey(strategyConfigDto.getProductId(), PRODUCT_ID);
        UpgradePlanResp upgradePlanResp = this.serviceApi.getUpgradePlanByProductId(productId);
        strategyConfigDto.setPlanId(upgradePlanResp.getId());
        strategyConfigDto.setTenantId(tenantId);
        if(null != strategyConfigDto && null != strategyConfigDto.getStrategyConfigList()
                && !strategyConfigDto.getStrategyConfigList().isEmpty()
                && !StringUtil.isEmpty(strategyConfigDto.getProductId())){
            for(StrategyConfigReq strategyConfigReq : strategyConfigDto.getStrategyConfigList()){
                strategyConfigReq.setTenantId(tenantId);
                strategyConfigReq.setPlanId(upgradePlanResp.getId());
            }
        }
        return this.serviceApi.saveStrategyConfig(strategyConfigDto);
    }

    @Override
    public List<StrategyConfigResp> getStrategyConfig(String prodId) {
        Long productId = super.decryptKey(prodId, PRODUCT_ID);
        UpgradePlanResp upgradePlanResp = serviceApi.getUpgradePlanByProductId(productId);
        if(null != upgradePlanResp && null != upgradePlanResp.getId()){
            Long planId = upgradePlanResp.getId();
            Long tenantId = SaaSContextHolder.currentTenantId();
            return this.serviceApi.getStrategyConfig(planId,tenantId);
        }else {
            List<StrategyConfigResp> strategyConfigRespList = new ArrayList<>();
            return strategyConfigRespList;
        }

    }

    @Override
    public List<StrategyReportGroupVo> getStrategyReport(String prodId) {
        Long productId = super.decryptKey(prodId, PRODUCT_ID);
        return this.serviceApi.getStrategyReport(productId);
    }

    @Override
    public Page<StrategyReportResp> selectStrategyReportByGroupAsPage(StrategyReportSearchReqDto dto) {
        return this.serviceApi.selectStrategyReportByGroupAsPage(dto);
    }

    /**
     * 描述：上传OTA升级文件
     * @author maochengyuan
     * @created 2018/7/27 16:11
     * @param multipartRequest 文件对象
     * @return java.lang.String 文件id
     */
    @Override
    public String uploadFirmwareOtaFile(MultipartHttpServletRequest multipartRequest){
        return super.upLoadSoleFile(multipartRequest, TenantConstant.OTA_ALLOW_FILE_TYPES, TenantConstant.OTA_MAX_FILE_SIZE);
    }

    /**
     * 描述：依据产品ID查询升级版本列表
     * @author maochengyuan
     * @created 2018/7/24 19:44
     * @param req 查询参数
     * @return com.iot.common.helper.Page<com.iot.device.vo.rsp.ota.FirmwareVersionResp>
     */
    @Override
    public Page<FirmwareVersionVo> getFirmwareVersionListByProductId(FirmwareVersionSearchReq req) {
        FirmwareVersionSearchReqDto dto = new FirmwareVersionSearchReqDto();
        BeanUtil.copyProperties(req, dto);
        dto.setUserId(SaaSContextHolder.getCurrentUserId());
        dto.setProductId(super.decryptKey(req.getProdId(), PRODUCT_ID));
        dto.setTenantId(SaaSContextHolder.currentTenantId());

        Page<FirmwareVersionResp> firmwareVersionRespPage = this.otaServiceApi.getFirmwareVersionListByProductId(dto);

        Page<FirmwareVersionVo> firmwareVersionVoPage=new Page<FirmwareVersionVo>();
        BeanUtils.copyProperties(firmwareVersionRespPage,firmwareVersionVoPage,"result");
        FirmwareVersionVo firmwareVersionVo = null;
        List<FirmwareVersionVo> firmwareVersionVoList = new ArrayList<FirmwareVersionVo>();

        List<FirmwareVersionResp> firmwareVersionRespList  = firmwareVersionRespPage.getResult();
        for(FirmwareVersionResp firmwareVersionResp : firmwareVersionRespList){
            firmwareVersionVo = new FirmwareVersionVo();
            BeanUtils.copyProperties(firmwareVersionResp, firmwareVersionVo);
            firmwareVersionVo.setFirmwareId(super.encryptKey(firmwareVersionResp.getId()));
            firmwareVersionVoList.add(firmwareVersionVo);
        }
        firmwareVersionVoPage.setResult(firmwareVersionVoList);
        return firmwareVersionVoPage;
    }

    /**
     * 描述：查询OTA版本合法性（唯一性）
     * @author maochengyuan
     * @created 2018/7/24 19:44
     * @param productId 产品id
     * @param otaVersion OTA版本
     * @return com.iot.device.vo.rsp.ota.CheckVersionResp
     */
    @Override
    public Boolean checkVersionLegality(String productId, String otaVersion) {
        Long tenantId = SaaSContextHolder.currentTenantId();
        Long prodId = super.decryptKey(productId, PRODUCT_ID);
        return this.otaServiceApi.checkVersionLegality(tenantId, prodId, otaVersion);
    }

    /**
     * 描述：查询OTA版本合法性（唯一性）
     * @author nongchongwei
     * @created 2018/7/24 19:44
     * @param productId 产品id
     * @param otaVersion OTA版本
     * @return com.iot.device.vo.rsp.ota.CheckVersionResp
     */
    @Override
    public Boolean checkVersionLegalityByProId(String productId, String otaVersion) {
        Long tenantId = SaaSContextHolder.currentTenantId();
        Long prodId = Long.parseLong(productId);
        return this.otaServiceApi.checkVersionLegality(tenantId, prodId, otaVersion);
    }

    /**
     * 描述：计划操作记录分页查询
     * @author nongchongwei
     * @date 2018/7/24 20:00
     * @param  upgradePlanSearchReq
     * @return
     */
    @Override
    public Page<UpgradePlanLogVo> getUpgradePlanLog(UpgradePlanSearchReq upgradePlanSearchReq) {
        UpgradePlanReq upgradePlanReq = new UpgradePlanReq();
        BeanUtils.copyProperties(upgradePlanSearchReq, upgradePlanReq);
        upgradePlanReq.setProductId(super.decryptKey(upgradePlanSearchReq.getProductId(), PRODUCT_ID));
        Long tenantId = SaaSContextHolder.currentTenantId();
        upgradePlanReq.setTenantId(tenantId);
        Page<UpgradePlanLogResp> upgradePlanLogPageInfo = this.otaServiceApi.getUpgradePlanLog(upgradePlanReq);
        Page<UpgradePlanLogVo> upgradePlanLogVoPage=new Page<>();
        BeanUtils.copyProperties(upgradePlanLogPageInfo,upgradePlanLogVoPage,"result");
        List<UpgradePlanLogResp> upgradePlanLogRespList = upgradePlanLogPageInfo.getResult();
        if(CollectionUtils.isEmpty(upgradePlanLogRespList)){
            return upgradePlanLogVoPage;
        }
        List<UpgradePlanLogVo> upgradePlanLogVoList = new ArrayList<>();
        UpgradePlanLogVo upgradePlanLogVo = null;
        List<Long> userIdList = new ArrayList<>();
        for(UpgradePlanLogResp upgradePlanLogResp : upgradePlanLogRespList){
            userIdList.add(upgradePlanLogResp.getCreateBy());
        }
        if(userIdList.size()<1){
            return upgradePlanLogVoPage;
        }
        Map<Long, FetchUserResp> fetchUserRespMap = userApi.getByUserIds(userIdList);
        for(UpgradePlanLogResp upgradePlanLogResp : upgradePlanLogRespList){
            upgradePlanLogVo = new UpgradePlanLogVo();
            BeanUtils.copyProperties(upgradePlanLogResp, upgradePlanLogVo);
            if(null != upgradePlanLogResp.getCreateBy() && null != fetchUserRespMap.get(upgradePlanLogResp.getCreateBy())){
                upgradePlanLogVo.setUserName(fetchUserRespMap.get(upgradePlanLogResp.getCreateBy()).getUserName());
            }
            upgradePlanLogVoList.add(upgradePlanLogVo);
        }
        upgradePlanLogVoPage.setResult(upgradePlanLogVoList);
        return upgradePlanLogVoPage;
    }

    /**
     * 描述：产品分页查询
     * @author nongchongwei
     * @date 2018/7/24 20:00
     * @param  productOtaSearchReq
     * @return
     */
    @Override
    public Page<ProductOtaVo> getProduct(ProductOtaSearchReq productOtaSearchReq) {
        ProductOtaReq productOtaReq = new ProductOtaReq();
        Long tenantId = SaaSContextHolder.currentTenantId();
        productOtaReq.setTenantId(tenantId);
        productOtaReq.setModel(productOtaSearchReq.getModel());
        productOtaReq.setProductName(productOtaSearchReq.getProductName());
        productOtaReq.setPageNum(productOtaSearchReq.getPageNum());
        productOtaReq.setPageSize(productOtaReq.getPageSize());
        Page productPageInfo = this.productApi.getProduct(productOtaReq);
        Page<ProductOtaVo> productOtaVoPage=new Page<>();
        BeanUtils.copyProperties(productPageInfo,productOtaVoPage,"result");
        List<ProductResp> productRespList = productPageInfo.getResult();
        List<ProductOtaVo> productOtaVoList = new ArrayList<>();
        ProductOtaVo productOtaVo = null;
        for(ProductResp productResp : productRespList){
            productOtaVo = new ProductOtaVo();
            BeanUtils.copyProperties(productResp, productOtaVo);
            productOtaVo.setProductId(super.encryptKey(productResp.getId()));
            if(StringUtil.isNotBlank(productOtaVo.getIcon())){
                productOtaVo.setIcon(fileApi.getGetUrl(productOtaVo.getIcon()).getPresignedUrl());
            }
            UpgradePlanResp upgradePlanResp = serviceApi.getUpgradePlanByProductId(productResp.getId());
            if(null != upgradePlanResp && null != upgradePlanResp.getId()){
                List<String> failVersionList =  serviceApi.selectFailUpgradeVersion(upgradePlanResp.getId());
                if(null != failVersionList && !failVersionList.isEmpty()){
                    productOtaVo.setUpgradeStatus(UpgradeResultEnum.Failed.getValue());
                }
            }

            productOtaVoList.add(productOtaVo);
        }
        productOtaVoPage.setResult(productOtaVoList);
        return productOtaVoPage;
    }

    /**
     * 描述：升级计划查询
     * @author nongchongwei
     * @date 2018/7/24 20:00
     * @param  productId 产品id
     * @return
     */
    @Override
    public UpgradePlanVo getUpgradePlan(String productId) {
        UpgradePlanReq upgradePlanReq = new UpgradePlanReq();
        upgradePlanReq.setProductId(super.decryptKey(productId, PRODUCT_ID));
        upgradePlanReq.setTenantId( SaaSContextHolder.currentTenantId());
        upgradePlanReq.setUserId(SaaSContextHolder.getCurrentUserId());
        UpgradePlanResp upgradePlanResp = this.otaServiceApi.getUpgradePlan(upgradePlanReq);
        UpgradePlanVo upgradePlanVo = new UpgradePlanVo();
        BeanUtils.copyProperties(upgradePlanResp, upgradePlanVo);
        upgradePlanVo.setId(super.encryptKey(upgradePlanResp.getId()));
        upgradePlanVo.setProductId(super.encryptKey(upgradePlanResp.getProductId()));
        List<UpgradePlanDetailResp> upgradePlanDetailRespList = upgradePlanResp.getUpgradePlanDetailRespList();
        List<UpgradePlanDetailVo> upgradePlanDetailVoList = new ArrayList<>();
        UpgradePlanDetailVo upgradePlanDetailVo = null;
        if(null != upgradePlanDetailRespList && upgradePlanDetailRespList.size() > 0){
            for(UpgradePlanDetailResp upgradePlanDetailResp : upgradePlanDetailRespList){
                upgradePlanDetailVo = new UpgradePlanDetailVo();
                BeanUtils.copyProperties(upgradePlanDetailResp, upgradePlanDetailVo);
                upgradePlanDetailVo.setId(super.encryptKey(upgradePlanDetailResp.getId()));
                upgradePlanDetailVo.setSubstituteVersionList(upgradePlanDetailResp.getSubstituteVersionList());
                upgradePlanDetailVo.setTransitionVersionList(upgradePlanDetailResp.getTransitionVersionList());
                upgradePlanDetailVoList.add(upgradePlanDetailVo);
            }
        }
        Integer strategySwitch = upgradePlanResp.getStrategySwitch();
        Integer upgradeScope = upgradePlanResp.getUpgradeScope();
        if(StrategySwitchEnum.UNUSE.getValue().equals(strategySwitch) && UpgradeScopeEnum.UUID.getValue().equals(upgradeScope)){
            upgradePlanVo.setDeviceIdEditReqList(new ArrayList<>(serviceApi.getStrategyDetailUuid(upgradePlanResp.getId())));
        }else if(StrategySwitchEnum.UNUSE.getValue().equals(strategySwitch) && UpgradeScopeEnum.BATCH.getValue().equals(upgradeScope)){
            upgradePlanVo.setBatchNumEditReqList(serviceApi.selectBatchByPlanId(upgradePlanResp.getId()));
        }

        upgradePlanVo.setUpgradePlanDetailVoList(upgradePlanDetailVoList);
        if(null != upgradePlanResp && PlanStatusEnum.Pause.getValue().equals(upgradePlanResp.getPlanStatus())){
            String strategyTriggerThresholdKey = RedisKeyUtil.getUpgradeStrategyTriggerThresholdKey(upgradePlanResp.getId());
            String strategyTriggerThreshold = RedisCacheUtil.valueGet(strategyTriggerThresholdKey);
            if(StrategyTriggerEnum.Trigger.getValue().equals(strategyTriggerThreshold)){
                List<String> versionList = this.serviceApi.selectFailUpgradeVersion(upgradePlanResp.getId());
                upgradePlanVo.setFailVersionList(versionList);
            }
        }
        return upgradePlanVo;
    }
    /**
     * 描述：升级计划启动或暂停
     * @author nongchongwei
     * @date 2018/7/24 20:00
     * @param  upgradePlanUpdateReq
     * @return
     */
    @Override
    public void operatePlan(UpgradePlanUpdateReq upgradePlanUpdateReq) {
        UpgradePlanOperateReq upgradePlanOperateReq = new UpgradePlanOperateReq();
        upgradePlanOperateReq.setPlanId(super.decryptKey(upgradePlanUpdateReq.getPlanId(), PLAN_ID));
        upgradePlanOperateReq.setProductId(super.decryptKey(upgradePlanUpdateReq.getProductId(), PRODUCT_ID));
        upgradePlanOperateReq.setPlanStatus(upgradePlanUpdateReq.getPlanStatus());
        upgradePlanOperateReq.setTenantId( SaaSContextHolder.currentTenantId());
        upgradePlanOperateReq.setUserId(SaaSContextHolder.getCurrentUserId());
        otaServiceApi.operatePlan(upgradePlanOperateReq);
        otaServiceApi.noticeDevice(super.decryptKey(upgradePlanUpdateReq.getProductId(), PRODUCT_ID), SaaSContextHolder.currentTenantId(), SaaSContextHolder.getCurrentUserId());
    }
    /**
     * 描述：升级计划编辑
     * @author nongchongwei
     * @date 2018/7/24 20:00
     * @param  upgradePlanVo
     * @return
     */
    /**
     * 描述：依据产品ID获取升级版本列表
     * @author maochengyuan
     * @created 2018/7/26 20:11
     * @param productId 产品id
     * @return java.util.List<java.lang.String>
     */
    @Override
    public List<String> getOTAVersionListByProductId(String productId){
        return this.otaServiceApi.getOTAVersionListByProductId(SaaSContextHolder.currentTenantId(), super.decryptKey(productId, PRODUCT_ID));
    }

    /**
     * 描述：删除固件
     * @author nongchongwei
     * @date 2018/10/23 11:19
     * @param
     * @return
     */
    @Override
    public void deleteByFirmwareId(String firmwareId) {
        this.otaServiceApi.deleteByFirmwareId(super.decryptKey(firmwareId, FIRMWARE_ID));
    }

    /**
     * 描述：依据产品ID获取版分页查询
     * @author maochengyuan
     * @created 2018/7/26 20:11
     * @param req 查询参数
     * @return com.github.pagehelper.PageInfo<com.iot.device.vo.rsp.ota.FirmwareVersionPageResp>
     */
    @Override
    public Page<FirmwareVersionPageResp> getOTAVersionPageByProductId(FirmwareVersionSearchReq req){
        FirmwareVersionSearchReqDto dto = new FirmwareVersionSearchReqDto();
        BeanUtil.copyProperties(req, dto);
        dto.setUserId(SaaSContextHolder.getCurrentUserId());
        dto.setProductId(super.decryptKey(req.getProdId(), PRODUCT_ID));
        dto.setTenantId(SaaSContextHolder.currentTenantId());
        return this.otaServiceApi.getOTAVersionPageByProductId(dto);
    }

    /**
     * 描述：升级计划修改
     * @author nongchongwei
     * @date 2018/7/24 20:00
     * @param  upgradePlanVo
     * @return
     */
    @Override
    public void editUpgradePlan(UpgradePlanVo upgradePlanVo) {
        UpgradePlanEditReq upgradePlanEditReq = new UpgradePlanEditReq();
        BeanUtils.copyProperties(upgradePlanVo, upgradePlanEditReq);

        List<UpgradePlanDetailEditReq> upgradePlanDetailEditReqList = new ArrayList<>();
        UpgradePlanDetailEditReq upgradePlanDetailEditReq = null;
        List<UpgradePlanDetailVo>  upgradePlanDetailVoList = upgradePlanVo.getUpgradePlanDetailVoList();
        if(null != upgradePlanDetailVoList && upgradePlanDetailVoList.size() > 0 ){
            for(UpgradePlanDetailVo upgradePlanDetailVo : upgradePlanDetailVoList){
                upgradePlanDetailEditReq = new UpgradePlanDetailEditReq();
                upgradePlanDetailEditReq.setSubstituteVersionList(upgradePlanDetailVo.getSubstituteVersionList());
                upgradePlanDetailEditReq.setTransitionVersionList(upgradePlanDetailVo.getTransitionVersionList());
                upgradePlanDetailEditReqList.add(upgradePlanDetailEditReq);
            }
        }
        upgradePlanEditReq.setProductId(super.decryptKey(upgradePlanVo.getProductId(), PRODUCT_ID));
        upgradePlanEditReq.setId(super.decryptKey(upgradePlanVo.getId(), PLAN_ID));
        upgradePlanEditReq.setTenantId( SaaSContextHolder.currentTenantId());
        upgradePlanEditReq.setUserId(SaaSContextHolder.getCurrentUserId());
        upgradePlanEditReq.setUpgradePlanDetailEditReqList(upgradePlanDetailEditReqList);
        upgradePlanEditReq.setBatchNumEditReqList(upgradePlanVo.getBatchNumEditReqList());
        upgradePlanEditReq.setDeviceIdEditReqList(upgradePlanVo.getDeviceIdEditReqList());
        otaServiceApi.editUpgradePlan(upgradePlanEditReq);
    }

    /**
     * 描述：查询版本使用百分比
     * @author nongchongwei
     * @date 2018/9/5 15:01
     * @param
     * @return
     */
    @Override
    public Map<String, String> getVersionPercent(PercentReq percentReq) {
        VersionPercentReq versionPercentReq = new VersionPercentReq();
        versionPercentReq.setProductId(super.decryptKey(percentReq.getProductId(), PRODUCT_ID));
        versionPercentReq.setVersionList(percentReq.getVersionList());
        versionPercentReq.setTenantId(SaaSContextHolder.currentTenantId());
        return otaServiceApi.getVersionPercent(versionPercentReq);
    }

}
