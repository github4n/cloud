package com.iot.tenant.mapper;

import com.baomidou.mybatisplus.plugins.Page;
import com.iot.tenant.entity.ReplyMessageRecord;
import com.iot.tenant.vo.req.reply.AddReplyMessageReq;
import com.iot.tenant.vo.req.reply.PageQueryReplyMessageReq;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 项目名称：cloud
 * 功能描述：回复消息记录 sql
 * 创建人： yeshiyuan
 * 创建时间：2018/11/1 19:50
 * 修改人： yeshiyuan
 * 修改时间：2018/11/1 19:50
 * 修改描述：
 */
@Mapper
public interface ReplyMessageRecordMapper {

    /**
     * @despriction：添加
     * @author  yeshiyuan
     * @created 2018/11/1 20:09
     * @return
     */
    @Insert("insert into reply_message_record(tenant_id,parent_id,object_type,object_id,message,message_type,create_time,create_by)" +
            "values(" +
            " #{req.tenantId,jdbcType=BIGINT}," +
            " #{req.parentId}," +
            " #{req.objectType}," +
            " #{req.objectId,jdbcType=BIGINT}," +
            " #{req.message}," +
            " #{req.messageType}," +
            " #{req.createTime}," +
            " #{req.createBy}" +
            ")")
    int add(@Param("req") AddReplyMessageReq messageReq);

    /**
      * @despriction：分页查询
      * @author  yeshiyuan
      * @created 2018/11/1 20:44
      * @return
      */
    @Select("select id, parent_id as parentId," +
            " message," +
            " message_type as messageType," +
            " create_time as createTime," +
            " create_by as createBy " +
            "from reply_message_record " +
            "  where tenant_id = #{pageReq.tenantId,jdbcType=BIGINT} " +
            " and object_type = #{pageReq.objectType} " +
            " and object_id = #{pageReq.objectId} " +
            " order by create_time desc")
    List<ReplyMessageRecord> pageQuery(Page<ReplyMessageRecord> page, @Param("pageReq")PageQueryReplyMessageReq pageReq);

    /**
     * @despriction：分页查询
     * @author  yeshiyuan
     * @created 2018/11/1 20:44
     * @return
     */
    @Select("select id, parent_id as parentId," +
            " message," +
            " message_type as messageType," +
            " create_time as createTime," +
            " create_by as createBy " +
            "from reply_message_record " +
            "  where tenant_id = #{tenantId,jdbcType=BIGINT} " +
            " and object_type = #{objectType} " +
            " and object_id = #{objectId} " +
            " order by create_time asc")
    List<ReplyMessageRecord> findByObjectIdAndObjectTypeAndTenantId(@Param("objectId") Long objectId, @Param("objectType") String objectType, @Param("tenantId") Long tenantId);
}
