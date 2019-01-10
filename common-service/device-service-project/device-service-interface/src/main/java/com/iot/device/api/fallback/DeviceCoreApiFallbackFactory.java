package com.iot.device.api.fallback;

import com.iot.common.helper.Page;
import com.iot.device.api.DeviceCoreApi;
import com.iot.device.vo.req.DevTypePageReq;
import com.iot.device.vo.req.device.ListCommDeviceReq;
import com.iot.device.vo.req.device.ListDeviceByParamsReq;
import com.iot.device.vo.req.device.ListDeviceInfoReq;
import com.iot.device.vo.req.device.PageDeviceInfoReq;
import com.iot.device.vo.req.device.UpdateDeviceConditionReq;
import com.iot.device.vo.req.device.UpdateDeviceInfoReq;
import com.iot.device.vo.rsp.DeviceResp;
import com.iot.device.vo.rsp.device.GetDeviceInfoRespVo;
import com.iot.device.vo.rsp.device.GetDeviceTypeByDeviceRespVo;
import com.iot.device.vo.rsp.device.GetProductByDeviceRespVo;
import com.iot.device.vo.rsp.device.ListDeviceByParamsRespVo;
import com.iot.device.vo.rsp.device.ListDeviceInfoRespVo;
import com.iot.device.vo.rsp.device.PageDeviceInfoRespVo;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class DeviceCoreApiFallbackFactory implements FallbackFactory<DeviceCoreApi> {

  @Override
  public DeviceCoreApi create(Throwable cause) {
    return new DeviceCoreApi() {
      @Override
      public List<ListDeviceInfoRespVo> listDevices(ListDeviceInfoReq params) {
        return null;
      }

      @Override
      public GetDeviceInfoRespVo get(String deviceId) {
        return null;
      }

      @Override
      public GetDeviceInfoRespVo saveOrUpdate(UpdateDeviceInfoReq params) {
        return null;
      }

      @Override
      public void updateByCondition(UpdateDeviceConditionReq params) {
        return;
      }

      @Override
      public void saveOrUpdateBatch(List<UpdateDeviceInfoReq> paramsList) {}

      @Override
      public List<ListDeviceInfoRespVo> listDevicesByParentId(String parentDeviceId) {
        return null;
      }

      @Override
      public List<String> deleteByDeviceId(String deviceId) {
        return null;
      }

      @Override
      public void deleteBatchByDeviceIds(List<String> deviceIds) {}

      @Override
      public GetProductByDeviceRespVo getProductByDeviceId(String deviceId) {
        return null;
      }

      @Override
      public GetDeviceTypeByDeviceRespVo getDeviceTypeByDeviceId(String deviceId) {
        return null;
      }

      @Override
      public Page<PageDeviceInfoRespVo> pageDeviceInfoByParams(PageDeviceInfoReq params) {
        return null;
      }

      @Override
      public Page<DeviceResp> pageDeviceByDeviceTypeList(DevTypePageReq pageReq) {
        return null;
      }

      @Override
      public List<ListDeviceByParamsRespVo> listDeviceByParams(ListDeviceByParamsReq params) {
        return null;
      }

      @Override
      public List<DeviceResp> findDevRelationListByDeviceIds(ListCommDeviceReq params) {
        return null;
      }
    };
  }
}
