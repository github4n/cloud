package com.iot.design.config.mybatis;

import com.baomidou.mybatisplus.plugins.parser.tenant.TenantHandler;
import com.iot.saas.SaaSContextHolder;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.LongValue;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * @Author: xfz
 * @Descrpiton: 租户拦截拼接处理
 * @Date: 13:33 2018/4/24
 * @Modify by:
 */
@Component
public class BaseTenantHandler implements TenantHandler {

    public HttpServletRequest getRequest(){
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        return request;
    }

    public Expression getTenantId() {

       Long tenantId = SaaSContextHolder.currentTenantId();
        //获取tenantId
        return new LongValue(tenantId);
    }

    public String getTenantIdColumn() {
        return "tenant_id";
    }

    public boolean doTableFilter(String tableName) {
        /**
         * 这里可以判断是否过滤表
         */
//        if (Device.TABLE_NAME.equals(tableName)) {//device 表 需要拦截 添加 tenant_id
//            return false;
//        }
//        if (UserDevice.TABLE_NAME.equals(tableName)) {//user_device 表 需要拦截 添加 tenant_id
//            return false;
//        }
        return true;
    }

}
