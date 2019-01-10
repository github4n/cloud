package com.iot.payment.service.impl;


import com.github.pagehelper.Page;
import com.iot.common.util.CommonUtil;
import com.iot.payment.cache.GoodsCacheApi;
import com.iot.payment.dao.GoodsMapper;
import com.iot.payment.entity.goods.GoodsExtendService;
import com.iot.payment.entity.goods.GoodsInfo;
import com.iot.payment.service.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/** 
 * 
 * 项目名称：IOT云平台
 * 模块名称：支付服务
 * 功能描述：商品服务接口实现
 * 创建人： maochengyuan 
 * 创建时间：2018/7/3 15:26
 * 修改人： maochengyuan
 * 修改时间：2018/7/3 15:26
 * 修改描述：
 */
@Service("goodsService")
public class GoodsServiceImpl implements GoodsService {

	@Autowired
	private GoodsCacheApi goodsCacheApi;

	@Autowired
	private GoodsMapper goodsMapper;

	/**
	 * 描述：依据商品类别id获取商品列表
	 * @author maochengyuan
	 * @created 2018/7/3 16:23
	 * @param typeId
	 * @return java.util.List<com.iot.payment.entity.goods.GoodsInfo>
	 */
	@Override
	public List<GoodsInfo> getGoodsInfoByTypeId(Integer typeId) {
		List<GoodsInfo> temp = this.goodsCacheApi.getGoodsInfoCacheByTypeId(typeId);
		if(CommonUtil.isEmpty(temp)){
			List<GoodsInfo> list = this.goodsMapper.getGoodsInfo();
			this.goodsCacheApi.setGoodsInfoCache(list);
			return list.stream().filter(o->o.getTypeId().equals(typeId)).collect(Collectors.toList());
		}
		return temp;
	}

	/**
	 * 描述：依据商品类别ids获取商品列表
	 * @author maochengyuan
	 * @created 2018/7/3 16:23
	 * @param typeIds
	 * @return java.util.List<com.iot.payment.entity.goods.GoodsInfo>
	 */
	@Override
	public List<GoodsInfo> getGoodsInfoByTypeIds(List<Integer> typeIds) {
		List<GoodsInfo> temp = this.goodsCacheApi.getGoodsInfoCacheByTypeIds(typeIds);
		if(CommonUtil.isEmpty(temp)){
			List<GoodsInfo> list = this.goodsMapper.getGoodsInfo();
			this.goodsCacheApi.setGoodsInfoCache(list);
			return list.stream().filter(o->typeIds.contains(o.getTypeId())).collect(Collectors.toList());
		}
		return temp;
	}


	/**
	 * 描述：依据商品id获取商品详情
	 * @author maochengyuan
	 * @created 2018/7/3 16:23
	 * @param goodsId
	 * @return com.iot.payment.entity.goods.GoodsInfo
	 */
	@Override
	public GoodsInfo getGoodsInfoByGoodsId(Long goodsId) {
		GoodsInfo goodsInfo = this.goodsCacheApi.getGoodsInfoCacheByGoodsId(goodsId);
		if(goodsInfo == null){
			List<GoodsInfo> list = this.goodsMapper.getGoodsInfo();
			this.goodsCacheApi.setGoodsInfoCache(list);
			list = list.stream().filter(o->o.getId().equals(goodsId)).collect(Collectors.toList());
			return list.isEmpty()?null:list.get(0);
		}
		return goodsInfo;
	}

	/**
	 * 描述：依据商品id获取商品附加服务列表
	 * @author maochengyuan
	 * @created 2018/7/3 16:24
	 * @param goodsId
	 * @return java.util.List<com.iot.payment.entity.goods.GoodsExtendService>
	 */
	@Override
	public List<GoodsExtendService> getGoodsExServiceByGoodsId(Long goodsId) {
		List<GoodsExtendService> temp = this.goodsCacheApi.getGoodsExServiceCache(goodsId);
		if(CommonUtil.isEmpty(temp)){
			List<GoodsExtendService> list = this.goodsMapper.getGoodsExService();
			this.goodsCacheApi.setGoodsExServiceCache(list);
			return list.stream().filter(o->o.getGoodsId().equals(goodsId)).collect(Collectors.toList());
		}
		return temp;
	}

	/**
	 * 描述：依据商品id商品名称
	 * @author maochengyuan
	 * @created 2018/7/3 16:25
	 * @param goodsIds
	 * @return java.util.Map<java.lang.Integer,java.lang.String>
	 */
	@Override
	public Map<Long, String> getGoodsNameByGoodsId(List<Long> goodsIds) {
		if(CommonUtil.isEmpty(goodsIds)){
			return Collections.emptyMap();
		}
		List<GoodsInfo> temp = this.goodsCacheApi.getGoodsInfoCacheByTypeId(null);
		if(CommonUtil.isEmpty(temp)){
			temp = this.goodsMapper.getGoodsInfo();
			this.goodsCacheApi.setGoodsInfoCache(temp);
			if(CommonUtil.isEmpty(temp)){
				return Collections.emptyMap();
			}
		}
		return temp.stream().filter(o->goodsIds.contains(o.getId())).collect(Collectors.toMap(GoodsInfo::getId, GoodsInfo::getGoodsName, (key1, key2) -> key2));
	}

	@Override
	public GoodsExtendService getGoodsExServiceById(Long id) {
		return goodsMapper.getGoodsExServiceById(id);
	}

	/**
	 * @despriction：获取计划套餐列表
	 * @author  yeshiyuan
	 * @created 2018/7/17 10:44
	 * @return
	 */
	@Override
	public Page<GoodsInfo> getVideoPackageList(List<Integer> packageTypes){
		return goodsMapper.getVideoPackageList(packageTypes);
	}

	/**
	 * @despriction：依据订单商品id查询原商品信息
	 * @author  yeshiyuan
	 * @created 2018/10/29 16:27
	 * @return
	 */
	@Override
	public GoodsInfo getOldGoodsInfoByOrderGoodsId(Long orderGoodsId) {
		return goodsMapper.getOldGoodsInfoByOrderGoodsId(orderGoodsId);
	}
    /**
     * @descrpiction: 根据商品编码 查询商品信息
     * @author wucheng
     * @created 2018/11/8 11:35
     * @param
     * @return
     */
	@Override
	public GoodsInfo getGoodsInfoByGoodsCode(String goodsCode) {
		return goodsMapper.getGoodsInfoByGoodsCode(goodsCode);
	}
	/**
	 * @descrpiction: 根据商品编码 查询商品列表信息
	 * @author wucheng
	 * @created 2018/11/8 11:35
	 * @param goodsCodes
	 * @return
	 */
	@Override
	public List<GoodsInfo> getListGoodsInfoByGoodsCode(List<String> goodsCodes) {
		return goodsMapper.getListGoodsInfoByGoodsCode(goodsCodes);
	}
}
