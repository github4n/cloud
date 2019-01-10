package com.iot.widget.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.iot.common.annotation.Action;
import com.iot.common.annotation.LoginRequired;
import com.iot.common.util.StringUtil;
import com.iot.saas.SaaSContextHolder;
import com.iot.shcs.widget.api.UserWidgetApi;
import com.iot.shcs.widget.constant.WidgetType;
import com.iot.shcs.widget.vo.req.UserWidgetReq;
import com.iot.shcs.widget.vo.resp.UserWidgetResp;
import com.iot.widget.utils.ResponseUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * 项目名称: IOT云平台
 * 模块名称：ios端widget功能接口
 * 功能描述：
 * 创建人: yuChangXing
 * 创建时间: 2019/1/3 21:45
 * 修改人:
 * 修改时间：
 */

@Api(description = "用户widget接口")
@RestController
@RequestMapping("/widget")
public class WidgetController {
    private Logger LOGGER = LoggerFactory.getLogger(WidgetController.class);

    @Autowired
    private UserWidgetApi userWidgetApi;


    @ApiOperation(value = "获取用户已选中widget数据")
    @LoginRequired(value = Action.Normal)
    @RequestMapping(value = "/findSelectedList", method = RequestMethod.GET)
    public JSONObject findSelectedList() {
        Long userId = SaaSContextHolder.getCurrentUserId();
        Long tenantId = SaaSContextHolder.currentTenantId();

        List<String> security = Lists.newArrayList();
        List<String> device = Lists.newArrayList();
        List<String> scene = Lists.newArrayList();

        List<UserWidgetResp> userWidgetRespList = userWidgetApi.getByUserId(userId);
        if (CollectionUtils.isNotEmpty(userWidgetRespList)) {
            for (UserWidgetResp uw : userWidgetRespList) {
                if (WidgetType.SECURITY.getType().equals(uw.getType())) {
                    security.add(uw.getValue());
                } else if (WidgetType.SCENE.getType().equals(uw.getType())) {
                    scene.add(uw.getValue());
                } else if (WidgetType.DEVICE.getType().equals(uw.getType())) {
                    device.add(uw.getValue());
                }
            }
        }

        Map<String, Object> payloadMap = Maps.newHashMap();
        payloadMap.put("security", security);
        payloadMap.put("device", device);
        payloadMap.put("scene", scene);

        Map<String, Object> resultMap = ResponseUtil.buildSuccessMap(payloadMap);

        JSONObject resultJsonObj = new JSONObject(resultMap);
        return resultJsonObj;
    }

    @ApiOperation(value = "用户widget的选中/取消选中")
    @LoginRequired(value = Action.Normal)
    @RequestMapping(value = "/setSelect", method = RequestMethod.POST)
    public JSONObject setSelect(@RequestBody JSONObject req) {
        Long userId = SaaSContextHolder.getCurrentUserId();
        Long tenantId = SaaSContextHolder.currentTenantId();

        JSONObject resultJsonObj = null;

        JSONObject jsonObject = req.getJSONObject("payload");
        if (jsonObject == null) {
            resultJsonObj = new JSONObject(ResponseUtil.buildFailMap("payload is empty."));
            return resultJsonObj;
        }

        String type = jsonObject.getString("type");
        if (StringUtil.isBlank(type)) {
            resultJsonObj = new JSONObject(ResponseUtil.buildFailMap("type is empty."));
            return resultJsonObj;
        }
        String id = jsonObject.getString("id");
        if (StringUtil.isBlank(id)) {
            resultJsonObj = new JSONObject(ResponseUtil.buildFailMap("id is empty."));
            return resultJsonObj;
        }
        int selectVal = jsonObject.getIntValue("selectVal");

        UserWidgetReq uwReq = new UserWidgetReq();
        uwReq.setUserId(userId);
        uwReq.setType(type);
        uwReq.setValue(id);
        uwReq.setTenantId(tenantId);

        if (selectVal == 0) {
            // 取消选中
            userWidgetApi.deleteUserWidget(uwReq);
        } else if (selectVal == 1) {
            // 选中
            userWidgetApi.addUserWidget(uwReq);
        }

        resultJsonObj = new JSONObject(ResponseUtil.buildSuccessMap(null));
        return resultJsonObj;
    }
}
