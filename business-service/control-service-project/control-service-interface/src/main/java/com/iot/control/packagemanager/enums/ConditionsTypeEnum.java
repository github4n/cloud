package com.iot.control.packagemanager.enums;

public enum ConditionsTypeEnum {
    AND("and"),
    OR("or");

    private String condition;

    ConditionsTypeEnum(String condition) {
        this.condition = condition;
    }

    public String getCondition() {
        return condition;
    }
}
