package com.iot.video.api.fallback;

import com.github.pagehelper.PageInfo;
import com.iot.video.api.VideoManageApi;
import com.iot.video.dto.*;
import com.iot.video.entity.VideoEvent;
import com.iot.video.entity.VideoFile;
import com.iot.video.entity.VideoPayRecord;
import com.iot.video.vo.req.CountVideoDateReq;
import com.iot.video.vo.req.GetEventVideoFileReq;
import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class VideoManageApiFallbackFactory implements FallbackFactory<VideoManageApi> {
    @Override
    public VideoManageApi create(Throwable throwable) {
        return new VideoManageApi() {
            @Override
            public PageInfo<VideoPlanDto> getPlanList(PlanSearchParam planSearchParam) {
                return null;
            }

            @Override
            public VideoPlanInfoDto getPlanInfoByDevId(PlanSearchParam planSearchParam) {
                return null;
            }

            @Override
            public void updatePlanExecStatus(Long tenantId, String userId, String planId, Integer planExecStatus) {

            }

            @Override
            public void updatePlanName(PlanNameParam planNameParam) {

            }

            @Override
            public List<VideoTaskDto> getVideoTaskList(Long tenantId, String userId, String planId) {
                return null;
            }

            @Override
            public void updatePlanTask(List<TaskPlanParam> taskPlanList) {

            }

            @Override
            public PageInfo<VideoPackageDto> getVideoPackageList(VideoPackageSearchParam videoPackageSearchParam) {
                return null;
            }

            @Override
            public String createPlan(PlanParam plan) {
                return null;
            }

            @Override
            public PageInfo<PayRecordDto> getBuyHisRecordList(HisRecordSearchParam hisRecordSearchParam) {
                return null;
            }

            @Override
            public PageInfo<PayRecordDto> getBuyRecordList(RecordSearchParam searchParam) {
                return null;
            }

            @Override
            public List<ALLRecordDto> getAllBuyRecordList(AllRecordSearchParam searchParam) {
                return null;
            }

            @Override
            public void saveWebPlan(WebPlanDto webPlan) {

            }

            @Override
            public WebPlanDto getWebPlan(String orderId) {
                return null;
            }

            @Override
            public AppPayDto getPackagePriceById(String packageId) {
                return null;
            }

            @Override
            public void saveAppPay(String payId, String planId) {

            }

            @Override
            public String getAppPay(String payId) {
                return null;
            }

            @Override
            public int renewPlan(Long tenantId, String userId, String planId, int counts, String orderId) {
                return 0;
            }

            @Override
            public void planBandingDevice(Long tenantId, String userId, String planId, String deviceId) {

            }

            @Override
            public void deleteVideoEvent(Long tenantId, String userId, String planId, String eventId) {

            }

            @Override
            public void insertVideoEvent(VideoEvent videoEvent) {

            }

            @Override
            public String getDeviceId(String planId) {
                return null;
            }

            @Override
            public String getPlanId(String deviceId) {
                return null;
            }

            @Override
            public List<VideoPlanTaskDto> getSyncTaskInfo(String planId) {
                return null;
            }

            @Override
            public void createVideoFile(VideoFile videoFile) {

            }

            @Override
            public List<String> judgeDeviceBandPlan(List<String> deviceIdList) {
                return null;
            }

            @Override
            public void deletePlanOrder(String orderId) {

            }

            @Override
            public boolean judgePlanExist(String planId) {
                return false;
            }

            @Override
            public boolean checkDeviceHasBindPlan(String deviceId) {
                return false;
            }

            @Override
            public VideoPayRecordDto getLatestOrderByPlanIdAndStatus(Long tenantId, String planId, Integer planStatus) {
                return null;
            }

            @Override
            public List<Integer> getVideoStartTimeHourByDate(String searchDate, String planId) {
                return null;
            }

            @Override
            public VideoPayRecord getVideoPayRecord(Long tenantId, String planId, String orderId) {
                return null;
            }

            @Override
            public void refundOneOrderOfPlan(String planId, String orderId, Long tenantId) {

            }

            @Override
            public List<VideoPlanOrderDto> queryPlanOtherPayRecord(String orderId, String planId) {
                return null;
            }

            @Override
            public void planUnBandingDevice(Long tenantId, String deviceId) {

            }

            @Override
            public void dealPlanAllTimeOver(String planId, String videoStartTime) {

            }

            @Override
            public void dealPlanEventOver(String planId) {

            }

            @Override
            public void dealPlanExpireRelateData(String planId) {

            }

            @Override
            public List<VideoTsFileDto> getVideoFileListByEventUuid(GetEventVideoFileReq getEventVideoFileReq) {
                return null;
            }

            @Override
            public List<String> countVideoDate(CountVideoDateReq req) {
                return null;
            }
        };
    }
}
