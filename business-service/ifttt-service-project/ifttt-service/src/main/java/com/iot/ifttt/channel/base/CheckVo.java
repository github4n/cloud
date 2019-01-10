package com.iot.ifttt.channel.base;

import com.iot.ifttt.entity.AppletItem;
import lombok.Data;

import java.util.List;

/**
 * 描述：检测请求对象
 * 创建人： LaiGuiMing
 * 创建时间： 2018/10/25 11:32
 */
@Data
public class CheckVo {
    String logic;
    List<AppletItem> items;
    String msg;
}
