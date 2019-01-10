package com.iot.version;

import com.google.common.collect.Maps;
import com.iot.common.annotation.Action;
import com.iot.common.annotation.LoginRequired;
import com.iot.common.beans.CommonResponse;
import com.iot.common.exception.ResultMsg;
import com.iot.common.helper.Page;
import com.iot.file.api.FileApi;
import com.iot.file.api.FileUploadApi;
import com.iot.file.dto.FileDto;
import com.iot.file.vo.FileInfoResp;
import com.iot.tenant.api.AppVersionApi;
import com.iot.tenant.vo.req.AppVersionListReq;
import com.iot.tenant.vo.req.CheckVersionRequest;
import com.iot.tenant.vo.req.SaveAppVersionReq;
import com.iot.tenant.vo.resp.AppVersionResp;
import com.iot.tenant.vo.resp.CheckVersionResponse;
import com.iot.version.vo.CheckVersionReq;
import com.iot.version.vo.CheckVersionResp;
import com.iot.vo.UploadFileResp;
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

import java.util.Map;

/**
 * 描述：应用版本记录
 * 创建人： LaiGuiMing
 * 创建时间： 2018/9/12 14:22
 */
@Api("应用版本记录接口")
@RestController
@RequestMapping("/appVersion")
public class AppVersionController {

    private String uploadFileStr;

    @Autowired
    private FileUploadApi fileUploadApi;

    @Autowired
    private FileApi fileApi;

    @Autowired
    private AppVersionApi appVersionApi;


    @LoginRequired(value = Action.Skip)
    @ApiOperation("上传文件")
    @RequestMapping(value = "/uploadFile", method = RequestMethod.POST)
    public CommonResponse<UploadFileResp> uploadFile(MultipartHttpServletRequest multipartRequest) {
        UploadFileResp resp = new UploadFileResp();
        //判空
        if (multipartRequest == null || !multipartRequest.getFileNames().hasNext()) {
            return new CommonResponse<UploadFileResp>(ResultMsg.FAIL, "The file cannot be empty.", resp);
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


    @LoginRequired(value = Action.Skip)
    @ApiOperation("保存app版本信息")
    @RequestMapping(value = "/saveAppVersion", method = RequestMethod.POST)
    public CommonResponse<Long> saveAppVersion(@RequestBody SaveAppVersionReq req) {
        return new CommonResponse<>(ResultMsg.SUCCESS, "Success.", appVersionApi.save(req));
    }


    @LoginRequired(value = Action.Skip)
    @ApiOperation("检测更新版本信息")
    @RequestMapping(value = "/checkVersion", method = RequestMethod.POST)
    public CommonResponse<CheckVersionResp> checkVersion(@RequestBody CheckVersionReq req) {
        CheckVersionResp resp = new CheckVersionResp();
        // rc 0 成功 1增量包 2全量包
        //TODO 鉴权校验
        String key = req.getAppKey();

        //获取最新版本信息
        AppVersionResp appVersionResp = appVersionApi.getLastVersion(req.getAppId());

        if (appVersionResp == null) {
            return new CommonResponse<>(ResultMsg.FAIL, "app version not exist! appId=" + req.getAppId(), null);
        }

        CheckVersionRequest request = new CheckVersionRequest();
        request.setOldVer(req.getVersion());
        request.setNewVer(appVersionResp.getVersion());
        CheckVersionResponse response = appVersionApi.checkVersion(request);
        resp.setRc(response.getResult());

        FileDto fileDto;
        switch (response.getResult()) {
            case 0:
                break;
            case 1:
                fileDto = fileApi.getGetUrl(appVersionResp.getIncrFileId());
                resp.setUrl(fileDto.getPresignedUrl());
                resp.setMd5(appVersionResp.getIncrFileMd5());
                break;
            case 2:
                fileDto = fileApi.getGetUrl(appVersionResp.getFullFileId());
                resp.setUrl(fileDto.getPresignedUrl());
                resp.setMd5(appVersionResp.getFullFileMd5());
                break;
            default:
                break;
        }

        //mode
        resp.setInstallMode(appVersionResp.getInstallMode());
        resp.setVersion(appVersionResp.getVersion());
        resp.setRemark(appVersionResp.getRemark());
        return new CommonResponse<>(ResultMsg.SUCCESS, "Success.", resp);
    }
}
