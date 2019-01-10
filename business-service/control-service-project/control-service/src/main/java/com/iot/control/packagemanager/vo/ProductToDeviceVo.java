package com.iot.control.packagemanager.vo;

import lombok.Data;

import java.util.Map;
import java.util.Queue;

/**
 * 描述：产品转设备主键关系类
 * 创建人： LaiGuiMing
 * 创建时间： 2018/10/12 17:17
 */
@Data
public class ProductToDeviceVo {
    Map<Long, String> product2devMap;
    Map<Long, Boolean> product2sameMap;
    Map<Long, Queue<String>> product2QueueMap;
}
