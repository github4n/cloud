package com.iot.ifttt.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.iot.ifttt.entity.Applet;
import com.iot.ifttt.mapper.AppletMapper;
import com.iot.ifttt.service.IAppletService;
import com.iot.ifttt.util.RedisKeyUtil;
import com.iot.redis.RedisCacheUtil;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 应用程序表 服务实现类
 * </p>
 *
 * @author laiguiming
 * @since 2018-09-28
 */
@Service
public class AppletServiceImpl extends ServiceImpl<AppletMapper, Applet> implements IAppletService {

    @Override
    public String getStatus(Long id) {
        String key = RedisKeyUtil.getAppletStatusKey(id);
        String status = RedisCacheUtil.valueGet(key);

        //缓存空
        if (status == null) {
            Applet applet = selectById(id);
            if (applet != null) {
                status = applet.getStatus();
            } else {
                status = "-1";
            }
            RedisCacheUtil.valueSet(key, status, RedisKeyUtil.DEFAULT_EXPIRE_TIME_OUT_7);
        }

        //真空
        if ("-1".equals(status)) {
            status = null;
        }
        return status;
    }

    @Override
    public boolean delete(Long id) {
        //数据库删除
        deleteById(id);
        //删除status缓存
        RedisCacheUtil.delete(RedisKeyUtil.getAppletStatusKey(id));
        return true;
    }

    @Override
    public boolean update(Applet applet) {
        if (applet.getId() == null) {
            return false;
        }

        updateById(applet);
        //删除status缓存
        RedisCacheUtil.delete(RedisKeyUtil.getAppletStatusKey(applet.getId()));
        return true;
    }
}
