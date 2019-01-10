package com.iot.device.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import com.iot.device.mapper.sql.DeviceSqlProvider;
import com.iot.device.model.Device;
import com.iot.device.model.DeviceStatus;
import com.iot.device.model.DeviceType;
import com.iot.device.model.Product;
import com.iot.device.vo.req.CommDeviceInfoReq;
import com.iot.device.vo.req.DeviceBusinessTypeIDSwitchReq;
import com.iot.device.vo.req.DevicePageReq;
import com.iot.device.vo.req.OtaPageReq;
import com.iot.device.vo.req.device.PageDeviceInfoReq;
import com.iot.device.vo.req.ota.DeviceQueryReq;
import com.iot.device.vo.req.ota.OtaFileInfoReq;
import com.iot.device.vo.rsp.DeviceResp;
import com.iot.device.vo.rsp.IftttDeviceResp;
import com.iot.device.vo.rsp.device.GetDeviceInfoRespVo;
import com.iot.device.vo.rsp.ota.ForceOtaDevInfo;
import com.iot.device.vo.rsp.ota.OtaDeviceInfo;
import com.iot.device.vo.rsp.ota.OtaFileInfoResp;
import com.iot.device.vo.rsp.ota.SubOtaDeviceInfo;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.UpdateProvider;
import org.apache.ibatis.type.JdbcType;

import java.util.List;
import java.util.Map;

/**
 * @Author: xfz
 * @Descrpiton: 设备服务
 * @Date: 20:16 2018/4/12
 * @Modify by:
 */
@Mapper
public interface DeviceMapper extends BaseMapper<Device>{

    @SelectProvider(type = DeviceSqlProvider.class,method = "selectDeviceListByIsDirectDeviceAndVenderCode")
    List<GetDeviceInfoRespVo> selectDeviceListByIsDirectDeviceAndVenderCode(@Param("isDirectDevice") Integer isDirectDevice, @Param("tenantId") Long tenantId, @Param("locationId") Long locationId,
    		@Param("venderFlag") String venderFlag);


    /**
     * 
     * 描述：通过设备Id 查询p2pId
     * @author 李帅
     * @created 2018年5月11日 下午2:02:24
     * @since 
     * @param deviceUUID
     * @return
     */
    @Select({ "<script>", 
		"SELECT " + 
		"	t.p2p_id " + 
		"FROM " + 
		"	device_extend t " + 
		"WHERE " + 
		"	t.device_id = #{deviceUUID} ", 
		"</script>" })
    String getP2pIdByDeviceUUID(String deviceUUID);

    @SelectProvider(type = DeviceSqlProvider.class,method = "selectDeviceListByParentId")
    List<DeviceResp> selectDeviceListByParentId(@Param("parentId") String parentId);

    @SelectProvider(type = DeviceSqlProvider.class,method = "selectDirectDevicePage")
    List<DeviceResp> selectDirectDevicePage(Pagination pagination, DevicePageReq pageReq);
    
    @SelectProvider(type = DeviceSqlProvider.class,method = "selectDirectDevicePageToCenter")
    List<DeviceResp> selectDirectDevicePageToCenter(Pagination pagination, DevicePageReq pageReq);

    @SelectProvider(type = DeviceSqlProvider.class,method = "selectUnDirectDevicePage")
    List<DeviceResp> selectUnDirectDevicePage(Pagination pagination, DevicePageReq pageReq);

    @SelectProvider(type = DeviceSqlProvider.class,method = "selectAllUnDirectDevicePage")
    List<DeviceResp> selectAllUnDirectDeviceList(DevicePageReq pageReq);

    @SelectProvider(type = DeviceSqlProvider.class, method = "findDeviceListByIds")
	List<DeviceResp> findDeviceListByIds(@Param("deviceIds") String deviceIds);
    
