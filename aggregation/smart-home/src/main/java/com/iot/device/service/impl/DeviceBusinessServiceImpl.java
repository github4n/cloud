package com.iot.device.service.impl;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.iot.common.beans.BeanUtil;
import com.iot.common.beans.CommonResponse;
import com.iot.common.exception.BusinessException;
import com.iot.common.helper.Page;
import com.iot.common.util.StringUtil;
import com.iot.constant.DeviceTypeConstant;
import com.iot.constant.SpaceTypeConstant;
import com.iot.control.device.vo.resp.ListUserDeviceInfoRespVo;
import com.iot.control.space.api.SpaceDeviceApi;
import com.iot.control.space.vo.SpaceAndSpaceDeviceVo;
import com.iot.control.space.vo.SpaceDeviceVo;
import com.iot.device.api.DeviceCoreApi;
import com.iot.device.api.DeviceExtendsCoreApi;
import com.iot.device.api.DeviceTypeApi;
import com.iot.device.service.DeviceBusinessService;
import com.iot.device.utils.BeanCopyUtils;
import com.iot.device.vo.DeviceVo;
import com.iot.device.vo.req.DevTypePageReq;
import com.iot.device.vo.rsp.DeviceResp;
import com.iot.device.vo.rsp.DeviceTypeResp;
import com.iot.device.vo.rsp.device.GetDeviceExtendInfoRespVo;
import com.iot.device.vo.rsp.device.ListDeviceInfoRespVo;
import com.iot.exception.BusinessExceptionEnum;
import com.iot.saas.SaaSContextHolder;
import com.iot.shcs.device.api.DeviceCoreServiceApi;
import com.iot.shcs.device.api.SmartHomeDeviceCoreApi;
import com.iot.shcs.ipc.api.IpcApi;
import com.iot.video.api.VideoManageApi;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/** 项目名称：IOT云平台 模块名称： 功能描述： 创建人： 创建时间：2018/3/20 17:26 修改人： 修改时间：2018/3/20 17:26 修改描述： */
@Slf4j
@Service("deviceBusiness")
public class DeviceBusinessServiceImpl implements DeviceBusinessService {

  /** IPC API */
  @Autowired private IpcApi ipcApi;

  /** 视频管理API */
  @Autowired private VideoManageApi videoManageApi;


  @Autowired
  private DeviceCoreApi deviceCoreApi;

  @Autowired
  private DeviceTypeApi deviceTypeApi;

  @Autowired
  private DeviceCoreServiceApi deviceCoreService;

  @Autowired
  private DeviceExtendsCoreApi deviceExtendsCoreApi;

    @Autowired
    private SmartHomeDeviceCoreApi smartHomeDeviceCoreApi;

  /** 房间API */
  @Autowired private SpaceDeviceApi spaceDeviceApi;

  /**
   * 描述：计划绑定设备
   *
   * @param
   * @return
   * @author 490485964@qq.com
   * @date 2018/4/8 15:11
   */
  @Override
  public void planBandingDevice(String planId, String deviceId) {
    log.info("planBandingDevice planId->" + planId + "  deviceId->" + deviceId);
    List<String> deviceIdList = new ArrayList<>();
    deviceIdList.add(deviceId);
    List<String> unBindDeviceIdList = videoManageApi.judgeDeviceBandPlan(deviceIdList);
    log.info("planBandingDevice unBindDeviceIdList ->" + unBindDeviceIdList);
    if (unBindDeviceIdList.contains(deviceId)) {
      // 更新数据库，设置绑定关系
      Long tenantId = SaaSContextHolder.currentTenantId();
      String userUUId = SaaSContextHolder.getCurrentUserUuid();
      // 计划是否存在
      boolean exist = videoManageApi.judgePlanExist(planId);
      if (!exist) {
        log.info("planBandingDevice planId->" + planId + " not exist");
        throw new BusinessException(BusinessExceptionEnum.PLAN_IS_NOT_EXIST);
      }
      this.videoManageApi.planBandingDevice(tenantId, userUUId, planId, deviceId);
      // 通知设备更新
      ipcApi.notifyDeviceRecordConfig(planId, deviceId);
      log.info("planBandingDevice notifyDeviceRecordConfig planId->" + planId);
    } else {
      throw new BusinessException(BusinessExceptionEnum.PLAN_BANDING_DEVICE_FAILED);
    }
  }

