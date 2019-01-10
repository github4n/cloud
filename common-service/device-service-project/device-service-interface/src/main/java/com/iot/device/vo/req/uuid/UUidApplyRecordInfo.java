package com.iot.device.vo.req.uuid;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

import java.util.Date;

/**
 * 项目名称：cloud
 * 模块名称：设备
 * 功能描述：UUID申请记录
 * 创建人： 490485964@qq.com
 * 创建时间：2018年06月28日 16:58
 * 修改人： 490485964@qq.com
 * 修改时间：2018年06月28日 16:58
 */
@Data
@ToString
public class UUidApplyRecordInfo {
    /**
     * 批次号,主键自动增长
     */
    private Long id;

    @ApiModelProperty(name = "userId", value = "用户id", dataType = "long")
    private Long userId;

    /**
     * 租户id
     */
    private Long tenantId;

    /**订单id*/
    private String orderId;

    /**
     * 下载次数
     */
    private Integer downNum;

    /**
     * 生成数量
     */
    private Integer createNum;

    /** 商品id，对应goods_info主键（主要用于做方案条件搜索） */
    private Long goodsId;

    /** uuid申请状态（1:处理中;2:已完成;3:生成失败;4: P2PID不足）*/
    private Integer uuidApplyStatus;

    /** 支付状态（1:待付款；2：已付款）*/
    private Integer payStatus;

    /**
     * UUID列表zip文件ID
     */
    private String fileId;

    /**
     * 产品id
     */
    private Long productId;



    /**
     * 有效时长，单位：年
     */
    private Integer uuidValidityYear;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 修改时间
     */
    private Date updateTime;


}