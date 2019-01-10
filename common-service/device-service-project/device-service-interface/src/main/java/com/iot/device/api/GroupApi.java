package com.iot.device.api;

import com.iot.common.helper.Page;
import com.iot.device.api.fallback.DeviceCoreApiFallbackFactory;
import com.iot.device.api.fallback.GroupApiFallbackFactory;
import com.iot.device.vo.req.DevTypePageReq;
import com.iot.device.vo.req.device.ListDeviceByParamsReq;
import com.iot.device.vo.req.device.ListDeviceInfoReq;
import com.iot.device.vo.req.device.PageDeviceInfoReq;
import com.iot.device.vo.req.device.UpdateDeviceConditionReq;
import com.iot.device.vo.req.device.UpdateDeviceInfoReq;
import com.iot.device.vo.req.group.UpdateGroupDetailReq;
import com.iot.device.vo.req.group.UpdateGroupReq;
import com.iot.device.vo.rsp.DeviceResp;
import com.iot.device.vo.rsp.device.GetDeviceInfoRespVo;
import com.iot.device.vo.rsp.device.GetDeviceTypeByDeviceRespVo;
import com.iot.device.vo.rsp.device.GetProductByDeviceRespVo;
import com.iot.device.vo.rsp.device.ListDeviceByParamsRespVo;
import com.iot.device.vo.rsp.device.ListDeviceInfoRespVo;
import com.iot.device.vo.rsp.device.PageDeviceInfoRespVo;
import com.iot.device.vo.rsp.group.GetGroupDetailResp;
import com.iot.device.vo.rsp.group.GetGroupInfoResp;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import java.util.List;

@FeignClient(value = "device-service", fallbackFactory = GroupApiFallbackFactory.class)
@RequestMapping("/group")
public interface GroupApi {

  @RequestMapping(value = "/save", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
  Long save(@RequestBody @Valid UpdateGroupReq params);

  @RequestMapping(value = "/update", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
  boolean update(@RequestBody @Valid UpdateGroupReq params);

  @RequestMapping(value = "/getGroupByUser", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
  List<GetGroupInfoResp> getGroupByUser(@RequestBody @Valid UpdateGroupReq params);

  @RequestMapping(value = "/getGroupById", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
  GetGroupInfoResp getGroupById(@RequestBody @Valid UpdateGroupReq params);

  @RequestMapping(value = "/getGroup", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
  List<GetGroupInfoResp> getGroup(@RequestBody @Valid UpdateGroupReq params);

  @RequestMapping(value = "/delGroupById", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
  boolean delGroupById(@RequestBody @Valid UpdateGroupReq params);

  @RequestMapping(value = "/saveGroupDetial", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
  Long saveGroupDetial(@RequestBody @Valid UpdateGroupDetailReq params);

  @RequestMapping(value = "/saveGroupDetialBatch", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
  boolean saveGroupDetialBatch(@RequestBody @Valid UpdateGroupDetailReq params);

  @RequestMapping(value = "/delGroupDetial", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
  boolean delGroupDetial(@RequestBody @Valid UpdateGroupDetailReq params);

  @RequestMapping(value = "/getGroupDetailByDevId", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
  List<GetGroupDetailResp> getGroupDetailByDevId(@RequestBody @Valid UpdateGroupDetailReq param);

  @RequestMapping(value = "/getGroupDetailByGroupId", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
  List<GetGroupDetailResp> getGroupDetailByGroupId(@RequestBody @Valid UpdateGroupDetailReq param);

  @RequestMapping(value = "/getGroupDetial", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
  List<GetGroupDetailResp> getGroupDetial(@RequestBody @Valid UpdateGroupDetailReq params);


}
