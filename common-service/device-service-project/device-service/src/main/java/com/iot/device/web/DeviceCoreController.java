package com.iot.device.web;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.iot.common.helper.Page;
import com.iot.device.api.DeviceCoreApi;
import com.iot.device.business.DeviceBusinessService;
import com.iot.device.business.DeviceExtendBusinessService;
import com.iot.device.business.DeviceStateBusinessService;
import com.iot.device.business.DeviceStatusBusinessService;
import com.iot.device.business.DeviceTypeBusinessService;
import com.iot.device.business.ProductBusinessService;
import com.iot.device.business.core.DeviceCoreBusinessService;
import com.iot.device.core.utils.BeanCopyUtils;
import com.iot.device.model.Device;
import com.iot.device.model.DeviceExtend;
import com.iot.device.model.DeviceStatus;
import com.iot.device.model.DeviceType;
import com.iot.device.model.Product;
import com.iot.device.service.IDeviceService;
import com.iot.device.service.IProductConfigNetmodeService;
import com.iot.device.vo.req.DevTypePageReq;
import com.iot.device.vo.req.device.ListCommDeviceReq;
import com.iot.device.vo.req.device.ListDeviceByParamsReq;
import com.iot.device.vo.req.device.ListDeviceInfoReq;
import com.iot.device.vo.req.device.PageDeviceInfoReq;
import com.iot.device.vo.req.device.UpdateDeviceConditionReq;
import com.iot.device.vo.req.device.UpdateDeviceInfoReq;
import com.iot.device.vo.rsp.DeviceResp;
import com.iot.device.vo.rsp.ProductConfigNetmodeRsp;
import com.iot.device.vo.rsp.device.GetDeviceInfoRespVo;
import com.iot.device.vo.rsp.device.GetDeviceTypeByDeviceRespVo;
import com.iot.device.vo.rsp.device.GetDeviceTypeInfoRespVo;
import com.iot.device.vo.rsp.device.GetProductByDeviceRespVo;
import com.iot.device.vo.rsp.device.GetProductInfoRespVo;
import com.iot.device.vo.rsp.device.ListDeviceByParamsRespVo;
import com.iot.device.vo.rsp.device.ListDeviceExtendRespVo;
import com.iot.device.vo.rsp.device.ListDeviceInfoRespVo;
import com.iot.device.vo.rsp.device.ListDeviceStatusRespVo;
import com.iot.device.vo.rsp.device.ListDeviceTypeRespVo;
import com.iot.device.vo.rsp.device.ListProductRespVo;
import com.iot.device.vo.rsp.device.PageDeviceInfoRespVo;
import com.iot.device.web.utils.DeviceCoreCopyUtils;
import com.iot.util.AssertUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/** @Author: lucky @Descrpiton: @Date: 11:03 2018/9/25 @Modify by: */
@Slf4j
@RestController
public class DeviceCoreController implements DeviceCoreApi {

  @Autowired
  private DeviceBusinessService deviceBusinessService;

  @Autowired
  private DeviceExtendBusinessService deviceExtendBusinessService;

  @Autowired
  private DeviceStatusBusinessService deviceStatusBusinessService;

  @Autowired
  private DeviceStateBusinessService deviceStateBusinessService;

  @Autowired
  private DeviceCoreBusinessService deviceCoreBusinessService;

  @Autowired
  private ProductBusinessService productBusinessService;

  @Autowired
  private DeviceTypeBusinessService deviceTypeBusinessService;

  @Autowired
  private IDeviceService deviceService;

  @Autowired
  private IProductConfigNetmodeService productConfigNetmodeService;

  @Override
  public List<ListDeviceInfoRespVo> listDevices(@RequestBody @Validated ListDeviceInfoReq params) {
    List<ListDeviceInfoRespVo> resultDataList = Lists.newArrayList();
    List<Device> sourceDataList =
        deviceBusinessService.listBatchDevices(null, params.getDeviceIds());
    if (CollectionUtils.isEmpty(sourceDataList)) {
      return resultDataList;
    }
    sourceDataList.forEach(
        source -> {
          ListDeviceInfoRespVo target = new ListDeviceInfoRespVo();
          DeviceCoreCopyUtils.copyDevice(source, target);
          resultDataList.add(target);
        });
    return resultDataList;
  }

  @Override
  public GetDeviceInfoRespVo get(
      @RequestParam(value = "deviceId", required = true) String deviceId) {
    GetDeviceInfoRespVo resultData = null;
    Device sourceData = deviceBusinessService.getDevice(null, deviceId);
    if (null != sourceData) {
      resultData = new GetDeviceInfoRespVo();
      DeviceCoreCopyUtils.copyDevice(sourceData, resultData);
    }
    return resultData;
  }

  @Override
  public GetDeviceInfoRespVo saveOrUpdate(@RequestBody @Validated UpdateDeviceInfoReq params) {
    GetDeviceInfoRespVo resultData = deviceBusinessService.saveOrUpdate(params);
    return resultData;
  }

