package com.iot.center.service;

import com.alibaba.fastjson.JSON;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.iot.airswitch.api.AirSwitchApi;
import com.iot.airswitch.api.AirSwitchStatisticsApi;
import com.iot.airswitch.vo.req.SwitchElectricityStatisticsReq;
import com.iot.airswitch.vo.resp.SwitchElectricityStatisticsHeadResp;
import com.iot.airswitch.vo.resp.SwitchElectricityStatisticsTopResp;
import com.iot.airswitch.vo.resp.SwtichElectricityStatisticsChartResp;
import com.iot.building.device.api.DeviceBusinessTypeApi;
import com.iot.building.device.vo.DeviceBusinessTypeReq;
import com.iot.building.device.vo.DeviceBusinessTypeResp;
import com.iot.building.space.api.SpaceApi;
import com.iot.building.space.vo.QueryParamReq;
import com.iot.center.helper.Constants;
import com.iot.common.beans.CommonResponse;
import com.iot.control.space.api.SpaceDeviceApi;
import com.iot.control.space.vo.SpaceAndSpaceDeviceVo;
import com.iot.control.space.vo.SpaceDeviceResp;
import com.iot.control.space.vo.SpaceResp;
import com.iot.device.api.DeviceCoreApi;
import com.iot.device.vo.req.device.ListDeviceByParamsReq;
import com.iot.device.vo.rsp.DailyElectricityStatisticsResp;
import com.iot.device.vo.rsp.device.GetDeviceInfoRespVo;
import com.iot.device.vo.rsp.device.ListDeviceByParamsRespVo;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Author: Xieby
 * @Date: 2018/11/6
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
    private AirSwitchApi airSwitchApi;
    @Autowired
    private DeviceCoreApi deviceCoreApi;
    @Autowired
    private SpaceDeviceApi spaceDeviceApi;
    @Autowired
    private DeviceBusinessTypeApi businessTypeApi;
    @Autowired
    private AirSwitchStatisticsApi airSwitchStatisticsApi;

    /**
     * 获取告警头部
     */
    public CommonResponse<SwitchElectricityStatisticsHeadResp> getStatisticsHeadInfo(SwitchElectricityStatisticsReq req, Long tenantId) {
        log.info("start getStatisticsHeadInfo...1");
        createReq(req, tenantId, true);
        log.info("start getStatisticsHeadInfo...2");
        SwitchElectricityStatisticsHeadResp resp = airSwitchStatisticsApi.getStatisticsHeadInfo(req);
        log.info("start getStatisticsHeadInfo...3");
        return CommonResponse.success(resp);
    }

    /**
     * 获取告警图表
     */
    public CommonResponse<SwtichElectricityStatisticsChartResp> getStatisticsChartInfo(SwitchElectricityStatisticsReq req, Long tenantId) {
        log.info("start getStatisticsChartInfo...1");
        createReq(req, tenantId, true);
        log.info("start getStatisticsChartInfo...2");
        SwtichElectricityStatisticsChartResp resp = airSwitchStatisticsApi.getStatisticsChartInfo(req);
        log.info("start getStatisticsChartInfo...3");
        return CommonResponse.success(resp);
    }

    /**
     * 获取电量统计Top
     */
    public CommonResponse<SwitchElectricityStatisticsTopResp> getStatisticsTopInfo(SwitchElectricityStatisticsReq req, Long tenantId) {
        log.info("start getStatisticsTopInfo >>>>>>> 1");
        createReq(req, tenantId, true);
        req.setTop(10);
        log.info("start getStatisticsTopInfo >>>>>>> 2");
        SwitchElectricityStatisticsTopResp resp = airSwitchStatisticsApi.getStatisticsTopInfo(req);
        List<String> deviceIds = Lists.newArrayList();

        if (CollectionUtils.isEmpty(resp.getList())) {
            return CommonResponse.success(resp);
        }

        resp.getList().forEach(t-> {
            deviceIds.add(t.getDeviceId());
        });
        log.info("start getStatisticsTopInfo >>>>>>> 3");
        Map<String, ListDeviceByParamsRespVo> deviceMap = getDeviceMap(deviceIds);
        Map<String, Long> spaceDeviceMap = getSpaceDeviceMap(deviceIds, tenantId);
        Map<Long, String> spaceMap = getSpaceMap(Lists.newArrayList(spaceDeviceMap.values()), tenantId);
        Map<String, String> businessMap = getBusinessMap(req.getDeviceIds());
        log.info("start getStatisticsTopInfo >>>>>>> 4");
        resp.getList().forEach(d-> {
            Long spaceId = spaceDeviceMap.get(d.getDeviceId());
            if (deviceMap.containsKey(d.getDeviceId())) {
                d.setDeviceName(deviceMap.get(d.getDeviceId()).getName());
            }
            d.setSpaceId(spaceId);
            d.setUseage(businessMap.get(d.getDeviceId()));
            d.setSpaceName(spaceId == null ? null : spaceMap.get(spaceId));
        });

        return CommonResponse.success(resp);
    }

    /**
     * 获取设备信息
     */
    private Map<String, ListDeviceByParamsRespVo> getDeviceMap(List<String> deviceIds) {
        ListDeviceByParamsReq deviceParam = new ListDeviceByParamsReq();
        deviceParam.setDeviceIds(deviceIds);
        List<ListDeviceByParamsRespVo> deviceList = deviceCoreApi.listDeviceByParams(deviceParam);
        Map<String, ListDeviceByParamsRespVo> deviceMap = Maps.newHashMap();
        deviceList.forEach(d-> {
            deviceMap.put(d.getUuid(), d);
        });
        return deviceMap;
    }

    /**
     * 获取空间信息
     */
    private Map<Long, String> getSpaceMap(List<Long> spaceIds, Long tenantId) {
        SpaceAndSpaceDeviceVo req=new SpaceAndSpaceDeviceVo();
        req.setSpaceIds(spaceIds);
        req.setTenantId(tenantId);
        List<SpaceResp> spaceList = controlSpaceApi.findSpaceInfoBySpaceIds(req);
        Map<Long, String> spaceMap = Maps.newHashMap();
        spaceList.forEach(s-> {
            spaceMap.put(s.getId(), s.getName());
        });
        return spaceMap;
    }

    /**
     * 获取设备空间关系
     */
    private Map<String, Long> getSpaceDeviceMap(List<String> deviceIds, Long tenantId) {
        SpaceAndSpaceDeviceVo vo = new SpaceAndSpaceDeviceVo();
        vo.setTenantId(tenantId);
        vo.setDeviceIds(deviceIds);
        List<SpaceDeviceResp> list = spaceDeviceApi.findSpaceDeviceBySpaceIdsOrDeviceIds(vo);
        Map<String, Long> spaceDeviceMap = Maps.newHashMap();
        list.forEach(l-> {
            spaceDeviceMap.put(l.getDeviceId(), l.getSpaceId());
        });
        return spaceDeviceMap;
    }

    /**
     * 获取事件图表信息
     */
    public CommonResponse<SwtichElectricityStatisticsChartResp> getEventChartInfo(SwitchElectricityStatisticsReq req, Long tenantId) {
        log.info("start getEventChartInfo >>>>>>> 1");
        createReq(req, tenantId, false);
        log.info("start getEventChartInfo >>>>>>> 2");
        SwtichElectricityStatisticsChartResp resp = airSwitchStatisticsApi.getEventStatisticsInfo(req);
        log.info("start getEventChartInfo >>>>>>> 3");
        return CommonResponse.success(resp);
    }

    /**
     * 获取告警消息
     */
    public CommonResponse<SwitchElectricityStatisticsTopResp> getEventTopInfo(SwitchElectricityStatisticsReq req, Long tenantId) {
        log.info("start getEventTopInfo >>>>>>> 1");
        createReq(req, tenantId, false);
        req.setTop(10);
        log.info("start getEventTopInfo >>>>>>> 2");
        SwitchElectricityStatisticsTopResp resp = airSwitchStatisticsApi.getEventTopInfo(req);
        log.info("start getEventTopInfo >>>>>>> 3");
        List<String> deviceIds = Lists.newArrayList();
        if (CollectionUtils.isEmpty(resp.getList())) {
            return CommonResponse.success(resp);
        }
        resp.getList().forEach(t-> {
            deviceIds.add(t.getDeviceId());
        });
        Map<String, ListDeviceByParamsRespVo> deviceMap = getDeviceMap(deviceIds);
        Map<String, Long> spaceDeviceMap = getSpaceDeviceMap(deviceIds, tenantId);
        Map<Long, String> spaceMap = getSpaceMap(Lists.newArrayList(spaceDeviceMap.values()), tenantId);
        log.info("start getEventTopInfo >>>>>>> 4");
        resp.getList().forEach(d-> {
            Long spaceId = spaceDeviceMap.get(d.getDeviceId());
            d.setSpaceId(spaceId);
            if (deviceMap.containsKey(d.getDeviceId())) {
                d.setDeviceName(deviceMap.get(d.getDeviceId()).getName());
            }
            d.setSpaceName(spaceId == null ? null : spaceMap.get(spaceId));
        });
        return CommonResponse.success(resp);
    }

    /**
     * 获取当天用途统计电量信息
     */
    public CommonResponse<List> getBusinessElectricityInfo(Long tenantId) {
        log.info("start getBusinessElectricityInfo...1");
        SwitchElectricityStatisticsReq req = new SwitchElectricityStatisticsReq();
        req.setSpaceId(-1L);
        req.setDate(DateFormatUtils.format(new Date(), "yyyy-MM-dd"));
        createReq(req, tenantId, true);
        log.info("start getBusinessElectricityInfo...2");
        List<Map> list = airSwitchStatisticsApi.getTotalElectricityList(req);
//        List<DailyElectricityStatisticsResp> list = airSwitchStatisticsApi.getBusinessElectricityInfo(req);
//        List resultList = Lists.newArrayList();
//        Map<String, Double> resultMap = Maps.newHashMap();
//        Map<String, String> businessMap = getBusinessMap(req.getDeviceIds());
//
//        list.forEach(e-> {
//            String businessType = businessMap.get(e.getDeviceId());
//            if (!Strings.isNullOrEmpty(businessType)) {
//                resultMap.put(businessType, resultMap.containsKey(businessType) ? (resultMap.get(businessType) + e.getElectricValue()) : e.getElectricValue());
//            }
//        });
//
//        resultMap.forEach((k, v) -> {
//            Map map = Maps.newHashMap();
//            map.put("name", k);
//            map.put("value", v);
//            resultList.add(map);
//        });
        log.info("start getBusinessElectricityInfo...3");
        return CommonResponse.success(list);
    }

    /**
     * 获取设备Id和设备用途关联
     */
    private Map<String, String> getBusinessMap(List<String> deviceIds) {
        Map<String, String> businessMap = Maps.newHashMap();
        if (CollectionUtils.isEmpty(deviceIds)) {
            return businessMap;
        }
        Map<String, ListDeviceByParamsRespVo> deviceMap = getDeviceMap(deviceIds);
        List<Long> businessTypeIds = Lists.newArrayList();
        deviceMap.forEach((k,v) -> {
            if (v.getBusinessTypeId() != null) {
                businessTypeIds.add(v.getBusinessTypeId());
            }
        });
        DeviceBusinessTypeReq req=new DeviceBusinessTypeReq();
        req.setIds(businessTypeIds);
        List<DeviceBusinessTypeResp> respList = businessTypeApi.findByCondition(req);
        Map<Long, String> bMap = Maps.newHashMap();
        respList.forEach(b-> {
            bMap.put(b.getId(), b.getBusinessType());
        });
        deviceMap.forEach((k, v)-> {
           businessMap.put(k, bMap.get(v.getBusinessTypeId()));
        });
        return businessMap;
    }


    private void createReq(SwitchElectricityStatisticsReq req, Long tenantId, boolean isElectricityFlag) {
        List<String> deviceIds = getDeviceIds(req.getSpaceId(), tenantId, isElectricityFlag);
//        List<String> deviceIds = Lists.newArrayList("98CC4D10036401", "98CC4D10036402", "98CC4D10036403");
        req.setTenantId(tenantId);
        req.setDeviceIds(deviceIds);
        req.setDate(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));

    }

    /**
     * 设置服务器指向
     */
    public CommonResponse setServerAddress(Long tenantId, String deviceId, String serverAddress) {
        String[] strings = serverAddress.split(":");
        airSwitchApi.setServerAddress(tenantId, deviceId, strings[0], strings[1]);
        return CommonResponse.success();
    }

    /**
     * 对设备进行漏电测试
     */
    public CommonResponse leakageTest(String deviceId, Long tenantId) {
        airSwitchApi.leakageTest(deviceId, tenantId);
        return CommonResponse.success();
    }

    /**
     * 对设备进行开闸
     */
    public CommonResponse switchOn(String deviceId, Long tenantId) {
        airSwitchApi.switchOn(deviceId, tenantId);
        return CommonResponse.success();
    }

    /**
     * 对设备进行合闸
     */
    public CommonResponse switchOff(String deviceId, Long tenantId) {
        airSwitchApi.switchOff(deviceId, tenantId);
        return CommonResponse.success();
    }

    /**
     * 设置上传时间间隔
     */
    public CommonResponse setRTVI(String deviceId, Integer interval, Long tenantId) {
        airSwitchApi.setRTVI(deviceId, interval, tenantId);
        return CommonResponse.success();
    }

    /**
     * 找到空间下面所有的设备列表
     */
    private List<String> getDeviceIds(Long spaceId, Long tenantId, boolean isElectricityFlag) {
        List<String> deviceIds = Lists.newArrayList();

        try {
            List<SpaceResp> spaceList=controlSpaceApi.findChild(tenantId, spaceId);
            List<Long> spaceIds = Lists.newArrayList();
            spaceIds.add(spaceId);
            spaceList.forEach(r-> {
                spaceIds.add(r.getId());
            });

            // find deviceIds
            SpaceAndSpaceDeviceVo vo = new SpaceAndSpaceDeviceVo();
            vo.setSpaceIds(spaceIds);
            vo.setTenantId(tenantId);
            log.info("SpaceAndSpaceDeviceVo = {}.", JSON.toJSONString(vo));
            List<SpaceDeviceResp> list = spaceDeviceApi.findSpaceDeviceBySpaceIdsOrDeviceIds(vo);

            list.forEach(m-> {
                deviceIds.add(m.getDeviceId());
            });

            if (CollectionUtils.isEmpty(deviceIds)) {
                return deviceIds;
            }


            // 排除空开节点为总路（用途开头为“Z”）
            if (isElectricityFlag) {
                List<String> idList = Lists.newArrayList();
                Map<String, String> businessMap = getBusinessMap(deviceIds);
                log.info("bussMap = {}, deviceId = {}.", JSON.toJSONString(businessMap), JSON.toJSONString(deviceIds));
                deviceIds.forEach(id-> {
                    if (businessMap.containsKey(id) && !Strings.isNullOrEmpty(businessMap.get(id)) ) {
                        if (!businessMap.get(id).startsWith("Z")) {
                            idList.add(id);
                        }
                    } else {
                        idList.add(id);
                    }
                });
                return idList;
            }

        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }

        return deviceIds;
    }

    public CommonResponse test() {

        List<String> deviceIds = Lists.newArrayList("98CC4D10036401", "98CC4D10036402", "98CC4D10036403");

        try {
            airSwitchApi.batchLeakageTest(deviceIds);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }

//        airSwitchApi.modifyDeviceName("98CC4D100364", "开空01");

//        airSwitchApi.switchOff("98CC4D10036401", 11L);
//
//        try {
//            Thread.sleep(3000L);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//
//        airSwitchApi.switchOn("98CC4D10036401", 11L);

//        airSwitchApi.eventPush("F1A4E103005405DD98CC4D1000B9100801B74534020005FFFFFF055BCFE8B680010000C0000000000000000001B84534020005FFFFFF055BCFE8B6800000008001000005FFFFFFB601B94534020005FFFFFF055BCFE8B7C000000080000000000000000060124721");
        return CommonResponse.success();
    }


    public CommonResponse syncEle(String date, Long tenantId) {
        airSwitchStatisticsApi.syncElectricity(date, tenantId);
        return CommonResponse.success();
    }
}
