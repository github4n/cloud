package com.iot.payment.manager;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.iot.common.exception.BusinessException;
import com.iot.payment.entity.goods.GoodsExtendService;
import com.iot.payment.entity.goods.GoodsInfo;
import com.iot.payment.exception.BusinessExceptionEnum;
import com.iot.payment.service.GoodsService;
import com.iot.payment.vo.goods.req.VideoPackageReq;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 *
 * 项目名称：IOT云平台
 * 模块名称：支付服务
 * 功能描述：商品服务Manager
 * 创建人： maochengyuan
 * 创建时间：2018/7/3 16:26
 * 修改人： maochengyuan
 * 修改时间：2018/7/3 16:26
 * 修改描述：
 */
@Service("goodsServiceManager")
public class GoodsServiceManager {

	private static final Logger logger = LoggerFactory.getLogger(GoodsServiceManager.class);

	@Autowired
	private GoodsService goodsService;

	/**
	 * 描述：依据商品类别id获取商品列表
	 *
	 * @param typeId
	 * @return java.util.List<com.iot.payment.entity.goods.GoodsInfo>
	 * @author maochengyuan
	 * @created 2018/7/3 16:23
	 */
	public List<GoodsInfo> getGoodsInfoByTypeId(Integer typeId) {
		return this.goodsService.getGoodsInfoByTypeId(typeId);
	}

	/**
	 * 描述：依据商品类别ids获取商品列表
	 *
	 * @param typeIds
	 * @return java.util.List<com.iot.payment.entity.goods.GoodsInfo>
	 * @author maochengyuan
	 * @created 2018/7/3 16:23
	 */
	public List<GoodsInfo> getGoodsInfoByTypeIds(List<Integer> typeIds) {
		return this.goodsService.getGoodsInfoByTypeIds(typeIds);
	}

	/**
	 * 描述：依据商品id获取商品详情
	 *
	 * @param goodsId
	 * @return com.iot.payment.entity.goods.GoodsInfo
	 * @author maochengyuan
	 * @created 2018/7/3 16:23
	 */
	public GoodsInfo getGoodsInfoByGoodsId(Long goodsId) {
		return this.goodsService.getGoodsInfoByGoodsId(goodsId);
	}

	/**
	 * 描述：依据商品id获取商品附加服务列表
	 *
	 * @param goodsId
	 * @return java.util.List<com.iot.payment.entity.goods.GoodsExtendService>
	 * @author maochengyuan
	 * @created 2018/7/3 16:24
	 */
	public List<GoodsExtendService> getGoodsExServiceByGoodsId(Long goodsId) {
		return this.goodsService.getGoodsExServiceByGoodsId(goodsId);
	}

	/**
	 * 描述：依据商品id商品名称
	 *
	 * @param goodsIds
	 * @return java.util.Map<java.lang.Integer,java.lang.String>
	 * @author maochengyuan
	 * @created 2018/7/3 16:25
	 */
	public Map<Long, String> getGoodsNameByGoodsId(List<Long> goodsIds) {
		return this.goodsService.getGoodsNameByGoodsId(goodsIds);
	}

	/**
	 * @return
	 * @despriction：查询计划套餐列表
	 * @author yeshiyuan
	 * @created 2018/7/17 10:29
	 */
	public PageInfo<GoodsInfo> getVideoPackageList(VideoPackageReq videoPackageReq) {
		if (null == videoPackageReq) {
			throw new BusinessException(BusinessExceptionEnum.EMPTY_OF_GOODSID);
		}
		PageHelper.startPage(videoPackageReq.getPageNum(), videoPackageReq.getPageSize(), true);
		Page<GoodsInfo> page = goodsService.getVideoPackageList(videoPackageReq.getPackageTypeList());
		page.setPageNum(videoPackageReq.getPageNum());
		page.setPageSize(videoPackageReq.getPageSize());
		PageInfo<GoodsInfo> pageInfo = new PageInfo<>(page);
		return pageInfo;
	}


	/**
	 * @return
	 * @despriction：依据订单商品id查询原商品信息
	 * @author yeshiyuan
	 * @created 2018/10/29 16:27
	 */
	public GoodsInfo getOldGoodsInfoByOrderGoodsId(Long orderGoodsId) {
		return goodsService.getOldGoodsInfoByOrderGoodsId(orderGoodsId);
	}

	/**
	 * @descrpiction: 根据商品编码 查询商品信息
	 * @author wucheng
	 * @created 2018/11/8 11:34
	 * @param
	 * @return
	 */
	public GoodsInfo getGoodsInfoByGoodsCode(String goodsCode) {
		return goodsService.getGoodsInfoByGoodsCode(goodsCode);
	}
	/**
	 * @descrpiction: 根据商品编码查询商品列表信息
	 * @author wucheng
	 * @created 2018/11/8 11:34
	 * @param goodsCodes
	 * @return
	 */
	public List<GoodsInfo> getListGoodsInfoByGoodsCode(List<String> goodsCodes) {
		return goodsService.getListGoodsInfoByGoodsCode(goodsCodes);
	}
}