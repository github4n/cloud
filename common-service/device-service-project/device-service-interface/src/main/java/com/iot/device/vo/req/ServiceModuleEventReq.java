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
 * @Date: 17:37 2018/6/29
 * @Modify by:
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
public class ServiceModuleEventReq extends SearchParam implements Serializable {
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
     * 事件名称
     */
    private String name;
    /**
     * 唯一标识code
     */
    private String code;
    /**
     * api等级
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
     * json参数集
     */
    private String params;
    /**
     * 标签
     */
    private String tags;
    /**
     * 测试用例
     */
    private String testCase;

    private Long createBy;

    private Date createTime;

    private Long updateBy;

    private Date updateTime;

    /**
     * 描述
     */
    private String description;

    @ApiModelProperty(value = "ifttt类型(0:不支持 1：支持if 2:支持then 3:支持if支持then)")
    private Integer iftttType;

    private String searchValues;

}
