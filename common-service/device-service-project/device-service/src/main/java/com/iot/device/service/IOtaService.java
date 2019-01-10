package com.iot.device.service;

import com.iot.common.helper.Page;
import com.iot.device.model.ota.*;
import com.iot.device.vo.req.OtaPageReq;
import com.iot.device.vo.req.ota.*;
import com.iot.device.vo.req.ota.FirmwareVersionDto;
import com.iot.device.vo.rsp.ota.*;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 项目名称：立达信IOT云平台
 * 模块名称：ota
 * 功能描述：ota
 * 创建人： nongchongwei
 * 创建时间：2018年07月24日 16:58
 * 修改人： nongchongwei
 * 修改时间：2018年07月24日 16:58
 */
public interface IOtaService {
    /**
     * 描述：查询升级日志
     * @author nongchongwei
     * @date 2018/7/24 20:00
     * @param  upgradeLogReq
     * @return
     */
    Page<UpgradeLogResp> getUpgradeLog(UpgradeLogReq upgradeLogReq);

    /**
     * 描述：查询OTA版本合法性（唯一性）
     * @author maochengyuan
     * @created 2018/7/25 11:17
     * @param tenantId 租户id
     * @param prodId 产品id
     * @param otaVersion ota版本
     * @return com.iot.device.vo.rsp.ota.CheckVersionResp
     */
    Boolean checkVersionLegality(Long tenantId, Long prodId, String otaVersion);

    /**
     * 描述：依据产品ID查询升级版本列表
     * @author maochengyuan
     * @created 2018/7/25 11:17
     * @param dto ota版本查询参数
     * @return com.iot.common.helper.Page<com.iot.device.vo.rsp.ota.FirmwareVersionResp>
     */
    Page<FirmwareVersionResp> getFirmwareVersionListByProductId(FirmwareVersionSearchReqDto dto);

    /**
     * 描述：新增OTA升级版本信息
     * @author maochengyuan
     * @created 2018/7/25 11:17
     * @param dto 版本对象
     * @return int
     */
    int createFirmwareVersion(FirmwareVersionReqDto dto);

    /**
     * 描述：新增OTA升级版本信息
     * @author nongchongwei
     * @created 2018/7/25 11:17
     * @param dto 版本对象
     * @return int
     */
    void initFirmwareVersion(FirmwareVersionDto dto);
    /**
     * 描述：计划操作记录分页查询
     * @author nongchongwei
     * @date 2018/7/24 20:00
     * @param  upgradePlanReq
     * @return
     */
    Page<UpgradePlanLogResp> getUpgradePlanLog(UpgradePlanReq upgradePlanReq);

    /**
     * 描述：指定版本信息查询
     * @author nongchongwei
     * @date 2018/7/24 20:00
     * @param  productId
     * @param  otaVersion
     * @return
     */
    FirmwareVersion getFirmwareVersionByProductIdAndVersion(Long productId, String otaVersion);

    /**
     * 描述：升级计划查询
     * @author nongchongwei
     * @date 2018/7/24 20:00
     * @param  upgradePlanReq
     * @return
     */
    UpgradePlanResp getUpgradePlan(UpgradePlanReq upgradePlanReq);

    /**
     * 描述：升级计划查询
     * @author nongchongwei
     * @date 2018/7/24 20:00
     * @param  productId
     * @return
     */
    UpgradePlanResp getUpgradePlan(Long productId);

    /**
     * 描述：升级计划查询
     * @author nongchongwei
     * @date 2018/7/24 20:00
     * @param  productIdList
     * @return
     */
    Map<Long,UpgradePlanResp> getUpgradePlan(List<Long> productIdList);

    /**  
     * 描述：修改计划状态 编辑、启动、暂停
     * @author nongchongwei  
     * @date 2018/10/22 15:07  
     * @param   
     * @return   
     */ 
    int updatePlanStatus( Long id, String planStatus);

    /**
     * 描述：查询子设备
     * @author nongchongwei
     * @date 2018/10/22 15:34
     * @param
     * @return
     */
    List<SubOtaDeviceInfo> getSubOtaDeviceInfo(Long productId, List<String> versionList );

    /**
     * 描述：查询直连设备
     * @author nongchongwei
     * @date 2018/10/22 15:59
     * @param
     * @return
     */
    List<ForceOtaDevInfo> getDirectForceOta(Long productId, List<String> versionList );

    /**
     * 描述：根据产品id和版本列表查询设备信息
     * @author nongchongwei
     * @date 2018/10/22 17:35
     * @param
     * @return
     */
    List<OtaDeviceInfo> getOtaDeviceInfo( Long productId, List<String> versionList);

    /**
     * 描述：更新上线时间
     * @author nongchongwei
     * @date 2018/10/22 17:48
     * @param
     * @return
     */
    int updateVersionOnlineTime(FirmwareVersionUpdateVersionDto dto);

