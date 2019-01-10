package com.iot.device.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.iot.common.exception.BusinessException;
import com.iot.device.dto.UUIDInfoDto;
import com.iot.device.dto.UUIDOrderDto;
import com.iot.device.model.CustUuidManage;
import com.iot.device.model.UUidApplyRecord;
import com.iot.device.vo.req.uuid.GenerateUUID;
import com.iot.device.vo.req.uuid.GetUUIDOrderReq;
import com.iot.device.vo.req.uuid.GetUUIDReq;
import com.iot.device.vo.req.uuid.UUIDRefundReq;

@Mapper
public interface UUIDManegerMapper {


	//	/**
//	 *
//	 * 描述：UUID生成列表
//	 * @author 李帅
//	 * @created 2018年5月25日 下午5:47:45
//	 * @since
//	 * @param custUuidManageReq
//	 * @return
//	 * @throws Exception
//	 */
//	@Select("<script>"
//            +"SELECT " +
//            "	id AS id, " +
//            "	batch_num AS batchNumId, " +
//            "	create_time AS createTime, " +
//            "	cust_code AS custCode, " +
//            "	cust_name AS custName, " +
//            "	uuid_type AS uuidType, " +
//            "	down_loaded_num AS downLoadedNum, " +
//            "	uuid_amount AS uuidAmount, " +
//            "	status AS status, " +
//            "	file_id AS fileId " +
//            "FROM	cust_uuid_manage where 1=1 "//and status in (1,2)"
//            +"<if test=\"batchNumId != null and batchNumId != ''\"> and batch_num=#{batchNumId,jdbcType=VARCHAR}</if>"
//            +"<if test=\"custCode != null and custCode != ''\"> and cust_code=#{custCode,jdbcType=VARCHAR}</if>"
//            +"<if test=\"custName != null and custName != ''\"> and cust_name=#{custName,jdbcType=VARCHAR}</if>"
//            +"<if test=\"uuidType != null and uuidType != ''\"> and uuid_type=#{uuidType,jdbcType=VARCHAR}</if>"
//            +"<if test=\"uuidMark != null and uuidMark == '0'.toString()\"> and down_loaded_num = 0</if>"
//            +"<if test=\"uuidMark != null and uuidMark == '1'.toString()\"> and down_loaded_num > 0</if>"
//            + "</script>")
//	public List<CustUuidManage> getUUIDGenerateList(CustUuidManageReq custUuidManageReq) throws Exception;

	/**
	 *
	 * 描述：通过批次号获取证书下载URL
	 * @author 李帅
	 * @created 2018年6月8日 下午2:08:47
	 * @since
	 * @param batchNum
	 * @return
	 * @throws Exception
	 */
	@Select("<script>"
			+"select id as batchNumId, " +
			"tenant_id as tenantId, " +
			"uuid_apply_status as status, " +
			"file_id as fileId " +
			" from uuid_apply_record where 1=1 and uuid_apply_status in (1,2) and id=#{batchNum} and tenant_id=#{tenantId}"
			+ "</script>")
	public CustUuidManage getDownloadUUID(@Param("batchNum") Long batchNum, @Param("tenantId") Long tenantId) throws Exception;

	@Select({ "<script>",
			"SELECT " +
					"	t.id AS batchNum, " +
					"	t.tenant_id AS tenantId, " +
					"	t.pay_status AS payStatus, " +
					"	t.product_id AS productId, " +
					"	t.uuid_validity_year * 365 AS uuidValidityDays, " +
					"	t.create_num AS createNum, " +
					"	t1.device_type_id AS deviceTypeId, " +
					"	t1.is_direct_device AS isDirectDevice, " +
					"	t2.type AS deviceType " +
					"FROM " +
					"	uuid_apply_record t, " +
					"	product t1, " +
					"	device_type t2 " +
					"WHERE " +
					"	t.product_id = t1.id " +
					"AND t1.device_type_id = t2.id " +
					"AND t.id = #{batchNum} and t.uuid_apply_status = 0 and t1.audit_status = 2",
			"</script>" })
	public GenerateUUID getGenerateUUIDInfo(@Param("batchNum") Long batchNum) throws Exception;
	
