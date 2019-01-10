package com.iot.shcs.device.queue.process;

import com.google.common.collect.Maps;
import com.iot.common.enums.UserType;
import com.iot.common.mq.process.AbsMessageProcess;
import com.iot.common.util.StringUtil;
import com.iot.control.device.vo.resp.ListUserDeviceInfoRespVo;
import com.iot.device.vo.req.device.UpdateDeviceStatusReq;
import com.iot.device.vo.rsp.device.GetDeviceInfoRespVo;
import com.iot.mqttsdk.common.MqttMsg;
import com.iot.mqttsdk.common.MqttMsgAck;
import com.iot.shcs.device.enums.OnlineStatusEnum;
import com.iot.shcs.device.queue.bean.DevEventMessage;
import com.iot.shcs.device.queue.utils.EventConstants;
import com.iot.shcs.device.service.impl.*;
import com.iot.shcs.ifttt.service.IAutoService;
import com.iot.tenant.api.UserVirtualOrgApi;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Author: lucky @Descrpiton: @Date: 15:34 2018/10/11 @Modify by:
 */
@Slf4j
@Component
public class DevEventProcess extends AbsMessageProcess<DevEventMessage> {

  @Autowired
  private MqttCoreService mqttCoreService;
  @Autowired
  private DeviceCoreService deviceCoreService;
  @Autowired
  private DeviceService deviceService;
  @Autowired
  private UserVirtualOrgApi userVirtualOrgApi;
  @Autowired
  private PushMessageService pushMessageService;
  @Autowired
  private IAutoService autoService;
  @Autowired
  @Qualifier("device")
  private DeviceMQTTService deviceMQTTService;

  public void processMessage(DevEventMessage message) {
    log.info("receive.queue.....DevEventProcess.{}", message.toString());

    String event = message.getEvent();
    try {
      if (StringUtil.isEmpty(event)) {
        log.debug("DevEventProcess event not null");
        return;
      }
      switch (event) {
        case EventConstants.COUNT_DOWN_EXEC_EVENT://倒计时功能事件
          //
          countDownExecEvent(message);
          break;
        case EventConstants.UPDATE_ENERGY_EVENT://电量报表功能事件
          updateEnergyEvent(message);
          break;
        case EventConstants.UPDATE_RUNTIME_EVENT://运行时间报表功能事件
          updateRuntimeEvent(message);
          break;

        case EventConstants.TAMPER_EVENT://防拆报警功能事件
          dealTamperEvent(message);
          break;
      }


    } catch (Exception e) {
      log.info("devEventNotif-error.", e);
    }

  }

  /**
   * 运行时间报表功能事件
   *
   * @param
   * @return
   * @author lucky
   * @date 2018/10/29 18:00
   */
  private void updateRuntimeEvent(DevEventMessage message) {
    log.debug("updateRuntimeEvent run content:{}", message.toString());
    MqttMsgAck ack = MqttMsgAck.successAck();
    try {
      List<Object> arguments = message.getArguments();
      if (!CollectionUtils.isEmpty(arguments)) {
        Integer step = (Integer) arguments.get(0);
        Date time = new Date((Long.parseLong(arguments.get(1) + "")));
        Long runtimeValue = (Long.parseLong(arguments.get(2) + ""));
        deviceMQTTService.commRuntimeProcess(message.getDevId(), step, time, runtimeValue);
      }
    } catch (Exception e) {
      ack.setCode(MqttMsgAck.BUSINESS_ERROR);
      ack.setDesc("Fail.updateRuntimeEvent-error. " + e);
      log.error("updateRuntimeEvent-error", e);
    } finally {// 响应给设备
      MqttMsg updateRuntimeRespMsg = new MqttMsg();
      updateRuntimeRespMsg.setService("device");
      updateRuntimeRespMsg.setMethod("updateRuntimeResp");
      updateRuntimeRespMsg.setSeq(message.getSeq());
      updateRuntimeRespMsg.setSrcAddr(message.getSrcAddress());
      Map<String, Object> respPayload = Maps.newHashMap();
      updateRuntimeRespMsg.setPayload(respPayload);
      updateRuntimeRespMsg.setAck(ack);
      String updateRuntimeRespTopic = "iot/v1/c/" + message.getParentDeviceId() + "/device/updateRuntimeResp";
      updateRuntimeRespMsg.setTopic(updateRuntimeRespTopic);
      log.debug("updateRuntimeResp-转发MQTT到设备({}, {})", updateRuntimeRespMsg, updateRuntimeRespTopic);

      mqttCoreService.sendMessage(updateRuntimeRespMsg);
    }
  }

