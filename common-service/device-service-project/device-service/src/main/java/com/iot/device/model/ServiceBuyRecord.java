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
 * 项目名称：cloud
 * 功能描述：虚拟服务购买记录
 * 创建人： yeshiyuan
 * 创建时间：2018/9/13 15:07
 * 修改人： yeshiyuan
 * 修改时间：2018/9/13 15:07
 * 修改描述：
 */
@Data
@ToString
@TableName("service_buy_record")
public class ServiceBuyRecord extends Model<ServiceBuyRecord>{

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
     * 用户id
     */
    @TableField("user_id")
    private Long userId;

    /**
     * 订单id，对应order_record表主键
     */
    @TableField("order_id")
    private String orderId;

    /**
     * 虚拟服务id（产品Id，appId）
     */
    @TableField("service_id")
    private Long serviceId;

    /**
     * 商品id，对应goods_info主键
     */
    @TableField("goods_id")
    private Long goodsId;

    /**
     * 商品类别id，字典表：type_id=4
     */
    @TableField("goods_type_id")
    private Integer goodsTypeId;

    /**
     * 购买数量
     */
    @TableField("goods_num")
    private Integer goodsNum;

    /**
     * 支付状态（1:待付款；2：已付款）
     */
    @TableField("pay_status")
    private Integer payStatus;

    /**
     * 附加需求描述
     */
    @TableField("add_demand_desc")
    private String addDemandDesc;

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

    @Override
    protected Serializable pkVal() {
        return id;
    }

}
