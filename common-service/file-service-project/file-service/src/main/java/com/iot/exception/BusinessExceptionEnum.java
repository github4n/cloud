package com.iot.exception;


import com.iot.common.exception.IBusinessException;

/**
 * 
 * 项目名称：IOT视频云
 * 模块名称：服务层
 * 功能描述：文件服务异常枚举
 * 创建人： mao2080@sina.com 
 * 创建时间：2018/3/22 15:16
 * 修改人： mao2080@sina.com 
 * 修改时间：2018/3/22 15:16
 * 修改描述：
 */
public enum BusinessExceptionEnum implements IBusinessException {

    /**未知异常*/
    UNKNOWN_EXCEPTION("fileservice.unknow.exception"),

    /**获取puturl出错*/
    GET_PUT_URL_ERROR("fileservice.get.put.url.error"),

    /**获取geturl出错*/
    GET_GET_URL_ERROR("fileservice.get.get.url.error"),

    /**删除文件出错*/
    DELETE_FILE_ERROR("fileservice.delete.file.error"),

    /**租户id为空*/
    TENANTID_ISNULL("fileservice.tenantId.isnull"),

    /**文件类型为空*/
    FILETYPE_ISNULL("fileservice.filetype.isnull"),

    /**文件数量不正确*/
    FILE_URL_NUM_INCORRECT("fileservice.file.url.num.incorrect"),

    /**批量获取puturl出错*/
    BATCH_GET_PUT_URL_ERROR("fileservice.batch.get.put.url.error"),

    /**批量获取geturl出错*/
    BATCH_GET_GET_URL_ERROR("fileservice.batch.get.get.url.error"),

    /**批量获取删除文件出错*/
    BATCH_DELETE_FILE_ERROR("fileservice.batch.delete.file.error"),

    /**未找到对应的文件关系*/
    FILE_RELATIONSHIP_NOT_FOUND("fileservice.relationship.not.found"),

    /**文件id为空*/
    FILEID_ISNULL("fileservice.fileId.isnull"),

    /**文件为空*/
    FILE_ISNULL("fileservice.file.isnull"),
    
    /**截图出错*/
    VIDEO_SCREENSHOT_ERROR("fileservice.screenshot.error"),

    /**上传文件出错*/
    VIDEO_UPLOADFILE_ERROR("fileservice.upLoadFile.error"),

    /**修改文件出错*/
    VIDEO_UPDATEFILE_ERROR("fileservice.updateFile.error"),

    /** 计划id为空*/
    PLANID_ISNULL("fileservice.planId.isnull"),

    /** 获取文件md5异常*/
    GET_MD5_ERROR("get.md5.error"),

    FILE_PATH_ISNULL("fileservice.filePath.isNull")
    ;

    private String messageKey;

    BusinessExceptionEnum(String messageKey) {
        this.messageKey = messageKey;
    }

    @Override
    public int getCode() {
        return 0;
    }

    public void setMessageKey(String messageKey) {
        this.messageKey = messageKey;
    }

    @Override
    public String getMessageKey() {
        return this.messageKey;
    }

}