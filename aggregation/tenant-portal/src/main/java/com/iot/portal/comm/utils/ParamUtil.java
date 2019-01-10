package com.iot.portal.comm.utils;

import com.iot.common.exception.BusinessException;
import com.iot.common.util.CommonUtil;
import com.iot.common.util.NumberUtil;
import com.iot.common.util.SecurityUtil;
import com.iot.common.util.StringUtil;
import com.iot.portal.constant.TenantConstant;
import com.iot.portal.exception.BusinessExceptionEnum;

public class ParamUtil {
	
	/**
	 * 
	 * 描述：检查主键是合法
	 * @author 李帅
	 * @created 2018年11月1日 下午8:35:08
	 * @since 
	 * @param key
	 * @param keyName
	 */
    public static void checkPrimaryKey(Long key, String keyName){
        if(CommonUtil.isEmpty(key)){
            throw new BusinessException(BusinessExceptionEnum.PARAM_IS_ERROR, keyName+" is empty");
        }
    }

    /**
     * 
     * 描述：检查参数是合法
     * @author 李帅
     * @created 2018年11月1日 下午8:34:59
     * @since 
     * @param operateDesc
     */
    public static void checkOperateDesc(String operateDesc){
        if(CommonUtil.isEmpty(operateDesc)){
            return;
        }
        if(operateDesc.length() > 500){
            throw new BusinessException(BusinessExceptionEnum.PARAM_IS_ERROR, "审核描述超过500个字符");
        }
    }
    
    /**
     * 描述：解密主键
     * @author maochengyuan
     * @created 2018/7/26 10:30
     * @param primaryKey 主键
     * @param filedName 字段名
     * @return java.lang.Long
     */
    public static Long decryptKey(String primaryKey, String filedName) {
        if(StringUtil.isEmpty(primaryKey)){
            throw new BusinessException(BusinessExceptionEnum.PARAM_IS_ERROR, filedName+" is null");
        }
        try {
            String key = SecurityUtil.DecryptAES(primaryKey, TenantConstant.SECURITY_KEY);
            return NumberUtil.toLong(key);
        } catch (Exception e) {
            throw new BusinessException(BusinessExceptionEnum.PARAM_IS_ERROR, filedName+" incorrect format");
        }
    }
}
