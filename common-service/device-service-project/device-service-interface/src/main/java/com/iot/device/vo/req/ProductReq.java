package com.iot.device.vo.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.ArrayList;

@Data
@ToString
public class ProductReq implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    /**
     * 产品id
     */
    @ApiModelProperty(value = "产品新增不需要填此参数", required = false)
    private Long id;

    /**
     * 设备类型id
     */
    private Long deviceTypeId;

    /**
     * 产品名称
     */
    private String productName;

    private Integer communicationMode;

    private Integer transmissionMode;

    private String model;

    private String configNetModes;

    private String remark;

    /**
     * 非自定义功能点Id
     */
    private ArrayList<Long> datapointIds = new ArrayList<>();

    /**
     * 自定义功能点
     */
    private ArrayList<DataPointReq> customDataPoints = new ArrayList<>();

    /**
     * 是否套包产品
     */
    private Integer isKit;
    /**
     * 是否直连设备
     */
    private Integer isDirectDevice;

    private Long tenantId;

    private Long createBy;

    private Long updateBy;
}