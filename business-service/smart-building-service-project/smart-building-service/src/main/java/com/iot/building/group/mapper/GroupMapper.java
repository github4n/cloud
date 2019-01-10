package com.iot.building.group.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.UpdateProvider;

import com.iot.building.group.vo.GroupReq;
import com.iot.building.group.vo.GroupResp;

public interface GroupMapper {
	
	
    @Select(
    		"select id,name,group_id as groupId,gateway_id as gatewayId,model,remote_id as remoteId from group_info where group_id=#{groupId}"
    		)
    GroupResp findGroupByGrouId(String groupId);
    
    @Select(
    		"select id,name,group_id as groupId,gateway_id as gatewayId,model,remote_id as remoteId from group_info where name=#{remoteId}"
    		)
    List<GroupResp> getDeploymentList(String remoteId);
    
    @Insert({
	        "insert into group_info (" ,
	        "				name, " ,
	        "				group_id, " ,
	        "				gateway_id, " ,
	        "				remote_id, " ,
	        "				model, " ,
	        "               device_id) values(" ,
	        "				#{name}, " ,
	        "				#{groupId}, " ,
	        "				#{gatewayId}, " ,
	        "				#{remoteId}, " ,
	        "               #{model}, " ,
	        "				#{deviceId} )" 
	})
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
	int save(GroupReq groupReq);
    
//    @Update({
//        "<script> " +
//                "update group_info " +
//                "		<set> " +
//                "			<if test=\"name != null\"> " +
//                "				name = #{name}, " +
//                "			</if> " +
//                "			<if test=\"model != null\"> " +
//                "				model = #{model}, " +
//                "			</if> " +
//                "			<if test=\"gateway_id != null\"> " +
//                "				gateway_id = #{gatewayId}, " +
//                "			</if> " +
//                "			<if test=\"remote_id != null\"> " +
//                "				remote_id = #{remoteId}, " +
//                "			</if> " +
//                "		</set> " +
//                "		where group_id = #{groupId}" +
//                "</script>                                                        "
//	})
//	int updateByGroupId(GroupReq groupReq);
    
    /**
	 * 	更新分组详情信息
	 *
	 * @param groupReq
	 * @return
	 */
	@UpdateProvider(type = GroupSqlProvider.class, method = "updateByGroupId")
	public int updateByGroupId(@Param("groupReq") GroupReq groupReq);

	@Select(
    		"select id,"
    		       + "name,"
    		       + "group_id as groupId,"
    		       + "gateway_id as gatewayId,"
    		       + "model,"
    		       + "remote_id as remoteId "
    		       + "from group_info where remote_id=#{remoteId}"
    		)
	List<GroupResp> findGroupListByRemoteId(String remoteId);
	
	/**
	 * 	删除网关分组信息
	 *
	 * @param gatewayId	网关id
	 */
	@Delete({
			"delete from group_info",
			"where gateway_id = #{gatewayId}"
	})
	public int delGroupListByGatewayId(@Param("gatewayId") String gatewayId);

	@Select(
			"select name from group_info where remote_id =#{remoteId}"
	)
	String getNameByRemoteId(@Param("remoteId") String remoteId);

	@Select(
			"select id as id,name as name,group_id as groupId,gateway_id as gatewayId,model as model,remote_id as remoteId from group_info where name=#{name} and remote_id is null"
	)
	List<GroupResp> getGroupListByName(@Param("name") String name);
	
	@Select(
			"select id as id,name as name,group_id as groupId,gateway_id as gatewayId,model as model,remote_id as remoteId,device_id as deviceId from group_info where remote_id=#{remoteId}"
	)
	List<GroupResp> getGroupListByRemoteId(@Param("remoteId") String remoteId);
}