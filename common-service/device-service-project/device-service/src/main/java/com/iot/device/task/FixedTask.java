package com.iot.device.task;

import com.google.common.collect.Lists;
import com.iot.device.comm.cache.VersionEnum;
import com.iot.device.core.DeviceStateCacheCoreUtils;
import com.iot.device.queue.bean.DeviceStateMessage;
import com.iot.device.queue.process.DeviceStateMessageProcess;
import com.iot.device.vo.req.device.AddCommDeviceStateInfoReq;
import com.iot.device.vo.req.device.UpdateDeviceStateReq;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @Author: lucky
 * @Descrpiton: 固定任务
 * @Date: 14:43 2018/11/8
 * @Modify by:
 */
@EnableScheduling
@Slf4j
@Component
public class FixedTask {

    @Autowired
    private DeviceStateMessageProcess deviceStateMessageProcess;

    @Scheduled(cron = "0 */5 * * * ?")
    public void runSaveDeviceState() {
        Date beginDate = new Date();
        try {
            log.debug(" current Date:{} runSaveDeviceState-task start", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
            runSaveStateCacheTask();
        } catch (Exception e) {
            log.error("runSaveDeviceState-task error.", e);
        }
        Date endTime = new Date();
        long times = endTime.getTime() - beginDate.getTime();
        log.debug("runSaveDeviceState-task end. consumer times:{} 毫秒", times);
    }

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
