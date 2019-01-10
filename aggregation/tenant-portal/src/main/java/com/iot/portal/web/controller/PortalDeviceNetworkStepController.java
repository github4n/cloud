package com.iot.portal.web.controller;

import com.iot.common.annotation.Action;
import com.iot.common.annotation.LoginRequired;
import com.iot.common.beans.BeanUtil;
import com.iot.common.beans.CommonResponse;
import com.iot.common.exception.BusinessException;
import com.iot.common.exception.ResultMsg;
import com.iot.common.util.StringUtil;
import com.iot.device.api.NetworkTypeApi;
import com.iot.device.api.ProductApi;
import com.iot.device.vo.rsp.NetworkTypeResp;
import com.iot.device.vo.rsp.ProductResp;
import com.iot.file.api.FileApi;
import com.iot.file.dto.FileDto;
import com.iot.portal.exception.BusinessExceptionEnum;
import com.iot.portal.web.vo.req.PortalSaveNetworkStepReq;
import com.iot.saas.SaaSContextHolder;
import com.iot.tenant.api.AppApi;
import com.iot.tenant.api.DeviceNetworkStepTenantApi;
import com.iot.tenant.vo.req.network.NetworkStepHelpReq;
import com.iot.tenant.vo.req.network.SaveNetworkStepTenantReq;
import com.iot.tenant.vo.resp.AppInfoResp;
import com.iot.tenant.vo.resp.network.DeviceNetworkStepTenantResp;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 项目名称：cloud
 * 功能描述：app应用下设备配网步骤管理（portal管理）
 * 创建人： yeshiyuan
 * 创建时间：2018/10/9 17:00
 * 修改人： yeshiyuan
 * 修改时间：2018/10/9 17:00
 * 修改描述：
 */
@Api(tags = "app应用下设备配网步骤管理（portal管理）")
@RestController
@RequestMapping(value = "/portal/deviceNetworkStep")
public class PortalDeviceNetworkStepController {

    @Autowired
    private FileApi fileApi;

    @Autowired
    private DeviceNetworkStepTenantApi deviceNetworkStepTenantApi;

    @Autowired
    private AppApi appApi;

    @Autowired
    private ProductApi productApi;

    @Autowired
    private NetworkTypeApi networkTypeApi;

    private final static String PRE = "default_";

    /**
      * @despriction：查询配网步骤详情
      * @author  yeshiyuan
      * @created 2018/10/9 17:07
      * @return
      */
    @LoginRequired(Action.Normal)
    @ApiOperation(value = "查询配网步骤详情", notes = "查询配网步骤详情")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "appId", value = "appId", dataType = "Long", paramType = "query"),
            @ApiImplicitParam(name = "productId", value = "产品id", dataType = "Long", paramType = "query")
    })
    @RequestMapping(value = "/queryDetail", method = RequestMethod.GET)
    public CommonResponse queryDetail(@RequestParam("appId") Long appId, @RequestParam("productId") Long productId){
        DeviceNetworkStepTenantResp resp = deviceNetworkStepTenantApi.queryNetworkStep(SaaSContextHolder.currentTenantId(), appId, productId);
        List<Long> networkTypeIds = new ArrayList<>();
        if (resp.getNetworkInfos()!=null) {
            resp.getNetworkInfos().forEach(networkInfoResp -> {
                networkTypeIds.add(networkInfoResp.getNetworkTypeId());
                networkInfoResp.getSteps().forEach(networkStepResp -> {
                    String icon = networkStepResp.getIcon();
                    if (!StringUtil.isBlank(icon)) {
                        //默认图片是会带default_下标
                        icon = icon.replace(PRE,"");
                        FileDto fileDto = fileApi.getGetUrl(icon);
                        if (fileDto!=null) {
                            networkStepResp.setIconUrl(fileDto.getPresignedUrl());
                        }
                    }
                    NetworkStepHelpReq helpResp = networkStepResp.getHelp();
                    if (helpResp != null && !StringUtil.isBlank(helpResp.getIcon())) {
                        FileDto fileDto = fileApi.getGetUrl(helpResp.getIcon().replace(PRE,""));
                        if (fileDto!=null) {
                            helpResp.setIconUrl(fileDto.getPresignedUrl());
                        }
                    }
                });
            });
            Map<Long, NetworkTypeResp> map = networkTypeApi.getNetworkTypes(networkTypeIds);
            resp.getNetworkInfos().forEach(networkInfoResp -> {
                networkInfoResp.setNetworkType(map.get(networkInfoResp.getNetworkTypeId()).getNetworkName());
            });
        }
        return ResultMsg.SUCCESS.info(resp);
    }

    /**
      * @despriction：保存配网步骤详情
      * @author  yeshiyuan
      * @created 2018/10/9 17:24
      * @return
      */
    @LoginRequired(Action.Normal)
    @ApiOperation(value = "保存配网步骤详情", notes = "保存配网步骤详情")
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public CommonResponse save(@RequestBody PortalSaveNetworkStepReq req) {
        //校验app是否属于当前租户
        AppInfoResp appInfoResp = appApi.getAppById(req.getAppId());
        if (appInfoResp==null) {
            throw new BusinessException(BusinessExceptionEnum.PARAM_IS_ERROR, "The APP ID does not exist.");
        }
        //校验product是否属于当前租户
        ProductResp resp = productApi.getProductById(req.getProductId());
        if (resp==null) {
            throw new BusinessException(BusinessExceptionEnum.PARAM_IS_ERROR, "The product does not exist.");
        }
        if (!appInfoResp.getTenantId().equals(SaaSContextHolder.currentTenantId())) {
            throw new BusinessException(BusinessExceptionEnum.PARAM_IS_ERROR, "The object doesn't belong to you.");
        }
        if (req.getOldFileList()!= null) {
            List<String> delFileIds = new ArrayList<>();
            req.getOldFileList().forEach(o -> {
                if (!o.contains(PRE)) { //默认图片不删除
                    delFileIds.add(o);
                }
            });
            if (!delFileIds.isEmpty()) {
                fileApi.deleteObject(delFileIds);
            }
        }
        if (req.getNewFileList() != null && !req.getNewFileList().isEmpty()) {
            fileApi.saveFileInfosToDb(req.getNewFileList());
        }

        SaveNetworkStepTenantReq saveNetworkStepTenantReq = new SaveNetworkStepTenantReq();
        BeanUtil.copyProperties(req,saveNetworkStepTenantReq);
        saveNetworkStepTenantReq.setTenantId(SaaSContextHolder.currentTenantId());
        saveNetworkStepTenantReq.setUserId(SaaSContextHolder.getCurrentUserId());
        deviceNetworkStepTenantApi.save(saveNetworkStepTenantReq);
        return ResultMsg.SUCCESS.info();
    }

}
