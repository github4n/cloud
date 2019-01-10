package com.iot.user.rabbit.consumer;

import com.alibaba.fastjson.JSON;
import com.iot.common.mq.process.AbsMessageProcess;
import com.iot.message.api.MessageApi;
import com.iot.message.entity.UserPhoneRelate;
import com.iot.redis.RedisCacheUtil;
import com.iot.saas.SaaSContextHolder;
import com.iot.user.api.UserApi;
import com.iot.user.constant.UserConstants;
import com.iot.user.entity.User;
import com.iot.user.entity.UserLogin;
import com.iot.user.mapper.UserMapper;
import com.iot.user.rabbit.bean.UserLoginAfterMessage;
import com.iot.user.service.UserService;
import com.iot.user.util.RedisKeyUtil;
import com.iot.user.vo.LoginReq;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @Descrpiton: userLoginAfter 处理器
 * @Author: yuChangXing
 * @Date: 2018/10/13 14:06
 * @Modify by:
 */

@Slf4j
@Component
public class UserLoginAfterProcess extends AbsMessageProcess<UserLoginAfterMessage> {
    @Autowired
    private UserService userService;
    @Autowired
    protected MessageApi messageApi;
    @Autowired
    protected UserMapper userMapper;


    @Override
    public void processMessage(UserLoginAfterMessage message) {
        log.debug("***** UserLoginAfterProcess, message={}", JSON.toJSONString(message));

        try {
            if (message == null) {
                log.debug("***** UserLoginAfterProcess end, because message is null");
                return ;
            }

            Long tenantId = message.getTenantId();
            Long userId = message.getUserId();
            String uuid = message.getUuid();

            //取出最新登录信息
            SaaSContextHolder.setCurrentTenantId(tenantId);

            //更新用户
            User user = new User();
            user.setId(userId);
            user.setUpdateTime(new Date());
            user.setState(UserConstants.USERSTATE_ONLINE); //在线状态
            userMapper.updateByPrimaryKeySelective(user);
            //删除用户缓存
            userService.delUserCache(userId);

            LoginReq req = new LoginReq();
            req.setTenantId(tenantId);
            req.setOs(message.getOs());
            req.setPhoneId(message.getPhoneId());
            req.setLastIp(message.getLastIp());

            UserLogin login = userService.getUserLogin(userId, tenantId);
            userService.updateUserLogin(login, req, userId);
            //如果是手机移动端登录，需要调用消息服务记录用户与手机phoneId的关系
            if (userService.isApp(message.getTerminalMark())) {
                UserPhoneRelate userPhoneRelate = new UserPhoneRelate(uuid, message.getPhoneId(), message.getOs(), uuid, tenantId, message.getAppId());
                messageApi.addUserPhoneRelate(userPhoneRelate);
            }
            //删除登录失败验证码
            RedisCacheUtil.delete(RedisKeyUtil.getLoginFailCodeKey(message.getUserName()));
        }catch (Exception e){
            log.error("更新登录信息失败",e);
        } finally {
            SaaSContextHolder.removeCurrentTenantId();
        }
    }
}
