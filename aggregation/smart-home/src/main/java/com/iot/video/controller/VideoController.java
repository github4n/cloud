package com.iot.video.controller;

import com.iot.common.annotation.Action;
import com.iot.common.annotation.LoginRequired;
import com.iot.common.beans.CommonResponse;
import com.iot.common.exception.ResultMsg;
import com.iot.video.service.VideoBusinessService;
import com.iot.video.vo.*;
import com.iot.video.vo.req.CountVideoDateReq;
import com.iot.video.vo.req.GetEventVideoFileReq;
import com.iot.vo.BasePageVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

//import com.iot.video.dto.TaskPlanParam;

/**
 * 项目名称：IOT视频云
 * 模块名称：聚合层
 * 功能描述：视频控制层
 * 创建人：mao2080@sina.com
 * 创建时间：2018/3/20 17:46
 * 修改人：mao2080@sina.com
 * 修改时间：2018/3/20 17:46
 * 修改描述：
 */
@Api(description = "视频云-视频接口")
@RestController
@RequestMapping("/videoController")
public class VideoController {

    @Autowired
    private VideoBusinessService videoBusiness;

    /**
     * 描述：更新录影计划状态
     *
     * @author wujianlong
     * @created 2018年3月19日 下午7:47:42
     * @since
     */
    @LoginRequired(value = Action.Normal)
    @ApiOperation(value = "更新录影计划状态", notes = "更新录影计划状态")
    @ApiImplicitParams({@ApiImplicitParam(name = "planId", value = "计划id", required = true, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "planExecStatus", value = "计划执行状态 0关闭  1打开", required = true, paramType = "query", dataType = "Integer")})
    @RequestMapping(value = "/updatePlanExecStatus", method = RequestMethod.POST)
    public CommonResponse updatePlanExecStatus(@RequestParam("planId") String planId, @RequestParam("planExecStatus") Integer planExecStatus) {
        this.videoBusiness.updatePlanExecStatus(planId, planExecStatus);
        return ResultMsg.SUCCESS.info();
    }

