package com.iot.building.allocation.service.impl;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.iot.building.allocation.mapper.ActivityRecordMapper;
import com.iot.building.allocation.service.ActivityRecordService;
import com.iot.building.allocation.vo.ActivityRecordReq;
import com.iot.building.allocation.vo.ActivityRecordResp;
import com.iot.building.excepiton.BusinessExceptionEnum;
import com.iot.building.helper.PageUtils;
import com.iot.common.exception.BusinessException;
import com.iot.common.helper.Page;

@Service
@Transactional
public class ActivityRecordServiceImpl implements ActivityRecordService {

    private final static Logger logger = LoggerFactory.getLogger(ActivityRecordServiceImpl.class);

    @Autowired
    private ActivityRecordMapper activityRecordMapper;

	/*@Override
	public int saveActivityRecord(String type, String icon, String activity, String foreignId, Long createBy) {
		ActivityRecord ar = new ActivityRecord();
		ar.setIcon(icon);
		ar.setActivity(activity);
		ar.setTime(new Date());
		ar.setType(type);
		ar.setForeignId(foreignId);
		ar.setCreateBy(createBy);
		
		int rec = activityRecordMapper.insertSelective(ar);
		
		return rec;
	}*/
    public int saveActivityRecord(ActivityRecordReq activityRecordReq) {
        if (activityRecordReq == null) {
            logger.error("----saveActivityRecord() error! activityRecordReq is null");
            throw new BusinessException(BusinessExceptionEnum.COMMOMN_EXCEPTION);
        }
        int rec = activityRecordMapper.insertSelective(activityRecordReq);
        return rec;
    }

    @Override
    public int delActivityRecord(ActivityRecordReq activityRecordReq) {
        int rec = activityRecordMapper.deleteByCondition(activityRecordReq);
        return rec;
    }

    @Override
    public Page<ActivityRecordResp> queryActivityRecord(ActivityRecordReq activityRecordReq) {
        PageHelper.startPage(activityRecordReq.getPageNum(), activityRecordReq.getPageSize());
        List<ActivityRecordResp> activityRecordRespList = activityRecordMapper.queryByCondition(activityRecordReq);
        PageInfo<ActivityRecordResp> pageInfo = new PageInfo(activityRecordRespList);
        return new PageUtils().buildPage(pageInfo);
    }

	@Override
	public List<ActivityRecordResp> queryActivityRecordByCondition(ActivityRecordReq activityRecordReq) {
		List<ActivityRecordResp> list = activityRecordMapper.queryByCondition(activityRecordReq);
		return list;
	}

	@Override
	public List<ActivityRecordResp> queryByValidityDate(Long spaceId,String templateId, String type, Long startTime,
			Long endTime,Long spaceTemplateId) {
		return activityRecordMapper.queryByValidityDate(spaceId,templateId, type, startTime, endTime,spaceTemplateId);
	}

}
