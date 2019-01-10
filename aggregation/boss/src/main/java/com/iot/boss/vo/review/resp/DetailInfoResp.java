package com.iot.boss.vo.review.resp;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * 项目名称：云平台
 * 功能描述：审核记录Resp
 * 创建人： maochengyuan
 * 创建时间：2018/10/23 13:52
 * 修改人： maochengyuan
 * 修改时间：2018/10/23 13:52
 * 修改描述：
 */
@ApiModel(description = "审核详情Resp")
public class DetailInfoResp {

    @ApiModelProperty(name = "objInfo", value = "对象信息")
    private Object objInfo;

    @ApiModelProperty(name = "records", value = "审核记录")
    private List<ReviewRecordResp> records;

    public DetailInfoResp() {

    }

    public DetailInfoResp(List<ReviewRecordResp> records) {
        this.records = records;
    }

    public Object getObjInfo() {
        return objInfo;
    }

    public void setObjInfo(Object objInfo) {
        this.objInfo = objInfo;
    }

    public List<ReviewRecordResp> getRecords() {
        return records;
    }

    public void setRecords(List<ReviewRecordResp> records) {
        this.records = records;
    }

}
