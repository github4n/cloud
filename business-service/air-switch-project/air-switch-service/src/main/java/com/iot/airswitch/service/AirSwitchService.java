package com.iot.airswitch.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Strings;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.iot.airswitch.api.AirSwitchStatisticsApi;
import com.iot.airswitch.constant.*;
import com.iot.airswitch.job.CmdJob;
import com.iot.airswitch.job.ElectricityCountJob;
import com.iot.airswitch.util.AnalysisDataUtil;
import com.iot.airswitch.util.HexUtil;
import com.iot.airswitch.util.PackageCmdUtil;
import com.iot.airswitch.util.ThreadPoolUtil;
import com.iot.airswitch.vo.CommToServerVo;
import com.iot.airswitch.vo.data.GwConfigData;
import com.iot.airswitch.vo.data.NetConfigData;
import com.iot.airswitch.vo.data.PushEventData;
import com.iot.airswitch.vo.data.StatusReportData;
import com.iot.airswitch.vo.req.SwitchElectricityStatisticsReq;
import com.iot.airswitch.vo.resp.SwitchElectricityStatisticsHeadResp;
import com.iot.building.device.api.CentralControlDeviceApi;
import com.iot.building.device.api.DeviceBusinessTypeApi;
import com.iot.building.device.util.RedisKeyUtil;
import com.iot.building.device.vo.DeviceBusinessTypeReq;
import com.iot.building.device.vo.DeviceBusinessTypeResp;
import com.iot.building.device.vo.DevicePropertyVo;
import com.iot.building.space.api.SpaceApi;
import com.iot.building.warning.api.WarningApi;
import com.iot.building.warning.vo.WarningReq;
import com.iot.building.warning.vo.WarningResp;
import com.iot.common.beans.BeanUtil;
import com.iot.control.space.api.SpaceDeviceApi;
import com.iot.control.space.vo.SpaceAndSpaceDeviceVo;
import com.iot.control.space.vo.SpaceDeviceResp;
import com.iot.control.space.vo.SpaceReq;
import com.iot.control.space.vo.SpaceResp;
import com.iot.device.api.*;
import com.iot.device.enums.OnlineStatusEnum;
import com.iot.device.vo.req.AirSwitchEventReq;
import com.iot.device.vo.req.DailyElectricityStatisticsReq;
import com.iot.device.vo.req.ElectricityStatisticsReq;
import com.iot.device.vo.req.MonthlyElectricityStatisticsReq;
import com.iot.device.vo.req.device.*;
import com.iot.device.vo.rsp.DailyElectricityStatisticsResp;
import com.iot.device.vo.rsp.ElectricityStatisticsRsp;
import com.iot.device.vo.rsp.device.GetDeviceExtendInfoRespVo;
import com.iot.device.vo.rsp.device.GetDeviceInfoRespVo;
import com.iot.device.vo.rsp.device.GetDeviceStatusInfoRespVo;
import com.iot.device.vo.rsp.device.ListDeviceByParamsRespVo;
import com.iot.redis.RedisCacheUtil;
import com.iot.saas.SaaSContextHolder;
import com.iot.schedule.api.ScheduleApi;
import com.iot.schedule.common.ScheduleConstants;
import com.iot.schedule.vo.AddJobReq;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.text.ParseException;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * @Author: Xieby
 * @Date: 2018/10/26
 * @Description: *
 */
@Service
public class AirSwitchService {

    private Logger log = LoggerFactory.getLogger(AirSwitchService.class);

    @Autowired
    private SpaceApi spaceApi;
    @Autowired
    private com.iot.control.space.api.SpaceApi controlSpaceApi;
    @Autowired
    private WarningApi warningApi;
    @Autowired
    private ScheduleApi scheduleApi;
    @Autowired
    private DeviceCoreApi deviceCoreApi;
    @Autowired
    private SpaceDeviceApi spaceDeviceApi;
    @Autowired
    private AirSwitchEventApi airSwitchEventApi;
    @Autowired
    private DeviceStateCoreApi deviceStateCoreApi;
    @Autowired
    private DeviceStatusCoreApi deviceStatusCoreApi;
    @Autowired
    private DeviceExtendsCoreApi deviceExtendsCoreApi;
    @Autowired
    private DeviceBusinessTypeApi deviceBusinessTypeApi;
    @Autowired
    private AirSwitchStatisticsApi airSwitchStatisticsApi;
    @Autowired
    private CentralControlDeviceApi centralControlDeviceApi;
    @Autowired
    private ElectricityStatisticsApi electricityStatisticsApi;
    @Autowired
    private AirSwitchStatisticsService airSwitchStatisticsService;

    /**
     * 保存上传事件
     */
    public boolean eventPush(String info) {
        CommToServerVo vo = AnalysisDataUtil.handleData(info);
        List<PushEventData> dataList = JSONObject.parseArray(vo.getDATA(), PushEventData.class);
        if (CollectionUtils.isEmpty(dataList)) {
            log.info("could not find warn info, push info = {}.", info);
            return false;
        }
        String deviceId = vo.getMAC();
        List<AirSwitchEventReq> list = Lists.newArrayList();
        dataList.forEach(d-> {
            AirSwitchEventReq req = new AirSwitchEventReq();
            Long time = Integer.valueOf(d.getTIME(), 16).longValue()*1000;
            Date uploadTime = new Date();
            uploadTime.setTime(time);
            req.setDeviceId(deviceId);
            req.setTime(time);
            req.setDay(DateFormatUtils.format(uploadTime, Constants.DATE_FORMAT_YYYY_MM_DD));
            req.setHour(DateFormatUtils.format(uploadTime, Constants.DATE_FORMAT_HH));
            req.setContent(d.getAPX());
            req.setTenantId(11L);

            list.add(req);

        });
        airSwitchEventApi.saveEventBatch(list);
        return false;
    }

