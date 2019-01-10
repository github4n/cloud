package com.iot.tenant.mapper;

import com.baomidou.mybatisplus.plugins.Page;
import com.iot.tenant.entity.LangInfoBase;
import com.iot.tenant.vo.req.lang.DelLangInfoBaseReq;
import com.iot.tenant.vo.req.lang.QueryLangInfoBasePageReq;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * 项目名称：cloud
 * 功能描述：
 * 创建人： yeshiyuan
 * 创建时间：2018/9/29 16:54
 * 修改人： yeshiyuan
 * 修改时间：2018/9/29 16:54
 * 修改描述：
 */
@Mapper
public interface LangInfoBaseMapper {

    /**
      * @despriction：删除文案基础信息
      * @author  yeshiyuan
      * @created 2018/9/29 17:11
      * @return
      */
    @Delete("<script>" +
            " delete from lang_info_base where object_type = #{objectType} and object_id = #{objectId} " +
            "<if test=\"belongModule!=null and belongModule!=''\"> " +
            " and belong_module = #{belongModule}" +
            "</if>" +
            "</script>")
    int deleteByObjectTypeAndObjectIdAndModule(@Param("objectType") String objectType, @Param("objectId") Long objectId, @Param("belongModule") String belongModule);

    /**
      * @despriction：删除配网步骤对应的文案
      * @author  yeshiyuan
      * @created 2018/10/17 11:09
      * @return
      */
    @Delete("delete from lang_info_base where object_type = #{objectType} " +
            " and object_id = #{objectId} " +
            " and lang_key like concat('%',#{langKey},'%')")
    int deleteNetworkStepLang(@Param("objectType") String objectType, @Param("objectId") Long objectId,
                              @Param("langKey") String likeLangKey);

    /**
     * @despriction：删除文案基础信息
     * @author  yeshiyuan
     * @created 2018/9/29 17:11
     * @return
     */
    @Delete("delete from lang_info_base where id = #{id,jdbcType=BIGINT}")
    int deleteById(@Param("id") Long id);

    /**
     * @despriction：删除文案基础信息
     * @author  yeshiyuan
     * @created 2018/9/29 17:11
     * @return
     */
    @Delete("<script>" +
            " delete from lang_info_base where id in " +
            " <foreach collection='ids' item='id' open='(' close=')' separator=','>" +
            " #{id,jdbcType=BIGINT}" +
            "</foreach>" +
            "</script>")
    int deleteByIds(@Param("ids") List<Long> ids);

    /**
      * @despriction：批量插入
      * @author  yeshiyuan
      * @created 2018/9/29 17:31
      * @return
      */
    @Insert("<script>" +
            "insert into lang_info_base(" +
            "	  object_id," +
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
    int batchInsert(@Param("langInfos") List<LangInfoBase> langInfos);


    @Select("select id from lang_info_base where object_type = #{objectType} and object_id = #{objectId} " +
            " and lang_key = #{langKey} and is_deleted = 'valid'")
    List<LangInfoBase> findByObjectIdAndObjectTypeAndLangKey(@Param("objectType") String objectType, @Param("objectId") Long objectId, @Param("langKey") String langKey);

    @Update("update lang_info_base set lang_value=#{langInfoBase.langValue}, update_by=#{langInfoBase.updateBy}," +
            "update_time=#{langInfoBase.updateTime} where object_type = #{langInfoBase.objectType} and object_id = #{langInfoBase.objectId} " +
            "and lang_key = #{langInfoBase.langKey} and lang_type = #{langInfoBase.langType} and is_deleted = 'valid'")
    int updateLangInfoBase(@Param("langInfoBase") LangInfoBase langInfoBase);

    @Select("select id,object_type as objectType,belong_module as belongModule," +
            " lang_key as langKey," +
            " lang_value as langValue," +
            " lang_type as langType " +
            " from lang_info_base where object_type = #{objectType} and object_id = #{objectId} " +
            " and is_deleted = 'valid' order by belong_module asc")
    List<LangInfoBase> findByObjectIdAndObjectType(@Param("objectType") String objectType, @Param("objectId") Long objectId);

    @Select("select id,object_type as objectType,belong_module as belongModule," +
            " lang_key as langKey," +
            " lang_value as langValue," +
            " lang_type as langType " +
            " from lang_info_base where object_type = #{objectType} and object_id = #{objectId} " +
            " and lang_key like concat('%',#{langKey},'%')" +
            " and is_deleted = 'valid' order by belong_module asc")
    List<LangInfoBase> findByObjectIdAndObjectTypeAndLikeKey(@Param("objectType") String objectType, @Param("objectId") Long objectId, @Param("langKey") String langKey);

    @Select("<script>select id,object_type as objectType,belong_module as belongModule," +
            " lang_key as langKey," +
            " lang_value as langValue," +
            " lang_type as langType " +
            " from lang_info_base where object_type = #{objectType} and object_id = #{objectId} " +
            " <if test=\"belongModule!=null and belongModule!=''\">" +
            "   and belong_module = #{belongModule}" +
            " </if>" +
            " and is_deleted = 'valid' order by belong_module asc, lang_key" +
            "</script>")
    List<LangInfoBase> findByObjectIdAndObjectTypeAndModule(@Param("objectType") String objectType, @Param("objectId") Long objectId, @Param("belongModule") String belongModule);

    /**
     * @despriction：删除基础文案
     * @author  yeshiyuan
     * @created 2018/10/10 15:35
     * @return
     */
    @Delete("<script>" +
            "delete from lang_info_base where object_id = #{req.objectId} and object_type = #{req.objectType}" +
            " and is_deleted = 'valid' and lang_key in " +
            " <foreach collection='req.keys' item='langKey' open='(' close=')' separator=','>" +
            " #{langKey}" +
            " </foreach>" +
            "</script>")
    int deleteByObjectIdAndTypeAndKeys(@Param("req") DelLangInfoBaseReq delLangInfoBaseReq);

    /**
      * @despriction：根据文案key 分组查询
      * @author  yeshiyuan
      * @created 2018/10/12 14:37
      * @return
      */
    @Select("<script>" +
            "SELECT lang_key, GROUP_CONCAT(CONCAT(lang_type,':',lang_value) separator '@@') as langValue" +
            " from lang_info_base where object_type = #{pageReq.objectType} and object_id = #{pageReq.objectId} " +
            " <if test=\"pageReq.belongModule!=null and pageReq.belongModule!=''\">" +
            "   and belong_module = #{pageReq.belongModule}" +
            " </if>" +
            " <if test=\"pageReq.langKey!=null and pageReq.langKey!=''\">" +
            "   and lang_key like concat('%',#{pageReq.langKey},'%')" +
            " </if>" +
            " and is_deleted = 'valid' " +
            " group by lang_key" +
            " order by belong_module asc, id desc " +
            "</script>" )
    List<LangInfoBase> queryLangValueGroupByLangKey(@Param("a") Page<LangInfoBase> page, @Param("pageReq")QueryLangInfoBasePageReq pageReq);
}
