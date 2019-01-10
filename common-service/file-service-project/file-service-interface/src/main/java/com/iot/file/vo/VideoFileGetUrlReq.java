package com.iot.file.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 项目名称：cloud
 * 功能描述：视频文件获取url时的入参
 * 创建人： yeshiyuan
 * 创建时间：2018/8/2 15:50
 * 修改人： yeshiyuan
 * 修改时间：2018/8/2 15:50
 * 修改描述：
 */
@ApiModel(value = "视频文件获取url时的入参")
public class VideoFileGetUrlReq {

    @ApiModelProperty(name = "fileId", value = "文件uuid", dataType = "String")
    private String fileId;

    @ApiModelProperty(name = "filePath", value = "文件路径", dataType = "String")
    private String filePath;

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public VideoFileGetUrlReq(String fileId, String filePath) {
        this.fileId = fileId;
        this.filePath = filePath;
    }

    public VideoFileGetUrlReq() {
    }
}
