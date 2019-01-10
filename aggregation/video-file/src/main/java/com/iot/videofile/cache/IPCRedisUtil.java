package com.iot.videofile.cache;

import com.iot.common.util.ToolUtil;
import com.iot.file.vo.FileInfoRedisVo;
import com.iot.redis.RedisCacheUtil;
import com.iot.video.vo.resp.CheckBeforeUploadResult;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 项目名称：cloud
 * 功能描述：
 * 创建人： yeshiyuan
 * 创建时间：2018/6/26 11:25
 * 修改人： yeshiyuan
 * 修改时间：2018/6/26 11:25
 * 修改描述：
 */
public class IPCRedisUtil {

    private final static Long Effective_Time = 5 * 60L ; //有效时间  300秒


    /**
      * @despriction：校验ipc是否通过
      * @author  yeshiyuan
      * @created 2018/11/28 19:11
      */
    public static CheckBeforeUploadResult getCheckResult(String deviceId, String planId) {
        return RedisCacheUtil.valueObjGet(IPCRedisKeyUtil.getIPCPlanKey(deviceId, planId), CheckBeforeUploadResult.class);
    }

    public static void setCheckResult(String deviceId, String planId, CheckBeforeUploadResult result) {
        RedisCacheUtil.valueObjSet(IPCRedisKeyUtil.getIPCPlanKey(deviceId, planId), result, Effective_Time);
    }
}
