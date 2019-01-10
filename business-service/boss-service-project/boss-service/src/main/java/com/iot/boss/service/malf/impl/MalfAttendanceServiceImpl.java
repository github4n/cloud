package com.iot.boss.service.malf.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.iot.boss.dao.malf.MalfAttendanceMapper;
import com.iot.boss.dto.MalfAttendanceParam;
import com.iot.boss.dto.MalfAttendanceTimerDto;
import com.iot.boss.dto.MalfParam;
import com.iot.boss.entity.MalfAttendanceTimer;
import com.iot.boss.service.malf.MalfAttendanceService;
import com.iot.boss.service.malf.WarningDispatchService;
import com.iot.boss.util.CommonUtil;

/**
 * 项目名称：cloud
 * 模块名称：
 * 功能描述：
 * 创建人： yeshiyuan
 * 创建时间：2018/5/15 14:10
 * 修改人： yeshiyuan
 * 修改时间：2018/5/15 14:10
 * 修改描述：
 */
@Service
public class MalfAttendanceServiceImpl implements MalfAttendanceService{
	
    @Autowired
    private MalfAttendanceMapper malfAttendanceMapper;
    
    @Autowired
    private WarningDispatchService warningDispatchService;

    /**
     * 
     * 描述：查询管理员值班
     * @author 李帅
     * @created 2018年5月15日 下午8:20:32
     * @since 
     * @param malfParam
     * @return
     */
    @Override
    public PageInfo<MalfAttendanceTimerDto> getMalfAttendance(MalfParam malfParam){
    	Integer pageNum = malfParam.getPageNum();
		Integer pageSize = malfParam.getPageSize();
		if (null == pageNum) {
            pageNum = 0;
        }
        if (null == pageSize || 0 == pageSize) {
            pageSize=10;
        }
        PageHelper.startPage(pageNum,pageSize,true);
        
        List<MalfAttendanceTimer> malfAttendanceTimerList = malfAttendanceMapper.getMalfAttendance(malfParam.getExecutionCycle());
        List<MalfAttendanceTimerDto> malfAttendanceTimerDtoList = new ArrayList<MalfAttendanceTimerDto>();
        MalfAttendanceTimerDto malfAttendanceTimerDto = null;
        for(MalfAttendanceTimer malfAttendanceTimer : malfAttendanceTimerList) {
        	malfAttendanceTimerDto = new MalfAttendanceTimerDto();
        	BeanUtils.copyProperties(malfAttendanceTimer, malfAttendanceTimerDto);
        	malfAttendanceTimerDto.setId(CommonUtil.encryptId(malfAttendanceTimer.getId()));
        	malfAttendanceTimerDto.setAdminId(CommonUtil.encryptId(malfAttendanceTimer.getAdminId()));
        	malfAttendanceTimerDtoList.add(malfAttendanceTimerDto);
        }
        PageInfo<MalfAttendanceTimerDto> pageInfo = new PageInfo<MalfAttendanceTimerDto>(malfAttendanceTimerDtoList);
        return pageInfo;
    }
    
    /**
     * 
     * 描述：设置管理员值班
     * @author 李帅
     * @created 2018年5月15日 下午8:21:48
     * @since 
     * @param malfAttendanceParam
     */
    @Override
    public void setMalfAttendance(MalfAttendanceParam malfAttendanceParam){
    	if("update".equals(malfAttendanceParam.getOperateType())) {
    		MalfAttendanceTimer malfAttendanceTimer = new MalfAttendanceTimer();
    		BeanUtils.copyProperties(malfAttendanceParam, malfAttendanceTimer);
    		malfAttendanceTimer.setAdminId(CommonUtil.decryptId(malfAttendanceParam.getAdminId()));
    		malfAttendanceTimer.setId(CommonUtil.decryptId(malfAttendanceParam.getMalfAttendanceId()));
    		malfAttendanceMapper.updateMalfAttendance(malfAttendanceTimer);
    		//更新Redis
    		warningDispatchService.updateOrDelete(malfAttendanceTimer, "update");
    	}else if("add".equals(malfAttendanceParam.getOperateType())) {
    		MalfAttendanceTimer malfAttendanceTimer = new MalfAttendanceTimer();
    		BeanUtils.copyProperties(malfAttendanceParam, malfAttendanceTimer);
//    		malfAttendanceTimer.setId(RedisCacheUtil.incr(ModuleConstants.DB_TABLE_MALF_ATTENDANCE_TIMER, 0L));
    		malfAttendanceTimer.setAdminId(CommonUtil.decryptId(malfAttendanceParam.getAdminId()));
    		malfAttendanceTimer.setTimerStatus(0);
    		malfAttendanceMapper.addMalfAttendance(malfAttendanceTimer);
    	}
    }
    
    /**
     * 
     * 描述：删除管理员值班
     * @author 李帅
     * @created 2018年5月15日 下午6:38:29
     * @since 
     * @param malfAttendanceId
     */
    @Override
    public void deleteMalfAttendance(String malfAttendanceId){
    	Long malfId = CommonUtil.decryptId(malfAttendanceId);
        
        MalfAttendanceTimer malfAttendanceTimer = malfAttendanceMapper.getMalfAttendanceById(malfId);
        if(malfAttendanceTimer != null) {
        	//更新Redis
    		warningDispatchService.updateOrDelete(malfAttendanceTimer, "delete");
    		//删除值班数据
    		malfAttendanceMapper.deleteMalfAttendance(malfId);
        }
    }
}
