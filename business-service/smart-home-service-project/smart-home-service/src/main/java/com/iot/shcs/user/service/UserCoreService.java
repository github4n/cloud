package com.iot.shcs.user.service;

import com.iot.common.exception.BusinessException;
import com.iot.user.api.UserApi;
import com.iot.user.exception.UserExceptionEnum;
import com.iot.user.vo.FetchUserResp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author lucky
 * @ClassName UserCoreService
 * @Description 用户核心信息
 * @date 2018/12/24 9:57
 * @Version 1.0
 */
@Component
public class UserCoreService {

    @Autowired
    private UserApi userApi;

    public FetchUserResp getUserByUuid(String userUuid) {
        FetchUserResp user = userApi.getUserByUuid(userUuid);
        if (user == null) {
            throw new BusinessException(UserExceptionEnum.USER_IS_NOT_EXIST);
        }
        return user;
    }

    public FetchUserResp getUserByUserId(Long userId) {
        FetchUserResp user = userApi.getUser(userId);
        if (user == null) {
            throw new BusinessException(UserExceptionEnum.USER_IS_NOT_EXIST);
        }
        return user;
    }
}
