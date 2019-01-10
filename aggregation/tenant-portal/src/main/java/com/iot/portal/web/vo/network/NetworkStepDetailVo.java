package com.iot.portal.web.vo.network;

import com.iot.tenant.vo.resp.network.NetworkHelpFormat;
import com.iot.tenant.vo.resp.network.NetworkStepFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * @despriction：配网步骤详情
 * @author  yeshiyuan
 * @created 2018/11/30 10:33
 */
@ApiModel(value = "配网步骤详情")
public class NetworkStepDetailVo {

    @ApiModelProperty(name = "title", value = "配网方式名称", dataType = "String")
    private String title;

    @ApiModelProperty(name = "mode", value = "配网方式编码", dataType = "String")
    private String mode;

    @ApiModelProperty(name = "steps", value = "配网步骤", dataType = "List")
    private List<NetworkStepFormat> steps;

    @ApiModelProperty(name = "helps", value = "帮助文档", dataType = "List")
    private List<NetworkHelpFormat> helps;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public List<NetworkStepFormat> getSteps() {
        return steps;
    }

    public void setSteps(List<NetworkStepFormat> steps) {
        this.steps = steps;
    }

    public List<NetworkHelpFormat> getHelps() {
        return helps;
    }

    public void setHelps(List<NetworkHelpFormat> helps) {
        this.helps = helps;
    }
}
