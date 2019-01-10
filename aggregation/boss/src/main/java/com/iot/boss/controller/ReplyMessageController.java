package com.iot.boss.controller;

import com.iot.boss.service.reply.IReplyMessageService;
import com.iot.boss.vo.reply.req.AppLeaveMessageReq;
import com.iot.boss.vo.reply.req.ProductLeaveMessageReq;
import com.iot.boss.vo.reply.req.ReplyMessageReq;
import com.iot.boss.vo.reply.req.ServiceLeaveMessageReq;
import com.iot.common.annotation.LoginRequired;
import com.iot.common.beans.CommonResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 项目名称：cloud
 * 功能描述：回复消息api
 * 创建人： yeshiyuan
 * 创建时间：2018/11/2 11:05
 * 修改人： yeshiyuan
 * 修改时间：2018/11/2 11:05
 * 修改描述：
 */
@Api(tags = "回复消息记录api")
@RestController
@RequestMapping(value = "/api/replyMessage")
public class ReplyMessageController {

    @Autowired
    private IReplyMessageService replyMessageService;

    /**
      * @despriction：app审核回复
      * @author  yeshiyuan
      * @created 2018/11/2 11:08
      * @return
      */
    @LoginRequired
    @ApiOperation(value = "app审核回复", notes = "app审核回复")
    @RequestMapping(value = "/appReviewReply", method = RequestMethod.POST)
    public CommonResponse appReviewReply(@RequestBody ReplyMessageReq req){
        replyMessageService.appReviewReply(req);
        return CommonResponse.success();
    }

    /**
      * @despriction：查询app审核回复详情
      * @author  yeshiyuan
      * @created 2018/11/2 14:01
      * @return
      */
    @LoginRequired
    @ApiOperation(value = "查询app审核回复详情", notes = "查询app审核回复详情")
    @RequestMapping(value = "/queryAppReviewReplyDetail", method = RequestMethod.GET)
    public CommonResponse queryAppReviewReplyDetail(@RequestParam Long objectId) {
        return CommonResponse.success(replyMessageService.queryAppReviewReplyDetail(objectId));
    }

    /**
      * @despriction：app留言
      * @author  yeshiyuan
      * @created 2018/11/2 14:20
      * @return
      */
    @LoginRequired
    @ApiOperation(value = "app留言", notes = "app留言")
    @RequestMapping(value = "/appLeaveMessage", method = RequestMethod.POST)
    public CommonResponse appLeaveMessage(@RequestBody AppLeaveMessageReq req){
        replyMessageService.appLeaveMessage(req);
        return CommonResponse.success();
    }


    /**
     * @despriction：第三方接入回复
     * @author  yeshiyuan
     * @created 2018/11/2 11:08
     * @return
     */
    @LoginRequired
    @ApiOperation(value = "第三方接入回复", notes = "第三方接入回复")
    @RequestMapping(value = "/serviceReply", method = RequestMethod.POST)
    public CommonResponse serviceReply(@RequestBody ReplyMessageReq req){
        replyMessageService.serviceReply(req);
        return CommonResponse.success();
    }

    /**
     * @despriction：第三方接入回复详情
     * @author  yeshiyuan
     * @created 2018/11/2 14:01
     * @return
     */
    @LoginRequired
    @ApiOperation(value = "第三方接入回复详情", notes = "第三方接入回复详情")
    @RequestMapping(value = "/queryServiceReplyDetail", method = RequestMethod.GET)
    public CommonResponse queryServiceReplyDetail(@RequestParam Long objectId) {
        return CommonResponse.success(replyMessageService.queryServiceReplyDetail(objectId));
    }

    /**
     * @despriction：第三方接入留言
     * @author  yeshiyuan
     * @created 2018/11/2 14:20
     * @return
     */
    @LoginRequired
    @ApiOperation(value = "第三方接入留言", notes = "第三方接入留言")
    @RequestMapping(value = "/serviceLeaveMessage", method = RequestMethod.POST)
    public CommonResponse serviceLeaveMessage(@RequestBody ServiceLeaveMessageReq req){
        replyMessageService.serviceLeaveMessage(req);
        return CommonResponse.success();
    }


    /**
     * @despriction：产品回复回复
     * @author  yeshiyuan
     * @created 2018/11/2 11:08
     * @return
     */
    @LoginRequired
    @ApiOperation(value = "产品回复", notes = "产品回复")
    @RequestMapping(value = "/productReply", method = RequestMethod.POST)
    public CommonResponse productReply(@RequestBody ReplyMessageReq req){
        replyMessageService.productReply(req);
        return CommonResponse.success();
    }

    /**
     * @despriction：产品回复详情
     * @author  yeshiyuan
     * @created 2018/11/2 14:01
     * @return
     */
    @LoginRequired
    @ApiOperation(value = "产品回复详情", notes = "产品回复详情")
    @RequestMapping(value = "/queryProductReplyDetail", method = RequestMethod.GET)
    public CommonResponse queryProductReplyDetail(@RequestParam Long objectId) {
        return CommonResponse.success(replyMessageService.queryProductReplyDetail(objectId));
    }

    /**
     * @despriction：产品留言
     * @author  yeshiyuan
     * @created 2018/11/2 14:20
     * @return
     */
    @LoginRequired
    @ApiOperation(value = "产品留言", notes = "产品留言")
    @RequestMapping(value = "/productLeaveMessage", method = RequestMethod.POST)
    public CommonResponse productLeaveMessage(@RequestBody ProductLeaveMessageReq req){
        replyMessageService.productLeaveMessage(req);
        return CommonResponse.success();
    }

}
