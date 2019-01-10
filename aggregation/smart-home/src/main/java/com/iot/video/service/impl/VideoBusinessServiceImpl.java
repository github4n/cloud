package com.iot.video.service.impl;

import com.github.pagehelper.PageInfo;
import com.iot.common.beans.BeanUtil;
import com.iot.common.exception.BusinessException;
import com.iot.common.util.JsonUtil;
import com.iot.common.util.SecurityUtil;
import com.iot.common.util.StringUtil;
import com.iot.constant.DeviceTypeConstant;
import com.iot.constant.SecurityKey;
import com.iot.control.device.vo.resp.ListUserDeviceInfoRespVo;
import com.iot.device.service.DeviceBusinessService;
import com.iot.device.vo.rsp.device.GetDeviceInfoRespVo;
import com.iot.device.vo.rsp.device.GetDeviceStatusInfoRespVo;
import com.iot.device.vo.rsp.device.GetDeviceTypeInfoRespVo;
import com.iot.device.vo.rsp.device.GetProductInfoRespVo;
import com.iot.exception.BusinessExceptionEnum;
import com.iot.file.api.FileApi;
import com.iot.file.api.VideoFileApi;
import com.iot.file.vo.VideoFileGetUrlReq;
import com.iot.payment.api.GoodsServiceApi;
import com.iot.payment.api.OrderApi;
import com.iot.payment.api.PaymentApi;
import com.iot.payment.entity.goods.GoodsInfo;
import com.iot.payment.entity.order.OrderGoods;
import com.iot.payment.enums.goods.GoodsTypeEnum;
import com.iot.payment.enums.order.OrderTypeEnum;
import com.iot.payment.vo.goods.req.GoodsExtendServiceReq;
import com.iot.payment.vo.goods.req.VideoPackageReq;
import com.iot.payment.vo.order.req.CreateOrderRecordReq;
import com.iot.payment.vo.order.resp.OrderDetailInfoResp;
import com.iot.payment.vo.pay.req.CreatePayReq;
import com.iot.payment.vo.pay.resp.CreatePayResp;
import com.iot.saas.SaaSContextHolder;
import com.iot.shcs.device.api.DeviceCoreServiceApi;
import com.iot.shcs.ipc.api.IpcApi;
import com.iot.system.api.LangApi;
import com.iot.video.api.VideoManageApi;
import com.iot.video.api.VideoPlanApi;
import com.iot.video.dto.EventDto;
import com.iot.video.dto.EventParamDto;
import com.iot.video.dto.HisRecordSearchParam;
import com.iot.video.dto.LastPicParamDto;
import com.iot.video.dto.PayRecordDto;
import com.iot.video.dto.PlanInfoDto;
import com.iot.video.dto.PlanLastPicDto;
import com.iot.video.dto.PlanNameParam;
import com.iot.video.dto.PlanOrderParamDto;
import com.iot.video.dto.PlanParam;
import com.iot.video.dto.PlanSearchParam;
import com.iot.video.dto.RecordSearchParam;
import com.iot.video.dto.TaskPlanParam;
import com.iot.video.dto.VideoEventParamDto;
import com.iot.video.dto.VideoFileDto;
import com.iot.video.dto.VideoFileParamDto;
import com.iot.video.dto.VideoPlanDto;
import com.iot.video.dto.VideoPlanInfoDto;
import com.iot.video.dto.VideoTaskDto;
import com.iot.video.dto.VideoTsFileDto;
import com.iot.video.dto.WebPlanDto;
import com.iot.video.entity.VideoPlan;
import com.iot.video.service.VideoBusinessService;
import com.iot.video.vo.BuyHisRecordListParamVO;
import com.iot.video.vo.BuyHisRecordListVO;
import com.iot.video.vo.BuyRecordListVO;
import com.iot.video.vo.EventParamVO;
import com.iot.video.vo.EventVO;
import com.iot.video.vo.LastPicParamVO;
import com.iot.video.vo.LastPicVO;
import com.iot.video.vo.PlanNameVo;
import com.iot.video.vo.PlanOrderParamVO;
import com.iot.video.vo.PlanParamVo;
import com.iot.video.vo.TaskPlanVo;
import com.iot.video.vo.VideoEventParamVO;
import com.iot.video.vo.VideoFileParamVO;
import com.iot.video.vo.VideoFileVO;
import com.iot.video.vo.VideoPackageParamVo;
import com.iot.video.vo.VideoPackageVo;
import com.iot.video.vo.VideoPlanInfoVo;
import com.iot.video.vo.VideoPlanVo;
import com.iot.video.vo.VideoTaskVo;
import com.iot.video.vo.WebPlanVo;
import com.iot.video.vo.req.CountVideoDateReq;
import com.iot.video.vo.req.GetEventVideoFileReq;
import com.iot.vo.BasePageVO;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

/**
 * 项目名称：IOT视频云 模块名称：聚合层 功能描述：视频接口实现 创建人：mao2080@sina.com 创建时间：2018/3/20 17:28 修改人：mao2080@sina.com
 * 修改时间：2018/3/2017:28 修改描述：
 */
@Service("videoBusiness")
public class VideoBusinessServiceImpl implements VideoBusinessService {

    private static final Logger LOGGER = LoggerFactory.getLogger(VideoBusinessServiceImpl.class);
    private static ExecutorService executorService = Executors.newCachedThreadPool();
    @Autowired
    private VideoPlanApi videoPlanApi;
    @Autowired
    private VideoManageApi videoManageApi;
    @Autowired
    private IpcApi ipcApi;
    @Autowired
    private FileApi fileApi;
    @Autowired
    private PaymentApi paymentApi;
    @Autowired
    private DeviceCoreServiceApi deviceCoreServiceApi;
    @Autowired
    private DeviceBusinessService deviceBusinessService;
    @Autowired
    private SecurityKey securityKey;
    @Autowired
    private GoodsServiceApi goodsServiceApi;
    @Autowired
    private OrderApi orderApi;
    @Autowired
    private LangApi langApi;
    @Autowired
    private VideoFileApi videoFileApi;

