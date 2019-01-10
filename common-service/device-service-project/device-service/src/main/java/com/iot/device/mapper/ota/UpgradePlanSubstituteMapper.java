package com.iot.device.mapper.ota;

import com.iot.device.model.ota.UpgradePlanSubstitute;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * 项目名称：立达信IOT云平台
 * 模块名称：ota
 * 功能描述：ota
 * 创建人： nongchongwei
 * 创建时间：2018年07月24日 16:58
 * 修改人： nongchongwei
 * 修改时间：2018年07月24日 16:58
 */
@Mapper
public interface UpgradePlanSubstituteMapper {

    @Select({
            "select substitute_version",
            "from ota_upgrade_plan_substitute",
            "where detail_id = #{detailId,jdbcType=BIGINT}"
    })
    List<String> selectBydetailId(Long detailId);

    @Delete({ "<script>",
            "delete from ota_upgrade_plan_substitute where detail_id in  ",
            "<foreach item='item' index='index' collection='detailIdList'",
            "open='(' separator=',' close=')'>",
            "#{item}",
            "</foreach>",
            "</script>" })
    int deleteByDetailIds(@Param("detailIdList") List<Long> detailIdList);

    @Insert({ "<script>",
            "insert into ota_upgrade_plan_substitute (id, detail_id,substitute_version) values ",
            "<foreach collection=\"list\" item=\"item\" index=\"index\" separator=\",\">",
            "(#{item.id,jdbcType=BIGINT}, #{item.detailId,jdbcType=BIGINT},#{item.substituteVersion,jdbcType=VARCHAR})",
            "</foreach>",
            "</script>" })
    int batchInsertUpgradePlanSubstitute(List<UpgradePlanSubstitute> upgradePlanSubstituteList);
}