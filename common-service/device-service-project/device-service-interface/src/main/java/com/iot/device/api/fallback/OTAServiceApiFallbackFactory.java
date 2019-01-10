package com.iot.device.api.fallback;

import com.iot.common.helper.Page;
import com.iot.device.api.OTAServiceApi;
import com.iot.device.vo.req.OtaPageReq;
import com.iot.device.vo.req.ota.*;
import com.iot.device.vo.rsp.ota.*;
import feign.hystrix.FallbackFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @Author: xfz
 * @Descrpiton:
 * @Date: 14:18 2018/5/2
 * @Modify by:
 */
@Component
public class OTAServiceApiFallbackFactory implements FallbackFactory<OTAServiceApi> {
    /**
     *
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(OTAServiceApiFallbackFactory.class);

    @Override
    public OTAServiceApi create(Throwable cause) {
        return new OTAServiceApi() {
            @Override
            public Page<UpgradeLogResp> getUpgradeLog(UpgradeLogReq upgradeLogReq) {
                return null;
            }

            @Override
            public Page<UpgradePlanLogResp> getUpgradePlanLog(UpgradePlanReq upgradePlanReq) {
                return null;
            }

            @Override
            public Boolean checkVersionLegality(Long tenantId, Long prodId, String otaVersion) {
                return null;
            }

            @Override
            public Page<FirmwareVersionResp> getFirmwareVersionListByProductId(FirmwareVersionSearchReqDto dto) {
                return null;
            }

            @Override
            public int createFirmwareVersion(FirmwareVersionReqDto dto) {
                return 0;
            }

            @Override
            public void initFirmwareVersion(FirmwareVersionDto dto) {

            }

            @Override
            public UpgradePlanResp getUpgradePlan(UpgradePlanReq upgradePlanReq) {
                return null;
            }

            @Override
            public UpgradePlanResp getUpgradePlanByProductId(Long productId) {
                return null;
            }

            @Override
            public Map<Long, UpgradePlanResp> getUpgradePlanByBathProductId(List<Long> productIdList) {
                return null;
            }

            @Override
            public int updatePlanStatus(Long id, String planStatus) {
                return 0;
            }

            @Override
            public int updatePlanStartTime(Long id, Date startTime) {
                return 0;
            }

            @Override
            public List<SubOtaDeviceInfo> getSubOtaDeviceInfo(Long productId, List<String> versionList) {
                return null;
            }

            @Override
            public List<ForceOtaDevInfo> getDirectForceOta(Long productId, List<String> versionList) {
                return null;
            }

            @Override
            public List<OtaDeviceInfo> getOtaDeviceInfo(Long productId, List<String> versionList) {
                return null;
            }

            @Override
            public int updateVersionOnlineTime(FirmwareVersionUpdateVersionDto dto) {
                return 0;
            }

            @Override
            public int updateVersionOnlineTimeNoVersion(FirmwareVersionUpdateVersionDto dto) {
                return 0;
            }

            @Override
            public int updateVersionOnlineTimeByProductId(FirmwareVersionUpdateVersionDto dto) {
                return 0;
            }

            @Override
            public List<FirmwareVersionQueryResp> getAllOTAVersionInfoListByProductId(Long tenantId, Long prodId) {
                return null;
            }

            @Override
            public List<FirmwareVersionQueryResp> getInitOTAVersionInfoListByProductId(Long tenantId, Long productId) {
                return null;
            }

            @Override
            public void recordUpgradePlanLog(UpgradePlanOperateReq upgradePlanOperateReq) {

            }

            @Override
            public int editUpgradePlan(UpgradePlanEditReq upgradePlanEditReq) {
                return 0;
            }

            @Override
            public List<String> getOTAVersionListByProductId(Long tenantId, Long productId) {
                return null;
            }

            @Override
            public Page<FirmwareVersionPageResp> getOTAVersionPageByProductId(FirmwareVersionSearchReqDto req) {
                return null;
            }

            @Override
            public UpgradeInfoResp getNextVersion(Long productId, String currentVersion) {
                return null;
            }

            @Override
            public Map<String, UpgradeInfoResp> getNextVersionMap(List<String> productIdVersionList) {
                return null;
            }

            @Override
            public Map<String, UpgradeInfoResp> getUpgradeInfoRespMap(Long productId, List<String> versionList) {
                return null;
            }

            @Override
            public void batchInsertUpgradeDeviceVersion(BatchIUpgradeDeviceVersion batchIUpgradeDeviceVersion) {

            }

            @Override
            public List<ForceOtaDevInfo> getDirForceOtaList(Long productId, int sta) {
                return null;
            }

            @Override
            public List<SubForceOta> getSubForceOtaList(Long productId, int sta) {
                return null;
            }

            @Override
            public List<PushOta> getPushOtaList(Long productId, int sta) {
                return null;
            }

            @Override
            public List<ForceOtaDevInfo> getDirForceStrategyOtaList(Long planId, Integer group, int sta) {
                return null;
            }

            @Override
            public List<SubForceOta> getSubForceStrategyOtaList(Long planId, Integer group, int sta) {
                return null;
            }

            @Override
            public List<PushOta> getPushStrategyOtaList(Long planId, Integer group, int sta) {
                return null;
            }

            @Override
            public List<String> getVersionByProductId(Long productId) {
                return null;
            }

            @Override
            public void cacheUpgradeLog(UpgradeLogAddReq req) {

            }

            @Override
            public UpgradeLogAddReq getCacheUpgradeLog(String deviceId) {
                return null;
            }

            @Override
            public void cacheBatchUpgradeLog(UpgradeLogAddBatchReq req) {

            }

            @Override
            public void updateUpgradeLog(String deviceId, String upgradeResult, String upgradeMsg) {

            }

            @Override
            public void updateStrategyReport(String deviceId, String upgradeResult, String reason) {

            }

            @Override
            public List<String> selectFailUpgradeVersion(Long planId) {
                return null;
            }

            @Override
            public int deleteStrategyReportByPlanId(Long planId) {
                return 0;
            }

            @Override
            public List<StrategyReportGroupVo> getStrategyReport(Long productId) {
                return null;
            }

            @Override
            public Page<StrategyReportResp> selectStrategyReportByGroupAsPage(StrategyReportSearchReqDto dto) {
                return null;
            }

            @Override
            public String getOtaDeviceVersion(String deviceId) {
                return null;
            }

            @Override
            public Map<String, String> getFirmwareUrl(Long productId, String version) {
                return null;
            }

            @Override
            public Map<String, String> getVersionPercent(VersionPercentReq versionPercentReq) {
                return null;
            }

            @Override
            public UpgradePlanResp getUpgradePlanByProductModel(String model) {
                return null;
            }

            @Override
            public Map<String, String> getFirmwareUrlByModel(String model, String version) {
                return null;
            }

            @Override
            public int saveStrategyConfig(StrategyConfigDto strategyConfigDto) {
                return 0;
            }

            @Override
            public List<StrategyConfigResp> getStrategyConfig(Long planId, Long tenantId) {
                return null;
            }

            @Override
            public Set<String> getStrategyDetailUuid(Long planId) {
                return null;
            }

            @Override
            public List<Long> selectBatchByPlanId(Long planId) {
                return null;
            }

            @Override
            public Set<String> getStrategyDetailWithBatchNum(Long planId) {
                return null;
            }

            @Override
            public List<String> getStrategyDetailWithGroup(Long planId, Integer group) {
                return null;
            }

            @Override
            public List<String> getStrategyDetailWithPlanId(Long planId) {
                return null;
            }

            @Override
            public int delStrategyDetail(Long planId) {
                return 0;
            }

            @Override
            public void saveStrategyDetail(StrategyDetailDto strategyDetailDto) {

            }

            @Override
            public Page<OtaFileInfoResp> getOtaFileList(OtaPageReq pageReq) {
                return null;
            }

            @Override
            public int saveOtaFileInfo(OtaFileInfoReq otaFileInfoReq) {
                return 0;
            }

            @Override
            public int updateOtaFileInfo(OtaFileInfoReq otaFileInfoReq) {
                return 0;
            }

            @Override
            public OtaFileInfoResp findOtaFileInfoByProductId(OtaFileInfoReq otaFileInfoReq) {
                return null;
            }

            @Override
            public int deleteByFirmwareId(Long id) {
                return 0;
            }

        };
    }
}
