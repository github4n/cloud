package com.iot.boss.restful.refund;

import com.github.pagehelper.PageInfo;
import com.iot.boss.api.VideoRefundApi;
import com.iot.boss.dto.OrderPriceDto;
import com.iot.boss.dto.RefundListDto;
import com.iot.boss.dto.RefundListSearch;
import com.iot.boss.dto.refund.VideoRefundRecordDto;
import com.iot.boss.entity.refund.VideoRefundRecord;
import com.iot.boss.manager.refund.VideoRefundManager;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 项目名称：cloud
 * 功能描述：视频退款业务ctrl
 * 创建人： yeshiyuan
 * 创建时间：2018/5/21 11:07
 * 修改人： yeshiyuan
 * 修改时间：2018/5/21 11:07
 * 修改描述：
 */
@RestController
public class VideoRefundRestful implements VideoRefundApi {

    @Autowired
    private VideoRefundManager videoRefundManager;

    /**
     * @despriction：退款申请提交
     * @author  490485964@qq.com
     * @created 2018/5/21 10:54
     * @param videoRefundRecordDto
     * @return
     */
    @Override
    public void applyRefund(@RequestBody VideoRefundRecordDto videoRefundRecordDto) {
        this.videoRefundManager.applyRefund(videoRefundRecordDto);
    }

    /**
     * @despriction：退款审批
     * @author  yeshiyuan
     * @created 2018/5/21 11:01
     * @param
     * @return
     */
    @Override
    public void audit(@RequestParam("refundId") Long refundId, @RequestParam("auditStatus") Integer auditStatus,
                     @RequestParam("auditMessage") String auditMessage, @RequestParam("adminId") Long adminId) {
        videoRefundManager.audit(refundId,auditStatus,auditMessage,adminId);
    }

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
    public OrderPriceDto getOrderPrice(@RequestParam("orderId") String orderId, @RequestParam("userId") String userId){
        return videoRefundManager.getOrderPrice(orderId, userId);
    }

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
    public PageInfo<RefundListDto> queryRefundApplyList(@RequestBody RefundListSearch searchParam){
        return videoRefundManager.queryRefundApplyList(searchParam);
    }
    
    /**
     * 
     * 描述：获取退款记录
     * @author 李帅
     * @created 2018年5月22日 下午5:28:36
     * @since 
     * @param orderIDList
     * @return
     */
    @Override
    public List<VideoRefundRecord> getVideoRefundRecord(@RequestBody List<String> orderIDList) {
        return this.videoRefundManager.getVideoRefundRecord(orderIDList);
    }


}
