package com.iot.common.util;

import com.iot.common.exception.BusinessException;
import com.iot.common.exception.ExceptionEnum;

import java.util.regex.Pattern;

/**
 * 项目名称：IOT云平台
 * 模块名称：常用工具
 * 功能描述：校验工具
 * 创建人： mao2080@sina.com
 * 创建时间：2017年4月1日 下午4:21:21
 * 修改人： mao2080@sina.com
 * 修改时间：2017年4月1日 下午4:21:21
 */
public class ValidateUtil {

    /**
     * 描述：邮件格式验证
     *
     * @param email 邮件地址
     * @throws BusinessException
     * @author mao2080@sina.com
     * @created 2017年4月1日 下午4:33:18
     * @since
     */
    public static void validateEmail(String email) throws BusinessException {
        if (StringUtil.isBlank(email)) {
            throw new BusinessException(ExceptionEnum.EMAIL_IS_ENPTY);
        }
        //String reg = "\\w+(\\.\\w)*@\\w+(\\.\\w{2,3}){1,3}";
        //String reg = "\\w+(\\.|\\w)*@\\w+(\\.\\w{2,3}){1,3}";
        //String reg = "\\w+((\\.)?(\\w)+)*@\\w+(\\.\\w{2,3}){1,3}";
        String reg="^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?(?:\\.[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?)*$";
        Pattern regex = Pattern.compile(reg);
        if (!regex.matcher(email).matches()) {
            throw new BusinessException(ExceptionEnum.EMAIL_FORMAT_INCORRECT);
        }
    }

    public static void isNumeric(String str) {
        if (str == null || str.length() > 20) {
            throw new BusinessException(ExceptionEnum.PHONE_FORMAT_INCORRECT);
        }
        Pattern pattern = Pattern.compile("[0-9]*");
        if (!pattern.matcher(str).matches()) {
            throw new BusinessException(ExceptionEnum.PHONE_FORMAT_INCORRECT);
        }
    }

    public static void main(String[] args) {
        try {
//            validateEmail(null);
//            validateEmail("781");
//            validateEmail("781@");
//            validateEmail("781@qq");
            validateEmail("lds.iris.test@gmail.com");
            validateEmail("Hlc123456_q@qq.com");
            validateEmail("Yong.Li@ul.com");
            validateEmail("781@qq.com");
            validateEmail("781@qq.com.cn");
            validateEmail("781_q@qq.com.cn");
//            validateEmail("781@qq.");
        } catch (BusinessException e) {
            e.printStackTrace();
        }
    }

}
