package com.iot.device.vo.rsp;

import com.iot.device.vo.rsp.product.ParentVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * @Author: xfz
 * @Descrpiton:
 * @Date: 15:35 2018/6/29
 * @Modify by:
 */
@ApiModel("功能模组基础信息")
public class ServiceModuleListResp implements Serializable {
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
     * 版本
     */
    @ApiModelProperty(value = "版本")
    private String version;
    /**
     * 模组名称
     */
    @ApiModelProperty(value = "模组名称")
    private String name;
    /**
     * 模组唯一标识code
     */
    @ApiModelProperty(value = "模组唯一标识code")
    private String code;
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
     * 测试用例
     */
    @ApiModelProperty(value = "测试用例")
    private String testCase;
    /**
     * 描述
     */
    @ApiModelProperty(value = "描述")
    private String description;

    /**
     * icon图片
     */
    private String img;


    /**
     *
     */
    private String changeImg;


    private Integer status;

    @ApiModelProperty(value = "功能组控件类型")
    private String componentType;

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

    public ParentVO getParent() {
        return parent;
    }

    public void setParent(ParentVO parent) {
        this.parent = parent;
    }

    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
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

    public Integer getDevelopStatus() {
        return developStatus;
    }

    public void setDevelopStatus(Integer developStatus) {
        this.developStatus = developStatus;
    }

    public Integer getPropertyStatus() {
        return propertyStatus;
    }

    public void setPropertyStatus(Integer propertyStatus) {
        this.propertyStatus = propertyStatus;
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

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getChangeImg() {
        return changeImg;
    }

    public void setChangeImg(String changeImg) {
        this.changeImg = changeImg;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getComponentType() {
        return componentType;
    }

    public void setComponentType(String componentType) {
        this.componentType = componentType;
    }
}