  /**
   * 描述：获取文件服务put预签名url
   *
   * @author 李帅
   * @created 2018年7月17日 上午9:33:06
   * @since
   * @param planId
   * @param fileType
   * @return
   */
  @Override
  public Map<String, Object> getFilePreSignUrls(
      String sslClientSDn, String planId, String fileType) {
    log.info(
        "getFilePreSignUrls ssl_client_s_dn->"
            + sslClientSDn
            + "  planId->"
            + planId
            + "  fileType->"
            + fileType);
    String[] strs = sslClientSDn.split(",");
    Map<String, String> CNMap = new HashMap<String, String>();
    for (String str : strs) {
      String[] strArray = str.split("=");
      CNMap.put(strArray[0], strArray[1]);
    }
    Map<String, Object> map = ipcApi.getFilePreSignUrls(CNMap.get("CN"), planId, fileType);
    return map;
  }

  @Override
  public List<DeviceVo> getDeviceList() {
    long userId = SaaSContextHolder.getCurrentUserId();
    Long tenantId = SaaSContextHolder.currentTenantId();

    List<DeviceResp> deviceInfoList = deviceCoreService.findDirectDeviceListByUserId(tenantId, userId);

    List<DeviceVo> deviceVoList;
    try {
      deviceVoList = BeanUtil.listTranslate(deviceInfoList, DeviceVo.class);
    } catch (Exception e) {
      log.error("getDeviceList->", e);
      throw new BusinessException(BusinessExceptionEnum.GET_DEVICELISET_FAILED, e);
    }
    return deviceVoList;
  }

  @Override
  public Map<String, Object> getDevList(Long tenantId, Long userId, Long homeId) {
      return smartHomeDeviceCoreApi.getDevList(tenantId, userId, homeId);
  }

  @Override
  public Boolean sortDev(List<String> deviceIds) {
    Long tenantId = SaaSContextHolder.currentTenantId();
    return smartHomeDeviceCoreApi.sortDev(tenantId, deviceIds);
  }

  /**
   * 描述：通过设备Id 查询p2pId
   *
   * @return
   * @author 李帅
   * @created 2018年5月11日 下午1:58:51
   * @since
   */
  @Override
  public String getP2pId(String deviceId) {
    GetDeviceExtendInfoRespVo deviceExtendInfoRespVo = deviceExtendsCoreApi.get(SaaSContextHolder.currentTenantId(), deviceId);
    if (deviceExtendInfoRespVo != null) {
      return deviceExtendInfoRespVo.getP2pId();
    }
    return null;
  }