    /**
     * 描述：更新上线时间
     * @author nongchongwei
     * @date 2018/10/22 17:48
     * @param
     * @return
     */
    int updateVersionOnlineTimeNoVersion(FirmwareVersionUpdateVersionDto dto);

    /**
     * 描述：更新上线时间
     * @author nongchongwei
     * @date 2018/10/22 17:48
     * @param
     * @return
     */
    int updateVersionOnlineTimeByProductId(FirmwareVersionUpdateVersionDto dto);

    /**
     * 描述：固件查询
     * @author nongchongwei
     * @date 2018/10/22 20:10
     * @param
     * @return
     */
    List<FirmwareVersionQueryResp> getAllOTAVersionInfoListByProductId(Long tenantId, Long prodId);

    /**
     * 描述：固件查询
     * @author nongchongwei
     * @date 2018/10/22 20:10
     * @param
     * @return
     */
    List<FirmwareVersionQueryResp> getInitOTAVersionInfoListByProductId(Long tenantId, Long prodId);

    /**
     * 描述：记录操作日志
     * @author nongchongwei
     * @date 2018/10/22 20:27
     * @param
     * @return
     */
    void recordUpgradePlanLog(UpgradePlanOperateReq upgradePlanOperateReq);

    /**
     * 描述：升级计划修改
     * @author nongchongwei
     * @date 2018/7/24 20:00
     * @param  upgradePlanEditReq
     * @return
     */
    int editUpgradePlan(UpgradePlanEditReq upgradePlanEditReq);

    /**
     * 描述：依据产品ID获取升级版本列表
     * @author maochengyuan
     * @created 2018/7/30 9:37
     * @param tenantId 租户id
     * @param productId 产品id
     * @return java.util.List<java.lang.String>
     */
    List<String> getOTAVersionListByProductId(Long tenantId, Long productId);

    /**
     * 描述：依据产品ID获取版分页查询
     * @author maochengyuan
     * @created 2018/7/30 9:37
     * @param req 查询参数
     * @return java.util.List<com.iot.device.vo.rsp.ota.FirmwareVersionPageResp>
     */
    Page<FirmwareVersionPageResp> getOTAVersionPageByProductId(FirmwareVersionSearchReqDto req);

    /**
     * 描述：获取下一个版本
     * @author nongchongwei
     * @date 2018/7/24 20:00
     * @param  productId
     * @param  currentVersion
     * @return
     */
    String getNextVersion(Long productId, String currentVersion);


    /**
     * 描述：获取下一个升级信息
     * @author nongchongwei
     * @date 2018/7/24 20:00
     * @param  productId
     * @param  currentVersion
     * @return
     */
    UpgradeInfoResp getNextUpgradeInfoResp(Long productId, String currentVersion);

    /**
     * 描述：批量插入设备版本信息
     * @author nongchongwei
     * @date 2018/7/24 20:00
     * @param  upgradeDeviceVersionList
     * @param  deviceIdList
     * @return
     */
    void batchInsertUpgradeDeviceVersion(List<UpgradeDeviceVersion> upgradeDeviceVersionList, List<String> deviceIdList);

    /**
     * 描述：获取设备强制升级设备列表
     * @author maochengyuan
     * @created 2018/8/7 16:34
     * @param productId 产品id
     * @param sta 开始下标
     * @param bean 返回实体类
     * @return java.util.List<T>
     */
    <T> List<T> getOtaListFromCache(Long productId, int sta, Class<T> bean);

    /**
     * 描述：获取设策略升级设备列表
     * @author nongchongwei
     * @date 2018/11/19 16:03
     * @param
     * @return
     */
    <T> List<T> getStrategyOtaListFromCache(Long planId, Integer group, int sta, Class<T> bean);

    /**
     * 描述：缓存升级信息
     * @author maochengyuan
     * @created 2018/8/9 10:35
     * @param req 版本信息
     * @return void
     */
    void cacheUpgradeLog(@RequestBody UpgradeLogAddReq req);

    /**
     * 描述：更新升级结果
     * @author maochengyuan
     * @created 2018/8/9 10:36
     * @param deviceId 设备id
     * @param upgradeResult 升级结果
     * @return void
     */
    void updateUpgradeLog(String deviceId, String upgradeResult,String upgradeMsg);

    /**
     * 描述：获取计划版本信息
     * @author nongchongwei
     * @date 2018/8/7 17:33
     * @param
     * @return
     */
    List<String> getVersionByProductId(Long productId);

    /**
     * 描述：获取设备版本
     * @author nongchongwei
     * @date 2018/8/7 17:33
     * @param
     * @return
     */
    String getOtaDeviceVersion(String deviceId);
    /**
     * 描述：获取固件下载url
     * @author nongchongwei
     * @date 2018/9/5 11:28
     * @param
     * @return
     */
    Map<String,String> getFirmwareUrl(Long productId, String version);
    /**
     * 描述：获取版本使用百分比
     * @author nongchongwei
     * @date 2018/9/5 14:36
     * @param
     * @return
     */
    Map<String, String> getVersionPercent(VersionPercentReq versionPercentReq);

