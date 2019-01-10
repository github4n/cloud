package com.iot.video.service;

import com.iot.video.dto.PlanInfoDto;
import com.iot.video.dto.PlanOrderParamDto;
import com.iot.video.dto.VideoPackageDto;
import com.iot.video.entity.VideoPayRecord;
import com.iot.video.entity.VideoPlan;

/**
 *
 * 项目名称：IOT云平台
 * 模块名称：
 * 功能描述：
 * 创建人：
 * 创建时间：2018/3/19 10:56
 * 修改人：
 * 修改时间：2018/3/19 10:56
 * 修改描述：
 */
public interface VideoPlanService {

	/**
	 * 描述：修改录影排序
	 * @author mao2080@sina.com
	 * @created 2018/3/23 14:38
	 * @param poDto 排序对象
	 * @return void
	 */
	void updatePlanOrder(PlanOrderParamDto poDto);

	/**
	 * 
	 * 描述：根据设备id查询计划类型
	 * @author 李帅
	 * @created 2018年4月27日 下午4:03:52
	 * @since 
	 * @param tenantId
	 * @param userId
	 * @param deviceId
	 * @return com.lds.iot.video.dto.PlanInfoDto
	 */
	PlanInfoDto getPlanType(Long tenantId, String userId, String deviceId);

	/**
	 * @despriction：获取video_plan详情
	 * @author  yeshiyuan
	 * @created 2018/5/17 15:37
	 * @param planId 计划id
	 * @param userId 用户uuid
	 * @return
	 */
	VideoPlan getVideoPlan(String planId, String userId);

	/**
	 * 描述：查询套餐Id
	 * @author nongchongwei
	 * @date 2018/7/17 17:10
	 * @param
	 * @return
	 */
	Long getPackageIdByPlanId(String planId);


	/**
	  * @despriction：获取最新一条计划购买记录
	  * @author  yeshiyuan
	  * @created 2018/7/18 9:43
	  * @param planId 计划id
	  * @return
	  */
	VideoPayRecord getLastVideoPayRecord(String planId);


	/**
	 * @despriction：查找计划绑定的用户id
	 * @author  yeshiyuan
	 * @created 2018/8/13 17:05
	 * @param null
	 * @return
	 */
	String getUserIdByPlanId(String planId);


}