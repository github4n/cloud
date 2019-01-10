package com.iot.device.controller;

import com.google.common.collect.Lists;
import com.iot.common.beans.BeanUtil;
import com.iot.common.helper.Page;
import com.iot.device.api.ElectricityStatisticsApi;
import com.iot.device.model.DailyElectricityStatistics;
import com.iot.device.model.DailyRuntime;
import com.iot.device.model.MinElectricityStatistics;
import com.iot.device.model.MinRuntime;
import com.iot.device.model.MonthlyElectricityStatistics;
import com.iot.device.model.MonthlyRuntime;
import com.iot.device.model.WeeklyElectricityStatistics;
import com.iot.device.model.WeeklyRuntime;
import com.iot.device.repository.MinElectricityStatisticsRepository;
import com.iot.device.repository.MinRuntimeRepository;
import com.iot.device.service.*;
import com.iot.device.vo.req.*;
import com.iot.device.vo.rsp.*;
import com.iot.util.AssertUtils;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author CHQ
 * @since 2018-05-02
 */
@RestController
public class ElectricityStatisticsController implements ElectricityStatisticsApi {
	
	public static final Logger LOGGER = LoggerFactory.getLogger(ElectricityStatisticsController.class);

//	@Autowired
//    private IMinElectricityStatisticsService minElectricityStatisticsService;

    @Autowired
	private MinElectricityStatisticsRepository minElectricityStatisticsRepository;
    @Autowired
    private IMinElectricityStatisticsMongoService iMinElectricityStatisticsMongoService;

//	@Autowired
//    private IDailyElectricityStatisticsService dailyElectricityStatisticsService;
    @Autowired
    private IDailyElectricityStatisticsMongoService iDailyElectricityStatisticsMongoService;

//	@Autowired
//    private IWeeklyElectricityStatisticsService weeklyElectricityStatisticsService;
    @Autowired
    private IWeeklyElectricityStatisticsMongoService iWeeklyElectricityStatisticsMongoService;

//	@Autowired
//    private IMonthlyElectricityStatisticsService monthlyElectricityStatisticsService;
    @Autowired
    private IMonthlyElectricityStatisticsMongoService iMonthlyElectricityStatisticsMongoService;

//	@Autowired
//    private IMinRuntimeService minRuntimeService;
    @Autowired
    private IMinRuntimeMongoService iMinRuntimeMongoService;
    @Autowired
    private MinRuntimeRepository minRuntimeRepository;
	
//	@Autowired
//    private IDailyRuntimeService dailyRuntimeService;
    @Autowired
    private IDailyRuntimeMongoService iDailyRuntimeMongoService;
	
//	@Autowired
//    private IWeeklyRuntimeService weeklyRuntimeService;
    @Autowired
    private IWeeklyRuntimeMongoService iWeeklyRuntimeMongoService;
	
//	@Autowired
//    private IMonthlyRuntimeService monthlyRuntimeService;
    @Autowired
    private IMonthlyRuntimeMongoService iMonthlyRuntimeMongoService;

	/**
	 * 将上报电量值插入数据库
	 */
	@Override
	public boolean insertElectricityStatistics(@RequestBody ElectricityStatisticsReq electrictyStatisticsReq) {
		LOGGER.info("insertElectricityStatistics {}",electrictyStatisticsReq);
		AssertUtils.notEmpty(electrictyStatisticsReq, "electrictyStatisticsReq.notnull");
		AssertUtils.notEmpty(electrictyStatisticsReq.getDeviceId(), "deviceId.notnull");
		AssertUtils.notEmpty(electrictyStatisticsReq.getUserId(), "userId.notnull");
		AssertUtils.notEmpty(electrictyStatisticsReq.getTime(), "time.notnull");
		// 数据插入minElectricityStatisticsService
		MinElectricityStatistics minElectricityStatistics = new MinElectricityStatistics();
		BeanUtil.copyProperties(electrictyStatisticsReq, minElectricityStatistics);
        minElectricityStatistics.setLocaltimeStr(new SimpleDateFormat("yyyy-MM-dd").format(electrictyStatisticsReq.getLocaltime()));
        minElectricityStatistics.setTimeStr(new SimpleDateFormat("yyyy-MM-dd").format(electrictyStatisticsReq.getTime()));
        minElectricityStatisticsRepository.insert(minElectricityStatistics);

        return true;
	}