	@Select({ "<script>",
		"SELECT " +
				"	t.id AS batchNum, " +
				"	t.tenant_id AS tenantId, " +
				"	t.pay_status AS payStatus, " +
				"	t.product_id AS productId, " +
				"	t.uuid_validity_year * 365 AS uuidValidityDays, " +
				"	t.create_num AS createNum, " +
				"	t1.device_type_id AS deviceTypeId, " +
				"	t1.is_direct_device AS isDirectDevice, " +
				"	t2.type AS deviceType " +
				"FROM " +
				"	uuid_apply_record t, " +
				"	product t1, " +
				"	device_type t2 " +
				"WHERE " +
				"	t.product_id = t1.id " +
				"AND t1.device_type_id = t2.id ",
				"AND t.id = #{batchNum} ",
		"</script>" })
public GenerateUUID getUUIDOrderInfo(@Param("batchNum") Long batchNum) throws Exception;

	@Select({"select * from iot_db_device.uuid_apply_record where id = #{batchNum}"})
	public GenerateUUID getUuidInfo(@Param("batchNum") Long batchNum) throws Exception;

//	/**
//	 *
//	 * 描述：获取最大SN
//	 * @author 李帅
//	 * @created 2017年9月26日 下午5:59:28
//	 * @since
//	 * @param custUUIDManages
//	 * @return
//	 * @throws Exception
//	 */
//	@Select({ "<script>",
//		"SELECT " +
//		"	t.id as productId, " +
//		"	t.tenant_id as tenantId, " +
//		"	t.device_type_id as deviceTypeId, " +
//		"	t.is_direct_device as isDirectDevice, " +
//		"	t1.type as deviceType " +
//		"FROM " +
//		"	product t, " +
//		"	device_type t1 " +
//		"WHERE " +
//		"	t.device_type_id = t1.id " +
//		" and t.id = #{productId} ",
//		"</script>" })
//	public ProductUUIDVo getProductUUID(Long productId) throws Exception;

//	/**
//	 *
//	 * 描述：保存客户UUID管理信息
//	 * @author 李帅
//	 * @created 2017年9月13日 下午3:33:33
//	 * @since
//	 * @param custUUIDManage
//	 * @return
//	 * @throws Exception
//	 */
//	@Insert({ "<script>",
//		"INSERT INTO cust_uuid_manage (" +
//		"			  CUST_CODE," +
//		"			  CUST_NAME," +
//		"			  BATCH_NUM," +
//		"			  CREATE_TIME," +
//		"			  UUID_TYPE," +
//		"			  UUID_AMOUNT," +
//		"			  DOWN_LOADED_NUM," +
//		"			  STATUS," +
//		"			  FILE_ID," +
//		"			  tenant_id" +
//		"		) VALUES (" +
//		"		    #{custCode}," +
//		"			#{custName}," +
//		"			#{batchNumId}," +
//		"			now()," +
//		"			#{uuidType}," +
//		"			#{uuidAmount}," +
//		"			#{downLoadedNum}," +
//		"			#{status}," +
//		"			#{fileId}," +
//		"			#{tenantId}" +
//		"		)",
//		"</script>" })
//	public void saveCustUUIDManage(CustUuidManage custUUIDManage) throws Exception;

	/**
	 *
	 * 描述：保存设备扩展信息
	 * @author 李帅
	 * @created 2017年9月23日 下午2:08:03
	 * @since
	 * @param deviceExtendParam
	 * @throws Exception
	 */
	@Insert({ "<script>",
			"INSERT INTO device_extend (" +
					"			  DEVICE_ID," +
					"			  tenant_id," +
					"			  P2P_ID," +
					"			  batch_num," +
					"			  CREATE_TIME," +
					"			  UUID_VALIDITY_DAYS," +
					"			  DEVICE_CIPHER" +
					"		) SELECT t.deviceId, t.tenantId, t.p2pId, t.batchNum, t.createTime, t.uuidValidityDays, t.deviceCipher FROM(" +
					"         <foreach collection=\"deviceExtendList\" index=\"\" item=\"deviceExtendList\" separator=\"union all\">" +
					"             select" +
					"               #{deviceExtendList.deviceId} AS deviceId," +
					"               #{deviceExtendList.tenantId} AS tenantId," +
					"               #{deviceExtendList.p2pId} AS p2pId," +
					"               #{deviceExtendList.batchNum} AS batchNum," +
					"               NOW() AS createTime," +
					"               #{deviceExtendList.uuidValidityDays} AS uuidValidityDays," +
					"               #{deviceExtendList.deviceCipher} AS deviceCipher" +
					"             from dual" +
					"         </foreach>" +
					"         ) t",
			"</script>" })
	public void saveDeviceExtend(Map<String, Object> deviceExtendParam) throws Exception;

