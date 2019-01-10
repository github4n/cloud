package com.iot.control.activity.vo.rsp;

import com.iot.common.annotation.AutoMongoId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.data.annotation.Id;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

@ApiModel(value = "app错误日志")
public class AppErrorRecordResp implements Serializable {

    private static final long serialVersionUID = -3120697549382646533L;

    @Id
    @AutoMongoId
    @ApiModelProperty("主键id，保存时不用传")
    private Long id;

    @NotNull(message = "userId.notnull")
    @ApiModelProperty("用户uuid")
    private String userId;

    @ApiModelProperty("租户Id")
    private Long tenantId;

    @ApiModelProperty("返回码")
    private Integer code;

    @NotNull(message = "error.message.notnull")
    @ApiModelProperty("错误信息描述")
    private String message;

    @ApiModelProperty("时间戳")
    private Long timeStamp;

    @ApiModelProperty("请求路径")
    private String path;

    @ApiModelProperty("记录时间")
    private Date recordTime;

    @ApiModelProperty("删除标志，0否1是")
    private Integer delFlag;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Date getRecordTime() {
        return recordTime;
    }

    public void setRecordTime(Date recordTime) {
        this.recordTime = recordTime;
    }

    public Integer getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(Integer delFlag) {
        this.delFlag = delFlag;
    }
}
