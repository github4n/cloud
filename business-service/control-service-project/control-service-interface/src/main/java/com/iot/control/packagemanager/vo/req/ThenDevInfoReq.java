package com.iot.control.packagemanager.vo.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
  * @despriction：then中设备信息
  * @author  yeshiyuan
  * @created 2018/11/23 10:39
  */
@ApiModel(description = "then中设备类型/产品信息")
@Data
public class ThenDevInfoReq {


    @ApiModelProperty(name = "id", value = "设备类型id或产品id", dataType = "Long" )
    private Long id;
    @ApiModelProperty(name = "idx", value = "执行顺序", dataType = "Long" )
    private Long idx;
    @ApiModelProperty(name = "attrInfo", value = "then参数信息", dataType = "List" )
    private List<ThenAttrInfoReq> attrInfo;

    public ThenDevInfoReq() {
    }

    public ThenDevInfoReq(Long id, Long idx) {
        this.id = id;
        this.idx = idx;
    }
}
