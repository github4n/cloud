package com.iot.videofile.cache;

/**
 * ipc服务 redis key 管理
 */
public class IPCRedisKeyUtil {

    /**
     * 文件缓存url（字符串类型  eg： ipc:deviceId:planId    value: tenantId）
     */
    private static final String IPC = "ipc:";

    /**
     * 获取ipc校验key
     * @param deviceId
     * @param planId
     * @return
     */
    public static String getIPCPlanKey(String deviceId, String planId) {
        return IPC + deviceId + ":" + planId;
    }


}
