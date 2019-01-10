package com.iot.ifttt.service;

import com.iot.ifttt.vo.AppletVo;
import com.iot.ifttt.vo.CheckAppletReq;
import com.iot.ifttt.vo.SetEnableReq;

/**
 * 描述：ifttt服务接口
 * 创建人： LaiGuiMing
 * 创建时间： 2018/9/27 17:44
 */
public interface IIftttService {

    /**
     * 触发校验
     */
    void check(CheckAppletReq req);

    /**
     * 保存联动规则
     */
    Long save(AppletVo req);

    /**
     * 启用或禁用联动
     */
    void setEnable(SetEnableReq req);

    /**
     * 获取联动详细信息
     */
    AppletVo get(Long id);

    /**
     * 删除联动
     */
    Boolean delete(Long id);

    /**
     * 删除关联子项
     *
     * @param itemId
     */
    void delItem(Long itemId);
}
