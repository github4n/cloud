package com.iot.boss.api;

import com.github.pagehelper.PageInfo;
import com.iot.boss.dto.OrderPriceDto;
import com.iot.boss.dto.RefundListDto;
import com.iot.boss.dto.RefundListSearch;
import com.iot.boss.dto.refund.VideoRefundRecordDto;
import com.iot.boss.entity.refund.VideoRefundRecord;
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

import java.util.List;

/**
 * 项目名称：cloud
 * 功能描述：视频退款业务
 * 创建人： yeshiyuan
 * 创建时间：2018/5/21 10:50
 * 修改人： yeshiyuan
 * 修改时间：2018/5/21 10:50
 * 修改描述：
 */
@Api(value = "视频退款业务",description = "视频退款业务")
@FeignClient(value = "boss-service")
@RequestMapping(value = "/videoRefund")
public interface VideoRefundApi {

    /**
      * @despriction：退款申请提交
      * @author  490485964@qq.com
      * @created 2018/5/21 10:54
      * @param videoRefundRecordDto
      * @return
      */
    @ApiOperation(value = "/退款申请提交",notes = "退款申请提交")
    @RequestMapping(value = "/applyRefund",method= RequestMethod.POST,consumes= MediaType.APPLICATION_JSON_VALUE)
    void applyRefund(@RequestBody VideoRefundRecordDto videoRefundRecordDto);


    /**
      * @despriction：退款审批
      * @author  yeshiyuan
      * @created 2018/5/21 11:01
      * @param null
      * @return
      */
    @ApiOperation(value = "退款审批",notes = "退款审批")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "refundId",value = "退款订单id",paramType = "query",dataType = "Long"),
            @ApiImplicitParam(name = "auditStatus",value = "审核结果",paramType = "query",dataType = "int"),
            @ApiImplicitParam(name = "auditMessage",value = "审核留言",paramType = "query",dataType = "String"),
            @ApiImplicitParam(name = "adminId",value = "操作人id",paramType = "query",dataType = "Long"),
    })
    @RequestMapping(value = "/audit",method= RequestMethod.POST)
    void audit(@RequestParam("refundId") Long refundId,@RequestParam("auditStatus") Integer auditStatus,@RequestParam("auditMessage") String auditMessage,@RequestParam("adminId") Long adminId);


    /**
     *
     * 描述：查询订单价格
     * @author ouyangjie
     * @created 2018年5月21日 上午10:09:56
     * @since
     * @param orderId
     * @return
     */
    @ApiOperation(value = "查询订单价格",notes = "查询订单价格")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "orderId",value = "订单id",dataType = "String",paramType = "query"),
            @ApiImplicitParam(name = "userId",value = "用户id",dataType = "String",paramType = "query")
    })
    @RequestMapping(value = "/getOrderPrice",method = RequestMethod.POST)
    OrderPriceDto getOrderPrice(@RequestParam("orderId") String orderId, @RequestParam("userId") String userId);

    /**
     *
     * 描述：退款申请列表查询
     * @author ouyangjie
     * @created 2018年5月21日 上午10:09:56
     * @since
     * @param searchParam
     * @return
     */
    @ApiOperation(value = "退款申请列表查询", notes = "退款申请列表查询")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "searchParam", value = "退款申请列表查询", required = true, dataType = "RefundListSearch")})
    @RequestMapping(value = "/queryRefundApplyList", method=RequestMethod.POST,consumes= MediaType.APPLICATION_JSON_VALUE)
    public PageInfo<RefundListDto> queryRefundApplyList(@RequestBody RefundListSearch searchParam);

    /**
     *
     * 描述：获取退款记录
     * @author 李帅
     * @created 2018年5月22日 下午5:27:39
     * @since
     * @param orderIDList
     * @return
     */
   @ApiOperation(value = "/获取退款记录",notes = "获取退款记录")
   @RequestMapping(value = "/getVideoRefundRecord",method= RequestMethod.POST,consumes= MediaType.APPLICATION_JSON_VALUE)
   List<VideoRefundRecord> getVideoRefundRecord(@RequestBody List<String> orderIDList);


}
