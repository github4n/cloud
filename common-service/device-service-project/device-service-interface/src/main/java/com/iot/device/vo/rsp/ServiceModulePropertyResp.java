package com.iot.device.vo.rsp;

import com.iot.device.vo.rsp.product.ParentVO;
import com.iot.device.vo.rsp.voicebox.SmartDataPointResp;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @Author: xfz
 * @Descrpiton:
 * @Date: 17:48 2018/6/29
 * @Modify by:
 */
@Data
@ToString
@ApiModel("功能组属性基础信息")
public class ServiceModulePropertyResp implements Serializable {
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
     * 属性名称
     */
    @ApiModelProperty(value = "属性名称")
    private String name;
    /**
     * 唯一标识code
     */
    @ApiModelProperty(value = "唯一标识code")
    private String code;
    /**
     * 标签
     */
    @ApiModelProperty(value = "标签")
    private String tags;
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
     * 请求参数格式：0 array ,1 object
     */
    @ApiModelProperty(value = "请求参数格式：0 array ,1 object")
    private Integer reqParamType;
    /**
     * 返回参数格式：0 array ,1 object
     */
    @ApiModelProperty(value = "返回参数格式：0 array ,1 object")
    private Integer returnType;
    /**
     * 属性状态，0:可选,1:必选
     */
    @ApiModelProperty(value = "属性状态，0:可选,1:必选")
    private Integer propertyStatus;
    /**
     * 读写特征 0:可读可写,1:不可读不可写,2:可读不可写
     */
    @ApiModelProperty(value = "读写特征 0:可读可写,1:不可读不可写,2:可读不可写")
    private Integer rwStatus;
    /**
     * 参数类型，0:int,1:float,2:bool,3:enum,4:string
     */
    @ApiModelProperty(value = "参数类型，0:int,1:float,2:bool,3:enum,4:string")
    private Integer paramType;
    /**
     * 最小值
     */
    @ApiModelProperty(value = "最小值")
    private String minValue;
    /**
     * 最大值
     */
    @ApiModelProperty(value = "最大值")
    private String maxValue;
    /**
     * 允许值(enum 可以多个逗号隔开)
     */
    @ApiModelProperty(value = "允许值(enum 可以多个逗号隔开)")
    private String allowedValues;
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

    @ApiModelProperty(value = "property类型(0:property类型 1：参数类型)")
    private Integer propertyType;

    private Long propertyId;

    @ApiModelProperty(value = "property类型（0:入参 1:出参）")
    private Integer propertyParamType;

    @ApiModelProperty(value = "inHomePage类型(0:默认不在首页 1：在首页)")
    private Integer inHomePage;
    /**
     * 对应的音箱功能点
     */
    private List<SmartDataPointResp> smart;

    private Integer status;

}
