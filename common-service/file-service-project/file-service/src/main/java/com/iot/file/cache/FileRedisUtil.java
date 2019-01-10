package com.iot.file.cache;

import com.iot.common.util.CommonUtil;
import com.iot.common.util.IOUtil;
import com.iot.common.util.JsonUtil;
import com.iot.common.util.ToolUtil;
import com.iot.file.vo.FileInfoRedisVo;
import com.iot.redis.RedisCacheUtil;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;

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
public class FileRedisUtil {

    /**
     * @despriction：往redis里添加文件对应url的数据
     * @author  yeshiyuan
     * @created 2018/6/25 20:36
     * @param fileId 文件uuid
     * @param url 文件下载url
     * @return
     */
    public static void setUrlToRedis(String fileId, String url, Long expireTime) {
        RedisCacheUtil.valueSet(FileRedisKeyUtil.getUrlByFileId(fileId), url, expireTime);
    }

    /**
     * @despriction：从redis里获取文件对应url的数据
     * @author  yeshiyuan
     * @created 2018/6/25 20:36
     * @param fileId 文件uuid
     * @param url 文件下载url
     * @return
     */
    public static String getUrlFromRedis(String fileId) {
        return RedisCacheUtil.valueGet(FileRedisKeyUtil.getUrlByFileId(fileId));
    }

    /**
     * @despriction：批量往redis里添加文件对应url的数据
     * @author  yeshiyuan
     * @created 2018/6/25 20:36
     * @param fileId 文件uuid
     * @param url 文件下载url
     * @return
     */
    public static void batchSetUrlToRedis(Map<String,String> fileUrlMap, Long expireTime) {
        Map<String,String> map = new HashMap<>();
        for (String fileId : fileUrlMap.keySet()){
            map.put(FileRedisKeyUtil.getUrlByFileId(fileId),fileUrlMap.get(fileId));
        }
        RedisCacheUtil.mset(map,expireTime);
    }

    /**
     * @despriction：批量从redis里获取文件对应url的数据
     * @author  yeshiyuan
     * @created 2018/6/26 11:25
     * @param null
     * @return
     */
    public static List<String> batchGetUrlFromRedis(List<String> fileIds){
        List<String> keys = new ArrayList<>();
        for (String fileId : fileIds) {
            keys.add(FileRedisKeyUtil.getUrlByFileId(fileId));
        }
        List<String> urls = RedisCacheUtil.mget(keys,false);
        return urls;
    }

    /**
     * @despriction：批量删除Redis中缓存的url
     * @author  yeshiyuan
     * @created 2018/6/26 14:06
     * @param null
     * @return
     */
    public static void batchDelUrlFromRedis(List<String> fileIds){
        List<String> keys = new ArrayList<>();
        for (String fileId : fileIds) {
            keys.add(FileRedisKeyUtil.getUrlByFileId(fileId));
        }
        RedisCacheUtil.delete(keys);
    }

    /**
     * @despriction：保存文件信息
     * @author  yeshiyuan
     * @created 2018/8/2 11:57
     * @return
     */
    public static void saveFileIno(String fileId, FileInfoRedisVo vo, Long expireTime){
        String key = FileRedisKeyUtil.getFileInfoKey(fileId);
        RedisCacheUtil.valueObjSet(key, vo, expireTime);
    }

    /**
     * @despriction：把文件uuid添加至微软云无效数据队列
     * @author  yeshiyuan
     * @created 2018/8/2 13:49
     * @return
     */
    public static void addFileUuid(String fileId, String dateStr){
        String key = FileRedisKeyUtil.getFileUuidSetKey(dateStr);
        RedisCacheUtil.setPush(key, fileId, false);
    }

    /**
     * @despriction：获取文件信息
     * @author  yeshiyuan
     * @created 2018/8/2 14:25
     * @param null
     * @return
     */
    public static FileInfoRedisVo getFileInfo(String fileId){
        return RedisCacheUtil.valueObjGet(FileRedisKeyUtil.getFileInfoKey(fileId), FileInfoRedisVo.class);
    }

