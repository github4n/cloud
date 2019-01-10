package com.iot.shcs.device.utils;

import com.iot.common.exception.BusinessException;
import com.iot.shcs.device.enums.DeviceCoreExceptionEnum;
import com.iot.user.constant.UserConstants;

/**
 * @Author: xfz
 * @Descrpiton:
 * @Date: 16:06 2018/6/13
 * @Modify by:
 */
public class UserUtils {


    /**
     * 检查用户是否在线
     *
     * @param userState 用户的上线状态
     * @return
     * @author lucky
     * @date 2018/6/13 16:14
     */
    public static boolean checkUserWhetherOnline(Byte userState) {
        if (UserConstants.USERSTATE_OFFLINE.compareTo(userState) == 0) {
            //不在线
            throw new BusinessException(DeviceCoreExceptionEnum.USER_DISCONNECTED);

        }
        return true;
    }
}
