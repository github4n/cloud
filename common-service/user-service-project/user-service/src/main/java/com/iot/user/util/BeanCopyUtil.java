package com.iot.user.util;

import com.iot.common.util.MD5SaltUtil;
import com.iot.user.entity.User;
import com.iot.user.entity.UserToken;
import com.iot.user.vo.*;

/**
 * 描述：bean字段赋值工具类
 * 创建人： LaiGuiMing
 * 创建时间： 2018/6/8 17:37
 */
public class BeanCopyUtil {

    public static User getUserFromRegister(RegisterReq source) {
        User target = new User();
        if (source == null) {
            return target;
        }
        target.setUserName(source.getUserName());
        target.setNickname(source.getNickname());
        target.setPassword(MD5SaltUtil.generate(source.getPassWord()));//source.getPassWord()
        target.setTel(source.getTel());
        target.setEmail(source.getEmail());
        target.setHeadImg(source.getHeadImg());
        target.setBackground(source.getBackground());
        target.setTenantId(source.getTenantId());
        target.setAdminStatus(source.getAdminStatus());
        return target;
    }

    public static LoginResp getRespFromToken(UserToken source) {
        LoginResp target = new LoginResp();
        if (source == null) {
            return target;
        }
        target.setAccessToken(source.getAccessToken());
        target.setRefreshToken(source.getRefreshToken());
        target.setExpireIn(source.getExpireIn());
        target.setUserId(source.getUserId());
        target.setUserUuid(source.getUserUuid());
        target.setTenantId(source.getTenantId());
        return target;
    }

    public static FetchUserResp getRespFromUser(User source) {
        FetchUserResp target = new FetchUserResp();
        if (source == null) {
            return target;
        }
        target.setId(source.getId());
        target.setTenantId(source.getTenantId());
        target.setUserName(source.getUserName());
        target.setNickname(source.getNickname());
        target.setState(source.getState());
        target.setPassword(source.getPassword());
        target.setUuid(source.getUuid());
        target.setMqttPassword(source.getMqttPassword());
        target.setTel(source.getTel());
        target.setHeadImg(source.getHeadImg());
        target.setBackground(source.getBackground());
        target.setEmail(source.getEmail());
        target.setLocationId(source.getLocationId());
        target.setAddress(source.getAddress());
        target.setAdminStatus(source.getAdminStatus());
        target.setCompany(source.getCompany());
        target.setCreateTime(source.getCreateTime());
        target.setUpdateTime(source.getUpdateTime());
        target.setUserLevel(source.getUserLevel());
        target.setUserStatus(source.getUserStatus());
        return target;
    }

    public static UserTokenResp getTokenRespFromToken(UserToken source) {
        UserTokenResp target = new UserTokenResp();
        if (source == null) {
            return target;
        }
        target.setAccessToken(source.getAccessToken());
        target.setRefreshToken(source.getRefreshToken());
        target.setTenantId(source.getTenantId());
        target.setExpireIn(source.getExpireIn());
        target.setUserId(source.getUserId());
        target.setUserUuid(source.getUserUuid());
        target.setTerminalMark(source.getTerminalMark());
        target.setOrgId(source.getOrgId());
        target.setAppId(source.getAppId());
        return target;
    }

    public static User getUserFromUpdate(UpdateUserReq source) {
        User target = new User();
        if (source == null) {
            return target;
        }
        target.setId(source.getId());
        target.setTenantId(source.getTenantId());
        target.setUserName(source.getUserName());
        target.setNickname(source.getNickname());
        target.setState(source.getState());
        target.setPassword(source.getPassword());
        target.setMqttPassword(source.getMqttPassword());
        target.setTel(source.getTel());
        target.setHeadImg(source.getHeadImg());
        target.setEmail(source.getEmail());
        target.setAddress(source.getAddress());
        target.setAdminStatus(source.getAdminStatus());
        target.setCompany(source.getCompany());
        target.setUpdateTime(source.getUpdateTime());
        return target;
    }
}
