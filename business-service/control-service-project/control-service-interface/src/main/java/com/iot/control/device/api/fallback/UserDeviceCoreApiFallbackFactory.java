package com.iot.control.device.api.fallback;

import com.iot.common.helper.Page;
import com.iot.control.device.api.UserDeviceCoreApi;
import com.iot.control.device.vo.req.DelUserDeviceInfoReq;
import com.iot.control.device.vo.req.GetUserDeviceInfoReq;
import com.iot.control.device.vo.req.ListUserDeviceInfoReq;
import com.iot.control.device.vo.req.PageUserDeviceInfoReq;
import com.iot.control.device.vo.req.UpdateUserDeviceInfoReq;
import com.iot.control.device.vo.resp.ListUserDeviceInfoRespVo;
import com.iot.control.device.vo.resp.PageUserDeviceInfoRespVo;
import com.iot.control.device.vo.resp.UpdateUserDeviceInfoResp;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class UserDeviceCoreApiFallbackFactory implements FallbackFactory<UserDeviceCoreApi> {

  @Override
  public UserDeviceCoreApi create(Throwable cause) {
    return new UserDeviceCoreApi() {

      @Override
      public UpdateUserDeviceInfoResp saveOrUpdate(UpdateUserDeviceInfoReq params) {
        return null;
      }

      @Override
      public List<UpdateUserDeviceInfoResp> saveOrUpdateBatch(
          List<UpdateUserDeviceInfoReq> paramsList) {
        return null;
      }

      @Override
      public List<ListUserDeviceInfoRespVo> listUserDevice(ListUserDeviceInfoReq params) {
        return null;
      }

      @Override
      public List<ListUserDeviceInfoRespVo> listBatchUserDevice(GetUserDeviceInfoReq params) {
        return null;
      }

      @Override
      public void delUserDevice(Long tenantId, Long userId, String deviceId) {}

      @Override
      public void delBatchUserDevice(DelUserDeviceInfoReq params) {

      }

      @Override
      public Page<PageUserDeviceInfoRespVo> pageUserDevice(PageUserDeviceInfoReq params) {
        return null;
      }
    };
  }
}
