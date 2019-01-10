package com.iot.device.mapper.ota;



import com.iot.device.model.ota.FirmwareVersion;
import com.iot.device.vo.req.ota.FirmwareVersionDto;
import com.iot.device.vo.req.ota.FirmwareVersionReqDto;
import com.iot.device.vo.req.ota.FirmwareVersionSearchReqDto;
import com.iot.device.vo.req.ota.FirmwareVersionUpdateVersionDto;
import com.iot.device.vo.rsp.ota.FirmwareVersionPageResp;
import com.iot.device.vo.rsp.ota.FirmwareVersionQueryResp;
import com.iot.device.vo.rsp.ota.FirmwareVersionResp;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;

import java.util.Date;
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
public interface FirmwareVersionMapper {

    /**
     * 描述：新增OTA升级版本信息
     * @author maochengyuan
     * @created 2018/7/25 15:54
     * @param record OTA版本
     * @return int
     */
    @Insert({
        "insert into ota_firmware_version (id, tenant_id, ",
        "product_id, ota_version, ",
        "ota_type, ota_file_id, ",
        "ota_md5, fw_type, ",
        "create_time, create_by, ",
        "update_time, update_by)",
        "values (#{id,jdbcType=BIGINT}, #{tenantId,jdbcType=BIGINT}, ",
        "#{productId,jdbcType=BIGINT}, #{otaVersion,jdbcType=VARCHAR}, ",
        "#{otaType,jdbcType=CHAR}, #{otaFileId,jdbcType=VARCHAR}, ",
        "#{otaMd5,jdbcType=VARCHAR}, #{fwType,jdbcType=TINYINT}, ",
        "#{createTime,jdbcType=TIMESTAMP}, #{createBy,jdbcType=BIGINT}, ",
        "#{updateTime,jdbcType=TIMESTAMP}, #{updateBy,jdbcType=BIGINT})"
    })
    int createFirmwareVersion(FirmwareVersionReqDto record);

    @Insert({
            "insert into ota_firmware_version (id, tenant_id, ",
            "product_id, ota_version, ",
            "ota_type, ota_file_id, ",
            "ota_md5, fw_type, init_version, ",
            "create_time, create_by, ",
            "update_time, update_by)",
            "values (#{id,jdbcType=BIGINT}, #{tenantId,jdbcType=BIGINT}, ",
            "#{productId,jdbcType=BIGINT}, #{otaVersion,jdbcType=VARCHAR}, ",
            "#{otaType,jdbcType=CHAR}, #{otaFileId,jdbcType=VARCHAR}, ",
            "#{otaMd5,jdbcType=VARCHAR}, #{fwType,jdbcType=TINYINT}, 1, ",
            "#{createTime,jdbcType=TIMESTAMP}, #{createBy,jdbcType=BIGINT}, ",
            "#{updateTime,jdbcType=TIMESTAMP}, #{updateBy,jdbcType=BIGINT})"
    })
    int initFirmwareVersion(FirmwareVersionDto record);

    @Select("select id as Id," +
            " tenant_id as tenantId," +
            " product_id as productId," +
            " ota_version as otaVersion," +
            " ota_type as otaType," +
            " ota_file_id as otaFileId," +
            " ota_md5 as otaMd5," +
            " fw_type as fwType," +
            " create_time as createTime," +
            " version_online_time as versionOnlineTime " +
            " from ota_firmware_version where" +
            " tenant_id = #{dto.tenantId,jdbcType=BIGINT}" +
            " and product_id = #{dto.productId,jdbcType=BIGINT}   and version_online_time is  null" +
            " order by create_time desc")
    List<FirmwareVersionResp> getFirmwareVersionListByProductId(com.baomidou.mybatisplus.plugins.Page<FirmwareVersionResp> page, @Param("dto") FirmwareVersionSearchReqDto dto);


    @Select("select  ota_version  from ota_firmware_version where tenant_id = #{tenantId,jdbcType=BIGINT}" +
            " and product_id = #{productId,jdbcType=BIGINT}   and version_online_time is  null ")
    List<String> getNotOnlineFirmwareList(@Param("tenantId") Long tenantId, @Param("productId") Long productId);

    @Select({
        "select",
        "id, tenant_id, product_id, ota_version, ota_type, ota_file_id, ota_md5, fw_type, ",
        "create_time, create_by, update_time, update_by",
        "from ota_firmware_version",
        "where id = #{id,jdbcType=BIGINT}"
    })
    @Results({
        @Result(column="id", property="id", jdbcType=JdbcType.BIGINT, id=true),
        @Result(column="tenant_id", property="tenantId", jdbcType=JdbcType.BIGINT),
        @Result(column="product_id", property="productId", jdbcType=JdbcType.BIGINT),
        @Result(column="ota_version", property="otaVersion", jdbcType=JdbcType.VARCHAR),
        @Result(column="ota_type", property="otaType", jdbcType=JdbcType.CHAR),
        @Result(column="ota_file_id", property="otaFileId", jdbcType=JdbcType.VARCHAR),
        @Result(column="ota_md5", property="otaMd5", jdbcType=JdbcType.VARCHAR),
        @Result(column="fw_type", property="fwType", jdbcType=JdbcType.TINYINT),
        @Result(column="create_time", property="createTime", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="create_by", property="createBy", jdbcType=JdbcType.BIGINT),
        @Result(column="update_time", property="updateTime", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="update_by", property="updateBy", jdbcType=JdbcType.BIGINT)
    })
    FirmwareVersion selectByPrimaryKey(Long id);



