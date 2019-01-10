package com.iot.tenant.vo.req.network;

import com.iot.common.exception.BusinessException;
import com.iot.tenant.entity.NetworkStepKey;
import com.iot.tenant.exception.TenantExceptionEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;
import java.util.TreeSet;

/**
 * 项目名称：cloud
 * 功能描述：保存设备配网步骤入参（BOSS使用）
 * 创建人： yeshiyuan
 * 创建时间：2018/10/8 15:12
 * 修改人： yeshiyuan
 * 修改时间：2018/10/8 15:12
 * 修改描述：
 */
@ApiModel(description = "保存设备配网步骤入参（BOSS使用）")
public class SaveNetworkStepBaseReq {

    @ApiModelProperty(name = "deviceTypeId", value = "设备类型id", dataType = "Long")
    private Long deviceTypeId;


    @ApiModelProperty(name = "userId", value = "用户id", dataType = "Long")
    private Long userId;

    @ApiModelProperty(name = "networkInfos", value = "配网信息", dataType = "List")
    private List<NetworkInfoReq> networkInfos;

    public Long getDeviceTypeId() {
        return deviceTypeId;
    }

    public void setDeviceTypeId(Long deviceTypeId) {
        this.deviceTypeId = deviceTypeId;
    }


    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public List<NetworkInfoReq> getNetworkInfos() {
        return networkInfos;
    }

    public void setNetworkInfos(List<NetworkInfoReq> networkInfos) {
        this.networkInfos = networkInfos;
    }

    public static void checkParam(SaveNetworkStepBaseReq req){
        if (req == null) {
            throw new BusinessException(TenantExceptionEnum.PARAM_IS_NULL);
        }
        if (req.getDeviceTypeId() == null) {
            throw new BusinessException(TenantExceptionEnum.PARAM_ERROR, "device type id is null");
        }
        if (req.getNetworkInfos()==null || req.getNetworkInfos().isEmpty()) {
            throw new BusinessException(TenantExceptionEnum.PARAM_ERROR, "network infos is empty");
        } else {
            for (NetworkInfoReq info : req.getNetworkInfos()) {
                checkNetworkInfoReq(info,"base");
            }
        }
    }

    public static void checkNetworkInfoReq(NetworkInfoReq info, String type) {
        if (info.getNetworkTypeId() == null) {
            throw new BusinessException(TenantExceptionEnum.PARAM_ERROR, "network type is null");
        }
        if (info.getSteps() == null || info.getSteps().isEmpty()) {
            throw new BusinessException(TenantExceptionEnum.PARAM_ERROR, "network steps is empty");
        } else {
            TreeSet<Integer> steps = new TreeSet<>();
            for (NetworkStepReq step: info.getSteps()) {
                if (step.getStep() == null) {
                    throw new BusinessException(TenantExceptionEnum.PARAM_ERROR, "step is null");
                }
                steps.add(step.getStep());
                if (step.getIcon() == null) {
                    throw new BusinessException(TenantExceptionEnum.PARAM_ERROR, "step("+step.getStep() +") icon is empty");
                }
                if (step.getLangInfos() == null || step.getLangInfos().isEmpty()) {
                    throw new BusinessException(TenantExceptionEnum.PARAM_ERROR, "step("+step.getStep() +") langInfos is empty");
                } else {
                    if ("base".equals(type)) {
                        NetworkStepKey.checkLangKey(step.getLangInfos());
                    } else if("tenant".equals(type)){
                        NetworkStepKey.checkTenantLangKey(step.getLangInfos());
                    }
                }
                if (step.getHelp()!=null ){
                    if ("base".equals(type)) {
                        NetworkStepKey.checkLangKey(step.getHelp().getLangInfos());
                    } else if("tenant".equals(type)){
                        NetworkStepKey.checkTenantLangKey(step.getHelp().getLangInfos());
                    }
                }
            }
            //防止跳步骤
            if (steps.first().intValue() != 1){
                throw new BusinessException(TenantExceptionEnum.PARAM_ERROR, "step must start 1");
            }
            if ((steps.last() - steps.first()) != (steps.size()-1)) {
                throw new BusinessException(TenantExceptionEnum.PARAM_ERROR, "Steps must be continuous");
            }
        }
    }

}
