package com.iot.device.service;

import com.iot.common.exception.BusinessException;
import com.iot.device.dto.NetworkTypeDto;
import com.iot.device.vo.NetworkTypeVo;
import com.iot.device.vo.req.technical.SaveDeviceTechnicalReq;
import com.iot.device.vo.rsp.NetworkTypeResp;

import java.util.List;

public interface ITechnicalRelateService {

    /**
     * 描述：查询UUID申请订单
     * @author shenxiang
     * @created 2018年6月29日 下午1:59:27
     * @param uuidOrderReq
     * @return
     * @throws BusinessException
     */
	NetworkTypeDto getNetworkInfo(Long networkTypeId) throws BusinessException;

	/**
	 * 
	 * 描述：更新配网模式信息
	 * @author 李帅
	 * @created 2018年10月12日 下午5:22:29
	 * @since 
	 * @param networkTypeVo
	 * @throws BusinessException
	 */
	void updateNetworkInfo(NetworkTypeVo networkTypeVo) throws BusinessException;

	/**
	 * @despriction：保存设备支持的技术方案
	 * @author  yeshiyuan
	 * @created 2018/10/15 19:18
	 * @return
	 */
	void saveDeviceTechnical(SaveDeviceTechnicalReq saveDeviceTechnicalReq);

	/**
	  * @despriction：查询设备类型支持的技术方案
	  * @author  yeshiyuan
	  * @created 2018/10/15 20:04
	  * @return
	  */
	List<Long> queryDeviceTechnical(Long deviceTypeId);

	/**
	 * @despriction：设备支持的配网类型
	 * @author  yeshiyuan
	 * @created 2018/10/15 20:31
	 * @return
	 */
	List<NetworkTypeResp> deviceSupportNetworkType(Long deviceTypeId);

	/**
	 * @despriction：方案支持的配网类型
	 * @author  yeshiyuan
	 * @created 2018/10/15 20:56
	 * @return
	 */
	List<NetworkTypeResp> technicalSupportNetworkType(Long technicalId);

	/**
	 * @despriction：方案支持的配网类型
	 * @author  yeshiyuan
	 * @created 2018/10/16 17:03
	 * @return
	 */
	List<NetworkTypeResp> findNetworkByTechnicalIds(List<Long> technicalIds);

	/**
	 * @despriction：查看设备类型某种方案支持的配网类型
	 * @author  yeshiyuan
	 * @created 2018/12/11 15:39
	 */
	List<NetworkTypeResp> deviceSupportNetwrokByTechnicalId(Long deviceTypeId, Long technicalId);
}
