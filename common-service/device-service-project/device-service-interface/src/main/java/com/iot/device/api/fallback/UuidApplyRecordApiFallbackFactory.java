package com.iot.device.api.fallback;

import com.iot.device.api.UuidApplyRecordApi;
import com.iot.device.vo.req.uuid.UuidApplyForBoss;
import com.iot.device.vo.req.uuid.UuidApplyRecordReq;
import com.iot.device.vo.rsp.uuid.UuidApplyRecordResp;
import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;

/**
 * 项目名称：cloud
 * 功能描述：uuid申请异常处理
 * 创建人： yeshiyuan
 * 创建时间：2018/6/29 14:35
 * 修改人： yeshiyuan
 * 修改时间：2018/6/29 14:35
 * 修改描述：
 */
@Component
public class UuidApplyRecordApiFallbackFactory implements FallbackFactory<UuidApplyRecordApi>{
    @Override
    public UuidApplyRecordApi create(Throwable throwable) {
        return new UuidApplyRecordApi() {
            @Override
            public void createUuidApplyRecord(UuidApplyRecordReq uuidApplyReq) {

            }

            @Override
            public void editOrderCreateNum(String orderId, Integer createNum, Long tenantId) {

            }

            @Override
            public UuidApplyRecordResp getUuidApplyRecordInfo(String orderId, Long tenantId) {
                return null;
            }

            @Override
            public int updatePayStatus(String orderId, Long tenantId, Integer payStatus, Integer oldPayStatus) {
                return 0;
            }

            @Override
            public Long uuidApplyForBoss(UuidApplyForBoss uuidApplyForBoss) {
                return null;
            }
        };
    }


}
