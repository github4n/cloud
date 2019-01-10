package com.iot.shcs.userprofile.controller;

import com.iot.shcs.userprofile.api.UserProfileApi;
import com.iot.shcs.userprofile.entity.UserProfile;
import com.iot.shcs.userprofile.service.UserProfileService;
import com.iot.shcs.userprofile.util.BeanCopyUtil;
import com.iot.shcs.userprofile.vo.req.UserProfileReq;
import com.iot.shcs.userprofile.vo.resp.UserProfileResp;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
public class UserProfileController implements UserProfileApi {
    @Autowired
    private UserProfileService userProfileService;


    @Override
    public UserProfileResp getByUserIdAndType(@RequestParam(value = "userId", required = true) Long userId,
                                              @RequestParam(value = "profileType", required = true) String profileType) {
        UserProfileResp userProfileResp = null;
        UserProfile userProfile = userProfileService.getByUserIdAndType(userId, profileType);
        if (userProfile != null) {
            userProfileResp = new UserProfileResp();
            userProfileResp = BeanCopyUtil.copyUserProfile(userProfileResp, userProfile);
        }
        return userProfileResp;
    }

    @Override
    public void saveOrUpdateUserProfile(@RequestBody UserProfileReq userProfileReq) {
        userProfileService.saveOrUpdateUserProfile(userProfileReq);
    }
}
