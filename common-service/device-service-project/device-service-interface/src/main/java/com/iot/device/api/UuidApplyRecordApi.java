package com.iot.device.api;

import com.iot.device.api.fallback.UuidApplyRecordApiFallbackFactory;
import com.iot.device.vo.req.uuid.UuidApplyForBoss;
import com.iot.device.vo.req.uuid.UuidApplyRecordReq;
import com.iot.device.vo.rsp.uuid.UuidApplyRecordResp;
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
 * 功能描述：uuid申请
 * 创建人： yeshiyuan
 * 创建时间：2018/6/29 14:33
 * 修改人： yeshiyuan
 * 修改时间：2018/6/29 14:33
 * 修改描述：
 */
@Api(tags = "Uuid订单管理",description = "Uuid订单管理")
@FeignClient(value = "device-service", fallbackFactory = UuidApplyRecordApiFallbackFactory.class)
@RequestMapping("/uuidApplyRecord")
public interface UuidApplyRecordApi {

    /**
     * @despriction：创建uuid申请记录
     * @author  yeshiyuan
     * @created 2018/6/29 14:16
     * @param uuidApplyReq
     * @return
     */
    @ApiOperation(value = "创建uuid申请记录", notes = "创建uuid申请记录")
    @RequestMapping(value = "/createUuidApplyRecord", method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE)
    void createUuidApplyRecord(@RequestBody UuidApplyRecordReq uuidApplyReq);

    /**
     * 描述：编辑UUID订单-修改数量
     * @author maochengyuan
     * @created 2018/7/3 19:13
     * @param orderId 订单id
     * @param createNum UUID数量
     * @return void
     */
    @ApiOperation(value = "编辑UUID订单", notes = "编辑UUID订单-修改数量")
    @RequestMapping(value = "/editOrderCreateNum", method = RequestMethod.GET)
    void editOrderCreateNum(@RequestParam("orderId") String orderId, @RequestParam("createNum") Integer createNum, @RequestParam("tenantId") Long tenantId);

    @ApiOperation(value = "uuid申请记录信息", notes = "uuid申请记录信息")
    @RequestMapping(value = "/getUuidApplyRecordInfo", method = RequestMethod.GET)
    UuidApplyRecordResp getUuidApplyRecordInfo(@RequestParam("orderId") String orderId, @RequestParam("tenantId") Long tenantId);

    /**
     * @despriction：修改uuid申请记录的支付状态
     * @author  yeshiyuan
     * @created 2018/7/4 16:59
     * @param orderId 订单id
     * @param 租户id 租户id
     * @param payStatus 支付状态
     * @return
     */
    @ApiOperation(value = "修改uuid申请记录的支付状态", notes = "修改uuid申请记录的支付状态")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "orderId", value = "订单id", dataType = "string", required = true),
            @ApiImplicitParam(name = "tenantId", value = "租户id", dataType = "long", required = true),
            @ApiImplicitParam(name = "payStatus", value = "支付状态", dataType = "int", required = true),
            @ApiImplicitParam(name = "oldPayStatus", value = "旧支付状态", dataType = "int", required = true),
    })
    @RequestMapping(value = "/updatePayStatus", method = RequestMethod.POST)
    int updatePayStatus(@RequestParam("orderId") String orderId, @RequestParam("tenantId") Long tenantId, @RequestParam("payStatus") Integer payStatus,
                        @RequestParam("oldPayStatus") Integer oldPayStatus);

    /**
     * @despriction：专门给boss系统用的uuid申请
     * @author  yeshiyuan
     * @created 2018/6/29 14:16
     * @param uuidApplyReq
     * @return 批次号
     */
    @ApiOperation(value = "专门给boss系统用的uuid申请", notes = "专门给boss系统用的uuid申请")
    @RequestMapping(value = "/uuidApplyForBoss", method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE)
    Long uuidApplyForBoss(@RequestBody UuidApplyForBoss uuidApplyForBoss);
}
