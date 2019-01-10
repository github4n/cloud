package com.iot.device.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.iot.device.mapper.DataPointMapper;
import com.iot.device.model.DataPoint;
import com.iot.device.service.IDataPointService;
import com.iot.device.vo.req.DataPointReq;
import com.iot.device.vo.rsp.DeviceFunResp;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author lucky
 * @since 2018-04-17
 */
@Service
public class DataPointServiceImpl extends ServiceImpl<DataPointMapper, DataPoint> implements IDataPointService {

    @Autowired
    private DataPointMapper dataPointMapper;

    @Override
    public List<DeviceFunResp> findDataPointsByProductId(Long deviceTypeId) {
        return dataPointMapper.selectDataPointsByProductId(deviceTypeId);
    }

	@Override
	public List<DeviceFunResp> findDataPointListByDeviceTypeId(Long deviceTypeId) {
		return dataPointMapper.findDataPointListByDeviceTypeId(deviceTypeId);
	}

    @Override
    public DataPoint insertDataPoint(DataPointReq dataPoint) {
        DataPoint point = new DataPoint();
        BeanUtils.copyProperties(dataPoint, point);
        point.setCreateTime(new Date());
        super.insert(point);

        return point;
    }
}
