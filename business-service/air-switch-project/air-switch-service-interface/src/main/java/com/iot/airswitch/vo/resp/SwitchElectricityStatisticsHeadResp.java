package com.iot.airswitch.vo.resp;

/**
 * @Author: Xieby
 * @Date: 2018/11/7
 * @Description: *
 */
public class SwitchElectricityStatisticsHeadResp {

    /**
     * 当天电量统计
     */
    private Long dayStatistics;

    /**
     * 当天电量与昨天电量比率
     */
    private Double dayRate;

    /**
     * 当月电量统计
     */
    private Long monthStatistics;

    /**
     * 当月电量与上个月电量比较比率
     */
    private Double monthRate;

    /**
     * 当年电量统计
     */
    private Long yearStatistics;

    /**
     * 当年电量与去年电量比较比率
     */
    private Double yearRate;

    public SwitchElectricityStatisticsHeadResp() {
        this.dayStatistics = 0L;
        this.dayRate = 0D;
        this.monthStatistics = 0L;
        this.monthRate = 0D;
        this.yearStatistics = 0L;
        this.yearRate = 0D;
    }

    public Long getDayStatistics() {
        return dayStatistics;
    }

    public void setDayStatistics(Long dayStatistics) {
        this.dayStatistics = dayStatistics;
    }

    public Double getDayRate() {
        return dayRate;
    }

    public void setDayRate(Double dayRate) {
        this.dayRate = dayRate;
    }

    public Long getMonthStatistics() {
        return monthStatistics;
    }

    public void setMonthStatistics(Long monthStatistics) {
        this.monthStatistics = monthStatistics;
    }

    public Double getMonthRate() {
        return monthRate;
    }

    public void setMonthRate(Double monthRate) {
        this.monthRate = monthRate;
    }

    public Long getYearStatistics() {
        return yearStatistics;
    }

    public void setYearStatistics(Long yearStatistics) {
        this.yearStatistics = yearStatistics;
    }

    public Double getYearRate() {
        return yearRate;
    }

    public void setYearRate(Double yearRate) {
        this.yearRate = yearRate;
    }
}
