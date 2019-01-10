package com.iot.file.service.impl;

import com.iot.common.exception.BusinessException;
import com.iot.common.util.CommonUtil;
import com.iot.common.util.JsonUtil;
import com.iot.common.util.SecurityUtil;
import com.iot.common.util.StringUtil;
import com.iot.exception.BusinessExceptionEnum;
import com.iot.file.PropertyConfigureUtil;
import com.iot.file.cache.FileRedisKeyUtil;
import com.iot.file.cache.FileRedisUtil;
import com.iot.file.contants.ModuleConstants;
import com.iot.file.dto.FileDto;
import com.iot.file.dto.VideoInfo;
import com.iot.file.entity.FileBean;
import com.iot.file.service.VideoFileService;
import com.iot.file.storage.IStorage;
import com.iot.file.storage.StorageFactory;
import com.iot.file.util.FileUtil;
import com.iot.file.vo.FileInfoRedisVo;
import com.iot.file.vo.VideoFileGetUrlReq;
import com.iot.redis.RedisCacheUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.File;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 项目名称：cloud
 * 模块名称：给视频服务专用的文件处理service
 * 创建人： yeshiyuan
 * 创建时间：2018/8/2 10:40
 * 修改人： yeshiyuan
 * 修改时间：2018/8/2 10:40
 * 修改描述：
 */
@Service
@DependsOn({"file_init"})
public class VideoFileServiceImpl implements VideoFileService{

    private final static Logger logger = LoggerFactory.getLogger(VideoFileServiceImpl.class);

    private IStorage storage;

    private static int width = 202;

    private static int height = 116;

    /**Redis 1h*/
    public static final long REDIS_DELAY_1H = 3600;

    /**
     * url有效期
     */
    @Value("${fileStorage.mapProps.expiration}")
    private Integer expiraHour;

    private static ExecutorService executorService = Executors.newCachedThreadPool();

    /**
     * 描述：默认构造
     * @author mao2080@sina.com
     * @created 2018/3/26 14:43
     * @param
     * @return
     */
    @PostConstruct
    public void init(){
        try {
            this.storage = StorageFactory.createStorage();
            logger.info("storage:{}",storage);
        } catch (Exception e) {
            logger.error("videoFileServiceImpl init error : ",e);
        }
    }

    /**
     * @despriction：获取文件服务器的上传url
     * @author  yeshiyuan
     * @created 2018/8/2 10:26
     * @param tenantId 租户id
     * @param planId 计划id
     * @param fileType 文件类型
     * @return
     */
    @Override
    public FileDto getUploadUrl(Long tenantId, String planId, String fileType) {
        if(tenantId == null){
            throw new BusinessException(BusinessExceptionEnum.TENANTID_ISNULL);
        }
        if(StringUtil.isBlank(fileType)){
            throw new BusinessException(BusinessExceptionEnum.FILETYPE_ISNULL);
        }
        if (StringUtil.isBlank(planId)){
            throw new BusinessException(BusinessExceptionEnum.PLANID_ISNULL);
        }
        try {
            //文件前缀格式： 加密的租户id/plan/计划uuid/文件类型
            //上传至文件服务器上的保存路径前缀
            String preFilePath = this.getUploadPreFilePath(planId, fileType, tenantId);
            FileBean fileBean = this.storage.getUploadUrl(preFilePath, fileType);
            //文件信息缓存至redis
            //设置过期时间比url的有效时间多一个小时，主要是为了给定时任务扫描微软云的，校验此文件是否有上传并上报信息
            FileRedisUtil.saveFileInfoToRedis(fileBean.getFilePath(), fileBean.getFileId(), fileType, tenantId, (expiraHour + 1) * 3600L);
            return new FileDto(fileBean.getPresignedUrl(),fileBean.getFileId(), fileBean.getFileId()+"."+fileType);
        } catch (Exception e) {
            logger.error("VideoFileServiceImpl getUploadUrl error",e);
            throw new BusinessException(BusinessExceptionEnum.GET_PUT_URL_ERROR);
        }
    }

