package com.iot.video.vo.resp;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * 项目名称：cloud
 * 功能描述：ipc上传前校验结果
 * 创建人： yeshiyuan
 * 创建时间：2018/9/3 17:45
 * 修改人： yeshiyuan
 * 修改时间：2018/9/3 17:45
 * 修改描述：
 */
@ApiModel(value = "ipc上传前校验结果")
public class CheckBeforeUploadResult implements Serializable {

    @ApiModelProperty(name = "paasFlag", value = "通过标志", dataType = "boolean")
    private boolean paasFlag;
    @ApiModelProperty(name = "desc", value = "结果描述", dataType = "String")
    private String desc;
    @ApiModelProperty(name = "data", value = "返回数据", dataType = "Object")
    private Object data;

    public boolean isPaasFlag() {
        return paasFlag;
    }

    public void setPaasFlag(boolean paasFlag) {
        this.paasFlag = paasFlag;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public CheckBeforeUploadResult() {
    }

    public CheckBeforeUploadResult(boolean paasFlag, String desc) {
        this.paasFlag = paasFlag;
        this.desc = desc;
    }

    public CheckBeforeUploadResult(boolean paasFlag, String desc, Object data) {
        this.paasFlag = paasFlag;
        this.desc = desc;
        this.data = data;
    }
}
