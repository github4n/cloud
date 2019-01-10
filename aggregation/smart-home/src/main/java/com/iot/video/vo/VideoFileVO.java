package com.iot.video.vo;

import java.io.Serializable;
import java.util.Date;

/**
 * 项目名称：立达信IOT云平台
 * 模块名称： 聚合层
 * 功能描述：视频文件VO
 * 创建人： wujianlong
 * 创建时间：2018年3月23日 下午5:58:32
 * 修改人：mao2080@sina.com
 * wujianlong 修改时间：2018年3月23日 下午5:58:32
 */
public class VideoFileVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 文件id
     */
    private String fileId;


    /**
     * 视频开始时间
     */
    private Date videoStartTime;

    /**
     * 视频结束时间
     */
    private Date videoEndTime;

    /**
     * 视频地址
     */
    private String url;

    /**
     * 选择角度
     */
    private Integer rotation;

    /**
     * 视频时常
     */
    /*private Float videoLength;


    *//**
     * 文件类型
     *//*
    private String fileType;*/

    /**
     * 文件大小
     */
    private int fileSize;

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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

   /* public Float getVideoLength() {
        return videoLength;
    }

    public void setVideoLength(Float videoLength) {
        this.videoLength = videoLength;
    }


    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }*/

    public int getFileSize() {
        return fileSize;
    }

    public void setFileSize(int fileSize) {
        this.fileSize = fileSize;
    }

    public Integer getRotation() {
        return rotation;
    }

    public void setRotation(Integer rotation) {
        this.rotation = rotation;
    }
}
