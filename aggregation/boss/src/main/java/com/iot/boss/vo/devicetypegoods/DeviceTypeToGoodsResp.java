package com.iot.boss.vo.devicetypegoods;

import lombok.Data;

import java.util.List;

/**
 * @author wangxi
 * @Description: 设备类型服务返回
 * @date 2018/12/2614:46
 */
@Data
public class DeviceTypeToGoodsResp {
    private String goodsCode;
    private String goodsName;
    private Boolean checkFlag;
    private String description;
    private String detailDesc;
    private List<DeviceTypeGoodsSubResp> subList;
}
