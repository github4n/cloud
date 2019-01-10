package com.iot.permission.dao;

import java.util.List;

import com.iot.permission.vo.PermissionVo;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.type.JdbcType;

import com.iot.permission.entity.Permission;
import com.iot.permission.entity.Role;
import com.iot.permission.entity.RolePermissionRelate;
import com.iot.permission.entity.UserRoleRelate;
import com.iot.permission.vo.PermissionDto;
import com.iot.permission.vo.RoleReqDto;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 
 * 项目名称：立达信IOT云平台
 * 模块名称：权限接口
 * 功能描述：权限接口
 * 创建人： 李帅
 * 创建时间：2018年7月3日 下午5:08:04
 * 修改人：李帅
 * 修改时间：2018年7月3日 下午5:08:04
 */
@Mapper
public interface PermissionMapper {

	/**
	 * 
	 * 描述：获取用户权限
	 * @author 李帅
	 * @created 2018年7月12日 下午2:35:39
	 * @since 
	 * @return
	 */
	@Select({ "<script>", 
			"SELECT " + 
			"	t1.id AS id, " + 
			"	t1.role_name AS roleName, " + 
			"	t1.role_code AS roleCode, " + 
			"	t1.role_type AS roleType, " + 
			"	t1.tenant_id AS tenantId, " + 
			"	t1.org_id AS orgId, " +
			"	t1.role_desc AS roleDesc, " +
			"	t1.create_by AS createBy, " + 
			"	t1.create_time AS createTime, " + 
			"	t1.update_by AS updateBy, " + 
			"	t1.update_time AS updateTime, " + 
			"	t1.is_deleted AS isDeleted " + 
			"FROM	role t1 " + 
			"WHERE " + 
			"	t1.is_deleted = 'valid'" + 
			" <if test=\"roleName != null\" > and t1.role_name = #{roleName}</if>" +
			" <if test=\"roleCode != null\" > and t1.role_code = #{roleCode}</if>" +
			" <if test=\"tenantId != null\" > and t1.tenant_id = #{tenantId}</if>" +
			" <if test=\"orgId != null\" > and t1.org_id = #{orgId}</if>" +
			" <if test=\"roleType != null\" > and t1.role_type = #{roleType}</if>"+
			" order by convert(t1.role_name USING gbk) COLLATE gbk_chinese_ci asc "+
			"</script>" })
	public List<Role> getRole(RoleReqDto roleReqVo);


	/**
	 * 
	 * 描述：查询默认角色
	 * @author 李帅
	 * @created 2018年7月12日 下午2:35:54
	 * @since 
	 * @param roleCode
	 * @param roleType
	 * @return
	 */
	@Select("SELECT id AS id, " + 
			"	role_name AS roleCode, " + 
			"	role_code AS roleName, " + 
			"	role_type AS roleType, " + 
			"	tenant_id AS tenantId, " + 
			"	org_id AS orgId, " +
			"	role_desc AS roleDesc, " +
			"	create_by AS createBy, " + 
			"	create_time AS createTime, " + 
			"	update_by AS updateBy, " + 
			"	update_time AS updateTime, " + 
			"	is_deleted AS isDeleted " + 
			"FROM	role WHERE is_deleted = 'valid' " + 
			"	and role_code = #{roleCode} AND role_type = #{roleType}")
	public Role getDefaultRole(@Param("roleCode") String roleCode,@Param("roleType") String roleType);

	/**
	 * 
	 * 描述：保存用户角色关联关系
	 * @author 李帅
	 * @created 2018年7月12日 下午7:22:24
	 * @since 
	 * @param userRoleRelate
	 */
	@Insert("INSERT INTO user_role_relate ("
//			+ "id,"
			+ "user_id,role_id,create_by,create_time)" +
			"  VALUES ("
//			+ "#{id},"
			+ " #{userId},"
			+ " #{roleId},"
			+ " #{createBy}," 
			+ " now())")
	public void saveUserRoleRelate(UserRoleRelate userRoleRelate);
	
