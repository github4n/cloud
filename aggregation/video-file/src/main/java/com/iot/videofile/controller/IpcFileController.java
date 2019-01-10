package com.iot.videofile.controller;

import com.iot.common.beans.CommonResponse;
import com.iot.common.exception.BusinessException;
import com.iot.common.exception.ResultMsg;
import com.iot.common.util.JsonUtil;
import com.iot.common.util.StringUtil;
import com.iot.file.api.FileApi;
import com.iot.file.api.VideoFileApi;
import com.iot.file.dto.FileDto;
import com.iot.file.vo.FileInfoRedisVo;
import com.iot.redis.RedisCacheUtil;
import com.iot.video.api.VideoManageApi;
import com.iot.video.api.VideoPlanApi;
import com.iot.video.entity.VideoFile;
import com.iot.video.vo.resp.CheckBeforeUploadResult;
import com.iot.videofile.cache.IPCRedisUtil;
import com.iot.videofile.exception.BusinessExceptionEnum;
import com.iot.videofile.utils.CertUtil;
import com.iot.videofile.vo.req.UploadFileInfoReq;
import com.iot.videofile.vo.req.VideoFileInfoReq;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.WebAsyncTask;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.Callable;

/**
 * @program: cloud
 * @description: ipc文件controller
 * @author: yeshiyuan
 * @create: 2018-12-19 09:34
 **/
@RestController
@RequestMapping(value = "/ipc/file")
public class IpcFileController {

    private static Logger logger = LoggerFactory.getLogger(IpcFileController.class);

    @Autowired
    private VideoFileApi videoFileApi;

    @Autowired
    private VideoPlanApi videoPlanApi;

    @Autowired
    private FileApi fileApi;

    @Autowired
    private VideoManageApi videoManageApi;

    private final static SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");




    @ApiOperation(value = "获取上传url(直接传deviceId，后台使用回调线程)", notes = "获取上传url(直接传deviceId，后台使用回调线程)")
    @RequestMapping(value = "/test", method = RequestMethod.POST)
    public WebAsyncTask<CommonResponse> test(@RequestParam String planId, @RequestParam String deviceId, @RequestParam String fileType) {
        WebAsyncTask<CommonResponse> webAsyncTask = new WebAsyncTask<CommonResponse>(3000, new Callable<CommonResponse>() {
            @Override
            public CommonResponse call() throws Exception {
                return CommonResponse.success(getFileInfo(deviceId, planId, fileType));
            }
        });
        webAsyncTask.onTimeout(() -> {
            return ResultMsg.FAIL.info("请求超时", null);
        });
        return webAsyncTask;
    }

    /**
     * @despriction：获取上传url
     * @author  yeshiyuan
     * @created 2018/12/19 9:35
     * @params []
     * @return com.iot.common.beans.CommonResponse
     */
    @ApiOperation(value = "获取上传url(由nginx转发证书，后台使用回调线程)", notes = "获取上传url(由nginx转发证书，后台使用回调线程)")
    @RequestMapping(value = "/getfsurlReq", method = RequestMethod.POST)
    public WebAsyncTask<CommonResponse> getfsurlReq(@RequestHeader("ssl_client_s_dn") String cert, @RequestParam("planId") String planId, @RequestParam("fileType") String fileType) {
        WebAsyncTask<CommonResponse> webAsyncTask = new WebAsyncTask<CommonResponse>(3000, new Callable<CommonResponse>() {
            @Override
            public CommonResponse call() throws Exception {
                String deviceId = CertUtil.getDeviceId(cert);
                return CommonResponse.success(getFileInfo(deviceId, planId, fileType));
            }
        });
        webAsyncTask.onTimeout(() -> {
            logger.info(Thread.currentThread().getName() + "请求超时**********************************************");
            return ResultMsg.FAIL.info("请求超时", null);
        });
        return webAsyncTask;
    }