    /**
     * 注册时 添加设备基本表
     * 空开(网关) ：deviceId = MAC 地址
     */
    public String registerAirSwitch(@RequestParam("info") String info,@RequestParam("tenantId") Long tenantId,
                                    @RequestParam("ip") String ip, @RequestParam("locationId") Long locationId) {
        log.info("save start");

        CommToServerVo vo = AnalysisDataUtil.handleData(info);
        String deviceUuid = vo.getMAC();

        try {

            //校验device是否已经导入
            GetDeviceInfoRespVo deviceVo = deviceCoreApi.get(deviceUuid);
            if (deviceVo == null) {
                log.info("该设备还没导入，设备mac = {}.", vo.getMAC());
                return "success";
            }

            deviceVo.setIp(ip);
            deviceVo.setMac(vo.getMAC());
            deviceVo.setIsDirectDevice(Constants.IS_DIRECT_DEVICE);
            deviceVo.setDeviceTypeId(Constants.AIR_SWITCH_DEVICE_TYPE_ID);
            deviceVo.setProductId(Constants.AIR_SWITCH_PRODUCT_ID);

            UpdateDeviceInfoReq deviceInfo = new UpdateDeviceInfoReq();

            BeanUtil.copyProperties(deviceVo, deviceInfo);
            log.info("save body = " + JSON.toJSONString(deviceInfo));
            deviceCoreApi.saveOrUpdate(deviceInfo);
            log.info("save success");
            // 添加设备状态表
            updateDeviceStatus(tenantId, deviceUuid);

//            // 保存设备基本表
//            UpdateDeviceInfoReq deviceInfo = new UpdateDeviceInfoReq();
//            deviceInfo.setUuid(deviceUuid);
//            deviceInfo.setTenantId(tenantId);
//            deviceInfo.setLocationId(locationId);
//            deviceInfo.setName(vo.getMAC());
//            deviceInfo.setMac(vo.getMAC());
//            deviceInfo.setIp(ip);
//            deviceInfo.setIsDirectDevice(Constants.IS_DIRECT_DEVICE);
//            deviceInfo.setDeviceTypeId(Constants.AIR_SWITCH_DEVICE_TYPE_ID);
//            deviceInfo.setProductId(Constants.AIR_SWITCH_PRODUCT_ID);
//            log.info("save body = " + JSON.toJSONString(deviceInfo));
//            deviceCoreApi.saveOrUpdate(deviceInfo);
//            log.info("save success");
//            // 添加设备状态表
//            updateDeviceStatus(tenantId, deviceUuid);

        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return deviceUuid;
    }

    /**
     * 获取设备的序列号
     */
    private Integer getDeviceNumber(Long tenantId) {
        Integer number = RedisCacheUtil.valueObjGet(RedisKeyUtil.getDeviceNumberKey(tenantId), Integer.class);
        if (number == null) {
            number = 0;
        } else {
            number += 1;
        }
        RedisCacheUtil.valueObjSet(RedisKeyUtil.getDeviceNumberKey(tenantId), number);
        return number;
    }

    /**
     * 同步设备
     * 1. 更新空开设备（网关）
     * 2. 更新空开节点设备
     */
    public void syncDevice(@RequestParam("netInfo") String netInfo, @RequestParam("gwInfo") String gwInfo) {

//        CommToServerVo netVo = AnalysisDataUtil.handleData(netInfo);
//        NetConfigData netData = JSONObject.parseObject(netVo.getDATA(), NetConfigData.class);
//        CommToServerVo gwVo = AnalysisDataUtil.handleData(gwInfo);
//
//        String deviceUuid = netVo.getMAC();
//
//        GetDeviceInfoRespVo deviceInfo = deviceCoreApi.get(netVo.getMAC());
//
//        if (deviceInfo == null) {
//            return;
//        }
//
//        Long tenantId = deviceInfo.getTenantId();
//        Long locationId = deviceInfo.getLocationId();
//        // 更新设备信息
//        //deviceInfo.setIp(netData.getIP());
//        deviceInfo.setSsid(netData.getSSID());
//        deviceInfo.setVersion(netData.getVER());
//        UpdateDeviceInfoReq deviceReq = new UpdateDeviceInfoReq();
//        BeanUtil.copyProperties(deviceInfo, deviceReq);
//        deviceCoreApi.saveOrUpdate(deviceReq);
//
//        // 添加设备额外信息表
//        updateDeviceExtend(tenantId, deviceUuid, netData.getTYPE(), netVo.getTIMEZONE(),
//                           netData.getSIP());
////
//        // 添加设备状态表
//        updateDeviceStatus(tenantId, deviceUuid);
//
//        // 更新设备功能点
//        //updateDeviceDataPoint(tenantId, deviceUuid, netData.getCTRL());
//
//        // 添加节点
//        List<GwConfigData> gwList = JSONObject.parseArray(gwVo.getDATA(), GwConfigData.class);
//        for (GwConfigData data : gwList) {
//            // 节点设备编号 -> MAC + NNO
//            String deviceNodeId = deviceUuid + data.getNNO();
//            GetDeviceInfoRespVo dInfo = deviceCoreApi.get(deviceNodeId);
//            if (dInfo == null) {
//                dInfo = new GetDeviceInfoRespVo();
//                dInfo.setId(null);
//            }
//            dInfo.setMac(deviceNodeId);
//            dInfo.setUuid(deviceNodeId);
//            dInfo.setTenantId(tenantId);
//            dInfo.setLocationId(locationId);
//            dInfo.setName(deviceNodeId);
//            // 设置为空开的子设备
//            dInfo.setParentId(deviceUuid);
//            dInfo.setIsDirectDevice(Constants.IS_NOT_DIRECT_DEVICE);
//            dInfo.setDeviceTypeId(Constants.AIR_SWITCH_NODE_DEVICE_TYPE_ID);
//            dInfo.setProductId(Constants.AIR_SWITCH_NODE_PRODUCT_ID);
//
//            UpdateDeviceInfoReq uReq = new UpdateDeviceInfoReq();
//            BeanUtil.copyProperties(dInfo, uReq);
//
//            deviceCoreApi.saveOrUpdate(uReq);
//
//            // 添加设备额外信息表
//            updateDeviceExtend(tenantId, deviceNodeId, data.getTYPE(), netVo.getTIMEZONE(),
//                               netData.getSIP());
//
//            // 添加设备状态表
//            updateDeviceStatus(tenantId, deviceNodeId);
//        }
    }

    /**
     * 同步空开设备
     */
    public void synAirSwitch(@RequestParam("ip") String ip) {
        String netCmd = PackageCmdUtil.packageGetNetConfig();
        String gwCmd = PackageCmdUtil.packageGetGWConfig();
        CmdJob netCmdJob = new CmdJob(ip, Constants.AIR_SWITCH_PORT, netCmd);
        CmdJob gwCmdJob = new CmdJob(ip, Constants.AIR_SWITCH_PORT, gwCmd);
        try {
            String netInfo = netCmdJob.call();
            String gwInfo = gwCmdJob.call();
            syncDevice(netInfo, gwInfo);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    /**
     * 保存定期上传的电量
     */
    public void saveElectricity(String info) {
        try {
            CommToServerVo vo = AnalysisDataUtil.handleData(info);
            StatusReportData reportData = JSONObject.parseObject(vo.getDATA(), StatusReportData.class);
            for (StatusReportData.StatusReport sr : reportData.getList()) {
                String deviceUuid = vo.getMAC() + sr.getNNO();
                GetDeviceInfoRespVo device = deviceCoreApi.get(deviceUuid);
                Long tenantId = device == null ? 11L : device.getTenantId();
                Long locationId = device == null ? 1L : device.getLocationId();
                Date uploadDate = new Date();
                uploadDate.setTime(Long.parseLong(reportData.getTIME())*1000);
                Integer ps = sr.getPS();
                Integer vol = sr.getVOL();
                Integer pwr = sr.getPWR();
                Integer tmp = sr.getTMP();
                Integer cur = sr.getCUR();
                // 保存电量
                ElectricityStatisticsReq electricity = createElectricityStatistics(deviceUuid, uploadDate, Constants.STATISTICS_MIN, ps.doubleValue(), 1L, tenantId,
                        sr.getTYPE(), vol, pwr, tmp, cur);
                electricityStatisticsApi.insertElectricityStatistics(electricity);
                // 获取告警信息
                handleAlarm(deviceUuid, sr.getALM(), uploadDate, tenantId, locationId);
                // 保存每小时最后的电量
                String redisKey = getElectricityKey(deviceUuid, DateFormatUtils.format(uploadDate, Constants.ELECTRICITY_REDIS_DATE));
                RedisCacheUtil.valueSet(redisKey, String.valueOf(ps), 3600*24*7L);
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    /**
     * 保存事件预警和告警信息
     */
    private void handleAlarm(String deviceUuid, String almInfo, Date uploadDate, Long tenantId, Long locationId) {
        try {
            List<AirSwitchEventReq> list = Lists.newArrayList();
            String info = HexUtil.hexToBinary(almInfo);
            for (int i=0; i<info.length(); i++) {
                String vl = String.valueOf(info.charAt(i));
                if (vl.equals("1")) {
                    if (Constants.EarlyWarningList.contains(i)) {
                        AirSwitchEventReq req = createEventReq(deviceUuid, i, AlarmTypeEnum.EARLY_WARNING.type, EarlyWarningEnum.getDesc(i), uploadDate, tenantId);

                        // 组装预警消息
                        WarningReq warningReq = createWarn(deviceUuid, AlarmTypeEnum.EARLY_WARNING.type, EarlyWarningEnum.getDesc(i), uploadDate, tenantId, locationId);
                        log.info("warningReq={}.", JSON.toJSONString(warningReq));
                        if (checkAlarmExist(warningReq)) {
                            list.add(req);
                            warningApi.addWarning(warningReq);
                        }
                    }
                    if (Constants.AlarmList.contains(i)) {
                        AirSwitchEventReq req = createEventReq(deviceUuid, i, AlarmTypeEnum.ALARM.type, AlarmEnum.getDesc(i), uploadDate, tenantId);
                        // 组装告警消息
                        WarningReq warningReq = createWarn(deviceUuid, AlarmTypeEnum.ALARM.type, AlarmEnum.getDesc(i), uploadDate, tenantId, locationId);
                        log.info("warningReq={}.", JSON.toJSONString(warningReq));
                        if (checkAlarmExist(warningReq)) {
                            list.add(req);
                            warningApi.addWarning(warningReq);
                        }
                    }
                }

                // 31位 开关开合状态 0:关 1:开
                if (i == 30) {
                    updateDeviceSwitch(deviceUuid, Integer.parseInt(vl));
                }

                // 32位 开关在线状态 0:不在线 1:在线
                if (i == 31) {
                    if ("1".equals(vl)) {
                        setDeviceHeartBeat(deviceUuid);
                    }
                }
            }

            // 保存预警,告警事件
            log.info("deviceId = {} , alarm = {}, save event list = {}.", deviceUuid, info, JSON.toJSONString(list));
            if (CollectionUtils.isNotEmpty(list)) {
                airSwitchEventApi.saveEventBatch(list);
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    /**
     * 检验告警是否存在
     */
    private boolean checkAlarmExist(WarningReq warningReq) {
        try {
            WarningReq wr = new WarningReq();
            wr.setDeviceId(warningReq.getDeviceId());
            wr.setEventType(warningReq.getEventType());
            wr.setStatus("0");
            List<WarningResp> list = warningApi.findWarningList(wr);
            return CollectionUtils.isEmpty(list);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return true;
    }

    /**
     * 组装告警消息
     */
    private WarningReq createWarn(String deviceId, Integer alarmType, String alarmContent, Date date, Long tenantId, Long locationId) {
        WarningReq req = new WarningReq();
        req.setDeviceId(deviceId);
        req.setEventType(alarmType.toString());
        req.setEventName(alarmContent);
        req.setCreateTime(date);
        req.setLocationId(locationId);
        req.setTenantId(tenantId.toString());
        req.setStatus("0"); // 待处理
        req.setSpaceName(getSpaceName(deviceId, tenantId));
        req.setUse(getDeviceUse(deviceId, tenantId));
        return req;
    }

    /**
     * 获取设备用途
     */
    private String getDeviceUse(String deviceId, Long tenantId) {
        if (Strings.isNullOrEmpty(deviceId)) {
            return null;
        }
        try {
            GetDeviceInfoRespVo deviceVo = deviceCoreApi.get(deviceId);
            if (deviceVo == null || deviceVo.getBusinessTypeId() == null) {
                return null;
            }
            DeviceBusinessTypeResp resp = deviceBusinessTypeApi.findById(tenantId, deviceVo.getBusinessTypeId());
            if (resp != null) {
                return resp.getBusinessType();
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }

    /**
     * 根据设备ID查找挂载空间名称
     */
    private String getSpaceName(String deviceId, Long tenantId) {

        if (Strings.isNullOrEmpty(deviceId)) {
            return null;
        }
        try {
            SpaceAndSpaceDeviceVo vo = new SpaceAndSpaceDeviceVo();
            vo.setDeviceIds(Lists.newArrayList(deviceId));
            vo.setTenantId(tenantId);
            List<SpaceDeviceResp> list = spaceDeviceApi.findSpaceDeviceBySpaceIdsOrDeviceIds(vo);
            if (CollectionUtils.isEmpty(list)) {
                return null;
            }
            SpaceReq req = new SpaceReq();
            req.setId(list.get(0).getSpaceId());
            req.setTenantId(tenantId);
            List<SpaceResp> spaceList = spaceApi.getSpaceByCondition(req);
            return CollectionUtils.isEmpty(spaceList) ? null : spaceList.get(0).getName();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }

    /**
     * 更新设备开关状态
     */
    private void updateDeviceSwitch(String deviceId, int onOff) {
        try {
            DevicePropertyVo propertyVo = new DevicePropertyVo();
            propertyVo.setDeviceId(deviceId);
            Map map = Maps.newHashMap();
            map.put("onOff", onOff);
            propertyVo.setProperty(map);
            centralControlDeviceApi.airSwitchBack(propertyVo);
            log.info("update air switch node({}) onOff status(status = {}).", deviceId, onOff);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    private AirSwitchEventReq createEventReq(String deviceUuid, int type, int alarmType, String alarmDesc, Date uploadDate, Long tenantId) {
        AirSwitchEventReq req = new AirSwitchEventReq();
        req.setAlarmType(alarmType);
        req.setDeviceId(deviceUuid);
        req.setType(String.valueOf(type));
        req.setAlarmDesc(alarmDesc);
        req.setTime(uploadDate.getTime());
        req.setDay(DateFormatUtils.format(uploadDate, Constants.DATE_FORMAT_YYYY_MM_DD));
        req.setHour(DateFormatUtils.format(uploadDate, Constants.DATE_FORMAT_HH));
        req.setTenantId(tenantId);
        return req;
    }

    /**
     * 获取redis key
     */
    private String getElectricityKey(String deviceId, String date) {
        StringBuffer sb = new StringBuffer();
        sb.append(Constants.ELECTRICITY_REDIS_PREFIX)
                .append(date)
                .append(":")
                .append(deviceId);
        return sb.toString();
    }

    /**
     * 创建分钟电量统计
     */
    private ElectricityStatisticsReq createElectricityStatistics(String deviceId, Date uploadDate, Integer step, Double value, Long userId,
                                                                 Long tenantId, String nodeType, Integer vol, Integer power, Integer tmp, Integer cur) {
        ElectricityStatisticsReq electricity = createHourElectricityStatistics(deviceId, uploadDate, step, value, userId, tenantId);
        electricity.setVoltage(vol.doubleValue());
        electricity.setTemperature(tmp.doubleValue());
        /**
         * 塑壳产品
         */
        if (CommGwTypeEnum.isPlasticCase(nodeType)) {
            electricity.setPower(power.doubleValue()*20);
            electricity.setCurrent(cur.doubleValue()*0.1);
        } else {
            electricity.setPower(power.doubleValue());
            electricity.setCurrent(cur.doubleValue()*0.01);
        }


        return electricity;
    }

    /**
     * 创建小时电量统计
     */
    private ElectricityStatisticsReq createHourElectricityStatistics(String deviceId, Date uploadDate, Integer step, Double value, Long userId, Long tenantId) {
        ElectricityStatisticsReq electricity = new ElectricityStatisticsReq();
        electricity.setStep(step);
        electricity.setUserId(userId);
        electricity.setTime(uploadDate);
        electricity.setDeviceId(deviceId);
        electricity.setElectricValue(value);
        electricity.setLocaltime(new Date());
        electricity.setTenantId(tenantId);
        electricity.setArea(DateFormatUtils.format(uploadDate, Constants.DATE_FORMAT_HH));
        String businessType = getDeviceUse(deviceId, tenantId);
        // 设置是否总路
        if (!Strings.isNullOrEmpty(businessType) && businessType.startsWith("Z")) {
            electricity.setIsMaster(0);
        } else {
            electricity.setIsMaster(1);
        }
        return electricity;
    }

    /**
     * 更新设备功能点
     */
    private void updateDeviceDataPoint(Long tenantId, String deviceId, String ctrl) {
        UpdateDeviceStateReq deviceState = new UpdateDeviceStateReq();
        deviceState.setDeviceId(deviceId);
        deviceState.setTenantId(tenantId);

        List<AddCommDeviceStateInfoReq> list = Lists.newArrayList();

        AddCommDeviceStateInfoReq dhcpReq = new AddCommDeviceStateInfoReq();
        dhcpReq.setPropertyName(Constants.PROPERTY_CODE_DHCP);
        dhcpReq.setPropertyName(ctrl.substring(0, 1));
        dhcpReq.setPropertyDesc("DHCP使能");
        list.add(dhcpReq);

        AddCommDeviceStateInfoReq dnsReq = new AddCommDeviceStateInfoReq();
        dnsReq.setPropertyName(Constants.PROPERTY_CODE_DNS);
        dnsReq.setPropertyName(ctrl.substring(1, 2));
        dnsReq.setPropertyDesc("DNS解析");
        list.add(dnsReq);

        AddCommDeviceStateInfoReq beatReq = new AddCommDeviceStateInfoReq();
        beatReq.setPropertyName(Constants.PROPERTY_CODE_HEART_BEAT);
        beatReq.setPropertyName(ctrl.substring(2, 3));
        beatReq.setPropertyDesc("心跳包启用");
        list.add(beatReq);

        deviceState.setStateList(list);

        deviceStateCoreApi.saveOrUpdate(deviceState);
    }

    /**
     * 更新空开节点的开关状态
     */
    private void updateNodeSwitchStatus(Long tenantId, String deviceUuid, Integer status) {
        GetDeviceStatusInfoRespVo statusInfo = deviceStatusCoreApi.get(tenantId, deviceUuid);
        if (statusInfo == null) {
            return;
        }
        statusInfo.setOnOff(status);
        UpdateDeviceStatusReq req = new UpdateDeviceStatusReq();
        BeanUtil.copyProperties(statusInfo, req);
        deviceStatusCoreApi.saveOrUpdate(req);
    }

    /**
     * 保存设备额外信息表
     */
    private void updateDeviceExtend(Long tenantId, String deviceUuid, String type,
                                    String timezone, String sip, Long sPort, Long interval) {

        GetDeviceExtendInfoRespVo vo = deviceExtendsCoreApi.get(tenantId, deviceUuid);
        UpdateDeviceExtendReq deviceExtendReq = new UpdateDeviceExtendReq();
        if (vo != null) {
            BeanUtil.copyProperties(vo, deviceExtendReq);
        }
        deviceExtendReq.setTenantId(tenantId);
        deviceExtendReq.setDeviceId(deviceUuid);
        deviceExtendReq.setCommType(type);
        deviceExtendReq.setTimezone(timezone);
        deviceExtendReq.setServerIp(sip);
        deviceExtendReq.setServerPort(sPort);
        deviceExtendReq.setReportInterval(interval);
        log.info("device extend info = {}.", JSON.toJSONString(deviceExtendReq) );
        deviceExtendsCoreApi.saveOrUpdate(deviceExtendReq);
    }

    /**
     * 更新设备状态表
     */
    private void updateDeviceStatus(Long tenantId, String deviceUuid) {
        UpdateDeviceStatusReq commStatus = new UpdateDeviceStatusReq();
        GetDeviceStatusInfoRespVo statusVo = deviceStatusCoreApi.get(tenantId, deviceUuid);
        if (statusVo != null) {
            BeanUtil.copyProperties(statusVo, commStatus);
        }
        commStatus.setDeviceId(deviceUuid);
        commStatus.setOnlineStatus(OnlineStatusEnum.CONNECTED.getCode());
        commStatus.setActiveStatus(OnlineStatusEnum.CONNECTED.getValue());
        commStatus.setOnOff(OnlineStatusEnum.CONNECTED.getValue());
        commStatus.setActiveTime(new Date());
        commStatus.setTenantId(tenantId);
        log.info("device status info = {}.", JSON.toJSONString(commStatus) );
        deviceStatusCoreApi.saveOrUpdate(commStatus);
    }

    /**
     * 定时统计小时电量
     */
    public void countHourElectricity(Long tenantId, String lastDate, String currentDate) {
        log.info("start count hour electricity............. lastDate = {}, currentDate = {}.", lastDate, currentDate);
        try {
            String lastKey = Constants.ELECTRICITY_REDIS_PREFIX + lastDate + ":";
            String currentKey = Constants.ELECTRICITY_REDIS_PREFIX + currentDate + ":";
            Set<String> lastKeySet = RedisCacheUtil.keys(lastKey + "*");
            Set<String> currentKeySet = RedisCacheUtil.keys(currentKey + "*");

            Multimap<String, Double> map = ArrayListMultimap.create();
            lastKeySet.forEach(k-> {
                String count = RedisCacheUtil.valueGet(k);
                if (!Strings.isNullOrEmpty(count)) {
                    map.put(StringUtils.difference(lastKey, k), Double.valueOf(count));
                }
            });
            currentKeySet.forEach(ck-> {
                String count = RedisCacheUtil.valueGet(ck);
                if (!Strings.isNullOrEmpty(count)) {
                    map.put(StringUtils.difference(currentKey, ck), Double.valueOf(count));
                }
            });

            log.info("hour electricity info = " + JSON.toJSONString(map));
            if (map.isEmpty()) {
                log.info("don't find redis electricity statistics.");
                return;
            }
            Date uploadDate = null;
            try {
                uploadDate = DateUtils.parseDate(currentDate, Constants.ELECTRICITY_REDIS_DATE);
            } catch (ParseException e) {
                log.error(e.getMessage(), e);
            }
            List<ElectricityStatisticsReq> dataList = Lists.newArrayList();
            for (String deviceId : map.keySet()) {
                Collection<Double> collection = map.get(deviceId);
                Double max = Collections.max(collection);
                Double min = Collections.min(collection);
//                Double min = collection.size() > 1 ? Collections.min(collection) : 0D;

                ElectricityStatisticsReq electricity = createHourElectricityStatistics(deviceId, uploadDate, Constants.STATISTICS_HOUR, (max - min), 1L,  tenantId);
                dataList.add(electricity);
            }
            log.info("hour electricity list = {}.", JSON.toJSONString(dataList) );
            if (CollectionUtils.isNotEmpty(dataList)) {
                electricityStatisticsApi.insertMinElectricityStatistics(dataList);
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        log.info("end count hour electricity.............");
    }

    /**
     * 统计电量按天
     */
    public void countDayElectricity(Long tenantId, String day) {
        log.info("start count day electricity.............." + day);
        try {
            ElectricityStatisticsReq req = new ElectricityStatisticsReq();
            req.setTenantId(tenantId);
            req.setTimeStr(day);
            req.setStep(Constants.STATISTICS_HOUR);
            // 只统计分类的节点电量
            req.setIsMaster(1);
            List<ElectricityStatisticsRsp> eleList = electricityStatisticsApi.getMinListByReq(req);
            Multimap<String, Double> multimap = ArrayListMultimap.create();
            for (ElectricityStatisticsRsp ele : eleList) {
                multimap.put(ele.getDeviceId(), ele.getElectricValue());
            }
            log.info("day electricity info = " + JSON.toJSONString(multimap));
            List<DailyElectricityStatisticsReq> list = Lists.newArrayList();
            for (String device : multimap.keySet()) {
                Double total = multimap.get(device).stream().mapToDouble(Double::doubleValue).sum();
                DailyElectricityStatisticsReq daily = createDailyElectricityStatistics(tenantId, device, 1L, total, day);
                list.add(daily);
            }
            log.info("daily electricity list = {}.", JSON.toJSONString(list) );
            electricityStatisticsApi.insertDailyElectricityStatistics(list);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        log.info("end count day electricity..............");
    }

    private DailyElectricityStatisticsReq createDailyElectricityStatistics(Long tenantId, String device, Long userId, Double total, String day) {
        DailyElectricityStatisticsReq daily = new DailyElectricityStatisticsReq();
        try {
            daily.setDay(DateUtils.parseDate(day, Constants.DATE_FORMAT_YYYY_MM_DD));
            daily.setDayStr(day);
            daily.setDeviceId(device);
            daily.setUserId(userId);
            daily.setElectricValue(total);
            daily.setTenantId(tenantId);
        } catch (ParseException e) {
            log.error(e.getMessage(), e);
        }
        return daily;
    }

    /**
     * 统计电量按月
     */
    public void countMonthElectricity(Long tenantId, int year, int month) {

        log.info("start month day electricity.............." + year + month);
        try {
            DailyElectricityStatisticsReq req = new DailyElectricityStatisticsReq();
            req.setTenantId(tenantId);
            req.setMonthPrefix(year + "-" + (month > 9 ? month : "0"+month));
            List<DailyElectricityStatisticsResp> list = electricityStatisticsApi.getDailyListByReq(req);
            Multimap<String, Double> multimap = ArrayListMultimap.create();
            for (DailyElectricityStatisticsResp daily : list) {
                multimap.put(daily.getDeviceId(), daily.getElectricValue());
            }
            log.info("month electricity info = " + JSON.toJSONString(multimap));
            List<MonthlyElectricityStatisticsReq> monthList = Lists.newArrayList();
            for (String deviceId : multimap.keySet()) {
                Double total = multimap.get(deviceId).stream().mapToDouble(Double::doubleValue).sum();
                MonthlyElectricityStatisticsReq monthReq = createMonthElectricityStatistics(tenantId, deviceId, 1L, year, month, total);
                monthList.add(monthReq);
            }
            log.info("month electricity list = {}.", JSON.toJSONString(monthList) );
            electricityStatisticsApi.insertMonthElectricityStatistics(monthList);

        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        log.info("end month day electricity..............");
    }

    private MonthlyElectricityStatisticsReq createMonthElectricityStatistics(Long tenantId, String deviceId, Long userId, int year, int month, Double total) {
        MonthlyElectricityStatisticsReq req = new MonthlyElectricityStatisticsReq();
        req.setDeviceId(deviceId);
        req.setTenantId(tenantId);
        req.setElectricValue(total);
        req.setYear(year);
        req.setMonth(month);
        req.setUserId(userId);
        return req;
    }

    /**
     * 修改设备的指向服务器
     */
    public void setServerAddress(@RequestParam("tenantId") Long tenantId, @RequestParam("deviceId") String deviceId,
                                 @RequestParam("ip") String ip, @RequestParam("port") String port) {
        GetDeviceInfoRespVo deviceInfo = deviceCoreApi.get(deviceId);
        if (deviceInfo == null) {
            return;
        }
        GetDeviceInfoRespVo parentDevice = deviceCoreApi.get(deviceInfo.getParentId());
        if (parentDevice == null) {
            return;
        }
        String nIp = HexUtil.ipToHex(ip);
        String nPort = HexUtil.intToHex(Integer.parseInt(port), 4);
        String cmd = PackageCmdUtil.packageSetServerAddress(nIp, nPort);
        CmdJob cmdJob = new CmdJob(parentDevice.getIp(), Constants.AIR_SWITCH_PORT, cmd);
        ThreadPoolUtil.instance().submit(cmdJob);
    }

    /**
     * 对设备进行漏电测试
     */
    public void leakageTest(String deviceId, Long tenantId) {
        String node = getNodeBydeviceId(deviceId);
        if (Strings.isNullOrEmpty(node)) {
            return;
        }
        GetDeviceInfoRespVo deviceInfo = deviceCoreApi.get(deviceId);
        if (deviceInfo == null) {
            return;
        }
        GetDeviceInfoRespVo parentDevice = deviceCoreApi.get(deviceInfo.getParentId());
        if (parentDevice == null) {
            return;
        }
        List<String> nodeList = Lists.newArrayList(node);
        String cmd = PackageCmdUtil.packageLeakageTest(nodeList);

        CmdJob cmdJob = new CmdJob(parentDevice.getIp(), Constants.AIR_SWITCH_PORT, cmd);
        ThreadPoolUtil.instance().submit(cmdJob);
    }

    /**
     * 对设备进行合闸
     */
    public void switchOn(@RequestParam("deviceId") String deviceId, @RequestParam("tenantId") Long tenantId) {
        airSwitchControl(deviceId, 0);
    }

    /**
     * 对设备进行分闸
     */
    public void switchOff(@RequestParam("deviceId") String deviceId, @RequestParam("tenantId") Long tenantId) {
        airSwitchControl(deviceId, 1);
    }

    /**
     * 更新设备上传时间间隔
     */
    public void setRTVI(String deviceId, Integer interval) {
        String node = getNodeBydeviceId(deviceId);
        if (Strings.isNullOrEmpty(node)) {
            return;
        }
        GetDeviceInfoRespVo deviceInfo = deviceCoreApi.get(deviceId);
        if (deviceInfo == null) {
            return;
        }
        GetDeviceInfoRespVo parentDevice = deviceCoreApi.get(deviceInfo.getParentId());
        if (parentDevice == null) {
            return;
        }
        String cmd = PackageCmdUtil.packageRTVI(interval);

        CmdJob cmdJob = new CmdJob(parentDevice.getIp(), Constants.AIR_SWITCH_PORT, cmd);
        ThreadPoolUtil.instance().submit(cmdJob);
    }

    /**
     * 空开自检
     * 1.漏电自检
     * 2.合闸
     */
    public void selfCheck(String deviceId, Long tenantId) {

        leakageTest(deviceId, tenantId);

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            log.error(e.getMessage());
        }

        airSwitchControl(deviceId, 0);
    }

    /**
     * 对设备进行开闸合闸操作
     * type ：0->合闸 1->分闸
     */
    private void airSwitchControl(String deviceId, int type) {
        String node = getNodeBydeviceId(deviceId);
        if (Strings.isNullOrEmpty(node)) {
            return;
        }
        GetDeviceInfoRespVo deviceInfo = deviceCoreApi.get(deviceId);
        if (deviceInfo == null) {
            return;
        }
        GetDeviceInfoRespVo parentDevice = deviceCoreApi.get(deviceInfo.getParentId());
        if (parentDevice == null) {
            return;
        }

        List<String> nodeList = Lists.newArrayList(node);

        // 拼装命令
        String cmd = (type == 0) ? PackageCmdUtil.packageOpenNode(nodeList) : PackageCmdUtil.packageCloseNode(nodeList);

        CmdJob cmdJob = new CmdJob(parentDevice.getIp(), Constants.AIR_SWITCH_PORT, cmd);
        ThreadPoolUtil.instance().submit(cmdJob);

        // 设置device status
        updateDeviceSwitch(deviceId, type == 1 ? 0 : 1);

//        GetDeviceStatusInfoRespVo statusVo = deviceStatusCoreApi.get(deviceInfo.getTenantId(), deviceId);
//        statusVo.setOnOff(type == 1 ? 0 : 1);
//        UpdateDeviceStatusReq statusReq = new UpdateDeviceStatusReq();
//        BeanUtil.copyProperties(statusVo, statusReq);
//        log.info("UpdateDeviceStatusReq = {}.", JSON.toJSONString(statusReq));
//        deviceStatusCoreApi.saveOrUpdate(statusReq);
    }

    /**
     * 根据设备ID获取MAC
     */
    private String getNodeBydeviceId(String deviceId) {
        GetDeviceInfoRespVo vo = deviceCoreApi.get(deviceId);
        if (vo == null) {
            return null;
        }
        return StringUtils.difference(vo.getParentId(), deviceId);
    }

    /**
     * 修改设备名称
     */
    public boolean modifyDeviceName(String deviceId, String name) {
        GetDeviceInfoRespVo vo = deviceCoreApi.get(deviceId);
        if (vo == null) {
            return false;
        }

        String sIp = vo.getIp();
        Integer sPort = Constants.AIR_SWITCH_PORT;
        String getNetConfigCmd = PackageCmdUtil.packageGetNetConfig();
        CmdJob cmdJob = new CmdJob(sIp, sPort, getNetConfigCmd);

        try {
            Future<String> future = ThreadPoolUtil.instance().submit(cmdJob);
            String result = future.get();
            CommToServerVo commVo = AnalysisDataUtil.handleData(result);
            NetConfigData config = JSONObject.parseObject(commVo.getDATA(), NetConfigData.class);
            String cmd = PackageCmdUtil.packageModifyName(config, name);
            CmdJob modifyCmd = new CmdJob(sIp, sPort, cmd);
            ThreadPoolUtil.instance().submit(modifyCmd);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 启动UDP服务
     */
    public boolean startup(Integer port) {
        new Thread(() -> {
            try (DatagramSocket socket = new DatagramSocket(port)) {
                while (true) {
                    try {
                        DatagramPacket request = new DatagramPacket(new byte[2048], 2048);
                        socket.receive(request);
                        String requestInfo = HexUtil.bytesToUdpCmd(request.getData());
                        System.out.println("start data = " + requestInfo);
                        List<String> dataList = AnalysisDataUtil.comminuteData(requestInfo);
                        CommToServerVo vo = AnalysisDataUtil.handleData(requestInfo);
                        // 电量上报
                        if (CMDEnum.STATUS_REPORT.code.equals(vo.getCMD())) {
                            log.info("status report info = " + requestInfo);
                            ElectricityCountJob job = new ElectricityCountJob(requestInfo);
                            ThreadPoolUtil.instance().submit(job);
                            // 空开注册
                        }else if(CMDEnum.REGISTER.code.equals(vo.getCMD())) {
                            String ip = request.getAddress().getHostAddress();
                            log.info("REGISTER info = " + requestInfo);
                            registerAirSwitch(requestInfo, 11L, ip, 1L);
                            // 空开网络信息
                        } else if(CMDEnum.PUSH_NET_CONFIG.code.equals(vo.getCMD())) {
                            log.info("push net config info = " + requestInfo);
                            updateAirSwitchInfo(requestInfo);
                            // 空开节点信息
                        } else if (CMDEnum.PUSH_GW_CONFIG.code.equals(vo.getCMD())) {
                            String ip = request.getAddress().getHostAddress();
                            log.info("push sw config info = {}, ip = {}", requestInfo, ip);
                            updateAirSwitchNodeInfo(requestInfo);
                            // 更新设备心跳
                        } else if (CMDEnum.HEART_BEAT.code.equals(vo.getCMD())) {
                            log.info("heart beat info = " + requestInfo);
                            airSwitchHeartBeat(requestInfo);
                        }
                        String currentDate = HexUtil.getCurrentDateToHex();
                        System.out.println("ip = "+ request.getAddress().getHostAddress() +" & message type = "
                                + dataList.get(1) + " & date = " + currentDate + " & vo =" + JSON.toJSONString(vo));
                        String cmdResp = PackageCmdUtil.packageResponse(dataList);
                        byte[] data = HexUtil.cmdToByte(cmdResp);
                        DatagramPacket response = new DatagramPacket(data, data.length, request.getAddress(), request.getPort());
                        socket.send(response);
                        System.out.println("end >>>>>>>>>>>>>>>>" + HexUtil.bytesToUdpCmd(response.getData()) + " & date" + new Date());
                    } catch (IOException e) {
                        log.error(e.getMessage(), e);
                    }
                }
            } catch (IOException e) {
                log.error(e.getMessage(), e);
            }
        }).start();

        return false;
    }

    /**
     * 更新空开心跳
     */
    private void airSwitchHeartBeat(String info) {
        CommToServerVo hVo = AnalysisDataUtil.handleData(info);
        setDeviceHeartBeat(hVo.getMAC());
    }

    /**
     * 设置设备的心跳数据(5min)
     */
    private void setDeviceHeartBeat(String deviceId) {
        try {
            String key = Constants.AIR_SWITCH_HEART_BEAT_REDIS_PREFIX + deviceId;
            RedisCacheUtil.valueSet(key, "1", 300L);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    /**
     * 新增空开节点信息
     */
    private void updateAirSwitchNodeInfo(String info) {

        try {
            CommToServerVo gwVo = AnalysisDataUtil.handleData(info);
            String deviceUuid = gwVo.getMAC();
            GetDeviceInfoRespVo deviceInfo = deviceCoreApi.get(gwVo.getMAC());
            if (deviceInfo == null) {
                return;
            }
            Long tenantId = deviceInfo.getTenantId();
            Long locationId = deviceInfo.getLocationId();

            GetDeviceExtendInfoRespVo extendInfo = deviceExtendsCoreApi.get(tenantId, deviceUuid);

            // 添加节点
            List<GwConfigData> gwList = JSONObject.parseArray(gwVo.getDATA(), GwConfigData.class);
            for (GwConfigData data : gwList) {
                // 节点设备编号 -> MAC + NNO
                String deviceNodeId = deviceUuid + data.getNNO();
                GetDeviceInfoRespVo dInfo = deviceCoreApi.get(deviceNodeId);
                // 已经有节点的数据的时候就不更新名称
                if (dInfo == null) {
                    dInfo = new GetDeviceInfoRespVo();
                    dInfo.setId(null);
                    // 设备名称 -> mac + "_" + 序列号
                    Integer deviceNum = getDeviceNumber(tenantId);
                    String nodeName = deviceUuid + "_" + deviceNum;
                    dInfo.setName(nodeName);
                }
                dInfo.setMac(deviceNodeId);
                dInfo.setUuid(deviceNodeId);
                dInfo.setTenantId(tenantId);
                dInfo.setLocationId(locationId);
                // 设置为空开的子设备
                dInfo.setParentId(deviceUuid);
                dInfo.setIsDirectDevice(Constants.IS_NOT_DIRECT_DEVICE);
                dInfo.setDeviceTypeId(Constants.AIR_SWITCH_NODE_DEVICE_TYPE_ID);
                dInfo.setProductId(Constants.AIR_SWITCH_NODE_PRODUCT_ID);

                UpdateDeviceInfoReq uReq = new UpdateDeviceInfoReq();
                BeanUtil.copyProperties(dInfo, uReq);
                log.info("device info = {}.", JSON.toJSONString(uReq) );
                deviceCoreApi.saveOrUpdate(uReq);

                // 添加设备额外信息表
                updateDeviceExtend(tenantId,
                        deviceNodeId,
                        data.getTYPE(),
                        gwVo.getTIMEZONE(),
                        extendInfo == null ? null : extendInfo.getServerIp(),
                        extendInfo == null ? null : extendInfo.getServerPort(),
                        extendInfo == null ? null : extendInfo.getReportInterval());

                // 添加设备状态表
                updateDeviceStatus(tenantId, deviceNodeId);
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    /**
     * 完善空开的设备信息
     */
    private void updateAirSwitchInfo(String info) {
        try {
            CommToServerVo netVo = AnalysisDataUtil.handleData(info);
            NetConfigData netData = JSONObject.parseObject(netVo.getDATA(), NetConfigData.class);

            String deviceUuid = netVo.getMAC();

            GetDeviceInfoRespVo deviceInfo = deviceCoreApi.get(netVo.getMAC());

            if (deviceInfo == null) {
                return;
            }

            Long tenantId = deviceInfo.getTenantId();
            // 更新设备信息
            //deviceInfo.setIp(netData.getIP());
            String nName = HexUtil.hexToStr(netData.getNAME());
            if (Strings.isNullOrEmpty(nName)) {
                deviceInfo.setName(deviceUuid);
            } else {
                deviceInfo.setName(nName);
            }

            deviceInfo.setDeviceTypeId(Constants.AIR_SWITCH_DEVICE_TYPE_ID);
            deviceInfo.setSsid(netData.getSSID());
            deviceInfo.setVersion(netData.getVER());
            UpdateDeviceInfoReq deviceReq = new UpdateDeviceInfoReq();
            BeanUtil.copyProperties(deviceInfo, deviceReq);
            log.info("device info = {}.", JSON.toJSONString(deviceReq) );
            deviceCoreApi.saveOrUpdate(deviceReq);

            // 添加设备额外信息表
            updateDeviceExtend(tenantId, deviceUuid, netData.getTYPE(), netVo.getTIMEZONE(),
                    netData.getSIP(), netData.getSPORT(), netData.getRTVI().longValue());
//
            // 添加设备状态表
            updateDeviceStatus(tenantId, deviceUuid);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    /**
     * 启动定时任务
     */
    public void startSchedule() {

        try {



            //tenantId 获取
            Map map = Maps.newHashMap();
            // map.put("tenantIds", Lists.newArrayList(11L));
            List<Long> idList = spaceApi.getLocationTenant();
            map.put("tenantIds", idList);

            if (CollectionUtils.isNotEmpty(idList)) {
                SaaSContextHolder.setCurrentTenantId(idList.get(0));
            }

            log.info("delete electricity schedule ......");
            String jobName = "countAirSwitchEletricityJob";
            scheduleApi.delJob(jobName);

            log.info("add electricity schedule ......");
            AddJobReq eleReq = new AddJobReq();
            eleReq.setCron("0 6 * * * ?");
            eleReq.setJobName(jobName);
            eleReq.setJobClass(ScheduleConstants.AIR_SWITCH_ELECTRICITY_JOB);
            eleReq.setData(map);
            scheduleApi.addJob(eleReq);

            log.info("delete alarm event schedule ......");
            String eventJobName = "countAirSwitchEventJob";
            scheduleApi.delJob(eventJobName);

            log.info("add alarm event schedule ......");
            AddJobReq eventReq = new AddJobReq();
            eventReq.setCron("0 9 * * * ?");
            eventReq.setData(map);
            eventReq.setJobName(eventJobName);
            eventReq.setJobClass(ScheduleConstants.AIR_SWITCH_EVENT_JOB);
            scheduleApi.addJob(eventReq);

        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    /**
     * 启动空开在线定时任务
     */
    public void startOnlineSchedule() {
        try {
            Map map = Maps.newHashMap();
            AddJobReq onlineReq = new AddJobReq();
            onlineReq.setCron("0 0/5 * * * ?");
            onlineReq.setData(map);
            onlineReq.setJobName("airswitchHeartBeatJob");
            onlineReq.setJobClass(ScheduleConstants.AIR_SWITCH_HEART_BEAT_JOB);
            scheduleApi.addJob(onlineReq);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    /**
     * 批量自检
     */
    public void batchLeakageTest(List<String> deviceIdList) {
        log.info("leakage test device id list = {}.", JSON.toJSONString(deviceIdList));
        if (CollectionUtils.isEmpty(deviceIdList)) {
            return;
        }

        for (String deviceId : deviceIdList) {
            GetDeviceInfoRespVo vo = deviceCoreApi.get(deviceId);
            if (vo == null) {
                continue;
            }
            DeviceBusinessTypeResp businessTypeResp = deviceBusinessTypeApi.findById(vo.getTenantId(), vo.getBusinessTypeId());
            // 总自类型的空开才能自检
            if (businessTypeResp != null && !Strings.isNullOrEmpty(businessTypeResp.getBusinessType()) && businessTypeResp.getBusinessType().startsWith("ZA")) {
                leakageTest(deviceId, vo.getTenantId());
            }
        }
    }

    /**
     * 统计首页
     */
    public void countHomePageElectricity(Long tenantId, String currentDate) {
        log.info("start count home page electricity....");

        List<String> deviceIdList = getDeviceIds(-1L, tenantId);

        if (CollectionUtils.isEmpty(deviceIdList)) {
            return;
        }
        SwitchElectricityStatisticsReq req = new SwitchElectricityStatisticsReq();
        req.setTenantId(tenantId);
        req.setDate(currentDate);
        req.setDeviceIds(deviceIdList);

        List<Map> list = getBusinessElectricityInfoFromDb(req);
        log.info("business electricity count = {}.", JSON.toJSONString(list));
        String businessKey = Constants.BUSINESS_ELECTRICITY_COUNT_REDIS + currentDate;
        RedisCacheUtil.valueSet(businessKey, JSON.toJSONString(list), 60*60*24*7L);

        SwitchElectricityStatisticsHeadResp statisticsHeadResp = airSwitchStatisticsService.getStatisticsHeadInfoFromDb(req);
        log.info("head electricity count = {}.", JSON.toJSONString(list));
        String totalKey = Constants.TOTAL_ELECTRICITY_COUNT_REDIS + currentDate;
        RedisCacheUtil.valueSet(totalKey, JSON.toJSONString(statisticsHeadResp), 60*60*24*7L);

        log.info("end count home page electricity....");
    }

    public List<Map> getBusinessElectricityInfoFromDb(SwitchElectricityStatisticsReq req) {
        List resultList = Lists.newArrayList();
        try {
            if (CollectionUtils.isEmpty(req.getDeviceIds())) {
                return resultList;
            }
            ElectricityStatisticsReq minReq = new ElectricityStatisticsReq();
            minReq.setTimeStr(req.getDate());
            minReq.setTenantId(req.getTenantId());
            minReq.setDeviceIds(req.getDeviceIds());
            minReq.setStep(Constants.STATISTICS_HOUR);
            log.info("min req = {}.", JSON.toJSONString(minReq));
            // 获取当天小时电量列表
            List<ElectricityStatisticsRsp> list = electricityStatisticsApi.getMinListByReq(minReq);
//            List<DailyElectricityStatisticsResp> list = airSwitchStatisticsApi.getBusinessElectricityInfo(req);
            Map<String, Double> resultMap = Maps.newHashMap();
            // 获取设备用途
            Map<String, String> businessMap = getBusinessMap(req.getDeviceIds());
            // 统计用途电量
            list.forEach(e-> {
                String businessType = businessMap.get(e.getDeviceId());
                if (!Strings.isNullOrEmpty(businessType)) {
                    resultMap.put(businessType, resultMap.containsKey(businessType) ? (resultMap.get(businessType) + e.getElectricValue()) : e.getElectricValue());
                }
            });

            resultMap.forEach((k, v) -> {
                Map map = Maps.newHashMap();
                map.put("value", v);
                map.put("name", k);
                resultList.add(map);
            });
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return resultList;
    }

    /**
     * 找到空间下面所有的设备列表
     */
    private List<String> getDeviceIds(Long spaceId, Long tenantId) {
        List<SpaceResp> resps = controlSpaceApi.findChild(tenantId, spaceId);
        List<Long> spaceIds = Lists.newArrayList();
        spaceIds.add(spaceId);
        resps.forEach(r-> {
            spaceIds.add(r.getId());
        });

        // find deviceIds
        SpaceAndSpaceDeviceVo vo = new SpaceAndSpaceDeviceVo();
        vo.setTenantId(tenantId);
        vo.setSpaceIds(spaceIds);
        List<SpaceDeviceResp> list = spaceDeviceApi.findSpaceDeviceBySpaceIdsOrDeviceIds(vo);
        List<String> deviceIds = Lists.newArrayList();

        if (CollectionUtils.isEmpty(list)) {
            return deviceIds;
        }
        list.forEach(m-> {
            deviceIds.add(m.getDeviceId());
        });

        if (CollectionUtils.isEmpty(deviceIds)) {
            return deviceIds;
        }

        // 排除空开节点为总路（用途开头为“Z”）
        Map<String, String> businessMap = getBusinessMap(deviceIds);
        if (MapUtils.isEmpty(businessMap)) {
            return deviceIds;
        }
        List<String> idList = Lists.newArrayList();
//        log.info("businessMap = {}.", JSON.toJSONString(businessMap));
//        log.info("deviceIds = {}.", JSON.toJSONString(deviceIds));
        for (String id : deviceIds) {
            if (businessMap.containsKey(id) && !Strings.isNullOrEmpty(businessMap.get(id)) ) {
                if (!businessMap.get(id).startsWith("Z")) {
                    idList.add(id);
                }
            } else {
                idList.add(id);
            }
        }
        return idList;
    }

    /**
     * 获取设备Id和设备用途关联
     */
    private Map<String, String> getBusinessMap(List<String> deviceIds) {
        Map<String, String> businessMap = Maps.newHashMap();
        try {
            Map<String, ListDeviceByParamsRespVo> deviceMap = getDeviceMap(deviceIds);
            List<Long> businessTypeIds=new ArrayList<>();
            deviceMap.forEach((k, v)-> {
                businessTypeIds.add(v.getBusinessTypeId());
            });
            DeviceBusinessTypeReq req=new DeviceBusinessTypeReq();
            req.setIds(businessTypeIds);
            List<DeviceBusinessTypeResp> respList = deviceBusinessTypeApi.findByCondition(req);
            Map<Long, String> business = Maps.newHashMap();
            if (CollectionUtils.isNotEmpty(respList)) {
                respList.forEach(resp->{
                    business.put(resp.getId(),resp.getBusinessType());
                });
                deviceMap.forEach((k, v)-> {
                    businessMap.put(k, business.get(v.getBusinessTypeId()));
                });
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return businessMap;
    }

    /**
     * 获取设备信息
     */
    private Map<String, ListDeviceByParamsRespVo> getDeviceMap(List<String> deviceIds) {
        Map<String, ListDeviceByParamsRespVo> deviceMap = Maps.newHashMap();
        if (CollectionUtils.isEmpty(deviceIds)) {
            return deviceMap;
        }
        try {
            ListDeviceByParamsReq deviceParam = new ListDeviceByParamsReq();
            deviceParam.setDeviceIds(deviceIds);
            List<ListDeviceByParamsRespVo> deviceList = deviceCoreApi.listDeviceByParams(deviceParam);
            deviceList.forEach(d-> {
                deviceMap.put(d.getUuid(), d);
            });
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return deviceMap;
    }

    public static void main(String[] args) {

//        String info = "00100000000000000000000000000001";
        String info = HexUtil.hexToBinary("80011040");
        System.out.println(info);
        for (int i=0; i<info.length(); i++) {
            String vl = String.valueOf(info.charAt(i));
            if (vl.equals("1")) {
                System.out.println(i);
                if (Constants.EarlyWarningList.contains(i)) {
                    System.out.println("warn="+EarlyWarningEnum.getDesc(i));
                }
                if (Constants.AlarmList.contains(i)) {
                    System.out.println("alarm="+AlarmEnum.getDesc(i));
                }
            }
        }
    }


}
