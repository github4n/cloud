package com.iot.control.space.util;

import com.iot.control.space.domain.Space;

import java.util.Comparator;

/**
 * @Author: xfz
 * @Descrpiton:
 * @Date: 14:07 2018/7/9
 * @Modify by:
 */
public class AesSpaceComparator implements Comparator<Space> {
    @Override
    public int compare(Space o1, Space o2) {

        if (o1.getId().compareTo(o2.getId()) < 0) {
            return -1;
        } else if (o1.getId().compareTo(o2.getId()) > 0) {
            return 0;
        }
        return 1;
    }

}
