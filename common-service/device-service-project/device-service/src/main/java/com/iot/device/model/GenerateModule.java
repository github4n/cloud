package com.iot.device.model;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

@Data
@ToString
@TableName("generate_module")
public class GenerateModule extends Model<GenerateModule> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private String customer;

    @TableField("device_type_name")
    private String deviceTypeName;

    private String agreement;

    @TableField("g_f")
    private String gF;

    @TableField("device_type_info")
    private String deviceTypeInfo;

    @TableField("g_n")
    private String gN;

    private String code;

    private Long number;

    @TableField("code_number")
    private String codeNumber;

    @TableField("create_by")
    private Long createBy;

    @TableField("create_time")
    private Date createTime;

    @TableField("update_by")
    private Long updateBy;

    @TableField("update_time")
    private Date updateTime;

    private String describes;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
