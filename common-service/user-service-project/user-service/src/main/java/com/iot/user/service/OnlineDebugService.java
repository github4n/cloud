package com.iot.user.service;

import com.iot.common.exception.BusinessException;
import com.iot.common.util.StringUtil;
import com.iot.redis.RedisCacheUtil;
import com.iot.user.api.OnlineDebugApi;
import com.iot.user.entity.OnlineDebugEntity;
import com.iot.user.exception.UserExceptionEnum;
import com.iot.user.mapper.OnlineDebugMapper;
import com.iot.user.util.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class OnlineDebugService {

    @Autowired
    private OnlineDebugMapper onlineDebugMapper;

    public Boolean checkOnlineDebug(String uuid) {

        if (uuid == null || uuid.isEmpty()) {
            throw new BusinessException(UserExceptionEnum.UUID_IS_NULL);
        }

        String state;
        String key = RedisKeyUtil.getOnlineDebugKey(uuid);
        Boolean hasKey = RedisCacheUtil.hasKey(key);

        if (hasKey) {
            state = RedisCacheUtil.valueGet(key);
        } else {
            OnlineDebugEntity onlineDebugEntity = onlineDebugMapper.checkOnlineDebug(uuid);
            state = onlineDebugEntity != null ? onlineDebugEntity.getState() : "0";
            Long oneDay = 24 * 60 * 60L;
            RedisCacheUtil.valueSet(key, state, oneDay);
        }

        return "1".equals(state);
    }

    //添加和修改用户debug权限
    public String updateOnlineDebug(String userName,int state,int tenantId){

        //要求输入的用户名不能为空
        if(StringUtil.isEmpty(userName)){
              return "userName can't be null";
          }
        //查找该用户是否存在，存在则可以找到uuid
          String uuid=onlineDebugMapper.getUuidByUserName(userName,tenantId);
          if(StringUtil.isEmpty(uuid)){
              return "userName does't exists or tenantId does't right";
          }

          //状态正值为开放权限
         state=state>0?1:0;

        //查看数据库中是否有数据
        OnlineDebugEntity onlineDebugEntity = onlineDebugMapper.checkOnlineDebug(uuid);
        if(onlineDebugEntity==null){
            onlineDebugMapper.addOnlineDebug(uuid,state);
        }else{
            onlineDebugMapper.updateState(uuid,state,new Date());
        }

        String key=RedisKeyUtil.getOnlineDebugKey(uuid);
        Long oneDay=24*60*60L;
        //更新缓存

        RedisCacheUtil.valueSet(key,String.valueOf(state),oneDay);
        return "add ok!";

    }




}
