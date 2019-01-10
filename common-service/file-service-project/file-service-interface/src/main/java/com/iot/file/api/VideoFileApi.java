package com.iot.file.api;

import com.iot.file.dto.FileDto;
import com.iot.file.vo.FileInfoRedisVo;
import com.iot.file.vo.VideoFileGetUrlReq;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 项目名称：cloud
 * 功能描述：视频服务专用的文件接口
 * 创建人： yeshiyuan
 * 创建时间：2018/8/2 10:24
 * 修改人： yeshiyuan
 * 修改时间：2018/8/2 10:24
 * 修改描述：
 */
@Api(value = "视频服务专用的文件接口")
@RequestMapping("/api/videoFile")
@FeignClient(value = "file-service",configuration = FileApi.MultipartSupportConfig.class)
public interface VideoFileApi {

    /**
     * @despriction：获取文件服务器的上传url
     * @author  yeshiyuan
     * @created 2018/8/2 10:26
     * @param tenantId 租户id
     * @param planId 计划id
     * @param fileType 文件类型
     * @return
     */
    @ApiOperation(value="获取文件服务器的上传url", notes="获取文件服务器的上传url")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "tenantId", value = "租户id",paramType = "query", required = true,dataType = "String"),
            @ApiImplicitParam(name = "planId", value = "计划id",paramType = "query", required = true,dataType = "String"),
            @ApiImplicitParam(name = "fileType", value = "文件类型", required = true, paramType = "query", dataType = "String")
    })
    @RequestMapping(value = "/getUploadUrl", method = {RequestMethod.POST})
    FileDto getUploadUrl(@RequestParam("tenantId") Long tenantId,@RequestParam("planId") String planId, @RequestParam("fileType") String fileType);

    /**
      * @despriction：通过文件路径获取文件的可访问url
      * @author  yeshiyuan
      * @created 2018/8/2 10:32
      * @param null
      * @return
      */
    @ApiOperation(value="通过文件路径获取文件的可访问url", notes="通过文件路径获取文件的可访问url")
    @ApiImplicitParam(name = "filePath", value = "文件路径", required = true, dataType = "String", paramType = "query")
    @RequestMapping(value = "/getUrlByFilePath", method = {RequestMethod.GET})
    FileDto getUrlByFilePath(@RequestParam("filePath") String filePath);

    /**
      * @despriction：通过文件路径批量获取文件的可访问url
      * @author  yeshiyuan
      * @created 2018/8/2 15:08
      * @return
      */
    @ApiOperation(value="通过文件路径批量获取文件的可访问url", notes="通过文件路径批量获取文件的可访问url")
    @RequestMapping(value = "/getUrlByFilePaths", method = {RequestMethod.POST}, consumes = MediaType.APPLICATION_JSON_VALUE)
    Map<String,String> getUrlByFilePaths(@RequestBody List<VideoFileGetUrlReq> reqs);

    /**
      * @despriction：从redis中获取文件信息
      * @author  yeshiyuan
      * @created 2018/8/2 14:23
      * @param fileId 文件uuid
      * @return
      */
    @ApiOperation(value = "从redis中获取文件信息", notes = "从redis中获取文件信息")
    @ApiImplicitParam(name = "fileId", value = "文件uuid", required = true, dataType = "String", paramType = "query")
    @RequestMapping(value = "/getFileInfoFromRedis", method = RequestMethod.GET)
    FileInfoRedisVo getFileInfoFromRedis(@RequestParam("fileId") String fileId);
    
    /**
      * @despriction：删除redis中的文件信息
      * @author  yeshiyuan
      * @created 2018/8/2 14:38
      * @param null
      * @return 
      */
    @ApiOperation(value = "删除redis中的文件信息", notes = "删除redis中的文件信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "fileId", value = "文件uuid", required = true, dataType = "String", paramType = "query") ,
            @ApiImplicitParam(name = "dateStr", value = "时间（YYYY-MM-dd:HH）", required = true, dataType = "String", paramType = "query") ,
    })
    @RequestMapping(value = "/deleteFileInfoFromRedis", method = RequestMethod.POST)
    void deleteFileInfoFromRedis(@RequestParam("fileId") String fileId, @RequestParam("dateStr") String dateStr);

    /**
      * @despriction：获取待进行校验清除的文件(定时任务使用)
      * @author  yeshiyuan
      * @created 2018/8/2 16:28
      * @return
      */
    @ApiOperation(value = "获取待进行校验清除的文件(定时任务使用)", notes = "获取待进行校验清除的文件(定时任务使用)")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "dateStr", value = "时间（YYYY-MM-dd:HH）", required = true, dataType = "String", paramType = "query") ,
    })
    @RequestMapping(value = "/getFileUuidFromRedisByDate", method = RequestMethod.GET)
    Set<String> getFileUuidFromRedisByDate(@RequestParam("dateStr") String dateStr);



    /**
      * @despriction：处理某时间内的未上报信息的文件(定时任务使用)
      * @author  yeshiyuan
      * @created 2018/8/2 17:06
      * @return
      */
    @ApiOperation(value = "处理某时间内的未上报信息的文件(定时任务使用)", notes = "处理某时间内的未上报信息的文件(定时任务使用)")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "date", value = "时间（YYYY-MM-dd:HH）", required = true, dataType = "String", paramType = "query") ,
    })
    @RequestMapping(value = "/dealUnUploadFileInfoTask", method = RequestMethod.POST)
    void dealUnUploadFileInfoTask(@RequestParam("date") String date);

    /**
     * @despriction：从redis中获取文件路径进而删除s3文件
     * @author  yeshiyuan
     * @created 2018/6/5 14:40
     * @param redisTaskId
     * @return
     */
    @ApiOperation(value = "从redis中获取文件路径进而删除s3文件",notes = "从redis中获取文件路径进而删除s3文件")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "redisTaskId",value = "redis中待删除文件的taskId",required = true,dataType = "String",paramType = "query")
    })
    @RequestMapping(value = "/deleteFileByRedisTaskId",method = RequestMethod.POST)
    void deleteFileByRedisTaskId(@RequestParam("redisTaskId") String redisTaskId);

    /**
      * @despriction：批量删除文件
      * @author  yeshiyuan
      * @created 2018/8/9 17:57
      * @return
      */
    @RequestMapping(value = "/deleteFileByFilePath",method = RequestMethod.POST)
    void deleteFileByFilePath(@RequestParam("filePaths") List<String> filePath);

    /**
     *
     * 描述：视频截图
     * @author yeshiyuan
     * @created 2018年8月10日 下午2:04:02
     * @return
     */
    @ApiOperation(value="视频截图", notes="视频截图")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "filePath", value = "文件路径", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "deviceId", value = "设备id", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "planId", value = "计划id", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "tenantId", value = "租户id", required = true, dataType = "Long", paramType = "query")
    })
    @RequestMapping(value = "/videoScreenshot", method = {RequestMethod.POST})
    String videoScreenshot(@RequestParam("filePath")String filePath,@RequestParam("deviceId")String deviceId,
                           @RequestParam("planId") String planId, @RequestParam("tenantId") Long tenantId);
}
