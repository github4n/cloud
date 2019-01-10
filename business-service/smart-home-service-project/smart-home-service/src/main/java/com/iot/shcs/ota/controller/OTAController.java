package com.iot.shcs.ota.controller;

import com.iot.common.exception.BusinessException;
import com.iot.common.helper.Page;
import com.iot.common.util.CommonUtil;
import com.iot.common.util.StringUtil;
import com.iot.device.enums.ota.PlanStatusEnum;
import com.iot.device.exception.OtaExceptionEnum;
import com.iot.device.vo.req.ota.*;
import com.iot.device.vo.rsp.ota.*;
import com.iot.shcs.ota.OtaSmartHomeApi;
import com.iot.shcs.ota.service.IOtaService;
import com.iot.shcs.ota.service.OTAService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * @Author: nongchongwei
 * @Descrpiton: ota　聚合服务
 * @Date: 9:21 2018/10/23
 * @Modify by:
 */
@RestController
public class OTAController implements OtaSmartHomeApi {

    @Autowired
    private OTAService otaService;

    @Autowired
    private IOtaService iOtaService;

    /**
     * 描述：启动计划，通知设备升级
     * @author nongchongwei
     * @date 2018/10/23 10:43
     * @param
     * @return
     */
    @Override
    @ResponseBody
    public void noticeDevice(@RequestParam("productId") Long productId, @RequestParam("tenantId") Long tenantId, @RequestParam("userId") Long userId) {
        this.otaService.noticeDevice(productId);
    }

    /**
     * 描述：查询升级日志
     * @author nongchongwei
     * @date 2018/10/23 10:43
     * @param
     * @return
     */
    @Override
    @ResponseBody
    public Page<UpgradeLogResp> getUpgradeLog(@RequestBody UpgradeLogReq upgradeLogReq) {
        return iOtaService.getUpgradeLog(upgradeLogReq);
    }

    /**
     * 描述：计划操作记录分页查询
     * @author nongchongwei
     * @date 2018/10/23 10:44
     * @param
     * @return
     */
    @Override
    @ResponseBody
    public Page<UpgradePlanLogResp> getUpgradePlanLog(@RequestBody UpgradePlanReq upgradePlanReq) {
        return this.iOtaService.getUpgradePlanLog(upgradePlanReq);
    }

    /**
     * 描述：查询OTA版本合法性（唯一性）
     * @author nongchongwei
     * @date 2018/10/23 11:12
     * @param
     * @return
     */
    @Override
    @ResponseBody
    public Boolean checkVersionLegality(@RequestParam(value = "tenantId") Long tenantId, @RequestParam(value = "prodId") Long prodId, @RequestParam(value = "otaVersion") String otaVersion) {
        return this.iOtaService.checkVersionLegality(tenantId,prodId,otaVersion);
    }

    /**
     * 描述：依据产品ID查询升级版本列表
     * @author nongchongwei
     * @date 2018/10/23 10:46
     * @param
     * @return
     */
    @Override
    @ResponseBody
    public Page<FirmwareVersionResp> getFirmwareVersionListByProductId(@RequestBody FirmwareVersionSearchReqDto dto) {
        return this.iOtaService.getFirmwareVersionListByProductId(dto);
    }

    /**
     * 描述：新增OTA升级版本信息
     * @author nongchongwei
     * @date 2018/10/23 10:46
     * @param
     * @return
     */
    @Override
    @ResponseBody
    public int createFirmwareVersion(@RequestBody FirmwareVersionReqDto dto) {
        return this.iOtaService.createFirmwareVersion(dto);
    }

    /**
     * 描述：新增OTA升级版本信息,一创建便上线，仅用于发布产品时使用
     * @author nongchongwei
     * @date 2018/10/23 10:46
     * @param
     * @return
     */
    @Override
    @ResponseBody
    public void initFirmwareVersion(@RequestBody FirmwareVersionDto dto) {

        this.iOtaService.initFirmwareVersion(dto);
    }
    /**
     * 描述： 初始固件查询
     * @author nongchongwei
     * @date 2018/11/12 15:31
     * @param
     * @return
     */
    @Override
    public FirmwareVersionQueryResp getInitOTAVersionInfoListByProductId(@RequestParam(value = "tenantId") Long tenantId, @RequestParam(value = "productId") Long productId) {
        return this.iOtaService.getInitOTAVersionInfoListByProductId(tenantId,productId);
    }

    /**
     * 描述：查询计划
     * @author nongchongwei
     * @date 2018/10/23 10:47
     * @param
     * @return
     */
    @Override
    @ResponseBody
    public UpgradePlanResp getUpgradePlan(@RequestBody UpgradePlanReq upgradePlanReq) {
        return this.iOtaService.getUpgradePlan(upgradePlanReq);
    }