  public void updateByCondition(@RequestBody UpdateDeviceConditionReq params) {
    deviceBusinessService.updateByCondition(params);
    return;
  }

  @Override
  public void saveOrUpdateBatch(@RequestBody @Validated List<UpdateDeviceInfoReq> paramsList) {
    deviceBusinessService.saveOrUpdateBatch(paramsList);
  }

  @Override
  public List<ListDeviceInfoRespVo> listDevicesByParentId(
      @RequestParam(value = "parentDeviceId", required = true) String parentDeviceId) {
    List<ListDeviceInfoRespVo> resultDataList = Lists.newArrayList();
    List<Device> sourceDataList =
        deviceBusinessService.findDevicesByParentDeviceId(null, parentDeviceId);
    if (CollectionUtils.isEmpty(sourceDataList)) {
      return resultDataList;
    }
    sourceDataList.forEach(
        source -> {
          ListDeviceInfoRespVo target = new ListDeviceInfoRespVo();
          DeviceCoreCopyUtils.copyDevice(source, target);
          resultDataList.add(target);
        });
    return resultDataList;
  }

  @Override
  public List<String> deleteByDeviceId(@RequestParam(value = "deviceId", required = true) String deviceId) {
    List<String> delDeviceIds = deviceCoreBusinessService.deleteByDeviceId(deviceId);
    return delDeviceIds;
  }

  @Override
  public void deleteBatchByDeviceIds(@RequestBody List<String> deviceIds) {
    deviceCoreBusinessService.deleteBatchDeviceIds(deviceIds);
  }

  @Override
  public GetProductByDeviceRespVo getProductByDeviceId(@RequestParam("deviceId") String deviceId) {
    GetProductByDeviceRespVo resultData = null;
    GetDeviceInfoRespVo deviceInfo = this.get(deviceId);
    if (deviceInfo != null) {
      Long productId = deviceInfo.getProductId();
      Product product = productBusinessService.getProduct(productId);
      GetProductInfoRespVo productInfo = null;
      if (product != null) {
        productInfo = new GetProductInfoRespVo();
        BeanUtils.copyProperties(product, productInfo);
      }
      resultData =
          GetProductByDeviceRespVo.builder()
              .deviceInfo(deviceInfo)
              .productInfo(productInfo)
              .build();
    }
    return resultData;
  }

  @Override
  public GetDeviceTypeByDeviceRespVo getDeviceTypeByDeviceId(
      @RequestParam("deviceId") String deviceId) {
    GetDeviceTypeByDeviceRespVo resultData = null;
    GetDeviceInfoRespVo deviceInfo = this.get(deviceId);
    if (deviceInfo != null) {
      Long productId = deviceInfo.getProductId();
      Product product = productBusinessService.getProduct(productId);
      GetProductInfoRespVo productInfo = null;
      GetDeviceTypeInfoRespVo deviceTypeInfo = null;
      if (product != null) {
        productInfo = new GetProductInfoRespVo();
        BeanUtils.copyProperties(product, productInfo);

        Long deviceTypeId = productInfo.getDeviceTypeId();
        DeviceType deviceType = deviceTypeBusinessService.getDeviceType(deviceTypeId);
        if (deviceType != null) {
          deviceTypeInfo = new GetDeviceTypeInfoRespVo();
          BeanUtils.copyProperties(deviceType, deviceTypeInfo);
        }
      }
      resultData =
          GetDeviceTypeByDeviceRespVo.builder()
              .deviceInfo(deviceInfo)
              .productInfo(productInfo)
              .deviceTypeInfo(deviceTypeInfo)
              .build();
    }
    return resultData;
  }

  @Override
  public Page<PageDeviceInfoRespVo> pageDeviceInfoByParams(
      @RequestBody @Validated PageDeviceInfoReq params) {

    Page<PageDeviceInfoRespVo> pageResult = deviceService.findPageByParams(params);
    return pageResult;
  }

  @Override
  public Page<DeviceResp> pageDeviceByDeviceTypeList(@RequestBody DevTypePageReq pageReq) {
    return deviceService.findPageByDeviceTypeList(pageReq);
  }

  @Override
  public List<ListDeviceByParamsRespVo> listDeviceByParams(
      @RequestBody ListDeviceByParamsReq params) {
    return deviceService.listByParams(params);
  }