    @Value("${paypal.callBackUrl}")
    private String callBackUrl;

    @Value("${paypal.callBackPort}")
    private String callBackPort;

    @Value("${paypal.serverIp}")
    private String serverIp;

    private static List<List<VideoFileGetUrlReq>> splitVideoFileList(
            List<VideoFileGetUrlReq> sourList, int batchCount) {
        int sourListSize = sourList.size();
        int subCount =
                sourListSize % batchCount == 0 ? sourListSize / batchCount : sourListSize / batchCount + 1;
        int startIndext = 0;
        int stopIndext = 0;
        int endIndext = sourListSize % batchCount == 0 ? batchCount : sourListSize % batchCount;
        List<List<VideoFileGetUrlReq>> tempListList = new ArrayList<>();
        List<VideoFileGetUrlReq> tempList = null;
        for (int i = 0; i < subCount; i++) {
            stopIndext = (i == subCount - 1) ? stopIndext + endIndext : stopIndext + batchCount;
            tempList = new ArrayList<>(sourList.subList(startIndext, stopIndext));
            tempListList.add(tempList);
            startIndext = stopIndext;
        }
        return tempListList;
    }

    /**
     * 描述：更新录影计划状态
     *
     * @param planId         计划id
     * @param planExecStatus 计划执行状态
     * @author mao2080@sina.com
     * @created 2018/3/23 9:34
     */
    public void updatePlanExecStatus(String planId, Integer planExecStatus) {
        // 在Header中获取数据
        this.videoManageApi.updatePlanExecStatus(
                SaaSContextHolder.currentTenantId(),
                SaaSContextHolder.getCurrentUserUuid(),
                planId,
                planExecStatus);
        if (null != planId && !"".equals(planId)) {
            ipcApi.notifyDevicePlanInfo(planId);
        }
    }

    /**
     * 描述：修改录影计划任务
     *
     * @param taskPlanList 录影任务参数
     * @return void
     * @author mao2080@sina.com
     * @created 2018/3/26 15:51
     */
    @Override
    public void updatePlanTask(List<TaskPlanVo> taskPlanList) {
        List<TaskPlanParam> taskPlanParamList = null;
        try {
            taskPlanParamList = BeanUtil.listTranslate(taskPlanList, TaskPlanParam.class);
        } catch (Exception e) {
            LOGGER.error("", e);
            throw new BusinessException(BusinessExceptionEnum.CONVERT_TO_LIST_EXCEPTION, e);
        }
        // 在Header中获取数据
        String userId = SaaSContextHolder.getCurrentUserUuid();
        Long tenantId = SaaSContextHolder.currentTenantId();
        if (CollectionUtils.isNotEmpty(taskPlanParamList)) {
            for (TaskPlanParam taskPlanParam : taskPlanParamList) {
                taskPlanParam.setUserId(userId);
                taskPlanParam.setTenantId(tenantId);
            }
            this.videoManageApi.updatePlanTask(taskPlanParamList);
            TaskPlanParam task = taskPlanParamList.get(0);
            if (null != task && null != task.getPlanId() && !"".equals(task.getPlanId())) {
                String planId = task.getPlanId();
                ipcApi.notifyDevicePlanInfo(planId);
            }
        }
    }

    /**
     * 描述：查询套餐列表
     *
     * @param videoPackageParamVo 套餐查询参数VO
     * @return com.github.pagehelper.PageInfo<com.iot.vo.video.VideoPackageVo>
     * @author wujianlong
     * @created 2018/3/26 15:51
     */
    @Override
    public PageInfo<VideoPackageVo> getVideoPackageList(VideoPackageParamVo videoPackageParamVo) {
        PageInfo<VideoPackageVo> page = new PageInfo<>();
        List<Integer> packageTypes = new ArrayList<>();
        packageTypes.add(GoodsTypeEnum.VIDEO_PLAN_ALL_TIME.getCode());
        packageTypes.add(GoodsTypeEnum.VIDEO_PLAN_ENEVT.getCode());
        VideoPackageReq req =
                new VideoPackageReq(
                        videoPackageParamVo.getPageNum(), videoPackageParamVo.getPageSize(), packageTypes);
        PageInfo<GoodsInfo> goodsInfoPage = goodsServiceApi.getVideoPackageList(req);
        List<VideoPackageVo> list = new ArrayList<>();
        List<String> langKey = new ArrayList<>();
        if (goodsInfoPage.getList() != null) {
            goodsInfoPage
                    .getList()
                    .forEach(
                            goodsInfo -> {
                                VideoPackageVo vo =
                                        new VideoPackageVo(
                                                "03",
                                                goodsInfo.getGoodsName(),
                                                goodsInfo.getDescription(),
                                                goodsInfo.getPrice().toString(),
                                                goodsInfo.getCurrency(),
                                                goodsInfo.getTypeId().toString(),
                                                Integer.valueOf(goodsInfo.getStandard()),
                                                goodsInfo.getDescription());
                                vo.setPackageId(
                                        SecurityUtil.EncryptByAES(
                                                goodsInfo.getId().toString(), securityKey.getVideoSerivceKey()));
                langKey.add(goodsInfo.getGoodsName());
                                list.add(vo);
                            });
        }
        BeanUtils.copyProperties(goodsInfoPage, page);
        Map<String, String> nameMap =
                this.langApi.getLangValueByKey(langKey, LocaleContextHolder.getLocale().toString());
        // 国际化语言
        list.forEach(
                o -> {
                    o.setPackageName(nameMap.get(o.getPackageName()));
                });
        page.setList(list);
        return page;
    }