    @Select({
    	"select                                                                   ",  
    	"a.id,									  ",
    	"a.name,								  ",
    	"a.mac,									  ",
    	"a.create_time as createTime,								  ",
    	"a.is_direct_device as isDirectDevice,							  ",
    	"a.ip,									  ",
    	"a.tenant_id as tenantId,									  ",
    	"a.location_id as locationId,									  ",
    	"a.uuid,									  ",
    	"a.device_type_id as deviceTypeId,							  ",
    	"a.product_id AS productId								  ",
    	"from device a left join device_type b on a.device_type_id=b.id	  ",
    	"where a.is_direct_device=#{isDirectDevice} 				  ",
    	"and b.vender_flag=#{venderFlag,jdbcType=VARCHAR}			  ",
    	"and a.tenant_id=#{tenantId,jdbcType=BIGINT}			  ",
    	"and a.location_id=#{locationId,jdbcType=BIGINT}			  "
    })
    List<GetDeviceInfoRespVo> findDeviceByDeviceCatgory(Map<String, Object> params);


    @SelectProvider(type = DeviceSqlProvider.class, method = "getCountByDeviceIdsAndBusinessTypesAndSwitch")
	Integer getCountByDeviceIdsAndBusinessTypesAndSwitch(@Param("req") DeviceBusinessTypeIDSwitchReq req);

	@SelectProvider(type = DeviceSqlProvider.class, method = "getCountByDeviceIdsAndTypesAndSwitch")
	Integer getCountByDeviceIdsAndTypesAndSwitch(@Param("req") DeviceBusinessTypeIDSwitchReq req);

	@SelectProvider(type = DeviceSqlProvider.class, method = "findDevPageByProductId")
	List<DeviceResp> findDevPageByProductId(Pagination pagination, @Param("productId") Long productId);
	
	@SelectProvider(type = DeviceSqlProvider.class, method = "findDeviceByCondition")
	public List<DeviceResp> findDeviceByCondition(@Param("req") DeviceBusinessTypeIDSwitchReq req);
	
	@Select("<script>"+
			"select                                                "+ 
			"DISTINCT					       "+
			"d.id,						       "+
			"d.uuid as deviceId,						       "+
			"d.NAME,					       "+
			"d.mac,						       "+
			"d.create_time as createTime,					       "+
			"d.last_update_date as lastUpdateDate,				       "+
			"d.is_direct_device as isDirectDevice,				       "+
			"d.parent_id as parentId,					       "+
			"dc.type as devType,					       "+
			"d.device_type_id as deviceTypeId,				       "+
			"d.ip,						       "+
			"d.conditional,					       "+
			"d.product_id as productId					       "+
			"from device d 					       "+
			"LEFT JOIN device_type dc ON dc.id=d.device_type_id    "+
			"WHERE d.is_direct_device!=1 and d.tenant_id=#{tenantId}      "+
			"<if test='deviceIds != null and deviceIds.size > 0'>       "+
			"and d.uuid in       "+
			"<foreach collection=\"deviceIds\" item=\"deviceId\"  open=\"(\" close=\")\" separator=\",\">" +
            "#{deviceId}" +
            "</foreach>" +
            "</if>" +
			"<if test=\"name != null and name !=''\">	       "+
			"and d.`name` like CONCAT(CONCAT('%', #{name}), '%')   "+
			"</if>						       "+
			"order by d.create_time				       "+
			"</script>				       "
	)
	public List<IftttDeviceResp> findIftttDeviceList(CommDeviceInfoReq req);

	@Select({
			"select " +
			        "p1.uuid as deviceId, " ,
					"p1.name as name, " ,
					"p1.icon as icon, " ,
					"p1.mac as mac," ,
					"p1.tenant_id AS tenantId," ,
					"p1.create_time AS createTime," ,
					"p1.last_update_date AS lastUpdateDate," ,
					"p1.is_direct_device AS isDirectDevice," ,
					"p1.parent_id AS parentId," ,
					"p1.device_type_id AS deviceTypeId," ,
					"p1.ip as ip," +
					"p1.reality_id as realityId," ,
					"p1.extra_name as extraName," ,
					"p1.product_id AS productId," ,
					"p1.reset_random AS resetRandom ",
					"from device p1	 ",
					" where p1.tenant_id=#{tenantId}",
					"and p1.device_type_id=#{deviceTypeId}			  "
	})
	List<DeviceResp> queryAirCondition(Pagination pagination, DevicePageReq pageReq);

