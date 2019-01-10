package com.iot.device.service.impl;

import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import com.iot.common.exception.BusinessException;
import com.iot.common.helper.Page;
import com.iot.common.util.*;
import com.iot.device.comm.constants.ModuleConstants;
import com.iot.device.core.utils.BeanCopyUtils;
import com.iot.device.enums.ota.*;
import com.iot.device.exception.OtaExceptionEnum;
import com.iot.device.mapper.DeviceMapper;
import com.iot.device.mapper.ota.*;
import com.iot.device.model.ota.*;
import com.iot.device.service.IOtaService;
import com.iot.device.util.RedisKeyUtil;
import com.iot.device.vo.req.OtaPageReq;
import com.iot.device.vo.req.ota.*;
import com.iot.device.vo.rsp.ota.*;
import com.iot.file.api.FileApi;
import com.iot.file.dto.FileDto;
import com.iot.redis.RedisCacheUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
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
    private UpgradeLogMapper upgradeLogMapper;

    @Autowired
    private DeviceMapper deviceMapper;

    @Autowired
    private FirmwareVersionMapper firmwareVersionMapper;

    @Autowired
    private UpgradePlanLogMapper upgradePlanLogMapper;

    @Autowired
    private UpgradePlanMapper upgradePlanMapper;

    @Autowired
    private UpgradePlanDetailMapper upgradePlanDetailMapper;

    @Autowired
    private UpgradePlanSubstituteMapper upgradePlanSubstituteMapper;

    @Autowired
    private UpgradePlanTransitionMapper upgradePlanTransitionMapper;

    @Autowired
    private UpgradeDeviceVersionMapper upgradeDeviceVersionMapper;

    @Autowired
    private StrategyConfigMapper strategyConfigMapper;

    @Autowired
    private StrategyDetailMapper strategyDetailMapper;

    @Autowired
    private StrategyReportMapper strategyReportMapper;

    @Autowired
    private FileApi fileApi;


    /**
     * 描述：查询升级日志
     * @author nongchongwei
     * @date 2018/7/24 20:00
     * @param  upgradeLogReq
     * @return
     */
    @Override
    public Page<UpgradeLogResp> getUpgradeLog(UpgradeLogReq upgradeLogReq) {
        LOGGER.info("***************tProductId->{},upgradeResult()->{},TenantId->{}",upgradeLogReq.getProductId(),upgradeLogReq.getUpgradeResult(),upgradeLogReq.getTenantId());
        com.baomidou.mybatisplus.plugins.Page<UpgradeLog> page =new com.baomidou.mybatisplus.plugins.Page<UpgradeLog>(CommonUtil.getPageNum(upgradeLogReq), CommonUtil.getPageSize(upgradeLogReq));
        List<UpgradeLog> upgradeLogList = null;
        try{
            upgradeLogList = upgradeLogMapper.getUpgradeLog(page,upgradeLogReq);
        }catch (Exception e){
            LOGGER.error("getUpgradeLog error", e);
            throw new BusinessException(OtaExceptionEnum.QUERY_ERROR, e);
        }

        page.setRecords(upgradeLogList);

        com.iot.common.helper.Page<UpgradeLogResp> myPage= BeanCopyUtils.copyMybatisPlusPageToPageNoResult(page);


        List<UpgradeLogResp> fetchUserRespList = new ArrayList<>();
        UpgradeLogResp upgradeLogResp = null;
        for(UpgradeLog upgradeLog : upgradeLogList){
            upgradeLogResp = new UpgradeLogResp();
            BeanUtils.copyProperties(upgradeLog, upgradeLogResp);
            fetchUserRespList.add(upgradeLogResp);
        }
        myPage.setResult(fetchUserRespList);
        return myPage;
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
    @Override
    public Boolean checkVersionLegality(Long tenantId, Long prodId, String otaVersion){
        int count = this.firmwareVersionMapper.checkVersionLegality(tenantId, prodId, otaVersion);
        return count == 0;
    }

    /**
     * 描述：依据产品ID查询升级版本列表
     * @author maochengyuan
     * @created 2018/7/25 11:17
     * @param dto ota版本查询参数
     * @return com.iot.common.helper.Page<com.iot.device.vo.rsp.ota.FirmwareVersionResp>
     */
    @Override
    public Page<FirmwareVersionResp> getFirmwareVersionListByProductId(FirmwareVersionSearchReqDto dto){

        com.baomidou.mybatisplus.plugins.Page<FirmwareVersionResp> page =new com.baomidou.mybatisplus.plugins.Page<FirmwareVersionResp>(CommonUtil.getPageNum(dto), CommonUtil.getPageSize(dto));
        List<FirmwareVersionResp> upgradeLogList = null;
        try {
            upgradeLogList = this.firmwareVersionMapper.getFirmwareVersionListByProductId(page,dto);
            /**用于升级 上报成功时处理未上线版本是只查缓存*/
            List<String> firmwareVersionList = firmwareVersionMapper.getNotOnlineFirmwareList(dto.getTenantId(),dto.getProductId());
            String notOnlineFirmwareKey = RedisKeyUtil.getOtaUpgradeNotOnlineFirmwareKey(dto.getProductId());
            RedisCacheUtil.delete(notOnlineFirmwareKey);
            Map<String,String> verMap = new HashMap<>();
            if(null != firmwareVersionList && !firmwareVersionList.isEmpty()){
                for(String ver : firmwareVersionList){
                    verMap.put(ver,ver);
                    RedisCacheUtil.hashPutAll(notOnlineFirmwareKey,verMap);
                }
            }
        }catch (Exception e){
            LOGGER.error("getFirmwareVersionListByProductId error", e);
            throw new BusinessException(OtaExceptionEnum.QUERY_ERROR, e);
        }
        page.setRecords(upgradeLogList);
        com.iot.common.helper.Page<FirmwareVersionResp> myPage=new com.iot.common.helper.Page<FirmwareVersionResp>();
        BeanCopyUtils.copyMybatisPlusPageToPage(page,myPage);
        return myPage;
    }

    /**
     * 描述：新增OTA升级版本信息
     * @author maochengyuan
     * @created 2018/7/25 11:17
     * @param dto 版本对象
     * @return int
     */
    @Override
    public int createFirmwareVersion(FirmwareVersionReqDto dto){
        int count = this.firmwareVersionMapper.createFirmwareVersion(dto);
        /**用于升级 上报成功时处理未上线版本是只查缓存*/

        List<String> firmwareVersionList = firmwareVersionMapper.getNotOnlineFirmwareList(dto.getTenantId(),dto.getProductId());
        String notOnlineFirmwareKey = RedisKeyUtil.getOtaUpgradeNotOnlineFirmwareKey(dto.getProductId());
        RedisCacheUtil.delete(notOnlineFirmwareKey);
        Map<String,String> verMap = new HashMap<>();
        if(null != firmwareVersionList && !firmwareVersionList.isEmpty()){
            for(String ver : firmwareVersionList){
                verMap.put(ver,ver);
                RedisCacheUtil.hashPutAll(notOnlineFirmwareKey,verMap);
            }
        }
        return count;
    }

    /**
     * 描述：新增OTA升级版本信息
     * @author nongchongwei
     * @created 2018/7/25 11:17
     * @param dto 版本对象
     * @return int
     */
    @Override
    public void initFirmwareVersion(FirmwareVersionDto dto) {
        try{
            /***删除之前的版本*/
            this.firmwareVersionMapper.deleteByFProdId(dto.getTenantId(),dto.getProductId());
            /***删除缓存*/
            String firmwareKey = RedisKeyUtil.getUpgradePlanFirmwareKey(dto.getProductId());
            RedisCacheUtil.delete(firmwareKey);
            /***入库*/
            this.firmwareVersionMapper.initFirmwareVersion(dto);
        }catch (Exception e){
            LOGGER.error("initFirmwareVersion error", e);
            throw new BusinessException(OtaExceptionEnum.OTA_VERSION_CREATE_FAILED, e);
        }

    }

    /**
     * 描述：依据产品ID获取升级版本列表
     * @author maochengyuan
     * @created 2018/7/30 9:37
     * @param tenantId 租户id
     * @param productId 产品id
     * @return java.util.List<java.lang.String>
     */
    @Override
    public List<String> getOTAVersionListByProductId(Long tenantId, Long productId){
        return this.firmwareVersionMapper.getOTAVersionListByProductId(tenantId, productId);
    }

    /**
     * 描述：依据产品ID获取版分页查询
     * @author maochengyuan
     * @created 2018/7/30 9:37
     * @param req 查询参数
     * @return java.util.List<com.iot.device.vo.rsp.ota.FirmwareVersionPageResp>
     */
    @Override
    public Page<FirmwareVersionPageResp> getOTAVersionPageByProductId(FirmwareVersionSearchReqDto req){
        com.baomidou.mybatisplus.plugins.Page<FirmwareVersion> page =new com.baomidou.mybatisplus.plugins.Page<FirmwareVersion>(CommonUtil.getPageNum(req), CommonUtil.getPageSize(req));
        List<FirmwareVersion> list =  null;
        try {
            list = this.firmwareVersionMapper.getOTAVersionInfoListByProductId(page,req.getTenantId(), req.getProductId());
        }catch (Exception e){
            LOGGER.error("getOTAVersionPageByProductId error", e);
            throw new BusinessException(OtaExceptionEnum.QUERY_ERROR, e);
        }
        page.setRecords(list);

        com.iot.common.helper.Page<FirmwareVersionPageResp> myPage = BeanCopyUtils.copyMybatisPlusPageToPageNoResult(page);
        if(CommonUtil.isEmpty(list)){
            return myPage;
        }
        Map<String, Date> otaMap = list.stream().collect(Collectors.toMap(FirmwareVersion::getOtaVersion, FirmwareVersion::getVersionOnlineTime, (k1, k2)->k1));
        req.setOtaVersion(otaMap.keySet());
        /**查询该产品id现有设备总数*/
        Integer total = this.deviceMapper.getTotalByProductId(req.getTenantId(), req.getProductId());
        /**统计各个版本现有设备总数*/
        Map<String, FirmwareVersionPageResp> temp = this.firmwareVersionMapper.getOTAVersionPageByProductId(req).stream().collect(Collectors.toMap(FirmwareVersionPageResp::getOtaVersion, FirmwareVersionPageResp->FirmwareVersionPageResp, (k1, k2)->k1));
        /**统计计划中的各个版本*/
        Set<String> versionSet = this.getCurrVersionListDetail(req);
        List<FirmwareVersionPageResp> resList = new ArrayList<>();
        list.forEach(o ->{
            FirmwareVersionPageResp resp = new FirmwareVersionPageResp();
            if(temp.containsKey(o.getOtaVersion())){
                resp.setVersionQuantity(temp.get(o.getOtaVersion()).getVersionQuantity());
            }
            resp.setOtaVersion(o.getOtaVersion());
            resp.setDeviceQuantity(total);
            resp.setVersionOnlineTime(otaMap.get(o.getOtaVersion()));
            resp.setInPlan(versionSet.contains(o.getOtaVersion()));
            resList.add(resp);
        });
        myPage.setResult(resList);
        return myPage;
    }

    /**
     * 描述：指定版本信息查询
     * @author nongchongwei
     * @date 2018/7/24 20:00
     * @param  productId
     * @param  otaVersion
     * @return
     */
    public FirmwareVersion getFirmwareVersionByProductIdAndVersion(Long productId, String otaVersion){
        if(CommonUtil.isEmpty(productId) || StringUtil.isEmpty(otaVersion)){
            return null;
        }
        String firmwareKey = RedisKeyUtil.getUpgradePlanFirmwareKey(productId);
        FirmwareVersion firmwareVersion = RedisCacheUtil.hashGet(firmwareKey,otaVersion,FirmwareVersion.class);
        if(null == firmwareVersion){
            firmwareVersion = this.firmwareVersionMapper.getFirmwareVersionByProductIdAndVersion(productId,otaVersion);
            RedisCacheUtil.hashPut(firmwareKey,otaVersion,firmwareVersion,true);
        }
        return firmwareVersion;
    }

    /**
     * 描述：计划操作记录分页查询
     * @author nongchongwei
     * @date 2018/7/24 20:00
     * @param  upgradePlanReq
     * @return
     */
    @Override
    public Page<UpgradePlanLogResp> getUpgradePlanLog(UpgradePlanReq upgradePlanReq) {
        com.baomidou.mybatisplus.plugins.Page<UpgradePlanLogResp> page =new com.baomidou.mybatisplus.plugins.Page<UpgradePlanLogResp>(CommonUtil.getPageNum(upgradePlanReq), CommonUtil.getPageSize(upgradePlanReq));
        List<UpgradePlanLogResp> upgradePlanLogRespList = null;
        try {
            upgradePlanLogRespList = upgradePlanLogMapper.getUpgradePlanLog(page,upgradePlanReq);
        }catch (Exception e){
            LOGGER.error("getUpgradePlanLog error", e);
            throw new BusinessException(OtaExceptionEnum.QUERY_ERROR, e);
        }
        page.setRecords(upgradePlanLogRespList);

        com.iot.common.helper.Page<UpgradePlanLogResp> myPage=new com.iot.common.helper.Page<UpgradePlanLogResp>();
        BeanCopyUtils.copyMybatisPlusPageToPage(page,myPage);

        return myPage;
    }

    /**
     * 描述：指定计划信息查询
     * @author nongchongwei
     * @date 2018/7/24 20:00
     * @param  upgradePlanReq
     * @return
     */
    @Override
    public UpgradePlanResp getUpgradePlan(UpgradePlanReq upgradePlanReq) {
        String key = RedisKeyUtil.getUpgradePlanKey(upgradePlanReq.getProductId());
        UpgradePlanResp upgradePlanResp = RedisCacheUtil.valueObjGet(key, UpgradePlanResp.class);
        if(null == upgradePlanResp){
            upgradePlanResp = upgradePlanMapper.selectByProductId(upgradePlanReq.getProductId());

            if (CommonUtil.isEmpty(upgradePlanResp)) {
                upgradePlanResp = new UpgradePlanResp();
                if(CommonUtil.isEmpty(upgradePlanReq.getTenantId()) || CommonUtil.isEmpty(upgradePlanReq.getUserId())){
                    return upgradePlanResp;
                }
                UpgradePlan upgradePlan = new UpgradePlan();
                upgradePlan.setProductId(upgradePlanReq.getProductId());
                upgradePlan.setTenantId(upgradePlanReq.getTenantId());
                upgradePlan.setCreateBy(upgradePlanReq.getUserId());
                upgradePlan.setCreateTime(new Date());
                upgradePlan.setUpdateBy(upgradePlanReq.getUserId());
                upgradePlan.setUpdateTime(new Date());
                upgradePlan.setPlanStatus(PlanStatusEnum.Pause.getValue());
                upgradePlan.setUpgradeType(UpgradeTypeEnum.Push.getValue());
                upgradePlan.setEditedTimes(0);
                upgradePlan.setStrategySwitch(StrategySwitchEnum.UNUSE.getValue());
                upgradePlan.setUpgradeScope(UpgradeScopeEnum.FULL.getValue());
                LOGGER.info("####ota#### create ota plan begin ProductId->{}",upgradePlan.getProductId());
                upgradePlanMapper.insertUpgradePlan(upgradePlan);
                LOGGER.info("####ota#### create ota plan end ProductId->{}",upgradePlan.getProductId());
                BeanUtils.copyProperties(upgradePlan, upgradePlanResp);
            }else {
                List<UpgradePlanDetailResp> upgradePlanDetailRespList = upgradePlanDetailMapper.selectByplanId(upgradePlanResp.getId());
                for(UpgradePlanDetailResp upgradePlanDetailResp : upgradePlanDetailRespList){
                    upgradePlanDetailResp.setSubstituteVersionList(upgradePlanSubstituteMapper.selectBydetailId(upgradePlanDetailResp.getId()));
                    if(HasTransitionEnum.HAS.getValue().equals(upgradePlanDetailResp.getHasTransition())){
                        upgradePlanDetailResp.setTransitionVersionList(upgradePlanTransitionMapper.selectBydetailId(upgradePlanDetailResp.getId()));
                    }
                }
                upgradePlanResp.setUpgradePlanDetailRespList(upgradePlanDetailRespList);
                RedisCacheUtil.valueObjSet(key, upgradePlanResp);
            }
        }
        return upgradePlanResp;
    }

    @Override
    public UpgradePlanResp getUpgradePlan(Long productId) {
        String key = RedisKeyUtil.getUpgradePlanKey(productId);
        UpgradePlanResp upgradePlanResp = RedisCacheUtil.valueObjGet(key, UpgradePlanResp.class);
        if(null == upgradePlanResp){
            upgradePlanResp = upgradePlanMapper.selectByProductId(productId);
            if(!CommonUtil.isEmpty(upgradePlanResp)){
                List<UpgradePlanDetailResp> upgradePlanDetailRespList = upgradePlanDetailMapper.selectByplanId(upgradePlanResp.getId());
                for(UpgradePlanDetailResp upgradePlanDetailResp : upgradePlanDetailRespList){
                    upgradePlanDetailResp.setSubstituteVersionList(upgradePlanSubstituteMapper.selectBydetailId(upgradePlanDetailResp.getId()));
                    if(HasTransitionEnum.HAS.getValue().equals(upgradePlanDetailResp.getHasTransition())){
                        upgradePlanDetailResp.setTransitionVersionList(upgradePlanTransitionMapper.selectBydetailId(upgradePlanDetailResp.getId()));
                    }
                }
                upgradePlanResp.setUpgradePlanDetailRespList(upgradePlanDetailRespList);
                RedisCacheUtil.valueObjSet(key, upgradePlanResp);
            }
        }
        return upgradePlanResp;
    }

    @Override
    public Map<Long, UpgradePlanResp> getUpgradePlan(List<Long> productIdList) {
        Map<Long, UpgradePlanResp> upgradePlanMap = new HashMap<>();
        for(Long productId : productIdList){
            UpgradePlanResp upgradePlanResp = getUpgradePlan(productId);
            upgradePlanMap.put(productId,upgradePlanResp);
        }
        return upgradePlanMap;
    }

    /**
     * 描述：下一个版本信息查询
     * @author nongchongwei
     * @date 2018/7/24 20:00
     * @param  productId
     * @param  currentVersion
     * @return
     */
    public String getNextVersion(Long productId,String currentVersion){
        String pathKey = RedisKeyUtil.getUpgradePlanPathKey(productId);
        String nextVersion = RedisCacheUtil.hashGetString(pathKey,currentVersion);
        if(StringUtil.isBlank(nextVersion)){
            Map<String,String>  pathMap = getPathMap(productId);
            nextVersion = pathMap.get(currentVersion);
        }
        return nextVersion;
    }
    /**
     * 描述：组装升级需要的信息
     * @author nongchongwei
     * @date 2018/8/3 15:44
     * @param
     * @return
     */
    @Override
    public UpgradeInfoResp getNextUpgradeInfoResp(Long productId, String currentVersion) {
        String nextVersion = this.getNextVersion(productId,currentVersion);
        if(StringUtil.isBlank(nextVersion)){
            return null;
        }
        UpgradePlanReq upgradePlanReq = new UpgradePlanReq();
        upgradePlanReq.setProductId(productId);
        UpgradePlanResp upgradePlanResp = this.getUpgradePlan(upgradePlanReq);
        FirmwareVersion firmwareVersion = this.getFirmwareVersionByProductIdAndVersion(productId, nextVersion);
        if(CommonUtil.isEmpty(firmwareVersion) || CommonUtil.isEmpty(upgradePlanResp)){
            return null;
        }
        UpgradeInfoResp upgradeInfoResp = new UpgradeInfoResp();
        upgradeInfoResp.setNextVersion(nextVersion);

        upgradeInfoResp.setPlanStatus(upgradePlanResp.getPlanStatus());
        upgradeInfoResp.setUpgradeType(upgradePlanResp.getUpgradeType());
        upgradeInfoResp.setTargetVersion(upgradePlanResp.getTargetVersion());
        upgradeInfoResp.setProductId(productId);
        upgradeInfoResp.setId(upgradePlanResp.getId());

        upgradeInfoResp.setOtaType(firmwareVersion.getOtaType());
        upgradeInfoResp.setOtaFileId(firmwareVersion.getOtaFileId());
        upgradeInfoResp.setOtaMd5(firmwareVersion.getOtaMd5());
        upgradeInfoResp.setFwType(firmwareVersion.getFwType());
        return upgradeInfoResp;
    }

    @Override
    public void batchInsertUpgradeDeviceVersion(List<UpgradeDeviceVersion> upgradeDeviceVersionList, List<String> deviceIdList) {
        if(null != deviceIdList && deviceIdList.size() > 0){
            upgradeDeviceVersionMapper.batchDeleteUpgradeDeviceVersion(deviceIdList);
        }
        if(null != upgradeDeviceVersionList && upgradeDeviceVersionList.size() > 0){
            upgradeDeviceVersionMapper.batchInsertUpgradeDeviceVersion(upgradeDeviceVersionList);
        }
    }

    /**
     * 描述：组装版本升级路径
     * @author nongchongwei
     * @date 2018/7/24 20:00
     * @param  productId
     * @param  productId
     * @return
     */
    private Map<String,String> getPathMap(Long productId){
        String pathKey = RedisKeyUtil.getUpgradePlanPathKey(productId);
        Map<String,String> pathMap = RedisCacheUtil.hashGetAll(pathKey,String.class,false);
        if(null==pathMap || pathMap.isEmpty()){
            pathMap = new HashMap<>();
            //查询升级计划信息
            UpgradePlanReq upgradePlanReq = new UpgradePlanReq();
            upgradePlanReq.setProductId(productId);
            UpgradePlanResp upgradePlanResp = this.getUpgradePlan(upgradePlanReq);
            List<UpgradePlanDetailResp> upgradePlanDetailRespList = upgradePlanResp.getUpgradePlanDetailRespList();

            //匹配升级明细中每个每个版本的下一个版本
            String firstTransitionVersion = "";
            List<String> substituteVersionList = null;
            List<String> transitionVersionList = null;

            if(null != upgradePlanDetailRespList && upgradePlanDetailRespList.size() > 0){
                for(UpgradePlanDetailResp upgradePlanDetailResp :upgradePlanDetailRespList){
                    substituteVersionList = upgradePlanDetailResp.getSubstituteVersionList();
                    transitionVersionList = upgradePlanDetailResp.getTransitionVersionList();
                    firstTransitionVersion = upgradePlanResp.getTargetVersion();
                    if(null != transitionVersionList && transitionVersionList.size() > 0){
                        firstTransitionVersion = transitionVersionList.get(0);
                        //过度版本入库时有序的，此处transitionVersionList也是有序的
                        for(int i = 0; i < transitionVersionList.size()-1 ; i++){
                            pathMap.put(transitionVersionList.get(i),transitionVersionList.get(i+1));
                        }
                        //过渡版本的下一个版本是目标版本
                        pathMap.put(transitionVersionList.get(transitionVersionList.size()-1),upgradePlanResp.getTargetVersion());
                    }
                    if(null != substituteVersionList && substituteVersionList.size() > 0){
                        //目标版本或过渡版本的第一个版本作为替换版本的下一个版本
                        for(String substituteVersion : substituteVersionList){
                            pathMap.put(substituteVersion,firstTransitionVersion);
                        }
                    }
                }
                //存目标版本 用于判断设备是否已经升级到目标版本
                pathMap.put(upgradePlanResp.getTargetVersion(),upgradePlanResp.getTargetVersion());
            }
            RedisCacheUtil.hashPutAll(pathKey,pathMap,false);
        }

        return pathMap;
    }


    /**
     * 描述：更新计划状态
     * @author nongchongwei
     * @date 2018/10/22 15:57
     * @param
     * @return
     */
    @Override
    public int updatePlanStatus(Long id, String planStatus) {
        /**更新计划状态*/
        return upgradePlanMapper.updatePlanStatus(id,planStatus);
    }

    /**
     * 描述：查询子设备信息
     * @author nongchongwei
     * @date 2018/10/22 15:57
     * @param
     * @return
     */
    @Override
    public List<SubOtaDeviceInfo> getSubOtaDeviceInfo(Long productId, List<String> versionList) {
        return deviceMapper.getSubOtaDeviceInfo(productId,versionList);
    }

    /**
     * 描述：查询直连设备
     * @author nongchongwei
     * @date 2018/10/22 16:01
     * @param
     * @return
     */
    @Override
    public List<ForceOtaDevInfo> getDirectForceOta(Long productId, List<String> versionList) {
        return deviceMapper.getDirectForceOta(productId,versionList);
    }

    /**
     * 描述：根据产品id和版本列表查询设备信息
     * @author nongchongwei
     * @date 2018/10/22 17:35
     * @param
     * @return
     */
    @Override
    public List<OtaDeviceInfo> getOtaDeviceInfo(Long productId, List<String> versionList) {
        return deviceMapper.getOtaDeviceInfo(productId,versionList);
    }

    /**
     * 描述：更新上线时间
     * @author nongchongwei
     * @date 2018/10/22 17:48
     * @param
     * @return
     */
    @Override
    public int updateVersionOnlineTime(FirmwareVersionUpdateVersionDto dto) {
        return this.firmwareVersionMapper.updateVersionOnlineTime(dto);
    }

    /**
     * 描述：更新上线时间 用于产品发布时
     * @author nongchongwei
     * @date 2018/10/22 17:48
     * @param
     * @return
     */
    @Override
    public int updateVersionOnlineTimeNoVersion(FirmwareVersionUpdateVersionDto dto) {
        return this.firmwareVersionMapper.updateVersionOnlineTimeNoVersion(dto);
    }

    /**
     * 描述：更新上线时间 用于产品发布时
     * @author nongchongwei
     * @date 2018/10/22 17:48
     * @param
     * @return
     */
    public int updateVersionOnlineTimeByProductId(FirmwareVersionUpdateVersionDto dto){
        return this.firmwareVersionMapper.updateVersionOnlineTimeByProductId(dto);
    }

    /**
     * 描述：固件查询
     * @author nongchongwei
     * @date 2018/10/22 20:10
     * @param
     * @return
     */
    @Override
    public List<FirmwareVersionQueryResp> getAllOTAVersionInfoListByProductId(Long tenantId, Long prodId) {
        return this.firmwareVersionMapper.getAllOTAVersionInfoListByProductId(tenantId,prodId);
    }

    /**
     * 描述：固件查询
     * @author nongchongwei
     * @date 2018/10/22 20:10
     * @param
     * @return
     */
    @Override
    public List<FirmwareVersionQueryResp> getInitOTAVersionInfoListByProductId(Long tenantId, Long prodId) {
        return this.firmwareVersionMapper.selectByProductId(tenantId,prodId);
    }

    /**
     * 描述：清除缓存
     * @author nongchongwei
     * @date 2018/8/7 11:08
     * @param
     * @return
     */
    private void removeCache(Long productId,Long planId) {
        /**删除计划缓存*/
        String key = RedisKeyUtil.getUpgradePlanKey(productId);
        RedisCacheUtil.delete(key);
        /**升级计划明细路缓存删除*/
        String pathKey = RedisKeyUtil.getUpgradePlanPathKey(productId);
        RedisCacheUtil.delete(pathKey);

        String fullKey = RedisKeyUtil.getUpgradeStrategyDetailFullKey(planId);
        String batchKey = RedisKeyUtil.getUpgradeStrategyDetailBatchKey(planId);
        String uuidKey = RedisKeyUtil.getUpgradeStrategyDetailUuidKey(planId);

        RedisCacheUtil.delete(fullKey);
        RedisCacheUtil.delete(batchKey);
        RedisCacheUtil.delete(uuidKey);
    }
    /**
     * 描述：记录操作日志
     * @author nongchongwei
     * @date 2018/8/7 11:08
     * @param
     * @return
     */
    public void recordUpgradePlanLog(UpgradePlanOperateReq upgradePlanOperateReq) {
        UpgradePlanLog upgradePlanLog = new UpgradePlanLog();
        upgradePlanLog.setCreateBy(upgradePlanOperateReq.getUserId());
        upgradePlanLog.setCreateTime(new Date());
        upgradePlanLog.setOperationType(upgradePlanOperateReq.getPlanStatus());
        upgradePlanLog.setPlanId(upgradePlanOperateReq.getPlanId());
        upgradePlanLog.setTenantId(upgradePlanOperateReq.getTenantId());
        upgradePlanLogMapper.insertUpgradePlanLog(upgradePlanLog);
    }

    /**
     * 描述： 检查操作时间间隔
     * @author nongchongwei
     * @date 2018/8/7 10:26
     * @param
     * @return
     */
    private void checkSpace(UpgradePlanResp upgradePlanResp) {

        /**相隔数个小时才能操作*/
        Date updateTime = upgradePlanResp.getUpdateTime();
        String updateTimeStr = CalendarUtil.format(updateTime, CalendarUtil.YYYYMMDDHHMMSS);
        String now = CalendarUtil.getNowTime();
        long difference = 0l;
        try {
            difference = CalendarUtil.getDistanceMinute(now,updateTimeStr);
        }catch (Exception e){
            LOGGER.error("",e);
        }
        String spaceVal = RedisCacheUtil.valueGet(RedisKeyUtil.OTA_UPGRADE_INFO_SPACE);
        if(StringUtil.isBlank(spaceVal)){
            RedisCacheUtil.valueSet(RedisKeyUtil.OTA_UPGRADE_INFO_SPACE,""+ ModuleConstants.DIFFERENCE_MINUTE);
            spaceVal = ""+ ModuleConstants.DIFFERENCE_MINUTE;
        }

        if(Long.parseLong(spaceVal) > difference){
            throw new BusinessException(OtaExceptionEnum.OPERATE_PLAN_TOO_FREQUENCY);
        }
    }

    /**
     * 描述：获取一个计划下的所有的版本
     * @author nongchongwei
     * @date 2018/7/24 20:00
     * @param  productId
     * @return
     */
    public List<String> getVersionByProductId(Long productId) {
        UpgradePlanReq upgradePlanReq = new UpgradePlanReq();
        upgradePlanReq.setProductId(productId);
        UpgradePlanResp upgradePlanResp = this.getUpgradePlan(upgradePlanReq);
        List<UpgradePlanDetailResp> upgradePlanDetailRespList = upgradePlanResp.getUpgradePlanDetailRespList();
        List<String> versionList = new ArrayList<>();
        List<String> substituteVersionList = null;
        List<String> transitionVersionList = null;
        if(null != upgradePlanDetailRespList && upgradePlanDetailRespList.size() > 0){
            for(UpgradePlanDetailResp upgradePlanDetailResp :upgradePlanDetailRespList){
                substituteVersionList = upgradePlanDetailResp.getSubstituteVersionList();
                transitionVersionList = upgradePlanDetailResp.getTransitionVersionList();
                if(null != substituteVersionList && substituteVersionList.size() > 0){
                    versionList.addAll(substituteVersionList);
                }
               if(null != transitionVersionList && transitionVersionList.size() > 0){
                   versionList.addAll(transitionVersionList);
               }
            }
        }
        return versionList;
    }

    @Override
    public String getOtaDeviceVersion(String deviceId) {
        return deviceMapper.getOtaDeviceVersion(deviceId);
    }

    @Override
    public Map<String,String> getFirmwareUrl(Long productId, String version) {
        Map<String,String> result = new HashMap<>();
        FirmwareVersion firmwareVersion = this.getFirmwareVersionByProductIdAndVersion(productId, version);
        if(null != firmwareVersion){
            result.put("md5",firmwareVersion.getOtaMd5());
            //中控微服务间调用，在这里获取url
            FileDto fileDto = fileApi.getGetUrl(firmwareVersion.getOtaFileId());
            if(null == fileDto || StringUtil.isBlank(fileDto.getPresignedUrl())) {
                result.put("url","");
            }else {
                result.put("url",fileDto.getPresignedUrl());
            }
        }
        return result;
    }

    /**
     * 描述：查询版本百分比
     * @author nongchongwei
     * @date 2018/11/15 11:11
     * @param
     * @return
     */
    @Override
    public Map<String, String> getVersionPercent(VersionPercentReq versionPercentReq) {
        Map<String, String> result = new HashMap<>();
        /**查询该产品id现有设备总数*/
        Integer total = this.deviceMapper.getTotalByProductId(versionPercentReq.getTenantId(), versionPercentReq.getProductId());
        /**统计各个版本现有设备总数*/
        FirmwareVersionSearchReqDto req = new FirmwareVersionSearchReqDto();
        req.setOtaVersion(new HashSet<>(versionPercentReq.getVersionList()));
        req.setProductId(versionPercentReq.getProductId());
        req.setTenantId(versionPercentReq.getTenantId());
        List<FirmwareVersionPageResp> list = this.firmwareVersionMapper.getOTAVersionPageByProductId(req);
        if(null != list && list.size() > 0){
            list.forEach(t -> {
                result.put(t.getOtaVersion(), NumberUtil.calcPercentage((double) t.getVersionQuantity(), (double) total, "0.00"));
            });
        }

        return result;
    }
    /**
     * 描述：保存策略配置
     * @author nongchongwei
     * @date 2018/11/15 11:11
     * @param
     * @return
     */
    @Override
    public int saveStrategyConfig(StrategyConfigDto strategyConfigDto) {
        Long planId = strategyConfigDto.getPlanId();
        Long tenantId = strategyConfigDto.getTenantId();
        String key = RedisKeyUtil.getUpgradeStrategyConfigKey(planId);
        RedisCacheUtil.delete(key);

        List<StrategyConfig> strategyConfigOldList = this.strategyConfigMapper.selectByPlanId(planId,tenantId);
        if(null != strategyConfigOldList && !strategyConfigOldList.isEmpty()){
            for(StrategyConfig strategyConfig : strategyConfigOldList){
                String successKey = RedisKeyUtil.getUpgradeStrategyGroupSuccessKey(planId,strategyConfig.getStrategyGroup());
                String failKey = RedisKeyUtil.getUpgradeStrategyGroupFailKey(planId,strategyConfig.getStrategyGroup());
                RedisCacheUtil.delete(successKey);
                RedisCacheUtil.delete(failKey);
            }
        }
        this.strategyConfigMapper.deleteByPlanId(planId,tenantId);
        int count = 0;
        List<StrategyConfigReq> StrategyConfigReqList = strategyConfigDto.getStrategyConfigList();
        StrategyConfig strategyConfig = null;
        if(null != StrategyConfigReqList && StrategyConfigReqList.size() != 0){
            for(StrategyConfigReq strategyConfigReq : StrategyConfigReqList){
                strategyConfig = new StrategyConfig();
                BeanUtils.copyProperties(strategyConfigReq, strategyConfig);
                count +=  this.strategyConfigMapper.insert(strategyConfig);
            }
        }
        return count;
    }
    /**
     * 功能描述:保存策略报告
     * @param: [strategyReport]
     * @return: int
     * @auther: nongchongwei
     * @date: 2018/11/26 16:32
     */
    @Override
    public void updateStrategyReport(String deviceId,String upgradeResult,String reason) {
        LOGGER.info("***************deviceId->{},upgradeResult->{}******************",deviceId,upgradeResult);
        String key = RedisKeyUtil.OTA_UPGRADE_INFO_DETAIL+deviceId;
        try {
            UpgradeLogAddReq req = RedisCacheUtil.valueObjGet(key, UpgradeLogAddReq.class);
            if(req == null){
                LOGGER.info("updateStrategyReport UpgradeLogAddReq is null deviceId->{}",deviceId);
                return;
            }
            LOGGER.info("***************deviceId->{},time->{}******************",deviceId,System.currentTimeMillis());
            StrategyReport strategyReport = new StrategyReport();
            List<StrategyReportResp> strategyReportList =  strategyReportMapper.selectStrategyReportByDevId(deviceId);
            if(null != strategyReportList && strategyReportList.size() > 0){
                for(StrategyReportResp strategyReportResp : strategyReportList){
                    strategyReportMapper.deleteByPrimaryKey(strategyReportResp.getId());
                }
            }
            BeanUtils.copyProperties(req, strategyReport);
            strategyReport.setCompleteTime(new Date());
            strategyReport.setDeviceUuid(deviceId);
            strategyReport.setUpgradeResult(upgradeResult);
            strategyReport.setReason(reason);
            strategyReportMapper.insert(strategyReport);
            LOGGER.info("***************req.toString()->{}******************",req.toString());
        } catch (Exception e) {
            LOGGER.error("add upgrade log failed: ", e);
        }
    }

    @Override
    public List<String> selectFailUpgradeVersion(Long planId) {
        return strategyReportMapper.selectFailUpgradeVersion(planId);
    }

    @Override
    public int deleteStrategyReportByPlanId(Long planId) {
        return strategyReportMapper.deleteByPlanId(planId);
    }

    @Override
    public List<StrategyReportGroupVo> selectStrategyReportByGroup(Long productId) {
        List<StrategyReportGroupVo> strategyReportGroupVoList = new ArrayList<>();
        UpgradePlanResp upgradePlanResp = this.getUpgradePlan(productId);
        Long planId = upgradePlanResp.getId();
        Long tenantId = upgradePlanResp.getTenantId();
        Integer upgradeScope = upgradePlanResp.getUpgradeScope();
        List<StrategyConfigResp> strategyConfigRespList = this.getStrategyConfig(planId, tenantId);
        if(null != strategyConfigRespList && !strategyConfigRespList.isEmpty()){
            for(StrategyConfigResp strategyConfigResp : strategyConfigRespList){
                List<StrategyReportResp> strategyReportRespList = strategyReportMapper.selectByGroup(planId,strategyConfigResp.getStrategyGroup());
                if(null != strategyReportRespList && !strategyReportRespList.isEmpty()){
                    StrategyReportGroupVo strategyReportGroupVo = new StrategyReportGroupVo();
                    strategyReportGroupVo.setThreshold(strategyConfigResp.getThreshold());
                    strategyReportGroupVo.setUpgradeTotal(strategyConfigResp.getUpgradeTotal());
                    strategyReportGroupVo.setUpgradeScope(upgradeScope);
                    strategyReportGroupVo.setStrategyGroup(strategyConfigResp.getStrategyGroup());
                    Integer success = 0;
                    Integer fail = 0;
                    for(StrategyReportResp strategyReportResp : strategyReportRespList){
                        if(UpgradeResultEnum.Success.getValue().equals(strategyReportResp.getUpgradeResult())){
                            success++;
                        }else {
                            fail++;
                        }
                    }
                    strategyReportGroupVo.setSuccess(success);
                    strategyReportGroupVo.setFail(fail);
                    if(fail>strategyConfigResp.getThreshold() || fail.equals(strategyConfigResp.getThreshold())){
                        strategyReportGroupVo.setStrategyStatus(UpgradeResultEnum.Failed.getValue());
                    }else {
                        strategyReportGroupVo.setStrategyStatus(UpgradeResultEnum.Success.getValue());
                    }
                    strategyReportGroupVo.setAllCompleteTime(strategyReportRespList.get(strategyReportRespList.size()-1).getCompleteTime());
                    strategyReportGroupVo.setPlanId(planId);
                    strategyReportGroupVoList.add(strategyReportGroupVo);
                }

            }
        }
        return strategyReportGroupVoList;
    }

    @Override
    public Page<StrategyReportResp> selectStrategyReportByGroupAsPage(StrategyReportSearchReqDto dto) {
        com.baomidou.mybatisplus.plugins.Page<StrategyReportResp> page =new com.baomidou.mybatisplus.plugins.Page<StrategyReportResp>(CommonUtil.getPageNum(dto), CommonUtil.getPageSize(dto));
        List<StrategyReportResp> strategyReportRespList = null;
        try {
            strategyReportRespList = this.strategyReportMapper.selectByGroupAsPage(page,dto.getPlanId(),dto.getStrategyGroup());

        }catch (Exception e){
            LOGGER.error("selectStrategyReportByGroupAsPage error", e);
            throw new BusinessException(OtaExceptionEnum.QUERY_ERROR, e);
        }
        page.setRecords(strategyReportRespList);
        com.iot.common.helper.Page<StrategyReportResp> myPage=new com.iot.common.helper.Page<StrategyReportResp>();
        BeanCopyUtils.copyMybatisPlusPageToPage(page,myPage);
        return myPage;
    }

    /**
     * 描述：查询策略配置
     * @author nongchongwei
     * @date 2018/11/15 11:10
     * @param
     * @return
     */
    @Override
    public List<StrategyConfigResp> getStrategyConfig(Long planId, Long tenantId) {
        String key = RedisKeyUtil.getUpgradeStrategyConfigKey(planId);
        List<StrategyConfigResp> strategyConfigRespList = RedisCacheUtil.listGetAll(key,StrategyConfigResp.class);
        if(null == strategyConfigRespList || strategyConfigRespList.size() == 0){
            strategyConfigRespList = new ArrayList<>();
            StrategyConfigResp strategyConfigResp = null;
            List<StrategyConfig> strategyConfigList = this.strategyConfigMapper.selectByPlanId(planId,tenantId);
            if(null != strategyConfigList && strategyConfigList.size() > 0){
                for(StrategyConfig strategyConfig : strategyConfigList){
                    strategyConfigResp = new StrategyConfigResp();
                    BeanUtils.copyProperties(strategyConfig, strategyConfigResp);
                    strategyConfigRespList.add(strategyConfigResp);
                }
                RedisCacheUtil.listSet(key,strategyConfigRespList);
            }
        }
        return strategyConfigRespList;
    }

    /**
     * 描述：保存策略明细
     * @author nongchongwei
     * @date 2018/11/16 16:12
     * @param
     * @return
     */
    @Override
    public void saveStrategyDetail(StrategyDetailDto strategyDetailDto) {
        Long planId = null;
        if(UpgradeScopeEnum.FULL.getValue().equals(strategyDetailDto.getUpgradeScope())){
            Map<Integer, List<StrategyDetailReq>> strategyDetailReqMap =  strategyDetailDto.getStrategyDetailReqMap();
            Map<String,List<String>> strategyDetailMap = new HashMap<>();
            for (Map.Entry<Integer, List<StrategyDetailReq>> entry : strategyDetailReqMap.entrySet()) {
                if(null != entry.getValue() && entry.getValue().size() > 0 && null != entry.getValue().get(0) && null != entry.getValue().get(0).getPlanId()){
                    planId = entry.getValue().get(0).getPlanId();
                    strategyDetailMapper.deleteByPlanId(planId);
                    break;
                }
            }
            for (Map.Entry<Integer, List<StrategyDetailReq>> entry : strategyDetailReqMap.entrySet()) {
                List<StrategyDetailReq> strategyDetailReqList = entry.getValue();
                Integer group = entry.getKey();
                List<String> devIdList = new ArrayList<>();
                for(StrategyDetailReq strategyDetail : strategyDetailReqList){
                    devIdList.add(strategyDetail.getDeviceUuid());
                    if(null == planId){
                        planId = strategyDetail.getPlanId();
                    }
                    this.strategyDetailMapper.insert(strategyDetail);
                }
                strategyDetailMap.put(group+"",devIdList);
            }
        }else if(UpgradeScopeEnum.BATCH.getValue().equals(strategyDetailDto.getUpgradeScope())
                || UpgradeScopeEnum.UUID.getValue().equals(strategyDetailDto.getUpgradeScope())){
            List<StrategyDetailReq> strategyDetailReqList = strategyDetailDto.getStrategyDetailReqList();
            StrategyDetailReq strategyDetailReq = strategyDetailReqList.get(0);
            planId = strategyDetailReq.getPlanId();
            strategyDetailMapper.deleteByPlanId(planId);
            strategyDetailReqList.forEach(
                    strategyDetail->{
                         this.strategyDetailMapper.insert(strategyDetail);
                    }
            );
        }
        String fullKey = RedisKeyUtil.getUpgradeStrategyDetailFullKey(planId);
        String uuidKey = RedisKeyUtil.getUpgradeStrategyDetailUuidKey(planId);
        String batchKey = RedisKeyUtil.getUpgradeStrategyDetailBatchKey(planId);
        RedisCacheUtil.delete(fullKey);
        RedisCacheUtil.delete(uuidKey);
        RedisCacheUtil.delete(batchKey);
    }
    /**
     * 描述： 获取策略明细  uuid
     * @author nongchongwei
     * @date 2018/11/16 16:11
     * @param
     * @return
     */
    @Override
    public Set<String> getStrategyDetail(Long planId) {
        String key = RedisKeyUtil.getUpgradeStrategyDetailUuidKey(planId);
        Set<String> devIdSet = RedisCacheUtil.setGetAll(key,String.class,false);
        if(null == devIdSet || devIdSet.size() == 0){
            List<String> devIdList = this.strategyDetailMapper.selectUuidByPlanId(planId);
            if(null != devIdList){
                devIdSet = new HashSet<>(devIdList);
                RedisCacheUtil.setAdd(key,devIdSet,false);
            }
        }
        return devIdSet;
    }

    @Override
    public List<Long> selectBatchByPlanId(Long planId) {
        return this.strategyDetailMapper.selectBatchByPlanId(planId);
    }

    /**
     * 描述： 获取策略明细  batch
     * @author nongchongwei
     * @date 2018/11/16 16:11
     * @param
     * @return
     */
    @Override
    public Set<String> getStrategyDetailWithBatchNum(Long planId) {
        String key = RedisKeyUtil.getUpgradeStrategyDetailBatchKey(planId);
        Set<String> devIdSet = RedisCacheUtil.setGetAll(key,String.class,false);
        if(null == devIdSet || devIdSet.size() == 0){
            List<String> devIdList = this.strategyDetailMapper.selectUuidByBatchNumAndPlanId(planId);
            if(null != devIdList){
                devIdSet = new HashSet<>(devIdList);
                RedisCacheUtil.setAdd(key,devIdSet,false);
            }
        }
        return devIdSet;
    }
    /**
     * 描述：获取策略明细  full
     * @author nongchongwei
     * @date 2018/11/16 16:11
     * @param
     * @return
     */
    @Override
    public List<String> getStrategyDetailWithGroup(Long planId, Integer group) {
        String taskKey = RedisKeyUtil.getUpgradeStrategyDetailFullKey(planId);
        List<String> devIdList =  RedisCacheUtil.hashGetArray(taskKey, group+"", String.class);
        if(null == devIdList || devIdList.isEmpty()){
            devIdList = this.strategyDetailMapper.selectUuidByPlanIdAndGroup(planId,group);
            Map<String,List<String>> devIdMap = new HashMap<>();
            devIdMap.put(group+"",devIdList);
            RedisCacheUtil.hashPutBatch(taskKey, devIdMap,null,true);
        }
        return devIdList;
    }

    @Override
    public List<String> getStrategyDetailWithPlanId(Long planId) {
        String taskKey = RedisKeyUtil.getUpgradeStrategyDetailFullKey(planId);
        Map<String, List<String>> detailMap = RedisCacheUtil.hashGetAllArray(taskKey,String.class);
        List<String> devIdList = new ArrayList<>();
        if (null == detailMap || detailMap.isEmpty()){
            devIdList = this.strategyDetailMapper.selectStrategyUuidByPlanId(planId);
        }else {
            for (Map.Entry<String,List<String>> entry : detailMap.entrySet()) {
                List<String> devListTemp = entry.getValue();
                if(null != devListTemp && !devListTemp.isEmpty()){
                    for(String devId : devListTemp){
                        if(!devIdList.contains(devId)){
                            devIdList.add(devId);
                        }
                    }
                }
            }
        }
        return devIdList;
    }

    /**
     * 描述：删除策略明细
     * @author nongchongwei
     * @date 2018/11/16 16:10
     * @param
     * @return
     */
    @Override
    public int delStrategyDetail(Long planId) {
        return strategyDetailMapper.deleteByPlanId(planId);
    }

    /**
     * 描述：编辑计划
     * @author nongchongwei
     * @date 2018/7/24 20:00
     * @param  upgradePlanEditReq
     * @return
     */
    @Override
    public int editUpgradePlan(UpgradePlanEditReq upgradePlanEditReq) {
        if(CollectionUtils.isEmpty(upgradePlanEditReq.getUpgradePlanDetailEditReqList())
                || CollectionUtils.isEmpty(upgradePlanEditReq.getUpgradePlanDetailEditReqList().get(0).getSubstituteVersionList())
                || StringUtil.isBlank(upgradePlanEditReq.getTargetVersion() ) || CommonUtil.isEmpty(upgradePlanEditReq.getStrategySwitch())
                || CommonUtil.isEmpty(upgradePlanEditReq.getUpgradeScope())
                ){
            throw new BusinessException(OtaExceptionEnum.PARAM_IS_ERROR, "substitute or transition is null");
        }
        /**策略开关**/
        Integer strategySwitch = upgradePlanEditReq.getStrategySwitch();
        /**升级范围**/
        Integer upgradeScope = upgradePlanEditReq.getUpgradeScope();
        if(StrategySwitchEnum.USE.getValue().equals(strategySwitch)&& (UpgradeScopeEnum.UUID.getValue().equals(upgradeScope)
                || UpgradeScopeEnum.BATCH.getValue().equals(upgradeScope))){
            LOGGER.info("####ota#### editUpgradePlan config error,strategySwitch->{},upgradeScope->",strategySwitch,upgradeScope);
            throw new BusinessException(OtaExceptionEnum.CONFIG_ERROR);
        }
        UpgradePlanResp upgradePlanResp = upgradePlanMapper.selectByProductId(upgradePlanEditReq.getProductId());
        if(null == upgradePlanResp || null == upgradePlanResp.getProductId() ||
                !upgradePlanEditReq.getId().equals(upgradePlanResp.getId())){
            throw new BusinessException(OtaExceptionEnum.PARAM_IS_ERROR, "There is no corresponding information");
        }
        if(PlanStatusEnum.Start.getValue().equals(upgradePlanResp.getPlanStatus())){
            throw new BusinessException(OtaExceptionEnum.START_STATE_CAN_NOT_EDITED);
        }
        if(0 < upgradePlanResp.getEditedTimes()){
            checkSpace(upgradePlanResp);
        }
        //更新计划
        upgradePlanEditReq.setUpdateTime(new Date());
        if(null == upgradePlanResp.getEditedTimes() || 0 == upgradePlanResp.getEditedTimes()){
            upgradePlanEditReq.setEditedTimes(1);
        }else {
            upgradePlanEditReq.setEditedTimes(1+upgradePlanResp.getEditedTimes());
        }
        int count = upgradePlanMapper.updateByPrimaryKey(upgradePlanEditReq);
        /**处理策略明细*/
        handleStrategyDetail(upgradeScope,upgradePlanEditReq);
        /**处理计划明细*/
        handlePlanDetail(upgradePlanEditReq);
        /**删除缓存*/
        removeCache(upgradePlanEditReq.getProductId(),upgradePlanEditReq.getId());
        /**记录计划操作日志*/
        recordUpgradePlanLog(upgradePlanEditReq);
        return count;
    }
    /**
     * 描述：处理策略明细
     * @author nongchongwei
     * @date 2018/7/24 20:00
     * @param  upgradePlanEditReq
     * @return
     */
    private void handleStrategyDetail(Integer upgradeScope, UpgradePlanEditReq upgradePlanEditReq) {
        strategyDetailMapper.deleteByPlanId(upgradePlanEditReq.getId());
        if(UpgradeScopeEnum.UUID.getValue().equals(upgradeScope)){
            List<String> devIdList =  upgradePlanEditReq.getDeviceIdEditReqList();
            if(null != devIdList && !devIdList.isEmpty()){
                for(String devId : devIdList){
                    StrategyDetailReq record = new StrategyDetailReq();
                    record.setDeviceUuid(devId);
                    record.setPlanId(upgradePlanEditReq.getId());
                    strategyDetailMapper.insert(record);
                }
            }
        }else if(UpgradeScopeEnum.BATCH.getValue().equals(upgradeScope)){
            List<Long> batchNumList =  upgradePlanEditReq.getBatchNumEditReqList();
            if(null != batchNumList && !batchNumList.isEmpty()){
                for(Long batchNum : batchNumList){
                    StrategyDetailReq record = new StrategyDetailReq();
                    record.setBatchNum(batchNum);
                    record.setPlanId(upgradePlanEditReq.getId());
                    strategyDetailMapper.insert(record);
                }
            }
        }
    }

    /**
     * 描述：处理计划明细
     * @author nongchongwei
     * @date 2018/7/24 20:00
     * @param  upgradePlanEditReq
     * @return
     */
    private void handlePlanDetail(UpgradePlanEditReq upgradePlanEditReq) {
        List<Long> detailIdList = upgradePlanDetailMapper.getDetailIdByPlanId(upgradePlanEditReq.getId());
        //删除明细 删除替换版本 删除过度版本
        upgradePlanDetailMapper.deleteByPlanId(upgradePlanEditReq.getId());
        if(null != detailIdList && detailIdList.size() > 0){
            upgradePlanSubstituteMapper.deleteByDetailIds(detailIdList);
            upgradePlanTransitionMapper.deleteByDetailIds(detailIdList);
        }
        List<UpgradePlanDetailEditReq> upgradePlanDetailEditReqList =  upgradePlanEditReq.getUpgradePlanDetailEditReqList();
        if(null != upgradePlanDetailEditReqList && upgradePlanDetailEditReqList.size() > 0){
            for(UpgradePlanDetailEditReq upgradePlanDetailEditReq : upgradePlanDetailEditReqList){
                upgradePlanDetailEditReq.setPlanId(upgradePlanEditReq.getId());
                if(null != upgradePlanDetailEditReq.getTransitionVersionList()
                        && upgradePlanDetailEditReq.getTransitionVersionList().size() > 0){
                    upgradePlanDetailEditReq.setHasTransition(HasTransitionEnum.HAS.getValue());
                }else {
                    upgradePlanDetailEditReq.setHasTransition(HasTransitionEnum.NO.getValue());
                }
            }
            //插入明细
            upgradePlanDetailMapper.batchInsertUpgradePlanDetail(upgradePlanDetailEditReqList);

            List<UpgradePlanSubstitute> upgradePlanSubstituteList = new ArrayList<>();
            UpgradePlanSubstitute upgradePlanSubstitute = null;

            List<UpgradePlanTransition> upgradePlanTransitionList = new ArrayList<>();
            UpgradePlanTransition upgradePlanTransition = null;

            for(UpgradePlanDetailEditReq upgradePlanDetailEditReq : upgradePlanDetailEditReqList){

                List<String> substituteVersionList = upgradePlanDetailEditReq.getSubstituteVersionList();
                if(null != substituteVersionList && substituteVersionList.size() > 0){
                    for(String substituteVersion : substituteVersionList){
                        upgradePlanSubstitute = new UpgradePlanSubstitute();
                        upgradePlanSubstitute.setDetailId(upgradePlanDetailEditReq.getId());
                        upgradePlanSubstitute.setSubstituteVersion(substituteVersion);
                        upgradePlanSubstituteList.add(upgradePlanSubstitute);
                    }
                }
                if(HasTransitionEnum.HAS.getValue().equals(upgradePlanDetailEditReq.getHasTransition())){
                    List<String> transitionVersionList = upgradePlanDetailEditReq.getTransitionVersionList();
                    if(null != transitionVersionList && transitionVersionList.size() > 0){
                        for(String transitionVersion : transitionVersionList){
                            upgradePlanTransition = new UpgradePlanTransition();
                            upgradePlanTransition.setDetailId(upgradePlanDetailEditReq.getId());
                            upgradePlanTransition.setTransitionVersion(transitionVersion);
                            upgradePlanTransitionList.add(upgradePlanTransition);
                        }
                    }
                }

            }

            //插入替换版本  插入过度版本
            if(null != upgradePlanSubstituteList && upgradePlanSubstituteList.size() > 0){
                upgradePlanSubstituteMapper.batchInsertUpgradePlanSubstitute(upgradePlanSubstituteList);
            }
            if(null != upgradePlanTransitionList && upgradePlanTransitionList.size() > 0){
                upgradePlanTransitionMapper.batchInsertUpgradePlanTransition(upgradePlanTransitionList);
            }

        }
    }
    /**
     * 描述：记录计划操作日志
     * @author nongchongwei
     * @date 2018/7/24 20:00
     * @param  upgradePlanEditReq
     * @return
     */
    private void recordUpgradePlanLog(UpgradePlanEditReq upgradePlanEditReq) {
        UpgradePlanLog upgradePlanLog = new UpgradePlanLog();
        upgradePlanLog.setCreateBy(upgradePlanEditReq.getUserId());
        upgradePlanLog.setCreateTime(new Date());
        upgradePlanLog.setOperationType("Edit");
        upgradePlanLog.setPlanId(upgradePlanEditReq.getId());
        upgradePlanLog.setTenantId(upgradePlanEditReq.getTenantId());
        upgradePlanLogMapper.insertUpgradePlanLog(upgradePlanLog);
    }

    /**
     * 描述：获取设备强制升级设备列表
     * @author maochengyuan
     * @created 2018/8/7 16:34
     * @param productId 产品id
     * @param sta 开始下标
     * @param bean 返回实体类
     * @return java.util.List<T>
     */
    @Override
    public <T> List<T> getOtaListFromCache(Long productId, int sta, Class<T> bean){
        String key = RedisKeyUtil.OTA_UPGRADE_PLAN_TASK_KEY+productId;
        Set<String> keys = CommonUtil.getStrKeys(sta, ModuleConstants.OTA_LIST_SIZE);
        return RedisCacheUtil.hashMultiGetArray(key, keys, bean);
    }
    /**
     * 描述：获取设策略升级设备列表
     * @author nongchongwei
     * @date 2018/11/19 16:05
     * @param
     * @return
     */
    @Override
    public <T> List<T> getStrategyOtaListFromCache(Long planId, Integer group, int sta, Class<T> bean) {
        String key = RedisKeyUtil.OTA_UPGRADE_STRATEGY_TASK+planId + ":" + group;
        Set<String> keys = CommonUtil.getStrKeys(sta, ModuleConstants.OTA_LIST_SIZE);
        return RedisCacheUtil.hashMultiGetArray(key, keys, bean);
    }

    /**
     * 描述：缓存升级信息
     * @author maochengyuan
     * @created 2018/8/9 10:35
     * @param req 版本信息
     * @return void
     */
    @Override
    public void cacheUpgradeLog(UpgradeLogAddReq req) {
        try {
            String key = RedisKeyUtil.OTA_UPGRADE_INFO_DETAIL+req.getDeviceUuid();
            RedisCacheUtil.valueSet(key, JsonUtil.toJson(req), ModuleConstants.OTA_UPGRADE_INFO_DETAIL_TIME);
        } catch (Exception e) {
            LOGGER.error("cache upgrade log failed: ", e);
        }
    }

    /**
     * 描述：更新升级结果，并清空缓存
     * @author maochengyuan
     * @created 2018/8/9 10:36
     * @param deviceId 设备id
     * @param upgradeResult 升级结果
     * @return void
     */
    @Override
    public void updateUpgradeLog(String deviceId, String upgradeResult,String upgradeMsg) {
        LOGGER.info("***************deviceId->{},upgradeResult->{}******************",deviceId,upgradeResult);
        String key = RedisKeyUtil.OTA_UPGRADE_INFO_DETAIL+deviceId;
        try {
            UpgradeLogAddReq req = RedisCacheUtil.valueObjGetAndSet(key, UpgradeLogAddReq.class,"");
            if(req == null){
                LOGGER.error("####ota####  upgradeLogAddReq is null devId-{},upgradeResult->{},upgradeMsg->{}",deviceId,upgradeResult,upgradeMsg);
                return;
            }
            LOGGER.info("***************deviceId->{},time->{}******************",deviceId,System.currentTimeMillis());
            UpgradeLog log = new UpgradeLog(upgradeResult, new Date(),upgradeMsg);
            BeanUtils.copyProperties(req, log);
            this.upgradeLogMapper.addUpgradeLog(log);
            LOGGER.info("***************req.toString()->{}******************",req.toString());
        } catch (Exception e) {
            LOGGER.error("add upgrade log failed: ", e);
        }finally {
            RedisCacheUtil.delete(key);
        }
    }

    /** 
     * 描述：获取计划中的版本
     * @author maochengyuan
     * @created 2018/7/30 14:36
     * @param req 请求参数
     * @return java.util.Set<java.lang.String>
     */
    private Set<String> getCurrVersionListDetail(FirmwareVersionSearchReqDto req){
        UpgradePlanResp resp = this.getUpgradePlan(new UpgradePlanReq(req.getTenantId(), req.getUserId(), req.getProductId()));
        if(resp == null || CommonUtil.isEmpty(resp.getUpgradePlanDetailRespList())){
               return Collections.emptySet();
        }
        Set<String> set = new HashSet<>();
        resp.getUpgradePlanDetailRespList().forEach(o ->{
            if(o.getSubstituteVersionList() != null){
                set.addAll(o.getSubstituteVersionList());
            }
            if(o.getTransitionVersionList() != null){
                set.addAll(o.getTransitionVersionList());
            }
        });
        return set;
    }

	@Override
	public Page<OtaFileInfoResp> getOtaFileList(OtaPageReq pageReq) {
		Page<OtaFileInfoResp> pageResult = new Page<OtaFileInfoResp>();
        Pagination pagination = new Pagination(pageReq.getPageNum(), pageReq.getPageSize());
        List<OtaFileInfoResp> otaFileInfoResps = deviceMapper.getOtaFileList(pagination,pageReq);
        pageResult.setPageNum(pageReq.getPageNum());
        pageResult.setPageSize(pageReq.getPageSize());
        pageResult.setTotal(pagination.getTotal());
        pageResult.setPages(pagination.getPages());
        pageResult.setResult(otaFileInfoResps);
		return pageResult;
	}

    @Override
    public int deleteByFirmwareId(Long id) {
       FirmwareVersion firmwareVersion = firmwareVersionMapper.selectByPrimaryKey(id);
       int ret = firmwareVersionMapper.deleteByFirmwareId(id);
       if(null != firmwareVersion && StringUtil.isNotBlank(firmwareVersion.getOtaFileId())){
          fileApi.deleteObject(firmwareVersion.getOtaFileId());
        }
        return ret;
    }

    @Override
	public int saveOtaFileInfo(OtaFileInfoReq otaFileInfoReq) {
		return deviceMapper.saveOtaFileInfo(otaFileInfoReq);
	}
	
	@Override
	public int updateOtaFileInfo(OtaFileInfoReq otaFileInfoReq) {
		return deviceMapper.updateOtaFileInfo(otaFileInfoReq);
	}

	@Override
	public OtaFileInfoResp findOtaFileInfoByProductId(OtaFileInfoReq otaFileInfoReq) {
		return deviceMapper.findOtaFileInfoByProductId(otaFileInfoReq);
	}
	
}