    /**
     * 描述：查询录影任务
     *
     * @param planId 计划id
     * @return java.util.List<com.iot.vo.video.VideoTaskVo>
     * @author wujianlong
     * @created 2018/3/26 15:52
     */
    @Override
    public List<VideoTaskVo> getVideoTaskList(String planId) {
        List<VideoTaskVo> videoTaskVoList = null;
        List<VideoTaskDto> listVideoTaskDto = null;
        try {
            // 在Header中获取数据
            String userId = SaaSContextHolder.getCurrentUserUuid();
            Long tenantId = SaaSContextHolder.currentTenantId();
            listVideoTaskDto = this.videoManageApi.getVideoTaskList(tenantId, userId, planId);
            videoTaskVoList = BeanUtil.listTranslate(listVideoTaskDto, VideoTaskVo.class);
        } catch (Exception e) {
            LOGGER.error("", e);
            throw new BusinessException(BusinessExceptionEnum.CONVERT_TO_LIST_EXCEPTION, e);
        }
        return videoTaskVoList;
    }

    /**
     * 描述：获取计划列表
     *
     * @param planParamVo 查询计划列表参数VO
     * @return com.github.pagehelper.PageInfo<com.iot.vo.video.VideoPlanVo>
     * @author wujianlong
     * @created 2018/3/26 15:52
     */
    @Override
    public PageInfo<VideoPlanVo> getPlanList(PlanParamVo planParamVo) {
        PlanSearchParam param = new PlanSearchParam();
        BeanUtils.copyProperties(planParamVo, param);
        param.setUserId(SaaSContextHolder.getCurrentUserUuid());
        param.setTenantId(SaaSContextHolder.currentTenantId());
        PageInfo<VideoPlanVo> page = new PageInfo<>();
        PageInfo<VideoPlanDto> planDtoPageInfo = this.videoManageApi.getPlanList(param);
        List<VideoPlanDto> list = planDtoPageInfo.getList();
        if (list != null && list.size() > 0) {
            List<String> langKey = new ArrayList<>();
            for (VideoPlanDto videoPlanDto : list) {
                if (StringUtil.isNotBlank(videoPlanDto.getDeviceId())) {
                    GetDeviceInfoRespVo deviceResp =
                            deviceCoreServiceApi.getDeviceInfoByDeviceId(videoPlanDto.getDeviceId());
                    GetDeviceStatusInfoRespVo deviceStatus =
                            deviceCoreServiceApi.getDeviceStatusByDeviceId(
                                    deviceResp.getTenantId(), videoPlanDto.getDeviceId());
                    videoPlanDto.setDeviceName(deviceResp.getName());
                    // 在线状态
                    videoPlanDto.setOnlineStatus(deviceStatus.getOnlineStatus());
                    // 激活状态（0-未激活，1-已激活）
                    videoPlanDto.setDeviceStatus(
                            null == deviceStatus.getActiveStatus() ? "0" : "" + deviceStatus.getActiveStatus());
                }
                // 套餐翻译
                langKey.add(videoPlanDto.getPackageName());
            }
            Map<String, String> nameMap =
                    this.langApi.getLangValueByKey(langKey, LocaleContextHolder.getLocale().toString());
            // 国际化语言
            list.forEach(
                    o -> {
                        o.setPackageName(nameMap.get(o.getPackageName()));
                    });
        }
        BeanUtils.copyProperties(planDtoPageInfo, page);
        return page;
    }

    /**
     * 描述：查询一段时间的事件列表
     *
     * @param vespVO 查询参数
     * @return java.util.List<com.iot.vo.video.VideoEventVO>
     * @author mao2080@sina.com
     * @created 2018/3/23 14:06
     */
  /* @Override
  public PageInfo<VideoEventVO> getVideoEventList(VideoEventParamVO vespVO) {
      if (null == vespVO) {
          throw new BusinessException(BusinessExceptionEnum.PARAM_ERROR);
      }
      VideoEventParamDto vepDto = new VideoEventParamDto();
      BeanUtils.copyProperties(vespVO, vepDto);
      vepDto.setTenantId(SaaSContextHolder.currentTenantId());
      vepDto.setUserId(SaaSContextHolder.getCurrentUserUuid());
      try {
          PageInfo<VideoEventVO> page = new PageInfo<>();
          BeanUtils.copyProperties(this.videoPlanApi.getVideoEventList(vepDto), page);
          return page;
      } catch (Exception e) {
          LOGGER.error("", e);
          throw new BusinessException(BusinessExceptionEnum.CONVERT_TO_LIST_EXCEPTION, e);
      }
  }*/

