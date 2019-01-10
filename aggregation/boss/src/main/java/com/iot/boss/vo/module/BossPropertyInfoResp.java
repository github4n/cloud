package com.iot.boss.vo.module;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * @Author: xfz
 * @Descrpiton:
 * @Date: 18:05 2018/7/3
 * @Modify by:
 */
@ApiModel("功能模组基础属性信息")
@Data
@ToString
public class BossPropertyInfoResp implements Serializable {


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
     * 属性名称
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
     * api等级
     */
    private Integer apiLevel;
    /**
     * 开发状态,0:未开发,1:开发中,2:已上线
     */
    private Integer developStatus;
    /**
     * 请求参数格式：0 array ,1 object
     */
    private Integer reqParamType;
    /**
     * 返回参数格式：0 array ,1 object
     */
    private Integer returnType;
    /**
     * 属性状态，0:可选,1:必选
     */
    private Integer propertyStatus;
    /**
     * 读写特征 0:可读可写,1:不可读不可写,2:可读不可写
     */
    private Integer rwStatus;
    /**
     * 参数类型，0:int,1:float,2:bool,3:enum,4:string
     */
    private Integer paramType;
    /**
     * 最小值
     */
    private String minValue;
    /**
     * 最大值
     */
    private String maxValue;
    /**
     * 允许值(enum 可以多个逗号隔开)
     */
    private String allowedValues;
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

    @ApiModelProperty(value = "property类型(0:property类型 1：参数类型)")
    private Integer propertyType;

    @ApiModelProperty(value = "inHomePage类型(0:默认不在首页 1：在首页)")
    private Integer inHomePage;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    public Long getServiceModuleId() {
        return serviceModuleId;
    }

    public void setServiceModuleId(Long serviceModuleId) {
        this.serviceModuleId = serviceModuleId;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public Integer getApiLevel() {
        return apiLevel;
    }

    public void setApiLevel(Integer apiLevel) {
        this.apiLevel = apiLevel;
    }

    public Integer getDevelopStatus() {
        return developStatus;
    }

    public void setDevelopStatus(Integer developStatus) {
        this.developStatus = developStatus;
    }

    public Integer getReqParamType() {
        return reqParamType;
    }

    public void setReqParamType(Integer reqParamType) {
        this.reqParamType = reqParamType;
    }

    public Integer getReturnType() {
        return returnType;
    }

    public void setReturnType(Integer returnType) {
        this.returnType = returnType;
    }

    public Integer getPropertyStatus() {
        return propertyStatus;
    }

    public void setPropertyStatus(Integer propertyStatus) {
        this.propertyStatus = propertyStatus;
    }

    public Integer getRwStatus() {
        return rwStatus;
    }

    public void setRwStatus(Integer rwStatus) {
        this.rwStatus = rwStatus;
    }

    public Integer getParamType() {
        return paramType;
    }

    public void setParamType(Integer paramType) {
        this.paramType = paramType;
    }

    public String getMinValue() {
        return minValue;
    }

    public void setMinValue(String minValue) {
        this.minValue = minValue;
    }

    public String getMaxValue() {
        return maxValue;
    }

    public void setMaxValue(String maxValue) {
        this.maxValue = maxValue;
    }

    public String getAllowedValues() {
        return allowedValues;
    }

    public void setAllowedValues(String allowedValues) {
        this.allowedValues = allowedValues;
    }

    public String getTestCase() {
        return testCase;
    }

    public void setTestCase(String testCase) {
        this.testCase = testCase;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
