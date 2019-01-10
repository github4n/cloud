package com.iot.tenant.util;

import com.iot.common.util.StringUtil;

public class CheckVersionUtil {

    /*
     * 0 - 不需要升级
     * 1 - 增量升级
     * 2 - 全量升级
     * */
    public static int compareVersion(String oldVer, String newVer) {
        if (StringUtil.isEmpty(newVer)) {
            return 0;
        } else if (StringUtil.isEmpty(oldVer)) {
            return 2;
        }

        String[] oldVerList = oldVer.split("\\.");
        String[] newVerList = newVer.split("\\.");

        if (newVerList.length > oldVerList.length) {
            return 2;
        } else if (newVerList.length < oldVerList.length){
            return 0;
        } else {
            for (int i=0; i < newVerList.length; i++) {
                Boolean isLast = i == (newVerList.length-1);
                Integer newVersion = Integer.parseInt(newVerList[i]);
                Integer oldVersion = Integer.parseInt(oldVerList[i]);
                if (isLast && (newVersion - oldVersion == 1)) {
                    return 1;
                } else {
                    if (newVersion > oldVersion) {
                        return 2;
                    } else if (newVersion < oldVersion) {
                        return 0;
                    }
                }
            }

            return 0;
        }

    }
}
