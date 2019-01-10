package com.iot.shcs.widget.service;

import com.baomidou.mybatisplus.service.IService;
import com.iot.shcs.widget.entity.UserWidget;
import com.iot.shcs.widget.vo.req.UserWidgetReq;

import java.util.List;

/**
 * 项目名称: IOT云平台
 * 模块名称：
 * 功能描述：
 * 创建人: yuChangXing
 * 创建时间: 2019/1/3 22:15
 * 修改人:
 * 修改时间：
 */
public interface UserWidgetService extends IService<UserWidget> {
    List<UserWidget> getByUserId(Long userId);

    void addUserWidget(UserWidgetReq userWidgetReq);

    int countByTypeValue(Long userId, String widgetType, String value);

    void deleteSecurityWidget(Long userId);

    void deleteByTypeValue(Long userId, String widgetType, String value);
}
