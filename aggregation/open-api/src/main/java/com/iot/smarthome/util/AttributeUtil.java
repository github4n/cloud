package com.iot.smarthome.util;

import org.apache.commons.lang3.StringUtils;

/**
 * @Descrpiton:
 * @Author: yuChangXing
 * @Date: 2018/12/14 17:21
 * @Modify by:
 */
public class AttributeUtil {
    private final static String DATA_TYPE_INT = "int";
    private final static String DATA_TYPE_FLOAT = "float";
    private final static String DATA_TYPE_BOOLEAN = "boolean";
    private final static String DATA_TYPE_ENUM = "enum";
    private final static String DATA_TYPE_STRING = "string";


    public static void main(String[] args) {
        System.out.println(AttributeUtil.parseMinMax("int", "2"));
        System.out.println(AttributeUtil.parseMinMax("float", "5.6"));
        System.out.println(AttributeUtil.parseMinMax("boolean", "true"));
        System.out.println(AttributeUtil.parseMinMax("enum", "quick"));
        System.out.println(AttributeUtil.parseMinMax("string", "flat"));
    }

    public static Object parseMinMax(String dataType, String numberStr) {
        Object numberObj = null;
        if (dataType == null) {
            return numberObj;
        }

        if (DATA_TYPE_INT.equals(dataType)) {
            numberObj = Integer.parseInt(numberStr);
        } else if (DATA_TYPE_FLOAT.equals(dataType)) {
            numberObj = Float.parseFloat(numberStr);
        } else if (DATA_TYPE_BOOLEAN.equals(dataType)) {
            numberObj = null;
        } else if (DATA_TYPE_ENUM.equals(dataType)) {
            numberObj = null;
        } else if (DATA_TYPE_STRING.equals(dataType)) {
            numberObj = null;
        }

        return numberObj;
    }

    public static boolean isTrue(String flag) {
        if (StringUtils.isBlank(flag)) {
            return false;
        }

        if ("1".equals(flag) || "true".equals(flag)) {
            return true;
        }

        return false;
    }

    /**
     * 解析 参数类型，0:int,1:float,2:bool,3:enum,4:string
     *
     * @param paramType
     * @return
     */
    public static String parseParamType(Integer paramType) {
        if (paramType == null) {
            return null;
        }

        String dataType = null;
        switch (paramType) {
            case 0:
                dataType = DATA_TYPE_INT;
                break;
            case 1:
                dataType = DATA_TYPE_FLOAT;
                break;
            case 2:
                dataType = DATA_TYPE_BOOLEAN;
                break;
            case 3:
                dataType = DATA_TYPE_ENUM;
                break;
            case 4:
                dataType = DATA_TYPE_STRING;
                break;
            default:
                dataType = null;
                break;
        }

        return dataType;
    }

    /**
     * 解析 读写特征 0:可读可写,1:不可读不可写,2:可读不可写
     *
     * @param rwStatus
     * @return
     */
    public static int parseRwStatus(Integer rwStatus) {
        int rw = 1;
        if (rwStatus == null) {
            return rw;
        }

        if (rwStatus == 0) {
            rw = 0;
        } else if (rwStatus == 1) {
            rw = 1;
        } else if (rwStatus == 2) {
            rw = 2;
        } else {
            rw = 1;
        }
        return rw;
    }
}
