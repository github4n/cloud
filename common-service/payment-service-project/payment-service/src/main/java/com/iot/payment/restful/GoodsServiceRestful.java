package com.iot.payment.restful;

import com.github.pagehelper.PageInfo;
import com.iot.payment.api.GoodsServiceApi;
import com.iot.payment.entity.goods.GoodsExtendService;
import com.iot.payment.entity.goods.GoodsInfo;
import com.iot.payment.manager.GoodsServiceManager;
import com.iot.payment.vo.goods.req.VideoPackageReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 *
 * 项目名称：IOT云平台
 * 模块名称：支付服务
 * 功能描述：商品接口
 * 创建人： maochengyuan
 * 创建时间：2018/7/3 16:38
 * 修改人： maochengyuan
 * 修改时间：2018/7/3 16:38
 * 修改描述：
 */
@RestController
public class GoodsServiceRestful implements GoodsServiceApi {

	@Autowired
	private GoodsServiceManager goodsServiceManager;

	/** 
	 * 描述：依据商品类别id获取商品列表
	 * @author maochengyuan
	 * @created 2018/7/3 16:41
	 * @param typeId
	 * @return java.util.List<com.iot.payment.entity.goods.GoodsInfo>
	 */
	@Override
	public List<GoodsInfo> getGoodsInfoByTypeId(@RequestParam("typeId") Integer typeId) {
		return this.goodsServiceManager.getGoodsInfoByTypeId(typeId);
	}

	/**
	 * 描述：依据商品类别idList获取商品列表
	 * @author nongchongwei
	 * @date 2018/7/17 14:22
	 * @param
	 * @return
	 */
	@Override
	public List<GoodsInfo> getGoodsInfoByTypeIdList(@RequestParam("typeIdList") List<Integer> typeIdList) {
		return this.goodsServiceManager.getGoodsInfoByTypeIds(typeIdList);
	}

	/** 
	 * 描述：依据商品id获取商品附加服务列表
	 * @author maochengyuan
	 * @created 2018/7/3 16:41
	 * @param goodsId
	 * @return java.util.List<com.iot.payment.entity.goods.GoodsExtendService>
	 */
	@Override
	public List<GoodsExtendService> getGoodsExServiceByGoodsId(@RequestParam("goodsId") Long goodsId) {
		return this.goodsServiceManager.getGoodsExServiceByGoodsId(goodsId);
	}

	/** 
	 * 描述：依据商品id商品名称
	 * @author maochengyuan
	 * @created 2018/7/3 16:41
	 * @param goodsIds
	 * @return java.util.Map<java.lang.Integer,java.lang.String>
	 */
	@Override
	public Map<Long, String> getGoodsNameByGoodsId(@RequestBody List<Long> goodsIds) {
		return this.goodsServiceManager.getGoodsNameByGoodsId(goodsIds);
	}

	/** 
	 * 描述：依据商品id获取商品详情
	 * @author maochengyuan
	 * @created 2018/7/3 16:41
	 * @param goodsId
	 * @return com.iot.payment.entity.goods.GoodsInfo
	 */
	@Override
	public GoodsInfo getGoodsInfoByGoodsId(@RequestParam("goodsId") Long goodsId) {
		return this.goodsServiceManager.getGoodsInfoByGoodsId(goodsId);
	}

	/**
	 * @despriction：查询计划套餐列表
	 * @author  yeshiyuan
	 * @created 2018/7/17 10:29
	 * @return
	 */
	@Override
	public PageInfo<GoodsInfo> getVideoPackageList(@RequestBody VideoPackageReq videoPackageReq) {
		return this.goodsServiceManager.getVideoPackageList(videoPackageReq);
	}

	/**
	 * @despriction：依据订单商品id查询原商品信息
	 * @author  yeshiyuan
	 * @created 2018/10/29 16:27
	 * @return
	 */
	@Override
	public GoodsInfo getOldGoodsInfoByOrderGoodsId(Long orderGoodsId) {
		return goodsServiceManager.getOldGoodsInfoByOrderGoodsId(orderGoodsId);
	}

	@Override
	public GoodsInfo getGoodsInfoByGoodsCode(String goodsCode) {
		return goodsServiceManager.getGoodsInfoByGoodsCode(goodsCode);
	}

	@Override
	public List<GoodsInfo> getListGoodsInfoByGoodsCode(@RequestBody List<String> goodsCodes) {
		return goodsServiceManager.getListGoodsInfoByGoodsCode(goodsCodes);
	}
}