	/**
	 *
	 * 描述：生成UUID时添加对应设备信息
	 * @author 李帅
	 * @created 2017年9月18日 下午4:59:25
	 * @since
	 * @param paramMap
	 * @throws Exception
	 */
//	@Insert({ "<script>",
//		"insert into device (" +
//		"			uuid," +
//		"			VENDERCODE," +
//		"			create_time," +
//		"			device_type_id," +
//		"			MANUFACTURER," +
//		"			DEVICESTATE" +
//		"		) select t.DEVICE_ID,#{venderCode},NOW(),#{deviceType},#{manufacturer},'0'" +
//		"		 from device_extend  t where 1=1" +
//		"		 <if test=\"idsList != null\"> and t.DEVICE_ID in" +
//		"               <foreach collection=\"idsList\" item=\"item\" index=\"index\" open=\"(\" close=\")\" separator=\",\">" +
//		"                 #{item}" +
//		"               </foreach>" +
//		"         </if>",
//		"</script>" })
	@Insert({ "<script>",
			"insert into device (" +
					"			uuid," +
					"			create_time," +
					"			product_id," +
					"			tenant_id," +
					"			is_direct_device," +
					"			device_type_id" +
					"		) select t.DEVICE_ID, NOW(),#{productId},#{tenantId},#{isDirectDevice},#{deviceTypeId}" +
					"		 from device_extend  t where 1=1" +
					"		 <if test=\"idsList != null\"> and t.DEVICE_ID in" +
					"               <foreach collection=\"idsList\" item=\"item\" index=\"index\" open=\"(\" close=\")\" separator=\",\">" +
					"                 #{item}" +
					"               </foreach>" +
					"         </if>",
			"</script>" })
	public void saveDeviceWithUUID(Map<String,Object> paramMap) throws Exception;

	/**
	 *
	 * 描述：回滚UUID时添加对应设备信息
	 * @author 李帅
	 * @created 2018年4月17日 下午1:44:22
	 * @since
	 * @param paramMap
	 * @throws Exception
	 */
	@Delete({ "<script>",
			"delete from device " +
					"		where 1 = 1 and uuid in" +
					"       <foreach collection=\"idsList\" item=\"item\" index=\"index\" open=\"(\" close=\")\" separator=\",\">" +
					"         #{item}" +
					"       </foreach>",
			"</script>" })
	public void deleteDeviceWithUUID(Map<String,List<String>> paramMap) throws Exception;

	/**
	 *
	 * 描述：回滚UUID时添加对应设备扩展信息
	 * @author 李帅
	 * @created 2018年4月17日 下午1:44:22
	 * @since
	 * @param paramMap
	 * @throws Exception
	 */
	@Delete({ "<script>",
			"delete from device_extend " +
					"		where 1 = 1 and device_id in" +
					"       <foreach collection=\"idsList\" item=\"item\" index=\"index\" open=\"(\" close=\")\" separator=\",\">" +
					"         #{item}" +
					"       </foreach>",
			"</script>" })
	public void deleteDeviceExtendWithUUID(Map<String,List<String>> paramMap) throws Exception;

	/**
	 *
	 * 描述：更新客户UUID管理信息
	 * @author 李帅
	 * @created 2017年9月13日 下午3:33:33
	 * @since
	 * @param resultParam
	 * @return
	 * @throws Exception
	 */
	@Update({ "<script>",
			"update uuid_apply_record" +
					"			set " +
					"			<if test=\"status != null\"> uuid_apply_status=#{status},</if>" +
					"			<if test=\"fileId != null\"> file_id=#{fileId},</if>" +
					"		    id = #{batchNum}" +
					"		  where 1=1" +
					"		    and id =  #{batchNum}",
			"</script>" })
	public void updateCustUUIDManage(Map<String, Object> resultParam) throws Exception;

