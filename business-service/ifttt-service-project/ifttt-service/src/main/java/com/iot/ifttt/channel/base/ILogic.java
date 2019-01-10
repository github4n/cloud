package com.iot.ifttt.channel.base;

import com.iot.ifttt.entity.AppletItem;

/**
 * 描述：业务逻辑
 * 创建人： LaiGuiMing
 * 创建时间： 2018/9/27 18:07
 */
public interface ILogic {
    void add(AppletItem item);

    void delete(AppletItem item);
}
