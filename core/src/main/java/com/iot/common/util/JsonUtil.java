package com.iot.common.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.iot.common.exception.BusinessException;
import com.iot.common.exception.ExceptionEnum;

import java.util.Map;

/**
 * 项目名称：IOT云平台
 * 模块名称：常用工具类
 * 功能描述：json工具类
 * 创建人： mao2080@sina.com
 * 创建时间：2017年3月28日 上午11:56:15
 * 修改人： mao2080@sina.com
 * 修改时间：2017年3月28日 上午11:56:15
 */
public class JsonUtil {

    /**
     * 描述：将json转化成map
     *
     * @param jsonStr json字符串
     * @return
     * @throws BusinessException
     * @author mao2080@sina.com
     * @created 2017年5月26日 下午1:58:56
     * @since
     */
    public static Map<String, Object> convertJsonStrToMap(String jsonStr) throws BusinessException {
        try {
            return JSON.parseObject(jsonStr, new TypeReference<Map<String, Object>>() {
            });
        } catch (Exception e) {
            throw new BusinessException(ExceptionEnum.JSONSTRING_TO_OBJECT_ERROR, e);
        }
    }

    /**
     * 描述：将对象格式化成json字符串
     *
     * @param object 对象
     * @return json字符串
     * @throws BusinessException
     * @author mao2080@sina.com
     * @created 2017年4月1日 下午4:38:18
     * @since
     */
    public static String toJson(Object object) throws BusinessException {
        try {
            return JSON.toJSONString(object, new SerializerFeature[]{
                SerializerFeature.WriteMapNullValue,
                SerializerFeature.DisableCircularReferenceDetect,
                SerializerFeature.WriteNonStringKeyAsString});
        } catch (Exception e) {
            throw new BusinessException(ExceptionEnum.OBJECT_TO_JSONSTRING_ERROR, e);
        }
    }

    /**
     * 描述：将对象格式化成json字符串（PrettyFormat格式）
     *
     * @param object 对象
     * @return json字符串
     * @throws BusinessException
     * @author mao2080@sina.com
     * @created 2017年4月1日 下午4:38:18
     * @since
     */
    public static String toJsonFormat(Object object) throws BusinessException {
        try {
            return JSON.toJSONString(object, new SerializerFeature[]{
                SerializerFeature.WriteMapNullValue,
                SerializerFeature.PrettyFormat,
                SerializerFeature.DisableCircularReferenceDetect,
                SerializerFeature.WriteNonStringKeyAsString});
        } catch (Exception e) {
            throw new BusinessException(ExceptionEnum.OBJECT_TO_JSONSTRING_ERROR, e);
        }
    }

    /**
     * 描述：转Map
     *
     * @param obj 对象
     * @return object
     * @throws BusinessException
     * @author mao2080@sina.com
     * @created 2017年4月1日 下午5:00:20
     * @since
     */
    public static Object toJsonObject(Object obj) throws BusinessException {
        try {
            return JSON.toJSON(obj);
        } catch (Exception e) {
            throw new BusinessException(ExceptionEnum.JSONSTRING_TO_OBJECT_ERROR, e);
        }
    }

    /**
     * 描述：将json串转为对象
     *
     * @param jsonString json串
     * @param clazz      对象
     * @return
     * @throws BusinessException
     * @author mao2080@sina.com
     * @created 2017年4月1日 下午5:01:23
     * @since
     */
    public static <T> T fromJson(String jsonString, Class<T> clazz) throws BusinessException {
        try {
            if (StringUtil.isEmpty(jsonString)) {
                return null;
            }
            return (T) JSON.parseObject(jsonString, clazz);
        } catch (Exception e) {
            throw new BusinessException(ExceptionEnum.JSONSTRING_TO_OBJECT_ERROR, e);
        }
    }

    public static <T> T fromJsonArray(String jsonString, Class<T> clazz) throws BusinessException {
        try {
            if (StringUtil.isEmpty(jsonString)) {
                return null;
            }

            return (T) JSONObject.parseArray(jsonString, clazz);
        } catch (Exception e) {
            throw new BusinessException(ExceptionEnum.JSONSTRING_TO_OBJECT_ERROR, e);
        }
    }

    /**
     * 描述：暂时不开通
     *
     * @param jsonString
     * @return
     * @throws Exception
     * @author mao2080@sina.com
     * @created 2017年4月1日 下午5:08:12
     * @since
     */
    @SuppressWarnings("unused")
    private static <T> T fromJson(String jsonString) throws Exception {
        return JSON.parseObject(jsonString, new TypeReference<T>() {
        }, new Feature[0]);
    }

}