    @Select({
            "select",
            "id, tenant_id, product_id, ota_version, ota_type, ota_file_id, ota_md5, fw_type, ",
            "create_time, create_by, update_time, update_by",
            "from ota_firmware_version",
            "where product_id = #{prodId,jdbcType=BIGINT} and tenant_id = #{tenantId,jdbcType=BIGINT} AND init_version=1 "
    })
    @Results({
            @Result(column="id", property="id", jdbcType=JdbcType.BIGINT, id=true),
            @Result(column="tenant_id", property="tenantId", jdbcType=JdbcType.BIGINT),
            @Result(column="product_id", property="productId", jdbcType=JdbcType.BIGINT),
            @Result(column="ota_version", property="otaVersion", jdbcType=JdbcType.VARCHAR),
            @Result(column="ota_type", property="otaType", jdbcType=JdbcType.CHAR),
            @Result(column="ota_file_id", property="otaFileId", jdbcType=JdbcType.VARCHAR),
            @Result(column="ota_md5", property="otaMd5", jdbcType=JdbcType.VARCHAR),
            @Result(column="fw_type", property="fwType", jdbcType=JdbcType.TINYINT),
            @Result(column="create_time", property="createTime", jdbcType=JdbcType.TIMESTAMP),
            @Result(column="create_by", property="createBy", jdbcType=JdbcType.BIGINT),
            @Result(column="update_time", property="updateTime", jdbcType=JdbcType.TIMESTAMP),
            @Result(column="update_by", property="updateBy", jdbcType=JdbcType.BIGINT)
    })
    List<FirmwareVersionQueryResp> selectByProductId(@Param("tenantId") Long tenantId, @Param("prodId") Long prodId);

    /**
     * 描述：查询OTA版本合法性（唯一性）
     * @author maochengyuan
     * @created 2018/7/25 14:14
     * @param tenantId 租户id
     * @param prodId 产品id
     * @param otaVersion ota版本
     * @return int
     */
    @Select("select count(1) from" +
            " ota_firmware_version where" +
            " tenant_id = #{tenantId,jdbcType=BIGINT}" +
            " and product_id = #{prodId,jdbcType=BIGINT}" +
            " and ota_version = #{otaVersion,jdbcType=BIGINT}")
    int checkVersionLegality(@Param("tenantId") Long tenantId, @Param("prodId") Long prodId, @Param("otaVersion") String otaVersion);

    /**
     * 描述：依据产品ID获取升级版本列表
     * @author maochengyuan
     * @created 2018/7/30 9:51
     * @param tenantId 租户id
     * @param prodId 产品id
     * @return java.util.List<java.lang.String>
     */
    @Select("select ota_version from" +
            " ota_firmware_version" +
            " where" +
            " tenant_id = #{tenantId,jdbcType=BIGINT}" +
            " and product_id = #{prodId,jdbcType=BIGINT}" +
            " order by create_time")
    List<String> getOTAVersionListByProductId(@Param("tenantId") Long tenantId, @Param("prodId") Long prodId);

    /**
     * 描述：依据产品ID获取升级版本信息列表
     * @author maochengyuan
     * @created 2018/7/30 9:51
     * @param tenantId 租户id
     * @param prodId 产品id
     * @return java.util.List<java.lang.String>
     */
    @Select("select ota_version, version_online_time from" +
            " ota_firmware_version" +
            " where" +
            " tenant_id = #{tenantId,jdbcType=BIGINT}" +
            " and product_id = #{prodId,jdbcType=BIGINT}" +
            " and version_online_time is not null" +
            " order by create_time")
    List<FirmwareVersion> getOTAVersionInfoListByProductId(com.baomidou.mybatisplus.plugins.Page<FirmwareVersion> page, @Param("tenantId") Long tenantId, @Param("prodId") Long prodId);


    /**
     * 描述：依据产品ID获取升级版本信息列表
     * @author nongchongwei
     * @date 2018/9/20 15:05
     * @param
     * @return
     */
    @Select("select ota_version, version_online_time from" +
            " ota_firmware_version" +
            " where" +
            " tenant_id = #{tenantId,jdbcType=BIGINT}" +
            " and product_id = #{prodId,jdbcType=BIGINT}" +
            " order by create_time")
    List<FirmwareVersionQueryResp> getAllOTAVersionInfoListByProductId(@Param("tenantId") Long tenantId, @Param("prodId") Long prodId);

