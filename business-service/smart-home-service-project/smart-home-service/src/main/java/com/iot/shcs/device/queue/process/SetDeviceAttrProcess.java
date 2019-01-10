package com.iot.shcs.device.queue.process;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.iot.common.enums.UserType;
import com.iot.common.exception.BusinessException;
import com.iot.common.exception.RemoteCallBusinessException;
import com.iot.common.mq.process.AbsMessageProcess;
import com.iot.control.activity.api.ActivityRecordApi;
import com.iot.control.activity.vo.req.ActivityRecordReq;
import com.iot.control.device.vo.resp.ListUserDeviceInfoRespVo;
import com.iot.device.api.DeviceStateCoreApi;
import com.iot.device.vo.req.device.UpdateDeviceStatusReq;
import com.iot.device.vo.rsp.device.GetDeviceInfoRespVo;
import com.iot.device.vo.rsp.device.GetDeviceStatusInfoRespVo;
import com.iot.shcs.common.util.DeviceUtils;
import com.iot.shcs.device.enums.OnlineStatusEnum;
import com.iot.shcs.device.queue.bean.SetDeviceAttrMessage;
import com.iot.shcs.device.service.impl.DeviceCoreService;
import com.iot.shcs.device.service.impl.DeviceService;
import com.iot.shcs.device.service.impl.PushMessageService;
import com.iot.shcs.helper.Constants;
import com.iot.shcs.ifttt.service.IAutoService;
import com.iot.tenant.api.UserVirtualOrgApi;
import com.iot.tenant.vo.resp.UserDefaultOrgInfoResp;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Author: lucky @Descrpiton: @Date: 15:34 2018/10/11 @Modify by:
 */
@Slf4j
@Component
public class SetDeviceAttrProcess extends AbsMessageProcess<SetDeviceAttrMessage> {
  @Autowired
  private DeviceCoreService deviceCoreService;
  @Autowired
  private DeviceService deviceService;
  @Autowired
  private UserVirtualOrgApi userVirtualOrgApi;
  @Autowired
  private ActivityRecordApi activityRecordApi;
  @Autowired
  private PushMessageService pushMessageService;
  @Autowired
  private IAutoService autoService;
  @Autowired
  private DeviceStateCoreApi deviceStateCoreApi;

  public void processMessage(SetDeviceAttrMessage message) {
    log.debug("receive.queue.....SetDeviceAttrProcess.{}", JSON.toJSONString(message));
    Map<String, Object> payload = message.getPayload();
    Map<String, Object> attrMap = message.getAttrMap();
    String subDeviceId = message.getSubDeviceId();
    Object online = message.getOnline();
    Long tenantId = message.getTenantId();
    String deviceId = message.getParentDeviceId();

    long st = new Date().getTime();
    try {

      GetDeviceInfoRespVo device = deviceCoreService.getDeviceInfoByDeviceId(subDeviceId);
      if (device == null) {
        log.error("setDevAttrNotif device not exist:{}", deviceId);
        return;
      }

      //能收到状态通知说明这个设备是在线,避免网关报离线还可控的问题
      GetDeviceStatusInfoRespVo deviceStatus = deviceCoreService.getDeviceStatusByDeviceId(tenantId, subDeviceId);
      if (deviceStatus == null || (!OnlineStatusEnum.CONNECTED.getCode().equalsIgnoreCase(deviceStatus.getOnlineStatus()))) {
        //非在线需要更新成在线
        UpdateDeviceStatusReq updateDeviceStatusParam = UpdateDeviceStatusReq.builder()
                .deviceId(subDeviceId).tenantId(tenantId).onlineStatus(OnlineStatusEnum.CONNECTED.getCode())
                .build();
        deviceCoreService.saveOrUpdateDeviceStatus(updateDeviceStatusParam);
      }



      if (attrMap != null && attrMap.containsKey("Smoke") && attrMap.get("Smoke") != null){
        Map map = deviceStateCoreApi.get(tenantId, device.getUuid());
        if (map!=null && map.get("Smoke")!=null){
          device.setLastState(map.get("Smoke").toString());
        }
      }

      // 添加到活动记录表
      deviceService.insertDeviceStatus(device, attrMap);

      // IFTTT处理 TODO
      autoService.checkByDevice(subDeviceId, attrMap);

      String icon = device.getIcon();
      Long rootUserId = null;
      List<ListUserDeviceInfoRespVo> userDeviceInfoRespList = deviceCoreService.listUserDevices(tenantId, deviceId);

      if (!CollectionUtils.isEmpty(userDeviceInfoRespList)) {
        for (ListUserDeviceInfoRespVo userDeviceInfoResp : userDeviceInfoRespList) {
          Long userId = userDeviceInfoResp.getUserId();
          String userType = userDeviceInfoResp.getUserType();
          if (UserType.ROOT.getName().equals(userType)) {
            rootUserId = userId;
            break;
          }
        }
      }
      if (rootUserId == null) {
        log.debug("****** setDevAttrNotif() end. because rootUserId is null.");
        return;
      }

      if (!CollectionUtils.isEmpty(attrMap)) {

        UserDefaultOrgInfoResp userOrgInfoResp =
                userVirtualOrgApi.getDefaultUsedOrgInfoByUserId(rootUserId);

        Long orgId = userOrgInfoResp != null ? userOrgInfoResp.getId() : null;
        List<ActivityRecordReq> list = Lists.newArrayList();
        for (Map.Entry<String, Object> entry : attrMap.entrySet()) {
          String info = DeviceUtils.changeValue(device.getName(), entry.getKey(), entry.getValue().toString());
          if (StringUtils.isEmpty(info)) {
            continue;
          }
          ActivityRecordReq activityRecordReq = new ActivityRecordReq();
          activityRecordReq.setCreateBy(rootUserId);
          activityRecordReq.setType(Constants.ACTIVITY_RECORD_DEVICE);
          if (StringUtils.isEmpty(icon)) {
            activityRecordReq.setIcon("default");
          }
          activityRecordReq.setIcon(icon);
          activityRecordReq.setActivity(info);
          activityRecordReq.setForeignId(subDeviceId);
          activityRecordReq.setDeviceName(device.getName());
          activityRecordReq.setTenantId(tenantId);
          activityRecordReq.setOrgId(orgId);
          list.add(activityRecordReq);
        }

        if (!CollectionUtils.isEmpty(list)) {
          activityRecordApi.saveActivityRecord(list);
        }
      }
      // 处理 推送消息给app
      pushMessageService.dealPushMessage2App(device, attrMap, rootUserId);



    } catch (RemoteCallBusinessException e) {
      log.debug("setDevAttrNotif-remote-error.:{}", e);
    } catch (BusinessException e) {
      log.debug("setDevAttrNotif-local-error.:{}", e);
    } catch (Exception e) {
      log.error("setDevAttrNotif-error.", e);
    }

    long et = System.currentTimeMillis();
    long consumerTime  = et - st;
    log.info("setDevAttrNotif.execute.time {}",consumerTime);
  }