    /**
     * 描述：保存策略配置
     * @author nongchongwei
     * @date 2018/11/15 8:45
     * @param
     * @return
     */
    int saveStrategyConfig(StrategyConfigDto strategyConfigDto);
    /**
     * 功能描述:保存策略报告
     * @param: [strategyReport]
     * @return: int
     * @auther: nongchongwei
     * @date: 2018/11/26 16:26
     */
    void updateStrategyReport(String deviceId,String upgradeResult,String reason);

    /**
     * 功能描述:查询策略升级 升级失败版本
     * @param: [planId]
     * @return: java.util.List<java.lang.String>
     * @auther: nongchongwei
     * @date: 2018/11/27 11:25
     */
    List<String> selectFailUpgradeVersion(Long planId);

    /**
     * 功能描述:根据计划杀出策略报告
     * @param: [planId]
     * @return: int
     * @auther: nongchongwei
     * @date: 2018/11/28 9:52
     */
    int deleteStrategyReportByPlanId(Long planId);


    /**
     * 功能描述:查询容灾报告
     * @param: [planId, strategyGroup]
     * @return: java.util.List<com.iot.device.model.ota.StrategyReport>
     * @auther: nongchongwei
     * @date: 2018/11/28 21:00
     */
    List<StrategyReportGroupVo> selectStrategyReportByGroup( Long productId);

    /**
     * 功能描述:分页查询容灾报告
     * @param: [strategyReportSearchReqDto]
     * @return: Page<StrategyReportResp>
     * @auther: nongchongwei
     * @date: 2018/11/28 21:00
     */
     Page<StrategyReportResp>  selectStrategyReportByGroupAsPage( StrategyReportSearchReqDto strategyReportSearchReqDto);
    /**
     * 描述：查询计划对应的策略配置
     * @author nongchongwei
     * @date 2018/11/15 8:47
     * @param
     * @return
     */
    List<StrategyConfigResp> getStrategyConfig(Long planId, Long tenantId);

    /**
     * 保存策略明细
     * @param strategyDetailDto
     * @return
     */
    void saveStrategyDetail(StrategyDetailDto strategyDetailDto);
    /**
     * 描述：查询策略明细 指定uuid升级
     * @author nongchongwei
     * @date 2018/11/15 18:07
     * @param
     * @return
     */
    Set<String> getStrategyDetail(Long planId);

    /**
     * 描述：查询计划编辑的批次列表 指定Batch升级
     * @author nongchongwei
     * @date 2018/11/15 18:07
     * @param
     * @return
     */
    List<Long> selectBatchByPlanId( Long planId);
    /**
     * 描述：查询策略明细 批次升级
     * @author nongchongwei
     * @date 2018/11/15 18:07
     * @param
     * @return
     */
    Set<String> getStrategyDetailWithBatchNum(Long planId);
    /**
     * 描述：查询策略明细 全量带策略升级
     * @author nongchongwei
     * @date 2018/11/15 18:07
     * @param
     * @return
     */
    List<String> getStrategyDetailWithGroup(Long planId,Integer group);
    /**
     * 描述：查询策略明细 全量带策略升级 查计划下全部
     * @author nongchongwei
     * @date 2018/11/15 18:07
     * @param
     * @return
     */
    List<String> getStrategyDetailWithPlanId(Long planId);
    /**
     * 描述：删除指定计划的策略明细
     * @author nongchongwei
     * @date 2018/11/15 18:08
     * @param
     * @return
     */
    int delStrategyDetail(Long planId);
    /**
     * 描述：获取OTA文件列表
     * @author linjihuang
     * @date 2018/9/10 14:36
     * @param
     * @return
     */
    Page<OtaFileInfoResp> getOtaFileList(@RequestBody OtaPageReq pageReq);

    /**
     * 描述：删除固件
     * @author nongchongwei
     * @date 2018/9/27 18:07
     * @param
     * @return
     */
    int deleteByFirmwareId(Long id);
	/**
     * 描述：添加OTA文件信息
     * @author linjihuang
     * @date 2018/9/10 14:36
     * @param otaFileInfoResp
     * @return
     */
	int saveOtaFileInfo(@RequestBody OtaFileInfoReq otaFileInfoReq);
	/**
     * 描述：更新OTA文件信息
     * @author linjihuang
     * @date 2018/9/10 14:36
     * @param otaFileInfoResp
     * @return
     */
	int updateOtaFileInfo(@RequestBody OtaFileInfoReq otaFileInfoReq);
	/**
     * 描述：根据productId获取OTA文件信息
     * @author linjihuang
     * @date 2018/9/10 14:36
     * @param otaFileInfoResp
     * @return
     */
	OtaFileInfoResp findOtaFileInfoByProductId(@RequestBody OtaFileInfoReq otaFileInfoReq);
}
