package com.iot.boss.service.malf.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.iot.boss.dao.malf.MalfAttendanceMapper;
import com.iot.boss.dao.malf.MalfProcesLogMapper;
import com.iot.boss.dao.malf.MalfRecordMapper;
import com.iot.boss.entity.MalfAttendanceTimer;
import com.iot.boss.entity.MalfProcesLog;
import com.iot.boss.enums.MalfHandleTypeEnum;
import com.iot.boss.service.malf.WarningDispatchService;
import com.iot.redis.RedisCacheUtil;


/**
 * 项目名称：立达信IOT云平台
 * 模块名称：视频云后台管理
 * 功能描述：报障指派管理
 * 创建人： xiangyitao
 * 创建时间：2017年10月17日
 */
@Service
public class WarningDispatchServiceImpl implements WarningDispatchService {



    private final Logger  logger = LoggerFactory.getLogger(getClass());
    
    private final int[] weekDays={0,1,2,3,4,5,6};

    //活动的定时器前缀
  	public static final String WARNING_TIMER_PREFIX="WARNING_TIMER_PREFIX_";

    //超级管理员前缀
  	public static final String SUPER_ADMIN_ID="super_admin_id";
  	
    @Autowired
    private MalfAttendanceMapper malfAttendanceMapper;

    @Autowired
    private MalfRecordMapper malfRecordMapper;

    @Autowired
    private MalfProcesLogMapper malfProcesLogMapper;
    
  	@SuppressWarnings({ "unused" })
	@Override
	public void addWarning(Long malfRecordId, Date createDateTime) {
		final int weekend = weekDays[getWeekDays(createDateTime)];
		List<MalfAttendanceTimer> listAll = null;// 所有的CYCLE下的timer
		List<MalfAttendanceTimer> activeDBWarning = null;// 数据库满足条件的timer
		List<MalfAttendanceTimer> activeRedisWarning = null;// reids下满足条件的timer
		final List<MalfAttendanceTimer> activeDbReidsWarning = new ArrayList<MalfAttendanceTimer>();// redis和DB合并
		List<MalfAttendanceTimer> all = null;
		MalfAttendanceTimer warningTimer;// 被选中的管理员
		int ptime = Integer.parseInt(getHourMinute(createDateTime).replace(":", ""));
		listAll = malfAttendanceMapper.getMalfAttendance(weekend);// 查询定时器列表
		activeDBWarning = listAll.stream().filter(e -> (Integer.parseInt(e.getStartTime().replace(":", "")) < ptime
				&& Integer.parseInt(e.getEndTime().replace(":", "")) > ptime)).collect(Collectors.toList()); // 找到DB满足条件的定时器

		List<MalfAttendanceTimer> redisWarning = (List<MalfAttendanceTimer>) RedisCacheUtil
				.listGetAll(WARNING_TIMER_PREFIX + weekend, MalfAttendanceTimer.class);// redis里存timer
		if (redisWarning != null && !redisWarning.isEmpty()) {// 把新的timer和reids满足条件的timer合并
			activeRedisWarning = redisWarning.stream()
					.filter(e -> (Integer.parseInt(e.getStartTime().replace(":", "")) < ptime
							&& Integer.parseInt(e.getEndTime().replace(":", "")) > ptime
							&& e.getExecutionCycle() == weekend))
					.collect(Collectors.toList()); // 找到redis满足条件的定时器
			// 合并DB和redis的
			activeDBWarning.stream().forEach(w -> {
				activeDbReidsWarning.add(w);
			});
			redisWarning.forEach(w -> {
				activeDbReidsWarning.add(w);
			});
			all = removeDuplicates(activeDbReidsWarning);// 去掉权重小的重复数据
		} else {
			// redis为空
			activeDBWarning.forEach(w -> {
				activeDbReidsWarning.add(w);
			});
			all = activeDbReidsWarning;
		}
		if (all != null && !all.isEmpty()) {
			all.sort((e, e1) -> (e.getWeight() > e1.getWeight()) ? 1 : -1);
			warningTimer = all.get(0);
			malfRecordMapper.updateMalfRecordStatus(warningTimer.getAdminId(),malfRecordId, 1);

			warningTimer.setWeight(warningTimer.getWeight() + 1);
			all.set(0, warningTimer);
			RedisCacheUtil.listSet(WARNING_TIMER_PREFIX + weekend, all); // redis里存人timer

			// 添加报障流水
			MalfProcesLog warningProcess = new MalfProcesLog();
			warningProcess.setHandleMsg("System automatically sends a single");
			warningProcess.setMalfId(malfRecordId);
			warningProcess.setHandleAdminId(0L);
			warningProcess.setHandleTime(new Date());
			warningProcess.setHandleType(MalfHandleTypeEnum.DISPATCH_COMPLETE.getValue());
//			warningProcess.setId(RedisCacheUtil.incr(ModuleConstants.DB_TABLE_MALF_PROCES_LOG, 0L));
			malfProcesLogMapper.insert(warningProcess);
		}
	}

