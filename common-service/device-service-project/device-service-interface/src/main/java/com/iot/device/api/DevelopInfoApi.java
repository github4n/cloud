package com.iot.device.api;

import com.iot.common.helper.Page;
import com.iot.device.api.fallback.DevelopInfoApiFallbackFactory;
import com.iot.device.vo.req.AddDevelopInfoReq;
import com.iot.device.vo.req.DevelopInfoListResp;
import com.iot.device.vo.req.DevelopInfoPageReq;
import io.swagger.annotations.Api;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * @Author: xfz
 * @Descrpiton:
 * @Date: 14:14 2018/6/29
 * @Modify by:
 */
@Api(tags = "开发组管理")
@FeignClient(value = "device-service", fallbackFactory = DevelopInfoApiFallbackFactory.class)
@RequestMapping("/developInfo")
public interface DevelopInfoApi {

    @RequestMapping(value = "/addOrUpdateDevelopInfo", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    Long addOrUpdateDevelopInfo(@RequestBody AddDevelopInfoReq infoReq);


    @RequestMapping(value = "/findDevelopInfoListAll", method = RequestMethod.GET)
    List<DevelopInfoListResp> findDevelopInfoListAll();

    @RequestMapping(value = "/findDevelopInfoPage", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    Page<DevelopInfoListResp> findDevelopInfoPage(@RequestBody DevelopInfoPageReq pageReq);
}