    /**
     * 描述：修改升级计划状态（启动/暂停）
     * @author nongchongwei
     * @date 2018/10/23 10:47
     * @param
     * @return
     */
    @Override
    @ResponseBody
    public void operatePlan(@RequestBody UpgradePlanOperateReq upgradePlanOperateReq) {
        if(CommonUtil.isEmpty(upgradePlanOperateReq.getPlanId())){
            throw new BusinessException(OtaExceptionEnum.PARAM_IS_ERROR, "plan id is empty");
        }
        if(CommonUtil.isEmpty(upgradePlanOperateReq.getTenantId())){
            throw new BusinessException(OtaExceptionEnum.PARAM_IS_ERROR, "tenant id is empty");
        }
        if(CommonUtil.isEmpty(upgradePlanOperateReq.getUserId())){
            throw new BusinessException(OtaExceptionEnum.PARAM_IS_ERROR, "user id is empty");
        }
        if(CommonUtil.isEmpty(upgradePlanOperateReq.getProductId())){
            throw new BusinessException(OtaExceptionEnum.PARAM_IS_ERROR, "product id is empty");
        }
        if(StringUtil.isEmpty(upgradePlanOperateReq.getPlanStatus())){
            throw new BusinessException(OtaExceptionEnum.PARAM_IS_ERROR, "operate type is empty");
        }
        if(!PlanStatusEnum.Start.getValue().equals(upgradePlanOperateReq.getPlanStatus()) &&
                !PlanStatusEnum.Pause.getValue().equals(upgradePlanOperateReq.getPlanStatus())){
            throw new BusinessException(OtaExceptionEnum.PARAM_IS_ERROR, "operate type is not satisfiable");
        }
        try{
            iOtaService.operatePlan(upgradePlanOperateReq);
        }catch (Exception e) {
            iOtaService.resetPlan(upgradePlanOperateReq);
            throw new BusinessException(OtaExceptionEnum.OPERATE_UPGRADE_PLAN__ERROR, e);
        }

    }

    /**
     * 描述：升级计划修改
     * @author nongchongwei
     * @date 2018/10/23 10:48
     * @param
     * @return
     */
    @Override
    @ResponseBody
    public int editUpgradePlan(@RequestBody UpgradePlanEditReq upgradePlanEditReq) {
        return this.iOtaService.editUpgradePlan( upgradePlanEditReq);
    }

    /**
     * 描述：依据产品ID获取升级版本列表
     * @author nongchongwei
     * @date 2018/10/23 10:48
     * @param
     * @return
     */
    @Override
    @ResponseBody
    public List<String> getOTAVersionListByProductId(@RequestParam(value = "tenantId") Long tenantId, @RequestParam(value = "productId") Long productId) {
        return this.iOtaService.getOTAVersionListByProductId(tenantId, productId);
    }

    /**
     * 描述：依据产品ID获取版分页查询
     * @author nongchongwei
     * @date 2018/10/23 10:49
     * @param
     * @return
     */
    @Override
    @ResponseBody
    public Page<FirmwareVersionPageResp> getOTAVersionPageByProductId(@RequestBody FirmwareVersionSearchReqDto req) {
        return this.iOtaService.getOTAVersionPageByProductId(req);
    }

    /**
     * 描述：获取版本百分比
     * @author nongchongwei
     * @date 2018/10/23 10:50
     * @param
     * @return
     */
    @Override
    @ResponseBody
    public Map<String, String> getVersionPercent(@RequestBody VersionPercentReq versionPercentReq) {
        return this.iOtaService.getVersionPercent(versionPercentReq);
    }

    /**
     * 描述：根据model获取升级计划
     * @author nongchongwei
     * @date 2018/10/23 10:50
     * @param
     * @return
     */
    @Override
    @ResponseBody
    public UpgradePlanResp getUpgradePlanByProductModel(@RequestParam(value = "model") String model) {
        return this.iOtaService.getUpgradePlanByProductModel(model);
    }

    /**
     * 描述：根据产品model获取固件下载url
     * @author nongchongwei
     * @date 2018/10/23 10:50
     * @param
     * @return
     */
    @Override
    @ResponseBody
    public Map<String, String> getFirmwareUrlByModel(@RequestParam(value = "model") String model, @RequestParam(value = "version") String version) {
        return this.iOtaService.getFirmwareUrlByModel(model, version);
    }

    /**
     * 描述：删除固件
     * @author nongchongwei
     * @date 2018/10/23 10:51
     * @param
     * @return
     */
    @Override
    @ResponseBody
    public int deleteByFirmwareId(@RequestParam(value = "id") Long id) {
        return this.iOtaService.deleteByFirmwareId(id);
    }

}
