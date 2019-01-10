package com.iot.device.mapper;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.iot.device.vo.rsp.product.PackageProductNameResp;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.iot.device.mapper.sql.ProductSqlProvider;
import com.iot.device.model.DeviceCatalog;
import com.iot.device.model.DeviceType;
import com.iot.device.model.PackageProduct;
import com.iot.device.model.Product;
import com.iot.device.model.ProductPublishHistory;
import com.iot.device.vo.req.product.ProductAuditPageReq;
import com.iot.device.vo.req.product.ProductConfirmReleaseReq;
import com.iot.device.vo.req.servicereview.ServiceAuditPageReq;
import com.iot.device.vo.rsp.ProductResp;
import com.iot.device.vo.rsp.product.ProductAuditListResp;
import com.iot.device.vo.rsp.product.ProductMinimumSubsetResp;
import com.iot.device.vo.rsp.servicereview.ServiceAuditListResp;

/**
 * <p>
  *  Mapper 接口
 * </p>
 *
 * @author lucky
 * @since 2018-04-12
 */
@Mapper
public interface ProductMapper extends BaseMapper<Product> {

//	 	private Long id;
//	    private Long deviceTypeId;
//	    private String productName;
//	    private Integer communicationMode;
//	    private Integer transmissionMode;
//	    private Date createTime;
//	    private Date updateTime;
//	    private String model;
//	    private String remark;
//	    private String configNetMode;
//		private Integer isDirectDevice;
//	    private Integer isKit;
//	    private String deviceTypeName;
//	    private String catalogName;
	@SelectProvider(type = ProductSqlProvider.class,method = "findProductListByTenantId")
	@Results({
		@Result(property = "id", column = "id"),
        @Result(property = "deviceTypeId", column = "device_type_id"),
        @Result(property = "productName", column = "product_name"),
        @Result(property = "communicationMode", column = "communication_mode"),
        @Result(property = "transmissionMode", column = "transmission_mode"),
        @Result(property = "createTime", column = "create_time", jdbcType = JdbcType.DATE),
		@Result(property = "updateTime", column = "update_ime", jdbcType = JdbcType.DATE),
			@Result(property = "remark", column = "remark"),
			@Result(property = "auditStatus", column = "auditStatus"),

		@Result(property = "isDirectDevice", column = "is_direct_device"),
		@Result(property = "isKit", column = "is_kit"),
		@Result(property = "deviceTypeName", column = "deviceTypeName"),
		@Result(property = "catalogName", column = "catalogName"),
		@Result(property = "auditStatus", column = "audit_status")
    })
	List<ProductResp> findProductListByTenantId(@Param("tenantId") Long tenantId, @Param("productName") String productName);

	@SelectProvider(type = ProductSqlProvider.class,method = "findDirectProductListByTenantId")
	@Results({
		@Result(property = "id", column = "id"),
        @Result(property = "deviceTypeId", column = "device_type_id"),
        @Result(property = "productName", column = "product_name"),
        @Result(property = "communicationMode", column = "communication_mode"),
        @Result(property = "transmissionMode", column = "transmission_mode"),
        @Result(property = "createTime", column = "create_time", jdbcType = JdbcType.DATE),
		@Result(property = "updateTime", column = "update_ime", jdbcType = JdbcType.DATE),
		@Result(property = "remark", column = "remark"),
//		@Result(property = "configNetMode", column = "config_net_mode"),
		@Result(property = "isDirectDevice", column = "is_direct_device"),
		@Result(property = "isKit", column = "is_kit"),
		@Result(property = "deviceTypeName", column = "deviceTypeName"),
		@Result(property = "catalogName", column = "catalogName")
    })
	List<ProductResp> findDirectProductListByTenantId(@Param("tenantId") Long tenantId, @Param("productName") String productName);
	
