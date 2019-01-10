package com.iot.design.dict.mapper;

import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import com.iot.design.dict.dto.SystemDict;
import com.iot.design.project.dto.ProjectDTO;
import com.iot.design.project.vo.req.ProjectPageReq;
import com.iot.design.project.vo.resp.ProjectPageResp;
import org.apache.ibatis.annotations.*;

import java.util.List;


/**
 * 创建人:chenweida
 * 创建时间:2018/8/10
 */
@Mapper
public interface DictMapper {

    @Select({"SELECT   " +
            "    dict_name dict_name,   " +
            "    dict_code dict_code   " +
            " FROM   " +
            "    system_dict    " +
            "WHERE   " +
            "    data_status = 1  " +
            "   and dict = #{dict,jdbcType=VARCHAR} " +
            " <if test=\"tenantId != null\"> and name=#{tenantId,jdbcType=INTEGER}</if>"})
    List<SystemDict> listDict(@Param("dict") String dict, @Param("tenantId") Long tenantId);
}