    /**
     * 描述：通过用户查询设备，再通过设备查找录影计划,用于云端录影个人模块计划管理及计划列表展示
     *
     * @param
     * @return
     * @author 490485964@qq.com
     * @date 2018/5/11 17:35
     */
    @Override
    public VideoPlanInfoVo getPlanInfoByDevId(PlanParamVo planParamVo) {
        PlanSearchParam param = new PlanSearchParam();
        BeanUtils.copyProperties(planParamVo, param);
        param.setUserId(SaaSContextHolder.getCurrentUserUuid());
        param.setTenantId(SaaSContextHolder.currentTenantId());
        VideoPlanInfoDto videoPlanDto = this.videoManageApi.getPlanInfoByDevId(param);
        GetDeviceInfoRespVo deviceResp =
                deviceCoreServiceApi.getDeviceInfoByDeviceId(param.getDeviceId());
        GetDeviceStatusInfoRespVo deviceStatus =
                deviceCoreServiceApi.getDeviceStatusByDeviceId(
                        deviceResp.getTenantId(), param.getDeviceId());
        VideoPlanInfoVo videoPlanInfoVo = null;
        if (null != videoPlanDto) {
            videoPlanInfoVo = new VideoPlanInfoVo();
            BeanUtils.copyProperties(videoPlanDto, videoPlanInfoVo);
            videoPlanInfoVo.setDayIndex(videoPlanDto.getDayIndex());
            videoPlanInfoVo.setPackageId(
                    SecurityUtil.EncryptByAES(
                            videoPlanDto.getPackageId().toString(), securityKey.getVideoSerivceKey()));
            videoPlanInfoVo.setDeviceName(deviceResp.getName());
            // 在线状态
            videoPlanInfoVo.setOnlineStatus(deviceStatus.getOnlineStatus());
            // 激活状态（0-未激活，1-已激活）
            videoPlanInfoVo.setDeviceStatus(
                    null == deviceStatus.getActiveStatus() ? "0" : "" + deviceStatus.getActiveStatus());
            List<String> langKey = Arrays.asList(videoPlanInfoVo.getPackageName());
            // 套餐名称国际化
            Map<String, String> nameMap =
                    this.langApi.getLangValueByKey(langKey, LocaleContextHolder.getLocale().toString());
            videoPlanInfoVo.setPackageName(nameMap.get(videoPlanInfoVo.getPackageName()));
        }
        return videoPlanInfoVo;
    }

    /**
     * 描述：查询一段时间的事件数量
     *
     * @param vespVO 查询参数
     * @return
     * @author 490485964@qq.com
     * @date 2018/6/6 14:30
     */
    @Override
    public int getVideoEventCount(VideoEventParamVO vespVO) {
        if (null == vespVO) {
            throw new BusinessException(BusinessExceptionEnum.PARAM_ERROR);
        }
        try {
            VideoEventParamDto vepDto = new VideoEventParamDto();
            BeanUtils.copyProperties(vespVO, vepDto);
            vepDto.setTenantId(SaaSContextHolder.currentTenantId());
            vepDto.setUserId(SaaSContextHolder.getCurrentUserUuid());
            return this.videoPlanApi.getVideoEventCount(vepDto);
        } catch (Exception e) {
            LOGGER.error("", e);
            throw new BusinessException(BusinessExceptionEnum.CONVERT_TO_DTO_EXCEPTION, e);
        }
    }

    /**
     * 描述：获取一段时间视频文件列表
     *
     * @param vfpVO
     * @return java.util.List<com.iot.vo.video.VideoFileVO>
     * @author mao2080@sina.com
     * @created 2018/3/23 14:29
     */
    @Override
    public List<VideoFileVO> getVideoFileList(VideoFileParamVO vfpVO) {
        if (null == vfpVO) {
            throw new BusinessException(BusinessExceptionEnum.PARAM_ERROR);
        }
        // 校验计划是否属于当前用户（隐私保护）
        String userId = videoPlanApi.getUserIdByPlanId(vfpVO.getPlanId());
        if (userId == null || !SaaSContextHolder.getCurrentUserUuid().equals(userId)) {
            throw new BusinessException(
                    BusinessExceptionEnum.CHECK_NO_PASS, "this plan isn't belong to you");
        }
        VideoFileParamDto vfpDto = new VideoFileParamDto();
        BeanUtils.copyProperties(vfpVO, vfpDto);
        vfpDto.setTenantId(SaaSContextHolder.currentTenantId());
        vfpDto.setUserId(SaaSContextHolder.getCurrentUserUuid());
        try {
            List<VideoFileDto> videoFileDtoList = this.videoPlanApi.getVideoFileList(vfpDto);
            List<VideoFileVO> videoFileVOList =
                    BeanUtil.listTranslate(videoFileDtoList, VideoFileVO.class);
            if (vfpVO.isNeedUrl()) {
                if (!videoFileVOList.isEmpty()) {
                    List<VideoFileGetUrlReq> reqs = new ArrayList<>();
                    videoFileDtoList.forEach(
                            vo -> {
                                reqs.add(new VideoFileGetUrlReq(vo.getFileId(), vo.getFilePath()));
                            });
                    // 采用多线程去获取url
                    Map<String, String> allFileMap = multiThreadGetUrl(reqs);
                    videoFileVOList.forEach(
                            vo -> {
                                vo.setUrl(allFileMap.get(vo.getFileId()));
                            });
                }
            }
            return videoFileVOList;
        } catch (Exception e) {
            LOGGER.error("getVideoFileList error:", e);
            throw new BusinessException(BusinessExceptionEnum.CONVERT_TO_LIST_EXCEPTION);
        }
    }

    /**
     * 描述：修改录影排序
     *
     * @param poVO 排序对象
     * @return void
     * @author mao2080@sina.com
     * @created 2018/3/23 15:06
     */
    @Override
    public void updatePlanOrder(PlanOrderParamVO poVO) {
        if (null == poVO) {
            throw new BusinessException(BusinessExceptionEnum.PARAM_ERROR);
        }
        PlanOrderParamDto popDto = new PlanOrderParamDto();
        BeanUtils.copyProperties(poVO, popDto);
        popDto.setTenantId(SaaSContextHolder.currentTenantId());
        popDto.setUserId(SaaSContextHolder.getCurrentUserUuid());
        this.videoPlanApi.updatePlanOrder(popDto);
    }

