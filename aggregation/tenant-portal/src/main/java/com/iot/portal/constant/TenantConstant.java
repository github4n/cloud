package com.iot.portal.constant;

import java.util.Arrays;
import java.util.List;

/**
 *
 * 项目名称：IOT云平台
 * 模块名称：聚合层
 * 功能描述：常量类
 * 创建人： maochengyuan
 * 创建时间：2018/7/3 18:59
 * 修改人： maochengyuan
 * 修改时间：2018/7/3 18:59
 * 修改描述：
 */
public class TenantConstant {

    /**AES加密key*/
    public static final String SECURITY_KEY = "Leedarson";

    /**
     * OTA文件最大大小100M
     */
    public static final int OTA_MAX_FILE_SIZE = 100 * 1024 * 1024;

    /**
     * OTA允许上传的文件格式
     */
    public static final List<String> OTA_ALLOW_FILE_TYPES = Arrays.asList("bin", "zip");

    /**
     * 文件大小单位：M
     */
    public static final String FILE_SIZE_UNIT = "M";

}