	/**
	 * 更新数据库、redis数据
	 */
	@Override
	public void insertOrUpdateElectricityStatistics() {
		// 从minElectricityStatistics表查询当天用电量
        List<ElectricityStatisticsRsp> electricityList = iMinElectricityStatisticsMongoService.selectByCondition();
		LOGGER.info("insertOrUpdateElectricityStatistics.electricityList({})",electricityList);
		if (CollectionUtils.isNotEmpty(electricityList)) {
			List<DailyElectricityStatistics> dailylist = new ArrayList<>();
			List<WeeklyElectricityStatistics> weeklylist = new ArrayList<>();
			List<MonthlyElectricityStatistics> monthlylist = new ArrayList<>();
			ElectricityStatisticsRsp electricityStatisticsRsp = electricityList.get(0);

			SimpleDateFormat format = new SimpleDateFormat("yyyy");
			String yearString = format.format(electricityStatisticsRsp.getDay());
			Integer year = Integer.valueOf(yearString);

			Date time = electricityStatisticsRsp.getDay();
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(time);
			Integer week = calendar.get(Calendar.WEEK_OF_YEAR);
			Integer month = calendar.get(Calendar.MONTH) + 1;

			for (ElectricityStatisticsRsp rsp : electricityList) {
				List<WeeklyElectricityStatistics> rspWeeklylist = new ArrayList<>();
				List<MonthlyElectricityStatistics> rspMonthlylist = new ArrayList<>();

				// 数据插入daily electricity statistics
				DailyElectricityStatistics dailyElectricity = new DailyElectricityStatistics();
				BeanUtil.copyProperties(rsp, dailyElectricity);
				dailylist.add(dailyElectricity);
				// 数据插入weekly electricity statistics
				WeeklyElectricityStatistics weeklyElectricityStatistics = new WeeklyElectricityStatistics();
				BeanUtil.copyProperties(rsp, weeklyElectricityStatistics);
				weeklyElectricityStatistics.setYear(year);
				weeklyElectricityStatistics.setWeek(week);
                //数据库取出周电量
                rspWeeklylist = iWeeklyElectricityStatisticsMongoService.selectByCondition(weeklyElectricityStatistics);

				if (CollectionUtils.isNotEmpty(rspWeeklylist)) {
					for (WeeklyElectricityStatistics weeklyElectricity : rspWeeklylist) {
						Double electricValue = rsp.getElectricValue();
						//累加后电量
						electricValue += weeklyElectricity.getElectricValue();
						//放回对象
						weeklyElectricity.setElectricValue(electricValue);
						//修改后的list
						weeklylist.add(weeklyElectricity);
					}
				} else {
					weeklylist.add(weeklyElectricityStatistics);
				}
				// 数据插入monthly electricity statistics
				MonthlyElectricityStatistics monthlyElectricityStatistics = new MonthlyElectricityStatistics();
				BeanUtil.copyProperties(rsp, monthlyElectricityStatistics);
				monthlyElectricityStatistics.setYear(year);
				monthlyElectricityStatistics.setMonth(month);
                rspMonthlylist = iMonthlyElectricityStatisticsMongoService.selectByCondition(monthlyElectricityStatistics);

				if (CollectionUtils.isNotEmpty(rspMonthlylist)) {
					for (MonthlyElectricityStatistics monthlyElectricity : rspMonthlylist) {
						Double electricValue = rsp.getElectricValue();
						electricValue += monthlyElectricity.getElectricValue();
						monthlyElectricity.setElectricValue(electricValue);
						monthlylist.add(monthlyElectricity);
					}
				} else {
					monthlylist.add(monthlyElectricityStatistics);
				}
			}

			// 数据放入redis
			if (CollectionUtils.isNotEmpty(dailylist)) {
				Boolean isSuccess = false;// TODO 变成直接插入数据库
                isSuccess = iDailyElectricityStatisticsMongoService.insertOrUpdateBatch(dailylist);
				LOGGER.info("ElectricityStatistics.dailylist({})",dailylist);
			}
			if (CollectionUtils.isNotEmpty(weeklylist)) {
				Boolean isSuccess = false;
                isSuccess = iWeeklyElectricityStatisticsMongoService.insertOrUpdateBatch(weeklylist);
				LOGGER.info("ElectricityStatistics.weeklylist({})",weeklylist);

			}
			if (CollectionUtils.isNotEmpty(monthlylist)) {
				Boolean isSuccess = false;
                isSuccess = iMonthlyElectricityStatisticsMongoService.insertOrUpdateBatch(monthlylist);
				LOGGER.info("ElectricityStatistics.monthlylist({})",monthlylist);
			}

		}else {
			LOGGER.info("===========minelectricityList is null==========", electricityList);
		}

	}

