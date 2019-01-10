package com.iot.boss.service.technicalrelate;

import com.iot.boss.vo.technicalrelate.BossNetworkTypeInfoResp;
import com.iot.boss.vo.technicalrelate.TechnicalAndNetworkResp;
import com.iot.boss.vo.technicalrelate.TechnicalListResp;
import com.iot.common.helper.Page;
import com.iot.device.dto.NetworkTypeDto;
import com.iot.device.vo.NetworkTypeVo;
import com.iot.device.vo.req.NetworkTypePageReq;
import com.iot.device.vo.req.technical.SaveDeviceTechnicalReq;
import com.iot.device.vo.rsp.NetworkTypeResp;
import com.iot.system.vo.DictItemResp;

import java.util.List;

/**
 * 
 * 项目名称：立达信IOT云平台
 * 模块名称：技术方案相关功能
 * 功能描述：技术方案相关功能
 * 创建人： 李帅
 * 创建时间：2018年10月15日 上午10:02:08
 * 修改人：李帅
 * 修改时间：2018年10月15日 上午10:02:08
 */
public interface TechnicalRelateService {

    /**
     * 
     * 描述：查询配网模式信息
     * @author 李帅
     * @created 2018年10月15日 上午10:01:20
     * @since 
     * @param networkTypeId
     * @return
     */
	NetworkTypeDto getNetworkInfo(Long networkTypeId);

	/**
	 * 
	 * 描述：更新配网模式信息
	 * @author 李帅
	 * @created 2018年10月15日 上午10:05:00
	 * @since 
	 * @param networkTypeVo
	 */
	void updateNetworkInfo(NetworkTypeVo networkTypeVo);

	/**
	  * @despriction：分页查询配网类型
	  * @author  yeshiyuan
	  * @created 2018/10/15 17:26
	  * @return
	  */
	Page<BossNetworkTypeInfoResp> queryNetworkType(NetworkTypePageReq pageReq);

	/**
	  * @despriction：加载所有的技术方案
	  * @author  yeshiyuan
	  * @created 2018/10/15 17:44
	  * @return
	  */
	List<TechnicalListResp> getAllTechnical();

	/**
	  * @despriction：保存设备支持的技术方案
	  * @author  yeshiyuan
	  * @created 2018/10/15 19:44
	  * @return
	  */
	void saveDeviceTechnical(SaveDeviceTechnicalReq req);

	/**
	 * @despriction：查询设备支持的技术方案
	 * @author  yeshiyuan
	 * @created 2018/10/15 20:10
	 * @return
	 */
	TechnicalAndNetworkResp queryTechnicalAndNetwork(Long deviceTypeId) ;

	/**
	  * @despriction：查询设备类型支持的配网模式
	  * @author  yeshiyuan
	  * @created 2018/10/16 10:41
	  * @return
	  */
	List<NetworkTypeResp> deviceSupportNetwork(Long deviceTypeId);

	/**
	  * @despriction：技术方案支持的配网模式
	  * @author  yeshiyuan
	  * @created 2018/10/16 16:15
	  * @return
	  */
	List<NetworkTypeResp> findNetworkByTechnicalIds(List<Long> technicalIds);

	/**
	 * 描述：加载所有配网code
	 * @author maochengyuan
	 * @created 2018/11/22 19:58
	 * @param
	 * @return java.util.List<com.iot.system.vo.DictItemResp>
	 */
	List<DictItemResp> getAllTypeCode();

	/**
	 * @despriction：加载设备类型支持的技术方案
	 * @author  yeshiyuan
	 * @created 2018/10/16 11:41
	 * @return
	 */
	List<TechnicalListResp> getDeviceTechnical(Long deviceTypeId);

}
