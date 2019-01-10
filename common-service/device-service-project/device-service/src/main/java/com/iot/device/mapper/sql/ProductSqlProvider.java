package com.iot.device.mapper.sql;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.iot.common.util.StringUtil;
import com.iot.device.model.DeviceCatalog;
import com.iot.device.model.DeviceType;
import com.iot.device.model.Product;
import org.apache.ibatis.annotations.Param;
import org.springframework.util.StringUtils;

public class ProductSqlProvider {

    public String findProductListByTenantId(@Param("tenantId") Long tenantId, @Param("productName") String productName) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select ");
        sql.append(" p1.* , p2.name as deviceTypeName, c.name as catalogName ");

        sql.append(" FROM " + Product.TABLE_NAME + " as p1 LEFT JOIN " + DeviceType.TABLE_NAME + " p2 ON (p2.id = p1.device_type_id) ");

        //关联状态表
        sql.append(" LEFT JOIN " + DeviceCatalog.TABLE_NAME + " AS c ON (p2.device_catalog_id = c.id) ");

        sql.append(" WHERE p1.tenant_id = #{tenantId} ");

        if (!StringUtil.isEmpty(productName)) {
            sql.append(" and  p1.product_name like '" + productName + "%' ");
        }

        sql.append(" ORDER BY p1.device_type_id desc, p1.product_name desc ");

        return sql.toString();
    }
    /**
     * @descrpiction:
     * @author wucheng
     * @created 2018/11/8 14:35
     * @param
     * @return
     */
    public String getProductListByTenantIdAndCommunicationMode(@Param("tenantId") Long tenantId, @Param("communicationMode") Long communicationMode) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT id,tenant_id,device_type_id, product_name, communication_mode,transmission_mode, create_time, update_time,model,config_net_mode," +
                "is_kit,remark, is_direct_device,icon,develop_status,enterprise_develop_id,audit_status, service_goo_audit_status, service_alx_audit_status, " +
                "0 as isSelected");

        if (tenantId == -1) {
            sql.append(" , 0 as myDeviceType");
        } else {
            sql.append(" , 1 as myDeviceType");
        }
        sql.append(" FROM product WHERE");
        sql.append(" audit_status = 2");
        sql.append(" and communication_mode = #{communicationMode}");
        sql.append(" and tenant_id = #{tenantId} order by create_time desc");
        return sql.toString();
    }

    public String findDirectProductListByTenantId(@Param("tenantId") Long tenantId, @Param("productName") String productName) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select ");
        sql.append(" p1.* , p2.name as deviceTypeName, c.name as catalogName ");

        sql.append(" FROM " + Product.TABLE_NAME + " as p1 LEFT JOIN " + DeviceType.TABLE_NAME + " p2 ON (p2.id = p1.device_type_id) ");

        //关联状态表
        sql.append(" LEFT JOIN " + DeviceCatalog.TABLE_NAME + " AS c ON (p2.device_catalog_id = c.id) ");

        sql.append(" WHERE p1.is_direct_device = 1 and p1.audit_status = 2 and p1.tenant_id = #{tenantId} ");

        if (!StringUtil.isEmpty(productName)) {
            sql.append(" and  p1.product_name like '" + productName + "%' ");
        }

        sql.append(" ORDER BY p1.device_type_id desc, p1.product_name desc ");

        return sql.toString();
    }

    public String findAllDirectProductList() {
        StringBuilder sql = new StringBuilder();
        sql.append(" select ");
        sql.append(" p1.* , p2.name as deviceTypeName, c.name as catalogName ");

        sql.append(" FROM " + Product.TABLE_NAME + " as p1 LEFT JOIN " + DeviceType.TABLE_NAME + " p2 ON (p2.id = p1.device_type_id) ");

        //关联状态表
        sql.append(" LEFT JOIN " + DeviceCatalog.TABLE_NAME + " AS c ON (p2.device_catalog_id = c.id) ");

        sql.append(" WHERE 1=1 ");

        sql.append(" and p1.is_direct_device = 1");

        sql.append(" ORDER BY p1.id desc ");

        return sql.toString();
    }

    public String findProductByDeviceTypeId(@Param("tenantId") Long tenantId, @Param("productName") String productName) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select ");
        sql.append(" p1.* , " +
                "p2.name as deviceTypeName, " +
                "c.name as catalogName "
        );

        sql.append(" FROM " + Product.TABLE_NAME + " as p1 LEFT JOIN " + DeviceType.TABLE_NAME + " p2 ON (p2.id = p1.device_type_id) ");

        //关联状态表
        sql.append(" LEFT JOIN " + DeviceCatalog.TABLE_NAME + " AS c ON (p2.device_catalog_id = c.id) ");

        sql.append(" WHERE p1.tenant_id = #{tenantId} ");


        if (!StringUtil.isEmpty(productName)) {

            sql.append(" and  p1.product_name like '" + productName + "%'");
        }

        return sql.toString();
    }

    public String selectProductPage(@Param("page") Page<Product> page, @Param("ew") EntityWrapper<Product> ew) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select ");
        sql.append(" a.* ");
        sql.append(" ,b.name AS deviceTypeName ");
        sql.append(" FROM ");
        sql.append(Product.TABLE_NAME).append(" AS a ").append(" LEFT JOIN ").append(DeviceType.TABLE_NAME).append(" AS b ");
        sql.append(" ON a.device_type_id  = b.id ");

        if (!StringUtils.isEmpty(ew.getSqlSegment())) {
            sql.append(" where ");
            sql.append(" ${ew.sqlSegment} ");
        }

        return sql.toString();

    }
}