    /**
     * 描述：获取事件图片URL列表
     *
     * @param epVO 查询条件VO
     * @return com.github.pagehelper.PageInfo<com.iot.vo.video.EventVO>
     * @author mao2080@sina.com
     * @created 2018/3/23 15:27
     */
    @Override
    public PageInfo<EventVO> getEventPhotoList(EventParamVO epVO) {
        if (null == epVO) {
            throw new BusinessException(BusinessExceptionEnum.PARAM_ERROR);
        }
        EventParamDto epDto = new EventParamDto();
        BeanUtils.copyProperties(epVO, epDto);
        epDto.setTenantId(SaaSContextHolder.currentTenantId());
        epDto.setUserId(SaaSContextHolder.getCurrentUserUuid());
        PageInfo<EventDto> page = this.videoPlanApi.getEventPhotoList(epDto);
        PageInfo<EventVO> voPage = new PageInfo<>();
        BeanUtil.copyProperties(page, voPage);
        List<EventVO> voList = new ArrayList<>();
        if (null != page && null != page.getList() && !page.getList().isEmpty()) {
            List<VideoFileGetUrlReq> list = new ArrayList<>();
            page.getList()
                    .forEach(
                            vo -> {
                                if (!StringUtil.isBlank(vo.getFileId())) {
                                    list.add(new VideoFileGetUrlReq(vo.getFileId(), vo.getFilePath()));
                                }
                            });
            Map<String, String> files = videoFileApi.getUrlByFilePaths(list);
            page.getList()
                    .forEach(
                            vo -> {
                vo.setUrl(files.get(vo.getFileId()));
                                EventVO eventVO = new EventVO();
                                BeanUtil.copyProperties(vo, eventVO);
                                voList.add(eventVO);
                            });
        }
        voPage.setList(voList);
        return voPage;
    }

    /**
     * 描述：获取计划最后一帧图片
     *
     * @param lppVO 查询条件VO
     * @return java.util.List<com.iot.vo.video.LastPicVO>
     * @author mao2080@sina.com
     * @created 2018/3/23 15:46
     */
    @Override
    public List<LastPicVO> getPlanLastPic(LastPicParamVO lppVO) {
        if (null == lppVO) {
            throw new BusinessException(BusinessExceptionEnum.PARAM_ERROR);
        }
        LastPicParamDto lppDto = new LastPicParamDto();
        BeanUtils.copyProperties(lppVO, lppDto);
        lppDto.setTenantId(SaaSContextHolder.currentTenantId());
        lppDto.setUserId(SaaSContextHolder.getCurrentUserUuid());
        List<LastPicVO> lastPicVOList = new ArrayList<>();
        List<PlanLastPicDto> planLastPicDtoList = this.videoPlanApi.getPlanLastPic(lppDto);
        if (null == planLastPicDtoList || planLastPicDtoList.size() == 0) return lastPicVOList;
        for (PlanLastPicDto planLastPicDto : planLastPicDtoList) {
            // 先从redis查询缓存(规则：file:videoScreen:计划id:文件id  文件对应s3url)
            String planLastPicS3url =
                    videoFileApi.videoScreenshot(
                            planLastPicDto.getFilePath(),
                            planLastPicDto.getDeviceID(),
                            planLastPicDto.getPlanId(),
                            SaaSContextHolder.currentTenantId());
            LastPicVO vo =
                    new LastPicVO(planLastPicDto.getPlanId(), planLastPicS3url, planLastPicDto.getDeviceID());
            lastPicVOList.add(vo);
        }
        return lastPicVOList;
    }

    /**
     * 描述：更新计划名称
     *
     * @param planNameVo 计划名称
     * @return void
     * @author mao2080@sina.com
     * @created 2018/3/26 16:53
     */
    @Override
    public void updatePlanName(PlanNameVo planNameVo) {
        if (StringUtil.isBlank(planNameVo.getPlanId())
                || StringUtil.isBlank(planNameVo.getPlanName())) {
            throw new BusinessException(BusinessExceptionEnum.PARAM_ERROR);
        }
        // 在Header中获取数据
        String userId = SaaSContextHolder.getCurrentUserUuid();
        Long tenantId = SaaSContextHolder.currentTenantId();
        PlanNameParam planNameParam = new PlanNameParam();
        planNameParam.setPlanId(planNameVo.getPlanId());
        planNameParam.setPlanName(planNameVo.getPlanName());
        planNameParam.setUserId(userId);
        planNameParam.setTenantId(tenantId);
        this.videoManageApi.updatePlanName(planNameParam);
    }

    /**
     * O 描述：分页查询购买历史
     *
     * @param bhrlpVO 查询参数VO
     * @return com.github.pagehelper.PageInfo<com.iot.vo.video.BuyHisRecordListVO>
     * @author mao2080@sina.com
     * @created 2018/3/26 17:12
     */
    @Override
    public PageInfo<BuyHisRecordListVO> getBuyHisRecordList(BuyHisRecordListParamVO bhrlpVO) {
        if (null == bhrlpVO) {
            throw new BusinessException(BusinessExceptionEnum.PARAM_ERROR);
        }
        HisRecordSearchParam hrspDto = new HisRecordSearchParam();
        BeanUtils.copyProperties(bhrlpVO, hrspDto);
        hrspDto.setTenantId(SaaSContextHolder.currentTenantId());
        hrspDto.setUserId(SaaSContextHolder.getCurrentUserUuid());
        PageInfo<PayRecordDto> temp = this.videoManageApi.getBuyHisRecordList(hrspDto);
        PageInfo<BuyHisRecordListVO> page = new PageInfo();
        BeanUtil.copyProperties(temp, page);
        return page;
    }