	/**
	 * 
	 * 描述：获取用户权限
	 * @author 李帅
	 * @created 2018年7月12日 下午2:35:39
	 * @since 
	 * @param userId
	 * @return
	 */
	@Select("SELECT DISTINCT " +
			"	t2.id as id, " + 
			"	t2.parent_id as parentId, " + 
			"	t2.permission_code as permissionCode, " + 
			"	t2.permission_url as permissionUrl, " + 
			"	t2.permission_name as permissionName, " + 
			"	t2.permission_type as permissionType, " + 
			"	t2.sort as sort, " + 
			"	t2.icon as icon, " + 
			"	t2.tenant_id as tenantId, " +
			"	t2.org_id as orgId, " +
			"	t2.service_id as serviceId, " +
			"	t2.system_type as systemType, " + 
			"	t2.create_by as createBy, " + 
			"	t2.create_time as createTime, " + 
			"	t2.update_by as updateBy, " + 
			"	t2.update_time as updateTime, " + 
			"	t2.is_deleted as isDeleted " + 
			"FROM " + 
			"	user_role_relate t, " + 
			"	role_permission_relate t1, " + 
			"	permission t2 " + 
			"WHERE " + 
			"	t.role_id = t1.role_id " +
			"AND t1.permission_id = t2.id " +
			"AND t.is_deleted = 'valid' " + 
			"AND t1.is_deleted = 'valid' " + 
			"AND t2.is_deleted = 'valid' " + 
			"AND t.user_id = #{userId} " + 
			"ORDER BY parent_id DESC, sort ASC")
	public List<Permission> getUserPermission(@Param("userId") Long userId);
	
	/**
	 * 
	 * 描述：获取角色权限
	 * @author 李帅
	 * @created 2018年7月12日 下午2:35:39
	 * @since 
	 * @param roleId
	 * @return
	 */
	@Select("SELECT  " + 
			"	t2.id as id,  " + 
			"	t2.parent_id as parentId,  " + 
			"	t2.permission_code as permissionCode,  " + 
			"	t2.permission_url as permissionUrl,  " + 
			"	t2.permission_name as permissionName,  " + 
			"	t2.permission_type as permissionType,  " + 
			"	t2.sort as sort,  " + 
			"	t2.icon as icon,  " +
			"	t2.tenant_id as tenantId, " +
			"	t2.org_id as orgId, " +
			"	t2.service_id as serviceId,  " + 
			"	t2.system_type as systemType,  " + 
			"	t2.create_by as createBy,  " + 
			"	t2.create_time as createTime,  " + 
			"	t2.update_by as updateBy,  " + 
			"	t2.update_time as updateTime,  " + 
			"	t2.is_deleted as isDeleted  " + 
			"FROM  " + 
			"	role_permission_relate t1,  " + 
			"	permission t2  " + 
			"WHERE  " + 
			"	t1.permission_id = t2.id  " + 
			"AND t1.is_deleted = 'valid'  " + 
			"AND t2.is_deleted = 'valid'  " + 
			"AND t1.role_id = #{roleId} " + 
			"ORDER BY parent_id DESC, sort ASC")
	public List<Permission> getRolePermission(@Param("roleId") Long roleId);
	
	/**
	 * 
	 * 描述：获取所有权限
	 * @author 李帅
	 * @created 2018年7月12日 下午2:35:39
	 * @since 
	 * @return
	 */
	@Select("SELECT  " +
			"	t2.id as id,  " + 
			"	t2.parent_id as parentId,  " + 
			"	t2.permission_code as permissionCode,  " + 
			"	t2.permission_url as permissionUrl,  " + 
			"	t2.permission_name as permissionName,  " + 
			"	t2.permission_type as permissionType,  " + 
			"	t2.sort as sort,  " + 
			"	t2.icon as icon,  " + 
			"	t2.service_id as serviceId,  " + 
			"	t2.system_type as systemType,  " + 
			"	t2.create_by as createBy,  " + 
			"	t2.create_time as createTime,  " + 
			"	t2.update_by as updateBy,  " + 
			"	t2.update_time as updateTime,  " + 
			"	t2.is_deleted as isDeleted  " + 
			"FROM  " + 
			"	permission t2  " + 
			"WHERE  " +
			" t2.system_type = 'Portal'" +
			" AND t2.is_deleted = 'valid'  " +
			"ORDER BY parent_id DESC, sort ASC")
	public List<Permission> getAllPermission();