    /**
     * @despriction：通过文件路径获取文件的可访问url
     * @author  yeshiyuan
     * @created 2018/8/2 10:32
     * @param filePath 文件路径
     * @return
     */
    @Override
    public FileDto getUrlByFilePath(String filePath) {
        if (StringUtil.isBlank(filePath)){
            throw new BusinessException(BusinessExceptionEnum.GET_GET_URL_ERROR);
        }
        try {
            String fileId = filePath.substring(filePath.lastIndexOf(ModuleConstants.SPRIT) + 1, filePath.lastIndexOf(ModuleConstants.POINT));
            String url = FileRedisUtil.getUrlFromRedis(fileId);
            if (StringUtil.isBlank(url)){
                url = storage.getGetUrl(filePath).getPresignedUrl();
                //把url放入缓存
                FileRedisUtil.setUrlToRedis(fileId, url, (expiraHour-1) * 3600L);
            }
            return new FileDto(url,fileId, null);
        } catch (Exception e) {
            logger.error("getUrlByFilePath error:", e);
            throw new BusinessException(BusinessExceptionEnum.GET_GET_URL_ERROR);
        }
    }

    /**
     * @despriction：通过文件路径批量获取文件的可访问url
     * @author  yeshiyuan
     * @created 2018/8/2 10:32
     * @param reqs 文件路径集合
     * @return
     */
    @Override
    public Map<String,String> getUrlByFilePaths(List<VideoFileGetUrlReq> reqs) {
        if (reqs == null || reqs.isEmpty()){
            return Collections.emptyMap();
        }else {
            try {
                List<String> fileIds = new ArrayList<>();
                reqs.forEach( req -> {
                    fileIds.add(req.getFileId());
                });
                Map<String, String> fileMap = new HashMap<>();
                //未在缓存里待获取url的文件ids
                Map<String,String> waitGetUrls = new HashMap<>();
                //在reidis查找文件对应的url，为空的再筛入待获取队列，进行查询
                List<String> urls = FileRedisUtil.batchGetUrlFromRedis(fileIds);
                if (urls!=null && !urls.isEmpty()){
                    for (int i = 0; i < urls.size(); i++) {
                        if (StringUtil.isBlank(urls.get(i))){
                            waitGetUrls.put(fileIds.get(i),reqs.get(i).getFilePath());
                        }else{
                            fileMap.put(fileIds.get(i),urls.get(i));
                        }
                    }
                }
                if (waitGetUrls.size()>0){
                    Map<String,String> newUrlMap = new HashMap<>(); //新获取的url
                    for (Map.Entry<String, String> entry : waitGetUrls.entrySet()){
                        FileBean fileBean = this.storage.getGetUrl(entry.getValue());
                        fileMap.put(entry.getKey(), fileBean.getPresignedUrl());
                        newUrlMap.put(entry.getKey(), fileBean.getPresignedUrl());
                    }
                    //异步存进redis里
                    executorService.execute(new Runnable() {
                        @Override
                        public void run() {
                            FileRedisUtil.batchSetUrlToRedis(newUrlMap, (expiraHour-1) * 3600L);
                        }
                    });
                }
                return fileMap;
            } catch (Exception e) {
                logger.error("getUrlByFilePaths -> error : ",e);
                throw new BusinessException(BusinessExceptionEnum.BATCH_GET_GET_URL_ERROR);
            }
        }
    }

