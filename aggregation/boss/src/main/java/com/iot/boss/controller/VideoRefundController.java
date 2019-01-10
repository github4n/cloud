package com.iot.boss.controller;

import com.iot.boss.dto.refund.VideoRefundRecordDto;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import com.iot.boss.service.refund.VideoRefundService;
import com.iot.common.annotation.Action;
import com.iot.common.annotation.LoginRequired;
import com.iot.common.beans.CommonResponse;
import com.iot.common.exception.ResultMsg;
import com.iot.video.dto.AllRecordSearchParam;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@Api(description = "退款接口",value ="退款接口" )
@RequestMapping("/videoRefundController")
public class VideoRefundController {

    @Autowired
    private VideoRefundService videoRefundService;

	@LoginRequired(value = Action.Normal)
    @ApiOperation(value = "订单列表查询", notes = "订单列表查询")
    @RequestMapping(value = "/queryPayRecordList",method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse queryPayRecordList(@RequestBody AllRecordSearchParam searchParam) {
        return ResultMsg.SUCCESS.info(this.videoRefundService.queryPayRecordList(searchParam));
    }

    @LoginRequired(value = Action.Normal)
    @ApiOperation(value = "退款申请", notes = "退款申请")
    @RequestMapping(value = "/applyRefund",method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse applyRefund(@RequestBody VideoRefundRecordDto videoRefundRecordDto) {
        this.videoRefundService.applyRefund(videoRefundRecordDto);
        return ResultMsg.SUCCESS.info();
    }

    @LoginRequired(value = Action.Normal)
    @ApiOperation(value = "查看计划的其他订单", notes = "查看计划的其他订单")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "planId", value = "计划id",paramType = "query",dataType = "String"),
            @ApiImplicitParam(name = "orderId", value = "订单id",paramType = "query",dataType = "String")
    })
    @RequestMapping(value = "/queryOtherPayRecord",method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse queryOtherPayRecord(@RequestParam(value = "orderId")String orderId,@RequestParam(value = "planId")String planId){
        return ResultMsg.SUCCESS.info(this.videoRefundService.queryOtherPayRecord(orderId, planId));
    }

    @LoginRequired(value = Action.Normal)
    @ApiOperation(value = "退款审核", notes = "退款审核")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "refundId",value = "退款订单id",paramType = "query",dataType = "Long"),
            @ApiImplicitParam(name = "auditStatus",value = "审核结果",paramType = "query",dataType = "int"),
            @ApiImplicitParam(name = "auditMessage",value = "审核留言",paramType = "query",dataType = "String")
    })
    @RequestMapping(value = "/refundAudit",method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse refundAudit(@RequestParam("refundId") Long refundId,@RequestParam("auditStatus") Integer auditStatus,@RequestParam("auditMessage") String auditMessage){
        this.videoRefundService.refundAudit(refundId, auditStatus, auditMessage);
        return ResultMsg.SUCCESS.info();
    }

}
