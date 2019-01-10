package com.iot.tenant.mapper;

import com.baomidou.mybatisplus.plugins.Page;
import com.iot.tenant.entity.LangInfoTenant;
import com.iot.tenant.vo.req.lang.CopyLangInfoReq;
import com.iot.tenant.vo.req.lang.QueryLangInfoTenantPageReq;
import org.apache.ibatis.annotations.*;

import java.util.Date;
import java.util.List;

/**
 * 项目名称：cloud
 * 功能描述：租户文案sql
 * 创建人： yeshiyuan
 * 创建时间：2018/9/30 14:06
 * 修改人： yeshiyuan
 * 修改时间：2018/9/30 14:06
 * 修改描述：
 */
@Mapper
public interface LangInfoTenantMapper {

    /**
     * @despriction：复制一份基础文案至对应的租户
     * @author  yeshiyuan
     * @created 2018/9/30 14:29
     * @return
     */
    @Insert("insert into lang_info_tenant(tenant_id,object_type,object_id,lang_type,lang_key,lang_value,create_by,create_time,belong_module) " +
            "select #{req.tenantId,jdbcType=BIGINT},object_type,#{req.objectId},lang_type," +
            "   lang_key,lang_value,#{req.userId,jdbcType=BIGINT},#{createTime},belong_module " +
            " from lang_info_base where object_id = #{req.copyObjectId} and object_type = #{req.objectType} and is_deleted = 'valid'")
    int copyLangInfo(@Param("req") CopyLangInfoReq copyLangInfoReq, @Param("createTime") Date now);

    /**
     * @despriction：查询租户下的某个对象文案
     * @author  yeshiyuan
     * @created 2018/9/30 15:01
     * @return
     */
    @Select("<script>" +
            "select id,lang_key as langKey," +
            " lang_value as langValue," +
            " lang_type as langType " +
            " from lang_info_tenant where object_type = #{objectType} " +
            " and object_id = #{objectId} and tenant_id = #{tenantId,jdbcType=BIGINT}" +
            " <if test=\"belongModule!=null and belongModule!=''\">" +
            "  and belong_module = #{belongModule}" +
            " </if>" +
            " and is_deleted = 'valid'" +
            " order by id asc" +
            "</script>")
    List<LangInfoTenant> queryLangInfos(@Param("tenantId") Long tenantId, @Param("objectId") String objectId,
                                        @Param("objectType") String objectType, @Param("belongModule") String belongModule);

    /**
     * @despriction：删除租户某个对象对应的文案信息
     * @author  yeshiyuan
     * @created 2018/9/29 17:11
     * @return
     */
    @Delete("delete from lang_info_tenant where object_type = #{objectType} and object_id = #{objectId} and tenant_id = #{tenantId,jdbcType=BIGINT}")
    int deleteByObjectTypeAndObjectIdAndTenantId(@Param("objectType") String objectType, @Param("objectId") String objectId, @Param("tenantId") Long tenantId);

    @Delete("<script>" +
            " delete from lang_info_tenant where object_type = #{objectType} and object_id = #{objectId} and tenant_id = #{tenantId,jdbcType=BIGINT}" +
            " <if test=\"belongModule!=null and belongModule!=''\">" +
            "   and belong_module = #{belongModule}" +
            " </if>" +
            "</script>")
    int deleteByTypeAndIdAndTenantAndModule(@Param("objectType") String objectType, @Param("objectId") String objectId,
                                            @Param("tenantId") Long tenantId, @Param("belongModule") String belongModule);

    /**
     * @despriction：批量插入
     * @author  yeshiyuan
     * @created 2018/9/29 17:31
     * @return
     */
    @Insert("<script>" +
            "insert into lang_info_tenant(" +
            "	  tenant_id," +
            "     object_id," +
            "	  object_type," +
            "	  lang_type," +
            "	  lang_key," +
            "	  lang_value," +
            "	  create_by," +
            "	  create_time," +
            "     belong_module" +
            ") values " +
            "<foreach collection='langInfos' item='lang' separator=','>" +
            " (" +
            " #{lang.tenantId}," +
            " #{lang.objectId}," +
            " #{lang.objectType}," +
            " #{lang.langType}," +
            " #{lang.langKey}," +
            " #{lang.langValue}," +
            " #{lang.createBy}," +
            " #{lang.createTime}," +
            " #{lang.belongModule}" +
            " )" +
            "</foreach>" +
            "</script>")
    int batchInsert(@Param("langInfos") List<LangInfoTenant> langInfos);

