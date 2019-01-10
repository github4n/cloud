package com.iot.device.vo.rsp;

import lombok.Data;

/**
 * @author wangxi
 * @Description: 服务子项返回
 * @date 2018/12/2714:59
 */
@Data
public class ListGoodsSubDictResp {
    private String code;
    private String name;
    private String goodsCode;
    private Boolean checkFlag;
}
