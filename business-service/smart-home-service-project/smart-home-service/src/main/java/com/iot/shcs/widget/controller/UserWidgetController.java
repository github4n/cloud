package com.iot.shcs.widget.controller;

import com.google.common.collect.Lists;
import com.iot.shcs.widget.api.UserWidgetApi;
import com.iot.shcs.widget.entity.UserWidget;
import com.iot.shcs.widget.service.UserWidgetService;
import com.iot.shcs.widget.util.BeanCopyUtil;
import com.iot.shcs.widget.vo.req.UserWidgetReq;
import com.iot.shcs.widget.vo.resp.UserWidgetResp;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 项目名称: IOT云平台
 * 模块名称：
 * 功能描述：
 * 创建人: yuChangXing
 * 创建时间: 2019/1/3 22:18
 * 修改人:
 * 修改时间：
 */

@Slf4j
@RestController
public class UserWidgetController implements UserWidgetApi {
    @Autowired
    private UserWidgetService userWidgetService;

    @Override
    public List<UserWidgetResp> getByUserId(@RequestParam(value = "userId", required = true) Long userId) {
        List<UserWidget> userWidgetList = userWidgetService.getByUserId(userId);

        List<UserWidgetResp> userWidgetRespList = Lists.newArrayList();
        if (CollectionUtils.isNotEmpty(userWidgetList)) {
            for (UserWidget uw : userWidgetList) {

                UserWidgetResp uwResp = new UserWidgetResp();
                uwResp = BeanCopyUtil.copyUserWidget(uwResp, uw);
                if (uwResp != null) {
                    userWidgetRespList.add(uwResp);
                }
            }
        }
        return userWidgetRespList;
    }

    @Override
    public void addUserWidget(@RequestBody UserWidgetReq userWidgetReq) {
        userWidgetService.addUserWidget(userWidgetReq);
    }

    @Override
    public void deleteUserWidget(@RequestBody UserWidgetReq userWidgetReq) {
        userWidgetService.deleteByTypeValue(userWidgetReq.getUserId(), userWidgetReq.getType(), userWidgetReq.getValue());
    }
}
