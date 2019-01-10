package com.iot.building.warning.service;

import java.util.List;

import com.iot.building.warning.vo.WarningReq;
import com.iot.building.warning.vo.WarningResp;
import com.iot.common.exception.BusinessException;
import com.iot.common.helper.Page;


public interface IWarningService {
	
	int countWarningById(Long id) throws Exception;
	
	public Page<WarningResp> findHistoryWarningList(String pageNum, String pageSize,String eventType,String timeType,
			Long tenantId,Long orgId,Long locationId) throws BusinessException;
	
    /**
	 * 描述：查询未读告警数据
	 *
	 * @return
	 * @throws BusinessException
	 * @since
	 */
	public List<WarningResp> findUnreadWarningList(Long tenantId,Long orgId,Long locationId) throws BusinessException;

    /**
     * 描述：插入告警数据
     *
     * @param warning
     * @throws BusinessException
     * @author zhouzongwei
     * @created 2017年11月30日 下午3:00:00
     * @since
     */
    public WarningResp addWarning(WarningReq warning) throws BusinessException;

    public int updateWarningStatus(WarningReq warning) throws BusinessException;

    List<WarningResp> findHistoryWarningListNoPage(String eventType,String count,Long tenantId,Long orgId,Long locationId);

    List<WarningResp> findWarningListByCondition(WarningReq warning);
}
