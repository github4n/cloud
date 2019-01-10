package com.iot.smarthome.model;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.enums.IdType;

import java.io.Serializable;

/**
 * @Descrpiton:
 * @Author: yuChangXing
 * @Date: 2018/12/13 8:53
 * @Modify by:
 */
public class DeviceClassifyProductXref implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 设备分类表id
     */
    private Long deviceClassifyId;
    /**
     * 产品id
     */
    private Long productId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getDeviceClassifyId() {
        return deviceClassifyId;
    }

    public void setDeviceClassifyId(Long deviceClassifyId) {
        this.deviceClassifyId = deviceClassifyId;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }
}
