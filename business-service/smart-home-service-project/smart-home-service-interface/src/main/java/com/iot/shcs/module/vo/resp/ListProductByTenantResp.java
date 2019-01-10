package com.iot.shcs.module.vo.resp;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * @Author: lucky
 * @Descrpiton:
 * @Date: 15:17 2018/11/12
 * @Modify by:
 */
@Data
@ToString
public class ListProductByTenantResp implements Serializable {

    private Long productId;
}
