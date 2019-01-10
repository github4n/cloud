package com.iot.device.mapper.sql;

import com.iot.device.model.DailyElectricityStatistics;
import com.iot.device.model.MinElectricityStatistics;
import com.iot.device.model.MonthlyElectricityStatistics;
import com.iot.device.model.WeeklyElectricityStatistics;
import com.iot.device.vo.req.EnergyReq;

public class ElectricityStatisticsSqlProvider {

	public String selectDailyElectricityByCondition(DailyElectricityStatistics dailyElectricityStatistics) {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT * " +
				" FROM "+
				DailyElectricityStatistics.TABLE_NAME +
				" WHERE " +
//				" org_id = #{orgId} " +
				" tenant_id = #{tenantId} " +
				" and device_id = #{deviceId} " +
				" and day = #{day}");
				if(dailyElectricityStatistics.getUserId() != null) {
					sql.append(" and user_id = #{userId}");
				}
		return sql.toString();
	}

	public String selectWeeklyElectricityByCondition(
			WeeklyElectricityStatistics weeklyElectricityStatistics) {

		StringBuilder sql = new StringBuilder();
		sql.append("SELECT * " +
				" FROM "+
				WeeklyElectricityStatistics.TABLE_NAME +
				" WHERE " +
//				" org_id = #{orgId} " +
				" tenant_id = #{tenantId} " +
				" and device_id = #{deviceId} " +
				" and week = #{week}" +
				" and year = #{year}");
				if(weeklyElectricityStatistics.getUserId() != null) {
					sql.append(" and user_id = #{userId}");
				}
		return sql.toString();


	}

	public String selectMonthlyElectricityByCondition(
			MonthlyElectricityStatistics monthlyElectricityStatistics) {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT * " +
				" FROM "+
				MonthlyElectricityStatistics.TABLE_NAME +
				" WHERE " +
//				" org_id = #{orgId} " +
				" tenant_id = #{tenantId} " +
				" and device_id = #{deviceId} " +
				" and month = #{month}" +
				" and year = #{year}");
				if(monthlyElectricityStatistics.getUserId() != null) {
					sql.append(" and user_id = #{userId}");
				}
		return sql.toString();
	}

	public String selectElectricityRspByCondition() {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT " +
				" DATE_FORMAT(`localtime`,'%Y-%m-%d') as day," +
				" SUM(electric_value) as electricValue," +
				" tenant_id as tenantId," +
				" org_id as orgId," +
				" device_id as deviceId," +
				" user_id as userId" +
				" FROM "+
				MinElectricityStatistics.TABLE_NAME +
				" WHERE " +
				" TO_DAYS(`localtime`) = TO_DAYS(NOW())" +
				" GROUP BY device_id, " +
				" org_id, " +
				" tenant_id, " +
				" user_id, " +
				" day");

		return sql.toString();
	}

	public String selectElectricityRspByDeviceAndUser(EnergyReq energyReq) {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT " +
				"DATE_FORMAT(`localtime`, '%Y-%m-%d') as day," +
				" SUM(electric_value) AS electricValue," +
				" tenant_id AS tenantId," +
				" org_id AS orgId," +
				" device_id AS deviceId," +
				" user_id AS userId" +
				" FROM " +
				MinElectricityStatistics.TABLE_NAME +
				" WHERE 1=1 " +
				" AND TO_DAYS(`localtime`) = TO_DAYS(NOW())"
		);
		if (energyReq.getUserId() != null) {
			sql.append(" AND user_id = #{userId}");
		}
		if (energyReq.getDevId() != null) {
			sql.append(" AND device_id = #{devId}");
		}
		if (energyReq.getTenantId() != null) {
			sql.append(" AND tenant_id = #{tenantId}");
		}
		return sql.toString();
	}

	public String selectDailyElectricityRsp(EnergyReq energyReq) {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT DISTINCT day as time, electric_value as value" +
				" FROM " +
				DailyElectricityStatistics.TABLE_NAME +
				" WHERE 1=1 "
		);
		if (energyReq.getUserId() != null) {
			sql.append(" AND user_id = #{userId}");
		}
		if (energyReq.getDevId() != null) {
			sql.append(" AND device_id = #{devId}");
		}
		if (energyReq.getTenantId() != null) {
			sql.append(" AND tenant_id = #{tenantId}");
		}
		sql.append(" ORDER BY day ASC LIMIT #{start},#{end} ");
		return sql.toString();
	}

	public String selectWeeklyElectricityRsp(EnergyReq energyReq) {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT DISTINCT CONCAT(year,'.',week) as time, electric_value as value" +
				" FROM " +
				WeeklyElectricityStatistics.TABLE_NAME +
				" WHERE 1=1 "
		);
		if (energyReq.getUserId() != null) {
			sql.append(" AND user_id = #{userId}");
		}
		if (energyReq.getDevId() != null) {
			sql.append(" AND device_id = #{devId}");
		}
		if (energyReq.getTenantId() != null) {
			sql.append(" AND tenant_id = #{tenantId}");
		}
		sql.append(" ORDER BY `year`,`week` ASC LIMIT #{start},#{end} ");
		return sql.toString();
	}

	public String selectMonthlyElectricityRsp(EnergyReq energyReq) {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT DISTINCT CONCAT(year,'-',month) as time, electric_value as value" +
				" FROM " +
				MonthlyElectricityStatistics.TABLE_NAME +
				" WHERE 1=1 "
		);
		if (energyReq.getUserId() != null) {
			sql.append(" AND user_id = #{userId}");
		}
		if (energyReq.getDevId() != null) {
			sql.append(" AND device_id = #{devId}");
		}
		if (energyReq.getTenantId() != null) {
			sql.append(" AND tenant_id = #{tenantId}");
		}
		sql.append(" ORDER BY `year`,`month` ASC LIMIT #{start},#{end} ");
		return sql.toString();
	}
}
