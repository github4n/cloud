package com.iot.control.packagemanager.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.iot.control.packagemanager.entity.PackageProduct;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
  * @despriction：套包产品sql
  * @author  yeshiyuan
  * @created 2018/11/23 17:51
  */
@Mapper
public interface PackageProductMapper extends BaseMapper<PackageProduct>{

    /**
      * @despriction：校验产品是否已添加至套包
      * @author  yeshiyuan
      * @created 2018/11/23 19:42
      */
    @Select("<script>" +
            " select product_id from package_product where tenant_id = #{tenantId} and product_id in " +
            " <foreach collection='productIds' item='productId' open='(' close=')' separator=','>" +
            "      #{productId}" +
            " </foreach>" +
            "</script>")
    List<Long> chcekProductHadAdd(@Param("productIds") List<Long> productIds, @Param("tenantId") Long tenantId);

    @Select("select product_id from package_product where package_id = #{packageId} and tenant_id =#{tenantId}")
    List<Long> getProductIds(@Param("packageId") Long packageId, @Param("tenantId") Long tenantId);

    @Insert("<script>" +
            " insert into package_product(package_id,product_id,tenant_id,create_by,create_time)" +
            " values" +
            " <foreach collection='list' item='a' separator=','>" +
            "    (#{a.packageId},#{a.productId},#{a.tenantId},#{a.createBy},#{a.createTime})" +
            " </foreach>" +
            "</script>")
    int batchInsert(@Param("list") List<PackageProduct> list);

    @Delete("delete from package_product where package_id = #{packageId} and tenant_id = #{tenantId}")
    int deleteByPackageIdAndTenantId(@Param("packageId") Long packageId, @Param("tenantId") Long tenantId);
}