	@SelectProvider(type = ProductSqlProvider.class,method = "findAllDirectProductList")
	@Results({
			@Result(property = "id", column = "id"),
			@Result(property = "deviceTypeId", column = "device_type_id"),
			@Result(property = "productName", column = "product_name"),
			@Result(property = "communicationMode", column = "communication_mode"),
			@Result(property = "transmissionMode", column = "transmission_mode"),
			@Result(property = "createTime", column = "create_time", jdbcType = JdbcType.DATE),
			@Result(property = "updateTime", column = "update_ime", jdbcType = JdbcType.DATE),
			@Result(property = "remark", column = "remark"),
//			@Result(property = "configNetMode", column = "config_net_mode"),
			@Result(property = "isDirectDevice", column = "is_direct_device"),
			@Result(property = "isKit", column = "is_kit"),
			@Result(property = "deviceTypeName", column = "deviceTypeName"),
			@Result(property = "catalogName", column = "catalogName")
	})
	List<ProductResp> findAllDirectProductList();

	@Select(
			value = "SELECT "
					+ " a.*"
					+ " ,b.name AS deviceTypeName "
					+ " ,b.whether_soc AS whetherSoc "
					+ " ,c.id AS deviceCatalogId "
					+ " ,c.name AS deviceCatalogName "
					+ " FROM "
					+ " " + Product.TABLE_NAME + " a "
					+ " LEFT JOIN " + DeviceType.TABLE_NAME + " b ON a.device_type_id = b.id "
					+ " LEFT JOIN " + DeviceCatalog.TABLE_NAME + " c ON b.device_catalog_id = c.id "
					+ " where 1 =1 "
					+ " ${ew.sqlSegment} "
	)
//	@SelectProvider(type = ProductSqlProvider.class, method = "selectProductPage")
	List<Product> selectProductPage(@Param("page") Page<Product> page, @Param("ew") EntityWrapper ew);

	@Select({
			"select ",
			"p2.whether_soc as whetherSoc, ",
			"p1.id as id, ",
			"p1.device_type_id as deviceTypeId, ",
			"p1.product_name as productName, ",
			"p1.communication_mode as communicationMode,",
			"p1.transmission_mode AS transmissionMode,",
			"p1.create_time AS createTime,",
			"p1.update_time AS updateTime,",
			"p1.remark AS remark,",
			"p1.config_net_mode AS configNetMode,",
			"p1.is_direct_device AS isDirectDevice,",
			"p1.is_kit as isKit",
			" from product p1 " +
					" left join "+DeviceType.TABLE_NAME+" AS p2 on (p1.device_type_id = p2.id) ",
			"where p1.device_type_id=#{deviceTypeId}			  "
	})
    ProductResp findProductByDeviceTypeId(Long deviceTypeId);

	@Select("<script>" +
			"SELECT p1.id as id, p1.product_name as productName, p1.model as model, p1.icon AS icon, p1.is_direct_device as isDirectDevice from product p1 " +
			"  where p1.tenant_id=#{tenantId}" +
			" <if test=\"productName != null\">" +
			" and p1.product_name LIKE  CONCAT(CONCAT('%',#{productName}),'%')" +
			"</if>" +
			" <if test=\"model != null\">" +
			" and p1.model LIKE  CONCAT(CONCAT('%',#{model}),'%')" +
			"</if>" +
			"</script>")
	List<ProductResp> findProductBytenantId(com.baomidou.mybatisplus.plugins.Page<ProductResp> page,@Param("tenantId")Long tenantId , @Param("productName")String productName, @Param("model")String model);

	/**
	  * @despriction：产品确认发布
	  * @author  yeshiyuan
	  * @created 2018/9/12 15:11
	  * @return
	  */
	@Update("update product set product_name=#{req.productName},model=#{req.model},icon=#{req.icon}," +
			" develop_status=#{newStatus,jdbcType=INTEGER}, remark=#{req.remark},audit_status=0,update_time = now(), apply_audit_time = now() " +
			" where id=#{req.productId} and develop_status=#{oldStatus,jdbcType=INTEGER} and tenant_id=#{req.tenantId}")
	int confirmRelease(@Param("req") ProductConfirmReleaseReq req, @Param("oldStatus") int oldStatus, @Param("newStatus") int newStatus);