	@Select({
			"<script>",
			"SELECT uuid,product_id FROM device",
			"where uuid in",
			"<foreach item='uuId' index='index' collection='uuIdList' open='(' separator=',' close=')'>",
			"#{uuId}",
			"</foreach>",
			"</script>"
	})
	@Results({
			@Result(column = "uuid", property = "uuId", jdbcType = JdbcType.VARCHAR),
			@Result(column = "product_id", property = "productId", jdbcType = JdbcType.BIGINT)

	})
	@MapKey("uuId")
	@ResultType(java.util.Map.class)
	Map<String, Map<String,Long>> findUuidProductIdMap(@Param("uuIdList") List<String> uuIdList);

	/**
	 * 描述：查设备id与租户id对应关系
	 * @author nongchongwei
	 * @date 2018/11/1 16:40
	 * @param
	 * @return
	 */
	@Select({
			"<script>",
			"SELECT uuid,tenant_id FROM device",
			"where uuid in",
			"<foreach item='uuId' index='index' collection='uuIdList' open='(' separator=',' close=')'>",
			"#{uuId}",
			"</foreach>",
			"</script>"
	})
	@Results({
			@Result(column = "uuid", property = "uuId", jdbcType = JdbcType.VARCHAR),
			@Result(column = "tenant_id", property = "tenantId", jdbcType = JdbcType.BIGINT)

	})
	@MapKey("uuId")
	@ResultType(java.util.Map.class)
	Map<String, Map<String,Long>> findUuidTenantIdMap(@Param("uuIdList") List<String> uuIdList);

	/**
	 * 描述：依据产品id获取设备总数
	 * @author maochengyuan
	 * @created 2018/7/30 11:37
	 * @param tenantId 租户id
	 * @param prodId 产品id
	 * @return java.lang.Integer
	 */
	@Select("select count(1) from" +
			" device where" +
			" tenant_id=#{tenantId}" +
			" and product_id = #{prodId}")
	Integer getTotalByProductId(@Param("tenantId") Long tenantId, @Param("prodId") Long prodId);


	@Select({
			"<script>",
			"SELECT p1.uuid as devId,p2.version AS ver ",
			"FROM  device p1,ota_device_version p2",
			"where p1.uuid=p2.device_id and p1.product_id=#{productId} and p2.version in",
			"<foreach item='version' index='index' collection='versionList' open='(' separator=',' close=')'>",
			"#{version}",
			"</foreach>",
			"</script>"
	})
	List<ForceOtaDevInfo> getDirectForceOta(@Param("productId") Long productId, @Param("versionList")List<String> versionList );


	@Select({
			"<script>",
			"SELECT p1.uuid as devId,p1.parent_id AS pdevId, p2.version AS ver  ",
			"FROM  device p1,ota_device_version p2",
			"where p1.uuid=p2.device_id and p1.product_id=#{productId} and p1.parent_id is not null and p2.version in",
			"<foreach item='version' index='index' collection='versionList' open='(' separator=',' close=')'>",
			"#{version}",
			"</foreach>",
			"</script>"
	})
	List<SubOtaDeviceInfo> getSubOtaDeviceInfo(@Param("productId") Long productId, @Param("versionList")List<String> versionList );


	@Select({
			"<script>",
			"SELECT p1.uuid as devId,p1.name AS devName, p1.tenant_id AS  tenantId , p2.version AS ver  ",
			"FROM  device p1,ota_device_version p2 ",
			"where p1.uuid=p2.device_id  and p1.product_id=#{productId} and p2.version in",
			"<foreach item='version' index='index' collection='versionList' open='(' separator=',' close=')'>",
			"#{version}",
			"</foreach>",
			"</script>"
	})
	List<OtaDeviceInfo> getOtaDeviceInfo(@Param("productId") Long productId, @Param("versionList")List<String> versionList);

	@Select({
			"<script>",
			"SELECT p1.uuid as deviceId,p1.parent_id AS parentId, p2.version AS version,p1.product_id AS productId,p1.is_direct_device AS isDirectDevice ",
			"FROM  device p1,ota_device_version p2",
			"where p1.uuid=p2.device_id and p1.uuid in",
			"<foreach item='deviceId' index='index' collection='deviceIdList' open='(' separator=',' close=')'>",
			"#{deviceId}",
			"</foreach>",
			"</script>"
	})
	List<DeviceResp> getVersionByDeviceIdList( @Param("deviceIdList")List<String> deviceIdList);

