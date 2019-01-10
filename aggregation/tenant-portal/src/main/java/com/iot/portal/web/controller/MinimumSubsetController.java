package com.iot.portal.web.controller;

import com.iot.common.annotation.Action;
import com.iot.common.annotation.LoginRequired;
import com.iot.common.beans.CommonResponse;
import com.iot.device.api.DeviceTypeApi;
import com.iot.device.api.GatewaySubDevRelationApi;
import com.iot.device.api.ProductApi;
import com.iot.device.vo.req.gatewaysubdev.GatewaySubDevRelationReq;
import com.iot.device.vo.rsp.DeviceTypeResp;
import com.iot.device.vo.rsp.gatewaysubdev.GatewaySubDevRelationResp;
import com.iot.device.vo.rsp.product.ProductMinimumSubsetResp;
import com.iot.file.api.FileApi;
import com.iot.payment.api.GoodsServiceApi;
import com.iot.payment.entity.goods.GoodsInfo;
import com.iot.payment.enums.goods.GoodsCoodEnum;
import com.iot.portal.service.FileService;
import com.iot.portal.web.vo.FileResp;
import com.iot.saas.SaaSContextHolder;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import jodd.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

/**
 * 项目名称：IOT云平台
 * 模块名称：最小功能子集业务
 * 功能描述：最小功能-网关子设备
 * 创建人： wucheng
 * 创建时间：2018-11-8 11:48:09
 */
@Api(value = "portal-最小功能子集接口", description = "portal-最小功能子集接口")
@RestController
@RequestMapping("/portal/subset")
public class MinimumSubsetController {
    @Autowired
    private GoodsServiceApi goodsServiceApi;
    @Autowired
    private ProductApi productApi;
    @Autowired
    private GatewaySubDevRelationApi gatewaySubDevRelationApi;
    @Autowired
    private FileApi fileApi;
    @Autowired
    private DeviceTypeApi deviceTypeApi;

    @LoginRequired(Action.Normal)
    @ApiOperation("查询boos租户与当前租户网关技术方案可以关联的子设备")
    @ApiImplicitParam(name ="parDevId", value = "网关产品id", dataType = "Long", paramType = "query")
    @RequestMapping(value = "/getSubFunSet", method = RequestMethod.GET)
    public CommonResponse getSubFunSet(@RequestParam("parDevId") Long parDevId) {
        // 获取产品CommunicationMode
        GoodsInfo goodsInfo = goodsServiceApi.getGoodsInfoByGoodsCode(GoodsCoodEnum.C0003.getCode());
        Long communicationMode = goodsInfo.getId();
        // 获取当前登录的租户id
        Long tenantId = SaaSContextHolder.currentTenantId();

        List<ProductMinimumSubsetResp> resultList = new ArrayList<>();

        // 获取网关子设备自定义设备
        List<ProductMinimumSubsetResp> customSubDevList =  productApi.getProductListByTenantIdAndCommunicationMode(tenantId, communicationMode);
        // 获取网关子设备默认设备
        List<ProductMinimumSubsetResp> defaultSubDevList =  productApi.getProductListByTenantIdAndCommunicationMode(new Long(-1), communicationMode);
        // 已添加的网关设备
        resultList.addAll(customSubDevList);
        resultList.addAll(defaultSubDevList);
        // 查询该网关产品id，是否已保存关联子设备
        List<GatewaySubDevRelationResp> saveSubDevList = gatewaySubDevRelationApi.getGatewaySubDevByParDevId(parDevId, tenantId);
        // 获取设备图片
        resultList.forEach(m-> {
            if (StringUtil.isNotBlank(m.getIcon())) {
                String url = this.fileApi.getGetUrl(m.getIcon()).getPresignedUrl();
                m.setIcon(url);
            } else if (m.getDeviceTypeId() != null){
                DeviceTypeResp deviceTypeResp = deviceTypeApi.getDeviceTypeById(m.getDeviceTypeId());
                if (deviceTypeResp != null) {
                    if (StringUtil.isNotBlank(deviceTypeResp.getImg())) {
                        String url = this.fileApi.getGetUrl(deviceTypeResp.getImg()).getPresignedUrl();
                        m.setIcon(url);
                    }
                }
            }
        });

        if (saveSubDevList == null || saveSubDevList.size() == 0) {
            return CommonResponse.success(resultList);
        } else { //存在已保存的子设备，设置其状态为选中状态
            for (ProductMinimumSubsetResp d: resultList) {
                for (GatewaySubDevRelationResp s : saveSubDevList) {
                    if (d.getId().equals(s.getSubDevId())) {
                        d.setIsSelected(1);
                        break;
                    }
                }
            }
        }
        return CommonResponse.success(resultList);
    }

