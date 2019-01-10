package com.iot.report.schedule;

import com.iot.common.util.CalendarUtil;
import com.iot.redis.RedisCacheUtil;
import com.iot.report.entity.UserActivatedEntity;
import com.iot.report.service.UserActiveService;
import com.iot.user.constant.UserConstants;
import com.iot.user.vo.UserActivated;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 *@description 定时去获取redis保存的信息，同步到mongodb中
 *@author wucheng
 *@create 2019/1/4 14:45
 */
@Slf4j
@Component
public class UserActivatedSchedule {

    @Autowired
    private UserActiveService userActiveService;

    @Scheduled(cron = "0 10 1 * * ?")
    public void saveUserActivated() {
        // 这个时间是2019-01-4  实际获取插入数据库的是前一天 2019-01-3
        log.info("同步用户数据到mongodb数据库start");
        String redisKey = UserConstants.USER_ACTIVATED + CalendarUtil.getYesterdayByCalendar(CalendarUtil.YYYYMMDD);
        //
        List<UserActivated> activatedUserList =  RedisCacheUtil.listGetAll(redisKey, UserActivated.class);
        if (activatedUserList != null && activatedUserList.size() > 0) {
            for (UserActivated t : activatedUserList) {
                UserActivatedEntity userActiveEntity = new UserActivatedEntity(t.getUuid(), t.getTenantId(), new Date(t.getTime()));
                userActiveService.saveUserActivated(userActiveEntity);
            }
        }
        log.info("同步用户数据到mongodb数据库end");
        RedisCacheUtil.delete(UserConstants.USER_ACTIVATED + CalendarUtil.getYesterdayByCalendar(CalendarUtil.YYYYMMDD));
    }
}