    /**
      * @despriction：删除语言类型对应的文案
      * @author  yeshiyuan
      * @created 2018/10/12 10:41
      * @return
      */
    @Delete("<script>" +
            " delete from lang_info_tenant where object_type = #{objectType} and object_id = #{objectId} and tenant_id = #{tenantId,jdbcType=BIGINT}" +
            " and lang_type in " +
            " <foreach collection='langTypes' item='langType' open='(' close=')' separator=','>" +
            "    #{langType}" +
            " </foreach>" +
            "</script>")
    int deleteByLangTypes(@Param("objectType") String objectType, @Param("objectId") String objectId,
                          @Param("tenantId") Long tenantId,@Param("langTypes") List<String> langTypes);

    /**
      * @despriction：查询某对象所有的文案key
      * @author  yeshiyuan
      * @created 2018/10/12 10:47
      * @return
      */
    @Select("select lang_key as langKey, belong_module as belongModule" +
            " from lang_info_tenant where object_type = #{objectType} and object_id = #{objectId} and tenant_id = #{tenantId,jdbcType=BIGINT}" +
            " group by lang_key")
    List<LangInfoTenant> queryLangKeys(@Param("objectType") String objectType, @Param("objectId") String objectId,
                                       @Param("tenantId") Long tenantId);

    @Update("update lang_info_tenant set lang_value = #{lang.langValue}," +
            " update_by = #{lang.updateBy,jdbcType=BIGINT}," +
            " update_time = #{lang.updateTime} " +
            "where object_type = #{lang.objectType} and object_id = #{lang.objectId} and tenant_id = #{lang.tenantId,jdbcType=BIGINT}" +
            " and lang_key = #{lang.langKey} and lang_type = #{lang.langType}")
    int update(@Param("lang") LangInfoTenant lang);

    @Select("select count(1)" +
            " from lang_info_tenant where object_type = #{objectType} and object_id = #{objectId} " +
            " and tenant_id = #{tenantId,jdbcType=BIGINT} and lang_type = #{langType} and is_deleted = 'valid'" +
            " limit 1")
    int checkLangTypeIsExist(@Param("objectType") String objectType, @Param("objectId") String objectId,
                             @Param("tenantId") Long tenantId, @Param("langType") String langType);

    /**
     * @despriction：根据文案key 分组查询
     * @author  yeshiyuan
     * @created 2018/10/12 14:37
     * @return
     */
    @Select("<script>" +
            "SELECT lang_key, GROUP_CONCAT(CONCAT(lang_type,':',lang_value) ORDER BY id ASC separator '@@') as langValue" +
            " from lang_info_tenant where object_type = #{pageReq.objectType} " +
            " and object_id = #{pageReq.objectId} and tenant_id = #{pageReq.tenantId}" +
            " <if test=\"pageReq.belongModule!=null and pageReq.belongModule!=''\">" +
            "   and belong_module = #{pageReq.belongModule}" +
            " </if>" +
            " <if test=\"pageReq.langKey!=null and pageReq.langKey!=''\">" +
            "   and lang_key like concat('%',#{pageReq.langKey},'%')" +
            " </if>" +
            " and is_deleted = 'valid' " +
            " group by lang_key" +
            " order by belong_module asc, lang_key desc  " +
            "</script>" )
    List<LangInfoTenant> queryLangValueGroupByLangKey(@Param("a") Page<LangInfoTenant> page, @Param("pageReq")QueryLangInfoTenantPageReq pageReq);


    @Select("<script>" +
            "select id,lang_key as langKey," +
            " lang_value as langValue," +
            " lang_type as langType " +
            " from lang_info_tenant where object_type = #{objectType} " +
            " and tenant_id = #{tenantId,jdbcType=BIGINT} " +
            " and object_id in " +
            " <foreach collection='objectIds' open='(' close=')' separator=',' item='objectId'>" +
            " #{objectId}" +
            " </foreach>" +
            " and is_deleted = 'valid'" +
            "</script>")
    List<LangInfoTenant> findByObjectIds(@Param("tenantId") Long tenantId, @Param("objectIds") List<String> objectIds,
                                        @Param("objectType") String objectType);

    @Select("select id,object_type as objectType,belong_module as belongModule," +
            " lang_key as langKey," +
            " lang_value as langValue," +
            " lang_type as langType " +
            " from lang_info_tenant where object_type = #{objectType} and object_id = #{objectId} " +
            " and tenant_id = #{tenantId}" +
            " and lang_key like concat('%',#{langKey},'%')" +
            " and is_deleted = 'valid' order by belong_module asc")
    List<LangInfoTenant> findByObjectTypeAndObjectIdAndLikeKey(@Param("objectType") String objectType, @Param("objectId") String objectId,
                                                               @Param("tenantId") Long tenantId, @Param("langKey") String langKey);

    @Delete("delete from lang_info_tenant where object_type = #{objectType} and tenant_id = #{tenantId,jdbcType=BIGINT} and object_id like concat(#{appId}, '_%') ")
    int deleteAppNetworkData(@Param("tenantId") Long tenantId, @Param("appId") Long appId, @Param("objectType") String objectType);
}
