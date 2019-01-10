package com.iot.device.vo.req;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * @Author: lucky
 * @Descrpiton:
 * @Date: 14:29 2018/9/7
 * @Modify by:
 */
@Data
@ToString
public class ProductStepVoReq implements Serializable {

    private Long productId;

    private Integer step;

    private Long tenantId;

}
