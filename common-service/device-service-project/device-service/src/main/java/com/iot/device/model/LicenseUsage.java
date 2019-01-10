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

/**
 * <p>
 * <p>
 * </p>
 *
 * @author chq
 * @since 2018-06-27
 */
@Data
@ToString
@TableName("license_usage")
public class LicenseUsage extends Model<LicenseUsage> {
    /**
     * table_name
     */
    public static final String TABLE_NAME = "license_usage";

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /**
     * 批次号
     */
    @TableField("batch_num")
    private String batchNum;
    /**
     * 设备id
     */
    private String licenseUuid;
    /**
     * mqtt秘钥
     */
    @TableField("secret_key")
    private String secretKey;
    /**
     * 设备的mac
     */
    private String mac;
    /**
     * 产品型号
     */
    @TableField("mode_id")
    private String modeId;
    /**
     * 上传者
     */
    @TableField("create_by")
    private Long createBy;
    /**
     * 生成时间
     */
    @TableField("create_time")
    private Date createTime;

    /**
     * 租户ID
     */
    private Long tenantId;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }


}