    /**
     * @despriction：获取上传url
     * @author  yeshiyuan
     * @created 2018/12/19 9:35
     * @params []
     * @return com.iot.common.beans.CommonResponse
     */
    @ApiOperation(value = "获取上传url(由nginx转发证书，不使用回调线程)", notes = "获取上传url(由nginx转发证书，不使用回调线程)")
    @RequestMapping(value = "/getUploadUrl", method = RequestMethod.POST)
    public CommonResponse getUploadUrl(@RequestHeader("ssl_client_s_dn") String cert, @RequestParam("planId") String planId, @RequestParam("fileType") String fileType) {
        String deviceId = CertUtil.getDeviceId(cert);
        return CommonResponse.success(getFileInfo(deviceId, planId, fileType));
    }

    /**
      * @despriction：上报文件信息
      * @author  yeshiyuan
      * @created 2018/12/19 10:23
      * @params []
      * @return com.iot.common.beans.CommonResponse
      */
    @ApiOperation(value = "上报文件信息", notes = "上报文件信息")
    @RequestMapping(value = "/uploadFileInfo", method = RequestMethod.POST)
    public CommonResponse uploadFileInfo(@RequestHeader("ssl_client_s_dn") String cert, @RequestBody UploadFileInfoReq req) {
        UploadFileInfoReq.checkParam(req);
        //设备id
        String deviceId = CertUtil.getDeviceId(cert);
        VideoFile videoFile = new VideoFile();
        videoFile.setDeviceId(deviceId);
        //计划id
        videoFile.setPlanId(req.getPlanId());
        VideoFileInfoReq fileInfo = req.getFile();
        //文件名
        String fileName = fileInfo.getFn();
        String fileId = null;
        //获取文件信息
        FileInfoRedisVo vo = null;
        if (StringUtil.isNotEmpty(fileName)) {
            fileId = fileName.substring(0, fileName.lastIndexOf("."));
            videoFile.setFileId(fileId);
            vo = videoFileApi.getFileInfoFromRedis(fileId);
            //文件信息不存在的话则不保存
            if (vo == null) {
                logger.debug("this video file info is expire, so don't save -> data:" + JsonUtil.toJson(fileInfo));
                return ResultMsg.FAIL.info("this video file info is expire", null);
            }
            videoFile.setFileType(vo.getFileType());
            videoFile.setFilePath(vo.getFilePath());
            videoFile.setTenantId(vo.getTenantId());
        }
        //校验设备是否已绑定计划、计划是否过期——防止解绑之后IPC还上传数据或者计划过期没通知到ipc停止录影
        CheckBeforeUploadResult result = getCheckResult(deviceId, videoFile.getPlanId());
        if (!result.isPaasFlag()) {
            logger.debug("uploadFileInfo fail -> desc: {} ,filePath : {}", result.getDesc(), videoFile.getFilePath());
            //解绑期间IPC还上传视频，在这里清理掉
            fileApi.deleteObjectByPath(videoFile.getFilePath());
            return ResultMsg.FAIL.info(result.getDesc(), null);
        }
        //事件id
        videoFile.setEventUuid(fileInfo.getEvtId());
        //开始录影时间
        if (null != fileInfo.getStartTime() && !"".equals(fileInfo.getStartTime())) {
            try {
                videoFile.setVideoStartTime(fileInfo.getStartTime());
            } catch (Exception e) {
                logger.error("uploadFileInfo ParseException", e);
            }
        }
        //视频时长
        videoFile.setVideoLength(fileInfo.getLength());
        //文件大小
        videoFile.setFileSize(fileInfo.getSize());
        //文件拍摄角度
        videoFile.setRotation(fileInfo.getRotation());
        videoManageApi.createVideoFile(videoFile);
        //把redis中的缓存文件信息删除
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd:HH");
        videoFileApi.deleteFileInfoFromRedis(fileId, dateFormat.format(vo.getCreateTime()));
        logger.debug("deviceId-> " + deviceId + "," + "fileId-> " + videoFile.getFileId() + "  PlanId->" + videoFile.getPlanId());
        return CommonResponse.success();
    }


