package com.iot.control.space.controller;

import com.iot.control.space.api.SpaceDeviceApi;
import com.iot.control.space.service.ISpaceDeviceService;
import com.iot.control.space.vo.DelSpaceDeviceReq;
import com.iot.control.space.vo.SpaceAndSpaceDeviceVo;
import com.iot.control.space.vo.SpaceDeviceReq;
import com.iot.control.space.vo.SpaceDeviceReqVo;
import com.iot.control.space.vo.SpaceDeviceResp;
import com.iot.control.space.vo.SpaceDeviceVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

/**
 * @description:
 * @program: cloud
 * @return:
 * @author: chq
 * @date: 2018/10/12 11:44
 **/
@RestController()
public class SpaceDeviceController implements SpaceDeviceApi {

    private static final Logger LOGGER = LoggerFactory.getLogger(SpaceDeviceController.class);

    @Autowired
    private ISpaceDeviceService spaceDeviceService;

    @Override
    public boolean inserSpaceDevice(@RequestBody @Valid SpaceDeviceReq spaceDeviceReq) {
        return spaceDeviceService.inserSpaceDevice(spaceDeviceReq);
    }

    @Override
    public boolean updateSpaceDevice(@RequestBody @Valid SpaceDeviceReq spaceDeviceReq) {
        return spaceDeviceService.updateSpaceDevice(spaceDeviceReq);
    }

    @Override
    public boolean insertOrUpdateSpaceDeviceByDevId(@RequestBody @Valid SpaceDeviceReq spaceDeviceReq){
        return spaceDeviceService.insertOrUpdateSpaceDeviceByDevId(spaceDeviceReq);
    }

    @Override
    public boolean saveSpaceDeviceList(@RequestBody @Valid List<SpaceDeviceReq> spaceDeviceReqs) {
        return spaceDeviceService.saveSpaceDeviceList(spaceDeviceReqs);
    }

    @Override
    public boolean updateSpaceDevices(@RequestBody @Valid List<SpaceDeviceReq> spaceDeviceReqs) {
        return spaceDeviceService.updateSpaceDevices(spaceDeviceReqs);
    }

    @Override
    public List<SpaceDeviceResp> findSpaceDeviceByCondition(@RequestBody @Valid SpaceDeviceReq spaceDeviceReq) {
        return spaceDeviceService.findSpaceDeviceByCondition(spaceDeviceReq);
    }

    @Override
    public List<SpaceDeviceVo> findSpaceDeviceVOBySpaceId(@RequestParam("tenantId") Long tenantId, @RequestParam("spaceId") Long spaceId) {
        return spaceDeviceService.findSpaceDeviceVOBySpaceId(tenantId, spaceId);
    }

    @Override
    public List<SpaceDeviceResp> findSpaceDeviceBySpaceIdsOrDeviceIds(@RequestBody @Valid SpaceAndSpaceDeviceVo req){
        return spaceDeviceService.findSpaceDeviceBySpaceIdsOrDeviceIds(req);
    }
    @Override
    public int countSpaceDeviceByCondition(@RequestBody @Valid SpaceDeviceReq spaceDeviceReq) {
        return spaceDeviceService.countSpaceDeviceByCondition(spaceDeviceReq);
    }

    @Override
    public boolean updateSpaceDeviceByCondition(@RequestBody @Valid SpaceDeviceReqVo reqVo) {
        return spaceDeviceService.updateSpaceDeviceByCondition(reqVo);
    }

    @Override
    public int deleteSpaceDeviceByDeviceId(@RequestParam("tenantId") Long tenantId, @RequestParam("deviceId") String deviceId) {
        return spaceDeviceService.deleteSpaceDeviceByDeviceId(tenantId, deviceId);
    }

    @Override
    public int deleteSpaceDeviceByBatchDeviceIds(@RequestBody DelSpaceDeviceReq params) {
        if (params != null && !CollectionUtils.isEmpty(params.getDeviceIds())) {
            params.getDeviceIds().forEach(deviceId ->{
                //TODO 批量删除
                spaceDeviceService.deleteSpaceDeviceByDeviceId(params.getTenantId(), deviceId);
            });
        }
        return 0;
    }

    @Override
    public boolean deleteSpaceDeviceBySpaceIdsOrDeviceIds(@RequestBody @Valid SpaceAndSpaceDeviceVo req) {
        return spaceDeviceService.deleteSpaceDeviceBySpaceIdsOrDeviceIds(req);
    }

    @Override
    public List<SpaceDeviceVo> findSpaceDeviceInfoByDeviceIds(@RequestBody @Valid SpaceAndSpaceDeviceVo req){
        return spaceDeviceService.findSpaceDeviceInfoByDeviceIds(req);
    }
}
