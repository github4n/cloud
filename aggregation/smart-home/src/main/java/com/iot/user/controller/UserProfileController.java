package com.iot.user.controller;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import com.iot.common.annotation.Action;
import com.iot.common.annotation.LoginRequired;
import com.iot.saas.SaaSContextHolder;
import com.iot.shcs.userprofile.api.UserProfileApi;
import com.iot.shcs.userprofile.constant.UserProfileType;
import com.iot.shcs.userprofile.vo.req.UserProfileReq;
import com.iot.shcs.userprofile.vo.resp.UserProfileResp;
import com.iot.widget.utils.ResponseUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * 项目名称: IOT云平台
 * 模块名称：
 * 功能描述：
 * 创建人: yuChangXing
 * 创建时间: 2019/1/8 20:29
 * 修改人:
 * 修改时间：
 */

@Api(description = "用户配置接口")
@RestController
@RequestMapping("/user/profile")
public class UserProfileController {
    private Logger LOGGER = LoggerFactory.getLogger(UserProfileController.class);

    @Autowired
    private UserProfileApi userProfileApi;

    @ApiOperation(value = "获取scene short cut开关状态")
    @LoginRequired(value = Action.Normal)
    @RequestMapping(value = "/getSceneShortCut", method = RequestMethod.GET)
    public JSONObject getSceneShortCut() {
        Long userId = SaaSContextHolder.getCurrentUserId();
        Long tenantId = SaaSContextHolder.currentTenantId();

        int sceneShortCut = 0;
        UserProfileResp userProfileResp = userProfileApi.getByUserIdAndType(userId, UserProfileType.SCENE_SHORT_CUT.getType());
        if (userProfileResp != null) {
            String value = userProfileResp.getValue();
            try {
                sceneShortCut = Integer.parseInt(value);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        Map<String, Object> payloadMap = Maps.newHashMap();
        payloadMap.put("scene_short_cut", sceneShortCut);

        Map<String, Object> resultMap = ResponseUtil.buildSuccessMap(payloadMap);
        JSONObject resultJsonObj = new JSONObject(resultMap);
        return resultJsonObj;
    }

    @ApiOperation(value = "保存scene short cut开关状态")
    @LoginRequired(value = Action.Normal)
    @RequestMapping(value = "/setSceneShortCut", method = RequestMethod.POST)
    public JSONObject setSceneShortCut(@RequestBody JSONObject req) {
        Long userId = SaaSContextHolder.getCurrentUserId();
        Long tenantId = SaaSContextHolder.currentTenantId();

        JSONObject resultJsonObj = null;

        JSONObject payload = req.getJSONObject("payload");
        if (payload == null) {
            resultJsonObj = new JSONObject(ResponseUtil.buildFailMap("payload is empty."));
            return resultJsonObj;
        }

        int value = payload.getIntValue("scene_short_cut");

        UserProfileReq upReq = new UserProfileReq();
        upReq.setUserId(userId);
        upReq.setType(UserProfileType.SCENE_SHORT_CUT.getType());
        upReq.setValue(String.valueOf(value));
        upReq.setTenantId(tenantId);
        upReq.setCreateBy(userId);

        userProfileApi.saveOrUpdateUserProfile(upReq);

        resultJsonObj = new JSONObject(ResponseUtil.buildSuccessMap(null));
        return resultJsonObj;
    }
}
