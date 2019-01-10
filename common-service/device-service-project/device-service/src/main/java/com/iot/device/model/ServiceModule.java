package com.iot.device.model;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import com.iot.device.model.enums.DevelopStatusEnum;
import com.iot.device.model.enums.PropertyStatusEnum;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 模组表
 * </p>
 *
 * @author lucky
 * @since 2018-06-28
 */
@Data
@ToString
@TableName("service_module")
public class ServiceModule extends Model<ServiceModule> {

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
     * 版本
     */
    private String version;
    /**
     * 模组名称
     */
    private String name;
    /**
     * 模组唯一标识code
     */
    private String code;
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

    /**
     * icon图片
     */
    private String img;

    @TableField("change_img")
    private String changeImg;

    @TableField(exist = false)
    private Long otherId;

    protected Serializable pkVal() {
        return this.id;
    }

    @TableField("component_type")
    private String componentType;

    @TableField(exist = false)
    private Integer status;

}
