package com.iot.device.model;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.enums.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableId;
import java.io.Serializable;

/**
 * <p>
 * 商品smart对照表
 * </p>
 *
 * @author wangxi
 * @since 2018-12-25
 */
public class GoodsSmart implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /**
     * 商品编码
     */
    @TableField("goods_code")
    private String goodsCode;
    /**
     * smart
     */
    @TableField("smart")
    private Integer smart;
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


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getGoodsCode() {
        return goodsCode;
    }

    public void setGoodsCode(String goodsCode) {
        this.goodsCode = goodsCode;
    }

    public Integer getSmart() {
        return smart;
    }

    public void setSmart(Integer smart) {
        this.smart = smart;
    }

    public Long getCreateBy() {
        return createBy;
    }

    public void setCreateBy(Long createBy) {
        this.createBy = createBy;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "GoodsSmart{" +
        ", id=" + id +
        ", goodsCode=" + goodsCode +
        ", smart=" + smart +
        ", createBy=" + createBy +
        ", createTime=" + createTime +
        "}";
    }
}
