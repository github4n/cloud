package com.iot.boss.restful.malf;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.github.pagehelper.PageInfo;
import com.iot.boss.api.MalfAttendanceApi;
import com.iot.boss.dto.MalfAttendanceParam;
import com.iot.boss.dto.MalfAttendanceTimerDto;
import com.iot.boss.dto.MalfParam;
import com.iot.boss.service.malf.MalfAttendanceService;

/** 
 * 
 * 项目名称：IOT云平台 
 * 模块名称：
 * 功能描述：
 * 创建人：
 * 创建时间：2018/3/19 10:47
 * 修改人：
 * 修改时间：2018/3/19 10:47
 * 修改描述：
 */
@RestController
public class MalfAttendanceRestful implements MalfAttendanceApi {

	@Autowired
    private MalfAttendanceService malfAttendanceService;
	
	/**
	 * 
	 * 描述：查询管理员值班
	 * @author 李帅
	 * @created 2018年5月15日 下午8:20:10
	 * @since 
	 * @param malfParam
	 * @return
	 */
	public PageInfo<MalfAttendanceTimerDto> getMalfAttendance(@RequestBody MalfParam malfParam){
		return malfAttendanceService.getMalfAttendance(malfParam);
	}
	
	/**
	 * 
	 * 描述：设置管理员值班
	 * @author 李帅
	 * @created 2018年5月15日 下午8:21:27
	 * @since 
	 * @param malfAttendanceParam
	 */
	public void setMalfAttendance(@RequestBody MalfAttendanceParam malfAttendanceParam){
		malfAttendanceService.setMalfAttendance(malfAttendanceParam);
	}
	
	/**
	 * 
	 * 描述：删除管理员值班
	 * @author 李帅
	 * @created 2018年5月15日 下午6:38:15
	 * @since 
	 * @param malfAttendanceId
	 */
	public void deleteMalfAttendance(@RequestParam("malfAttendanceId") String malfAttendanceId){
		malfAttendanceService.deleteMalfAttendance(malfAttendanceId);
	}
}