    /**
     * @despriction：处理某时间内的未上报信息的文件(定时任务使用)
     * @author  yeshiyuan
     * @created 2018/8/2 17:06
     * @return
     */
    @Override
    public void dealUnUploadFileInfoTask(String dateStr) {
        Set<String> fileUuids = FileRedisUtil.getFileUuid(dateStr);
        if (fileUuids != null && !fileUuids.isEmpty()){
            List<String> fileInfoKeys = new ArrayList<>();
            fileUuids.forEach(fileId -> {
                fileInfoKeys.add(FileRedisKeyUtil.getFileInfoKey(fileId));
            });
            List<List<String>> keyslList = new ArrayList<>();
            if (fileInfoKeys.size() > ModuleConstants.batchDelNum) {
                keyslList = CommonUtil.dealBySubList(fileInfoKeys, ModuleConstants.batchDelNum);
            }else {
                keyslList.add(fileInfoKeys);
            }
            CompletableFuture[] cfs = keyslList.stream().map(
                    keys -> CompletableFuture.runAsync(() -> {
                        List<FileInfoRedisVo> fileInfoRedisVos = FileRedisUtil.batchGetFileInfo(keys);
                        fileInfoRedisVos.forEach(vo -> {
                            try {
                                this.storage.deleteObject(vo.getFilePath());
                            } catch (Exception e) {
                                logger.error("删除未上报信息的文件报错：" + JsonUtil.toJson(vo), e);
                            }
                            RedisCacheUtil.delete(keys);
                        });
                    }, executorService)
                    .whenComplete((s, e) -> {
                        logger.debug("task {} complete,finish time: {}", Thread.currentThread().getName(), new Date());
                    })
            ).toArray(CompletableFuture[]::new);
            CompletableFuture.allOf(cfs).whenCompleteAsync((s,e)->{
                String redisKey = FileRedisKeyUtil.getFileUuidSetKey(dateStr);
                RedisCacheUtil.delete(redisKey);
                logger.info("VideoFileServiceImpl.dealUnUploadFileInfoTask success -> redisKey({})", redisKey);
            });
        }
    }

    /**
     * @despriction：从redis中获取文件路径进而删除s3数据
     * @author  yeshiyuan
     * @created 2018/6/5 14:40
     * @param redisTaskId
     * @return
     */
    @Override
    public void deleteFileByRedisTaskId(String redisTaskId) {
        Set<String> filePaths = RedisCacheUtil.setGetAll(redisTaskId,String.class,false);
        if (filePaths != null && !filePaths.isEmpty()){
            Long startTime = System.currentTimeMillis();
            List<List<String>> lists = new ArrayList<>();
            if (filePaths.size() > ModuleConstants.batchDelNum){
                lists = CommonUtil.dealBySubList(new ArrayList<>(filePaths), ModuleConstants.batchDelNum);
            }else{
                lists.add(new ArrayList<>(filePaths));
            }
            CompletableFuture[] futures = lists.stream().map(filePathList ->
                CompletableFuture.runAsync(()->{
                    for (String filePath: filePathList){
                        try{
                            storage.deleteObject(filePath);
                        }catch (Exception e){
                            logger.error("storage.deleteObject error : ", e);
                        }
                    }
                })
            ).toArray(CompletableFuture[]::new);
            CompletableFuture.allOf(futures).whenComplete((s,e)->{
                RedisCacheUtil.delete(redisTaskId);
                Long endTime = System.currentTimeMillis();
                logger.debug("VideoFileServiceImpl.deleteFileByRedisTaskId(taskKey:{}, dealNum:{}) success, consum time {}ms", redisTaskId, filePaths.size(), (endTime - startTime));
            });
        }
    }

    @Override
    public void deleteFileByFilePath(List<String> filePaths) {
        if (filePaths != null && !filePaths.isEmpty()){
            Long startTime = System.currentTimeMillis();
            List<List<String>> lists = new ArrayList<>();
            if (filePaths.size() > ModuleConstants.batchDelNum){
                lists = CommonUtil.dealBySubList(new ArrayList<>(filePaths), ModuleConstants.batchDelNum);
            }else{
                lists.add(new ArrayList<>(filePaths));
            }
            CompletableFuture[] futures = lists.stream().map(filePathList ->
                CompletableFuture.runAsync(()->{
                    for (String filePath: filePathList){
                        try{
                            storage.deleteObject(filePath);
                        }catch (Exception e){
                            logger.error("storage.deleteObject error : ",e);
                        }
                    }
                })
            ).toArray(CompletableFuture[]::new);
            CompletableFuture.allOf(futures).whenComplete((s,e)->{
                Long endTime = System.currentTimeMillis();
                logger.debug("VideoFileServiceImpl.deleteFileByFilePath(size:{}) success, consum time {}ms",  filePaths.size(), (endTime - startTime));
            });
        }
    }