	/**
	 * 
	 * 描述：获取用户权限
	 * @author 李帅
	 * @created 2018年7月12日 下午2:35:39
	 * @since 
	 * @param userId
	 * @return
	 */
	@Select("SELECT " +
			"	t1.id AS id, " + 
			"	t1.role_name AS roleName, " + 
			"	t1.role_code AS roleCode, " + 
			"	t1.role_type AS roleType, " + 
			"	t1.tenant_id AS tenantId, " + 
			"	t1.org_id AS orgId, " +
			"	t1.role_desc AS roleDesc, " +
			"	t1.create_by AS createBy, " + 
			"	t1.create_time AS createTime, " + 
			"	t1.update_by AS updateBy, " + 
			"	t1.update_time AS updateTime, " + 
			"	t1.is_deleted AS isDeleted " + 
			"FROM " + 
			"	user_role_relate t, " + 
			"	role t1 " + 
			"WHERE " + 
			"	t.role_id = t1.id " + 
			"AND t.is_deleted = 'valid' " + 
			"AND t1.is_deleted = 'valid' " + 
			"AND t.user_id = #{userId}")
	public List<Role> getUserRoles(@Param("userId") Long userId);
	
	/**
	 * 
	 * 描述：获取用户权限URL
	 * @author 李帅
	 * @created 2018年7月12日 下午2:35:39
	 * @since 
	 * @param userId
	 * @return
	 */
	@Select("SELECT DISTINCT " + 
			"	t2.permission_url " + 
			"FROM " + 
			"	user_role_relate t, " + 
			"	role_permission_relate t1, " + 
			"	permission t2 " + 
			"WHERE " + 
			"	t.role_id = t1.role_id " + 
			"AND t1.permission_id = t2.id " + 
			"and t.is_deleted = 'valid' " + 
			"and t1.is_deleted = 'valid' " + 
			"and t2.is_deleted = 'valid' " + 
			"AND t.user_id = #{userId}")
	public List<String> getUserPermissionUrl(@Param("userId") Long userId);

	@Delete({"delete from role_permission_relate ",
			"where role_id = #{roleId,jdbcType=BIGINT}"})
	void deletePermissionByRoleId(Long roleId);

	@Insert("INSERT INTO role_permission_relate ("
			+ "role_id,permission_id,create_by,create_time,update_by,update_time)" +
			"  VALUES ("
			+ " #{roleId},"
			+ " #{permissionId},"
			+ " #{createBy},"
			+ " now(),"
			+ " #{updateBy},"
	        + " now())")
	void saveRolePermissionRelate(RolePermissionRelate r);

	@Delete({"delete from user_role_relate ",
			"where user_id = #{userId,jdbcType=BIGINT}"})
	void deleteUserRoleRelateByUserId(Long userId);

	@Select({"<script> select id, parent_id as parentId, permission_code as permissionCode, " +
			" permission_url as permissionUrl, permission_name as permissionName, ",
			" permission_type as permissionType, sort, icon, " +
			" tenant_id as tenantId, org_id as orgId, service_id as serviceId, system_type as systemType, " +
			" create_by as createBy, create_time as createTime, ",
			" update_by as updateBy, update_time as updateTime, is_deleted as isDeleted",
			" from permission " +
			" where system_type = #{systemType}" +
			" <if test=\"tenantId != null \">" +
			"   and tenant_id = #{tenantId}" +
			" </if>" +
			" <if test=\"orgId != null \">" +
			"   and org_id = #{orgId}" +
			" </if>" +
			"</script>"})
    List<Permission> queryList(PermissionVo vo);

	@Select("SELECT " +
			"	t2.id as id, " +
			"	t2.parent_id as parentId, " +
			"	t2.permission_code as permissionCode, " +
			"	t2.permission_url as permissionUrl, " +
			"	t2.permission_name as permissionName, " +
			"	t2.permission_type as permissionType, " +
			"	t2.tenant_id as tenantId, " +
			"	t2.org_id as orgId " +
			"FROM " +
			"	role_permission_relate t1, " +
			"	permission t2 " +
			"WHERE " +
			" t1.permission_id = t2.id " +
			"AND t1.is_deleted = 'valid' " +
			"AND t2.is_deleted = 'valid' " +
			"AND t1.role_id = #{roleId} ")
	List<PermissionDto> getPermissionByRoleId(@Param("roleId") Long roleId);
	