  @Override
  public List<DeviceVo> getUnBindPlanDeviceList() {
    List<DeviceVo> deviceVoRetList = new ArrayList<DeviceVo>();
    Long userId = SaaSContextHolder.getCurrentUserId();
    Long tenantId = SaaSContextHolder.currentTenantId();
    List<String> typeList = new ArrayList<>();
    typeList.add(DeviceTypeConstant.IPC);
    List<String> deviceIds = Lists.newArrayList();

    List<ListUserDeviceInfoRespVo> userDeviceInfoRespVoList = deviceCoreService.listUserDevicesByUserId(tenantId, userId);
    if (CollectionUtils.isEmpty(userDeviceInfoRespVoList)) {
      return deviceVoRetList;
    }
    userDeviceInfoRespVoList.forEach(userDevice -> {
      deviceIds.add(userDevice.getDeviceId());
    });
    DevTypePageReq devTypePageReq = DevTypePageReq.builder().deviceIds(deviceIds)
            .tenantId(tenantId)
            .deviceTypeList(typeList).pageNum(DeviceTypeConstant.PAGENUM).pageSize(DeviceTypeConstant.PAGESIZE).build();
    // 获取该用户绑定的IPC设备
    Page<DeviceResp> deviceRespPage = deviceCoreApi.pageDeviceByDeviceTypeList(devTypePageReq);
    List<DeviceResp> deviceInfoList = null;
    if (null != deviceRespPage) {
      deviceInfoList = deviceRespPage.getResult();
    }
    log.debug("getUnBindPlanDeviceList findDevPageByDeviceTypeList->" + deviceInfoList);
    if (null == deviceInfoList || deviceInfoList.size() == 0) {
      return deviceVoRetList;
    }
    List<String> deviceIdList = new ArrayList<>();
    for (DeviceResp deviceResp : deviceInfoList) {
      deviceIdList.add(deviceResp.getDeviceId());
    }
    // 获取没有绑定计划的设备的ID列表
    List<String> unBindDeviceIdList = videoManageApi.judgeDeviceBandPlan(deviceIdList);
    log.info("getUnBindPlanDeviceList unBindDeviceIdList->" + unBindDeviceIdList);
    if (null == unBindDeviceIdList || unBindDeviceIdList.size() == 0) {
      return deviceVoRetList;
    }
    List<DeviceVo> deviceVoList = null;
    try {
      deviceVoList = BeanUtil.listTranslate(deviceInfoList, DeviceVo.class);
    } catch (Exception e) {
      log.error("getUnBindPlanDeviceList->", e);
      throw new BusinessException(BusinessExceptionEnum.GET_UNBIND_PLAN_DEVICELIST_FAILED, e);
    }
    // 过滤出未绑定计划的设备信息
    String deviceId = null;
    if (null != deviceVoList && deviceVoList.size() > 0) {
      for (DeviceVo deviceVo : deviceVoList) {
        deviceId = deviceVo.getDeviceId();
        if (unBindDeviceIdList.contains(deviceId)) {
          deviceVoRetList.add(deviceVo);
        }
      }
    }

    SpaceAndSpaceDeviceVo req = new SpaceAndSpaceDeviceVo();
    req.setDeviceIds(unBindDeviceIdList);
    req.setTenantId(SaaSContextHolder.currentTenantId());
    List<SpaceDeviceVo> spaceDeviceVoList = spaceDeviceApi.findSpaceDeviceInfoByDeviceIds(req);
    Map<String, String> spaceRespMap = new HashMap<String, String>();
    for (SpaceDeviceVo spaceResp : spaceDeviceVoList) {
      spaceRespMap.put(spaceResp.getDeviceId() + spaceResp.getType(), spaceResp.getName());
    }
    for (DeviceVo deviceVo : deviceVoRetList) {
      deviceVo.setSpaceName(spaceRespMap.get(deviceVo.getDeviceId() + SpaceTypeConstant.HOME));
      deviceVo.setRoomName(spaceRespMap.get(deviceVo.getDeviceId() + SpaceTypeConstant.ROOM));
    }
    return deviceVoRetList;
  }

  @Override
  public List<DeviceResp> getDeviceListByDirectDevice(String deviceId) {
    log.info("***getDeviceListByDirectDevice deviceId->" + deviceId);
    if(StringUtil.isEmpty(deviceId)){
      throw new BusinessException(com.iot.BusinessExceptionEnum.COMMOMN_EXCEPTION);
    }
    List<ListDeviceInfoRespVo> devList= deviceCoreApi.listDevicesByParentId(deviceId);
    List<DeviceResp> deviceRespList=new ArrayList<>();
    if(!CollectionUtils.isEmpty(devList)){
      BeanCopyUtils.copyDeviceInfoList(devList,deviceRespList);
      for(DeviceResp deviceResp:deviceRespList){
        DeviceTypeResp deviceTypeResp=deviceTypeApi.getDeviceTypeById(deviceResp.getDeviceTypeId());
        deviceResp.setDevType(deviceTypeResp.getType());
      }
      log.info("***getDeviceListByDirectDevice deviceRespList->" + JSON.toJSONString(deviceRespList));
    }
     return deviceRespList;


  }

}