	/**
	 * 描述：查询设备列表（供OTA使用）
	 * @author maochengyuan
	 * @created 2018/8/17 9:41
	 * @param params
	 * @return java.util.List<com.iot.device.vo.rsp.DeviceResp>
	 */
	@Select({
			"<script> ",
			" select de.product_id as productId,",
			" de.uuid as deviceId from device de, product pr, device_type dt, user_device ud where",
			" de.uuid = ud.device_id",
			" and dt.id = pr.device_type_id",
			" and ud.user_id = #{params.userId}",
			" and de.product_id = pr.id",
			" and dt.type = #{params.deviceType}",
			"<if test=\"params.productId != null\"> and de.product_id = #{params.productId}</if>",
			"<if test=\"params.deviceIds != null and params.deviceIds.size() > 0\"> and de.uuid in <foreach item='deviceId' index='index' collection='params.deviceIds' open='(' separator=',' close=')'>#{deviceId}</foreach></if>",
			"</script> "
	})
	List<DeviceResp> getDeviceListForOTAWithDeviceType(@Param("params") DeviceQueryReq params);

	/**
	 * 描述：查询设备列表（供OTA使用）
	 * @author maochengyuan
	 * @created 2018/8/17 9:41
	 * @param params
	 * @return java.util.List<com.iot.device.vo.rsp.DeviceResp>
	 */
	@Select({
			"<script> ",
			" select de.product_id as productId,",
			" de.uuid as deviceId from device de, user_device ud where",
			" de.uuid = ud.device_id",
			" and ud.user_id = #{params.userId}",
			"<if test=\"params.productId != null\"> and de.product_id = #{params.productId}</if>",
			"<if test=\"params.deviceIds != null and params.deviceIds.size() > 0\"> and de.uuid in <foreach item='deviceId' index='index' collection='params.deviceIds' open='(' separator=',' close=')'>#{deviceId}</foreach></if>",
			"</script> "
	})
	List<DeviceResp> getDeviceListByForOTAWithoutDeviceType(@Param("params") DeviceQueryReq params);

	/**
	 * 描述：查设备固件版本
	 * @author nongchongwei
	 * @date 2018/9/5 13:47
	 * @param
	 * @return
	 */
	@Select("select version from ota_device_version where device_id=#{deviceId}")
	String getOtaDeviceVersion(@Param("deviceId") String deviceId);

	@SelectProvider(type = DeviceSqlProvider.class,method = "selectAllDeviceToCenter")
    List<GetDeviceInfoRespVo> selectAllDeviceToCenter(DevicePageReq pageReq);
	
	@SelectProvider(type = DeviceSqlProvider.class,method = "getOtaFileList")
	List<OtaFileInfoResp> getOtaFileList(Pagination pagination, @Param("pageReq") OtaPageReq pageReq);
	
	@Insert({
        "insert into ota_file_info (",
        "id, 			    ",
        "version,    ",
        "create_time, 		",
        "update_time,		",
        "create_by, 		",
        "update_by,			",
        "tenant_id,         ",
		"product_id,        ",
		"download_url,         ",
		"md5,         ",
		"location_id        ",
        ")				    ",
        "values				",
        "( 	    			",
        "#{id},		    	",
        "#{version},	    ",
        "#{createTime},	    ",
        "#{updateTime},	    ",
        "#{createBy},	    ",
        "#{updateBy},	    ",
        "#{tenantId},		",
		"#{productId},		",
		"#{downloadUrl},		",
		"#{md5},		",
		"#{locationId}		",
        ")                  "
	})
	int saveOtaFileInfo(OtaFileInfoReq otaFileInfoReq);

	@SelectProvider(type = DeviceSqlProvider.class,method = "findOtaFileInfoByProductId")
	OtaFileInfoResp findOtaFileInfoByProductId(OtaFileInfoReq otaFileInfoReq);

