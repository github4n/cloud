package com.iot.device.comm.utils;

import com.iot.common.exception.BusinessException;
import com.iot.common.exception.ExceptionEnum;

public class CommonStringUtil {

    public static void checkStringParam(String param, int min, int max, String language) {
        if (min > 0 && param.length() < min) {
            throw new BusinessException(ExceptionEnum.STRING_TOO_SHORT);
        }
        if (param != null && "zh_CN".equals(language)) {
            String regEx = "[\\u4e00-\\u9fa5]";
            String term = param.replaceAll(regEx, "aa");
            if (term.length() - param.length() > max) {
                throw new BusinessException(ExceptionEnum.STRING_TOO_LONG);
            }
        } else {
            if (param != null && param.length() > max) {
                throw new BusinessException(ExceptionEnum.STRING_TOO_LONG);
            }
        }
    }
}
