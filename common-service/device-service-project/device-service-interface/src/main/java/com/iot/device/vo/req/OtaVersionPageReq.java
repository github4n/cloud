package com.iot.device.vo.req;

import java.io.Serializable;

/**
 * @Author: xfz
 * @Descrpiton:
 * @Date: 14:23 2018/5/2
 * @Modify by:
 */
public class OtaVersionPageReq implements Serializable {
    private Integer pageNum = 1;

    private Integer pageSize = 10;

    /**
     * 分位的类型  固件类型
     * fw_type
     * 0:所有的模块在一个分位里面  1:wifi 模块的分位  2: zigbee模块的分位 3: z-wave模块的分位 4：ble模块的分位 不填默认值为0.
     */
    private Integer fwType;

    private Long productId;

    /**
     * 设备版本号
     * version_num
     */
    private String versionNum;

    public Integer getPageNum() {
        return pageNum;
    }

    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getFwType() {
        return fwType;
    }

    public void setFwType(Integer fwType) {
        this.fwType = fwType;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getVersionNum() {
        return versionNum;
    }

    public void setVersionNum(String versionNum) {
        this.versionNum = versionNum;
    }
}
