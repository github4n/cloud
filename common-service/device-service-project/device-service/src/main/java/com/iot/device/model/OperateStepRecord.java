package com.iot.device.model;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

/**
 * 项目名称：cloud
 * 功能描述：操作步骤记录实体类
 * 创建人： yeshiyuan
 * 创建时间：2018/9/11 15:04
 * 修改人： yeshiyuan
 * 修改时间：2018/9/11 15:04
 * 修改描述：
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@TableName("operate_step_record")
public class OperateStepRecord extends Model<OperateStepRecord> {

    /**
     * 主键id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 租户id
     */
    @TableField("tenant_id")
    private Long tenantId;

    /**
     * 操作id
     */
    @TableField("operate_id")
    private Long operateId;

    /**
     * 操作类型（product：产品；application：应用）
     */
    @TableField("operate_type")
    private String operateType;

    /**
     * 步骤下标
     */
    @TableField("step_index")
    private Integer stepIndex;

     /**
     * 创建时间
     */
    @TableField("create_time")
    private Date createTime;

    @TableField("create_by")
    private Long createBy;

    /**
     * 修改时间
     */
    @TableField("update_time")
    private Date updateTime;

    @TableField("update_by")
    private Long updateBy;

    protected Serializable pkVal() {
        return this.id;
    }
}