	@Insert({ "<script>", 
		"insert into product_publish_history " + 
		"    <trim prefix=\"(\" suffix=\")\" suffixOverrides=\",\" >" + 
		"      <if test=\"tenantId != null\" > tenant_id, </if>" + 
		"      <if test=\"productId != null\" > product_id, </if>" + 
		" publish_time," + 
		"      <if test=\"publishStatus != null\" > publish_status, </if>" + 
		"      <if test=\"failureReason != null\" > failure_reason, </if>" + 
		"      <if test=\"createBy != null\" > create_by, </if>" + 
		" create_time," +
		" is_deleted," +
		"    </trim>" +
		"    <trim prefix=\"values (\" suffix=\")\" suffixOverrides=\",\" >" +
		"      <if test=\"tenantId != null\" > #{tenantId,jdbcType=BIGINT}, </if>" + 
		"      <if test=\"productId != null\" > #{productId,jdbcType=BIGINT}, </if>" + 
		" now(), " + 
		"      <if test=\"publishStatus != null\" > #{publishStatus,jdbcType=VARCHAR}, </if>" + 
		"      <if test=\"failureReason != null\" > #{failureReason,jdbcType=VARCHAR}, </if>" + 
		"      <if test=\"createBy != null\" > #{createBy,jdbcType=VARCHAR}, </if>" + 
		" now(), " +
		" 'valid', " +
		"    </trim>",
		"</script>" })
	void addProductPublish(ProductPublishHistory req);
	
	/**
	 * 
	 * 描述：通过租户ID获取租户地址信息
	 * @author 李帅
	 * @created 2018年9月11日 下午2:25:20
	 * @since 
	 * @param tenantId
	 * @return
	 */
	@Select({ "<script>", 
		"SELECT " + 
		"	id AS id, " + 
		"	tenant_id AS tenantId, " + 
		"	product_id AS productId, " + 
		"	publish_time AS publishTime, " + 
		"	publish_status AS publishStatus, " + 
		"	failure_reason AS failureReason, " + 
		"	create_by AS createBy, " + 
		"	create_time AS createTime, " + 
		"	update_by AS updateBy, " + 
		"	update_time AS updateTime, " + 
		"	is_deleted AS isDeleted " + 
		"FROM " + 
		"	product_publish_history " + 
		"where product_id = #{productId} and tenant_id = #{tenantId} and is_deleted = 'valid' ", 
		"</script>" })
	List<ProductPublishHistory> getProductPublishHis(@Param("productId") Long productId, @Param("tenantId") Long tenantId);

	@Select("<script>" +
			"SELECT p.id as productId ,p.product_name as productName, p.tenant_id as tenantId, p.audit_status as auditStatus, p.model," +
			" d.name as deviceTypeName, p.apply_audit_time as applyTime " +
			" from product p " +
			" left join device_type d on p.device_type_id = d.id" +
			"<trim prefix=\"WHERE\" prefixOverrides='and'>" +
			" <if test='pageReq.type==0'>" +
			"  and p.audit_status = 0" +
			" </if>" +
			" <if test='pageReq.type==1'>" +
			"   and p.audit_status in (1,2)"  +
			" </if>" +
			" <if test='pageReq.createBy != null'>" +
			"   and p.create_by = #{pageReq.createBy}"  +
			" </if>" +
			" <if test=\"pageReq.productName !=null and pageReq.productName!=''\">" +
			"  and p.product_name like concat('%',#{pageReq.productName},'%')" +
			" </if>" +
			" <if test=\"pageReq.tenantIds!=null and pageReq.tenantIds.size!=0\">" +
			"  and p.tenant_id in " +
			"  <foreach collection='pageReq.tenantIds' open='(' close=')' separator=',' item='tenantId'>" +
			"    #{tenantId}" +
			"  </foreach>" +
			" </if>" +
			"</trim>" +
			" order by p.apply_audit_time desc " +
			"</script>")
	List<ProductAuditListResp> queryProductAuditList(Page page, @Param("pageReq")ProductAuditPageReq pageReq);

