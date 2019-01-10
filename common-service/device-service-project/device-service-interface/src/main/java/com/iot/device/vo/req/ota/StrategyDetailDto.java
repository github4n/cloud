package com.iot.device.vo.req.ota;

import java.util.List;
import java.util.Map;

public class StrategyDetailDto {

    private Integer upgradeScope;

    private Map<Integer,List<StrategyDetailReq>> strategyDetailReqMap;

    private List<StrategyDetailReq> strategyDetailReqList;

    public Integer getUpgradeScope() {
        return upgradeScope;
    }

    public void setUpgradeScope(Integer upgradeScope) {
        this.upgradeScope = upgradeScope;
    }

    public List<StrategyDetailReq> getStrategyDetailReqList() {
        return strategyDetailReqList;
    }

    public void setStrategyDetailReqList(List<StrategyDetailReq> strategyDetailReqList) {
        this.strategyDetailReqList = strategyDetailReqList;
    }

    public Map<Integer, List<StrategyDetailReq>> getStrategyDetailReqMap() {
        return strategyDetailReqMap;
    }

    public void setStrategyDetailReqMap(Map<Integer, List<StrategyDetailReq>> strategyDetailReqMap) {
        this.strategyDetailReqMap = strategyDetailReqMap;
    }
}