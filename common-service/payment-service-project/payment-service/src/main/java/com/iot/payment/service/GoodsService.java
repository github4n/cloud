package com.iot.payment.service;

import com.github.pagehelper.Page;
import com.iot.payment.entity.goods.GoodsExtendService;
import com.iot.payment.entity.goods.GoodsInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 *
 * 项目名称：IOT云平台
 * 模块名称：支付服务
 * 功能描述：商品服务接口
 * 创建人： mao2080@sina.com
 * 创建时间：2018/7/3 14:36
 * 修改人： mao2080@sina.com
 * 修改时间：2018/7/3 14:36
 * 修改描述：
 */
public interface GoodsService {

	/** 
	 * 描述：依据商品类别获取商品列表（只查询上线及有效商品）
	 * @author maochengyuan
	 * @created 2018/7/3 15:21
	 * @param typeId 商品类别id
	 * @return java.util.List<com.iot.payment.entity.goods.GoodsInfo>
	 */
	List<GoodsInfo> getGoodsInfoByTypeId(Integer typeId);

	/**
	 * 描述：依据商品类别ids获取商品列表
	 * @author maochengyuan
	 * @created 2018/7/3 16:23
	 * @param typeIds
	 * @return java.util.List<com.iot.payment.entity.goods.GoodsInfo>
	 */
	List<GoodsInfo> getGoodsInfoByTypeIds(List<Integer> typeIds);

	/**
	 * 描述：依据商品id获取商品信息
	 * @author maochengyuan
	 * @created 2018/7/3 15:21
	 * @param goodsId 商品id
	 * @return java.util.List<com.iot.payment.entity.goods.GoodsInfo>
	 */
	GoodsInfo getGoodsInfoByGoodsId(Long goodsId);

	/** 
	 * 描述：依据商品类别获取商品附加服务列表（只查询上线及有效商品附加服务）
	 * @author maochengyuan
	 * @created 2018/7/3 15:21
	 * @param goodsId 商品id
	 * @return java.util.List<com.iot.payment.entity.goods.GoodsExtendService>
	 */
	List<GoodsExtendService> getGoodsExServiceByGoodsId(Long goodsId);

	/** 
	 * 描述：依据商品id获取商品名称
	 * @author maochengyuan
	 * @created 2018/7/3 15:21
	 * @param goodsIds 商品id集合
	 * @return java.util.Map<java.lang.Integer,java.lang.String>
	 */
	Map<Long, String> getGoodsNameByGoodsId(List<Long> goodsIds);

	/**
	  * @despriction：依据id获取商品附加服务信息
	  * @author  yeshiyuan
	  * @created 2018/7/3 17:42
	  * @param id
	  * @return
	  */
	GoodsExtendService getGoodsExServiceById(Long id);

	/**
	  * @despriction：获取计划套餐列表
	  * @author  yeshiyuan
	  * @created 2018/7/17 10:44
	  * @return
	  */
	Page<GoodsInfo> getVideoPackageList(List<Integer> packageTypes);

	/**
	 * @despriction：依据订单商品id查询原商品信息
	 * @author  yeshiyuan
	 * @created 2018/10/29 16:27
	 * @return
	 */
	GoodsInfo getOldGoodsInfoByOrderGoodsId(Long orderGoodsId);

	/**
	 * @descrpiction: 根据商品编码 查询商品信息
	 * @author wucheng
	 * @created 2018/11/8 11:34
	 * @param
	 * @return
	 */
	GoodsInfo getGoodsInfoByGoodsCode(String goodsCode);
	/**
	 * @descrpiction: 根据商品编号获取商品列表信息
	 * @author wucheng
	 * @created 2018/11/13 19:51
	 * @parma goodsCodes
	 * @return List<GoodsInfo>
	 */
	List<GoodsInfo> getListGoodsInfoByGoodsCode(List<String> goodsCodes);
}