    /**
     * 描述：分页查询购买记录
     *
     * @param bpVO 基础分页VO
     * @return com.github.pagehelper.PageInfo<com.iot.vo.video.BuyRecordListVO>
     * @author mao2080@sina.com
     * @created 2018/3/26 17:20
     */
    @Override
    public PageInfo<BuyRecordListVO> getBuyRecordList(BasePageVO bpVO) {
        if (null == bpVO) {
            throw new BusinessException(BusinessExceptionEnum.PARAM_ERROR);
        }
        RecordSearchParam rspDto = new RecordSearchParam();
        BeanUtils.copyProperties(bpVO, rspDto);
        rspDto.setTenantId(SaaSContextHolder.currentTenantId());
        rspDto.setUserId(SaaSContextHolder.getCurrentUserUuid());
        PageInfo<PayRecordDto> temp = this.videoManageApi.getBuyRecordList(rspDto);
        PageInfo<BuyRecordListVO> page = new PageInfo();
        BeanUtil.copyProperties(temp, page);
        return page;
    }

    /**
     * 描述：购买视频计划
     *
     * @param webPlanVo
     * @param localServerUrl 本服务器的ip、端口
     * @return
     * @author wujianlong
     * @created 2018年3月28日 上午11:34:32
     * @since
     */
    @Override
    public String buyVideoPlan(WebPlanVo webPlanVo, String localServerUrl) {
        Long tenantId = SaaSContextHolder.currentTenantId();
        Long userId = SaaSContextHolder.getCurrentUserId();
        webPlanVo.setTenantId(tenantId);
        webPlanVo.setUserId(SaaSContextHolder.getCurrentUserUuid());
        // 校验参数webPlanVo
        verifyWebPlanVo(webPlanVo);
        String packageIdStr =
                SecurityUtil.DecryptAES(webPlanVo.getPackageId(), securityKey.getVideoSerivceKey());
        Long packageId = Long.valueOf(packageIdStr);
        // 如果有设备id的话
        String deviceId = webPlanVo.getDeviceId();
        if (StringUtil.isNotBlank(deviceId) && StringUtil.isBlank(webPlanVo.getPlanId())) {
            checkBuyPlanWithDeviceId(deviceId);
        }
        // 续费时校验
        if (StringUtil.isNotBlank(webPlanVo.getPlanId())) {
            checkRenewPlan(webPlanVo.getPlanId(), packageId);
        }
        try {
            // 封装订单-商品信息
            CreateOrderRecordReq createOrderRecordReq =
                    new CreateOrderRecordReq(
                            tenantId,
                            userId,
                            webPlanVo.getPayPrice(),
                            webPlanVo.getCurrency(),
                            OrderTypeEnum.VIDEO_PLAN.getCode());
            GoodsExtendServiceReq goodsExtendServiceReq =
                    new GoodsExtendServiceReq(packageId, webPlanVo.getCounts());
            List<GoodsExtendServiceReq> list = new ArrayList<>();
            list.add(goodsExtendServiceReq);
            createOrderRecordReq.setGoodsExtendServiceReq(list);
            // 创建交易记录
            String callbackAddress = serverIp + ":" + callBackPort + callBackUrl;
            CreatePayReq req = new CreatePayReq();
            BeanUtil.copyProperties(webPlanVo, req);
            req.setReturnUrl(callbackAddress);
            req.setCreateOrderRecordReq(createOrderRecordReq);
            req.setGoodsId(packageId);
            // 返回订单id、支付url
            CreatePayResp createPayResp = paymentApi.createPay(req);
            // 存redis
            WebPlanDto webPlanDto = new WebPlanDto();
            BeanUtils.copyProperties(webPlanVo, webPlanDto);
            webPlanDto.setOrderId(createPayResp.getOrderId());
            webPlanDto.setPackageId(packageId);
            this.videoManageApi.saveWebPlan(webPlanDto);
            return createPayResp.getPayUrl();
        } catch (Exception e) {
            LOGGER.error("buyVideoPlan error:", e);
            throw new BusinessException(BusinessExceptionEnum.ERROE, e.getMessage());
        }
    }

    // 校验参数webPlanVo
    private void verifyWebPlanVo(WebPlanVo webPlanVo) {
        if (webPlanVo == null) {
            throw new BusinessException(BusinessExceptionEnum.EMPTY_OF_WEBPLANVO);
        }
        if (StringUtils.isBlank(webPlanVo.getPackageId())) {
            throw new BusinessException(BusinessExceptionEnum.EMPTY_OF_PACKAGEID);
        }
        if (webPlanVo.getCounts() == null || webPlanVo.getCounts() == 0) {
            throw new BusinessException(BusinessExceptionEnum.EMPTY_OF_COUNTS);
        }
        if (webPlanVo.getPayPrice() == null || webPlanVo.getPayPrice().intValue() == 0) {
            throw new BusinessException(BusinessExceptionEnum.EMPTY_OF_PAYPRICE);
        }
        if (StringUtils.isBlank(webPlanVo.getCurrency())) {
            throw new BusinessException(BusinessExceptionEnum.EMPTY_OF_CURRENCY);
        }
    }