	@Override
	public ElectricityStatisticsRsp selectElectricityRspByDeviceAndUser(@RequestBody EnergyReq energyReq) {
        return iMinElectricityStatisticsMongoService.selectElectricityRspByDeviceAndUser(energyReq);

	}

	/**
	 * 将上报运行时间保存进数据库
	 */
	@Override
	public boolean insertRuntime(@RequestBody RuntimeReq runtimeReq) {
		LOGGER.info("insertRuntime({})",runtimeReq);
		AssertUtils.notEmpty(runtimeReq, "runtimeReq.notnull");
		AssertUtils.notEmpty(runtimeReq.getDeviceId(), "deviceId.notnull");
		AssertUtils.notEmpty(runtimeReq.getUserId(), "userId.notnull");
		AssertUtils.notEmpty(runtimeReq.getTime(), "time.notnull");
		// 数据插入minRuntime
		MinRuntime minRuntime = new MinRuntime();
		BeanUtil.copyProperties(runtimeReq, minRuntime);
        minRuntime.setLocaltimeStr(new SimpleDateFormat("yyyy-MM-dd").format(runtimeReq.getLocaltime()));
        minRuntime.setTimeStr(new SimpleDateFormat("yyyy-MM-dd").format(runtimeReq.getTime()));
        String hourse = new SimpleDateFormat("HH").format(runtimeReq.getLocaltime());
        minRuntime.setHourse(hourse);
        Boolean isOk = iMinRuntimeMongoService.haveSameDataWithHourses(minRuntime);
        if (isOk){
            minRuntimeRepository.insert(minRuntime);
            return true;
        } else {
            return false;
        }

	}

	@Override
	public void insertOrUpdateRuntime() {
		// 从minElectricityStatistics表查询当天用电量
        List<RuntimeRsp> RuntimeList = iMinRuntimeMongoService.selectByCondition();
		LOGGER.info("insertOrUpdateRuntime.RuntimeList({})",RuntimeList);
		if (CollectionUtils.isNotEmpty(RuntimeList)) {
			List<DailyRuntime> dailylist = new ArrayList<>();
			List<WeeklyRuntime> weeklylist = new ArrayList<>();
			List<MonthlyRuntime> monthlylist = new ArrayList<>();
			RuntimeRsp runtimeRsp = RuntimeList.get(0);

			SimpleDateFormat format = new SimpleDateFormat("yyyy");
			String yearString = format.format(runtimeRsp.getDay());
			Integer year = Integer.valueOf(yearString);

			Date time = runtimeRsp.getDay();
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(time);
			Integer week = calendar.get(Calendar.WEEK_OF_YEAR);
			Integer month = calendar.get(Calendar.MONTH) + 1;

			for (RuntimeRsp rsp : RuntimeList) {
				List<WeeklyRuntime> rspWeeklylist = new ArrayList<>();
				List<MonthlyRuntime> rspMonthlylist = new ArrayList<>();

				// 数据插入daily_runtime
				DailyRuntime dailyRuntime = new DailyRuntime();
				BeanUtil.copyProperties(rsp, dailyRuntime);
				dailylist.add(dailyRuntime);
				// 数据插入weekly_runtime
				WeeklyRuntime weeklyRuntime = new WeeklyRuntime();
				BeanUtil.copyProperties(rsp, weeklyRuntime);
				weeklyRuntime.setYear(year);
				weeklyRuntime.setWeek(week);
                rspWeeklylist = iWeeklyRuntimeMongoService.selectByCondition(weeklyRuntime);

				if (CollectionUtils.isNotEmpty(rspWeeklylist)) {
					for (WeeklyRuntime weeklyRuntimes : rspWeeklylist) {
						Long runtime = rsp.getRuntime();
						runtime += weeklyRuntimes.getRuntime();
						weeklyRuntimes.setRuntime(runtime);
						weeklylist.add(weeklyRuntimes);
					}
				} else {
					weeklylist.add(weeklyRuntime);
				}
				// 数据插入monthly electricity statistics
				MonthlyRuntime monthlyRuntime = new MonthlyRuntime();
				BeanUtil.copyProperties(rsp, monthlyRuntime);
				monthlyRuntime.setYear(year);
				monthlyRuntime.setMonth(month);
                rspMonthlylist = iMonthlyRuntimeMongoService.selectByCondition(monthlyRuntime);

				if (CollectionUtils.isNotEmpty(rspMonthlylist)) {
					for (MonthlyRuntime monthlyRuntimes : rspMonthlylist) {
						Long runtime = rsp.getRuntime();
						runtime += monthlyRuntimes.getRuntime();
						monthlyRuntimes.setRuntime(runtime);
						monthlylist.add(monthlyRuntimes);
					}
				} else {
					monthlylist.add(monthlyRuntime);
				}
			}

			// 数据放入redis
			if (CollectionUtils.isNotEmpty(dailylist)) {
				Boolean isSuccess = false;// TODO 变成直接插入数据库
                isSuccess = iDailyRuntimeMongoService.insertOrUpdateBatch(dailylist);
				LOGGER.info("runtime.dailylist({})",dailylist);

			}
			if (CollectionUtils.isNotEmpty(weeklylist)) {
				Boolean isSuccess = false;
                isSuccess = iWeeklyRuntimeMongoService.insertOrUpdateBatch(weeklylist);
				LOGGER.info("runtime.weeklylist({})",weeklylist);

			}
			if (CollectionUtils.isNotEmpty(monthlylist)) {
				Boolean isSuccess = false;
                isSuccess = iMonthlyRuntimeMongoService.insertOrUpdateBatch(monthlylist);
				LOGGER.info("runtime.monthlylist({})",monthlylist);

			}

		}else {
			LOGGER.info("===========minRuntimeList is null==========", RuntimeList);
		}

	}

