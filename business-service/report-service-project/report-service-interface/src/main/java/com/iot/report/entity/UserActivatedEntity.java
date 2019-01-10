package com.iot.report.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

/**
 *@description 用户激活实体类，对应mongodb中collection为user_activated_info
 *@author wucheng
 *@create 2018/12/29 13:51
 */
@ApiModel(value = "用户激活实体类")
@Data
@Document(collection = "user_activated_info")
public class UserActivatedEntity {

    @ApiModelProperty(name = "uuid", value = "用户uuid")
    private String uuid;

    @ApiModelProperty(name = "tenantId", value = "租户id")
    private Long tenantId;

    @ApiModelProperty(name = "dataDate", value = "激活日期")
    private Date activatedDate;

    public UserActivatedEntity(String uuid, Long tenantId, Date activatedDate) {
        this.uuid = uuid;
        this.tenantId = tenantId;
        this.activatedDate = activatedDate;
    }
}
