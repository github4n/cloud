package com.iot.device.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.iot.device.api.DeviceTypeDataPointApi;
import com.iot.device.model.DeviceTypeDataPoint;
import com.iot.device.service.IDeviceTypeDataPointService;
import com.iot.device.vo.req.DeviceType2PointsReq;
import com.iot.device.vo.rsp.DeviceType2PointsRes;
import com.iot.util.AssertUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 创建人：chenxiaolin
 * 创建时间：2018年5月3日 上午11:26:26
 * 修改人： chenxiaolin
 * 修改时间：2018年5月3日 上午11:26:26
 */
@RestController
public class DeviceTypeDataPointController implements DeviceTypeDataPointApi {

	@Autowired
	private IDeviceTypeDataPointService deviceTypeDataPointService;
	
	/**
	 * override说明：
	 * @author chenxiaolin
	 */
	@Override
	public boolean typeMap2Point(@RequestBody DeviceType2PointsReq req) {
		Long userId = 1l;//SaaSContextHolder.getCurrentUserId();
		Long deviceTypeId = req.getDeviceTypeId();
		List<Long> pointIds = req.getPointIds();
		AssertUtils.notEmpty(deviceTypeId, "deviceTypeId.notnull");
		AssertUtils.notEmpty(pointIds, "datapointIds.notnull");

		List<DeviceTypeDataPoint> ins = new ArrayList<>();
		pointIds.forEach((id) -> {
			EntityWrapper<DeviceTypeDataPoint> q = new EntityWrapper<DeviceTypeDataPoint>();
			q.eq("device_type_id", deviceTypeId).eq("data_point_id", id);
			if (deviceTypeDataPointService.selectCount(q) < 1) {
				DeviceTypeDataPoint dp = new DeviceTypeDataPoint();
				dp.setDeviceTypeId(deviceTypeId);
				dp.setDataPointId(id);
				dp.setCreateBy(userId);
				dp.setCreateTime(new Date());
				dp.setTenantId(req.getTenantId());
				ins.add(dp);
			}
		});
		
		if (!ins.isEmpty()) {
			return deviceTypeDataPointService.insertBatch(ins);
		}
		return false;
	}

	/**
	 * override说明：
	 * @author chenxiaolin
	 */
	@Override
	public DeviceType2PointsRes getPointsByDeviceTypeId(@RequestParam Long deviceTypeId) {
		EntityWrapper<DeviceTypeDataPoint> q = new EntityWrapper<DeviceTypeDataPoint>();
		q.eq("device_type_id", deviceTypeId);
		DeviceType2PointsRes res = new DeviceType2PointsRes();
		List<Long> ids = new ArrayList<Long>();
		res.setDeviceTypeId(deviceTypeId);

		List<DeviceTypeDataPoint> points = deviceTypeDataPointService.selectList(q);
		if (!CollectionUtils.isEmpty(points))
			points.forEach((p) -> {
				ids.add(p.getDataPointId());
			});
		res.setPointIds(ids);
		return res;
	}

}