    /**
     * @despriction：删除文件信息
     * @author  yeshiyuan
     * @created 2018/8/2 14:49
     * @return
     */
    public static void deleteFileInfo(String fileId, String dateStr){
        RedisCacheUtil.delete(FileRedisKeyUtil.getFileInfoKey(fileId));
        RedisCacheUtil.setRemove(FileRedisKeyUtil.getFileUuidSetKey(dateStr), fileId ,false);
    }

    /**
     * @despriction：获取待进行校验清除的文件id集合
     * @author  yeshiyuan
     * @created 2018/8/2 16:45
     * @param null
     * @return
     */
    public static Set<String> getFileUuid(String dateStr){
        return RedisCacheUtil.setGetAll(FileRedisKeyUtil.getFileUuidSetKey(dateStr), String.class, false);
    }

    /**
     * @despriction：批量获取缓存文件信息
     * @author  yeshiyuan
     * @created 2018/8/10 15:44
     * @return
     */
    public static List<FileInfoRedisVo> batchGetFileInfo(List<String> fileInfoKeys){
        return RedisCacheUtil.mget(fileInfoKeys, FileInfoRedisVo.class);
    }

    /**
     * @despriction：从缓存里获取视频截图的url
     * @author  yeshiyuan
     * @created 2018/8/10 15:46
     * @param null
     * @return
     */
    public static String getVideoScreenUrl(String planId, String fileId) {
        return RedisCacheUtil.valueGet(FileRedisKeyUtil.getVideoScreenKey(planId, fileId));
    }

    /**
     * @despriction：把视频截图url缓存在redis里
     * @author  yeshiyuan
     * @created 2018/8/10 15:51
     * @return
     */
    public static void saveVideoScreenUrl(String planId, String fileId, String url, Long expireTime) {
        String key = FileRedisKeyUtil.getVideoScreenKey(planId, fileId);
        RedisCacheUtil.valueSet(key, url, expireTime);
    }

    /**
     * @despriction：保存文件信息至redis
     * @author  yeshiyuan
     * @created 2018/8/10 15:06
     * @return
     */
    public static void saveFileInfoToRedis(String filePath, String fileId, String fileType, Long tenantId, Long expireTime){
        Date now = new Date();
        FileInfoRedisVo vo = new FileInfoRedisVo(fileType, filePath, tenantId, now);
        //saveFileIno(fileId, vo, expireTime);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd:HH");
        String dateStr = dateFormat.format(now);
        //addFileUuid(fileId, dateStr);
        String fileInfoKey = FileRedisKeyUtil.getFileInfoKey(fileId);
        String fileDateKey = FileRedisKeyUtil.getFileUuidSetKey(dateStr);
        RedisCacheUtil.getRedisTemplate().executePipelined((RedisCallback<Object>) redisConnection -> {
            //设置过期时间比url的有效时间多一个小时，主要是为了给定时任务扫描微软云的，校验此文件是否有上传并上报信息
            String fileInfoValue = JsonUtil.toJson(vo);
            redisConnection.setEx(fileInfoKey.getBytes(), expireTime, fileInfoValue.getBytes());
            //添加至待清除微软云无效数据队列里（文件信息上报会从此队列里挪走）
            redisConnection.sAdd(fileDateKey.getBytes(), fileId.getBytes());
            return null;
        });
    }

    /**
     * @despriction：获取下载验证码uuid(有效期单位秒)
     * @author  yeshiyuan
     * @created 2018/11/28 19:07
     */
    public static String getDownloadCode(Integer second) {
        String uuid = ToolUtil.getUUID();
        RedisCacheUtil.valueSet(FileRedisKeyUtil.getDownloadCodeKey(uuid), "1", Long.valueOf(second));
        return uuid;
    }

    /**
     * @despriction：检验下载验证码是否过期
     * @author  yeshiyuan
     * @created 2018/11/28 19:11
     */
    public static boolean checkDownloadCode(String uuid) {
        return RedisCacheUtil.hasKey(FileRedisKeyUtil.getDownloadCodeKey(uuid));
    }
}
