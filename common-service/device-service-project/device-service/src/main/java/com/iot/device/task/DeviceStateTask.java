package com.iot.device.task;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Sets;
import com.iot.redis.RedisCacheUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.Set;

/**
 * @Author: lucky
 * @Descrpiton: state 任务
 * @Date: 14:40 2018/11/8
 * @Modify by:
 */
@Slf4j
@Component
public class DeviceStateTask {

    public static final String INDEX_DEVICE_STATE_TASK = "state:index_task_device_state";

    public static final String ZSET__DEVICE_STATE_TASK = "state:zset_device_state:";

    public static final String LIST_DEVICE_STATE_TASK = "state:list_device:state";

    public static final long MAX_PAGE_SIZE = 200;

    public static String getDeviceStateTaskKey(long currentPage) {
        return ZSET__DEVICE_STATE_TASK + currentPage;
    }

    public static StateTaskDTO getCacheIndex() {
        String cacheValue = RedisCacheUtil.valueGet(INDEX_DEVICE_STATE_TASK);
        if (StringUtils.isEmpty(cacheValue)) {
            return null;
        }
        StateTaskDTO stateTask = JSON.parseObject(cacheValue, StateTaskDTO.class);
        return stateTask;
    }

    public static void setCacheIndex(StateTaskDTO index) {
        RedisCacheUtil.valueSet(INDEX_DEVICE_STATE_TASK, JSON.toJSONString(index));
    }

    private static long getPages(long total) {
        long pages = 0;
        if (total == 0) {
            return 0;
        }
        pages = total / MAX_PAGE_SIZE;
        if (total % MAX_PAGE_SIZE != 0) {
            pages++;
        }
        return pages;
    }

    public static Set<String> getCacheStateListTask(long currentPage) {
        long totalSize = RedisCacheUtil.zSetSize(getDeviceStateTaskKey(currentPage));
        Set<String> resultDataList = Sets.newHashSet();
        long pages = getPages(totalSize);
        if (pages > 0) {
            for (long i = 1; i <= pages; i++) {
                long start = (i - 1) * MAX_PAGE_SIZE;
                long end = i * MAX_PAGE_SIZE;
                Set<String> values =
                        RedisCacheUtil.zSetReverseRange(getDeviceStateTaskKey(currentPage), start, end);
                if (!CollectionUtils.isEmpty(values)) {
                    resultDataList.addAll(values);
                }
            }
        }
        return resultDataList;
    }

    public static void removeCacheStateListTask(long currentPage) {
        RedisCacheUtil.delete(getDeviceStateTaskKey(currentPage));

    }

    /**
     * 注意 在消费的地方进行 设置正在消费 并且设置他的现在可用的当前页+1
     *
     * @param tenantId 租户
     * @param deviceId 设备id
     * @return
     * @author lucky
     * @date 2018/11/8 17:36
     */
    public static StateTaskDTO addCacheStateTask(Long tenantId, String deviceId) {
        String value = tenantId + ":" + deviceId;
        StateTaskDTO index = getCacheIndex();
        if (index == null) {
            index =
                    StateTaskDTO.builder()
                            .currentPage(1)
                            .nextPage(2)
                            .pages(1)
                            .total(0)
                            .whetherConsumer(false)
                            .currentConsumerPage(0) // 如果正在消费 则 currentPage +1  nextPage+1 currentSize =1
                            .pageSize(1000)
                            .currentSize(1)
                            .build();
            // add cache index
            setCacheIndex(index);
            // add cache value
            boolean isAdd = RedisCacheUtil.zSetAdd(
                    getDeviceStateTaskKey(index.getCurrentPage()), value, index.getCurrentSize());
            if (isAdd) {
                index.setTotal(index.getTotal() + 1);
            }
            return index;
        }

        long size = RedisCacheUtil.zSetSize(getDeviceStateTaskKey(index.getCurrentPage()));//获取当前页队列的大小 如果超过指定的页大小 则放下一个队列
        if (size > 0) {
            if (size < index.getPageSize()) {
                // 不足可以添加
                boolean isAdd = RedisCacheUtil.zSetAdd(
                        getDeviceStateTaskKey(index.getCurrentPage()), value, index.getCurrentSize());
                if (isAdd) {
                    index.setTotal(index.getTotal() + 1);
                    size = size + 1;
                }
            } else {
                // 重新开一个队列添加
                index.setCurrentPage(index.getNext());
                index.setNextPage((index.getNext())); // 下一个页码是多少

                index.setPages(index.getPages() + 1);//总页码+1
                boolean isAdd = RedisCacheUtil.zSetAdd(
                        getDeviceStateTaskKey(index.getCurrentPage()), value, index.getCurrentSize());
                if (isAdd) {
                    index.setTotal(index.getTotal() + 1);
                    size = size + 1;
                }
            }
            index.setCurrentSize(size);
        } else {
            //队列大小为0直接往里加
            index.setCurrentSize(1);
            RedisCacheUtil.zSetAdd(
                    getDeviceStateTaskKey(index.getCurrentPage()), value, index.getCurrentSize());
            index.setTotal(index.getTotal() + 1);
        }
        // add cache index
        setCacheIndex(index);
        return index;
    }


}
