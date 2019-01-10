package com.iot.device.vo.req;

import com.iot.common.beans.SearchParam;
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
 * @Date: 15:41 2018/6/29
 * @Modify by:
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
public class ServiceModuleActionReq extends SearchParam implements Serializable {

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
     * 创建人
     */
    private Long createBy;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 修改人
     */
    private Long updateBy;

    /**
     * 修改时间
     */
    private Date updateTime;

    /**
     * 描述
     */
    private String description;

    @ApiModelProperty(value = "ifttt类型(0:不支持 1：支持if 2:支持then 3:支持if支持then)")
    private Integer iftttType;

    private String searchValues;
}
