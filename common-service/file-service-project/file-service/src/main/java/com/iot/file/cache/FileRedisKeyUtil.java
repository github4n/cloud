package com.iot.file.cache;

/**
 * 项目名称：cloud
 * 功能描述：文件服务的redis key维护
 * 创建人： yeshiyuan
 * 创建时间：2018/6/25 15:10
 * 修改人： yeshiyuan
 * 修改时间：2018/6/25 15:10
 * 修改描述：
 */
public class FileRedisKeyUtil {

    /**
     * 文件缓存url（字符串类型  eg： file:url:fileId    value: http://baidu.com）
     */
    private static final String FILE_URL = "file:url:@fileId@";

    public final static Long FILE_URL_EXPIRE_TIME = 23 * 60 * 60L; //23小时有效期

    /**
     * 文件信息缓存（字符串类型  eg： file:url:fileId   value: {}）
     */
    private final static String FILE_INFO = "file:info:";

    /**
     * 清除微软云无效数据队列（set类型 file:uuid:YYYY-MM-dd:HH）
     */
    private final static String FILE_UUID = "file:uuid:";

    /**
     * 视频截图缓存（String 类型，file:videoScreen:{planId}:{fileId} url）
     */
    private final static String FILE_VIDEO_SCREEN = "file:videoScreen:";

    /**
     * 文件下载验证码(String 类型, file:download:{uuid})
     */
    private final static String FILE_DOWNLOAD_CODE = "file:download:";

    /**
     * @despriction：获取url的rediskey
     * @author  yeshiyuan
     * @created 2018/6/25 15:30
     * @param null
     * @return
     */
    public static String getUrlByFileId(String fileId) {
        String urlKey = FILE_URL.replace("@fileId@",fileId);
        return urlKey;
    }

    /**
     * @despriction：获取文件信息的rediskey
     * @author  yeshiyuan
     * @created 2018/8/2 10:56
     * @return
     */
    public static String getFileInfoKey(String fileId){
        return FILE_INFO + fileId;
    }

    /**
     * @despriction：获取某小时里待清除的微软云垃圾数据
     * @author  yeshiyuan
     * @created 2018/8/2 13:47
     * @return
     */
    public static String getFileUuidSetKey(String dateStr){
        return FILE_UUID + dateStr;
    }

    /**
     * @despriction：获取视频截图rediskey
     * @author  yeshiyuan
     * @created 2018/8/10 15:43
     * @return
     */
    public static String getVideoScreenKey(String planId, String fileId){
        return FILE_VIDEO_SCREEN + planId + ":" + fileId;
    }

    /**
     * @despriction：获取文件下载验证码key
     * @author  yeshiyuan
     * @created 2018/11/28 19:05
     */
    public static String getDownloadCodeKey(String uuid) {
        return FILE_DOWNLOAD_CODE + uuid;
    }
}
