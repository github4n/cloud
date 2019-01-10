package com.iot.device.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.iot.common.helper.Page;
import com.iot.device.mapper.DailyRuntimeMapper;
import com.iot.device.model.DailyRuntime;
import com.iot.device.service.IDailyRuntimeService;
import com.iot.device.vo.req.RuntimeReq2Runtime;
import com.iot.device.vo.rsp.EnergyRsp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author CHQ
 * @since 2018-05-09
 */
@Service
public class DailyRuntimeServiceImpl extends ServiceImpl<DailyRuntimeMapper, DailyRuntime> implements IDailyRuntimeService {

    @Autowired
    DailyRuntimeMapper dailyRuntimeMapper;

//    @Override
//    public List<EnergyRsp> selectDailyRuntimeRsp(RuntimeReq2Runtime runtimeReq) {
//        return dailyRuntimeMapper.selectDailyRuntimeRsp(runtimeReq);
//    }

    @Override
    public Page<EnergyRsp> selectDailyRuntimeRsp(RuntimeReq2Runtime runtimeReq) {

        runtimeReq.setOffset(runtimeReq.getOffset() + 1);
        com.baomidou.mybatisplus.plugins.Page page = new com.baomidou.mybatisplus.plugins.Page(runtimeReq.getOffset(), runtimeReq.getPageSize());
        EntityWrapper ew = new EntityWrapper();

        ew.setSqlSelect(" DISTINCT day as time, runtime as value");
        if (runtimeReq.getUserId() != null) {
            ew.eq("user_id", runtimeReq.getUserId());
        }
        if (runtimeReq.getDevId() != null) {
            ew.eq("device_id", runtimeReq.getDevId());
        }
        if (runtimeReq.getTenantId() != null) {
            ew.eq("tenant_id", runtimeReq.getTenantId());
        }
        ew.orderBy(true, "day", false);
        List<Map<String, Object>> returnDataList = dailyRuntimeMapper.selectMapsPage(page, ew);
        Page<EnergyRsp> returnPageData = new Page<>(runtimeReq.getOffset(), runtimeReq.getPageSize());
        List<EnergyRsp> resultList = Lists.newArrayList();
        if (!CollectionUtils.isEmpty(returnDataList)) {
            returnDataList.forEach(map -> {
                EnergyRsp target = new EnergyRsp();
                String value = map.get("value") != null ? map.get("value").toString() : null;
                String time = map.get("time") != null ? map.get("time").toString() : null;
                target.setValue(value);
                target.setTime(time);
                resultList.add(target);

            });
        }

        returnPageData.setResult(resultList);
        returnPageData.setTotal(page.getTotal());
        returnPageData.setPages(page.getPages());
        return returnPageData;

    }
}