  //属性上报实际上云不太关注需要干什么 最终只记录到 device_state 和对应日志记录里 无需关心他有哪些具体的属性
  private void processPorperties(SetDeviceAttrMessage message) {
    try {
      log.debug("SetDeviceAttrProcess-processPorperties....begin");
      Map<String, Object> payload = message.getPayload();
      Map<String, Object> attrMap = message.getAttrMap();
      String subDeviceId = message.getSubDeviceId();
      Object online = message.getOnline();
      Long tenantId = message.getTenantId();
      String deviceId = message.getParentDeviceId();

      if (attrMap.containsKey("OnOff")) {//功能组：OnOffControl
        log.debug("SetDeviceAttrProcess-processPorperties....begin...OnOff");
//        属性名称：OnOff
//        属性类型：bool
//        属性值：  0/1
//        描述：    0 = off, 1 = on
//        读写特性：R/W
      }
      if (attrMap.containsKey("SingleEnergy")) {//功能组：EnergyManage
        log.debug("SetDeviceAttrProcess-processPorperties....begin...SingleEnergy");
//        属性类型：double
//        属性值：  0.0~99999999.9
//        描述：    单次电量
//        读写特性：R
      }
      if (attrMap.containsKey("PeriodEnergy")) {//功能组：EnergyReportManage //上报电量事件，时间段内的用电量    参数1：step
        log.debug("SetDeviceAttrProcess-processPorperties....begin...PeriodEnergy");
//        参数名称：step
//        参数类型：int
//        描述：周期的步进阶段时间
//        参数2：time
//        参数名称：time
//        参数类型：int
//        描述：当前上报的UTC时间
//        参数3：value
//        参数名称：value
//        参数类型：double
//        描述：上报的电量
//        参数4：area
//        参数名称：area
//        参数类型：string
//        描述：上报电量的地区


      }
      if (attrMap.containsKey("SingleRuntime")) {//功能组：RuntimeManage
        log.debug("SetDeviceAttrProcess-processPorperties....begin...SingleRuntime");
//        属性名称：SingleRuntime
//        属性类型：int
//        属性值：  0~99999999
//        描述：    单次运行时间
//        读写特性：R
      }
      if (attrMap.containsKey("PeriodRuntime")) {//功能组：RuntimeReportManage
        log.debug("SetDeviceAttrProcess-processPorperties....begin...PeriodRuntime");
//        属性类型：int
//        属性值：  0~99999999
//        描述：    周期运行时间
//        读写特性：R
      }
      if (attrMap.containsKey("CountDown")) {//功能组：CounterManage
        log.debug("SetDeviceAttrProcess-processPorperties....begin...CountDown");
//        属性类型：int
//        属性值：  0~43200
//        描述：    剩余倒计时时间，不超过12H，单位S
//        读写特性：R/W
      }

      if (attrMap.containsKey("CountDownEnable")) {//功能组：CounterEnableManage
        log.debug("SetDeviceAttrProcess-processPorperties....begin...CountDownEnable");
//        属性类型：bool
//        属性值：  0 = disable, 1 = enable
//        描述：    倒计时使能
//        读写特性：R/W

      }

      if (attrMap.containsKey("UserModeEnable")) {//功能组：SettingManage
        log.debug("SetDeviceAttrProcess-processPorperties....begin...UserModeEnable");
//        属性类型：bool
//        属性值：  0 = disable, 1 = enable
//        描述：    用户模式使能
//        读写特性：R/W
      }
    } catch (Exception e) {
      log.error("SetDeviceAttrProcess-processPorperties....error.", e);
    }

    log.debug("SetDeviceAttrProcess-processPorperties....end");
  }
}
