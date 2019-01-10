package com.iot.ifttt.channel.base;

import com.iot.ifttt.entity.AppletItem;

import java.util.List;

/**
 * 描述：执行器接口
 * 创建人： LaiGuiMing
 * 创建时间： 2018/9/27 18:05
 */
public interface IExecutor {
    void execute(List<AppletItem> items);
}
