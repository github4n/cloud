package com.iot.device.api;

import com.iot.device.vo.req.OperateStepRecordReq;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 项目名称：cloud
 * 功能描述：操作步骤记录api
 * 创建人： yeshiyuan
 * 创建时间：2018/9/11 16:12
 * 修改人： yeshiyuan
 * 修改时间：2018/9/11 16:12
 * 修改描述：
 */
@Api(tags = "操作步骤记录api")
@FeignClient(name = "device-service")
public interface OperateStepRecordApi {

    /**
      * @despriction：查询当前所处步骤
      * @author  yeshiyuan
      * @created 2018/9/11 16:17
      * @return
      */
    @ApiOperation(value = "查询当前所处步骤", notes = "查询当前所处步骤")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "operateId", value = "操作对象id", dataType = "Long", paramType = "query"),
            @ApiImplicitParam(name = "tenantId", value = "租户id", dataType = "Long", paramType = "query"),
            @ApiImplicitParam(name = "operateType", value = "操作类型", dataType = "String", paramType = "query")
    })
    @RequestMapping(value = "/api/operateStepRecord/currentStep", method = RequestMethod.POST)
    int currentStep(@RequestParam("operateId") Long operateId, @RequestParam("tenantId") Long tenantId, @RequestParam("operateType") String operateType);

    /**
      * @despriction：保存操作步骤记录
      * @author  yeshiyuan
      * @created 2018/9/11 16:21
      * @return
      */
    @ApiOperation(value = "保存操作步骤记录", notes = "保存操作步骤记录")
    @RequestMapping(value = "/api/operateStepRecord/save", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    void save(@RequestBody OperateStepRecordReq recordReq);

}