	@Select("<script>" +
	        "SELECT t.productId as productId ,t.createBy as createBy, t.productName as productName, t.tenantId as tenantId, t.auditStatus as auditStatus, t.model as model, " +
	        " t.deviceTypeName as deviceTypeName , t.serviceDesc as serviceDesc, t.orderId as orderId " +
	        "from( " +
			"SELECT p.id as productId , p.create_by as createBy, p.product_name as productName, p.tenant_id as tenantId, p.service_goo_audit_status as auditStatus, p.model," +
			" d.name as deviceTypeName, 'Google Assistant' as serviceDesc, t.order_id as orderId, t.create_time as createTime, t.goods_id as goodsId " +
			" from product p " +
			" left join device_type d on p.device_type_id = d.id" +
			" LEFT JOIN service_buy_record t ON t.goods_id = #{pageReq.googleAssistantId} AND t.tenant_id = p.tenant_id AND t.service_id = p.id and t.pay_status != 5 " +
			"<trim prefix=\"WHERE\" prefixoverride=\"AND\">" +
			" <if test='pageReq.type==0'>" +
			"  p.service_goo_audit_status = 0" +
			" </if>" +
			" <if test='pageReq.type==1'>" +
			"   p.service_goo_audit_status in (1,2)"  +
			" </if>" +
			" <if test=\"pageReq.productParam != null and pageReq.productParam != ''\">" +
			"  and p.product_name like concat('%',#{pageReq.productParam},'%')" +
			" </if>" +
			" <if test=\"pageReq.tenantIds != null and pageReq.tenantIds.size != 0\">" +
			"  and p.tenant_id in " +
			"  <foreach collection='pageReq.tenantIds' open='(' close=')' separator=',' item='tenantId'>" +
			"    #{tenantId}" +
			"  </foreach>" +
			" </if>" +
			"</trim>" +
			" UNION ALL " +
			"SELECT p.id as productId , p.create_by as createBy, p.product_name as productName, p.tenant_id as tenantId, p.service_alx_audit_status as auditStatus, p.model," +
			" d.name as deviceTypeName, 'Amazon Alexa' as serviceDesc, t.order_id as orderId, t.create_time as createTime , t.goods_id as goodsId " +
			" from product p " +
			" left join device_type d on p.device_type_id = d.id" +
			" LEFT JOIN service_buy_record t ON t.goods_id = #{pageReq.amazonAlexaId} AND t.tenant_id = p.tenant_id AND t.service_id = p.id and t.pay_status != 5 " +
			"<trim prefix=\"WHERE\" prefixoverride=\"AND\">" +
			" <if test='pageReq.type==0'>" +
			"  p.service_alx_audit_status = 0" +
			" </if>" +
			" <if test='pageReq.type==1'>" +
			"   p.service_alx_audit_status in (1,2)"  +
			" </if>" +
			" <if test=\"pageReq.productParam != null and pageReq.productParam != ''\">" +
			"  and p.product_name like concat('%',#{pageReq.productParam},'%') " +
			" </if>" +
			" <if test=\"pageReq.tenantIds != null and pageReq.tenantIds.size != 0\">" +
			"  and p.tenant_id in " +
			"  <foreach collection='pageReq.tenantIds' open='(' close=')' separator=',' item='tenantId'>" +
			"    #{tenantId}" +
			"  </foreach>" +
			" </if>" +
			"</trim>" +
			")t where 1=1 " +
			" <if test=\"pageReq.orderId != null and pageReq.orderId !=''\"> AND t.orderId like CONCAT('%', #{pageReq.orderId}, '%')</if>" +
			" <if test='pageReq.createBy != null'> AND t.createBy=#{pageReq.createBy}</if>" +
			" <if test='pageReq.serviceType != null'> AND t.goodsId=#{pageReq.serviceType}</if>" +
			" ORDER BY t.createTime desc" +
			"</script>")
	List<ServiceAuditListResp> queryServiceAuditList(Page page, @Param("pageReq") ServiceAuditPageReq pageReq);
	
