package com.iot.boss.controller;

import com.iot.boss.service.file.FileService;
import com.iot.boss.vo.FileResp;
import com.iot.common.annotation.Action;
import com.iot.common.annotation.LoginRequired;
import com.iot.common.beans.CommonResponse;
import com.iot.common.exception.BusinessException;
import com.iot.common.exception.IBusinessException;
import com.iot.file.vo.FileInfoResp;
import com.iot.saas.SaaSContextHolder;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartHttpServletRequest;

/**
 * @Author: lucky
 * @Descrpiton:
 * @Date: 11:49 2018/7/11
 * @Modify by:
 */
@Api(value = "portal-文件管理", description = "portal-文件管理")
@RestController
@RequestMapping("/api/file")
public class FileController {

    private Logger log = LoggerFactory.getLogger(FileController.class);
    @Autowired
    private FileService fileService;

    @ApiOperation(value = "getUrl", notes = "获取")
    @LoginRequired(value = Action.Normal)
    @RequestMapping(value = "/getUrl", method = RequestMethod.GET)
    public CommonResponse<FileResp> getUrl(@RequestParam("fileId") String fileId) {
        if (StringUtils.isEmpty(fileId) || "null".equals(fileId)) {
            return new CommonResponse(400, "fileId not null");
        }
        FileResp fileResp = fileService.getUrl(fileId);
        return CommonResponse.success(fileResp);
    }

    /**
     * 描述：upload ota
     *
     * @param multipartRequest 文件参数
     */
    @ApiOperation(value = "upload", notes = "上传文件")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "multipartRequest", value = "ota文件", required = false, paramType = "query", dataType = "String")
    })
    @LoginRequired(value = Action.Normal)
    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public CommonResponse<FileResp> upload(MultipartHttpServletRequest multipartRequest) {
        checkMulitpartRequest(multipartRequest);
        log.debug("boss.uploadControl"+ SaaSContextHolder.currentTenantId());
        FileResp fileResp = fileService.upload(multipartRequest);
        return CommonResponse.success(fileResp);
    }

    /**
     * 描述：upload ota
     *
     * @param multipartRequest 文件参数
     */
    @ApiOperation(value = "upLoadAndValidation", notes = "上传文件")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "multipartRequest", value = "ota文件", required = false, paramType = "query", dataType = "String")
    })
    @LoginRequired(value = Action.Normal)
    @RequestMapping(value = "/upLoadAndValidation", method = RequestMethod.POST)
    public CommonResponse<FileInfoResp> upLoadAndValidation(MultipartHttpServletRequest multipartRequest){
        checkMulitpartRequest(multipartRequest);
        FileInfoResp fileResp = fileService.upLoadAndValidation(multipartRequest);
        return CommonResponse.success(fileResp);
    }



    /**
     * @despriction：上传文件但不保存文件信息至数据库（需另外调用FileApi.saveFileInfosToDb入库）
     * @author  yeshiyuan
     * @created 2018/10/9 19:46
     * @return
     */
    @LoginRequired(Action.Normal)
    @ApiOperation(value = "上传文件但不保存文件信息至数据库", notes = "需另外调用FileApi.saveFileInfosToDb入库")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "multipartRequest", value = "文件", required = false, paramType = "query", dataType = "String")
    })
    @RequestMapping(value = "/uploadButNoSaveToDb", method = RequestMethod.POST)
    public CommonResponse<FileResp> uploadButNoSaveToDb(MultipartHttpServletRequest multipartRequest) {
        checkMulitpartRequest(multipartRequest);
        FileResp fileResp = fileService.uploadButNoSaveToDb(multipartRequest);
        return CommonResponse.success(fileResp);
    }

    private void checkMulitpartRequest(MultipartHttpServletRequest multipartRequest) {
        if (multipartRequest == null || (multipartRequest != null && !multipartRequest.getFileNames().hasNext())) {
            throw new BusinessException(new IBusinessException() {
                @Override
                public int getCode() {
                    return 400;
                }

                @Override
                public String getMessageKey() {
                    return "The file cannot be empty.";
                }
            });
        }
    }
}
