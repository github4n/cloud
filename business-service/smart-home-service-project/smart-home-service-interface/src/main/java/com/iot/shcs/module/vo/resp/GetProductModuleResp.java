package com.iot.shcs.module.vo.resp;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.iot.device.vo.rsp.StyleTemplateResp;
import lombok.Data;
import lombok.ToString;

import java.util.Date;
import java.util.List;

/**
 * @Author: lucky
 * @Descrpiton: 产品对应的功能组信息
 * @Date: 9:44 2018/10/23
 * @Modify by:
 */
@Data
@ToString
public class GetProductModuleResp {
    //产品对应的样式
    StyleTemplateResp style;

    //功能组列表
    private List<GetServiceModuleResp> serviceModules;

    //if 相关事件方法属性
    private List<ListIfInfoResp> ifList;

    //then 相关事件方法属性
    private List<ListThenInfoResp> thenList;

    private Long id;
    /**
     * 设备类型id
     * device_type_id
     */
    private Long deviceTypeId;
    private Long tenantId;
    /**
     * 产品名称
     * product_name
     */
    private String productName;
    /**
     * 通信类型（0 WIFI 1 蓝牙 2 网关 3 IPC）
     * communication_mode
     */
    private Integer communicationMode;
    /**
     * 数据传输方式（定长<所有数据点一并上报>、变长<只上报有变化的数据点数据>）
     * transmission_mode
     */
    private Integer transmissionMode;
    /**
     * create_time
     */
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    /**
     * update_time
     */
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date updateTime;
    /**
     * 产品型号
     */
    private String model;
    /**
     * 产品型号
     */
    private String remark;
    /**
     * 配置网络方式
     * config_net_mode
     */
    private String configNetMode;
    /**
     * 是否套包产品
     */
    private Integer isKit;
    /**
     * 是否直连设备
     */
    private Integer isDirectDevice;
    /**
     * 图标 icon
     */
    private String icon;
    /**
     * 开发状态,0:未开发,1:开发中,2:已上线
     */
    private Integer developStatus;
    /**
     * 企业开发者表关联的id
     */
    private Long enterpriseDevelopId;


}
