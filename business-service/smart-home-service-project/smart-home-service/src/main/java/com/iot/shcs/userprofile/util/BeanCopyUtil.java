package com.iot.shcs.userprofile.util;

import com.iot.shcs.userprofile.entity.UserProfile;
import com.iot.shcs.userprofile.vo.resp.UserProfileResp;
import lombok.extern.slf4j.Slf4j;

/**
 * @Descrpiton:
 * @Author: yuChangXing
 * @Date: 2019/01/04 13:38
 * @Modify by:
 */

@Slf4j
public class BeanCopyUtil {

    public static UserProfileResp copyUserProfile(UserProfileResp target, UserProfile source) {
        if (target == null || source == null) {
            log.error("copyUserProfile, target or source is null.");
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
