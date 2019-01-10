package com.iot.device.web;

import com.google.common.collect.Lists;
import com.iot.device.api.DeviceStateCoreApi;
import com.iot.device.business.DeviceStateBusinessService;
import com.iot.device.business.core.DeviceCoreBusinessService;
import com.iot.device.comm.cache.VersionEnum;
import com.iot.device.core.DeviceStateCacheCoreUtils;
import com.iot.device.queue.bean.DeviceStateMessage;
import com.iot.device.queue.process.DeviceStateMessageProcess;
import com.iot.device.task.DeviceStateTask;
import com.iot.device.task.StateTaskDTO;
import com.iot.device.vo.req.device.AddCommDeviceStateInfoReq;
import com.iot.device.vo.req.device.ListDeviceStateReq;
import com.iot.device.vo.req.device.UpdateDeviceStateReq;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @Author: lucky
 * @Descrpiton:
 * @Date: 11:03 2018/9/25
 * @Modify by:
 */
@Slf4j
@RestController
public class DeviceStateCoreController implements DeviceStateCoreApi {

    @Autowired
    private DeviceCoreBusinessService deviceCoreBusinessService;


    @Autowired
    private DeviceStateBusinessService deviceStateBusinessService;
    @Autowired
    private DeviceStateMessageProcess deviceStateMessageProcess;

    @Override
    public Map<String, Map<String, Object>> listStates(@RequestBody @Validated ListDeviceStateReq params) {
        Map<String, Map<String, Object>> resultDataMap = deviceStateBusinessService.listBatchDeviceStates(params.getTenantId(), params.getDeviceIds());
        return resultDataMap;
    }

    @Override
    public Map<String, Object> get(@RequestParam(value = "tenantId", required = true) Long tenantId
            , @RequestParam(value = "deviceId", required = true) String deviceId) {
        Map<String, Object> resultDataMap = deviceStateBusinessService.getDeviceState(tenantId, deviceId);
        return resultDataMap;
    }

    @Override
    public void saveOrUpdate(@RequestBody UpdateDeviceStateReq params) {
        deviceStateBusinessService.saveOrUpdate(params);
    }

    @Override
    public void saveOrUpdateBatch(@RequestBody List<UpdateDeviceStateReq> params) {
        deviceStateBusinessService.saveOrUpdateBatch(params);
    }

    @Override
    public void recoveryDefaultState(@RequestParam("tenantId") Long tenantId, @RequestParam("deviceId") String deviceId) {
        deviceStateBusinessService.recoveryDefaultState(tenantId, deviceId);
    }

    @Override
    public void testSaveStateTask() {
        log.debug("testSaveStateTask add start");
        int i = 0;
        long j = 0;
        while (true) {

            DeviceStateTask.addCacheStateTask(j, i + "");
            if (i > 2000) {
                break;
            }
            i++;
            j++;
        }

        log.debug("testSaveStateTask add end");
    }

    public void runSaveStateCacheTask() {
        log.debug("runSaveStateCacheTask consumer start");
        //确定消费
        StateTaskDTO index = DeviceStateTask.getCacheIndex();
        if (index == null) {
            index = StateTaskDTO.builder()
                    .currentPage(1)
                    .nextPage(2)
                    .pages(1)
                    .total(0)
                    .whetherConsumer(true)
                    .currentConsumerPage(0) // 如果正在消费 则 currentPage +1  nextPage+1 currentSize =1
                    .pageSize(1000)
                    .currentSize(1)
                    .build();
        } else {
            index.setWhetherConsumer(true);
        }
        index.setCurrentPage(index.getNext());
        index.setNextPage((index.getNext())); // 下一个页码是多少
        DeviceStateTask.setCacheIndex(index);//保存
        //中断防止 有新的落到当前要去消费的队列

        for (long i = index.getCurrentConsumerPage(); i < index.getCurrentPage(); i++) {
            long currentPage = i;
            log.debug("runSaveStateCacheTask consumer page:{}", currentPage);
            index.setCurrentConsumerPage(currentPage);
            Set<String> objectSet = DeviceStateTask.getCacheStateListTask(currentPage);
            if (!CollectionUtils.isEmpty(objectSet)) {
                objectSet.forEach(id -> {

                    String[] args = id.split(":");
                    String deviceId = args[1];
                    Long tenantId = Long.valueOf(args[0]);

                    log.info("---------------------" + tenantId + "----------" + deviceId);
                    Map<String, Object> resultCacheDataMap = DeviceStateCacheCoreUtils.getCacheData(tenantId, deviceId, VersionEnum.V1);

                    if (!CollectionUtils.isEmpty(resultCacheDataMap)) {
                        List<AddCommDeviceStateInfoReq> stateList = Lists.newArrayList();
                        resultCacheDataMap.keySet().forEach(key -> {
                            AddCommDeviceStateInfoReq state = new AddCommDeviceStateInfoReq();
                            state.setPropertyName(key);
                            state.setPropertyValue(resultCacheDataMap.get(key) + "");
                            stateList.add(state);
                        });
                        UpdateDeviceStateReq stateReq = UpdateDeviceStateReq.builder().stateList(stateList)
                                .deviceId(deviceId).tenantId(tenantId).build();

                        List<UpdateDeviceStateReq> resultDataList = Lists.newArrayList();
                        resultDataList.add(stateReq);

                        deviceStateMessageProcess.processMessage(DeviceStateMessage.builder().params(resultDataList).build());
                    }

                });
            }
            DeviceStateTask.removeCacheStateListTask(currentPage);

        }

        //关闭消费
        index.setWhetherConsumer(false);
        DeviceStateTask.setCacheIndex(index);
        log.debug("runSaveStateCacheTask consumer end");
    }
}
