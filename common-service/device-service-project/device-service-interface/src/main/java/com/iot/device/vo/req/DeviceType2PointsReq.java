package com.iot.device.vo.req;

import lombok.Data;
import lombok.ToString;

import java.util.List;

@Data
@ToString
public class DeviceType2PointsReq {

	private Long deviceTypeId;

	private List<Long> pointIds;
	/**
	 * 租户ID
	 */
	private Long tenantId;

}
