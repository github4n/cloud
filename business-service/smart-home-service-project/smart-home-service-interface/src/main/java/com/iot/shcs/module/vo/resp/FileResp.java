package com.iot.shcs.module.vo.resp;

import java.io.Serializable;

/**
 * @Author: lucky @Descrpiton: @Date: 11:52 2018/7/11 @Modify by:
 */
public class FileResp implements Serializable {

    private String fileId;

    private String url;

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
}
