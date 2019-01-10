package com.iot.portal.report.vo.resp;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 项目名称：立达信IOT云平台
 * 模块名称：聚合层
 * 功能描述：基础数据
 * 创建人： maochengyuan
 * 创建时间：2019/1/7 11:50
 * 修改人： maochengyuan
 * 修改时间：2019/1/7 11:50
 * 修改描述：
 */
@ApiModel(value = "报表基础对象",description = "报表基础对象")
public class BaseBean {

    @ApiModelProperty(value="日期",name="dataDate")
    private String dataDate;

    @ApiModelProperty(value="数量",name="amount")
    private Long amount = 0L;

    public BaseBean() {

    }

    public BaseBean(String dataDate, Long amount) {
        this.dataDate = dataDate;
        this.amount = amount;
    }

    public String getDataDate() {
        return dataDate;
    }

    public void setDataDate(String dataDate) {
        this.dataDate = dataDate;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

}
