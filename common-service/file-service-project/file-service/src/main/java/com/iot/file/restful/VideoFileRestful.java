package com.iot.file.restful;

import com.iot.file.api.VideoFileApi;
import com.iot.file.cache.FileRedisUtil;
import com.iot.file.dto.FileDto;
import com.iot.file.service.VideoFileService;
import com.iot.file.vo.FileInfoRedisVo;
import com.iot.file.vo.VideoFileGetUrlReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 项目名称：cloud
 * 功能描述：视频服务专用的文件接口
 * 创建人： yeshiyuan
 * 创建时间：2018/8/2 10:37
 * 修改人： yeshiyuan
 * 修改时间：2018/8/2 10:37
 * 修改描述：
 */
@RestController
public class VideoFileRestful implements VideoFileApi{

    @Autowired
    private VideoFileService videoFileService;

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
    public FileDto getUploadUrl(@RequestParam("tenantId") Long tenantId, @RequestParam("planId") String planId, @RequestParam("fileType") String fileType) {
        return videoFileService.getUploadUrl(tenantId, planId, fileType);
    }

    /**
     * @despriction：通过文件路径获取文件的可访问url
     * @author  yeshiyuan
     * @created 2018/8/2 10:32
     * @param null
     * @return
     */
    @Override
    public FileDto getUrlByFilePath(@RequestParam("filePath") String filePath) {
        return videoFileService.getUrlByFilePath(filePath);
    }

    /**
     * @despriction：通过文件路径批量获取文件的可访问url
     * @author  yeshiyuan
     * @created 2018/8/2 15:08
     * @return
     */
    @Override
    public Map<String,String> getUrlByFilePaths(@RequestBody List<VideoFileGetUrlReq> reqs) {
        return videoFileService.getUrlByFilePaths(reqs);
    }

    /**
     * @despriction：从redis中获取文件信息
     * @author  yeshiyuan
     * @created 2018/8/2 14:23
     * @param fileId 文件uuid
     * @return
     */
    @Override
    public FileInfoRedisVo getFileInfoFromRedis(@RequestParam("fileId") String fileId) {
        return FileRedisUtil.getFileInfo(fileId);
    }

    /**
     * @despriction：删除redis中的文件信息
     * @author  yeshiyuan
     * @created 2018/8/2 14:38
     * @return
     */
    @Override
    public void deleteFileInfoFromRedis(@RequestParam("fileId") String fileId, @RequestParam("dateStr") String dateStr) {
        FileRedisUtil.deleteFileInfo(fileId, dateStr);
    }

    /**
     * @despriction：获取待进行校验清除的文件(定时任务使用)
     * @author  yeshiyuan
     * @created 2018/8/2 16:28
     * @return
     */
    @Override
    public Set<String> getFileUuidFromRedisByDate(@RequestParam("dateStr") String dateStr) {
        return FileRedisUtil.getFileUuid(dateStr);
    }

    /**
     * @despriction：处理某时间内的未上报信息的文件(定时任务使用)
     * @author  yeshiyuan
     * @created 2018/8/2 17:06
     * @return
     */
    @Override
    public void dealUnUploadFileInfoTask(@RequestParam("date") String date) {
        videoFileService.dealUnUploadFileInfoTask(date);
    }

    /**
     * @despriction：从redis中获取文件路径进而删除s3文件
     * @author  yeshiyuan
     * @created 2018/6/5 14:40
     * @param redisTaskId
     * @return
     */
    @Override
    public void deleteFileByRedisTaskId(@RequestParam("redisTaskId") String redisTaskId) {
        videoFileService.deleteFileByRedisTaskId(redisTaskId);
    }

    /**
     * @despriction：批量删除文件
     * @author  yeshiyuan
     * @created 2018/8/9 17:57
     * @return
     */
    @Override
    public void deleteFileByFilePath(@RequestParam("filePaths") List<String> filePath) {
        videoFileService.deleteFileByFilePath(filePath);
    }

    /**
     *
     * 描述：视频截图
     * @author yeshiyuan
     * @created 2018年8月10日 下午2:04:02
     * @return
     */
    @Override
    public String videoScreenshot(@RequestParam("filePath")String filePath,@RequestParam("deviceId")String deviceId,
                                  @RequestParam("planId") String planId, @RequestParam("tenantId") Long tenantId) {
        return videoFileService.videoScreenshot(filePath, deviceId, planId, tenantId);
    }
}
