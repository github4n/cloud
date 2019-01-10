package com.iot.device.mapper.sql;

import com.iot.device.model.DailyElectricityStatistics;
import com.iot.device.model.DailyRuntime;
import com.iot.device.model.MinRuntime;
import com.iot.device.model.MonthlyRuntime;
import com.iot.device.model.WeeklyRuntime;
import com.iot.device.vo.req.RuntimeReq2Runtime;

public class RuntimeSqlProvider {

	public String selectDailyElectricityByCondition(DailyElectricityStatistics dailyElectricityStatistics) {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT * " +
				" FROM "+
				DailyElectricityStatistics.TABLE_NAME +
				" WHERE 1 = 1 " +
				" and device_id = #{deviceId} " +
				" and day = #{day}");
		if (dailyElectricityStatistics.getUserId() != null) {
			sql.append(" and user_id = #{userId}");
		}
		if (dailyElectricityStatistics.getTenantId() != null) {
			sql.append(" and tenant_id = #{tenantId} ");
		}

		return sql.toString();
	}
	
	public String selectWeeklyRuntimeByCondition(WeeklyRuntime weeklyRuntime) {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT * " +
				" FROM "+
				WeeklyRuntime.TABLE_NAME +
				" WHERE 1 = 1 " +
//				" org_id = #{orgId} " +
				" and device_id = #{deviceId} " +
				" and week = #{week}" +
				" and year = #{year}");
		if (weeklyRuntime.getUserId() != null) {
			sql.append(" and user_id = #{userId}");
		}
		if (weeklyRuntime.getTenantId() != null) {
			sql.append(" and tenant_id = #{tenantId}");
		}

		return sql.toString();
		
		
	}
	
	public String selectMonthlyRuntimeByCondition(MonthlyRuntime monthlyRuntime) {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT * " +
				" FROM "+
				MonthlyRuntime.TABLE_NAME +
				" WHERE 1 = 1 " +
//				" org_id = #{orgId} " +
				" and device_id = #{deviceId} " +
				" and month = #{month}" +
				" and year = #{year}");
		if (monthlyRuntime.getUserId() != null) {
			sql.append(" and user_id = #{userId}");
		}
		if (monthlyRuntime.getTenantId() != null) {
			sql.append(" and tenant_id = #{tenantId}");
		}
		return sql.toString();
	}
	
	public String selectRuntimeRspByCondition() {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT " +
				" DATE_FORMAT(`localtime`,'%Y-%m-%d') as day," +
				" SUM(runtime) as runtime," + 
				" tenant_id as tenantId," + 
				" org_id as orgId," + 
				" device_id as deviceId," + 
				" user_id as userId" + 
				" FROM "+
				MinRuntime.TABLE_NAME + 
				" WHERE " +
				" TO_DAYS(`localtime`) = TO_DAYS(NOW())" +
				" GROUP BY device_id, " + 
				" org_id, " + 
				" tenant_id, " + 
				" user_id, " + 
				" day");
				
		return sql.toString();
	}
	
	public String selectRuntimeRspByDeviceAndUser(RuntimeReq2Runtime runtimeReq) {
		StringBuilder sql = new StringBuilder();
        sql.append("SELECT " +
				"DATE_FORMAT(`localtime`, '%Y-%m-%d') as day," +
				" SUM(runtime) AS runtime," +
				" tenant_id AS tenantId," +
				" org_id AS orgId," +
				" device_id AS deviceId," +
				" user_id AS userId" +
				" FROM " +
				MinRuntime.TABLE_NAME +
				" WHERE 1=1 " +
				" AND TO_DAYS(`localtime`) = TO_DAYS(NOW())"
		);
		if (runtimeReq.getUserId() != null) {
			sql.append(" AND user_id = #{userId}");
		}
		if (runtimeReq.getDevId() != null) {
			sql.append(" AND device_id = #{devId}");
		}
		if (runtimeReq.getTenantId() != null) {
			sql.append(" AND tenant_id = #{tenantId}");
		}
		return sql.toString();
	}

	public String selectDailyRuntimeRsp(RuntimeReq2Runtime runtimeReq) {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT DISTINCT day as time, runtime as value" +
				" FROM " +
				DailyRuntime.TABLE_NAME +
				" WHERE 1=1 "
		);
		if (runtimeReq.getUserId() != null) {
			sql.append(" AND user_id = #{userId}");
		}
		if (runtimeReq.getDevId() != null) {
			sql.append(" AND device_id = #{devId}");
		}
		if (runtimeReq.getTenantId() != null) {
			sql.append(" AND tenant_id = #{tenantId}");
		}
		sql.append(" ORDER BY day ASC LIMIT #{start},#{end} ");

		return sql.toString();
	}

	public String selectWeeklyRuntimeRsp(RuntimeReq2Runtime runtimeReq) {
		StringBuilder sql = new StringBuilder();
        sql.append("SELECT CONCAT(year,'.',week) as time, runtime as value" +
				" FROM " +
				WeeklyRuntime.TABLE_NAME +
				" WHERE 1=1 "
		);
		if (runtimeReq.getUserId() != null) {
			sql.append(" AND user_id = #{userId}");
		}
		if (runtimeReq.getDevId() != null) {
			sql.append(" AND device_id = #{devId}");
		}
		if (runtimeReq.getTenantId() != null) {
			sql.append(" AND tenant_id = #{tenantId}");
		}
		sql.append(" ORDER BY `year`,`week` ASC LIMIT #{start},#{end} ");
		return sql.toString();
	}

	public String selectMonthlyRuntimeRsp(RuntimeReq2Runtime runtimeReq) {
		StringBuilder sql = new StringBuilder();
        sql.append("SELECT CONCAT(year,'-',month) as time, runtime as value" +
				" FROM " +
				MonthlyRuntime.TABLE_NAME +
				" WHERE 1 = 1 "
		);
		if (runtimeReq.getUserId() != null) {
			sql.append(" AND user_id = #{userId}");
		}
		if (runtimeReq.getDevId() != null) {
			sql.append(" AND device_id = #{devId}");
		}
		if (runtimeReq.getTenantId() != null) {
			sql.append(" AND tenant_id = #{tenantId}");
		}
		sql.append(" ORDER BY `year`,`month` ASC LIMIT #{start},#{end} ");
		return sql.toString();
	}
  
}
