package com.iot.device.vo.rsp.device;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;

/**
 * @Author: lucky
 * @Descrpiton:
 * @Date: 16:48 2018/10/12
 * @Modify by:
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@ToString
public class GetDeviceTypeInfoRespVo {

    private Long id;
    /**
     * 类型名称
     */
    private String name;
    /**
     * catalog大分类的名称
     */
    private String deviceCatalogName;
    /**
     * 类型描述
     */
    private String description;
    /**
     * 设备分类id  extend device_catalog table  id
     * device_catalog_id
     */
    private Long deviceCatalogId;
    /**
     * tenant_id
     */
    private Long tenantId;
    /**
     * create_time
     */
    private Date createTime;
    /**
     * update_time
     */
    private Date updateTime;
    /**
     * create_by
     */
    private Long createBy;
    /**
     * update_by
     */
    private Long updateBy;
    /**
     * is_deleted
     */
    private Integer isDeleted;
    /**
     * 厂商标识
     * vender_flag
     */
    private String venderFlag;
    /**
     * 设备真实类型
     */
    private String type;
    /**
     * icon圖片id
     */
    private String img;
    /**
     * 是否为免开发产品
     */
    private Integer whetherSoc;

    @ApiModelProperty(value = "ifttt类型(0:不支持 1：支持if 2:支持then 3:支持if支持then)")
    private Integer iftttType;

}