	@Override
	public RuntimeRsp selectRuntimeRspByDeviceAndUser(@RequestBody RuntimeReq2Runtime runtimeReq) {

        return iMinRuntimeMongoService.selectRuntimeRspByDeviceAndUser(runtimeReq);
	}

	@Override
	public void testJob() {
		LOGGER.info("*******************testJob*************************");

    }

    @Override
    public Map<String, Object> getEnergyTab(@Valid @RequestBody EnergyReq energyReq) {
        LOGGER.info("getEnergyTab {}", energyReq);
        AssertUtils.notEmpty(energyReq.getDevId(), "deviceId.notnull");
        Map<String, Object> dataMap = new HashMap<>();
        EnergyRsp energyRsp = new EnergyRsp();
        String statisticsType = energyReq.getStatisticsType();
        java.text.DecimalFormat df = new java.text.DecimalFormat("0.000");

        int offset = energyReq.getOffset();
        int pageSize = energyReq.getPageSize();
        int start = offset * pageSize;
        int end = offset * pageSize + pageSize;
        dataMap.put("offset", offset);
        dataMap.put("pageSize", pageSize);
        Integer isAdd = 0;

        // 获取当天到目前为止的电量
        ElectricityStatisticsRsp electricityRsp = selectElectricityRspByDeviceAndUser(energyReq);

        Date today = new Date();
        Double value = Double.valueOf(0);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat format = new SimpleDateFormat("yyyy");
        if (!StringUtils.isEmpty(electricityRsp)) {
            value = electricityRsp.getElectricValue();
        }
        if (value != null) {
            energyRsp.setValue(df.format(value));
        }
        String yearString = format.format(today);
        Integer year = Integer.valueOf(yearString);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(today);
        Integer weekOfYear = calendar.get(Calendar.WEEK_OF_YEAR);
        Integer monthOfYear = calendar.get(Calendar.MONTH) + 1;
        Integer week = calendar.get(Calendar.DAY_OF_WEEK);
        Integer month = calendar.get(Calendar.DAY_OF_MONTH);

        if (statisticsType.equals("day")) {
            //从数据库取数据，组装数据
            List<EnergyRsp> dailyList = new LinkedList<EnergyRsp>();
            Page<EnergyRsp> dailyPage = new Page<>(energyReq.getOffset(), energyReq.getPageSize());
            String dateStr = dateFormat.format(today);
            if (start == 0) {
                dailyPage = iDailyElectricityStatisticsMongoService.selectDailyElectricityRsp(energyReq);
                dailyList = dailyPage.getResult();
                if (dailyList.size() >= pageSize) {
                    Integer lastIndex = dailyList.size() - 1;
                    if (dailyList.size() == 0 || lastIndex <= 0) {
                        lastIndex = 0;
                    }
                    if (CollectionUtils.isNotEmpty(dailyList)) {
                        EnergyRsp lastlDay = dailyList.get(lastIndex);
                        dailyList.remove(lastlDay);
                    }
                }
                isAdd = 1;
                energyRsp.setTime(dateStr);
                dailyList.add(0, energyRsp);
                dailyPage.setResult(dailyList);
            } else {
                dailyPage = iDailyElectricityStatisticsMongoService.selectDailyElectricityRsp(energyReq);
            }
            dataMap.put("statisticsType", "day");
            dataMap.put("totalCount", dailyPage.getTotal() + isAdd);
            dataMap.put("energys", dailyPage.getResult());

        } else if (statisticsType.equals("week")) {

            List<EnergyRsp> weeklyList = new LinkedList<EnergyRsp>();
            Page<EnergyRsp> weeklyPage = new Page<>(energyReq.getOffset(), energyReq.getPageSize());
            if (start == 0) {
                String nowTime = year + "." + weekOfYear;
                // 是否是当周的第一天
                if (week == 1) {
                    isAdd = 1;
                    //从数据库取数据，组装数据
                    weeklyPage = iWeeklyElectricityStatisticsMongoService.selectWeeklyElectricityRsp(energyReq);
                    weeklyList = weeklyPage.getResult();
                    if (weeklyList.size() >= pageSize) {
                        Integer lastIndex = weeklyList.size() - 1;
                        if (weeklyList.size() == 0 || lastIndex <= 0) {
                            lastIndex = 0;
                        }
                        if (CollectionUtils.isNotEmpty(weeklyList)) {
                            EnergyRsp lastlyWeek = weeklyList.get(lastIndex);
                            weeklyList.remove(lastlyWeek);
                        }
                    }
                } else {
                    weeklyPage = iWeeklyElectricityStatisticsMongoService.selectWeeklyElectricityRsp(energyReq);
                    weeklyList = weeklyPage.getResult();
                    //本周电量
                    if (CollectionUtils.isNotEmpty(weeklyList)) {
                        EnergyRsp latelyWeek = weeklyList.get(0); //最近的一周
                        latelyWeek.setTime(year+"."+latelyWeek.getTime());
                        //如果获取列表大于单页数量，需将最后一个数删除拼装本周电量
                        if (weeklyList.size() >= pageSize) {
                            //移除最后一个电量，重新插入新电量
                            if (!nowTime.equals(latelyWeek.getTime())) {
                                isAdd = 1;
                                Integer lastIndex = weeklyList.size() - 1;
                                EnergyRsp lastlyWeek = weeklyList.get(lastIndex);
                                weeklyList.remove(lastlyWeek);
                            }
                        }
                        //如果最近周与本周时间相同，累加，
                        if (nowTime.equals(latelyWeek.getTime())) {
                            weeklyList.remove(latelyWeek); //移除最近周的数据
                            String lateValue = "0";
                            if (latelyWeek != null) {
                                lateValue = latelyWeek.getValue();
                            }
                            Double latelyWeekValue = lateValue != null ? Double.valueOf(lateValue) : 0;
                            if (value != null) {
                                latelyWeekValue = latelyWeekValue + value;
                            }
                            energyRsp.setValue(df.format(latelyWeekValue));
                        }
                    }
                }
                energyRsp.setTime(nowTime);
                weeklyList.add(0, energyRsp);
                weeklyPage.setResult(weeklyList);
            } else {
                weeklyPage = iWeeklyElectricityStatisticsMongoService.selectWeeklyElectricityRsp(energyReq);
            }
            dataMap.put("statisticsType", "week");
            dataMap.put("totalCount", weeklyPage.getTotal() + isAdd);
            dataMap.put("energys", weeklyPage.getResult());

        } else if (statisticsType.equals("month")) {
            //从数据库取数据，组装数据
            List<EnergyRsp> monthlyList = new LinkedList<EnergyRsp>();
            Page<EnergyRsp> monthlyPage = new Page<>(energyReq.getOffset(), energyReq.getPageSize());
            if (start == 0) {
                String nowTime = year + "-" + monthOfYear;
                // 是否是当月的第一天
                if (month == 1) {
                    //从数据库取数据，组装数据
                    monthlyPage = iMonthlyElectricityStatisticsMongoService.selectMonthlyElectricityRsp(energyReq);
                    isAdd = 1;
                    monthlyList = monthlyPage.getResult();
                    if (monthlyList.size() >= pageSize) {
                        Integer lastIndex = monthlyList.size() - 1;
                        if (monthlyList.size() == 0 || lastIndex <= 0) {
                            lastIndex = 0;
                        }
                        if (CollectionUtils.isNotEmpty(monthlyList)) {
                            EnergyRsp lastlyMonth = monthlyList.get(lastIndex);
                            monthlyList.remove(lastlyMonth);
                        }
                    }
                } else {
                    monthlyPage = iMonthlyElectricityStatisticsMongoService.selectMonthlyElectricityRsp(energyReq);
                    monthlyList = monthlyPage.getResult();
                    //本月电量
                    if (CollectionUtils.isNotEmpty(monthlyList)) {
                        EnergyRsp latelyMonth = monthlyList.get(0);//最近的一月
                        latelyMonth.setTime(year+"-"+latelyMonth.getTime());
                        //如果获取列表大于单页数量，需将最后一个数删除拼装本周电量
                        if (monthlyList.size() >= pageSize) {
                            if (!nowTime.equals(latelyMonth.getTime())) {
                                isAdd = 1;
                                Integer lastIndex = monthlyList.size() - 1;
                                EnergyRsp lastlyWeek = monthlyList.get(lastIndex);
                                monthlyList.remove(lastlyWeek);
                            }

                        }
                        if (nowTime.equals(latelyMonth.getTime())) {
                            monthlyList.remove(latelyMonth);
                            String lateValue = "0";
                            if (latelyMonth != null) {
                                lateValue = latelyMonth.getValue();
                            }
                            Double latelyMonthValue = lateValue != null ? Double.valueOf(lateValue) : 0;
                            if (value != null) {
                                latelyMonthValue = latelyMonthValue + value;
                            }
                            energyRsp.setValue(df.format(latelyMonthValue));
                        }
                    }
                }
                energyRsp.setTime(nowTime);
                monthlyList.add(0, energyRsp);
                monthlyPage.setResult(monthlyList);
            } else {
                monthlyPage = iMonthlyElectricityStatisticsMongoService.selectMonthlyElectricityRsp(energyReq);
            }
            dataMap.put("statisticsType", "month");
            dataMap.put("totalCount", monthlyPage.getTotal() + isAdd);
            dataMap.put("energys", monthlyPage.getResult());
        }

        return dataMap;
    }

