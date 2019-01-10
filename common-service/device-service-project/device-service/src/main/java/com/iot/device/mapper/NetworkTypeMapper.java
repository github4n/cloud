package com.iot.device.mapper;

import com.baomidou.mybatisplus.plugins.Page;
import com.iot.device.vo.rsp.NetworkTypeResp;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 项目名称：cloud
 * 功能描述：network_type 的sql语句
 * 创建人： yeshiyuan
 * 创建时间：2018/10/15 16:18
 * 修改人： yeshiyuan
 * 修改时间：2018/10/15 16:18
 * 修改描述：
 */
@Mapper
public interface NetworkTypeMapper {

    @Select("select id,network_name as networkName, description as description, type_code as typeCode from network_type where id = #{id} and is_deleted = 'valid'")
    NetworkTypeResp findById(@Param("id") Long id);

    @Select("<script>" +
            " select id,network_name as networkName, type_code as typeCode, description as description " +
            "from network_type where id in " +
            " <foreach collection='ids' item='id' open='(' close=')' separator=','>" +
            " #{id,jdbcType=BIGINT}" +
            " </foreach>" +
            " and is_deleted = 'valid'" +
            "</script>")
    List<NetworkTypeResp> findByIds(@Param("ids") List<Long> ids);


    @Select("select n.id,n.network_name as networkName, n.type_code as typeCode, n.description as description ,GROUP_CONCAT(r.technical_scheme_id) as technicalIdsStr" +
            " from network_type  n " +
            " LEFT JOIN technical_network_relate r on n.id = r.network_type_id" +
            "  GROUP BY n.id" )
    List<NetworkTypeResp> page(@Param("page")Page page);

    /**
     * @despriction：通过配网编码查找配网方式
     * @author  yeshiyuan
     * @created 2018/12/11 16:34
     */
    @Select("<script>" +
            "select id, network_name as networkName, type_code as typeCode from network_type where type_code in " +
            "<foreach collection='typeCodes' item='typeCode' open='(' close=')' separator=',' >" +
            " #{typeCode} " +
            "</foreach>" +
            "</script>")
    List<NetworkTypeResp> findByTypeCode(@Param("typeCodes") List<String> typeCodes);
}
