package com.iot.payment.api;

import com.github.pagehelper.PageInfo;
import com.iot.payment.entity.goods.GoodsExtendService;
import com.iot.payment.entity.goods.GoodsInfo;
import com.iot.payment.vo.goods.req.VideoPackageReq;
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
import java.util.Map;

/**
 * 项目名称：立达信IOT云平台
 * 模块名称：支付服务
 * 功能描述：商品服务接口
 * 创建人： maochengyuan
 * 创建时间：2018/7/3 13:34
 * 修改人： maochengyuan
 * 修改时间：2018/7/3 13:34
 * 修改描述：
 */
@Api(value = "商品服务接口")
@FeignClient("payment-service")
@RequestMapping("/api/service/goodsService")
public interface GoodsServiceApi {

    /** 
     * 描述：依据商品类别获取商品列表（只查询上线及有效商品）
     * @author maochengyuan
     * @created 2018/7/3 13:48
     * @param typeId 商品类别id
     * @return java.util.List<com.iot.payment.entity.goods.GoodsInfo>
     */
    @ApiOperation(value = "依据商品类别获取商品列表", notes = "依据商品类别获取商品列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "typeId", value = "商品类别id", required = true, paramType = "query", dataType = "String")
    })
    @RequestMapping(value = "/getGoodsInfoByTypeId", method = {RequestMethod.GET})
    List<GoodsInfo> getGoodsInfoByTypeId(@RequestParam("typeId") Integer typeId);

    /**
     * 描述：依据商品类别获取商品列表（只查询上线及有效商品）
     * @author maochengyuan
     * @created 2018/7/3 13:48
     * @param typeIdList 商品类别ids
     * @return java.util.List<com.iot.payment.entity.goods.GoodsInfo>
     */
    @ApiOperation(value = "依据商品多个类别获取商品列表", notes = "依据多个商品类别获取商品列表")
//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "typeIds", value = "商品类别id", required = true, paramType = "query", dataType = "String")
//    })
    @RequestMapping(value = "/getGoodsInfoByTypeIdList", method = {RequestMethod.GET})
    List<GoodsInfo> getGoodsInfoByTypeIdList(@RequestParam("typeIdList") List<Integer> typeIdList);

    /**
     * 描述：依据商品类别获取商品附加服务列表（只查询上线及有效商品附加服务）
     * @author maochengyuan
     * @created 2018/7/3 13:49
     * @param goodsId 商品id
     * @return java.util.List<com.iot.payment.entity.goods.GoodsExtendService>
     */
    @ApiOperation(value = "依据商品类别获取商品附加服务列表", notes = "依据商品类别获取商品附加服务列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "goodsId", value = "商品id", required = true, paramType = "query", dataType = "String")
    })
    @RequestMapping(value = "/getGoodsExServiceByGoodsId", method = {RequestMethod.GET})
    List<GoodsExtendService> getGoodsExServiceByGoodsId(@RequestParam("goodsId") Long goodsId);

    /**
     * 描述：依据商品id查询商品信息
     * @author maochengyuan
     * @created 2018/7/3 13:49
     * @param goodsId 商品id
     * @return com.iot.payment.entity.goods.GoodsExtendService
     */
    @ApiOperation(value = "依据商品id查询商品信息", notes = "依据商品id查询商品信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "goodsId", value = "商品id", required = true, paramType = "query", dataType = "String")
    })
    @RequestMapping(value = "/getGoodsInfoByGoodsId", method = {RequestMethod.GET})
    GoodsInfo getGoodsInfoByGoodsId(@RequestParam("goodsId") Long goodsId);

    /**
     * 描述：依据商品id获取商品名称
     * @author maochengyuan
     * @created 2018/7/3 14:02
     * @param goodsIds 商品id
     * @return java.util.Map<java.lang.Integer,java.lang.String>
     */
    @ApiOperation(value = "依据商品id获取商品名称", notes = "依据商品id获取商品名称")
    @RequestMapping(value = "/getGoodsNameByGoodsId", method = {RequestMethod.POST}, consumes = MediaType.APPLICATION_JSON_VALUE)
    Map<Long, String> getGoodsNameByGoodsId(@RequestBody List<Long> goodsIds);

    /**
      * @despriction：查询计划套餐列表
      * @author  yeshiyuan
      * @created 2018/7/17 10:29
      * @return
      */
    @ApiOperation(value = "查询计划套餐列表" ,  notes="查询计划套餐列表")
    @RequestMapping(value="/getVideoPackageList",method=RequestMethod.POST,consumes=MediaType.APPLICATION_JSON_VALUE)
    PageInfo<GoodsInfo> getVideoPackageList(@RequestBody VideoPackageReq videoPackageReq);

    /**
      * @despriction：依据订单商品id查询原商品信息
      * @author  yeshiyuan
      * @created 2018/10/29 16:27
      * @return
      */
    @ApiOperation(value = "依据订单商品id查询原商品信息", notes = "依据订单商品id查询原商品信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "orderGoodsId", value = "订单商品id", required = true, paramType = "query", dataType = "Long")
    })
    @RequestMapping(value = "/getOldGoodsInfoByOrderGoodsId", method = {RequestMethod.GET})
    GoodsInfo getOldGoodsInfoByOrderGoodsId(@RequestParam("orderGoodsId") Long orderGoodsId);


    @ApiOperation(value = "根据商品goods_code查询商品信息", notes = "根据商品goods_code查询商品信息")
    @ApiImplicitParam(name = "goodsCode", value = "商品编码", required = true, paramType = "query", dataType = "String")
    @RequestMapping(value = "/getGoodsInfoByGoodsCode", method = {RequestMethod.POST})
    GoodsInfo getGoodsInfoByGoodsCode(@RequestParam("goodsCode") String goodsCode);

    @ApiOperation(value = "根据商品goodsCodes list查询商品列表信息", notes = "根据商品goodsCodes list查询商品列表信息")
    @RequestMapping(value = "/getListGoodsInfoByGoodsCode", method = {RequestMethod.POST}, consumes = MediaType.APPLICATION_JSON_VALUE)
    List<GoodsInfo> getListGoodsInfoByGoodsCode(@RequestBody List<String> goodsCodes);
}
