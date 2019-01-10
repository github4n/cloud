package com.iot.device.vo.req;

import lombok.*;

import java.io.Serializable;
import java.util.List;

/**
 * @Author: xfz
 * @Descrpiton:
 * @Date: 10:27 2018/5/11
 * @Modify by:
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@ToString
public class DevTypePageReq implements Serializable {

    private int pageNum = 1;

    private int pageSize = 20;

    private Long tenantId;

    private List<String> deviceIds;

    private List<String> deviceTypeList;

    private Long orgId;

}