    /**
     * 描述：购买视频计划回调
     *
     * @param req
     * @return url 跳转路径
     * @author wujianlong
     * @created 2018年3月31日 上午11:07:19
     * @since
     */
    @Override
    public String buyVideoPlanCallBack(HttpServletRequest req) {
        String url = null;
        WebPlanDto webPlan = null;
        try {
            String payerId = req.getParameter("PayerID");
            String paymentId = req.getParameter("paymentId");
            String orderId = req.getParameter("orderId");
            // 扣款确认
            this.paymentApi.pay(payerId, paymentId);
            webPlan = this.videoManageApi.getWebPlan(orderId);
            PlanParam planParam = new PlanParam();
            BeanUtils.copyProperties(webPlan, planParam);
            // 有计划id就是续费
            if (!StringUtils.isBlank(webPlan.getPlanId())) {
                this.videoManageApi.renewPlan(
                        webPlan.getTenantId(),
                        webPlan.getUserId(),
                        webPlan.getPlanId(),
                        webPlan.getCounts(),
                        webPlan.getOrderId());
                // 通知设备计划
                ipcApi.notifyDevicePlanInfo(webPlan.getPlanId());
            } else {
                // 查询计划套餐名称
                OrderDetailInfoResp orderDetailInfoResp =
                        orderApi.getOrderDetailInfo(orderId, webPlan.getTenantId());
                OrderGoods orderGoods = orderDetailInfoResp.getGoodsList().get(0).getOrderGoods();
                List<String> langKey = new ArrayList<>();
                langKey.add(orderGoods.getGoodsName());
                Map<String, String> nameMap =
                        this.langApi.getLangValueByKey(langKey, LocaleContextHolder.getLocale().toString());
                planParam.setPackageName(nameMap.get(orderGoods.getGoodsName()));
                String planId = this.videoManageApi.createPlan(planParam);
                if (StringUtil.isNotBlank(webPlan.getDeviceId())) {
                    // 如果是通过设备购买计划，则购买成功需绑定上计划
                    SaaSContextHolder.setCurrentUserUuid(webPlan.getUserId());
                    SaaSContextHolder.setCurrentTenantId(webPlan.getTenantId());
                    deviceBusinessService.planBandingDevice(planId, webPlan.getDeviceId());
                }
            }
            url = webPlan.getSuccessUrl();
        } catch (Exception e) {
            LOGGER.error("支付回调地址出错:", e);
            if (webPlan != null) {
                LOGGER.info("支付回调出错的webplan：{}", JsonUtil.toJson(webPlan));
                url = webPlan.getErrorUrl();
            }
        }
        return url;
    }

    /**
     * 描述：根据设备id查询计划类型
     *
     * @param deviceId
     * @return com.lds.iot.video.dto.PlanInfoDto
     * @author 李帅
     * @created 2018年4月16日 下午2:15:40
     * @since
     */
    @Override
    public PlanInfoDto getPlanType(String deviceId) {
        if (StringUtil.isBlank(deviceId)) {
            throw new BusinessException(BusinessExceptionEnum.PARAM_ERROR);
        }
        return this.videoPlanApi.getPlanType(
                SaaSContextHolder.currentTenantId(), SaaSContextHolder.getCurrentUserUuid(), deviceId);
    }

    /**
     * 描述：计划绑定设备
     *
     * @param planId
     * @param deviceId
     * @author 李帅
     * @created 2018年4月27日 下午1:42:16
     * @since
     */
    @Override
    public void planBandingDevice(String planId, String deviceId) {
        if (StringUtil.isBlank(deviceId)) {
            throw new BusinessException(BusinessExceptionEnum.PARAM_ERROR);
        }
        GetDeviceInfoRespVo deviceResp = deviceCoreServiceApi.getDeviceInfoByDeviceId(deviceId);
        if (null == deviceResp) {
            throw new BusinessException(BusinessExceptionEnum.DEVICE_IS_NOT_EXIST);
        }
        GetProductInfoRespVo product = deviceCoreServiceApi.getProductById(deviceResp.getProductId());
        // 校验设备类型是否是IPC
        GetDeviceTypeInfoRespVo deviceTypeResp =
                deviceCoreServiceApi.getDeviceTypeById(product.getDeviceTypeId());
        if (!DeviceTypeConstant.IPC.equals(deviceTypeResp.getType())) {
            throw new BusinessException(BusinessExceptionEnum.CHECK_NO_PASS, " device type is not IPC ");
        }
        // 在Header中获取数据
        Long tenantId = SaaSContextHolder.currentTenantId();
        String userUUId = SaaSContextHolder.getCurrentUserUuid();
        LOGGER.info("planBandingDevice planId->" + planId + "  deviceId->" + deviceId);
        List<String> deviceIdList = new ArrayList<>();
        deviceIdList.add(deviceId);
        List<String> unBindDeviceIdList = videoManageApi.judgeDeviceBandPlan(deviceIdList);
        LOGGER.info("planBandingDevice unBindDeviceIdList ->" + unBindDeviceIdList);
        if (unBindDeviceIdList.contains(deviceId)) {
            // 更新数据库，设置绑定关系
            // 计划是否存在
            boolean exist = videoManageApi.judgePlanExist(planId);
            if (!exist) {
                LOGGER.info("planBandingDevice planId->" + planId + " not exist");
                throw new BusinessException(BusinessExceptionEnum.PLAN_IS_NOT_EXIST);
            }
            this.videoManageApi.planBandingDevice(tenantId, userUUId, planId, deviceId);
            // 通知设备更新
            ipcApi.notifyDeviceRecordConfig(planId, deviceId);
            LOGGER.info("planBandingDevice notifyDeviceRecordConfig planId->" + planId);
        } else {
            throw new BusinessException(BusinessExceptionEnum.PLAN_BANDING_DEVICE_FAILED);
        }
    }

    /**
     * 描述：删除录影事件
     *
     * @param planId
     * @param eventId
     * @author 李帅
     * @created 2018年4月27日 下午1:42:16
     * @since
     */
    @Override
    public void deleteVideoEvent(String planId, String eventId) {
        if (StringUtil.isBlank(eventId)) {
            throw new BusinessException(BusinessExceptionEnum.PARAM_ERROR);
        }
        // 在Header中获取数据
        String userId = SaaSContextHolder.getCurrentUserUuid();
        Long tenantId = SaaSContextHolder.currentTenantId();
        this.videoManageApi.deleteVideoEvent(tenantId, userId, planId, eventId);
    }