	@Insert({ "<script>", 
		"insert into permission " + 
		"    <trim prefix=\"(\" suffix=\")\" suffixOverrides=\",\" >" + 
		"      <if test=\"parentId != null\" > parent_id, </if>" + 
		"      <if test=\"permissionCode != null\" > permission_code, </if>" + 
		"      <if test=\"permissionUrl != null\" > permission_url, </if>" + 
		"      <if test=\"permissionName != null\" > permission_name, </if>" + 
		"      <if test=\"permissionType != null\" > permission_type, </if>" +
		"      <if test=\"sort != null\" > sort, </if>" +
		"      <if test=\"icon != null\" > icon, </if>" +
		"      <if test=\"tenantId != null\" > tenant_id, </if>" +
		"      <if test=\"orgId != null\" > org_id, </if>" +
		"      <if test=\"serviceId != null\" > service_id, </if>" +
		"      <if test=\"systemType != null\" > system_type, </if>" + 
		"      <if test=\"createBy != null\" > create_by, </if>" +
		"      <if test=\"createTime != null\" > create_time, </if>" +
		"      <if test=\"isDeleted != null\" > is_deleted, </if>" +
		"    </trim>" +
		"    <trim prefix=\"values (\" suffix=\")\" suffixOverrides=\",\" >" +
		"      <if test=\"parentId != null\" > #{parentId,jdbcType=BIGINT}, </if>" + 
		"      <if test=\"permissionCode != null\" > #{permissionCode,jdbcType=VARCHAR}, </if>" + 
		"      <if test=\"permissionUrl != null\" > #{permissionUrl,jdbcType=VARCHAR}, </if>" + 
		"      <if test=\"permissionName != null\" > #{permissionName,jdbcType=VARCHAR}, </if>" + 
		"      <if test=\"permissionType != null\" > #{permissionType,jdbcType=VARCHAR}, </if>" +
		"      <if test=\"sort != null\" > #{sort,jdbcType=BIGINT}, </if>" +
		"      <if test=\"icon != null\" > #{icon,jdbcType=VARCHAR}, </if>" +
		"      <if test=\"tenantId != null\" > #{tenantId,jdbcType=BIGINT}, </if>" +
		"      <if test=\"orgId != null\" > #{orgId,jdbcType=BIGINT}, </if>" +
		"      <if test=\"serviceId != null\" > #{serviceId,jdbcType=BIGINT}, </if>" +
		"      <if test=\"systemType != null\" > #{systemType,jdbcType=BIGINT}, </if>" + 
		"      <if test=\"createBy != null\" > #{createBy,jdbcType=BIGINT}, </if>" +
		"      <if test=\"createTime != null\" > now(), </if>" +
		"      <if test=\"isDeleted != null\" > 'valid', </if>" +
		"    </trim>",
		"</script>" })
    void savePermission(Permission permission);
	
	@Update({ "<script>", 
		"update permission" + 
		"			set " + 
		"			<if test=\"parentId != null\"> parent_id=#{parentId,jdbcType=BIGINT},</if>" +
		"			<if test=\"permissionCode != null\"> permission_code=#{permissionCode,jdbcType=VARCHAR},</if>" + 
		"			<if test=\"permissionUrl != null\"> permission_url=#{permissionUrl,jdbcType=VARCHAR},</if>" + 
		"			<if test=\"permissionName != null\"> permission_name=#{permissionName,jdbcType=VARCHAR},</if>" + 
		"			<if test=\"permissionType != null\"> permission_type=#{permissionType,jdbcType=VARCHAR},</if>" + 
		"			<if test=\"sort != null\"> sort=#{sort,jdbcType=BIGINT},</if>" + 
		"			<if test=\"icon != null\"> icon=#{icon,jdbcType=VARCHAR},</if>" + 
		"			<if test=\"serviceId != null\"> service_id=#{serviceId,jdbcType=BIGINT},</if>" + 
		"			<if test=\"systemType != null\"> system_type=#{systemType,jdbcType=VARCHAR},</if>" + 
		"			<if test=\"createBy != null\"> update_by=#{createBy,jdbcType=BIGINT},</if>" + 
		"           update_time = now()" +
		"		where id = #{id,jdbcType=BIGINT}",
		"</script>" })
	void editPermission(Permission permission);
	
	@Delete("<script>"+
            "delete from permission where id in"+
            "<foreach item='permissionId' index='index' collection='permissionIds' open='(' separator=',' close=')'>" +
            "#{permissionId}"+
            "</foreach>"+
            "</script>"
    )
	void deletePermission(@Param("permissionIds") List<Long> permissionIds);
	
	@Delete("<script>"+
            "delete from role_permission_relate where permission_id in"+
            "<foreach item='permissionId' index='index' collection='permissionIds' open='(' separator=',' close=')'>" +
            "#{permissionId}"+
            "</foreach>"+
            "</script>"
    )
	void deleteRolePermissionRelate(@Param("permissionIds") List<Long> permissionIds);
}