    /** 
     * 描述：依据产品ID获取版分页查询
     * @author maochengyuan
     * @created 2018/7/30 11:24
     * @param req
     * @return java.util.List<com.iot.device.vo.rsp.ota.FirmwareVersionPageResp>
     */
    @Select( "<script>"+
            "select dv.version as otaVersion, count(1) as versionQuantity from" +
            " ota_device_version dv," +
            " device de where" +
            " dv.device_id = de.uuid" +
            " and de.tenant_id = #{dto.tenantId,jdbcType=BIGINT}" +
            " and de.product_id = #{dto.productId,jdbcType=BIGINT}" +
            " and dv.version in " +
            "<foreach item='version' index='index' collection='dto.otaVersion' open='(' separator=',' close=')'>"+
            "#{version}"+
            "</foreach>"+
            " group by dv.version"+
            "</script>")
    List<FirmwareVersionPageResp> getOTAVersionPageByProductId(@Param("dto") FirmwareVersionSearchReqDto req);

    @Update({
        "update ota_firmware_version",
        "set tenant_id = #{tenantId,jdbcType=BIGINT},",
          "product_id = #{productId,jdbcType=BIGINT},",
          "ota_version = #{otaVersion,jdbcType=VARCHAR},",
          "ota_type = #{otaType,jdbcType=CHAR},",
          "ota_file_id = #{otaFileId,jdbcType=VARCHAR},",
          "ota_md5 = #{otaMd5,jdbcType=VARCHAR},",
          "fw_type = #{fwType,jdbcType=TINYINT},",
          "create_time = #{createTime,jdbcType=TIMESTAMP},",
          "create_by = #{createBy,jdbcType=BIGINT},",
          "update_time = #{updateTime,jdbcType=TIMESTAMP},",
          "update_by = #{updateBy,jdbcType=BIGINT}",
        "where id = #{id,jdbcType=BIGINT}"
    })
    int updateByPrimaryKey(FirmwareVersion record);


    /**
     * 描述：查询固件信息
     * @author nongchongwei
     * @date 2018/9/11 16:11
     * @param
     * @return
     */
    @Select("select id," +
            " tenant_id as tenantId," +
            " product_id as productId," +
            " ota_version as otaVersion," +
            " ota_type as otaType," +
            " ota_file_id as otaFileId," +
            " ota_md5 as otaMd5," +
            " fw_type as fwType," +
            " create_time as createTime" +
            " from ota_firmware_version where" +
            " ota_version = #{otaVersion,jdbcType=VARCHAR}" +
            " and product_id = #{productId,jdbcType=BIGINT}" +
            " order by create_time desc")
    FirmwareVersion getFirmwareVersionByProductIdAndVersion(@Param("productId") Long productId, @Param("otaVersion") String otaVersion);

    /**
     * 描述：更新版本上线时间
     * @author nongchongwei
     * @date 2018/9/11 16:02
     * @param
     * @return
     */
    @Update( "<script>"+
            "update ota_firmware_version dv set dv.version_online_time = #{dto.versionOnlineTime,jdbcType=TIMESTAMP}" +
            " where dv.tenant_id = #{dto.tenantId,jdbcType=BIGINT}" +
            " and dv.product_id = #{dto.productId,jdbcType=BIGINT}" +
            " and dv.ota_version in " +
            "<foreach item='version' index='index' collection='dto.otaVersion' open='(' separator=',' close=')'>"+
            "#{version}"+
            "</foreach>"+
            "</script>")
    int updateVersionOnlineTime(@Param("dto") FirmwareVersionUpdateVersionDto dto);

    @Update( "<script>"+
            "update ota_firmware_version dv set dv.version_online_time = #{dto.versionOnlineTime,jdbcType=TIMESTAMP}"+
            " where dv.tenant_id = #{dto.tenantId,jdbcType=BIGINT}"+
            " and dv.product_id = #{dto.productId,jdbcType=BIGINT}"+
            "</script>")
    int updateVersionOnlineTimeNoVersion(@Param("dto") FirmwareVersionUpdateVersionDto dto);



    @Update( "<script>"+
            "update ota_firmware_version dv set dv.version_online_time = #{dto.versionOnlineTime,jdbcType=TIMESTAMP}" +
            " where dv.product_id = #{dto.productId,jdbcType=BIGINT}" +
            " and dv.ota_version in " +
            "<foreach item='version' index='index' collection='dto.otaVersion' open='(' separator=',' close=')'>"+
            "#{version}"+
            "</foreach>"+
            "</script>")
    int updateVersionOnlineTimeByProductId(@Param("dto") FirmwareVersionUpdateVersionDto dto);

    @Delete("delete from ota_firmware_version where id =  #{id,jdbcType=BIGINT}")
    int deleteByFirmwareId(@Param("id") Long id);

    @Delete("delete from ota_firmware_version where product_id =  #{productId,jdbcType=BIGINT} and tenant_id = #{tenantId,jdbcType=BIGINT} ")
    int deleteByFProdId(@Param("tenantId") Long tenantId,@Param("productId") Long productId);




}