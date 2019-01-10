package com.iot.shcs.space.util;

import com.iot.control.space.vo.SpaceDeviceVo;

import java.util.Comparator;

/**
 * @Author: xfz
 * @Descrpiton:
 * @Date: 14:07 2018/7/9
 * @Modify by:
 */
public class AesSpaceDeviceVoComparator implements Comparator<SpaceDeviceVo> {
    @Override
    public int compare(SpaceDeviceVo o1, SpaceDeviceVo o2) {
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
        if (o1.getOrder() - o2.getOrder() < 0)
            return 0;
        else {
            return (o1.getOrder() - o2.getOrder()) > 0 ? 1 : -1;
        }
    }
}