	public List<MalfAttendanceTimer> removeDuplicates(List<MalfAttendanceTimer> l) {
		List<MalfAttendanceTimer> all = new ArrayList<MalfAttendanceTimer>();
		Map<Long, MalfAttendanceTimer> map = new HashMap<Long, MalfAttendanceTimer>();
		l.stream().forEach(w -> {
			if (map.get(w.getId()) == null) {
				map.put(w.getId(), w);
			} else {
				MalfAttendanceTimer wsrc = map.get(w.getId());
				if (w.getWeight() > wsrc.getWeight()) {
					map.put(w.getId(), w);
				}
			}
		});
		map.forEach((k, v) -> {
			all.add(v);
		});
		return all;
	}


    /**
     * 返回当前的星期数
     * @param today
     * @return
     */
	public int getWeekDays(Date today) {
		Calendar c = Calendar.getInstance();
		c.setTime(today);
		int weekday = c.get(Calendar.DAY_OF_WEEK);
		return weekday - 1;
	}

	public String getHourMinute(Date today) {
		SimpleDateFormat sdf = new SimpleDateFormat("HHmm");
		return sdf.format(today);
	}

    /**
     * 
     * 描述：更新redis保障信息
     * @author 李帅
     * @created 2018年5月16日 下午1:49:37
     * @since 
     * @param warningTimer
     * @param type 1 ： 修改  2 ： 删除
     */
	@Override
	public void updateOrDelete(MalfAttendanceTimer warningTimer, String type) {
		List<MalfAttendanceTimer> redisWarningToRedis = null;
		
		int weekend = weekDays[getWeekDays(new Date())];
		List<MalfAttendanceTimer> redisWarning = (List<MalfAttendanceTimer>) RedisCacheUtil
				.listGetAll(WARNING_TIMER_PREFIX + weekend, MalfAttendanceTimer.class);// redis里存timer
		if (redisWarning == null)
			return;
		if (redisWarning.isEmpty())
			return;
		if (warningTimer.getAdminId() == null || warningTimer.getAdminId() == 0 || warningTimer.getExecutionCycle() == 0) {
			logger.warn("updateOrDelete:::", "param error");
			return;
		}
		if ("update".endsWith(type)) {// 修改
			redisWarningToRedis = new ArrayList<MalfAttendanceTimer>();
			for (MalfAttendanceTimer malfAttendanceTimer : redisWarning) {
				if(malfAttendanceTimer.getAdminId().equals(warningTimer.getAdminId())) {
					warningTimer.setWeight(malfAttendanceTimer.getWeight());
					redisWarningToRedis.add(warningTimer);
				}else {
					redisWarningToRedis.add(malfAttendanceTimer);
				}
			}
		} else if ("delete".endsWith(type)) {// 删除
			redisWarningToRedis = new ArrayList<MalfAttendanceTimer>();
			for (MalfAttendanceTimer malfAttendanceTimer : redisWarning) {
				if(malfAttendanceTimer.getAdminId().equals(warningTimer.getAdminId())) {

				}else {
					redisWarningToRedis.add(malfAttendanceTimer);
				}
			}
		}
		RedisCacheUtil.listSet(WARNING_TIMER_PREFIX + weekend, redisWarningToRedis);// 只操作当前的周期
	}
}
