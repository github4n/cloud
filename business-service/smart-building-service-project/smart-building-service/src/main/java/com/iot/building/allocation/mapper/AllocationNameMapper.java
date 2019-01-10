package com.iot.building.allocation.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.annotations.UpdateProvider;
import org.apache.ibatis.type.JdbcType;

import com.iot.building.allocation.entity.AllocationName;
import com.iot.building.allocation.vo.AllocationNameReq;
import com.iot.building.allocation.vo.AllocationNameResp;

public interface AllocationNameMapper {
    @Delete({
        "delete from allocation_name",
        "where id = #{id,jdbcType=BIGINT}"
    })
    int deleteByPrimaryKey(Long id);

    @Insert({
        "insert into allocation_name (id, name, ",
        "description)",
        "values (#{id,jdbcType=BIGINT}, #{name,jdbcType=VARCHAR}, ",
        "#{description,jdbcType=VARCHAR})"
    })
    int insert(AllocationName record);

    @InsertProvider(type=AllocationNameSqlProvider.class, method="insertSelective")
    int insertSelective(AllocationName record);

    @Select({
        "select",
        "id, name, description",
        "from allocation_name",
        "where id = #{id,jdbcType=BIGINT}"
    })
    @Results({
        @Result(column="id", property="id", jdbcType=JdbcType.BIGINT, id=true),
        @Result(column="name", property="name", jdbcType=JdbcType.VARCHAR),
        @Result(column="description", property="description", jdbcType=JdbcType.VARCHAR)
    })
    AllocationName selectByPrimaryKey(Long id);

    @UpdateProvider(type=AllocationNameSqlProvider.class, method="updateByPrimaryKeySelective")
    int updateByPrimaryKeySelective(AllocationName record);

    @Update({
        "update allocation_name",
        "set name = #{name,jdbcType=VARCHAR},",
          "description = #{description,jdbcType=VARCHAR}",
        "where id = #{id,jdbcType=BIGINT}"
    })
    int updateByPrimaryKey(AllocationName record);

    @SelectProvider(type = AllocationNameSqlProvider.class, method = "queryList")
    @Results({
            @Result(column="id", property="id", jdbcType=JdbcType.BIGINT, id=true),
            @Result(column="name", property="name", jdbcType=JdbcType.VARCHAR),
            @Result(column="description", property="description", jdbcType=JdbcType.VARCHAR)
    })
    List<AllocationNameResp> getList(AllocationNameReq req);
}