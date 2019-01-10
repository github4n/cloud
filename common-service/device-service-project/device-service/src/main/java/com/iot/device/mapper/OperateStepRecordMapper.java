package com.iot.device.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.iot.device.model.OperateStepRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.Date;

/**
 * 项目名称：cloud
 * 功能描述：operate_step_record表sql语句
 * 创建人： yeshiyuan
 * 创建时间：2018/9/11 14:45
 * 修改人： yeshiyuan
 * 修改时间：2018/9/11 14:45
 * 修改描述：
 */
@Mapper
public interface OperateStepRecordMapper extends BaseMapper<OperateStepRecord>{

    /**
     * 查询当前所处步骤
     * @param operateId
     * @param operateType
     * @return
     */
    @Select("select id,max(step_index) as stepIndex from operate_step_record where operate_type =#{operateType} and operate_id=#{operateId} and tenant_id = #{tenantId}")
    OperateStepRecord queryCurrentStep(@Param("operateId") Long operateId, @Param("operateType") String operateType, @Param("tenantId") Long tenantId);


    /**
     * @despriction：查询某一步的操作记录是否存在
     * @author  yeshiyuan
     * @created 2018/9/11 20:29
     * @return
     */
    @Select("select id from operate_step_record where operate_type =#{operateType} and operate_id=#{operateId} and tenant_id = #{tenantId} and step_index=#{stepIndex}")
    Long queryExists(@Param("operateId") Long operateId, @Param("operateType") String operateType, @Param("tenantId") Long tenantId, @Param("stepIndex") Integer stepIndex);

    /**
     * @despriction：修改操作步骤记录
     * @author  yeshiyuan
     * @created 2018/9/11 20:36
     * @return
     */
    @Update("update operate_step_record set update_time = #{updateTime}, update_by = #{userId} where id = #{id,jdbcType=BIGINT}")
    int update(@Param("id") Long id, @Param("userId") Long userId, @Param("updateTime") Date updateTime);
}
