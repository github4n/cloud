package com.iot.building.ifttt.entity;

/**
 * @Author: chq
 * @Descrpiton:
 * @Date: 10:22 2018/5/12
 * @Modify by:
 */
public class AreaTime {
	/**
	 * 纬度
	 */
	private Float latitude;
	/**
	 * 经度
	 */
	private Float longitude;
	/**
	 * 时区	
	 */
	private Float timeZone;
	/**
	 * 年
	 */
	private Integer year;
	/**
	 * 月
	 */
	private Integer month;
	/**
	 * 天
	 */
	private Integer day;
	/**
	 * 小时
	 */
	private Integer hour = 0;
	/**
	 * 分钟
	 */
	private Integer minute = 0;
	/**
	 * 秒
	 */
	private Integer second = 0;

	public Float getLatitude() {
		return latitude;
	}

	public void setLatitude(Float latitude) {
		this.latitude = latitude;
	}

	public Float getLongitude() {
		return longitude;
	}

	public void setLongitude(Float longitude) {
		this.longitude = longitude;
	}

	public Float getTimeZone() {
		return timeZone;
	}

	public void setTimeZone(Float timeZone) {
		this.timeZone = timeZone;
	}

	public Integer getYear() {
		return year;
	}

	public void setYear(Integer year) {
		this.year = year;
	}

	public Integer getMonth() {
		return month;
	}

	public void setMonth(Integer month) {
		this.month = month;
	}

	public Integer getDay() {
		return day;
	}

	public void setDay(Integer day) {
		this.day = day;
	}

	public Integer getHour() {
		return hour;
	}

	public void setHour(Integer hour) {
		this.hour = hour;
	}

	public Integer getMinute() {
		return minute;
	}

	public void setMinute(Integer minute) {
		this.minute = minute;
	}

	public Integer getSecond() {
		return second;
	}

	public void setSecond(Integer second) {
		this.second = second;
	}
	
		
	

}
