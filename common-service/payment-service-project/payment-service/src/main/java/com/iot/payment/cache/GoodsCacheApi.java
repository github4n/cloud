package com.iot.payment.cache;

import com.iot.common.util.CommonUtil;
import com.iot.payment.entity.goods.GoodsExtendService;
import com.iot.payment.entity.goods.GoodsInfo;
import com.iot.redis.RedisCacheUtil;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 项目名称：IOT云平台
 * 模块名称：支付服务
 * 功能描述：商品缓存
 * 创建人： maochengyuan
 * 创建时间：2018/6/28 20:17
 * 修改人： maochengyuan
 * 修改时间：2018/6/28 20:17
 * 修改描述：
 */
@Component
public class GoodsCacheApi {

    /**商品列表*/
    public static final String GOODS_SERVICE_INFO_BASE = "goods:service-info:base";

    /**商品附加服务列表*/
    public static final String GOODS_SERVICE_INFO_EXTEND = "goods:service-info:extend";

    /**
     * 描述：设置商品
     * @author maochengyuan
     * @created 2018/7/3 15:38
     * @param list 商品列表
     * @return void
     */
    public void setGoodsInfoCache(List<GoodsInfo> list){
        if(CommonUtil.isEmpty(list)){
            return ;
        }
        RedisCacheUtil.delete(GOODS_SERVICE_INFO_BASE);
        RedisCacheUtil.listSet(GOODS_SERVICE_INFO_BASE, list);
    }

    /**
     * 描述：依据商品类别id获取商品列表
     * @author maochengyuan
     * @created 2018/7/3 15:38
     * @param typeId 商品类别id
     * @return java.util.List<com.iot.payment.entity.goods.GoodsInfo>
     */
    public List<GoodsInfo> getGoodsInfoCacheByTypeId(Integer typeId){
        List<GoodsInfo> temp = RedisCacheUtil.listGetAll(GOODS_SERVICE_INFO_BASE, GoodsInfo.class);
        if(typeId == null){
            return temp == null?Collections.emptyList():temp;
        }
        return temp.stream().filter(o -> typeId.equals(o.getTypeId())).collect(Collectors.toList());
    }

    /**
     * 描述：依据商品类别id获取商品列表
     * @author maochengyuan
     * @created 2018/7/3 15:38
     * @param typeIds 商品类别id
     * @return java.util.List<com.iot.payment.entity.goods.GoodsInfo>
     */
    public List<GoodsInfo> getGoodsInfoCacheByTypeIds(List<Integer> typeIds){
        if(CommonUtil.isEmpty(typeIds)){
            return Collections.emptyList();
        }
        List<GoodsInfo> temp = RedisCacheUtil.listGetAll(GOODS_SERVICE_INFO_BASE, GoodsInfo.class);
        if(typeIds == null){
            return temp == null?Collections.emptyList():temp;
        }
        return temp.stream().filter(o -> typeIds.contains(o.getTypeId())).collect(Collectors.toList());
    }

    /**
     * 描述：依据id获取商品
     * @author maochengyuan
     * @created 2018/7/3 15:38
     * @param
     * @return java.util.List<com.iot.payment.entity.goods.GoodsInfo>
     */
    public GoodsInfo getGoodsInfoCacheByGoodsId(Long goodsId){
        if(goodsId == null){
            return null;
        }
        List<GoodsInfo> temp = RedisCacheUtil.listGetAll(GOODS_SERVICE_INFO_BASE, GoodsInfo.class);
        if(CommonUtil.isEmpty(temp)){
            return null;
        }
        temp = temp.stream().filter(o -> goodsId.equals(o.getId())).collect(Collectors.toList());
        return temp.isEmpty()?null:temp.get(0);
    }

    /**
     * 描述：设置商品附加服务
     * @author maochengyuan
     * @created 2018/7/3 15:38
     * @param list 商品附加服务列表
     * @return void
     */
    public void setGoodsExServiceCache(List<GoodsExtendService> list){
        if(CommonUtil.isEmpty(list)){
            return ;
        }
        RedisCacheUtil.delete(GOODS_SERVICE_INFO_EXTEND);
        RedisCacheUtil.listSet(GOODS_SERVICE_INFO_EXTEND, list);
    }

    /**
     * 描述：获取所有商品附加服务
     * @author maochengyuan
     * @created 2018/7/3 15:38
     * @param goodsId 商品id（为空查询全部）
     * @return java.util.List<com.iot.payment.entity.goods.GoodsInfo>
     */
    public List<GoodsExtendService> getGoodsExServiceCache(Long goodsId){
        List<GoodsExtendService> temp = RedisCacheUtil.listGetAll(GOODS_SERVICE_INFO_EXTEND, GoodsExtendService.class);
        if(goodsId == null){
            return temp == null?Collections.emptyList():temp;
        }
        return temp.stream().filter(o -> goodsId.equals(o.getGoodsId())).collect(Collectors.toList());
    }

}
