package com.iot.device.controller;

import com.google.common.collect.Lists;
import com.iot.common.helper.Page;
import com.iot.device.api.DeviceCoreApi;
import com.iot.device.api.GroupApi;
import com.iot.device.business.DeviceBusinessService;
import com.iot.device.business.DeviceTypeBusinessService;
import com.iot.device.business.ProductBusinessService;
import com.iot.device.business.core.DeviceCoreBusinessService;
import com.iot.device.model.Device;
import com.iot.device.model.DeviceType;
import com.iot.device.model.Product;
import com.iot.device.service.IDeviceService;
import com.iot.device.service.IGroupDetailService;
import com.iot.device.service.IGroupService;
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
import com.iot.device.vo.rsp.device.GetDeviceTypeInfoRespVo;
import com.iot.device.vo.rsp.device.GetProductByDeviceRespVo;
import com.iot.device.vo.rsp.device.GetProductInfoRespVo;
import com.iot.device.vo.rsp.device.ListDeviceByParamsRespVo;
import com.iot.device.vo.rsp.device.ListDeviceInfoRespVo;
import com.iot.device.vo.rsp.device.PageDeviceInfoRespVo;
import com.iot.device.vo.rsp.group.GetGroupDetailResp;
import com.iot.device.vo.rsp.group.GetGroupInfoResp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
public class GroupController implements GroupApi {

  @Autowired
  private IGroupService groupService;

  @Autowired
  private IGroupDetailService groupDetailService;


  @Override
  public Long save(@RequestBody @Valid UpdateGroupReq params) {
    return groupService.save(params);
  }

  @Override
  public boolean update(@RequestBody @Valid UpdateGroupReq params) {
    return groupService.update(params);
  }

  @Override
  public List<GetGroupInfoResp> getGroupByUser(@RequestBody @Valid UpdateGroupReq params){
    return groupService.getGroupByUser(params);
  }

  @Override
  public GetGroupInfoResp getGroupById(@RequestBody @Valid UpdateGroupReq params){
    return groupService.getGroupById(params);
  }

  @Override
  public List<GetGroupInfoResp> getGroup(@RequestBody @Valid UpdateGroupReq params){
    return groupService.getGroup(params);
  }

  @Override
  public boolean delGroupById(@RequestBody @Valid UpdateGroupReq params){
    return groupService.delByCondition(params);
  }



  @Override
  public Long saveGroupDetial(@RequestBody @Valid UpdateGroupDetailReq params){
    return groupDetailService.save(params);
  }

  @Override
  public boolean saveGroupDetialBatch(@RequestBody @Valid UpdateGroupDetailReq params) {
    return groupDetailService.saveBatch(params);
  }

  @Override
  public boolean delGroupDetial(@RequestBody @Valid UpdateGroupDetailReq params) {
    return groupDetailService.delByCondition(params);
  }

  @Override
  public List<GetGroupDetailResp> getGroupDetailByDevId(@RequestBody @Valid UpdateGroupDetailReq param){
    return groupDetailService.getGroupDetailByDevId(param);
  }

  @Override
  public List<GetGroupDetailResp> getGroupDetailByGroupId(@RequestBody @Valid UpdateGroupDetailReq param){
    return groupDetailService.getGroupDetailByGroupId(param);
  }

  @Override
  public List<GetGroupDetailResp> getGroupDetial(@RequestBody @Valid UpdateGroupDetailReq params) {
    return groupDetailService.getGroupDetail(params);
  }
}
