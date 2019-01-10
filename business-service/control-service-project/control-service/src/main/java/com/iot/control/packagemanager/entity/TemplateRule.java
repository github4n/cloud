package com.iot.control.packagemanager.entity;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 *@description 模板规则实体类
 *@author wucheng
 *@create 2018/11/22 16:36
 */
@TableName("template_rule")
@ApiModel("模板实体类")
@Data
public class TemplateRule extends Model<TemplateRule>{
    @Override
    protected Serializable pkVal() {
        return id;
    }

    @ApiModelProperty(name = "id", value = "主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(name = "packageId", value = "套包id")
    private Long packageId;

    @ApiModelProperty(name = "tenantId", value = "租户id")
    private Long tenantId;

    @ApiModelProperty(name = "type", value = "模板类型 0:安防 1:scene 2:ifttt;3:策略")
    private Integer type;

    @ApiModelProperty(name = "json", value = "规则体")
    private String json;

    @ApiModelProperty(name = "createTime", value = "创建时间")
    private Date createTime;

    @ApiModelProperty(name = "updateTime", value = "更新时间")
    private Date updateTime;

    @ApiModelProperty(name = "ruleName", value = "规则名称")
    private String  ruleName;

}
