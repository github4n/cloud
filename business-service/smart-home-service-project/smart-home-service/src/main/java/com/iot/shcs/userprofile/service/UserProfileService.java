package com.iot.shcs.userprofile.service;

import com.baomidou.mybatisplus.service.IService;
import com.iot.shcs.userprofile.constant.UserProfileType;
import com.iot.shcs.userprofile.entity.UserProfile;
import com.iot.shcs.userprofile.vo.req.UserProfileReq;

/**
 * 项目名称: IOT云平台
 * 模块名称：
 * 功能描述：
 * 创建人: yuChangXing
 * 创建时间: 2019/1/3 22:15
 * 修改人:
 * 修改时间：
 */
public interface UserProfileService extends IService<UserProfile> {

    UserProfile getByUserIdAndType(Long userId, String profileType);

    void saveOrUpdateUserProfile(UserProfileReq userProfileReq);
}