    /**
     *
     * 描述：视频截图
     * @author yeshiyuan
     * @created 2018年8月10日 下午2:04:59
     * @since
     * @param fileId
     * @return
     */
    @Override
    public String videoScreenshot(String filePath, String deviceId, String planId, Long tenantId) throws BusinessException{
        if(StringUtil.isBlank(filePath)){
            throw new BusinessException(BusinessExceptionEnum.FILE_PATH_ISNULL);
        }
        String fileId = filePath.substring(filePath.lastIndexOf(ModuleConstants.SPRIT)+1 ,filePath.lastIndexOf(ModuleConstants.POINT));
        //从缓存里获取视频截图
        String s3url = FileRedisUtil.getVideoScreenUrl(planId, fileId);
        if (!StringUtil.isBlank(s3url)){
            return s3url;
        }
        //获取文件预签名URL，有效时间1小时
        FileDto fileDto = this.getUrlByFilePath(filePath);
        String srcFile = FileUtil.createTempDir(fileId);
        try {
            String fileName = fileId.concat(".ts");
            FileUtil.downLoadFromUrl(fileDto.getPresignedUrl(), fileName, srcFile);
            //下载后的ts文件路径
            String videoFilename = srcFile.concat(System.getProperty("file.separator")).concat(fileName);
            //待要截图保存的路径
            String newFileId =UUID.randomUUID().toString().replace("-","");
            String pictureName = newFileId.concat(".png");
            String pictureSavePath = srcFile.concat(System.getProperty("file.separator")).concat(pictureName);
            //截图插件工具路径
            String ffmpegPic = PropertyConfigureUtil.mapProps.get("ffmpeg.url").toString();
            VideoInfo videoInfo = new VideoInfo(ffmpegPic);
            videoInfo.getInfo(videoFilename);
            //把下载的视频截图
            FileUtil.getThumb(ffmpegPic, videoFilename, pictureSavePath, width, height, 0, 0, 0.1f);
            File filePng = new File(pictureSavePath);
            //上传至文件服务器上的保存路径前缀
            String preFilePath = this.getUploadPreFilePath(planId, "png", tenantId);
            if(filePng.length() >0 ) {
                String s3key = null;
                // 存图片
                if (CommonUtil.isEmpty(filePng)) {
                    throw new BusinessException(BusinessExceptionEnum.FILE_ISNULL);
                }else {
                    //微软云不能设置有效期，所以通过下面的redis来做回收截图
                    s3key = this.storage.putObject(preFilePath, filePng, 60);
                }
                s3url = this.storage.getGetUrl(s3key).getPresignedUrl();
                //缓存此url
                FileRedisUtil.saveVideoScreenUrl(planId, fileId, s3url, expiraHour*3590L);
            }
            //文件信息缓存至redis
            //设置过期时间比url的有效时间多一个小时，主要是为了给定时任务扫描微软云的，校验此文件是否有上传并上报信息
            FileRedisUtil.saveFileInfoToRedis(preFilePath + ModuleConstants.SPRIT + pictureName, newFileId, "png", tenantId, (expiraHour + 1) * 3600L);
        } catch (Exception e) {
            logger.error("视频截图异常",e);
            throw new BusinessException(BusinessExceptionEnum.VIDEO_SCREENSHOT_ERROR);
        } finally {
            //删掉临时文件
            File tempDir = new File(srcFile);
            FileUtil.deleteAllFilesOfDir(tempDir);
        }
        return s3url;
    }

    /**
      * @despriction：获取视频文件上传至微软云的存储路径(文件前缀：加密的租户id/plan/计划uuid/文件类型)
      * @author  yeshiyuan
      * @created 2018/8/10 15:00
      * @param null
      * @return
      */
    private String getUploadPreFilePath(String planId, String fileType, Long tenantId){
        StringBuffer preFilePath = new StringBuffer()
                .append(SecurityUtil.EncryptByAES(tenantId.toString(), ModuleConstants.AES_KEY)).append(ModuleConstants.SPRIT)
                .append("plan").append(ModuleConstants.SPRIT)
                .append(planId).append(ModuleConstants.SPRIT)
                .append(fileType);
        return preFilePath.toString();
    }


}