	/**
	 * 
	 * 描述：更新下载次数
	 * @author 李帅
	 * @created 2018年8月8日 下午4:47:46
	 * @since 
	 * @param batchNum
	 * @throws Exception
	 */
	@Update({ "<script>", "update uuid_apply_record set   down_num = down_num + 1 where id =  #{batchNum}",
			"</script>" })
	public void updateUuidApplyRecord(Long batchNum) throws Exception;
	/**
	 *
	 * 描述：回滚P2PID使用状态
	 * @author 李帅
	 * @created 2018年1月8日 下午3:30:27
	 * @since
	 * @param map
	 * @throws Exception
	 */
	@Update({ "<script>",
			"update cust_p2pid" +
					"			set USE_MARK = '0'" +
					"		where 1=1" +
					"		    <if test=\"p2pId != null\"> and P2P_ID in " +
					"               <foreach collection=\"p2pId\" item=\"item\" index=\"index\" open=\"(\" close=\")\" separator=\",\">" +
					"                 #{item}" +
					"               </foreach>" +
					"            </if>",
			"</script>" })
	public void rollBackP2PIdMark(Map<String, Object> map) throws Exception;

	/**
	 *
	 * 描述：获取最大SN
	 * @author 李帅
	 * @created 2017年9月26日 下午5:59:28
	 * @since
	 * @param custUUIDManages
	 * @return
	 * @throws Exception
	 */
	@Select({ "<script>",
			"SELECT max(device_sn) FROM device_sn",
			"</script>" })
	public Long getMaxSN() throws Exception;

	/**
	 *
	 * 描述：设置最大SN
	 * @author 李帅
	 * @created 2017年9月26日 下午5:59:28
	 * @since
	 * @param custUUIDManages
	 * @return
	 * @throws Exception
	 */
	@Insert({ "<script>",
			"insert into device_sn ( DEVICE_SN, USING_DATE ) VALUES ( #{sn}, NOW() )",
			"</script>" })
	public void setMaxSN(Long sn) throws Exception;

	/**
	 *
	 * 描述：获取P2PID
	 * @author 李帅
	 * @created 2018年1月5日 下午4:54:28
	 * @since
	 * @return
	 */
	@Select({ "<script>",
			"select P2P_ID from cust_p2pid where 1=1 and use_mark = '0' order by P2P_ID asc limit #{num}",
			"</script>" })
	public List<String> getP2PId(Map<String, Object> map) throws Exception;

	/**
	 *
	 * 描述：更新P2PID状态
	 * @author 李帅
	 * @created 2018年1月5日 下午5:07:29
	 * @since
	 * @param p2pIdList
	 * @throws Exception
	 */
	@Update({ "<script>",
			"update cust_p2pid" +
					"			set use_mark = '1'" +
					"		where 1=1" +
					"		    <if test=\"p2pId != null\"> and P2P_ID in " +
					"               <foreach collection=\"p2pId\" item=\"item\" index=\"index\" open=\"(\" close=\")\" separator=\",\">" +
					"                 #{item}" +
					"               </foreach>" +
					"            </if>",
			"</script>" })
	public void updateP2PIdMark(Map<String, Object> map) throws Exception;

	@Select("<script>"
			+"SELECT " +
			"	uar.id AS batchNumId, " +
			"	uar.user_id AS userId, " +
			"	uar.tenant_id AS tenantId, " +
			"	uar.order_id AS orderId, " +
			"	uar.create_num AS uuidNum, " +
			"	uar.goods_id AS goodsId, " +
			"	p.product_name AS productName, " +
			"	p.model AS productModel, " +
			"	uar.product_id AS productId, " +
			"	uar.uuid_apply_status AS applyStatus, " +
			"	uar.pay_status AS payStatus, " +
			"	uar.create_time AS createTime " +
			"FROM uuid_apply_record uar left join product p on uar.product_id=p.id where 1=1 "
			+"<if test=\"userId != null\"> and uar.user_id=#{userId}</if>"
			+"<if test=\"tenantId != null\"> and uar.tenant_id=#{tenantId}</if>"
			+"<if test=\"batchNumId != null and batchNumId != ''\"> and uar.id=#{batchNumId}</if>"
			+"<if test=\"model != null and model != ''\"> and p.model LIKE  CONCAT(CONCAT('%',#{model}),'%') </if>"
			+"<if test=\"applyStatus != null\"> and uar.uuid_apply_status=#{applyStatus}</if>"
			+"<if test=\"payStatus != null\"> and uar.pay_status=#{payStatus}</if>"
			+"<if test=\"start != null\"> and uar.create_time >= #{start}</if>"
			+"<if test=\"end != null\"> and uar.create_time &lt; #{end}</if>"
			+"<if test=\"refundFlag != null\"> and uar.order_id  NOT LIKE 'leedarson%' and uar.pay_status in (2,4,5,6) </if>"
			+" order by uar.create_time desc "
			+ "</script>")
	public List<UUIDOrderDto> getUUIDOrder(Page<UUIDOrderDto> page,GetUUIDOrderReq uuidOrderReq);

