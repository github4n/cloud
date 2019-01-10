package com.iot.device.vo.req.device;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @Author: lucky
 * @Descrpiton:
 * @Date: 15:06 2018/10/10
 * @Modify by:
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@ToString
public class ListProductInfoReq {

    @NotNull(message = "product.id.notnull")
    private List<Long> productIds;
}
