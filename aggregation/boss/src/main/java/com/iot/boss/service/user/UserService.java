package com.iot.boss.service.user;

import com.iot.user.vo.LoginReq;
import com.iot.user.vo.LoginResp;
import com.iot.user.vo.RegisterReq;

public interface UserService {

    /**
     * 描述：添加子账户
     *
     * @param user
     * @author chq
     * @created 2018年8月21日 下午2:37:31
     * @since
     */
    public void createSubUser(RegisterReq user);

    public LoginResp userLogin(LoginReq req);

    public Long userRegister(RegisterReq user);

    public void sendVerifyCode(String email, Byte type);

    public void checkUserName(RegisterReq req);

    public void auditUser( RegisterReq registerReq);

    public void checkUserHadRight(Long userId);

}
