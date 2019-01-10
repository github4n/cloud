package com.iot.smarthome.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.iot.smarthome.api.DeviceClassifyApi;
import com.iot.smarthome.api.ThirdPartyInfoApi;
import com.iot.smarthome.model.ThirdPartyInfo;
import com.iot.smarthome.service.IThirdPartyInfoService;
import com.iot.smarthome.util.BeanCopyUtil;
import com.iot.smarthome.vo.resp.ThirdPartyInfoResp;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Descrpiton:
 * @Author: yuChangXing
 * @Date: 2018/12/13 9:39
 * @Modify by:
 */

@Slf4j
@RestController
public class ThirdPartyInfoController implements ThirdPartyInfoApi {

    @Autowired
    private IThirdPartyInfoService iThirdPartyInfoService;

    @Override
    public ThirdPartyInfoResp getByClientId(@RequestParam(value = "clientId", required = true) String clientId) {
        ThirdPartyInfoResp resp = null;
        EntityWrapper wrapper = new EntityWrapper<>();
        wrapper.eq("client_id", clientId);
        ThirdPartyInfo thirdPartyInfo = iThirdPartyInfoService.selectOne(wrapper);
        if (thirdPartyInfo != null) {
            resp = new ThirdPartyInfoResp();
            BeanCopyUtil.copyThirdPartyInfo(resp, thirdPartyInfo);
        }
        return resp;
    }

    @Override
    public ThirdPartyInfoResp getById(@RequestParam(value = "id", required = true) Long id) {
        ThirdPartyInfoResp resp = null;
        ThirdPartyInfo thirdPartyInfo = iThirdPartyInfoService.selectById(id);
        if (thirdPartyInfo != null) {
            resp = new ThirdPartyInfoResp();
            BeanCopyUtil.copyThirdPartyInfo(resp, thirdPartyInfo);
        }
        return resp;
    }
}
