package com.iot.boss.service.malf;

import com.github.pagehelper.PageInfo;
import com.iot.boss.dto.MalfAttendanceParam;
import com.iot.boss.dto.MalfAttendanceTimerDto;
import com.iot.boss.dto.MalfParam;

/**
 * 项目名称：cloud
 * 模块名称：
 * 功能描述：
 * 创建人： yeshiyuan
 * 创建时间：2018/5/15 14:09
 * 修改人： yeshiyuan
 * 修改时间：2018/5/15 14:09
 * 修改描述：
 */
public interface MalfAttendanceService {
	
	/**
	 * 
	 * 描述：查询管理员值班
	 * @author 李帅
	 * @created 2018年5月15日 下午8:20:23
	 * @since 
	 * @param malfParam
	 * @return
	 */
    public PageInfo<MalfAttendanceTimerDto> getMalfAttendance(MalfParam malfParam);
    
	/**
	 * 
	 * 描述：设置管理员值班
	 * @author 李帅
	 * @created 2018年5月15日 下午8:21:38
	 * @since 
	 * @param malfAttendanceParam
	 */
    public void setMalfAttendance(MalfAttendanceParam malfAttendanceParam);
    
    /**
     * 
     * 描述：删除管理员值班
     * @author 李帅
     * @created 2018年5月15日 下午6:38:21
     * @since 
     * @param malfAttendanceId
     */
    public void deleteMalfAttendance(String malfAttendanceId);
}