	@Update("<script>" +
			" update product set audit_status = #{auditStatus},update_time = #{updateTime}, apply_audit_time=#{updateTime}" +
			" <if test='developStatus!=null'>" +
			" ,develop_status = #{developStatus}" +
			" </if>" +
			" where id = #{productId}" +
			"</script>")
	int updateAuditStatus(@Param("productId") Long productId, @Param("auditStatus") Integer auditStatus,
						  @Param("developStatus")Integer developStatus, @Param("updateTime")Date updateTime);

	/**
	 * 根据id更新 gooAuditStatus 和 alxAuditStatus 审核状态
	 * @author wucheng
	 * @param flag 0: 谷歌 1：alexa
	 * @param productId
	 * @param gooAuditStatus
	 * @param alxAuditStatus
	 * @create 2018-11-15 14:43
	 * @return
	 */
	@Update("<script>" +
			" update product <set>" +
			" <if test='flag == 0'>service_goo_audit_status = #{gooAuditStatus},</if>" +
			" <if test='flag == 1'>service_alx_audit_status = #{alxAuditStatus},</if>" +
			" update_time = now() " +
			" </set>" +
			" where id = #{productId}" +
			"</script>")
	int updateServiceGooAndAlxAuditStatus(@Param("productId") Long productId,@Param("flag") int flag, @Param("gooAuditStatus") Integer gooAuditStatus, @Param("alxAuditStatus") Integer alxAuditStatus);

    /**
     * @descrpiction: 根据租户tenantId 和 CommunicationMode 获取设备列表
     * @author wucheng
     * @created 2018/11/8 14:27
     * @param
     * @return
     */
	@SelectProvider(type = ProductSqlProvider.class,method = "getProductListByTenantIdAndCommunicationMode")
	@Results({
			@Result(property = "id", column = "id"),
			@Result(property = "tenantId", column = "tenant_id"),
			@Result(property = "deviceTypeId", column = "device_type_id"),
			@Result(property = "productName", column = "product_name"),
			@Result(property = "communicationMode", column = "communication_mode"),
			@Result(property = "transmissionMode", column = "transmission_mode"),
			@Result(property = "createTime", column = "create_time", jdbcType = JdbcType.DATE),
			@Result(property = "updateTime", column = "update_ime", jdbcType = JdbcType.DATE),
			@Result(property = "model", column = "model"),
			@Result(property = "icon", column = "icon"),
			@Result(property = "configNetMode", column = "config_net_mode"),
			@Result(property = "isKit", column = "is_kit"),
			@Result(property = "remark", column = "remark"),
			@Result(property = "isDirectDevice", column = "is_direct_device"),
			@Result(property = "developStatus", column = "develop_status"),
			@Result(property = "enterpriseDevelopId", column = "enterprise_develop_id"),
			@Result(property = "auditStatus", column = "audit_status"),
			@Result(property = "serviceGooAuditStatus", column = "service_goo_audit_status"),
			@Result(property = "serviceAlxAuditStatus", column = "service_alx_audit_status"),
			@Result(property = "myDeviceType", column = "myDeviceType"),
			@Result(property = "isSelected", column = "isSelected")
	})
	List<ProductMinimumSubsetResp> getProductListByTenantIdAndCommunicationMode(@Param("tenantId") Long tenantId, @Param("communicationMode") Long communicationMode);

