package com.iot.core;

import com.iot.common.beans.UserTenantVO;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;

import java.sql.Connection;
import java.util.Properties;

@Intercepts({@Signature(type = StatementHandler.class, method = "prepare", args = {Connection.class})})
public class TenantIntercept implements Interceptor {

    public static final ThreadLocal<UserTenantVO> LOCAL_TENANT = new ThreadLocal<UserTenantVO>();

    /**
     * 开始使用系统逻辑tenant过滤
     *
     * @param userId
     * @param tenantId
     */
    public static void userSysTenantFilter(String userId, String tenantId) {
        UserTenantVO vo = new UserTenantVO();
        vo.setTenantId(tenantId);
        vo.setUserId(userId);
        LOCAL_TENANT.set(vo);
    }

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        /*if (LOCAL_TENANT.get() == null) {
            return invocation.proceed();
        }
        if (invocation.getTarget() instanceof StatementHandler) {
            StatementHandler statementHandler = (StatementHandler) invocation.getTarget();
            MetaObject metaStatementHandler = SystemMetaObject.forObject(statementHandler);
            // 分离代理对象链(由于目标类可能被多个拦截器拦截，从而形成多次代理，通过下面的两次循环  
            // 可以分离出最原始的的目标类)  
            while (metaStatementHandler.hasGetter("h")) {
                Object object = metaStatementHandler.getValue("h");
                metaStatementHandler = SystemMetaObject.forObject(object);
            }
            // 分离最后一个代理对象的目标类  
            while (metaStatementHandler.hasGetter("target")) {
                Object object = metaStatementHandler.getValue("target");
                metaStatementHandler = SystemMetaObject.forObject(object);
            }
            //分页信息if (localPage.get() != null) {  
            BoundSql boundSql = (BoundSql) metaStatementHandler.getValue("delegate.boundSql");
            // 分页参数作为参数对象parameterObject的一个属性  
            String sql = boundSql.getSql();
            // 重写sql  
            String newSql = buildTenantIdSql(sql);
            //重写sql  
            metaStatementHandler.setValue("delegate.boundSql.sql", newSql);
            // 将执行权交给下一个拦截器  
            return invocation.proceed();
        } else if (invocation.getTarget() instanceof ResultSetHandler) {
            return invocation.proceed();
        }*/
        return null;
    }

    /**
     * 修改原SQL为区分tenantId的sql
     *
     * @param
     * @return
     */
    /*private String buildTenantIdSql(String sql) {
        StringBuilder newSql = new StringBuilder(200);
        newSql.append(sql);
        if (!sql.contains("where")) {
            newSql.append(" where ");
        }
        newSql.append(" and tenant_id= " + LOCAL_TENANT.get().getTenantId());
        return newSql.toString();
    }*/

    @Override
    public Object plugin(Object target) {
        if (target instanceof StatementHandler) {
            return Plugin.wrap(target, this);
        } else {
            return target;
        }
    }

    @Override
    public void setProperties(Properties arg0) {

    }

}
