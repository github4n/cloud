package com.iot.device.model;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import com.iot.device.model.enums.DevelopStatusEnum;
import com.iot.device.model.enums.PropertyStatusEnum;
import com.iot.device.model.enums.ReqParamTypeEnum;
import com.iot.device.model.enums.ReturnTypeEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 模组-方法表
 * </p>
 *
 * @author lucky
 * @since 2018-06-28
 */
@Data
@ToString
@TableName("service_module_action")
public class ServiceModuleAction extends Model<ServiceModuleAction> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /**
     * parent_ID
     */
    @TableField("parent_id")
    private Long parentId;
    /**
     * 租户ID
     */
    @TableField("tenant_id")
    private Long tenantId;
    /**
     * 模组id
     */
    @TableField("service_module_id")
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
    @TableField("api_level")
    private Integer apiLevel;
    /**
     * 开发状态,0:未开发,1:开发中,2:已上线
     */
    @TableField("develop_status")
    private DevelopStatusEnum developStatus;
    /**
     * 属性状态，0:可选,1:必选
     */
    @TableField("property_status")
    private PropertyStatusEnum propertyStatus;

    /**
     * 请求参数格式：0 array ,1 object
     */
    @TableField("req_param_type")
    private ReqParamTypeEnum reqParamType;
    /**
     * 返回参数格式：0 array ,1 object
     */
    @TableField("return_type")
    private ReturnTypeEnum returnType;

    /**
     * json参数集
     */
    private String params;
    /**
     * 返回内容描述
     */
    @TableField("return_desc")
    private String returnDesc;
    /**
     * 返回结果集
     */
    private String returns;
    /**
     * 测试用例
     */
    @TableField("test_case")
    private String testCase;
    /**
     * 创建人
     */
    @TableField("create_by")
    private Long createBy;
    /**
     * 创建时间
     */
    @TableField("create_time")
    private Date createTime;
    /**
     * 修改人
     */
    @TableField("update_by")
    private Long updateBy;
    /**
     * 修改时间
     */
    @TableField("update_time")
    private Date updateTime;
    /**
     * 描述
     */
    private String description;

    @TableField(exist = false)
    @ApiModelProperty(value = "service_to_action 关联表id")
    private Long actionId;

    @TableField(exist = false)
    @ApiModelProperty(value = "service_to_action status")
    private Integer status;

    @TableField("ifttt_type")
    @ApiModelProperty(value = "ifttt类型(0:不支持 1：支持if 2:支持then 3:支持if支持then)")
    private Integer iftttType;

    @TableField("portal_ifttt_type")
    @ApiModelProperty(value = "portal联动设置(0:不支持 1：支持if 2:支持then 3:支持if支持then)")
    private Integer portalIftttType;

    protected Serializable pkVal() {
        return id;
    }
}
