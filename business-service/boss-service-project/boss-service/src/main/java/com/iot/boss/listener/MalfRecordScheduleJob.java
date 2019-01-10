package com.iot.boss.listener;

import java.text.SimpleDateFormat;
import java.util.List;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.iot.boss.entity.MalfRecord;
import com.iot.boss.service.malf.MalfRecordService;
import com.iot.boss.service.malf.WarningDispatchService;
import com.iot.common.util.CalendarUtil;

/**
 * 
 * 项目名称：立达信IOT云平台
 * 模块名称：报障处理进度定时处理
 * 功能描述：报障处理进度定时处理
 * 创建人： 李帅
 * 创建时间：2018年5月17日 下午6:29:20
 * 修改人：李帅
 * 修改时间：2018年5月17日 下午6:29:20
 */
public class MalfRecordScheduleJob implements Job {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	//0：及时  	1：延迟  2：严重延迟  3紧急
	private final int MALFSTATUS0 = 0;
	private final int MALFSTATUS1 = 1;
	private final int MALFSTATUS2 = 2;
	private final int MALFSTATUS3 = 3;
	
	WarningDispatchService warningDispatchService;;
	
	MalfRecordService malfRecordService;
	
	/**
	 * 
	 * 描述：报障处理进度定时处理
	 * @author 李帅
	 * @created 2018年5月17日 下午6:29:40
	 * @since 
	 * @param jobExecutionContext
	 * @throws JobExecutionException
	 */
	@Override
	public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
		warningDispatchService = (WarningDispatchService)(jobExecutionContext.getMergedJobDataMap().get("warningDispatchService"));
		malfRecordService = (MalfRecordService)(jobExecutionContext.getMergedJobDataMap().get("malfRecordService"));
		try {
			logger.info("MalfRecordScheduleJob Start");
			//获取未分配报障单信息
			List<MalfRecord> unassignedMalfRecord = malfRecordService.getUnassignedMalfRecord();
			for(MalfRecord malfRecord : unassignedMalfRecord) {
				warningDispatchService.addWarning(malfRecord.getId(), CalendarUtil.format(CalendarUtil.getNowTime(),"yyyy-MM-dd HH:mm:ss").getTime());
			}
			
			//获取未定级报障单信息
			List<MalfRecord> unclassifiedMalfRecord = malfRecordService.getUnclassifiedMalfRecord();
			for(MalfRecord malfRecord : unclassifiedMalfRecord) {
				if(malfRecord.getHandleStartTime() == null) {
					if(malfRecord.getMalfStatus() == null || malfRecord.getMalfStatus() != 3){
						//更新报障记录报障处理进度-3：紧急
						malfRecordService.updateRecordMalfStatus(malfRecord.getId(), MALFSTATUS3);
					}
				}else {
					String nowTime = CalendarUtil.getNowTime(CalendarUtil.YYYYMMDDHHMMSS);
					SimpleDateFormat formatter = new SimpleDateFormat(CalendarUtil.YYYYMMDDHHMMSS);
					String dateString = formatter.format(malfRecord.getHandleStartTime());
					long hours  =   CalendarUtil.getDistanceHours(dateString,nowTime);
					if(hours >= 1 && malfRecord.getMalfStatus() != 3){
						//更新报障记录报障处理进度-3：紧急
						malfRecordService.updateRecordMalfStatus(malfRecord.getId(), MALFSTATUS3);
					}
				}
				
			}
			
			//获取处理中报障单信息
			List<MalfRecord> processingMalfRecord = malfRecordService.getProcessingMalfRecord();
			for(MalfRecord malfRecord : processingMalfRecord) {
				String nowTime = CalendarUtil.getNowTime(CalendarUtil.YYYYMMDDHHMMSS);
				SimpleDateFormat formatter = new SimpleDateFormat(CalendarUtil.YYYYMMDDHHMMSS);
				String dateString = formatter.format(malfRecord.getHandleStartTime());
                long days = CalendarUtil.getDistanceDays(dateString, nowTime);
                if(days <= 5){
                	//更新报障记录报障处理进度-0：及时
                	if(malfRecord.getMalfStatus() == null || malfRecord.getMalfStatus() != 0) {
                		malfRecordService.updateRecordMalfStatus(malfRecord.getId(), MALFSTATUS0);
                	}
				}else if(5 < days && days <= 12){
					//更新报障记录报障处理进度-1：延迟
					if(malfRecord.getMalfStatus() == null || malfRecord.getMalfStatus() != 1) {
						malfRecordService.updateRecordMalfStatus(malfRecord.getId(), MALFSTATUS1);
					}
				}else{
					//更新报障记录报障处理进度-2：严重延迟
					if(malfRecord.getMalfStatus() == null || malfRecord.getMalfStatus() != 2) {
						malfRecordService.updateRecordMalfStatus(malfRecord.getId(), MALFSTATUS2);
					}
				}
			}
			logger.info("MalfRecordScheduleJob End");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