    @Override
    public Map<String, Object> getRuntimeTab(@Valid @RequestBody RuntimeReq2Runtime runtimeReq) {
        LOGGER.info("getRuntimeTab {}", runtimeReq);
        Map<String, Object> dataMap = new HashMap<>();
        EnergyRsp energyRsp = new EnergyRsp();

        String statisticsType = runtimeReq.getStatisticsType();
        int offset = runtimeReq.getOffset();
        int pageSize = runtimeReq.getPageSize();
        int start = offset * pageSize;
        int end = offset * pageSize + pageSize;
        dataMap.put("offset", offset);
        dataMap.put("pageSize", pageSize);
        Integer isAdd = 0;
        // 获取当天到目前为止的电量
        RuntimeRsp runtimeRsp = selectRuntimeRspByDeviceAndUser(runtimeReq);

        Date today = new Date();
        Long value = 0l;
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat format = new SimpleDateFormat("yyyy");
        if (!StringUtils.isEmpty(runtimeRsp)) {
            value = runtimeRsp.getRuntime();
        }
        if (value != null) {
            energyRsp.setValue(value.toString());
        }
        String yearString = format.format(today);
        Integer year = Integer.valueOf(yearString);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(today);
        Integer weekOfYear = calendar.get(Calendar.WEEK_OF_YEAR);
        Integer monthOfYear = calendar.get(Calendar.MONTH) + 1;
        Integer week = calendar.get(Calendar.DAY_OF_WEEK);
        Integer month = calendar.get(Calendar.DAY_OF_MONTH);


        if (statisticsType.equals("day")) {
            List<EnergyRsp> dailyList = new LinkedList<EnergyRsp>();
            Page<EnergyRsp> weeklyPage = new Page<>(runtimeReq.getOffset(), runtimeReq.getPageSize());
            String dateStr = dateFormat.format(today);
            if (start == 0) {
                isAdd = 1;
                weeklyPage = iDailyRuntimeMongoService.selectDailyRuntimeRsp(runtimeReq);
                dailyList = weeklyPage.getResult();
                if (dailyList.size() >= pageSize) {
                    Integer lastIndex = dailyList.size() - 1;
                    if (dailyList.size() == 0 || lastIndex <= 0) {
                        lastIndex = 0;
                    }
                    if (CollectionUtils.isNotEmpty(dailyList)) {
                        EnergyRsp lastlyDay = dailyList.get(lastIndex);
                        dailyList.remove(lastlyDay);
                    }
                }
                energyRsp.setTime(dateStr);
                dailyList.add(0, energyRsp);
                weeklyPage.setResult(dailyList);
            } else {
                weeklyPage = iDailyRuntimeMongoService.selectDailyRuntimeRsp(runtimeReq);
            }
            dataMap.put("statisticsType", "day");
            dataMap.put("totalCount", weeklyPage.getTotal() + isAdd);
            dataMap.put("energys", weeklyPage.getResult());

        } else if (statisticsType.equals("week")) {
            List<EnergyRsp> weeklyList = new LinkedList<EnergyRsp>();
            Page<EnergyRsp> weeklyPage = new Page<>(runtimeReq.getOffset(), runtimeReq.getPageSize());
            if (start == 0) {
                String nowTime = year + "." + weekOfYear;
                // 是否是当周的第一天
                if (week == 1) {
                    //从数据库取数据，组装数据
                    weeklyPage = iWeeklyRuntimeMongoService.selectWeeklyRuntimeRsp(runtimeReq);
                    isAdd = 1;
                    weeklyList = weeklyPage.getResult();
                    if (weeklyList.size() >= pageSize) {
                        Integer lastIndex = weeklyList.size() - 1;
                        if (weeklyList.size() == 0 || lastIndex <= 0) {
                            lastIndex = 0;
                        }
                        if (CollectionUtils.isNotEmpty(weeklyList)) {
                            EnergyRsp lastlyWeek = weeklyList.get(lastIndex);
                            weeklyList.remove(lastlyWeek);
                        }
                    }
                } else {
                    weeklyPage = iWeeklyRuntimeMongoService.selectWeeklyRuntimeRsp(runtimeReq);
                    weeklyList = weeklyPage.getResult();
                    //本周电量
                    if (CollectionUtils.isNotEmpty(weeklyList)) {
                        EnergyRsp latelyWeek = weeklyList.get(0);
                        latelyWeek.setTime(year+"."+latelyWeek.getTime());
                        if (weeklyList.size() >= pageSize) {
                            if (!nowTime.equals(latelyWeek.getTime())) {
                                isAdd = 1;
                                Integer lastIndex = weeklyList.size() - 1;
                                EnergyRsp lastlyWeek = weeklyList.get(lastIndex);
                                weeklyList.remove(lastlyWeek);
                            }
                        }
                        if (nowTime.equals((latelyWeek.getTime()))) {
                            weeklyList.remove(latelyWeek);
                            String lateValue = "0";
                            if (latelyWeek != null) {
                                lateValue = latelyWeek.getValue();
                            }
                            Long latelyWeekValue = lateValue != null ? Long.valueOf(lateValue) : 0l;
                            if (value != null) {
                                latelyWeekValue = latelyWeekValue + value;
                            }
                            energyRsp.setValue(latelyWeekValue.toString());
                        }
                    }
                }
                energyRsp.setTime(nowTime);
                weeklyList.add(0, energyRsp);
                weeklyPage.setResult(weeklyList);
            } else {
                weeklyPage = iWeeklyRuntimeMongoService.selectWeeklyRuntimeRsp(runtimeReq);
            }
            dataMap.put("statisticsType", "week");
            dataMap.put("totalCount", weeklyPage.getTotal() + isAdd);
            dataMap.put("energys", weeklyPage.getResult());
        } else if (statisticsType.equals("month")) {
            //从数据库取数据，组装数据
            List<EnergyRsp> monthlyList = new LinkedList<EnergyRsp>();
            Page<EnergyRsp> monthlyPage = new Page<>(runtimeReq.getOffset(), runtimeReq.getPageSize());
            if (start == 0) {
                String nowTime = year + "-" + monthOfYear;
                // 是否是当月的第一天
                if (month == 1) {
                    //从数据库取数据，组装数据
                    isAdd = 1;
                    monthlyPage = iMonthlyRuntimeMongoService.selectMonthlyRuntimeRsp(runtimeReq);
                    monthlyList = monthlyPage.getResult();
                    if (monthlyList.size() >= pageSize) {
                        Integer lastIndex = monthlyList.size() - 1;
                        if (monthlyList.size() == 0 || lastIndex <= 0) {
                            lastIndex = 0;
                        }
                        if (CollectionUtils.isNotEmpty(monthlyList)) {
                            EnergyRsp lastlyMonth = monthlyList.get(lastIndex);
                            monthlyList.remove(lastlyMonth);
                        }
                    }
                } else {
                    monthlyPage = iMonthlyRuntimeMongoService.selectMonthlyRuntimeRsp(runtimeReq);
                    monthlyList = monthlyPage.getResult();

                    //本月电量
                    if (CollectionUtils.isNotEmpty(monthlyList)) {
                        EnergyRsp latelyMonth = monthlyList.get(0);
                        latelyMonth.setTime(year+"-"+latelyMonth.getTime());
                        if (monthlyList.size() >= pageSize) {
                            if (!nowTime.equals(latelyMonth.getTime())) {
                                isAdd = 1;
                                Integer lastIndex = monthlyList.size() - 1;
                                EnergyRsp lastlyWeek = monthlyList.get(lastIndex);
                                monthlyList.remove(lastlyWeek);
                            }

                        }
                        if (nowTime.equals(latelyMonth.getTime())) {
                            monthlyList.remove(latelyMonth);
                            String lateValue = "0";
                            if (latelyMonth != null) {
                                lateValue = latelyMonth.getValue();
                            }
                            Long latelyMonthValue = lateValue != null ? Long.valueOf(lateValue) : 0l;
                            if (value != null) {
                                latelyMonthValue = latelyMonthValue + value;
                            }
                            energyRsp.setValue(latelyMonthValue.toString());
                        }
                    }
                }
                energyRsp.setTime(year + "-" + monthOfYear);
                monthlyList.add(0, energyRsp);
                monthlyPage.setResult(monthlyList);
            } else {
                monthlyPage = iMonthlyRuntimeMongoService.selectMonthlyRuntimeRsp(runtimeReq);
            }

            dataMap.put("statisticsType", "month");
            dataMap.put("totalCount", monthlyPage.getTotal() + isAdd);
            dataMap.put("energys", monthlyPage.getResult());
        }
        return dataMap;
    }

