package com.iot.file.vo;

import java.io.Serializable;
import java.util.Date;

/**
 * 项目名称：cloud
 * 功能描述：redis中缓存的文件信息
 * 创建人： yeshiyuan
 * 创建时间：2018/8/2 11:13
 * 修改人： yeshiyuan
 * 修改时间：2018/8/2 11:13
 * 修改描述：
 */
public class FileInfoRedisVo implements Serializable{

    /**
     * 文件类型
     */
    private String fileType;

    /**
     * 存储文件路径
     */
    private String filePath;

    /**
     * 租户id
     */
    private Long tenantId;

    /**
     * 创建日期
     */
    private Date createTime;

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
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

    public FileInfoRedisVo(String fileType, String filePath, Long tenantId, Date createTime) {
        this.fileType = fileType;
        this.filePath = filePath;
        this.tenantId = tenantId;
        this.createTime = createTime;
    }

    public FileInfoRedisVo() {
    }
}
