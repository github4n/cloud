package com.iot.device.controller;


import com.iot.common.helper.Page;
import com.iot.device.api.DevelopInfoApi;
import com.iot.device.service.IDevelopInfoService;
import com.iot.device.vo.req.AddDevelopInfoReq;
import com.iot.device.vo.req.DevelopInfoListResp;
import com.iot.device.vo.req.DevelopInfoPageReq;
import com.iot.util.AssertUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * 开发信息表 前端控制器
 * </p>
 *
 * @author lucky
 * @since 2018-06-29
 */
@RestController
public class DevelopInfoController implements DevelopInfoApi {

    @Autowired
    private IDevelopInfoService developInfoService;

    public Long addOrUpdateDevelopInfo(@RequestBody AddDevelopInfoReq infoReq) {
        AssertUtils.notNull(infoReq, "add.develop.info.notnull");
        AssertUtils.notEmpty(infoReq.getCode(), "develop.info.code.notnull");

        return developInfoService.addOrUpdateDevelopInfo(infoReq);
    }

    public List<DevelopInfoListResp> findDevelopInfoListAll() {
        return developInfoService.findDevelopInfoListAll();
    }

    public Page<DevelopInfoListResp> findDevelopInfoPage(@RequestBody DevelopInfoPageReq pageReq) {
        return developInfoService.findDevelopInfoPage(pageReq);
    }
}

