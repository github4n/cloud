package com.iot.shcs.ota.service;

import com.iot.common.helper.Page;
import com.iot.device.vo.req.ota.*;
import com.iot.device.vo.rsp.ota.*;

import java.util.List;
import java.util.Map;

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
     * 描述：修改升级计划状态（启动/暂停）
     * @author nongchongwei
     * @date 2018/7/24 20:00
     * @param  upgradePlanOperateReq
     * @return
     */
    void operatePlan(UpgradePlanOperateReq upgradePlanOperateReq);

    /**
     * 描述：修改升级计划状态（启动/暂停） 异常重置成暂停状态
     * @author nongchongwei
     * @date 2018/7/24 20:00
     * @param  upgradePlanOperateReq
     * @return
     */
    void resetPlan(UpgradePlanOperateReq upgradePlanOperateReq);

    /**
     * 描述：查询升级日志
     * @author nongchongwei
     * @date 2018/10/23 10:43
     * @param
     * @return
     */
    Page<UpgradeLogResp> getUpgradeLog(UpgradeLogReq upgradeLogReq);
    /**
     * 描述：计划操作记录分页查询
     * @author nongchongwei
     * @date 2018/10/23 10:44
     * @param
     * @return
     */
    Page<UpgradePlanLogResp> getUpgradePlanLog(UpgradePlanReq upgradePlanReq);
    /**
     * 描述：查询OTA版本合法性（唯一性）
     * @author nongchongwei
     * @date 2018/10/23 11:13
     * @param
     * @return
     */
    Boolean checkVersionLegality(Long tenantId, Long prodId, String otaVersion);
    /**
     * 描述：依据产品ID查询升级版本列表
     * @author nongchongwei
     * @date 2018/10/23 10:46
     * @param
     * @return
     */
    Page<FirmwareVersionResp> getFirmwareVersionListByProductId(FirmwareVersionSearchReqDto dto);
    /**
     * 描述：新增OTA升级版本信息
     * @author nongchongwei
     * @date 2018/10/23 10:46
     * @param
     * @return
     */
    int createFirmwareVersion(FirmwareVersionReqDto dto);
    /**
     * 描述：新增OTA升级版本信息,一创建便上线，仅用于发布产品时使用
     * @author nongchongwei
     * @date 2018/10/23 10:46
     * @param
     * @return
     */
    void initFirmwareVersion(FirmwareVersionDto dto);

    /**
     * 描述：固件查询
     * @author nongchongwei
     * @date 2018/10/22 20:10
     * @param
     * @return
     */
    FirmwareVersionQueryResp getInitOTAVersionInfoListByProductId(Long tenantId, Long prodId);
    /**
     * 描述：查询计划
     * @author nongchongwei
     * @date 2018/10/23 10:47
     * @param
     * @return
     */
     UpgradePlanResp getUpgradePlan(UpgradePlanReq upgradePlanReq);

    /**
     * 描述：升级计划修改
     * @author nongchongwei
     * @date 2018/10/23 10:48
     * @param
     * @return
     */
    int editUpgradePlan(UpgradePlanEditReq upgradePlanEditReq);
    /**
     * 描述：依据产品ID获取升级版本列表
     * @author nongchongwei
     * @date 2018/10/23 10:48
     * @param
     * @return
     */
    List<String> getOTAVersionListByProductId(Long tenantId, Long productId);
    /**
     * 描述：依据产品ID获取版分页查询
     * @author nongchongwei
     * @date 2018/10/23 10:49
     * @param
     * @return
     */
    Page<FirmwareVersionPageResp> getOTAVersionPageByProductId(FirmwareVersionSearchReqDto req);
    /**
     * 描述：获取版本百分比
     * @author nongchongwei
     * @date 2018/10/23 10:50
     * @param
     * @return
     */
    Map<String, String> getVersionPercent(VersionPercentReq versionPercentReq);
    /**
     * 描述：根据model获取升级计划
     * @author nongchongwei
     * @date 2018/10/23 10:50
     * @param
     * @return
     */
     UpgradePlanResp getUpgradePlanByProductModel(String model);
    /**
     * 描述：根据产品model获取固件下载url
     * @author nongchongwei
     * @date 2018/10/23 10:50
     * @param
     * @return
     */
    Map<String, String> getFirmwareUrlByModel(String model, String version);
    /**
     * 描述：删除固件
     * @author nongchongwei
     * @date 2018/10/23 10:51
     * @param
     * @return
     */
    int deleteByFirmwareId( Long id);

}