    @LoginRequired(Action.Normal)
    @ApiOperation("保存网关关联的子设备")
    @ApiImplicitParams({
            @ApiImplicitParam(name ="parDevId", value = "网关产品id", dataType = "Long", paramType = "query"),
            @ApiImplicitParam(name ="defaultSubDevIds", value = "默认产品id集合，以英文逗号隔开", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name ="customSubDevIds",value = "自定义产品集合，以英文逗号隔开", dataType = "String", paramType = "query")
    })
    @RequestMapping(value = "/saveRelation", method = RequestMethod.POST)
    public CommonResponse saveRelation(@RequestParam("parDevId") Long parDevId,@RequestParam("defaultSubDevIds") String defaultSubDevIds, @RequestParam("customSubDevIds") String customSubDevIds) {
        List<GatewaySubDevRelationReq> gatewaySubDevRelationReq = new ArrayList<>();
        Long tenantId = SaaSContextHolder.currentTenantId();
        Long userId = SaaSContextHolder.getCurrentUserId();
        // 查询之前有没有保存关联子设备
        List<GatewaySubDevRelationResp> subDevList = gatewaySubDevRelationApi.getGatewaySubDevByParDevId(parDevId, tenantId);
        Long[] ids = new Long[subDevList.size()];
        if (subDevList !=null && subDevList.size() > 0) {
            for (int i = 0; i < subDevList.size();i++) {
                ids[i] = subDevList.get(i).getId();
            }
        }
        // 存在子节点，删除点之前的保存的内容
        if(ids != null && ids.length > 0) {
            gatewaySubDevRelationApi.deleteById(Arrays.asList(ids));
        }
        // 默认网关子设备
        if (defaultSubDevIds != null && defaultSubDevIds != "") {
            String[] defaultSubDevIdsArray = defaultSubDevIds.split(",");
            for (String r : defaultSubDevIdsArray) {
                Long subDevId = Long.parseLong(r);
                GatewaySubDevRelationReq defaultSubDev = new GatewaySubDevRelationReq(tenantId, parDevId, subDevId, userId,new Date(), userId, new Date(),"valid");
                gatewaySubDevRelationReq.add(defaultSubDev);
            }
        }
        // 自定义网关子设备
        if (customSubDevIds != null && customSubDevIds != "") {
            String[] customSubDevIdsArray = customSubDevIds.split(",");
            for (String r : customSubDevIdsArray) {
                Long subDevId = Long.parseLong(r);
                GatewaySubDevRelationReq defaultSubDev = new GatewaySubDevRelationReq(tenantId, parDevId, subDevId, userId,new Date(), userId, new Date(),"valid");
                gatewaySubDevRelationReq.add(defaultSubDev);
            }
        }
        int result = 0;
        // 保存网关子设备
        if (gatewaySubDevRelationReq != null && gatewaySubDevRelationReq.size() > 0) {
            result = gatewaySubDevRelationApi.batchInsert(gatewaySubDevRelationReq);
        }
        return CommonResponse.success(result);
    }
}