	/**
	 * 
	 * 描述：查询套包产品
	 * @author 李帅
	 * @created 2018年12月10日 下午3:42:38
	 * @since 
	 * @param tenantId
	 * @return
	 */
	@Select("<script>" +
			"SELECT " +
				"t.id AS id, " +
				"t.tenant_id AS tenantId, " +
				"t.device_type_id AS deviceTypeId, " +
				"t.product_name AS productName, " +
				"t.communication_mode AS communicationMode, " +
				"t.transmission_mode AS transmissionMode, " +
				"t.create_time AS createTime, " +
				"t.update_time AS updateTime, " +
				"t.model AS model, " +
				"t.config_net_mode AS configNetMode, " +
				"t.is_kit AS isKit, " +
				"t.remark AS remark, " +
				"t.is_direct_device AS isDirectDevice, " +
				"t.icon AS icon, " +
				"t.develop_status AS developStatus, " +
				"t.enterprise_develop_id AS enterpriseDevelopId, " +
				"t.audit_status AS auditStatus, " +
				"t.service_goo_audit_status AS serviceGooAuditStatus, " +
				"t.service_alx_audit_status AS serviceAlxAuditStatus, " +
				"t.apply_audit_time AS applyAuditTime, " +
				"t.create_by AS createBy, " +
				"t1.device_catalog_id AS deviceCatalogId " +
			"FROM " +
				"product t, " +
				"device_type t1 " +
			"WHERE " +
				"t.device_type_id = t1.id " +
			"AND t.tenant_id = #{tenantId} " +
			"AND t.communication_mode IN (1, 4) " +
			"</script>")
	List<PackageProduct> getPackageProducts(@Param("tenantId") Long tenantId);
	
	/**
	 * 
	 * 描述：查询网管子产品
	 * @author 李帅
	 * @created 2018年12月10日 下午6:11:40
	 * @since 
	 * @param tenantId
	 * @param productId
	 * @return
	 */
	@Select("<script>" +
			"SELECT " +
				"t.id AS id, " +
				"t.tenant_id AS tenantId, " +
				"t.device_type_id AS deviceTypeId, " +
				"t.product_name AS productName, " +
				"t.communication_mode AS communicationMode, " +
				"t.transmission_mode AS transmissionMode, " +
				"t.create_time AS createTime, " +
				"t.update_time AS updateTime, " +
				"t.model AS model, " +
				"t.config_net_mode AS configNetMode, " +
				"t.is_kit AS isKit, " +
				"t.remark AS remark, " +
				"t.is_direct_device AS isDirectDevice, " +
				"t.icon AS icon, " +
				"t.develop_status AS developStatus, " +
				"t.enterprise_develop_id AS enterpriseDevelopId, " +
				"t.audit_status AS auditStatus, " +
				"t.service_goo_audit_status AS serviceGooAuditStatus, " +
				"t.service_alx_audit_status AS serviceAlxAuditStatus, " +
				"t.apply_audit_time AS applyAuditTime, " +
				"t.create_by AS createBy " +
			"FROM " +
				"gateway_subdev_relation t1, " +
				"product t " +
			"WHERE " +
				"t.id = t1.subdev_id " +
			"AND t1.tenant_id = #{tenantId} " +
			"AND t1.pardev_id = #{productId} " +
			"</script>")
	List<PackageProduct> getGatewayChildProducts(@Param("tenantId") Long tenantId, @Param("productId") Long productId);
    /**
     *@description 套包根据ids查询产品的名称
     *@author wucheng
     *@params [ids]
     *@create 2018/12/12 16:58
     *@return java.util.List<java.util.Map<java.lang.Long,java.lang.String>>
     */
	@Select("<script>" +
			"SELECT " +
			"t.id AS id, " +
			"t.product_name AS productName," +
			"t.device_type_id as deviceTypeId " +
			"FROM " +
			"product t " +
			"WHERE " +
			" t.id  in" +
			" <foreach index='index' item='id' collection='ids' open='(' separator=',' close=')'>" +
			"    #{id}" +
            " </foreach>" +
			"</script>")
	List<PackageProductNameResp>  getProductByIds(@Param("ids") List<Long> ids);
}