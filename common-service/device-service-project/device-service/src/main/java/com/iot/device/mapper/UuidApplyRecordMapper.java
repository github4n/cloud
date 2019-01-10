package com.iot.device.mapper;

import com.iot.device.model.UUidApplyRecord;
import org.apache.ibatis.annotations.*;

import java.util.Date;

/**
 * 项目名称：cloud
 * 功能描述：uuid申请记录操作
 * 创建人： yeshiyuan
 * 创建时间：2018/6/29 14:45
 * 修改人： yeshiyuan
 * 修改时间：2018/6/29 14:45
 * 修改描述：
 */
@Mapper
public interface UuidApplyRecordMapper {

    /**
      * @despriction：添加uuid申请记录
      * @author  yeshiyuan
      * @created 2018/6/29 14:48
      * @param uUidApplyRecord
      * @return
      */
    @Insert("insert into uuid_apply_record("
//    		+ "id,"
    		+ "tenant_id,user_id,order_id,create_num,goods_id,uuid_apply_status,pay_status,product_id,uuid_validity_year,create_time)" +
            " values(" +
//            "   #{uuidApplyRecord.id}," +
            "   #{tenantId}," +
            "   #{userId}," +
            "   #{orderId}," +
            "   #{createNum}," +
            "   #{goodsId}," +
            "   #{uuidApplyStatus}," +
            "   #{payStatus}," +
            "   #{productId}," +
            "   #{uuidValidityYear}," +
            "   #{createTime}" +
            ")")
    @Options(useGeneratedKeys = true, keyColumn = "id", keyProperty = "id")
    int add(UUidApplyRecord uuidApplyRecord);

    @Delete("delete from uuid_apply_record where id = #{id}")
    int delete(@Param("id") Long id);

    /**
     * 描述：依据订单id查询申请记录
     * @author maochengyuan
     * @created 2018/7/4 10:25
     * @param tenantId 租户id
     * @param orderId 订单id
     * @return com.iot.device.model.UUidApplyRecord
     */
    @Select("<script>" +
            " select id," +
            " tenant_id as tenantId," +
            " user_id as userId," +
            " order_id as orderId," +
            " down_num as downNum," +
            " create_num as createNum," +
            " goods_id as goodsId," +
            " uuid_apply_status as uuidApplyStatus," +
            " pay_status as payStatus," +
            " file_id as fileId," +
            " product_id as productId," +
            " uuid_validity_year as uuidValidityYear," +
            " create_time as create_time," +
            " update_time as updateTime" +
            " from uuid_apply_record" +
            " where order_id = #{orderId,jdbcType=VARCHAR}" +
            "<if test=\"tenantId != null\"> and tenant_id = #{tenantId,jdbcType=BIGINT}</if>"+
            "</script>")
    UUidApplyRecord getUUidApplyRecordByOrderId(@Param("tenantId") Long tenantId, @Param("orderId") String orderId);

    /**
     * 描述：修改UUID申请数量
     * @author maochengyuan
     * @created 2018/7/4 10:25
     * @param applyId 申请id
     * @param createNum 申请数量
     * @return int
     */
    @Update("update uuid_apply_record" +
            " set create_num = #{createNum,jdbcType=BIGINT},update_time=#{updateTime} " +
            " where id = #{applyId,jdbcType=BIGINT}")
    int editOrderCreateNum(@Param("applyId") Long applyId, @Param("createNum") Integer createNum, @Param("updateTime") Date updateTime);


    /**
     * @despriction：修改uuid申请记录的支付状态
     * @author  yeshiyuan
     * @created 2018/7/4 16:59
     * @param orderId 订单id
     * @param 租户id 租户id
     * @param payStatus 支付状态
     * @param oldPayStatus 旧支付状态
     * @return
     */
    @Update("update uuid_apply_record set update_time=#{updateTime},pay_status=#{payStatus} where order_id=#{orderId} and tenant_id=#{tenantId} and pay_status=#{oldPayStatus}")
    int updatePayStatus(@Param("orderId") String orderId, @Param("tenantId") Long tenantId, @Param("payStatus") Integer payStatus,
                        @Param("oldPayStatus") Integer oldPayStatus, @Param("updateTime") Date updateTime) ;
}
