package com.iot.shcs.widget.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.iot.common.util.StringUtil;
import com.iot.shcs.widget.constant.WidgetType;
import com.iot.shcs.widget.mapper.UserWidgetMapper;
import com.iot.shcs.widget.entity.UserWidget;
import com.iot.shcs.widget.service.UserWidgetService;
import com.iot.shcs.widget.vo.req.UserWidgetReq;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * 项目名称: IOT云平台
 * 模块名称：
 * 功能描述：
 * 创建人: yuChangXing
 * 创建时间: 2019/1/3 22:16
 * 修改人:
 * 修改时间：
 */

@Service
public class UserWidgetServiceImpl extends ServiceImpl<UserWidgetMapper, UserWidget> implements UserWidgetService {
    private Logger LOGGER = LoggerFactory.getLogger(UserWidgetServiceImpl.class);

    @Autowired
    private UserWidgetMapper userWidgetMapper;


    @Override
    public List<UserWidget> getByUserId(Long userId) {
        if (userId == null) {
            return null;
        }

        EntityWrapper<UserWidget> wrapper = new EntityWrapper<>();
        wrapper.eq("user_id", userId);
        List<UserWidget> userWidgetList = super.selectList(wrapper);
        return userWidgetList;
    }

    @Override
    public void addUserWidget(UserWidgetReq userWidgetReq) {
        int count = countByTypeValue(userWidgetReq.getUserId(), userWidgetReq.getType(), userWidgetReq.getValue());
        LOGGER.debug("addUserWidget, count={}", count);
        if (count == 0) {
            UserWidget userWidget = new UserWidget();
            userWidget.setUserId(userWidgetReq.getUserId());
            userWidget.setType(userWidgetReq.getType());
            userWidget.setValue(userWidgetReq.getValue());
            userWidget.setTenantId(userWidgetReq.getTenantId());

            Date currentTime = new Date();
            userWidget.setCreateTime(currentTime);
            userWidget.setUpdateTime(currentTime);
            userWidget.setCreateBy(userWidgetReq.getUserId());
            userWidget.setUpdateBy(userWidgetReq.getUserId());

            super.insert(userWidget);
        }
    }

    @Override
    public int countByTypeValue(Long userId, String widgetType, String value) {
        WidgetType type = WidgetType.getWidgetType(widgetType);
        if(userId == null || type == null || StringUtil.isBlank(value)){
            LOGGER.info("countByTypeValue error. because some parameter is null, userId={}," +
                    "type={}, value={}", userId, widgetType, value);
            return -1;
        }

        EntityWrapper<UserWidget> wrapper = new EntityWrapper<>();
        wrapper.eq("user_id",userId);
        wrapper.eq("type", type.getType());
        wrapper.eq("value", value);
        return super.selectCount(wrapper);
    }

    /**
     *  删除用户 security的widget
     * @param userId
     */
    @Override
    public void deleteSecurityWidget(Long userId) {
        LOGGER.info("deleteSecurityWidget, userId={}", userId);
        if(userId == null){
            LOGGER.info("deleteSecurityWidget error. because userId is null.");
            return ;
        }
        EntityWrapper<UserWidget> wrapper = new EntityWrapper<>();
        wrapper.eq("user_id",userId);
        wrapper.eq("type", WidgetType.SECURITY.getType());
        super.delete(wrapper);
    }

    /**
     *  删除用户 widget
     * @param userId
     * @param widgetType
     * @param value
     */
    @Override
    public void deleteByTypeValue(Long userId, String widgetType, String value) {
        LOGGER.info("deleteByTypeValue, userId={}, widgetType={}, value={}", userId, widgetType, value);

        WidgetType type = WidgetType.getWidgetType(widgetType);
        if(userId == null || type == null || StringUtil.isBlank(value)){
            LOGGER.info("deleteByTypeValue error.");
            return ;
        }
        EntityWrapper<UserWidget> wrapper = new EntityWrapper<>();
        wrapper.eq("user_id",userId);
        wrapper.eq("type", type.getType());
        wrapper.eq("value", value);
        super.delete(wrapper);
    }
}
