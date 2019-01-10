package com.iot.videofile.vo.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @program: cloud
 * @description: 视频文件信息入参
 * @author: yeshiyuan
 * @create: 2018-12-19 10:27
 **/
@ApiModel(description = "视频文件信息入参")
@Data
public class VideoFileInfoReq {

    @ApiModelProperty(name = "fn", value = "文件名", dataType = "String")
    private String fn;
    @ApiModelProperty(name = "fileType", value = "文件类型", dataType = "String")
    private String fileType;
    @ApiModelProperty(name = "startTime", value = "开始时间", dataType = "String")
    private Date startTime;
    @ApiModelProperty(name = "length", value = "录影文件的时长，单位秒", dataType = "Float")
    private Float length;
    @ApiModelProperty(name = "size", value = "文件大小，单位字节", dataType = "Integer")
    private Integer size;
    @ApiModelProperty(name = "evtType", value = "事件编码", dataType = "String")
    private String evtType;
    @ApiModelProperty(name = "evtId", value = "事件uuid", dataType = "String")
    private String evtId;
    @ApiModelProperty(name = "rotation", value = "拍摄角度", dataType = "Integer")
    private Integer rotation;
}