	/**
	 * 	更新OTA文件信息
	 *
	 * @param otaFileInfoReq
	 * @return
	 */
	@UpdateProvider(type = DeviceSqlProvider.class, method = "updateOtaFileInfo")
	int updateOtaFileInfo(@Param("otaFileInfoReq") OtaFileInfoReq otaFileInfoReq);

	@Select({"select  "
			+ " ds.online_status as onlineStatus "
			+ " ,ds.active_status as activeStatus "
			+ " ,ds.on_off as switchStatus "
			+ " ,p1.id " +
			",p1.uuid as deviceId, " +
			"p1.name as name, " +
			"p1.icon as icon, " +
			"p1.mac as mac," +
			"p1.tenant_id AS tenantId," +
			"p1.create_time AS createTime," +
			"p1.last_update_date AS lastUpdateDate," +
			"p1.is_direct_device AS isDirectDevice," +
			"p1.parent_id AS parentId," +
			"p1.device_type_id AS deviceTypeId," +
			"p1.ip as ip," +
			"p1.reality_id as realityId," +
			"p1.extra_name as extraName," +
			"p1.product_id AS productId," +
			"p1.reset_random AS resetRandom "
			+ ",dt.type as devType "
			+ " from " + Device.TABLE_NAME + " AS p1  "
			+ " LEFT JOIN " + DeviceStatus.TABLE_NAME + " AS ds ON (p1.uuid = ds.device_id) "
			+ " LEFT JOIN " + Product.TABLE_NAME + " AS p ON (p1.product_id = p.id) "
			+ " LEFT JOIN " + DeviceType.TABLE_NAME + " AS dt ON (p.device_type_id = dt.id)"
			+ " where 1=1 " +
			" ${ew.sqlSegment}"
	})
	List<DeviceResp> selectPageByDeviceTypeList(@Param("page") Page page, @Param("ew") EntityWrapper wrapper);

    @Select({"select  "
            + "p1.id, " +
            "p1.uuid, " +
            "p1.name as name, " +
            "p1.icon as icon, " +
            "p1.mac as mac," +
            "p1.tenant_id AS tenantId," +
            "p1.is_direct_device AS isDirectDevice," +
            "p1.parent_id AS parentId," +
			"dt.id AS deviceTypeId," +
            "p1.ip as ip," +
            "p1.reality_id as realityId," +
            "p1.extra_name as extraName," +
            "p1.product_id AS productId," +
			"p1.location_id AS locationId," +
			"p1.business_type_id AS businessTypeId "

            + ",dt.type as devType "
            + " from " + Device.TABLE_NAME + " AS p1  "
            + " LEFT JOIN " + Product.TABLE_NAME + " AS p ON (p1.product_id = p.id) "
            + " LEFT JOIN " + DeviceType.TABLE_NAME + " AS dt ON (p.device_type_id = dt.id)"
            + " where 1=1 " +
            " ${ew.sqlSegment}"
    })
    List<Device> selectListByParams(@Param("ew") EntityWrapper wrapper);
    
    @SelectProvider(type = DeviceSqlProvider.class,method = "getGatewayAndSubDeviceList")
    List<DeviceResp> getGatewayAndSubDeviceList(Pagination pagination, DevicePageReq pageReq);
    
    @Select({
		"<script> ",
		" select DISTINCT product_id as productId ",
		" from device where 1=1 ",
		"<if test=\"params.productId != null\"> and product_id = #{params.productId}</if>",
		"<if test=\"params.tenantId != null\"> and tenant_id = #{params.tenantId}</if>",
		"<if test=\"params.locationId != null\"> and location_id = #{params.locationId}</if>",
		"<if test=\"params.productIds != null and params.productIds.size() > 0\"> and product_id in <foreach item='productId' index='index' collection='params.productIds' open='(' separator=',' close=')'>#{productId}</foreach></if>",
		"</script> "
     })
    List<Long> getExistProductList(@Param("params") PageDeviceInfoReq params);
    
    @SelectProvider(type = DeviceSqlProvider.class,method = "getDeviceListByParentId")
    List<GetDeviceInfoRespVo> getDeviceListByParentId(CommDeviceInfoReq commDeviceInfoReq);
  }