  /**
   * 电量报表功能事件
   *
   * @param
   * @return
   * @author lucky
   * @date 2018/10/29 18:00
   */
  private void updateEnergyEvent(DevEventMessage message) {
    log.debug("updateEnergyEvent run content:{}", message.toString());
    MqttMsgAck ack = MqttMsgAck.successAck();
    try {
      List<Object> arguments = message.getArguments();
      if (!CollectionUtils.isEmpty(arguments)) {
        Integer step = (Integer) arguments.get(0);
        Date time = new Date((Long.parseLong(arguments.get(1) + "")));
        Double electricValue =
                arguments.get(2) != null ? Double.valueOf(arguments.get(2) + "") : null;
        String area = arguments.get(3) + "";
        deviceMQTTService.commEnergyProcess(message.getDevId(), step, time, electricValue, area);
      }
    } catch (Exception e) {
      ack.setCode(MqttMsgAck.BUSINESS_ERROR);
      ack.setDesc("Fail.updateEnergyEvent-error. " + e);
      log.error("updateEnergyEvent-error", e);
    } // 响应给设备
    finally {
      MqttMsg updateEnergyRespMsg = new MqttMsg();
      updateEnergyRespMsg.setService("device");
      updateEnergyRespMsg.setMethod("updateEnergyResp");
      updateEnergyRespMsg.setSeq(message.getSeq());
      updateEnergyRespMsg.setSrcAddr(message.getSrcAddress());
      Map<String, Object> respPayload = Maps.newHashMap();
      updateEnergyRespMsg.setPayload(respPayload);
      updateEnergyRespMsg.setAck(ack);

      String updateEnergyRespTopic = "iot/v1/c/" + message.getParentDeviceId() + "/device/updateEnergyResp";
      log.debug("updateEnergyEvent-转发MQTT到设备({}, {})", updateEnergyRespMsg, updateEnergyRespTopic);
      updateEnergyRespMsg.setTopic(updateEnergyRespTopic);

      mqttCoreService.sendMessage(updateEnergyRespMsg);
    }
  }

  /**
   * 倒计时功能事件
   *
   * @param
   * @return
   * @author lucky
   * @date 2018/10/29 17:59
   */
  private void countDownExecEvent(DevEventMessage message) {
    log.debug("countDownExecEvent run content:{}", message.toString());

    List<Object> arguments = message.getArguments();
    String devId = message.getDevId();
    Long tenantId = message.getTenantId();
    String directDevId = message.getParentDeviceId();
    if (!CollectionUtils.isEmpty(arguments)) {
      // TODO
      arguments.forEach(argument -> {
        log.debug("countDownExecEvent process:{}", arguments);
      });
    }
  }

  private void dealTamperEvent(DevEventMessage message) {
    List<Object> arguments = message.getArguments();
    String devId = message.getDevId();
    Long tenantId = message.getTenantId();
    String directDevId = message.getParentDeviceId();
    if (!CollectionUtils.isEmpty(arguments)) {
      Map<String, Object> attrMap = Maps.newHashMap();
      arguments.forEach(argument -> {
        //防拆报警处理
        if ("1".equals(argument)) {
          attrMap.put("Tamper", argument);
          //处理设备用户关系及消息推送
          GetDeviceInfoRespVo device = deviceCoreService.getDeviceInfoByDeviceId(devId);
          if (device.getParentId() != null && !device.getParentId().equals(directDevId)) {
            log.error("devEventNotif-parentId-error.orig-parentId:{},nowParentId:{}", device.getParentId(), directDevId);
            return;
          }

          //能收到状态通知说明这个设备是在线,避免网关报离线还可控的问题
          UpdateDeviceStatusReq updateDeviceStatusParam = UpdateDeviceStatusReq.builder()
                  .deviceId(devId).tenantId(tenantId).onlineStatus(OnlineStatusEnum.CONNECTED.getCode())
                  .build();
          deviceCoreService.saveOrUpdateDeviceStatus(updateDeviceStatusParam);
          //设备上报判断触发
//      autoService.checkByDevice(subDeviceId, attrMap);

          Long rootUserId = null;
          List<ListUserDeviceInfoRespVo> userDeviceInfoRespList = deviceCoreService.listUserDevices(tenantId, directDevId);

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
            log.info("****** devEventNotif() end. because rootUserId is null.");
            return;
          }

          // 处理 推送消息给app
          pushMessageService.dealPushMessage2App(device, attrMap, rootUserId);
        }
      });
    }
  }
}
