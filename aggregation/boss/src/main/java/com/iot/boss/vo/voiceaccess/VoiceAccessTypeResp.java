package com.iot.boss.vo.voiceaccess;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 项目名称：IOT云平台
 * 模块名称：订单管理
 * 功能描述：获取虚拟服务的类型
 * 创建人： wucheng
 * 创建时间：2018-11-15
 */
@ApiModel(value = "虚拟服务类型返回实体类")
@Data
public class VoiceAccessTypeResp implements Serializable{

    @ApiModelProperty(name = "goodsId", value = "商品类型id")
    private Long goodsId;
    @ApiModelProperty(name = "goodsName", value = "商品类型名")
    private String goodsName;

    public VoiceAccessTypeResp(Long goodsId, String goodsName) {
        this.goodsId = goodsId;
        this.goodsName = goodsName;
    }
}
