package com.iot.portal.uuid.ctrl;

import com.iot.common.annotation.Action;
import com.iot.common.annotation.LoginRequired;
import com.iot.common.beans.CommonResponse;
import com.iot.common.exception.ResultMsg;
import com.iot.common.helper.Page;
import com.iot.payment.vo.order.req.OrderPayReq;
import com.iot.portal.uuid.service.UUIDManageService;
import com.iot.portal.uuid.vo.req.GetUUIDOrderReq;
import com.iot.portal.uuid.vo.req.GetUUIDReq;
import com.iot.portal.uuid.vo.req.UuidApplyReq;
import com.iot.portal.uuid.vo.resp.UUIDInfoResp;
import com.iot.portal.uuid.vo.resp.UUIDOrderResp;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 项目名称：IOT云平台
 * 模块名称：聚合层
 * 功能描述：UUID控制层
 * 创建人： maochengyuan
 * 创建时间：2018/6/29 15:20
 * 修改人： maochengyuan
 * 修改时间：2018/6/29 15:20
 * 修改描述：
 */
@Api(description = "UUID管理")
@RestController
@RequestMapping("/UUIDManageController")
public class UUIDManageController {

    @Autowired
    private UUIDManageService uuidManageService;

    /**
     * 描述：获取UUID商品列表
     * @author maochengyuan
     * @created 2018/6/29 15:30
     * @param
     * @return com.iot.common.beans.CommonResponse
     */
    @LoginRequired(value = Action.Normal)
    @ApiOperation(value = "获取UUID商品列表", notes = "获取UUID商品列表")
    @RequestMapping(value = "/getUUIDGoodsList", method = RequestMethod.GET)
    public CommonResponse getUUIDGoodsList() {
        return ResultMsg.SUCCESS.info(this.uuidManageService.getUUIDGoodsList());
    }

    /**
      * @despriction：加载设备类型支持的技术方案
      * @author  yeshiyuan
      * @created 2018/10/16 11:41
      * @return
      */
    @LoginRequired(value = Action.Normal)
    @ApiOperation(value = "加载设备类型支持的技术方案", notes = "加载设备类型支持的技术方案")
    @RequestMapping(value = "/getTechnicals", method = RequestMethod.GET)
    public CommonResponse getTechnicals(@RequestParam("deviceTypeId") Long deviceTypeId) {
        return ResultMsg.SUCCESS.info(this.uuidManageService.getTechnicals(deviceTypeId));
    }

    /**
      * @despriction：描述
      * @author  yeshiyuan
      * @created 2018/10/16 13:37
      * @return
      */
    @LoginRequired(value = Action.Normal)
    @ApiOperation(value = "通过技术方案id加载uuid商品", notes = "通过技术方案id加载uuid商品")
    @RequestMapping(value = "/getUuidGoodsByTechnicalId", method = RequestMethod.GET)
    public CommonResponse getUuidGoodsByTechnicalId(@RequestParam("technicalId") Long technicalId) {
        return ResultMsg.SUCCESS.info(this.uuidManageService.getUuidGoodsByTechnicalId(technicalId));
    }

    /**
     * 
     * 描述：通过技术方案对应的UUID商品列表(没有gateway)
     * @author 李帅
     * @created 2018年11月8日 下午2:15:59
     * @since 
     * @param technicalId
     * @return
     */
   @LoginRequired(value = Action.Normal)
   @ApiOperation(value = "通过技术方案对应的UUID商品列表(没有gateway)", notes = "通过技术方案对应的UUID商品列表(没有gateway)")
   @RequestMapping(value = "/getUuidGoodsNoGateWayByTechnicalId", method = RequestMethod.GET)
   public CommonResponse getUuidGoodsNoGateWayByTechnicalId(@RequestParam("technicalId") Long technicalId) {
       return ResultMsg.SUCCESS.info(this.uuidManageService.getUuidGoodsNoGateWayByTechnicalId(technicalId));
   }
   
