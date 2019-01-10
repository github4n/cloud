package com.iot.boss.service.uuid;

import com.iot.common.exception.BusinessException;
import com.iot.common.helper.Page;
import com.iot.device.dto.UUIDOrderDto;
import com.iot.device.vo.req.LicenseUsageReq;
import com.iot.device.vo.req.UserLoginReq;
import com.iot.device.vo.req.uuid.GetUUIDOrderReq;
import com.iot.device.vo.req.uuid.UuidApplyForBoss;

/**
 * 描述：租户管理
 * 创建人： LaiGuiMing
 * 创建时间： 2018/5/22 17:04
 */
public interface UUIDManegerService {

    /**
     * 
     * 描述：UUID生成
     * @author 李帅
     * @created 2018年7月11日 下午3:19:46
     * @since 
     * @param uuidApplyForBoss
     * @return
     */
	Long generateUUID(UuidApplyForBoss uuidApplyForBoss);

	/**
	 *
	 * 描述：UUID生成列表
	 * @author 李帅
	 * @created 2018年5月25日 下午5:48:15
	 * @since
	 * @param custUuidManageReq
	 * @return
	 */
	Page<UUIDOrderDto> getUUIDGenerateList(GetUUIDOrderReq uuidOrderReq);

	/**
	 *
	 * 描述：通过批次号获取证书下载URL
	 * @author 李帅
	 * @created 2018年6月8日 下午1:51:13
	 * @since
	 * @param batchNum
	 * @return
	 */
	String getDownloadUUID(Long batchNum);


	/**
	 * 描述：管理员登陆
	 *
	 * @param userLoginReq
	 * @author 李帅
	 * @created 2018年6月8日 下午2:37:31
	 * @since
	 */
	void userLogin(UserLoginReq userLoginReq);

	/**
	 * 描述：上报总装测试数据
	 *
	 * @param licenseUsageReq
	 * @author chq
	 * @created 2018年6月28日 下午2:37:31
	 * @since
	 */
	void licenseUsage(LicenseUsageReq licenseUsageReq) throws BusinessException;
}
