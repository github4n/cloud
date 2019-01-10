package com.iot.schedule.job;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.iot.common.beans.BeanUtil;
import com.iot.common.helper.ApplicationContextHelper;
import com.iot.device.api.DeviceCoreApi;
import com.iot.device.api.DeviceStatusCoreApi;
import com.iot.device.enums.OnlineStatusEnum;
import com.iot.device.vo.req.device.ListDeviceByParamsReq;
import com.iot.device.vo.req.device.UpdateDeviceStatusReq;
import com.iot.device.vo.rsp.device.GetDeviceStatusInfoRespVo;
import com.iot.device.vo.rsp.device.ListDeviceByParamsRespVo;
import com.iot.redis.RedisCacheUtil;
import org.apache.commons.lang3.StringUtils;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * @Author: Xieby
 * @Date: 2018/12/4
 * @Description: *
 */
public class AirSwitchHeartBeatJob implements Job {

    public static final Long air_switch_product_id = 1090210993L;
    public static final Long air_switch_node_product_id = 1090210994L;
    public static final String AIR_SWITCH_HEART_BEAT_PREFIX = "airswitch:heartbeat:";

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {

        System.out.println("========================= air switch heart beat job start ======================" + new Date());

        try {

            List<ListDeviceByParamsRespVo> totalList = Lists.newArrayList();

            DeviceCoreApi deviceCoreApi = ApplicationContextHelper.getBean(DeviceCoreApi.class);
            DeviceStatusCoreApi deviceStatusCoreApi = ApplicationContextHelper.getBean(DeviceStatusCoreApi.class);

            ListDeviceByParamsReq req = new ListDeviceByParamsReq();
            // 空开产品ID
            req.setProductId(air_switch_product_id);
            List<ListDeviceByParamsRespVo> list1 = deviceCoreApi.listDeviceByParams(req);
            // 空开节点产品ID
            req.setProductId(air_switch_node_product_id);
            List<ListDeviceByParamsRespVo> list2 = deviceCoreApi.listDeviceByParams(req);

            totalList.addAll(list1);
            totalList.addAll(list2);

            Set<String> keySet = RedisCacheUtil.keys(AIR_SWITCH_HEART_BEAT_PREFIX + "*");

            Set<String> deviceUuidSet = Sets.newHashSet();
            keySet.forEach(k-> {
                deviceUuidSet.add(StringUtils.difference(AIR_SWITCH_HEART_BEAT_PREFIX, k));
            });
            System.out.println("find redis device uuid list = " + JSON.toJSONString(deviceUuidSet));
            System.out.println("find db device info list = " + JSON.toJSONString(totalList));

            totalList.forEach(d-> {
                String deviceUuid = d.getUuid();
                GetDeviceStatusInfoRespVo v = deviceStatusCoreApi.get(d.getTenantId(), deviceUuid);
                // 判断redis是否有空开的心跳状态
                if (!deviceUuidSet.contains(deviceUuid) && v != null) {
                    v.setOnlineStatus(OnlineStatusEnum.DISCONNECTED.getCode());
                    UpdateDeviceStatusReq r = new UpdateDeviceStatusReq();
                    BeanUtil.copyProperties(v, r);
                    deviceStatusCoreApi.saveOrUpdate(r);
                    System.out.println("set device = " + deviceUuid + " disconnected.");
                } else if (deviceUuidSet.contains(deviceUuid) && v != null) {
                    v.setOnlineStatus(OnlineStatusEnum.CONNECTED.getCode());
                    UpdateDeviceStatusReq r = new UpdateDeviceStatusReq();
                    BeanUtil.copyProperties(v, r);
                    deviceStatusCoreApi.saveOrUpdate(r);
                    System.out.println("set device = " + deviceUuid + " connected.");
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("========================= air switch heart beat job end ======================" + new Date());
    }

    public static void main(String[] args) {
        String uuidList = "[\"98CC4D10036601\",\"98CC4D10036403\",\"98CC4D10036602\",\"98CC4D100366\",\"98CC4D10036401\",\"98CC4D10036402\",\"98CC4D1000B904\",\"98CC4D10036605\",\"98CC4D1000B903\",\"98CC4D10036603\",\"98CC4D10036604\",\"98CC4D1000B905\",\"98CC4D1000B902\",\"98CC4D1000B901\",\"98CC4D1000B9\",\"98CC4D100364\"]";

        List<String> uList = JSONArray.parseArray(uuidList, String.class);

        String deviceList = "[{\"devType\":\"AirSwitch\",\"deviceTypeId\":-4000,\"id\":322,\"ip\":\"172.16.28.96\",\"isDirectDevice\":1,\"locationId\":3,\"mac\":\"98CC4D100366\",\"name\":\"H栋1层\",\"productId\":1090210993,\"tenantId\":11,\"uuid\":\"98CC4D100366\"},{\"devType\":\"AirSwitch\",\"deviceTypeId\":-4000,\"id\":388,\"ip\":\"172.16.28.238\",\"isDirectDevice\":1,\"locationId\":1,\"mac\":\"98CC4D1000B9\",\"name\":\"空开0B9\",\"productId\":1090210993,\"tenantId\":11,\"uuid\":\"98CC4D1000B9\"},{\"devType\":\"AirSwitch\",\"deviceTypeId\":-4000,\"id\":409,\"ip\":\"172.16.28.230\",\"isDirectDevice\":1,\"locationId\":1,\"mac\":\"98CC4D100364\",\"name\":\"开空364\",\"productId\":1090210993,\"tenantId\":11,\"uuid\":\"98CC4D100364\"},{\"businessTypeId\":60,\"devType\":\"AirSwitchNode\",\"deviceTypeId\":-4001,\"id\":323,\"isDirectDevice\":0,\"locationId\":3,\"mac\":\"98CC4D10036603\",\"name\":\"AirConditionSwitch_98CC4D10036603\",\"parentId\":\"98CC4D100366\",\"productId\":1090210994,\"tenantId\":11,\"uuid\":\"98CC4D10036603\"},{\"businessTypeId\":60,\"devType\":\"AirSwitchNode\",\"deviceTypeId\":-4001,\"id\":324,\"isDirectDevice\":0,\"locationId\":3,\"mac\":\"98CC4D10036601\",\"name\":\"AirConditionSwitch_98CC4D10036601\",\"parentId\":\"98CC4D100366\",\"productId\":1090210994,\"tenantId\":11,\"uuid\":\"98CC4D10036601\"},{\"businessTypeId\":60,\"devType\":\"AirSwitchNode\",\"deviceTypeId\":-4001,\"id\":325,\"isDirectDevice\":0,\"locationId\":3,\"mac\":\"98CC4D10036602\",\"name\":\"AirConditionSwitch_98CC4D10036602\",\"parentId\":\"98CC4D100366\",\"productId\":1090210994,\"tenantId\":11,\"uuid\":\"98CC4D10036602\"},{\"businessTypeId\":60,\"devType\":\"AirSwitchNode\",\"deviceTypeId\":-4001,\"id\":326,\"isDirectDevice\":0,\"locationId\":3,\"mac\":\"98CC4D10036604\",\"name\":\"AirConditionSwitch_98CC4D10036604\",\"parentId\":\"98CC4D100366\",\"productId\":1090210994,\"tenantId\":11,\"uuid\":\"98CC4D10036604\"},{\"businessTypeId\":60,\"devType\":\"AirSwitchNode\",\"deviceTypeId\":-4001,\"id\":327,\"isDirectDevice\":0,\"locationId\":3,\"mac\":\"98CC4D10036605\",\"name\":\"AirConditionSwitch_98CC4D10036605\",\"parentId\":\"98CC4D100366\",\"productId\":1090210994,\"tenantId\":11,\"uuid\":\"98CC4D10036605\"},{\"devType\":\"AirSwitchNode\",\"deviceTypeId\":-4001,\"id\":389,\"isDirectDevice\":0,\"locationId\":1,\"mac\":\"98CC4D1000B901\",\"name\":\"98CC4D1000B901\",\"parentId\":\"98CC4D1000B9\",\"productId\":1090210994,\"tenantId\":11,\"uuid\":\"98CC4D1000B901\"},{\"devType\":\"AirSwitchNode\",\"deviceTypeId\":-4001,\"id\":390,\"isDirectDevice\":0,\"locationId\":1,\"mac\":\"98CC4D1000B902\",\"name\":\"98CC4D1000B902\",\"parentId\":\"98CC4D1000B9\",\"productId\":1090210994,\"tenantId\":11,\"uuid\":\"98CC4D1000B902\"},{\"devType\":\"AirSwitchNode\",\"deviceTypeId\":-4001,\"id\":391,\"isDirectDevice\":0,\"locationId\":1,\"mac\":\"98CC4D1000B903\",\"name\":\"98CC4D1000B903\",\"parentId\":\"98CC4D1000B9\",\"productId\":1090210994,\"tenantId\":11,\"uuid\":\"98CC4D1000B903\"},{\"devType\":\"AirSwitchNode\",\"deviceTypeId\":-4001,\"id\":392,\"isDirectDevice\":0,\"locationId\":1,\"mac\":\"98CC4D1000B904\",\"name\":\"98CC4D1000B904\",\"parentId\":\"98CC4D1000B9\",\"productId\":1090210994,\"tenantId\":11,\"uuid\":\"98CC4D1000B904\"},{\"devType\":\"AirSwitchNode\",\"deviceTypeId\":-4001,\"id\":393,\"isDirectDevice\":0,\"locationId\":1,\"mac\":\"98CC4D1000B905\",\"name\":\"98CC4D1000B905\",\"parentId\":\"98CC4D1000B9\",\"productId\":1090210994,\"tenantId\":11,\"uuid\":\"98CC4D1000B905\"},{\"businessTypeId\":177,\"devType\":\"AirSwitchNode\",\"deviceTypeId\":-4001,\"id\":410,\"isDirectDevice\":0,\"locationId\":1,\"mac\":\"98CC4D10036401\",\"name\":\"ZA总开关_44\",\"parentId\":\"98CC4D100364\",\"productId\":1090210994,\"tenantId\":11,\"uuid\":\"98CC4D10036401\"},{\"businessTypeId\":178,\"devType\":\"AirSwitchNode\",\"deviceTypeId\":-4001,\"id\":411,\"isDirectDevice\":0,\"locationId\":1,\"mac\":\"98CC4D10036402\",\"name\":\"空调_45\",\"parentId\":\"98CC4D100364\",\"productId\":1090210994,\"tenantId\":11,\"uuid\":\"98CC4D10036402\"},{\"businessTypeId\":180,\"devType\":\"AirSwitchNode\",\"deviceTypeId\":-4001,\"id\":412,\"isDirectDevice\":0,\"locationId\":1,\"mac\":\"98CC4D10036403\",\"name\":\"电脑_46\",\"parentId\":\"98CC4D100364\",\"productId\":1090210994,\"tenantId\":11,\"uuid\":\"98CC4D10036403\"}]";

        List<ListDeviceByParamsRespVo> dList = JSONArray.parseArray(deviceList, ListDeviceByParamsRespVo.class);

        dList.forEach(d -> {
            String deviceUuid = d.getUuid();
            if (!uList.contains(deviceUuid)) {
                System.out.println("heart beat = " + deviceUuid);
            }
        });

    }

}
