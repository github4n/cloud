package com.iot.building.index.mapper;

import java.util.List;

import com.iot.common.helper.Page;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import com.iot.building.index.vo.IndexDetailReq;
import com.iot.building.index.vo.IndexDetailResp;
import com.iot.building.index.vo.IndexReq;
import com.iot.building.index.vo.IndexResp;
import org.springframework.web.bind.annotation.RequestParam;

public interface IndexMapper extends BaseMapper<IndexReq> {
	
	 @Select({
         "select id,image,type,top,`left`,width, ",
         "build_id as buildId,location_id as locationId,tenant_id as tenantId,zindex as zindex,org_id as orgId ",
         "from index_content where tenant_id=#{tenantId} and location_id= #{locationId} and org_id=#{orgId} and image is not null"
	 })
	 public List<IndexResp> getInexInfoByLocationId(@Param("tenantId") Long tenantId,@Param("orgId") Long orgId,@Param("locationId") Long locationId);

	 @Insert({
         "insert into index_content (",
         "id, 			    ",
         "type, 			    ",
         "image,		    ",
         "top, 				    ",
         "`left`, 			    ",
         "width,				    ",
         "build_id, 			    ",
         "location_id,			    ",
         "tenant_id	,		    ",
         "title,		    ",
         "create_time,				    ",
         "create_by, 			    ",
         "enable, 			    ",
         "org_id 			    ",
         ")				    ",
         "values				    ",
         "(",
         "#{id},		    ",
         "#{type},		    ",
         "#{image},		    ",
         "#{top}, 	    ",
         "#{left},	    ",
         "#{width},	    ",
         "#{buildId},	    ",
         "#{locationId},    ",
         "#{tenantId}, 	    ",
         "#{title}, 	    ",
         "#{createTime},	    ",
         "#{createBy},	    ",
         "#{enable},	    ",
         "#{orgId}	    ",
         ")                                  "
	 })
	 @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
	 Long saveIndexContent(IndexReq indexReq);
	
	 @Update(
	         "<script>							  " +
	                 "update index_content							  " +
	                 "<set>								  " +
	                 "	<if test=\"type != null\">			  " +
	                 "		type = #{type},	  " +
	                 "	</if>							  " +
	                 "	<if test=\"image != null\">				  " +
	                 "		image = #{image},		  " +
	                 "	</if>							  " +
	                 "	<if test=\"top != null\">				  " +
	                 "		top = #{top},	  " +
	                 "	</if>							  " +
	                 "	<if test=\"left != null\">				  " +
	                 "		`left` = #{left},		  " +
	                 "	</if>							  " +
	                 "	<if test=\"width != null\">				  " +
	                 "		width = #{width},	  " +
	                 "	</if>							  " +
	                 "	<if test=\"buildId != null\">				  " +
	                 "		build_id = #{buildId},		  " +
	                 "	</if>							  " +
	                 "	<if test=\"locationId != null\">			  " +
	                 "		location_id = #{locationId},	  " +
	                 "	</if>							  " +
	                 "	<if test=\"tenantId != null\">				  " +
	                 "		tenant_id = #{tenantId},	  " +
	                 "	</if>							  " +
	                 "	<if test=\"title != null\">				  " +
	                 "		title = #{title},	  " +
	                 "	</if>							  " +
	                 "	<if test=\"enable != null\">				  " +
	                 "		enable = #{enable},	  " +
	                 "	</if>							  " +
	                 "</set>								  " +
	                 "where id = #{id}				  " +
	                 "</script>                              "
	 )
	 void updateIndexContent(IndexReq indexReq);
	
	 @Insert({
	         "insert into index_detail (",
	         "id, 			    ",
	         "index_content_id, 			    ",
	         "module_sort,		    ",
	         "module_name, 				    ",
	         "parameter, 			    ",
	         "create_time,				    ",
	         "create_by, 			    ",
	         "update_time,			    ",
	         "update_by,			    ",
	         "fresh			    ",
	         ")				    ",
	         "values				    ",
	         "(",
	         "#{id},		    ",
	         "#{indexContentId},		    ",
	         "#{moduleSort},		    ",
	         "#{moduleName}, 	    ",
	         "#{parameter},	    ",
	         "#{createTime},	    ",
	         "#{createBy},	    ",
	         "#{updateTime},    ",
	         "#{updateBy}, 	    ",
	         "#{fresh} 	    ",
	         ")                                  "
	 })
	 @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
	 int saveIndexDetail(IndexDetailReq indexDetailReq);
	