	@Select(value=
			"SELECT " +
					"	uar.id AS batchNumId, " +
					"	uar.user_id AS userId, " +
					"	uar.tenant_id AS tenantId, " +
					"	uar.create_num AS uuidNum, " +
					"	uar.goods_id AS goodsId, " +
					"	p.product_name AS productName, " +
					"	p.model AS productModel, " +
					"	uar.product_id AS productId, " +
					"	uar.uuid_apply_status AS applyStatus, " +
					"	uar.pay_status AS payStatus, " +
					"	uar.create_time AS createTime " +
					"FROM uuid_apply_record uar left join product p on uar.product_id=p.id where 1=1 "
					+ "${ew.sqlSegment}"
	)
	public List<UUIDOrderDto> getUUIDOrder1(@Param("page") Page<UUIDOrderDto> page, @Param("ew") EntityWrapper ew);

	@Select("<script>"
			+"SELECT " +
			"	d.uuid AS uuid, " +
			"	d.product_id AS productId, " +
			"	de.batch_num AS batchNumId, " +
			"	ds.active_status AS activeStatus, " +
			"	ds.active_time AS activeTime, " +
			"	ds.last_login_time AS lastLoginTime, " +
			"	p.product_name AS productName, " +
			"	p.model AS productModel, " +
			" 	uar.goods_id AS goodsId " +
			" FROM device d left join device_extend de on d.uuid=de.device_id left join device_status ds on d.uuid=ds.device_id left join product p on d.product_id=p.id left join uuid_apply_record uar on de.batch_num=uar.id " +
			" WHERE 1=1 "
			+"<if test=\"tenantId != null and tenantId != ''\"> and d.tenant_id=#{tenantId}</if>"
			+"<if test=\"batchNumId != null and batchNumId != ''\"> and de.batch_num=#{batchNumId}</if>"
			+"<if test=\"uuid != null and uuid != ''\"> and d.uuid like concat('%',#{uuid},'%') </if>"
			+"<if test=\"activeStatus != null and activeStatus != ''\"> and ds.active_status=#{activeStatus}</if>"
			+"<if test=\"timeType!=null and timeType==1 and timeStart != null\"> and ds.active_time >= #{timeStart}</if>"
			+"<if test=\"timeType!=null and timeType==1 and timeEnd != null\"> and ds.active_time &lt; #{timeEnd}</if>"
			+"<if test=\"timeType!=null and timeType==2 and timeStart != null\"> and ds.last_login_time >= #{timeStart}</if>"
			+"<if test=\"timeType!=null and timeType==2 and timeEnd != null\"> and ds.last_login_time &lt; #{timeEnd}</if>"
			+" order by d.id desc "
			+ "</script>")
	public List<UUIDInfoDto> getUUIDInfo(Page<UUIDInfoDto> page, GetUUIDReq uuidReq);
	