    /**
     * @param deviceId 设备id
     * @return
     * @despriction：购买计划时校验设备
     * @author yeshiyuan
     * @created 2018/5/17 15:16
     */
    private void checkBuyPlanWithDeviceId(String deviceId) {
        // 校验设备是否属于当前用户
        Long userId = SaaSContextHolder.getCurrentUserId();
        Long tenantId = SaaSContextHolder.currentTenantId();

        ListUserDeviceInfoRespVo userDeviceInfo =
                deviceCoreServiceApi.getUserDevice(tenantId, userId, deviceId);
        if (userDeviceInfo == null) {
            throw new BusinessException(
                    BusinessExceptionEnum.CHECK_NO_PASS, "this device isn't belong to you");
        }
        GetDeviceInfoRespVo deviceResp = deviceCoreServiceApi.getDeviceInfoByDeviceId(deviceId);
        GetProductInfoRespVo product = deviceCoreServiceApi.getProductById(deviceResp.getProductId());
        // 需校验设备类型是否是IPC
        GetDeviceTypeInfoRespVo deviceTypeResp =
                deviceCoreServiceApi.getDeviceTypeById(product.getDeviceTypeId());
        if (!DeviceTypeConstant.IPC.equals(deviceTypeResp.getType())) {
            throw new BusinessException(
                    BusinessExceptionEnum.CHECK_NO_PASS, "this device type isn't " + DeviceTypeConstant.IPC);
        }
        // 校验设备是否已绑定计划
        boolean result = videoManageApi.checkDeviceHasBindPlan(deviceId);
        if (result) {
            throw new BusinessException(
                    BusinessExceptionEnum.CHECK_NO_PASS, "this device has binding plan !");
        }
    }

    /**
     * @param planId 计划id
     * @return
     * @despriction：计划续费校验
     * @author yeshiyuan
     * @created 2018/5/17 15:20
     */
    private void checkRenewPlan(String planId, Long packageId) {
        // 判断计划是否存在
        VideoPlan videoPlan =
                videoPlanApi.getVideoPlanDetail(planId, SaaSContextHolder.getCurrentUserUuid());
        if (videoPlan == null) {
            throw new BusinessException(
                    BusinessExceptionEnum.CHECK_NO_PASS, "this plan isn't belong to you");
        }
        if (packageId.longValue() != videoPlan.getPackageId()) {
            throw new BusinessException(
                    BusinessExceptionEnum.CHECK_NO_PASS, "packageId is not the same as before");
        }
    }

    /**
     * @param req
     * @return
     * @despriction：通过事件id获取事件对应的视频文件
     * @author yeshiyuan
     * @created 2018/6/25 9:28
     */
    @Override
    public List<VideoFileVO> getVideoTsFileListByEventUuid(GetEventVideoFileReq req) {
        if (req == null || StringUtil.isBlank(req.getPlanId())) {
            throw new BusinessException(BusinessExceptionEnum.PARAM_ERROR, "planId is null");
        }
        // 校验计划是否属于当前用户（隐私保护）
        String userId = videoPlanApi.getUserIdByPlanId(req.getPlanId());
        if (userId == null || !SaaSContextHolder.getCurrentUserUuid().equals(userId)) {
            throw new BusinessException(
                    BusinessExceptionEnum.CHECK_NO_PASS, "this plan isn't belong to you");
        }
        req.setFileType("ts");
        List<VideoTsFileDto> list = videoManageApi.getVideoFileListByEventUuid(req);
        List<VideoFileVO> vos = null;
        try {
            vos = BeanUtil.listTranslate(list, VideoFileVO.class);
        } catch (Exception e) {
            LOGGER.error("getVideoTsFileListByEventUuid listTranslate error", e);
        }
        // 获取视频播放地址
        if (vos != null && !vos.isEmpty()) {
            List<VideoFileGetUrlReq> reqs = new ArrayList<>();
            for (VideoTsFileDto videoTsFileDto : list) {
                reqs.add(new VideoFileGetUrlReq(videoTsFileDto.getFileId(), videoTsFileDto.getFilePath()));
            }
            Map<String, String> files = this.videoFileApi.getUrlByFilePaths(reqs);
            vos.forEach(
                    vo -> {
                        vo.setUrl(files.get(vo.getFileId()));
                    });
        }
        return vos;
    }

    /**
     * @param reqs
     * @return
     * @despriction：多线程获取url
     * @author yeshiyuan
     * @created 2018/6/27 11:03
     */
    private Map<String, String> multiThreadGetUrl(List<VideoFileGetUrlReq> reqs)
            throws ExecutionException, InterruptedException {
        List<List<VideoFileGetUrlReq>> list = new ArrayList<>();
        if (reqs.size() > 300) {
            list = this.splitVideoFileList(reqs, 300);
        } else {
            list.add(reqs);
        }
        List<FutureTask> futureTasks = new ArrayList<>();
        for (List<VideoFileGetUrlReq> reqList : list) {
            FutureTask<Map<String, String>> futureTask =
                    new FutureTask(
                            new Callable() {
                                @Override
                                public Map<String, String> call() throws Exception {
                                    return videoFileApi.getUrlByFilePaths(reqList);
                                }
                            });
            executorService.submit(futureTask);
            futureTasks.add(futureTask);
        }
        Map<String, String> allFileMap = new HashMap<>();
        for (FutureTask futureTask : futureTasks) {
            while (true) {
                if (futureTask.isDone()) {
                    allFileMap.putAll((Map<String, String>) futureTask.get());
                    break;
                }
            }
        }
        return allFileMap;
    }

    /**
     * @return
     * @despriction：统计IPC录影日期
     * @author yeshiyuan
     * @created 2018/7/26 10:56
     */
    @Override
    public List<String> countVideoDate(CountVideoDateReq req) {
        return videoManageApi.countVideoDate(req);
    }
}
