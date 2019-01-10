package com.iot.building.device.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.Update;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import com.iot.building.device.vo.DeviceBusinessTypeReq;
import com.iot.building.device.vo.DeviceBusinessTypeResp;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * <p>
 * 设备-业务类型表 Mapper 接口
 * </p>
 *
 * @author lucky
 * @since 2018-05-09
 */
@Mapper
public interface DeviceBusinessTypeMapper extends BaseMapper<DeviceBusinessTypeResp> {

	@SelectProvider(type = DeviceBusinessTypeSqlProvider.class,method = "selectDeviceBusinessTypeId")
	DeviceBusinessTypeResp getBusinessTypeIdByType(Long orgId, String businessType);

	@SelectProvider(type = DeviceBusinessTypeSqlProvider.class,method = "selectDeviceBusinessTypeList")
	List<DeviceBusinessTypeResp> getBusinessTypeList(@Param("orgId") Long orgId, @Param("tenantId") Long tenantId, @Param("name") String name);

	@SelectProvider(type = DeviceBusinessTypeSqlProvider.class,method = "selectDeviceBusinessTypeList")
	List<DeviceBusinessTypeResp> getBusinessTypeList(Pagination pagination,@Param("tenantId") Long tenantId, @Param("model") String model);

	@Insert({
        "insert into device_business_type (",
        "id, 			    ",
        "business_type,		",
        "description, 		",
        "create_time, 		",
        "update_time,		",
        "create_by, 		",
        "update_by,			",
        "is_deleted,        ",
        "tenant_id,          ",
		"product_id,          ",
		"is_home_show          ",
        ")				    ",
        "values				",
        "( 	    			",
        "#{id},		    	",
        "#{businessType},	",
        "#{description}, 	",
        "#{createTime},	    ",
        "#{updateTime},	    ",
        "#{createBy},	    ",
        "#{updateBy},	    ",
        "#{isDeleted},		",
        "#{tenantId},		",
		"#{productId},		",
		"#{isHomeShow}		",
        ")                  "
	})
	void save(DeviceBusinessTypeReq deviceBusinessTypeReq);

//	@Update({
//         "<script> 												 " +
//                 "update device_business_type 					 " +
//                 "		<set> 									 " +
//                 "			<if test=\"updateTime != null\">     " +
//                 "				update_time = #{updateTime},     " +
//                 "			</if> 								 " +
//                 "			<if test=\"businessType != null\">   " +
//                 "				business_type = #{req.businessType}, " +
//                 "			</if> 								 " +
//                 "			<if test=\"description != null\">    " +
//                 "				description = #{description},    " +
//                 "			</if> 								 " +
//                 "			<if test=\"updateBy != null\"> 		 " +
//                 "				update_by = #{updateBy},   		 " +
//                 "			</if> 								 " +
//                 "			<if test=\"isDeleted != null\"> 	 " +
//                 "				is_deleted = #{isDeleted}, 		 " +
//                 "			</if> 					   			 " +
//				 "			<if test=\"productId != null\"> 	 " +
//				 "				product_id = #{productId}, 		 " +
//				 "			</if> 					   			 " +
//				 "			<if test=\"isHomeShow != null\"> 	 " +
//				 "				is_home_show = #{isHomeShow} ,	 " +
//				 "			</if> 					   			 " +
//                 "		</set> 									 " +
//                 "      where id = #{id}						 " +
//         "</script>                                              "
//	})
//	@UpdateProvider(type = DeviceBusinessTypeSqlProvider.class,method = "updateBusinessType")
//	void update(@Param("req") DeviceBusinessTypeReq req);
	
    @Update({
        "<script> " +
                "update device_business_type " +
                "		<set> " +
                "			<if test=\"updateTime != null\"> " +
                "				update_time = #{updateTime}, " +
                "			</if> " +
                "			<if test=\"businessType != null\"> " +
                "				business_type = #{businessType}, " +
                "			</if> " +
                "			<if test=\"description != null\"> " +
                "				description = #{description}, " +
                "			</if> " +
                "			<if test=\"updateBy != null\"> " +
                "				update_by = #{updateBy}, " +
                "			</if> " +
                "			<if test=\"productId != null\"> " +
                "				product_id = " +
                "				#{productId}, " +
                "			</if> " +
                "			<if test=\"isDeleted != null\"> " +
                "				is_deleted = #{isDeleted}, " +
                "			</if> " +
                "		</set> " +
                "		where id = #{id}" +
                "</script>                                                        "
    })
    int updateByPrimaryKeySelective(DeviceBusinessTypeReq deviceBusinessTypeReq);

	@Delete({
         "delete from " +
                 "	device_business_type " +
                 "	where id = #{id}       "
	 })
	 void delBusinessTypeIdByType(Long id);

