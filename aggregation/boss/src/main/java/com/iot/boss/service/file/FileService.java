package com.iot.boss.service.file;

import com.iot.boss.vo.FileResp;
import com.iot.common.exception.BusinessException;
import com.iot.common.exception.IBusinessException;
import com.iot.file.api.FileApi;
import com.iot.file.api.FileUploadApi;
import com.iot.file.vo.FileInfoResp;
import com.iot.saas.SaaSContextHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: lucky
 * @Descrpiton:
 * @Date: 11:56 2018/7/11
 * @Modify by:
 */
@Component
public class FileService {
    private static Logger LOGGER = LoggerFactory.getLogger(FileService.class);
    @Autowired
    private FileApi fileApi;

    @Autowired
    private FileUploadApi fileUploadApi;

    public FileResp getUrl(String fileId) {
        String url = this.fileApi.getGetUrl(fileId).getPresignedUrl();
        FileResp fileResp = new FileResp();
        fileResp.setUrl(url);
        fileResp.setFileId(fileId);
        return fileResp;
    }

    public Map getUrl(List<String> fileIds) {
        Map map = new HashMap();
        if (fileIds.size()>0){
            map = this.fileApi.getGetUrl(fileIds);
        } else {
            map = null;
        }
        return map;
    }

    public FileResp upload(MultipartHttpServletRequest multipartRequest) {
        try {

            MultipartFile multipartFile = multipartRequest.getFile(multipartRequest.getFileNames().next());
            //上传文件
            LOGGER.debug("boss.uploadService"+SaaSContextHolder.currentTenantId());
            FileInfoResp resp = this.fileUploadApi.upLoadFileAndGetUrl(multipartFile, SaaSContextHolder.currentTenantId());
            FileResp fileResp = new FileResp();
            fileResp.setUrl(resp.getUrl());
            fileResp.setFileId(resp.getFileId());
            return fileResp;
        } catch (Exception e) {
            LOGGER.error("SYSTEM.ERROR", e);
            throw new BusinessException(new IBusinessException() {
                @Override
                public int getCode() {
                    return 500;
                }

                @Override
                public String getMessageKey() {
                    return "upload.file.error";
                }
            }, e);
        }
    }

    public FileInfoResp upLoadAndValidation(MultipartHttpServletRequest multipartRequest){
        MultipartFile multipartFile = multipartRequest.getFile(multipartRequest.getFileNames().next());
        FileInfoResp fileInfoResp = this.fileUploadApi.upLoadFileAndGetMd5(multipartFile, SaaSContextHolder.currentTenantId());
        return fileInfoResp;
    }


    /**
     * @despriction：上传文件但不保存文件信息至数据库（需另外调用FileApi.saveFileInfosToDb入库）
     * @author  yeshiyuan
     * @created 2018/10/9 19:52
     * @return
     */
    public FileResp uploadButNoSaveToDb(MultipartHttpServletRequest multipartRequest) {
        MultipartFile multipartFile = multipartRequest.getFile(multipartRequest.getFileNames().next());
        FileInfoResp resp = this.fileUploadApi.uploadFileButNoSaveToDb(multipartFile, SaaSContextHolder.currentTenantId());
        FileResp fileResp = new FileResp();
        fileResp.setUrl(resp.getUrl());
        fileResp.setFileId(resp.getFileId());
        return fileResp;
    }
}
