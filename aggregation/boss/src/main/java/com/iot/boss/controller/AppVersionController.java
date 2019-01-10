package com.iot.boss.controller;

import com.iot.boss.vo.UploadFileResp;
import com.iot.common.beans.CommonResponse;
import com.iot.common.exception.ResultMsg;
import com.iot.common.helper.Page;
import com.iot.file.api.FileApi;
import com.iot.file.api.FileUploadApi;
import com.iot.file.vo.FileInfoResp;
import com.iot.tenant.api.AppVersionApi;
import com.iot.tenant.vo.req.AppVersionListReq;
import com.iot.tenant.vo.req.SaveAppVersionReq;
import com.iot.tenant.vo.resp.AppVersionResp;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

/**
 * 描述：应用版本记录
 * 创建人： LaiGuiMing
 * 创建时间： 2018/9/12 14:22
 */
@Api(description = "应用版本记录接口",value = "应用版本记录接口")
@RestController
@RequestMapping("/api/appVersion")
public class AppVersionController {

    private String uploadFileStr;

    @Autowired
    private FileUploadApi fileUploadApi;

    @Autowired
    private FileApi fileApi;

    @Autowired
    private AppVersionApi appVersionApi;


    @ApiOperation("上传文件")
    @RequestMapping(value = "/uploadFile", method = RequestMethod.POST)
    public CommonResponse<UploadFileResp> uploadFile(MultipartHttpServletRequest multipartRequest) {
        UploadFileResp resp = new UploadFileResp();
        //判空
        if (multipartRequest == null || !multipartRequest.getFileNames().hasNext()) {
            return new CommonResponse<UploadFileResp>(ResultMsg.FAIL, "The file cannot be empty..", resp);
        }

        //上传文件
        MultipartFile multipartFile = multipartRequest.getFile(multipartRequest.getFileNames().next());
        String fileSuffix = multipartFile.getOriginalFilename().substring(multipartFile.getOriginalFilename().lastIndexOf(".") + 1).toLowerCase();
        //String[] fileResult=uploadFileStr.split(",");
        //Boolean isOk=Arrays.asList(fileResult).contains(fileSuffix);
        //if(isOk){
        String tenantIdStr = multipartRequest.getParameter("tenantId");
        Long tenantId = 0l;
        if (!StringUtils.isEmpty(tenantIdStr)) {
            tenantId = Long.parseLong(tenantIdStr);
        }
        FileInfoResp fileInfoResp = this.fileUploadApi.upLoadFileAndGetMd5(multipartFile, tenantId);
        resp.setFileId(fileInfoResp.getFileId());
        resp.setMd5(fileInfoResp.getMd5());

        return new CommonResponse<>(ResultMsg.SUCCESS, "Success.", resp);
        /*}else {
            return new CommonResponse<>(ResultMsg.FAIL, "file type is not allowed", "");
        }*/
    }

    @ApiOperation("分页获取应用版本列表")
    @RequestMapping(value = "/getAppVersionPage", method = RequestMethod.POST)
    public CommonResponse<Page<AppVersionResp>> getAppVersionPage(@RequestBody AppVersionListReq req) {
        Page<AppVersionResp> page = appVersionApi.list(req);
        page.setPageNum(req.getPageNum());
        return new CommonResponse<>(ResultMsg.SUCCESS, "Success.", page);
    }

    @ApiOperation("保存app版本信息")
    @RequestMapping(value = "/saveAppVersion", method = RequestMethod.POST)
    public CommonResponse<Long>  saveAppVersion(@RequestBody SaveAppVersionReq req){
        return new CommonResponse<>(ResultMsg.SUCCESS, "Success.", appVersionApi.save(req));
    }
}
