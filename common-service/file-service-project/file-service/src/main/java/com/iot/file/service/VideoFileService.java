package com.iot.file.service;

import com.iot.common.exception.BusinessException;
import com.iot.file.dto.FileDto;
import com.iot.file.vo.VideoFileGetUrlReq;

import java.util.List;
import java.util.Map;

/**
 * 项目名称：cloud
 * 功能描述：给视频服务专用的文件处理service
 * 创建人： yeshiyuan
 * 创建时间：2018/8/2 10:39
 * 修改人： yeshiyuan
 * 修改时间：2018/8/2 10:39
 * 修改描述：
 */
public interface VideoFileService {

    /**
     * @despriction：获取文件服务器的上传url
     * @author  yeshiyuan
     * @created 2018/8/2 10:26
     * @param tenantId 租户id
     * @param planId 计划id
     * @param fileType 文件类型
     * @return
     */
    FileDto getUploadUrl(Long tenantId, String planId, String fileType);

    /**
     * @despriction：通过文件路径获取文件的可访问url
     * @author  yeshiyuan
     * @created 2018/8/2 10:32
     * @param filePath 文件路径
     * @return
     */
    FileDto getUrlByFilePath(String filePath);

    /**
     * @despriction：通过文件路径批量获取文件的可访问url
     * @author  yeshiyuan
     * @created 2018/8/2 10:32
     * @param filePaths 文件路径集合
     * @return
     */
    Map<String,String> getUrlByFilePaths(List<VideoFileGetUrlReq> filePaths);

    /**
     * @despriction：处理某时间内的未上报信息的文件(定时任务使用)
     * @author  yeshiyuan
     * @created 2018/8/2 17:06
     * @return
     */
    void dealUnUploadFileInfoTask(String dateStr) ;

    /**
     * @despriction：从redis中获取文件路径进而删除s3数据
     * @author  yeshiyuan
     * @created 2018/6/5 14:40
     * @param redisTaskId
     * @return
     */
    void deleteFileByRedisTaskId(String redisTaskId);

    /**
      * @despriction：批量删除文件
      * @author  yeshiyuan
      * @created 2018/8/10 11:48
      * @return
      */
    void deleteFileByFilePath(List<String> filePath);

    /**
     *
     * 描述：视频截图
     * @author yeshiyuan
     * @created 2018年8月29日 下午2:04:59
     * @since
     * @param fileId
     * @return
     */
    String videoScreenshot(String filePath,String deviceId,String planId, Long tenantId) throws BusinessException;
}
