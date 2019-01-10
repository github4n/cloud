package com.iot.video.vo;

import io.swagger.annotations.ApiModelProperty;

/**
 * 项目名称：cloud
 * 功能描述：
 * 创建人： yeshiyuan
 * 创建时间：2018/10/11 13:39
 * 修改人： yeshiyuan
 * 修改时间：2018/10/11 13:39
 * 修改描述：
 */

public class VideoEventJpg  {
    @ApiModelProperty(name = "rotation", value = "旋转角度", dataType = "Integer")
    private Integer rotation;

    @ApiModelProperty(name = "fileId", value = "文件uuid", dataType = "String")
    private String fileId;

    @ApiModelProperty(name = "filePath", value = "文件路径", dataType = "String")
    private String filePath;

    public Integer getRotation() {
        return rotation;
    }

    public void setRotation(Integer rotation) {
        this.rotation = rotation;
    }

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

    public VideoEventJpg(Integer rotation, String fileId, String filePath) {
        this.rotation = rotation;
        this.fileId = fileId;
        this.filePath = filePath;
    }
}