    /**
     * 描述：编辑UUID订单
     * @author maochengyuan
     * @created 2018/7/3 19:10
     * @param orderId 订单id
     * @param createNum UUID数量
     * @return com.iot.common.beans.CommonResponse
     */
    @LoginRequired(value = Action.Normal)
    @ApiOperation(value = "编辑UUID订单", notes = "编辑UUID订单-修改数量")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "orderId", value = "订单id", required = true, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "createNum", value = "UUID数量", required = true, paramType = "query", dataType = "Integer")
    })
    @RequestMapping(value = "/editOrderCreateNum", method = RequestMethod.GET)
    public CommonResponse editOrderCreateNum(@RequestParam("orderId") String orderId, @RequestParam("createNum") Integer createNum) {
        this.uuidManageService.editOrderCreateNum(orderId, createNum);
        return ResultMsg.SUCCESS.info();
    }

    /**
     * 描述：查询UUID申请订单
     *
     * @param uuidOrderReq
     * @return
     * @author shenxiang
     * @created 2018年6月29日 下午1:59:27
     */
    @LoginRequired(value = Action.Normal)
    @ApiOperation(value = "查询UUID申请订单", notes = "查询UUID申请订单")
    @RequestMapping(value = "/getUUIDOrder", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse getUUIDOrder(@RequestBody GetUUIDOrderReq uuidOrderReq) {
        Page<UUIDOrderResp> respPage = uuidManageService.getUUIDOrder(uuidOrderReq);
        return CommonResponse.success(respPage);
    }

    /**
     * 描述：查询UUID信息，可以查询某批次或某个UUID,也可以根据状态查询，
     *
     * @param uuidReq
     * @return
     * @author shenxiang
     * @created 2018年6月29日 下午1:59:27
     */
    @LoginRequired(value = Action.Normal)
    @ApiOperation(value = "查询UUID信息，可以查询某批次或某个UUID,也可以根据状态查询", notes = "查询UUID信息，可以查询某批次或某个UUID,也可以根据状态查询")
    @RequestMapping(value = "/getUUIDInfo", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse getUUIDInfo(@RequestBody GetUUIDReq uuidReq) {
        Page<UUIDInfoResp> respPage = uuidManageService.getUUIDInfo(uuidReq);
        return CommonResponse.success(respPage);
    }

    /**
      * @despriction：uuid申请
      * @author  yeshiyuan
      * @created 2018/7/3 20:41
      * @param uuidApplyReq uuid申请入参
      * @return
      */
    @LoginRequired(value = Action.Normal)
    @ApiOperation(value = "uuid申请", notes = "uuid申请")
    @RequestMapping(value = "/uuidApply", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse uuidApply(@RequestBody UuidApplyReq uuidApplyReq){
        return ResultMsg.SUCCESS.info(this.uuidManageService.uuidApply(uuidApplyReq));
    }

    @ApiOperation(value = "通过批次号获取证书下载URL", notes = "通过批次号获取证书下载URL")
	@LoginRequired(value = Action.Normal)
	@ApiImplicitParams({@ApiImplicitParam(name = "batchNum", value = "批次号", required = true, paramType = "query", dataType = "Long")})
	@RequestMapping(value = "getDownloadUUID", method = RequestMethod.GET)
	public CommonResponse getDownloadUUID(@RequestParam("batchNum")  Long batchNum) {
		return ResultMsg.SUCCESS.info(this.uuidManageService.getDownloadUUID(batchNum));
	}

	/**
	  * @despriction：uuid订单支付
	  * @author  yeshiyuan
	  * @created 2018/7/4 16:20
	  * @param uuidOrderPayReq
	  * @return
	  */
    @LoginRequired(value = Action.Normal)
    @ApiOperation(value = "uuid订单支付", notes = "uuid订单支付")
    @RequestMapping(value = "/uuidOrderPay", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse uuidOrderPay(@RequestBody OrderPayReq orderPayReq){
        return ResultMsg.SUCCESS.info(this.uuidManageService.uuidOrderPay(orderPayReq));
    }

    /**
      * @despriction：paypal支付回调（uuid订单支付）
      * @author  yeshiyuan
      * @created 2018/7/4 16:20
      * @param request
      * @return
      */
    @LoginRequired(value = Action.Skip)
    @ApiIgnore
    @ApiOperation(value = "paypal支付回调（uuid订单支付）", notes = "paypal支付回调（uuid订单支付）")
    @RequestMapping(value = "/uuidOrderPayCallBack", method = {RequestMethod.GET})
    public void uuidOrderPayCallBack(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String url = uuidManageService.uuidOrderPayCallBack(request);
        response.sendRedirect(url);
    }

    /**
      * @despriction：获取uuid订单信息
      * @author  yeshiyuan
      * @created 2018/7/5 9:44
      * @param orderId 订单id
      * @return
      */
    @LoginRequired(value = Action.Normal)
    @ApiOperation(value = "获取uuid订单信息", notes = "获取uuid订单信息")
    @RequestMapping(value = "/getUuidOrderInfo", method = {RequestMethod.GET})
    public CommonResponse getUuidOrderInfo(@RequestParam("orderId") String orderId) {
        return ResultMsg.SUCCESS.info(uuidManageService.getUuidOrderInfo(orderId));
    }

    /**
     * @despriction：查找某个租户下面的产品
     * @author  yeshiyuan
     * @created 2018/7/9 10:48
     * @return
     */
    @LoginRequired(value = Action.Normal)
    @ApiOperation(value = "查找某个租户下面的产品", notes = "查找某个租户下面的产品")
    @RequestMapping(value = "/findProductList", method = {RequestMethod.GET})
    public CommonResponse findProductList(){
        return ResultMsg.SUCCESS.info(uuidManageService.findProductListByTenantId());
    }

    /**
     * 
     * 描述：查找某个租户下面的直连产品
     * @author 李帅
     * @created 2018年11月8日 上午11:25:32
     * @since 
     * @return
     */
    @LoginRequired(value = Action.Normal)
    @ApiOperation(value = "查找某个租户下面的直连产品", notes = "查找某个租户下面的直连产品")
    @RequestMapping(value = "/findDirectProductList", method = {RequestMethod.GET})
    public CommonResponse findDirectProductList(){
        return ResultMsg.SUCCESS.info(uuidManageService.findDirectProductListByTenantId());
    }
}
