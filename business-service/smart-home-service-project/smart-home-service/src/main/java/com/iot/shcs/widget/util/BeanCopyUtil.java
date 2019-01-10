package com.iot.shcs.widget.util;

import com.iot.shcs.widget.entity.UserWidget;
import com.iot.shcs.widget.vo.resp.UserWidgetResp;
import lombok.extern.slf4j.Slf4j;

/**
 * @Descrpiton:
 * @Author: yuChangXing
 * @Date: 2019/01/04 13:38
 * @Modify by:
 */

@Slf4j
public class BeanCopyUtil {

    public static UserWidgetResp copyUserWidget(UserWidgetResp target, UserWidget source) {
        if (target == null || source == null) {
            log.error("copyUserWidget, target or source is null.");
            return null;
        }

        target.setId(source.getId());
        target.setUserId(source.getUserId());
        target.setType(source.getType());
        target.setValue(source.getValue());
        target.setTenantId(source.getTenantId());
        target.setCreateTime(source.getCreateTime());
        target.setCreateBy(source.getCreateBy());
        target.setUpdateTime(source.getUpdateTime());
        target.setUpdateBy(source.getUpdateBy());
        return target;
    }
}
