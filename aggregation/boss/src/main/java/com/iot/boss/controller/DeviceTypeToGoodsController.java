package com.iot.boss.controller;

import com.iot.boss.vo.devicetypegoods.DeviceTypeGoodsSubResp;
import com.iot.boss.vo.devicetypegoods.DeviceTypeToGoodsResp;
import com.iot.common.annotation.Action;
import com.iot.common.annotation.LoginRequired;
import com.iot.common.beans.CommonResponse;
import com.iot.common.util.CommonUtil;
import com.iot.common.util.StringUtil;
import com.iot.device.api.DeviceTypeToGoodsApi;
import com.iot.device.vo.rsp.ListGoodsSubDictResp;
import com.iot.payment.api.GoodsServiceApi;
import com.iot.payment.entity.goods.GoodsInfo;
import com.iot.payment.enums.goods.GoodsTypeEnum;
import com.iot.system.api.LangApi;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@Api(description = "设备类型商品接口", value = "设备类型商品接口")
@RequestMapping("/api/device/type/goods")
public class DeviceTypeToGoodsController {
    @Autowired
    private DeviceTypeToGoodsApi deviceTypeToGoodsApi;

    @Autowired
    private GoodsServiceApi goodsServiceApi;

    @Autowired
    private LangApi langApi;

    /**
     * 商品描述特殊分隔符
     */
    private static final String DESC_SPLIT_BY = "@@";

    @ApiOperation("根据设备类型获取商品列表")
    @LoginRequired(value = Action.Normal)
    @RequestMapping(value = "getAllDeviceTypeToGoodsListByDeviceTypeId", method = RequestMethod.GET)
    public CommonResponse getAllDeviceTypeToGoodsListByDeviceTypeId(@RequestParam("deviceTypeId") Long deviceTypeId) {
        List<DeviceTypeToGoodsResp> respList = new ArrayList<>();
        /**
         * 获取商品信息
         */
        List<GoodsInfo> goodsList = this.goodsServiceApi.getGoodsInfoByTypeId(GoodsTypeEnum.VoiceService.getCode());
        Set<String> goodsName = goodsList.stream().map(GoodsInfo::getGoodsName).collect(Collectors.toSet());
        Set<String> goodsDesc = goodsList.stream().map(GoodsInfo::getDescription).collect(Collectors.toSet());
        if (!CommonUtil.isEmpty(goodsName) && !CommonUtil.isEmpty(goodsDesc)) {
            goodsName.addAll(goodsDesc);
        }
        Map<String, String> nameMap = this.langApi.getLangValueByKey(goodsName, LocaleContextHolder.getLocale().toString());
        /**
         * 获取设备类型商品配置信息
         */
        List<String> configGoodslist = this.deviceTypeToGoodsApi.getDeviceTypeGoodsCodeByDeviceTypeId(deviceTypeId);
        /**
         * 获取设备类型商品子项配置信息
         */
        Map<String, List<ListGoodsSubDictResp>> goodsSubMap = this.deviceTypeToGoodsApi.getGoodsSubDictMapByDeviceTypeId(deviceTypeId);
        /**
         * 构造返回值
         */
        goodsList.forEach(goodsInfo -> {
            DeviceTypeToGoodsResp goodsResp = new DeviceTypeToGoodsResp();
            goodsResp.setGoodsCode(goodsInfo.getGoodsCode());
            String desc = nameMap.get(goodsInfo.getDescription());
            if (!StringUtil.isEmpty(desc)) {
                String[] array = desc.split(DESC_SPLIT_BY);
                if (array.length == 1) {
                    goodsResp.setDescription(array[0]);
                } else if (array.length == 2) {
                    goodsResp.setDescription(array[0]);
                    goodsResp.setDetailDesc(array[1]);
                }
            }
            goodsResp.setGoodsName(nameMap.get(goodsInfo.getGoodsName()));
            //商品是否配置
            if (configGoodslist.contains(goodsInfo.getGoodsCode())) {
                goodsResp.setCheckFlag(true);
            } else {
                goodsResp.setCheckFlag(false);
            }
            //设置子项
            if (goodsSubMap.get(goodsInfo.getGoodsCode()) != null) {
                List<DeviceTypeGoodsSubResp> subRespList = transGoodsSubDictResp(goodsSubMap.get(goodsInfo.getGoodsCode()));
                goodsResp.setSubList(subRespList);
            }
            respList.add(goodsResp);
        });
        return CommonResponse.success(respList);
    }

    @ApiOperation("获取商品列表")
    @LoginRequired(value = Action.Normal)
    @RequestMapping(value = "getAllDeviceTypeToGoodsList", method = RequestMethod.GET)
    public CommonResponse getAllDeviceTypeToGoodsList() {
        List<DeviceTypeToGoodsResp> respList = new ArrayList<>();
        /**
         * 获取商品信息
         */
        List<GoodsInfo> goodsList = this.goodsServiceApi.getGoodsInfoByTypeId(GoodsTypeEnum.VoiceService.getCode());
        Set<String> goodsName = goodsList.stream().map(GoodsInfo::getGoodsName).collect(Collectors.toSet());
        Set<String> goodsDesc = goodsList.stream().map(GoodsInfo::getDescription).collect(Collectors.toSet());
        if (!CommonUtil.isEmpty(goodsName) && !CommonUtil.isEmpty(goodsDesc)) {
            goodsName.addAll(goodsDesc);
        }
        Map<String, String> nameMap = this.langApi.getLangValueByKey(goodsName, LocaleContextHolder.getLocale().toString());
        //获取服务子项
        Map<String, List<ListGoodsSubDictResp>> goodsSubMap = this.deviceTypeToGoodsApi.getAllGoodsSubDictMap();
        /**
         * 构造返回值
         */
        goodsList.forEach(goodsInfo -> {
            DeviceTypeToGoodsResp goodsResp = new DeviceTypeToGoodsResp();
            goodsResp.setGoodsCode(goodsInfo.getGoodsCode());
            String desc = nameMap.get(goodsInfo.getDescription());
            if (!StringUtil.isEmpty(desc)) {
                String[] array = desc.split(DESC_SPLIT_BY);
                if (array.length == 1) {
                    goodsResp.setDescription(array[0]);
                } else if (array.length == 2) {
                    goodsResp.setDescription(array[0]);
                    goodsResp.setDetailDesc(array[1]);
                }
            }
            goodsResp.setGoodsName(nameMap.get(goodsInfo.getGoodsName()));
            //设置子项
            if (goodsSubMap.get(goodsInfo.getGoodsCode()) != null) {
                List<DeviceTypeGoodsSubResp> subRespList = transGoodsSubDictResp(goodsSubMap.get(goodsInfo.getGoodsCode()));
                goodsResp.setSubList(subRespList);
            }
            respList.add(goodsResp);
        });
        return CommonResponse.success(respList);
    }

    private List<DeviceTypeGoodsSubResp> transGoodsSubDictResp(List<ListGoodsSubDictResp> source) {
        List<DeviceTypeGoodsSubResp> result = new ArrayList<>();
        result = source.stream().map(sub -> {
            DeviceTypeGoodsSubResp subResp = new DeviceTypeGoodsSubResp();
            subResp.setCode(sub.getCode());
            subResp.setName(sub.getName());
            subResp.setCheckFlag(sub.getCheckFlag());
            return subResp;
        }).collect(Collectors.toList());
        return result;
    }
}
