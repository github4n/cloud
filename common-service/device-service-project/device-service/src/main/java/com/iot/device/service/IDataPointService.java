package com.iot.device.service;

import com.baomidou.mybatisplus.service.IService;
import com.iot.device.model.DataPoint;
import com.iot.device.vo.req.DataPointReq;
import com.iot.device.vo.rsp.DeviceFunResp;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author lucky
 * @since 2018-04-17
 */
public interface IDataPointService extends IService<DataPoint> {

    List<DeviceFunResp> findDataPointsByProductId(Long deviceTypeId);

	List<DeviceFunResp> findDataPointListByDeviceTypeId(Long deviceTypeId);

    DataPoint insertDataPoint(DataPointReq dataPoint);
}