	@Select({
		"select "+
		"   id as id," +
		"   business_type as businessType ," +
		"   description as description, " +
		"   tenant_id as tenantId, " +
		"   product_id as productId, " +
		"   is_home_show as isHomeShow " +
		"   from device_business_type " +
		"	where id = #{id}       " +
		"   <if test=\"tenantId != null\">" +
		"	and tenant_id =#{tenantId}" +
		"   </if> " +
		"   <if test=\"orgId != null\">" +
		"		 and org_id =#{orgId}" +
		"   </if> " 
	})
	DeviceBusinessTypeResp findById(Long orgId,Long tenanetId,Long id);

	@Select({
			"select "+
					"   business_type AS businessType ," +
					"   description as description ," +
					"   tenant_id as tenantId, " +
					"   product_id as productId, " +
					"   is_home_show as isHomeShow " +
					"	from device_business_type where 1=1" +
					"   <if test=\"tenantId != null\">" +
					"	and tenant_id =#{tenantId}" +
					"   </if> " +
					"   <if test=\"orgId != null\">" +
					"		 and org_id =#{orgId}" +
					"   </if> " 
	})
	List<DeviceBusinessTypeResp> businessTypeWithProduct(Long orgId,Long tenantId);

//	@Select({
//			"select "+
//					"   t2.id as productId,"+
//			        "   t1.tenant_id as tenantId,"+
//					"   t1.business_type AS businessType," +
//					"   t1.id as id," +
//					"   t2.device_type_id as deviceTypeId," +
//					"   t2.product_name as productName," +
//					"   t3.type as deviceTypeName" +
//					"		from device_business_type t1"+
//					"		left join product t2 on t2.id = t1.product_id"+
//					"      left join device_type t3 on t3.id = t2.device_type_id "+
//			"	where t1.tenant_id = #{tenanetId}       "
//	})
//    List<DeviceBusinessTypeResp> findBusinessTypeList(Long tenanetId);

	@Select({
		"select "+
				"   business_type AS businessType " +
				"   from device_business_type " +
				"   where description = #{description} " +
				"   and tenant_id = #{tenantId} "
    })
    List<String> getBusinessListByDescription(@Param("description") String description, @Param("tenantId") Long tenantId);
//	@Select({
//			" SELECT " +
//			"   distinct bt.id code, " +
//			"  bt.business_type name " +
//			" FROM " +
//			"  device_business_type bt " +
//			" JOIN product p ON bt.product_id = p.id " +
//			" JOIN device_type t ON p.device_type_id = t.id " +
//			" left join device_remote_template re on bt.id=re.business_type_id " +
//			" WHERE " +
//			" t.`name` LIKE '%remote%' AND " +
//			" re.id is null"
//	})
	@Select({
			"<script> 												 " +
			" SELECT DISTINCT " +
			"  bt.id CODE, " +
			"  bt.business_type NAME, " +
			"  re.id " +
			"FROM " +
			"  device_business_type bt " +
			"JOIN product p ON bt.product_id = p.id " +
			"JOIN device_type t ON p.device_type_id = t.id " +
			"LEFT JOIN device_remote_template re ON bt.id = re.business_type_id " +
			"WHERE " +
			"  t.`name` LIKE '%remote%' " +
			"AND (re.id IS NULL " +
			"			<if test=\"id != null\">" +
			"				 or re.id =#{id}" +
			"			</if> " +
			" ) "+
			"</script>                                              "
	})
    List<DeviceBusinessTypeResp> listDeviceRemoteBusinessType(@Param("orgId")Long orgId,@Param("tenantId")Long tenantId,@Param("id")Long id);


	@Select({
			"<script> 												 " +
					" SELECT " +
					"  d.id, d.product_id as productId,d.business_type as businessType,d.description,d.create_time as createTime, " +
					"  d.update_time as updateTime, d.create_by as createBy, " +
					"  d.update_by as updateBy, d.is_deleted as isDeleted " +
					"  FROM " +
					"  device_business_type d" +
					"  WHERE 1=1" +
					"<if test=\"tenantId != null\">" +
					"		 and d.tenant_id =#{tenantId}" +
					"</if> " +
					"<if test=\"orgId != null\">" +
					"		 and d.org_id =#{orgId}" +
					"</if> " +
					"<if test=\"productId != null\">" +
					"		 and d.product_id =#{productId}" +
					"</if> " +
					"<if test=\"businessType != null\">" +
					"       and d.business_type like CONCAT(CONCAT('%', #{businessType}), '%')   "+
					"</if> " +
					"<if test=\"isDeleted != null\">" +
					"		 and d.is_deleted =#{isDeleted}" +
					"</if> " +
					"<if test=\"ids != null and ids.size>0\">"+
				        "AND d.id IN"+
				        "<foreach collection=\"ids\" item=\"item\" index=\"index\" open=\"(\" close=\")\" separator=\",\">"+
				            "#{item}" +
				        "</foreach>" +
	        		"</if>"+
					"</script>                                              "
	})
    List<DeviceBusinessTypeResp> findBusinessTypeList(DeviceBusinessTypeReq deviceBusinessTypeReq);
}
