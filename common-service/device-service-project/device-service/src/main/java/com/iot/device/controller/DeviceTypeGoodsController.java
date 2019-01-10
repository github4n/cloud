package com.iot.device.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.toolkit.CollectionUtils;
import com.iot.device.api.DeviceTypeToGoodsApi;
import com.iot.device.model.DeviceTypeToGoods;
import com.iot.device.model.GoodsSmart;
import com.iot.device.model.GoodsSubDict;
import com.iot.device.model.SmartDeviceType;
import com.iot.device.service.IDeviceTypeToGoodsService;
import com.iot.device.service.IGoodsSmartService;
import com.iot.device.service.IGoodsSubDictService;
import com.iot.device.service.ISmartDeviceTypeService;
import com.iot.device.vo.rsp.DeviceTypeGoodsResp;
import com.iot.device.vo.rsp.ListGoodsSubDictResp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.stream.Collectors;

@RestController
public class DeviceTypeGoodsController implements DeviceTypeToGoodsApi {
    @Autowired
    private IDeviceTypeToGoodsService deviceTypeToGoodsService;

    @Autowired
    private ISmartDeviceTypeService smartDeviceTypeService;

    @Autowired
    private IGoodsSubDictService goodsSubDictService;

    @Autowired
    private IGoodsSmartService goodsSmartService;

    @Override
    public List<String> getDeviceTypeGoodsCodeByDeviceTypeId(Long deviceTypeId) {
        List<String> result = new ArrayList<>();
        EntityWrapper<DeviceTypeToGoods> goodsEntityWrapper = new EntityWrapper<>();
        goodsEntityWrapper.eq("device_type_id", deviceTypeId);
        List<DeviceTypeToGoods> goodsList = deviceTypeToGoodsService.selectList(goodsEntityWrapper);
        if (CollectionUtils.isNotEmpty(goodsList)) {
            result = goodsList.stream().map(DeviceTypeToGoods::getGoodsCode).collect(Collectors.toList());
        }
        return result;
    }


    public List<ListGoodsSubDictResp> getAllGoodsSubDict() {
        List<ListGoodsSubDictResp> result = new ArrayList<>();
        EntityWrapper<GoodsSubDict> entityWrapper = new EntityWrapper<>();
        List<GoodsSubDict> list = goodsSubDictService.selectList(entityWrapper);
        if (CollectionUtils.isNotEmpty(list)) {
            result = list.stream().map(goodsSubDict -> {
                ListGoodsSubDictResp subDictResp = new ListGoodsSubDictResp();
                subDictResp.setCode(goodsSubDict.getCode());
                subDictResp.setName(goodsSubDict.getName());
                subDictResp.setGoodsCode(goodsSubDict.getGoodsCode());
                return subDictResp;
            }).collect(Collectors.toList());
        }
        return result;
    }

    @Override
    public Map<String, List<ListGoodsSubDictResp>> getAllGoodsSubDictMap() {
        Map<String, List<ListGoodsSubDictResp>> result = new HashMap<>();
        List<ListGoodsSubDictResp> list = getAllGoodsSubDict();
        if (CollectionUtils.isNotEmpty(list)) {
            result = list.stream().collect(Collectors.groupingBy(ListGoodsSubDictResp::getGoodsCode));
        }
        return result;
    }

    @Override
    public Map<String, List<ListGoodsSubDictResp>> getGoodsSubDictMapByDeviceTypeId(Long deviceTypeId) {
        List<ListGoodsSubDictResp> list = getAllGoodsSubDict();
        EntityWrapper<SmartDeviceType> entityWrapper = new EntityWrapper<>();
        entityWrapper.eq("device_type_id", deviceTypeId);
        List<SmartDeviceType> smartDeviceTypeList = smartDeviceTypeService.selectList(entityWrapper);
        Map<Integer, String> goodsSmartMap = getAllGoodsSmartMap();
        List<String> goodsSubCodeList = smartDeviceTypeList.stream().map(k -> goodsSmartMap.get(k.getSmart()) + ":" + k.getSmartType()).collect(Collectors.toList());
        list.forEach(listGoodsSubDictResp -> {
            String code = listGoodsSubDictResp.getGoodsCode() + ":" + listGoodsSubDictResp.getCode();
            if (goodsSubCodeList.contains(code)) {
                listGoodsSubDictResp.setCheckFlag(true);
            } else {
                listGoodsSubDictResp.setCheckFlag(false);
            }
        });
        return list.stream().collect(Collectors.groupingBy(ListGoodsSubDictResp::getGoodsCode));
    }


    public Map<Integer, String> getAllGoodsSmartMap() {
        Map<Integer, String> result = new HashMap<>();
        EntityWrapper<GoodsSmart> entityWrapper = new EntityWrapper<>();
        List<GoodsSmart> list = goodsSmartService.selectList(entityWrapper);
        if (CollectionUtils.isNotEmpty(list)) {
            result = list.stream().collect(Collectors.toMap(GoodsSmart::getSmart, GoodsSmart::getGoodsCode, (k1, k2) -> k1));
        }
        return result;
    }

    @Override
    public List<DeviceTypeGoodsResp> getConfigGoodsByDeviceTypeId(Long deviceTypeId) {
        List<DeviceTypeGoodsResp> result = new ArrayList<>();
        List<String> goodsCodeList = this.getDeviceTypeGoodsCodeByDeviceTypeId(deviceTypeId);
        Map<String, List<ListGoodsSubDictResp>> subMap = getGoodsSubDictMapByDeviceTypeId(deviceTypeId);
        if (CollectionUtils.isNotEmpty(goodsCodeList)) {
            result = goodsCodeList.stream().map(goodsCode -> {
                DeviceTypeGoodsResp deviceTypeGoodsResp = new DeviceTypeGoodsResp();
                deviceTypeGoodsResp.setGoodsCode(goodsCode);
                List<ListGoodsSubDictResp> subList = subMap.get(goodsCode);
                if (CollectionUtils.isNotEmpty(subList)) {
                    Optional<ListGoodsSubDictResp> subOp = subList.stream().filter(subResp -> subResp.getCheckFlag()).findFirst();
                    ListGoodsSubDictResp sub = subOp.get();
                    if (sub != null) {
                        deviceTypeGoodsResp.setSubCode(sub.getCode());
                        deviceTypeGoodsResp.setSubName(sub.getName());
                    }
                }
                return deviceTypeGoodsResp;
            }).collect(Collectors.toList());
        }
        return result;
    }
}
