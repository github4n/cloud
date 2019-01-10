package com.iot.file.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 项目名称：cloud
 * 模块名称：
 * 功能描述：
 * 创建人： yeshiyuan
 * 创建时间：2018/8/30 15:21
 * 修改人： yeshiyuan
 * 修改时间：2018/8/30 15:21
 * 修改描述：
 */
@ApiModel(value = "文件信息")
public class FileInfoResp {

    @ApiModelProperty(name = "fileId", value = "文件uuid", dataType = "String")
    private String fileId;

    @ApiModelProperty(name = "url", value = "文件访问url", dataType = "String")
    private String url;

    @ApiModelProperty(name = "md5", value = "文件对应的MD5", dataType = "String")
    private String md5;


    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }

    public FileInfoResp() {
    }

    public FileInfoResp(String fileId) {
        this.fileId = fileId;
    }

    public FileInfoResp(String fileId, String url) {
        this.fileId = fileId;
        this.url = url;
    }

    public FileInfoResp(String fileId, String url, String md5) {
        this.fileId = fileId;
        this.url = url;
        this.md5 = md5;
    }
}
