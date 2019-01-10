package com.iot.device.vo.req;

import lombok.Data;

@Data
public class SaveDeviceTypeToGoodsReq {
    private String goodsCode;
    private String subCode;
}
