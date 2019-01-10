package com.iot.shcs.device.utils;


import com.iot.shcs.space.vo.DeviceVo;

import java.util.Comparator;

/**
 * @Author: xfz
 * @Descrpiton:
 * @Date: 14:07 2018/7/9
 * @Modify by:
 */
public class DescDeviceVoComparator implements Comparator<DeviceVo> {
    @Override
    public int compare(DeviceVo o1, DeviceVo o2) {
        if (o1 == null) {
            return 0;
        }
        if (o2 == null) {
            return 0;
        }
        if (o1.getOrder() == null) {
            return 0;
        }
        if (o2.getOrder() == null) {
            return 0;
        }
        if (o1.getOrder() - o2.getOrder() < 0) {
            return 0;
        } else {
            return (o1.getOrder() - o2.getOrder()) > 0 ? -1 : 1;
        }
    }
}
