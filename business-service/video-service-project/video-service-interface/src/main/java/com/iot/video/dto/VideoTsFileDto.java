package com.iot.video.dto;

import java.io.Serializable;
import java.util.Date;

/**
 * 项目名称：cloud
 * 功能描述：视频文件信息（ts）
 * 创建人： yeshiyuan
 * 创建时间：2018/6/25 11:08
 * 修改人： yeshiyuan
 * 修改时间：2018/6/25 11:08
 * 修改描述：
 */
public class VideoTsFileDto implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 文件id */
    private String fileId;


    /** 视频开始时间 */
    private Date videoStartTime;

    /** 视频结束时间 */
    private Date videoEndTime;


    /** 视频时常 *//*
    private Float videoLength;*/

    /**
     * 文件大小
     */
    private int fileSize;

    /**
     * 文件路径
     */
    private String filePath;

    /**
     * 旋转角度
     */
    private Integer rotation;

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }


    public Date getVideoStartTime() {
        return videoStartTime;
    }

    public void setVideoStartTime(Date videoStartTime) {
        this.videoStartTime = videoStartTime;
    }

    public Date getVideoEndTime() {
        return videoEndTime;
    }

    public void setVideoEndTime(Date videoEndTime) {
        this.videoEndTime = videoEndTime;
    }


    public int getFileSize() {
        return fileSize;
    }

    public void setFileSize(int fileSize) {
        this.fileSize = fileSize;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public Integer getRotation() {
        if (rotation == null) {
            rotation = 0;
        }
        return rotation;
    }

    public void setRotation(Integer rotation) {
        this.rotation = rotation;
    }
}
