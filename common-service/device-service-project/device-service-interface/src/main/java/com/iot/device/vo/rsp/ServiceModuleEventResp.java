package com.iot.device.vo.rsp;

import com.iot.device.vo.rsp.product.ParentVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

/**
 * @Author: xfz
 * @Descrpiton:
 * @Date: 17:37 2018/6/29
 * @Modify by:
 */

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
@ApiModel("功能模组事件信息")
public class ServiceModuleEventResp implements Serializable {
    /**
     * 主键id
     */
    @ApiModelProperty(value = "主键id")
    private Long id;
    /**
     * parent_ID
     */
    @ApiModelProperty(value = "parent_ID")
    private Long parentId;

    @ApiModelProperty(value = "标准信息")
    private ParentVO parent;

    /**
     * 租户ID
     */
    @ApiModelProperty(value = "租户ID")
    private Long tenantId;
    /**
     * 模组id
     */
    @ApiModelProperty(value = "模组id")
    private Long serviceModuleId;
    /**
     * 版本
     */
    @ApiModelProperty(value = "版本")
    private String version;
    /**
     * 事件名称
     */
    @ApiModelProperty(value = "事件名称")
    private String name;
    /**
     * 唯一标识code
     */
    @ApiModelProperty(value = "唯一标识code")
    private String code;
    /**
     * api等级
     */
    @ApiModelProperty(value = "api等级")
    private Integer apiLevel;
    /**
     * 开发状态,0:未开发,1:开发中,2:已上线
     */
    @ApiModelProperty(value = "开发状态,0:未开发,1:开发中,2:已上线")
    private Integer developStatus;
    /**
     * 属性状态，0:可选,1:必选
     */
    @ApiModelProperty(value = "属性状态，0:可选,1:必选")
    private Integer propertyStatus;
    /**
     * json参数集
     */
    @ApiModelProperty(value = "json参数集")
    private String params;
    /**
     * 标签
     */
    @ApiModelProperty(value = "标签")
    private String tags;
    /**
     * 测试用例
     */
    @ApiModelProperty(value = "测试用例")
    private String testCase;

    private Long createBy;

    private Date createTime;

    private Long updateBy;

    private Date updateTime;

    /**
     * 描述
     */
    @ApiModelProperty(value = "描述")
    private String description;

    @ApiModelProperty(value = "ifttt类型(0:不支持 1：支持if 2:支持then 3:支持if支持then)")
    private Integer iftttType;

    @ApiModelProperty(value = "portal联动设置(0:不支持 1：支持if 2:支持then 3:支持if支持then)")
    private Integer portalIftttType;


    private Long eventId;

    private Integer status;

}
