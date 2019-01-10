package com.iot.building.warning.api.callback;

import java.util.List;

import org.springframework.stereotype.Component;

import com.iot.building.reservation.vo.ReservationReq;
import com.iot.building.reservation.vo.ReservationResp;
import com.iot.building.warning.api.WarningApi;
import com.iot.building.warning.vo.WarningReq;
import com.iot.building.warning.vo.WarningResp;
import com.iot.common.exception.BusinessException;
import com.iot.common.helper.Page;

import feign.hystrix.FallbackFactory;

/**
 * @Author: Xieby
 * @Date: 2018/9/4
 * @Description: *
 */
@Component
public class WarningApiFallbackFactory implements FallbackFactory<WarningApi> {


    @Override
    public WarningApi create(Throwable throwable) {
        return new WarningApi() {

			@Override
			public int countWarningById(Long id) throws Exception {
				return 0;
			}

			@Override
			public WarningResp addWarning(WarningReq warning) throws BusinessException {
				return null;
			}

			@Override
			public int updateWarningStatus(WarningReq warning) throws BusinessException {
				return 0;
			}

			@Override
			public List<WarningResp> findWarningList(WarningReq warning) throws BusinessException {
				return null;
			}

			@Override
			public Page<WarningResp> findHistoryWarningList(String pageNum, String pageSize, String eventType,
					String timeType, Long tenantId, Long orgId, Long locationId) throws BusinessException {
				return null;
			}

			@Override
			public List<WarningResp> findUnreadWarningList(Long tenantId, Long orgId, Long locationId)
					throws BusinessException {
				return null;
			}

			@Override
			public List<WarningResp> findHistoryWarningListNoPage(String eventType, String count, Long tenantId,
					Long orgId, Long locationId) {
				return null;
			}

		};
    }
}
