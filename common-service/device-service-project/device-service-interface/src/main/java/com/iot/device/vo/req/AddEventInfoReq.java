package com.iot.device.vo.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * @Author: xfz
 * @Descrpiton: 事件
 * @Date: 17:32 2018/7/3
 * @Modify by:
 */
@Data
@ToString
public class AddEventInfoReq implements Serializable {

    /**
     * 主键id
     */
    private Long id;
    /**
     * parent_ID
     */
    private Long parentId;
    /**
     * 租户ID
     */
    private Long tenantId;
    /**
     * 模组id
     */
    private Long serviceModuleId;
    /**
     * 版本
     */
    private String version;
    /**
     * 方法名称
     */
    private String name;
    /**
     * 唯一标识code
     */
    private String code;
    /**
     * 标签
     */
    private String tags;
    /**
     * 等级
     */
    private Integer apiLevel;
    /**
     * 开发状态,0:未开发,1:开发中,2:已上线
     */
    private Integer developStatus;
    /**
     * 属性状态，0:可选,1:必选
     */
    private Integer propertyStatus;
    /**
     * 请求参数格式：0 array ,1 object
     */
    private Integer reqParamType;
    /**
     * 返回参数格式：0 array ,1 object
     */
    private Integer returnType;
    /**
     * json参数集
     */
    private String params;
    /**
     * 返回内容描述
     */
    private String returnDesc;
    /**
     * 返回结果集
     */
    private String returns;
    /**
     * 测试用例
     */
    private String testCase;
    /**
     * 描述
     */
    private String description;

    @ApiModelProperty(value = "ifttt类型(0:不支持 1：支持if 2:支持then 3:支持if支持then)")
    private Integer iftttType;

    @ApiModelProperty(value = "portal联动设置(0:不支持 1：支持if 2:支持then 3:支持if支持then)")
    private Integer portalIftttType;
}