	 @Update(
	         "<script>							  " +
	                 "update index_detail							  " +
	                 "<set>								  " +
	                 "	<if test=\"moduleSort != null\">			  " +
	                 "		module_sort = #{moduleSort},	  " +
	                 "	</if>							  " +
	                 "	<if test=\"moduleName != null\">				  " +
	                 "		module_name = #{moduleName},		  " +
	                 "	</if>							  " +
	                 "	<if test=\"parameter != null\">				  " +
	                 "		parameter = #{parameter},	  " +
	                 "	</if>							  " +
	                 "	<if test=\"updateTime != null\">				  " +
	                 "		update_time = #{updateTime},		  " +
	                 "	</if>							  " +
	                 "	<if test=\"updateBy != null\">				  " +
	                 "		update_by = #{updateBy},	  " +
	                 "	</if>							  " +
	                 "	<if test=\"fresh != null\">				  " +
	                 "		fresh = #{fresh},		  " +
	                 "	</if>							  " +
	                 "</set>								  " +
	                 "where index_content_id = #{indexContentId} and id = #{id}				  " +
	                 "</script>                              "
	 )
	 void updateIndexDetail(IndexDetailReq indexDetailReq);
	
	 @Select({
	         "select                                    ",
	         "s.id as id, 					   ",
	         "s.type as type, 					   ",
	         "s.location_id as locationId, 		   ",
	         "s.tenant_id as tenantId, 	   ",
	         "s.org_id as orgId, 	   ",
	         "s.title as title, 	   ",
	         "s.create_time as createTime, 	   ",
	         "s.create_by as createBy, 	   ",
	         "s.enable as enable   ",
	         "from index_content s ",
	         "where s.tenant_id = #{tenantId} and s.location_id = #{locationId} and org_id =#{orgId} and s.image is null",
	 })
	 List<IndexResp> findCustomIndex(IndexReq indexReq);
	
	 @Select({
	         "select                                    ",
	         "s.id as id, 					   ",
	         "s.index_content_id as indexContentId, 					   ",
	         "s.module_sort as moduleSort, 					   ",
	         "s.module_name as moduleName, 					   ",
	         "s.parameter as parameter, 					   ",
	         "s.create_time as createTime, 					   ",
	         "s.create_by as createBy, 		   ",
	         "s.update_time as updateTime, 	   ",
	         "s.update_by as updateBy, 	   ",
	         "s.fresh as fresh 	   ",
	         "from index_detail s ",
	         "where s.index_content_id = #{indexContentIdStr} ",
		 })
	 List<IndexDetailResp> findIndexDetailByIndexId(@Param("indexContentIdStr") Long indexContentIdStr);
	
	 @Delete({
	         "delete from index_detail		     ",
	         "where index_content_id=#{indexContentId}     "
	 })
	 void deleteIndexDatailByIndexId(@Param("indexContentId") Long indexContentId);
	
	 @Delete({
	         "delete from index_content		     ",
	         "where id=#{id}     "
	 })
	 void deleteIndexContent(IndexReq indexReq);
	
	 @Select({
	         "select                                    ",
	         "s.id as id, 					   ",
	         "s.type as type, 					   ",
	         "s.location_id as locationId, 		   ",
	         "s.tenant_id as tenantId, 	   ",
	         "s.org_id as orgId, 	   ",
	         "s.title as title 	   ",
	         "from index_content s ",
	         "where s.id = #{indexContentIdStr} ",
	 })
	 IndexResp findIndexContentById(@Param("indexContentIdStr") Long indexContentIdStr);
	
	
	 @Update(
	         "<script>							  " +
	                 "update index_content					 " +
	                 "set  enable = 0	  " +
	                 "where enable = #{enable}		"
	                 + "and tenant_id=#{tenantId} and location_id=#{locationId} and org_id=#{orgId}		  " +
	                 "</script>                              "
	 )
	 void setAllEnableOff(@Param("tenantId") Long tenantId,@Param("orgId") Long orgId,@Param("locationId") Long locationId,@Param("enable") int enable);
	
	
	 @Select({
	         "select                                    ",
	         "s.id as id, 					   ",
	         "s.type as type, 					   ",
	         "s.location_id as locationId, 		   ",
	         "s.tenant_id as tenantId, 	   ",
	         "s.org_id as orgId, 	   ",
	         "s.title as title, 	   ",
	         "s.enable as enable 	   ",
	         "from index_content s ",
	         "where s.enable = '1' and tenant_id=#{tenantId} and location_id=#{locationId} and org_id=#{orgId}",
	 })
	 IndexResp getEnableIndex(@Param("tenantId") Long tenantId,@Param("orgId") Long orgId,@Param("locationId") Long locationId);

}
