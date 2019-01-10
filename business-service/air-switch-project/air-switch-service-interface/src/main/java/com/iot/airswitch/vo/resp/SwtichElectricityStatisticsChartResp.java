package com.iot.airswitch.vo.resp;

import java.util.List;

/**
 * @Author: Xieby
 * @Date: 2018/11/7
 * @Description: *
 */
public class SwtichElectricityStatisticsChartResp {

    private List<Integer> xList;

    private List<Long> yList;

    public List<Integer> getxList() {
        return xList;
    }

    public void setxList(List<Integer> xList) {
        this.xList = xList;
    }

    public List<Long> getyList() {
        return yList;
    }

    public void setyList(List<Long> yList) {
        this.yList = yList;
    }
}