	@Select("<script>"
			+"SELECT DISTINCT " + 
			"	id AS batchNumId " + 
			"FROM " + 
			"	uuid_apply_record " + 
			"WHERE " + 
			"	1 = 1 " + 
			"AND uuid_apply_status = 0 " + 
			"AND pay_status = 2 "
			+ "</script>")
	public List<Long> getNeedCompensateBatch();
	
	
	@Select("<script>"
			+"SELECT " + 
			"	id AS id, " + 
			"	tenant_id AS tenantId, " + 
			"	user_id AS userId, " + 
			"	order_id AS orderId, " + 
			"	down_num AS downNum, " + 
			"	create_num AS createNum, " + 
			"	goods_id AS goodsId, " + 
			"	uuid_apply_status AS uuidApplyStatus, " + 
			"	pay_status AS payStatus, " + 
			"	file_id AS fileId, " + 
			"	product_id AS productId, " + 
			"	uuid_validity_year AS uuidValidityYear, " + 
			"	create_time AS createTime, " + 
			"	update_time AS updateTime " + 
			"FROM " + 
			"	uuid_apply_record " + 
			"WHERE " + 
			"	1 = 1" + 
			"<if test=\"tenantId != null\"> and tenant_id = #{tenantId}</if>" +
			"<if test=\"orderId != null and orderId != ''\"> and order_id = #{orderId}</if>"
			+ "</script>")
	public UUidApplyRecord getUUidApplyRecord(UUIDRefundReq uuidRefundReq);
	
	/**
	 * 
	 * 描述：UUID订单退款前操作
	 * @author 李帅
	 * @created 2018年11月14日 下午4:58:12
	 * @since 
	 * @param uuidRefundReq
	 */
	@Update({ "<script>",
			"update uuid_apply_record" +
				" set " +
				" pay_status = 4" +
				" where 1=1 " +
				"<if test=\"tenantId != null\"> and tenant_id = #{tenantId}</if>" +
				"<if test=\"orderId != null and orderId != ''\"> and order_id = #{orderId}</if>",
			"</script>" })
	public void beforeUUIDRefund(UUIDRefundReq uuidRefundReq) throws BusinessException;
	
	/**
	 * 
	 * 描述：UUID订单退款成功操作
	 * @author 李帅
	 * @created 2018年11月15日 下午4:00:24
	 * @since 
	 * @param uuidRefundReq
	 * @throws BusinessException
	 */
	@Update({ "<script>",
			"update uuid_apply_record" +
				" set " +
				" pay_status = 5" +
				" where 1=1 " +
				"<if test=\"tenantId != null\"> and tenant_id = #{tenantId}</if>" +
				"<if test=\"orderId != null and orderId != ''\"> and order_id = #{orderId}</if>",
			"</script>" })
	public void refundSuccess(UUIDRefundReq uuidRefundReq) throws BusinessException;
	
	/**
	 * 
	 * 描述：UUID订单退款失败操作
	 * @author 李帅
	 * @created 2018年11月15日 下午3:59:39
	 * @since 
	 * @param uuidRefundReq
	 * @throws BusinessException
	 */
	@Update({ "<script>",
			"update uuid_apply_record" +
				" set " +
				" pay_status = 6" +
				" where 1=1 " +
				"<if test=\"tenantId != null\"> and tenant_id = #{tenantId}</if>" +
				"<if test=\"orderId != null and orderId != ''\"> and order_id = #{orderId}</if>",
			"</script>" })
	public void refundFail(UUIDRefundReq uuidRefundReq) throws BusinessException;
	
	/**
	 * 
	 * 描述：通过批次ID获取P2PID
	 * @author 李帅
	 * @created 2018年11月15日 下午5:25:13
	 * @since 
	 * @param batchNum
	 * @return
	 */
	@Select("<script>"
			+"SELECT DISTINCT  " + 
			"	p2p_id AS p2pId  " + 
			"FROM  " + 
			"	device_extend  " + 
			"WHERE  " + 
			"	1 = 1  " + 
			"AND batch_num = #{batchNum} "
			+ "</script>")
	public List<String> getP2PIdByBatchNum(Long batchNum);
	
	/**
	 * 
	 * 描述：通过批次ID获取设备ID
	 * @author 李帅
	 * @created 2018年11月15日 下午5:25:46
	 * @since 
	 * @param batchNum
	 * @return
	 */
	@Select("<script>"
			+"SELECT DISTINCT  " + 
			"	device_id AS deviceId  " + 
			"FROM  " + 
			"	device_extend  " + 
			"WHERE  " + 
			"	1 = 1  " + 
			"AND batch_num = #{batchNum} "
			+ "</script>")
	public List<String> getDeviceIdByBatchNum(Long batchNum);
}