    public void lock(String owner,String key,int expireTime){
        while (!Thread.currentThread().isInterrupted()){
            boolean selectAuth = RedisCacheUtil.setNx(key, owner, expireTime);
            if (selectAuth)
                break;
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void unlock(String owner,String key){
        String oldowner=RedisCacheUtil.valueGet(key);
        if (owner.equals(oldowner)){
            RedisCacheUtil.delete(key);
        }
    }

    /**
     * 处理前校验
     * @return
     */
   /* private CheckBeforeUploadResult getCheckResult(String deviceId, String planId) {
        CheckBeforeUploadResult result = IPCRedisUtil.getCheckResult(deviceId, planId);
        if (result == null) {
            String owner= UUID.randomUUID().toString();
            String key = deviceId + ":" + planId;
            lock(owner, key,5);
            try{
                result = IPCRedisUtil.getCheckResult(deviceId, planId);
                if (result == null) {
                    result = videoPlanApi.checkBeforeUpload(planId, deviceId);
                    if (result.isPaasFlag()) {
                        IPCRedisUtil.setCheckResult(deviceId, planId, result);
                    }
                }
            }finally {
               unlock(owner,key);
            }
        }
        return result;
    }*/

    /**
     * 处理前校验
     * @return
     */
    private CheckBeforeUploadResult getCheckResult(String deviceId, String planId) {
        CheckBeforeUploadResult result = IPCRedisUtil.getCheckResult(deviceId, planId);
        if (result == null) {
            synchronized (deviceId.intern()) {
                result = IPCRedisUtil.getCheckResult(deviceId, planId);
                if (result == null) {
                    result = videoPlanApi.checkBeforeUpload(planId, deviceId);
                    if (result.isPaasFlag()) {
                        IPCRedisUtil.setCheckResult(deviceId, planId, result);
                    }
                }
            }
        }
        return result;
    }


    private Map<String, String> getFileInfo (String deviceId, String planId, String fileType) {
        Long sTime = System.currentTimeMillis();
        Long startTime = System.currentTimeMillis();
        Long endTime = null;
        //计划是否为空
        if (StringUtil.isBlank(planId)) {
            throw new BusinessException(BusinessExceptionEnum.PARAM_ERROR, "plan id is null");
        }
        //校验设备是否已绑定计划、计划是否过期——防止解绑之后IPC还上传数据或者计划过期没通知到ipc停止录影
        CheckBeforeUploadResult result = getCheckResult(deviceId, planId);
        if (!result.isPaasFlag()) {
            logger.debug("getUploadUrl fail-> {}", result.getDesc());
            throw new BusinessException(BusinessExceptionEnum.PARAM_ERROR, result.getDesc());
        }
        endTime = System.currentTimeMillis();
       // logger.info(Thread.currentThread().getName() + "参数校验耗时:" + (endTime - startTime));
        startTime = System.currentTimeMillis();
        Long tenantId = Long.valueOf(result.getData().toString());
        FileDto fileDto = videoFileApi.getUploadUrl(tenantId, planId, fileType);
        endTime = System.currentTimeMillis();
      //  logger.info(Thread.currentThread().getName() + "获取上传url耗时:" + (endTime - startTime));
        Map<String, String> responseMap = new HashMap<>();
        responseMap.put("url", fileDto.getPresignedUrl());
        responseMap.put("timestamp", SIMPLE_DATE_FORMAT.format(new Date()));
        responseMap.put("fileType", fileType);
        responseMap.put("fileName", fileDto.getFileName());
        //logger.info(Thread.currentThread().getName() + "总共耗时:" + (System.currentTimeMillis() - sTime));
        return responseMap;
    }

}
