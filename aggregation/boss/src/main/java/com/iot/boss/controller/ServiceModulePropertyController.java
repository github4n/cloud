package com.iot.boss.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.iot.common.annotation.Action;
import com.iot.common.annotation.LoginRequired;
import com.iot.common.beans.CommonResponse;
import com.iot.common.exception.ResultMsg;
import com.iot.device.api.ServicePropertyApi;
import com.iot.device.vo.req.ServiceModulePropertyReq;
import com.iot.saas.SaaSContextHolder;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 模组-属性表 前端控制器
 * </p>
 *
 * @author lucky
 * @since 2018-06-27
 */
@RestController
@Api(description = "模组属性", value = "模组属性")
@RequestMapping("/api/service/module/property")
public class ServiceModulePropertyController {

    private Logger log = LoggerFactory.getLogger(ServiceModulePropertyController.class);

    @Autowired
    private ServicePropertyApi servicePropertyApi;

    @ApiOperation("增加或者更新")
    @LoginRequired(value = Action.Normal)
    @RequestMapping(value = "saveOrUpdate", method = RequestMethod.POST)
    public CommonResponse saveOrUpdate(@RequestBody ServiceModulePropertyReq serviceModulePropertyReq) {
        log.debug("增加或者更新:{}", JSONObject.toJSON(serviceModulePropertyReq));
        serviceModulePropertyReq.setCreateBy(SaaSContextHolder.getCurrentUserId());
        serviceModulePropertyReq.setUpdateBy(SaaSContextHolder.getCurrentUserId());
        serviceModulePropertyReq.setTenantId(SaaSContextHolder.currentTenantId());
        if (serviceModulePropertyReq.getParamType().equals(2) || serviceModulePropertyReq.getParamType().equals(3)){
            JSONArray jsonArray = JSONArray.parseArray(serviceModulePropertyReq.getAllowedValues());
            List<String> list = new ArrayList();
            jsonArray.forEach(m->{
                JSONObject jsonObject = JSONObject.parseObject(m.toString());
                list.add(jsonObject.get("value").toString());
            });
            if (serviceModulePropertyReq.getParamType().equals(2)){
                if (list.size()!=2){
                    return  new CommonResponse<>(ResultMsg.FAIL,"类型为boolean的参数值必须有两项", null);
                }
            }
            List<String> result = list.stream().distinct().collect(Collectors.toList());
            if (list.size()!=result.size()){
                return  new CommonResponse<>(ResultMsg.FAIL,"参数值不允许有重复", null);
            } else {
                return new CommonResponse<>(ResultMsg.SUCCESS, servicePropertyApi.saveOrUpdate(serviceModulePropertyReq));
            }
        }
        CommonResponse<Long> result = new CommonResponse<>(ResultMsg.SUCCESS, servicePropertyApi.saveOrUpdate(serviceModulePropertyReq));
        return result;
    }

    @ApiOperation("批量增加或者更新")
    @LoginRequired(value = Action.Normal)
    @RequestMapping(value = "saveOrUpdateBatch", method = RequestMethod.POST)
    public CommonResponse saveOrUpdateBatch(@RequestBody List<ServiceModulePropertyReq> ServiceModulePropertyReqs) {
        log.debug("批量增加或者更新:{}", JSONObject.toJSON(ServiceModulePropertyReqs));
        CommonResponse result = new CommonResponse<>(ResultMsg.SUCCESS, servicePropertyApi.saveOrUpdateBatch(ServiceModulePropertyReqs));
        return result;
    }

    @ApiOperation("根据id删除")
    @LoginRequired(value = Action.Normal)
    @RequestMapping(value = "delete", method = RequestMethod.DELETE)
    public CommonResponse delete(@RequestBody ArrayList<Long> ids) {
        log.debug("根据id删除", ids.toArray());
        servicePropertyApi.delete(ids);
        return CommonResponse.success();
    }

    @ApiOperation("根据id删除")
    @LoginRequired(value = Action.Normal)
    @RequestMapping(value = "deleteByPost", method = RequestMethod.POST)
    public CommonResponse deleteByPost(@RequestBody ArrayList<Long> ids) {
        log.debug("根据id删除", ids.toArray());
        servicePropertyApi.delete(ids);
        return CommonResponse.success();
    }

    @ApiOperation("分页获取列表")
    @LoginRequired(value = Action.Normal)
    @RequestMapping(value = "list", method = RequestMethod.POST)
    public CommonResponse list(@RequestBody ServiceModulePropertyReq serviceModulePropertyReq) {
        PageInfo result = servicePropertyApi.list(serviceModulePropertyReq);
        return CommonResponse.success(result);
    }

    @ApiOperation("根据serviceModuleId获取列表")
    @LoginRequired(value = Action.Normal)
    @RequestMapping(value = "listByServiceModuleId/{serviceModuleId}", method = RequestMethod.GET)
    public CommonResponse listByServiceModuleId(@PathVariable(value = "serviceModuleId", required = true) Long serviceModuleId) {
        log.debug("根据serviceModuleId获取列表", serviceModuleId);
        List list = servicePropertyApi.listByServiceModuleId(serviceModuleId);
        return CommonResponse.success(list);
    }

    @ApiOperation("根据actionModuleId获取列表")
    @LoginRequired(value = Action.Normal)
    @RequestMapping(value = "listByActionModuleId/{actionModuleId}", method = RequestMethod.GET)
    public CommonResponse listByActionModuleId(@PathVariable(value = "actionModuleId", required = true) Long actionModuleId) {
        log.debug("根据actionModuleId获取列表", actionModuleId);
        List list = servicePropertyApi.listByActionModuleId(actionModuleId);
        return CommonResponse.success(list);
    }

    @ApiOperation("根据eventModuleId获取列表")
    @LoginRequired(value = Action.Normal)
    @RequestMapping(value = "listByEventModuleId/{eventModuleId}", method = RequestMethod.GET)
    public CommonResponse listByEventModuleId(@PathVariable(value = "eventModuleId", required = true) Long eventModuleId) {
        log.debug("根据eventModuleId获取列表", eventModuleId);
        List list = servicePropertyApi.listByEventModuleId(eventModuleId);
        return CommonResponse.success(list);
    }

}

