package com.iot.payment.dao;

import com.github.pagehelper.Page;
import com.iot.payment.entity.goods.GoodsExtendService;
import com.iot.payment.entity.goods.GoodsInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface GoodsMapper {

	/**
	 * 描述：获取商品列表
	 * @author maochengyuan
	 * @created 2018/7/3 14:19
	 * @param
	 * @return java.util.List<com.iot.payment.entity.goods.GoodsInfo>
	 */
	@Select({ "select id," +
			" type_id as typeId," +
			" goods_code as goodsCode," +
			" goods_name as goodsName," +
			" icon," +
			" description," +
			" standard," +
			" standard_unit as standardUnit," +
			" price," +
			" currency," +
			" select_extend_service as selectExtendService," +
			" sort," +
			" create_time as createTime," +
			" update_time as updateTime," +
			" online_status as onlineStatus," +
			" data_status as dataStatus" +
			" from goods_info" +
			" order by type_id, sort asc" })
	List<GoodsInfo> getGoodsInfo();

	/**
	 * 描述：获取商品附加服务列表
	 * @author maochengyuan
	 * @created 2018/7/3 14:19
	 * @param
	 * @return java.util.List<com.iot.payment.entity.goods.GoodsInfo>
	 */
	@Select({ "select id," +
			" goods_id as goodsId," +
			" goods_ex_name as goodsExName," +
			" goods_ex_desc as goodsExDesc," +
			" price," +
			" currency," +
			" sort," +
			" create_time as createTime," +
			" update_time as updateTime," +
			" online_status as onlineStatus," +
			" data_status as dataStatus" +
			" from goods_extend_service" +
			" order by goods_id, sort asc" })
	List<GoodsExtendService> getGoodsExService();


	/**
	 * 描述：通过id获取商品附加服务信息
	 * @author maochengyuan
	 * @created 2018/7/3 14:19
	 * @param
	 * @return java.util.List<com.iot.payment.entity.goods.GoodsInfo>
	 */
	@Select({ "select id," +
			" goods_id as goodsId," +
			" goods_ex_name as goodsExName," +
			" goods_ex_desc as goodsExDesc," +
			" price," +
			" currency," +
			" sort," +
			" create_time as createTime," +
			" update_time as updateTime," +
			" online_status as onlineStatus," +
			" data_status as dataStatus" +
			" from goods_extend_service" +
			" where id = #{id} and online_status=1 and data_status=1" })
	GoodsExtendService getGoodsExServiceById(@Param("id") Long id);

	@Select({ "<script>" +
			"select id," +
			" type_id as typeId," +
			" goods_code as goodsCode," +
			" goods_name as goodsName," +
			" icon," +
			" description," +
			" standard," +
			" standard_unit as standardUnit," +
			" price," +
			" currency," +
			" select_extend_service as selectExtendService," +
			" sort," +
			" create_time as createTime," +
			" update_time as updateTime," +
			" online_status as onlineStatus," +
			" data_status as dataStatus" +
			" from goods_info " +
			" where type_id in " +
			" <foreach collection='packageTypes' item='typeId' separator=',' open='(' close=')'>" +
			" #{typeId,jdbcType=INTEGER} " +
			" </foreach>" +
			" and online_status=1 and data_status=1" +
			" order by sort asc " +
			"</script>" })
	Page<GoodsInfo> getVideoPackageList(@Param("packageTypes") List<Integer> packageTypes);

	/**
	 * @despriction：依据订单商品id查询原商品信息
	 * @author  yeshiyuan
	 * @created 2018/10/29 16:27
	 * @return
	 */
	@Select("select id, type_id as typeId,goods_code as goodsCode, " +
			"goods_name as goodsName, icon, description, standard, " +
			"standard_unit as standardUnit, price, currency, select_extend_service as selectExtendService," +
			"sort,create_time as createTime, update_time as updateTime, online_status as onlineStatus, data_status as dataStatus " +
			" from goods_info where id = (select goods_id from order_goods where id = #{orderGoodsId, jdbcType=BIGINT})")
	GoodsInfo getOldGoodsInfoByOrderGoodsId(@Param("orderGoodsId") Long orderGoodsId);

    /**
     * @descrpiction: 根据商品编号 获取商品信息
     * @author wucheng
     * @created 2018/11/8 11:33
     * @parma goodsCode
     * @return 
     */
	@Select("select id, type_id as typeId,goods_code as goodsCode, " +
			"goods_name as goodsName, icon, description, standard, " +
			"standard_unit as standardUnit, price, currency, select_extend_service as selectExtendService," +
			"sort,create_time as createTime, update_time as updateTime, online_status as onlineStatus, data_status as dataStatus " +
			" from goods_info where goods_code = #{goodsCode}")
	GoodsInfo getGoodsInfoByGoodsCode(@Param("goodsCode") String goodsCode);

	/**
	 * @descrpiction: 根据商品编号获取商品列表信息
	 * @author wucheng
	 * @created 2018/11/13 19:51
	 * @parma goodsCode
	 * @return List<GoodsInfo>
	 */
	@Select("<script> select id, type_id as typeId,goods_code as goodsCode, " +
			"goods_name as goodsName, icon, description, standard, " +
			"standard_unit as standardUnit, price, currency, select_extend_service as selectExtendService," +
			"sort,create_time as createTime, update_time as updateTime, online_status as onlineStatus, data_status as dataStatus " +
			" from goods_info where goods_code in " +
	        " <foreach item='goodsCode' index='index' collection='goodsCodes' open='(' separator=',' close=')'>" +
			"    #{goodsCode} " +
			"  </foreach> " +
			" </script>")
	List<GoodsInfo> getListGoodsInfoByGoodsCode(@Param("goodsCodes") List<String> goodsCodes);
}
