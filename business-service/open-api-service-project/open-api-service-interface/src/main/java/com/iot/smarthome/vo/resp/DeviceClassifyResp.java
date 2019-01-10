package com.iot.smarthome.vo.resp;

import lombok.Data;
import lombok.ToString;

import java.util.Date;

/**
 * @Descrpiton:
 * @Author: yuChangXing
 * @Date: 2018/12/14 14:48
 * @Modify by:
 */

@Data
@ToString
public class DeviceClassifyResp {
    private Long id;
    /**
     * 分类名称
     */
    private String typeName;
    /**
     * 分类编码
     */
    private String typeCode;
    /**
     * 最小属性集的产品id
     */
    private Long productId;

    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 创建者
     */
    private Long createBy;
    /**
     * 更新时间
     */
    private Date updateTime;
    /**
     * 更新者
     */
    private Long updateBy;
}