    /**
     * 描述：修改录影计划任务
     *
     * @param taskPlanList 任务列表参数
     * @return
     * @author wujianlong
     * @created 2018年3月23日 上午11:47:48
     * @since
     */
    @LoginRequired(value = Action.Normal)
    @ApiOperation(value = "修改录影计划任务", notes = "修改录影计划任务")
    @RequestMapping(value = "/updatePlanTask", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse updatePlanTask(@RequestBody List<TaskPlanVo> taskPlanList) {
        this.videoBusiness.updatePlanTask(taskPlanList);
        return ResultMsg.SUCCESS.info();
    }

    /**
     * 描述：查询套餐列表
     *
     * @param videoPackageParamVo 套餐查询参数VO
     * @return com.lds.iot.common.beans.newBeans.CommonResponse
     * @author wujianlong
     * @created 2018年3月23日 上午11:47:58
     */
    @LoginRequired(value = Action.Normal)
    @ApiOperation(value = "查询套餐列表", notes = "查询套餐列表")
    @RequestMapping(value = "/getVideoPackageList", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse getVideoPackageList(@RequestBody VideoPackageParamVo videoPackageParamVo) {
        return ResultMsg.SUCCESS.info(this.videoBusiness.getVideoPackageList(videoPackageParamVo));
    }

    /**
     * 描述：查询录影任务
     *
     * @param planId 计划id
     * @return com.lds.iot.common.beans.newBeans.CommonResponse
     * @author wujianlong
     * @created 2018年3月23日 上午11:47:58
     */
    @LoginRequired(value = Action.Normal)
    @ApiOperation(value = "查询录影任务", notes = "查询录影任务")
    @ApiImplicitParams({@ApiImplicitParam(name = "planId", value = "计划id", required = true, paramType = "query", dataType = "String")})
    @RequestMapping(value = "/getVideoTaskList", method = RequestMethod.POST)
    public CommonResponse getVideoTaskList(@RequestParam("planId") String planId) {
        return ResultMsg.SUCCESS.info(this.videoBusiness.getVideoTaskList(planId));
    }

    /**
     * 描述：查询计划列表
     *
     * @param planParamVo 查询计划列表参数VO
     * @return com.lds.iot.common.beans.newBeans.CommonResponse
     * @author wujianlong
     * @created 2018年3月23日 上午11:47:58
     */
    @LoginRequired(value = Action.Normal)
    @ApiOperation(value = "查询计划列表", notes = "查询计划列表")
    @RequestMapping(value = "/getPlanList", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse getPlanList(@RequestBody PlanParamVo planParamVo) {
        return ResultMsg.SUCCESS.info(this.videoBusiness.getPlanList(planParamVo));
    }

    @LoginRequired(value = Action.Normal)
    @ApiOperation(value = "依据设备id查询计划详情", notes = "依据设备id查询计划详情")
    @RequestMapping(value = "/getPlanInfoByDevId", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse getPlanInfoByDevId(@RequestBody PlanParamVo planParamVo) {
        return ResultMsg.SUCCESS.info(this.videoBusiness.getPlanInfoByDevId(planParamVo));
    }

  /*  *//**
     * 描述：查询一段时间事件信息列表
     *
     * @param vespVO 查询参数
     * @return com.lds.iot.common.beans.newBeans.CommonResponse
     * @author mao2080@sina.com
     * @created 2018/3/23 14:39
     *//*
    @LoginRequired(value = Action.Normal)
    @ApiOperation(value = "查询一段时间事件信息列表", notes = "查询一段时间事件信息列表")
    @RequestMapping(value = "/getVideoEventList", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse getVideoEventList(@RequestBody VideoEventParamVO vespVO) {
        return ResultMsg.SUCCESS.info(this.videoBusiness.getVideoEventList(vespVO));
    }
*/
    /**
     * 描述：查询一段时间的事件数量
     *
     * @param vespVO 查询参数
     * @return
     * @author 490485964@qq.com
     * @date 2018/6/6 14:30
     */
    @LoginRequired(value = Action.Normal)
    @ApiOperation(value = "查询一段时间事件数量", notes = "查询一段时间事件数量")
    @RequestMapping(value = "/getVideoEventCount", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse getVideoEventCount(@RequestBody VideoEventParamVO vespVO) {
        return ResultMsg.SUCCESS.info(this.videoBusiness.getVideoEventCount(vespVO));
    }

    /**
     * 描述：查询一段时间视频文件列表
     *
     * @param vfpVO 查询参数
     * @return com.lds.iot.common.beans.newBeans.CommonResponse
     * @author mao2080@sina.com
     * @created 2018/3/23 14:39
     */
    @LoginRequired(value = Action.Normal)
    @ApiOperation(value = "查询一段时间视频文件列表", notes = "查询一段时间视频文件列表")
    @RequestMapping(value = "/getVideoFileList", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse getVideoFileList(@RequestBody VideoFileParamVO vfpVO) {
        return ResultMsg.SUCCESS.info(this.videoBusiness.getVideoFileList(vfpVO));
    }

    /**
     * 描述：修改录影计划排序
     *
     * @param poVO 排序对象
     * @return com.lds.iot.common.beans.newBeans.CommonResponse
     * @author mao2080@sina.com
     * @created 2018/3/23 14:38
     */
    @LoginRequired(value = Action.Normal)
    @ApiOperation(value = "修改录影计划排序", notes = "修改录影计划排序")
    @RequestMapping(value = "/updatePlanOrder", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse updatePlanOrder(@RequestBody PlanOrderParamVO poVO) {
        this.videoBusiness.updatePlanOrder(poVO);
        return ResultMsg.SUCCESS.info();
    }

    /**
     * 描述：分页查询事件列表
     *
     * @param epVO 查询参数VO
     * @return com.lds.iot.common.beans.newBeans.CommonResponse
     * @author mao2080@sina.com
     * @created 2018/3/23 15:29
     */
    @LoginRequired(value = Action.Normal)
    @ApiOperation(value = "获取事件图片URL列表", notes = "获取事件图片URL列表")
    @RequestMapping(value = "/getEventPhotoList", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse getEventPhotoList(@RequestBody EventParamVO epVO) {
        return ResultMsg.SUCCESS.info(this.videoBusiness.getEventPhotoList(epVO));
    }

    /**
     * 描述：获取计划最后一帧图片
     *
     * @param lppVO 查询参数VO
     * @return com.lds.iot.common.beans.newBeans.CommonResponse
     * @author mao2080@sina.com
     * @created 2018/3/23 15:48
     */
    @LoginRequired(value = Action.Normal)
    @ApiOperation(value = "获取计划最后一帧图片", notes = "获取计划最后一帧图片")
    @RequestMapping(value = "/getPlanLastPic", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse getPlanLastPic(@RequestBody LastPicParamVO lppVO) {
        return ResultMsg.SUCCESS.info(this.videoBusiness.getPlanLastPic(lppVO));
    }

    /**
     * 描述：更新计划名称
     *
     * @param planNameVo 计划名称
     * @return com.lds.iot.common.beans.newBeans.CommonResponse
     * @author mao2080@sina.com
     * @created 2018/3/26 17:40
     */
    @LoginRequired(value = Action.Normal)
    @ApiOperation(value = "更新计划名称", notes = "更新计划名称")
    @RequestMapping(value = "/updatePlanName", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse updatePlanName(@RequestBody PlanNameVo planNameVo) {
        this.videoBusiness.updatePlanName(planNameVo);
        return ResultMsg.SUCCESS.info();
    }

    /**
     * 描述：分页查询购买历史
     *
     * @param bhrlpVO 查询参数VO
     * @return com.lds.iot.common.beans.newBeans.CommonResponse
     * @author mao2080@sina.com
     * @created 2018/3/26 17:41
     */
    @LoginRequired(value = Action.Normal)
    @ApiOperation(value = "分页查询购买历史", notes = "分页查询购买历史")
    @RequestMapping(value = "/getBuyHisRecordList", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse getBuyHisRecordList(@RequestBody BuyHisRecordListParamVO bhrlpVO) {
        return ResultMsg.SUCCESS.info(this.videoBusiness.getBuyHisRecordList(bhrlpVO));
    }

    /**
     * 描述：分页查询购买记录
     *
     * @param bpVO 基础分页VO
     * @return com.lds.iot.common.beans.newBeans.CommonResponse
     * @author mao2080@sina.com
     * @created 2018/3/26 17:44
     */
    @LoginRequired(value = Action.Normal)
    @ApiOperation(value = "分页查询购买记录", notes = "分页查询购买记录")
    @RequestMapping(value = "/getBuyRecordList", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse getBuyRecordList(@RequestBody BasePageVO bpVO) {
        return ResultMsg.SUCCESS.info(this.videoBusiness.getBuyRecordList(bpVO));
    }

    /**
     * 描述：购买视频计划
     *
     * @param webPlanVo
     * @return
     * @author wujianlong
     * @created 2018年3月28日 上午10:17:42
     * @since
     */
    @LoginRequired(value = Action.Normal)
    @ApiOperation(value = "购买视频计划", notes = "购买视频计划")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "counts", value = "购买数量", dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "currency", value = "货币代码", dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "packageId", value = "套餐包id（AES加密）", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "payPrice", value = "支付金额", dataType = "double", paramType = "query"),
            @ApiImplicitParam(name = "successUrl", value = "支付成功跳转url", dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "errorUrl", value = "支付失败跳转url", dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "cancelUrl", value = "取消支付跳转url", dataType = "string", paramType = "query")})
    @RequestMapping(value = "/buyVideoPlan", method = RequestMethod.POST)
    public CommonResponse buyVideoPlan(@RequestBody WebPlanVo webPlanVo, HttpServletRequest request) {
        //获取当前服务器访问地址
        String localServerUrl = request.getScheme() + "://" + request.getLocalAddr() + ":" + request.getLocalPort();
        return ResultMsg.SUCCESS.info(this.videoBusiness.buyVideoPlan(webPlanVo, localServerUrl));
    }

    /**
     * 描述：购买视频计划回调
     *
     * @param req
     * @return
     * @author wujianlong
     * @created 2018年3月31日 上午11:06:26
     * @since
     */
    @LoginRequired(value = Action.Skip)
    @ApiOperation(value = "购买视频计划回调", notes = "购买视频计划回调")
    @RequestMapping(value = "/buyVideoPlanCallBack", method = RequestMethod.GET)
    public void buyVideoPlanCallBack(HttpServletRequest req, HttpServletResponse response) throws IOException {
        String url = this.videoBusiness.buyVideoPlanCallBack(req);
        response.sendRedirect(url);
    }


    /**
     * 描述：根据设备id查询计划信息
     *
     * @param deviceId
     * @return com.lds.iot.video.dto.PlanInfoDto
     * @author 李帅
     * @created 2018年4月16日 下午2:15:58
     * @since
     */
    @LoginRequired(value = Action.Normal)
    @ApiOperation(value = "根据设备id查询计划类型", notes = "根据设备id查询计划类型")
    @ApiImplicitParams({@ApiImplicitParam(name = "deviceId", value = "设备id", required = true, paramType = "query", dataType = "String")})
    @RequestMapping(value = "/getPlanType", method = RequestMethod.POST)
    public CommonResponse getPlanType(@RequestParam("deviceId") String deviceId) {
        return ResultMsg.SUCCESS.info(this.videoBusiness.getPlanType(deviceId));
    }

    /**
     * 描述：计划绑定设备
     *
     * @param planId
     * @param deviceId
     * @return
     * @author 李帅
     * @created 2018年4月27日 下午1:42:35
     * @since
     */
    @LoginRequired(value = Action.Normal)
    @ApiOperation(value = "计划绑定设备", notes = "计划绑定设备")
    @ApiImplicitParams({@ApiImplicitParam(name = "planId", value = "计划id", required = true, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "deviceId", value = "设备id", required = true, paramType = "query", dataType = "String")})
    @RequestMapping(value = "/planBandingDevice", method = RequestMethod.POST)
    public CommonResponse planBandingDevice(@RequestParam("planId") String planId, @RequestParam("deviceId") String deviceId) {
        this.videoBusiness.planBandingDevice(planId, deviceId);
        return ResultMsg.SUCCESS.info();
    }

    /**
     * 描述：删除录影事件
     *
     * @param planId
     * @param eventId
     * @return
     * @author 李帅
     * @created 2018年4月27日 下午1:42:35
     * @since
     */
    @LoginRequired(value = Action.Normal)
    @ApiOperation(value = "删除录影事件", notes = "删除录影事件")
    @ApiImplicitParams({@ApiImplicitParam(name = "planId", value = "计划id", required = true, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "eventId", value = "事件id", required = true, paramType = "query", dataType = "String")})
    @RequestMapping(value = "/deleteVideoEvent", method = RequestMethod.POST)
    public CommonResponse deleteVideoEvent(@RequestParam("planId") String planId, @RequestParam("eventId") String eventId) {
        this.videoBusiness.deleteVideoEvent(planId, eventId);
        return ResultMsg.SUCCESS.info();
    }

    /**
      * @despriction：通过事件id获取事件对应的视频文件
      * @author  yeshiyuan
      * @created 2018/6/25 9:31
      * @param null
      * @return
      */
    @LoginRequired(value = Action.Normal)
    @ApiOperation(value = "通过事件id获取事件对应的视频文件" , notes = "通过事件id获取事件对应的视频文件")
    @RequestMapping(value = "/getVideoTsFileListByEventUuid",method = RequestMethod.POST)
    public CommonResponse getVideoTsFileListByEventUuid(@RequestBody GetEventVideoFileReq req) {
        return ResultMsg.SUCCESS.info(this.videoBusiness.getVideoTsFileListByEventUuid(req));
    }


    /**
     * @despriction：统计IPC录影日期
     * @author  yeshiyuan
     * @created 2018/7/26 10:56
     * @return
     */
    @LoginRequired(value = Action.Normal)
    @ApiOperation(value = "统计IPC录影日期" , notes = "统计IPC录影日期")
    @RequestMapping(value = "/countVideoDate", method = RequestMethod.POST)
    public CommonResponse countVideoDate(@RequestBody CountVideoDateReq req) {
        return ResultMsg.SUCCESS.info(this.videoBusiness.countVideoDate(req));
    }
}
