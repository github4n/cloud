package com.iot.shcs.userprofile.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.iot.common.util.StringUtil;
import com.iot.shcs.userprofile.constant.UserProfileType;
import com.iot.shcs.userprofile.entity.UserProfile;
import com.iot.shcs.userprofile.mapper.UserProfileMapper;
import com.iot.shcs.userprofile.service.UserProfileService;
import com.iot.shcs.userprofile.vo.req.UserProfileReq;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * 项目名称: IOT云平台
 * 模块名称：
 * 功能描述：
 * 创建人: yuChangXing
 * 创建时间: 2019/1/8 19:12
 * 修改人:
 * 修改时间：
 */
@Service
public class UserProfileServiceImpl extends ServiceImpl<UserProfileMapper, UserProfile> implements UserProfileService {
    private Logger LOGGER = LoggerFactory.getLogger(UserProfileServiceImpl.class);

    @Autowired
    private UserProfileMapper userProfileMapper;


    @Override
    public UserProfile getByUserIdAndType(Long userId, String profileType) {
        UserProfileType type = UserProfileType.getProfileType(profileType);
        if (userId == null || type == null) {
            LOGGER.error("getByUserIdAndType error, userId={}, profileType={}", userId, profileType);
            return null;
        }

        EntityWrapper<UserProfile> wrapper = new EntityWrapper<>();
        wrapper.eq("user_id", userId);
        wrapper.eq("type", type.getType());
        return super.selectOne(wrapper);
    }

    @Override
    public void saveOrUpdateUserProfile(UserProfileReq userProfileReq) {
        LOGGER.info("saveOrUpdateUserProfile, userProfileReq={}", JSON.toJSONString(userProfileReq));
        if (userProfileReq.getUserId() == null || StringUtil.isEmpty(userProfileReq.getType())) {
            LOGGER.error("saveOrUpdateUserProfile error, userId={}, profileType={}", userProfileReq.getUserId(), userProfileReq.getType());
            return;
        }

        Date currentTime = new Date();
        UserProfile userProfile = getByUserIdAndType(userProfileReq.getUserId(), userProfileReq.getType());
        if (userProfile == null) {
            // 新增
            userProfile = new UserProfile();
            userProfile.setUserId(userProfileReq.getUserId());
            userProfile.setType(userProfileReq.getType());
            userProfile.setValue(userProfileReq.getValue());
            userProfile.setTenantId(userProfileReq.getTenantId());

            userProfile.setCreateTime(currentTime);
            userProfile.setUpdateTime(currentTime);
            userProfile.setCreateBy(userProfileReq.getUserId());
            userProfile.setUpdateBy(userProfileReq.getUserId());

            super.insert(userProfile);
        } else {
            // 更新
            userProfile.setValue(userProfileReq.getValue());
            userProfile.setUpdateTime(currentTime);
            userProfile.setUpdateBy(userProfileReq.getUpdateBy());
            super.updateById(userProfile);
        }
    }
}
