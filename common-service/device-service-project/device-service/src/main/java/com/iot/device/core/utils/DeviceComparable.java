package com.iot.device.core.utils;

import com.iot.device.model.Device;

/**
 * @Author: xfz
 * @Descrpiton:
 * @Date: 11:10 2018/6/25
 * @Modify by:
 */
public class DeviceComparable implements Comparable<Device> {

    private Long id;

    public DeviceComparable(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    @Override
    public int compareTo(Device o) {
        return Integer.valueOf((this.id - o.getId()) + "");
    }
}
