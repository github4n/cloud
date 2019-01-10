package com.iot.portal.web.controller;

import com.iot.common.annotation.Action;
import com.iot.common.annotation.LoginRequired;
import com.iot.common.beans.CommonResponse;
import com.iot.common.exception.BusinessException;
import com.iot.common.exception.IBusinessException;
import com.iot.file.api.FileApi;
import com.iot.portal.service.FileService;
import com.iot.portal.web.vo.FileResp;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.util.Arrays;

/**
 * @Author: lucky
 * @Descrpiton:
 * @Date: 11:49 2018/7/11
 * @Modify by:
 */
@Api(value = "portal-文件管理", description = "portal-文件管理")
@RestController
@RequestMapping("/portal/file")
public class PortalFileController {

    @Autowired
    private FileService fileService;

    @Value("${upload.img.str}")
    private String uploadImgStr;

    @Value("${upload.file.str}")
    private String uploadFileStr;

    @Autowired
    private FileApi fileApi;

    @ApiOperation(value = "getUrl", notes = "获取")
    @RequestMapping(value = "/getUrl", method = RequestMethod.GET)
    public CommonResponse<FileResp> getUrl(@RequestParam("fileId") String fileId) {
        if (StringUtils.isEmpty(fileId) || "null".equals(fileId)) {
            return new CommonResponse(400, "fileId not null");
        }
        FileResp fileResp = fileService.getUrl(fileId);
        return CommonResponse.success(fileResp);
    }

    /**
     * 描述：uploadImg ota
     *
     * @param multipartRequest 文件参数
     */
    @ApiOperation(value = "uploadImg", notes = "上传图片")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "multipartRequest", value = "ota文件", required = false, paramType = "query", dataType = "String")
    })
    @RequestMapping(value = "/uploadImg", method = RequestMethod.POST)
    public CommonResponse<FileResp> uploadImg(MultipartHttpServletRequest multipartRequest) {
        if (multipartRequest != null && multipartRequest.getFileNames().hasNext()) {
            MultipartFile file = multipartRequest.getFile("file");
            String fileNameSuffix = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".") + 1).toLowerCase();
            String[] result = uploadImgStr.split(",");
            Boolean isOk = Arrays.asList(result).contains(fileNameSuffix);
            if (isOk) {
                FileResp fileResp = fileService.upload(multipartRequest);
                return CommonResponse.success(fileResp);
            } else {
                throw new BusinessException(new IBusinessException() {
                    @Override
                    public int getCode() {
                        return 400;
                    }

                    @Override
                    public String getMessageKey() {
                        return "Illegal file type.";
                    }
                });
            }
        }else {
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


    /**
     * 描述：uploadfile ota
     *
     * @param multipartRequest 文件参数
     */
    @ApiOperation(value = "uploadFile", notes = "上传文件")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "multipartRequest", value = "ota文件", required = false, paramType = "query", dataType = "String")
    })
    @RequestMapping(value = "/uploadFile", method = RequestMethod.POST)
    public CommonResponse<FileResp> uploadFile(MultipartHttpServletRequest multipartRequest) {
        if (multipartRequest != null && multipartRequest.getFileNames().hasNext()) {
            MultipartFile file = multipartRequest.getFile("file");
            String fileNameSuffix = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".") + 1).toLowerCase();
            String[] result = uploadFileStr.split(",");
            Boolean isOk = Arrays.asList(result).contains(fileNameSuffix);
            if (isOk) {
                FileResp fileResp = fileService.upload(multipartRequest);
                return CommonResponse.success(fileResp);
            } else {
                throw new BusinessException(new IBusinessException() {
                    @Override
                    public int getCode() {
                        return 400;
                    }

                    @Override
                    public String getMessageKey() {
                        return "Illegal file type.";
                    }
                });
            }
        }else {
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
        FileResp fileResp = fileService.uploadButNoSaveToDb(multipartRequest);
        return CommonResponse.success(fileResp);
    }

    /**
      * @despriction：获取下载验证码
      * @author  yeshiyuan
      * @created 2018/11/28 19:29
      */
    @LoginRequired(Action.Normal)
    @ApiOperation(value = "获取下载验证码", notes = "获取下载验证码")
    @RequestMapping(value = "/getDownloadCode", method = RequestMethod.GET)
    public CommonResponse getDownloadCode() {
        return CommonResponse.success(fileApi.getDownloadCode(30));
    }
}