    @Override
    public boolean insertDailyElectricityStatistics(@RequestBody List<DailyElectricityStatisticsReq> list) {
	    List<DailyElectricityStatistics> dailyList = Lists.newArrayList();
	    for (DailyElectricityStatisticsReq req : list) {
            DailyElectricityStatistics d = new DailyElectricityStatistics();
            BeanUtil.copyProperties(req, d);
            dailyList.add(d);
        }
        return iDailyElectricityStatisticsMongoService.insertOrUpdateBatch(dailyList);
    }

    @Override
    public boolean insertMonthElectricityStatistics(@RequestBody List<MonthlyElectricityStatisticsReq> list) {
        List<MonthlyElectricityStatistics> monthList = Lists.newArrayList();
        for (MonthlyElectricityStatisticsReq req : list) {
            MonthlyElectricityStatistics st = new MonthlyElectricityStatistics();
            BeanUtil.copyProperties(req, st);
            monthList.add(st);
        }
	    return iMonthlyElectricityStatisticsMongoService.insertOrUpdateBatch(monthList);
    }

    @Override
    public boolean insertMinElectricityStatistics(@RequestBody List<ElectricityStatisticsReq> list) {
	    List<MinElectricityStatistics> minList = Lists.newArrayList();
	    for (ElectricityStatisticsReq req : list) {
            MinElectricityStatistics minEle = new MinElectricityStatistics();
            BeanUtil.copyProperties(req, minEle);
            minEle.setLocaltimeStr(new SimpleDateFormat("yyyy-MM-dd").format(req.getLocaltime()));
            minEle.setTimeStr(new SimpleDateFormat("yyyy-MM-dd").format(req.getTime()));
            minList.add(minEle);
        }
        return iMinElectricityStatisticsMongoService.insertOrUpdateBatch(minList);
    }

    @Override
    public List<ElectricityStatisticsRsp> getMinListByReq(@RequestBody ElectricityStatisticsReq req) {
        return iMinElectricityStatisticsMongoService.selectByReq(req);
    }

    @Override
    public List<DailyElectricityStatisticsResp> getDailyListByReq(@RequestBody DailyElectricityStatisticsReq req) {
        return iDailyElectricityStatisticsMongoService.selectListByReq(req);
    }

    @Override
    public List<MonthlyElectricityStatisticsResp> getMonthlyListByReq(@RequestBody MonthlyElectricityStatisticsReq req) {
        return iMonthlyElectricityStatisticsMongoService.selectListByReq(req);
    }


}
