package com.iot.portal.ota.service;

import com.iot.common.helper.Page;
import com.iot.device.vo.req.ota.StrategyConfigDto;
import com.iot.device.vo.req.ota.StrategyReportResp;
import com.iot.device.vo.req.ota.StrategyReportSearchReqDto;
import com.iot.device.vo.rsp.ota.FirmwareVersionPageResp;
import com.iot.device.vo.rsp.ota.FirmwareVersionQueryResp;
import com.iot.device.vo.rsp.ota.StrategyConfigResp;
import com.iot.device.vo.rsp.ota.StrategyReportGroupVo;
import com.iot.portal.ota.vo.*;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.util.List;
import java.util.Map;

/**
 * 项目名称：IOT云平台
 * 模块名称：聚合层
 * 功能描述：OTA管理接口
 * 创建人： maochengyuan
 * 创建时间：2018/7/24 15:32
 * 修改人： maochengyuan
 * 修改时间：2018/7/24 15:32
 * 修改描述：
 */
public interface OTAManageService {

    /**
     * 描述：查询升级日志
     * @author nongchongwei
     * @date 2018/7/24 20:00
     * @param  upgradeLogSearchReq
     * @return
     */
    Page<UpgradeLogVo> getUpgradeLog(UpgradeLogSearchReq upgradeLogSearchReq);

    /**
     * 描述：新增OTA升级版本信息
     * @author maochengyuan
     * @created 2018/7/24 19:44
     * @param firmwareVersionReq OTA升级版本
     * @return com.iot.common.beans.CommonResponse
     */
    int createFirmwareVersion(FirmwareVersionReq firmwareVersionReq);

    /**
     * 描述：新增OTA升级版本信息,一创建便上线，仅用于产品发布
     * @author nongchongwei
     * @created 2018/7/24 19:44
     * @param firmwareVersionReq OTA升级版本
     * @return com.iot.common.beans.CommonResponse
     */
    void initFirmwareVersion(FirmwareVersionReq firmwareVersionReq);

    /**
     * 描述 初始固件查询
     * @author nongchongwei
     * @date 2018/11/12 15:23
     * @param
     * @return
     */
    FirmwareVersionQueryResp getInitOTAVersionInfoListByProductId(Long productId);

    /**
     * 描述：保存策略配置
     * @author nongchongwei
     * @date 2018/11/15 8:45
     * @param
     * @return
     */
    int saveStrategyConfig(StrategyConfigDto strategyConfigDto);
    /**
     * 描述：查询计划对应的策略配置
     * @author nongchongwei
     * @date 2018/11/15 8:47
     * @param
     * @return
     */
    List<StrategyConfigResp> getStrategyConfig(String productId);

    /**
     * 描述：查询容灾报告
     * @author nongchongwei
     * @date 2018/11/15 8:47
     * @param
     * @return
     */
    List<StrategyReportGroupVo> getStrategyReport(String prodId);

    /**
     * 描述：分页查询容灾报告
     * @author nongchongwei
     * @date 2018/11/15 8:47
     * @param
     * @return
     */
    Page<StrategyReportResp> selectStrategyReportByGroupAsPage(StrategyReportSearchReqDto dto);
    /**
     * 描述：上传OTA升级文件
     * @author maochengyuan
     * @created 2018/7/27 16:11
     * @param multipartRequest 文件对象
     * @return java.lang.String 文件id
     */
    String uploadFirmwareOtaFile(MultipartHttpServletRequest multipartRequest);

    /**
     * 描述：依据产品ID查询升级版本列表
     * @author maochengyuan
     * @created 2018/7/24 19:44
     * @param req 查询参数
     * @return com.iot.common.beans.CommonResponse
     */
    Page<FirmwareVersionVo> getFirmwareVersionListByProductId(FirmwareVersionSearchReq req);

    /**
     * 描述：查询OTA版本合法性（唯一性）
     * @author maochengyuan
     * @created 2018/7/24 19:44
     * @param productId 产品id
     * @param otaVersion OTA版本
     * @return com.iot.common.beans.CommonResponse
     */
    Boolean checkVersionLegality(String productId, String otaVersion);

    /**
     * 描述：查询OTA版本合法性（唯一性）
     * @author nongchongwei
     * @created 2018/7/24 19:44
     * @param productId 产品id
     * @param otaVersion OTA版本
     * @return com.iot.common.beans.CommonResponse
     */
    Boolean checkVersionLegalityByProId(String productId, String otaVersion);

    /**
     * 描述：计划操作记录分页查询
     * @author nongchongwei
     * @date 2018/7/24 20:00
     * @param  upgradePlanSearchReq
     * @return
     */
    Page<UpgradePlanLogVo> getUpgradePlanLog(UpgradePlanSearchReq upgradePlanSearchReq);

    /**
     * 描述：产品分页查询
     * @author nongchongwei
     * @date 2018/7/24 20:00
     * @param  productOtaSearchReq
     * @return
     */
    Page<ProductOtaVo> getProduct(ProductOtaSearchReq productOtaSearchReq);

    /**
     * 描述：升级计划查询
     * @author nongchongwei
     * @date 2018/7/24 20:00
     * @param  productId
     * @return
     */
    UpgradePlanVo getUpgradePlan(String productId);

    /**
     * 描述：修改升级计划状态（启动/暂停）
     * @author nongchongwei
     * @date 2018/7/24 20:00
     * @param  upgradePlanUpdateReq
     * @return
     */
    void operatePlan(UpgradePlanUpdateReq upgradePlanUpdateReq);

    /**
     * 描述：依据产品ID获取升级版本列表
     * @author maochengyuan
     * @created 2018/7/26 20:11
     * @param productId 产品id
     * @return java.util.List<java.lang.String>
     */
    List<String> getOTAVersionListByProductId(String productId);

    /**
     * 描述：删除固件
     * @author nongchongwei
     * @created 2018/7/26 20:11
     * @param firmwareId 固件id
     * @return
     */
    void deleteByFirmwareId(String firmwareId);

    /**
     * 描述：依据产品ID获取版分页查询
     * @author maochengyuan
     * @created 2018/7/26 20:11
     * @param req 查询参数
     * @return com.github.pagehelper.PageInfo<com.iot.device.vo.rsp.ota.FirmwareVersionPageResp>
     */
    Page<FirmwareVersionPageResp> getOTAVersionPageByProductId(FirmwareVersionSearchReq req);

    /**
     * 描述：升级计划修改
     * @author nongchongwei
     * @date 2018/7/24 20:00
     * @param  upgradePlanVo
     * @return
     */
    void editUpgradePlan(UpgradePlanVo upgradePlanVo);
    /**
     * 描述：查询版本使用百分比
     * @author nongchongwei
     * @date 2018/9/5 15:01
     * @param
     * @return
     */
    Map<String,String> getVersionPercent(PercentReq versionPercentReq);
}
