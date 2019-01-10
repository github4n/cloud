package com.iot.device.api.fallback;

import com.iot.device.api.ElectricityStatisticsApi;
import com.iot.device.vo.req.*;
import com.iot.device.vo.rsp.DailyElectricityStatisticsResp;
import com.iot.device.vo.rsp.ElectricityStatisticsRsp;
import com.iot.device.vo.rsp.MonthlyElectricityStatisticsResp;
import com.iot.device.vo.rsp.RuntimeRsp;
import feign.hystrix.FallbackFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class ElectricityStatisticsApiFallbackFactory implements FallbackFactory<ElectricityStatisticsApi> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ElectricityStatisticsApiFallbackFactory.class);

    @Override
    public ElectricityStatisticsApi create(Throwable arg0) {

        return new ElectricityStatisticsApi() {

            @Override
            public boolean insertElectricityStatistics(ElectricityStatisticsReq electrictyStatistics) {
                return false;
            }

            @Override
            public void insertOrUpdateElectricityStatistics() {

            }

            @Override
            public ElectricityStatisticsRsp selectElectricityRspByDeviceAndUser(EnergyReq energyReq) {

                return null;
            }

            @Override
            public boolean insertRuntime(RuntimeReq runtimeReq) {
                return false;
            }

            @Override
            public void insertOrUpdateRuntime() {

            }

            @Override
            public RuntimeRsp selectRuntimeRspByDeviceAndUser(RuntimeReq2Runtime runtimeReq) {

                return null;
            }

            @Override
            public void testJob() {


            }

            @Override
            public Map<String, Object> getEnergyTab(EnergyReq energyReq) {
                return null;
            }

            @Override
            public Map<String, Object> getRuntimeTab(RuntimeReq2Runtime runtimeReq) {
                return null;
            }

            @Override
            public boolean insertMinElectricityStatistics(List<ElectricityStatisticsReq> list) {
                return false;
            }

            @Override
            public boolean insertDailyElectricityStatistics(List<DailyElectricityStatisticsReq> list) {
                return false;
            }

            @Override
            public boolean insertMonthElectricityStatistics(List<MonthlyElectricityStatisticsReq> list) {
                return false;
            }

            @Override
            public List<ElectricityStatisticsRsp> getMinListByReq(ElectricityStatisticsReq req) {
                return null;
            }

            @Override
            public List<DailyElectricityStatisticsResp> getDailyListByReq(DailyElectricityStatisticsReq req) {
                return null;
            }

            @Override
            public List<MonthlyElectricityStatisticsResp> getMonthlyListByReq(MonthlyElectricityStatisticsReq req) {
                return null;
            }

        };
    }
}