  public List<DeviceResp> findDevRelationListByDeviceIds(@RequestBody  ListCommDeviceReq params){
    long startTime = new Date().getTime();
    AssertUtils.notNull(params.getTenantId(),"tenantId.notnull");
    Long tenantId = params.getTenantId();
    List<String> targetDeviceIds = params.getDeviceIds();
    List<DeviceResp> resultDataList = Lists.newArrayList();
    if (CollectionUtils.isEmpty(targetDeviceIds)) {
      return resultDataList;
    }
    //1.设备明细
    List<Device> deviceInfoList = deviceBusinessService.listBatchDevices(null,targetDeviceIds);
    if (CollectionUtils.isEmpty(deviceInfoList)) {
      return resultDataList;
    }
    DeviceCoreCopyUtils.copyDeviceInfoList(deviceInfoList, resultDataList);//设备详情

    //2.获取设备扩展 deviceExtends
    Map<String, DeviceExtend> deviceExtendMap = Maps.newHashMap();
    List<DeviceExtend> deviceExtendList = deviceExtendBusinessService.listBatchDeviceExtends(tenantId,targetDeviceIds);
    if (!CollectionUtils.isEmpty(deviceExtendList)) {
      deviceExtendList.forEach(deviceExtend -> {
        deviceExtendMap.put(deviceExtend.getDeviceId(), deviceExtend);
      });
//      deviceExtendMap = deviceExtendList.stream().collect(Collectors.toMap(DeviceExtend::getDeviceId, deviceExtend -> deviceExtend));
    }
    DeviceCoreCopyUtils.copyDeviceExtendList(deviceExtendMap, resultDataList);//设备p2p id

    //3.获取设备状态【属性状态】
    Map<String, Map<String, Object>> deviceStateMap = deviceStateBusinessService.listBatchDeviceStates(tenantId,targetDeviceIds);
    DeviceCoreCopyUtils.copyDeviceStateList(deviceStateMap, resultDataList);//设备状态

    //4 设备状态【上下线】
    Map<String, DeviceStatus> deviceStatusMap = Maps.newHashMap();
    List<DeviceStatus> deviceStatusList = deviceStatusBusinessService.listBatchDeviceStatus(tenantId, targetDeviceIds);
    if (!CollectionUtils.isEmpty(deviceStatusList)) {
      deviceStatusList.forEach(deviceStatus -> {
        deviceStatusMap.put(deviceStatus.getDeviceId(), deviceStatus);
      });
//      deviceStatusMap = deviceStatusList.stream().collect(Collectors.toMap(DeviceStatus::getDeviceId, deviceStatus -> deviceStatus));
    }
    DeviceCoreCopyUtils.copyDeviceStatusList(deviceStatusMap, resultDataList);//设备上下线状态

    //5.获取设备对应产品
    List<Long> targetProductIds = getDistinctProductIdList(deviceInfoList);
    Map<Long, Product> productInfoMap = Maps.newHashMap();
    List<Product> productList = productBusinessService.listBatchProducts(targetProductIds);
    if (!CollectionUtils.isEmpty(productList)){
      productList.forEach(product -> {
        List<ProductConfigNetmodeRsp> configNetmodeRsps = productConfigNetmodeService.listByProductId(product.getId());
        product.setConfigNetmodeRsps(configNetmodeRsps);
        productInfoMap.put(product.getId(), product);
      });
//      productInfoMap = productList.stream().collect(Collectors.toMap(Product::getId, productInfo -> productInfo));
    }
    DeviceCoreCopyUtils.copyProductList(productInfoMap, resultDataList);//产品信息

    //6.获取设备对应的设备类型
    List<Long> targetDeviceTypeIds = getDistinctDeviceTypeIdList(productInfoMap.values());
    Map<Long, DeviceType> deviceTypeMap = Maps.newHashMap();
    List<DeviceType> deviceTypeList = deviceTypeBusinessService.listBatchDeviceTypes(targetDeviceTypeIds);
    if (!CollectionUtils.isEmpty(deviceTypeList)){
      deviceTypeList.forEach(deviceType -> {
        deviceTypeMap.put(deviceType.getId(), deviceType);
      });
//      deviceTypeMap = deviceTypeList.stream().collect(Collectors.toMap(DeviceType::getId, deviceType -> deviceType));
    }
    DeviceCoreCopyUtils.copyDeviceTypeList(deviceTypeMap, resultDataList);//设备类型
    long consumerTime = new Date().getTime() - startTime;
    if (consumerTime >= 1000) {
      //超过1s的输出
      log.debug("findDevRelationListByDeviceIds:consumerTime:{}", consumerTime);
    }
    return resultDataList;
  }

  private List<Long> getDistinctProductIdList(List<Device> deviceInfoList) {
    Set<Long> productIdsSet = Sets.newHashSet();
    if(CollectionUtils.isEmpty(deviceInfoList)) {
      return Lists.newArrayList();
    }
    deviceInfoList.forEach(deviceInfo->{
      Long productId = deviceInfo.getProductId();
      if(productId != null && !productIdsSet.contains(productId)){
        productIdsSet.add(productId);
      }
    });
    return Lists.newArrayList(productIdsSet);
  }

  private List<Long> getDistinctDeviceTypeIdList(Collection<Product> productInfoList) {
    Set<Long> resDeviceTypeIds = Sets.newHashSet();
    if (CollectionUtils.isEmpty(productInfoList)) {
      return Lists.newArrayList();
    }
    productInfoList.forEach(product -> {
      Long deviceTypeId = product.getDeviceTypeId();
      if (deviceTypeId != null && !resDeviceTypeIds.contains(deviceTypeId)) {
        resDeviceTypeIds.add(deviceTypeId);
      }
    });
    return Lists.newArrayList(resDeviceTypeIds);
  }
}
