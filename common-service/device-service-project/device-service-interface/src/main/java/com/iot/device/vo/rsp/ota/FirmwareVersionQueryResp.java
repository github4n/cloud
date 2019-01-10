package com.iot.device.vo.rsp.ota;

import lombok.Data;
import lombok.ToString;

import java.util.Date;

/**
 * 项目名称：立达信IOT云平台
 * 模块名称：ota
 * 功能描述：ota
 * 创建人： nongchongwei
 * 创建时间：2018年07月24日 16:58
 * 修改人： nongchongwei
 * 修改时间：2018年07月24日 16:58
 */
@Data
@ToString
public class FirmwareVersionQueryResp {
    /**
     * 主键id 主键主动增长
     */
    private Long id;

    /**
     * 租户id
     */
    private Long tenantId;
    /**
     * 产品id
     */
    private Long productId;

    /**
     * 版本号
     */
    private String otaVersion;

    /**
     * 整包升级WholePackage  差分升级Difference
     */
    private String otaType;

    /**
     * 升级文件id
     */
    private String otaFileId;

    /**
     * MD5值
     */
    private String otaMd5;

    /**
     * 分位的类型 0:所有的模块在一个分位里面(默认) 1:wifi 模块的分位 2: zigbee模块的分位 3: z-wave模块的分位 4：ble模块的分位 
     */
    private Integer fwType;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 版本上线时间
     */
    private Date versionOnlineTime;

    /**
     * 创建人
     */
    private Long createBy;

    /**
     * 修改时间
     */
    private Date updateTime;

    /**
     * 修改人
     */
    private Long updateBy;

}