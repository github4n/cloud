package com.iot.file.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

/**
 * 项目名称：cloud
 * 功能描述：file_info对应实体类
 * 创建人： yeshiyuan
 * 创建时间：2018/8/21 14:41
 * 修改人： yeshiyuan
 * 修改时间：2018/8/21 14:41
 * 修改描述：
 */
@ApiModel(value = "file_info对应实体类")
public class FileInfo {

    @ApiModelProperty(name = "id", value = "主键", dataType = "Long")
    private Long id;
    @ApiModelProperty(name = "fileId", value = "文件uuid", dataType = "String")
    private String fileId;
    @ApiModelProperty(name = "fileType", value = "文件类型", dataType = "String")
    private String fileType;
    @ApiModelProperty(name = "tenantId", value = "租户id", dataType = "Long")
    private Long tenantId;
    @ApiModelProperty(name = "createTime", value = "创建时间", dataType = "Date")
    private Date createTime;
    @ApiModelProperty(name = "filePath", value = "文件路径", dataType = "String")
    private String filePath;
    @ApiModelProperty(name = "updateTime", value = "修改时间", dataType = "Date")
    private Date updateTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}
