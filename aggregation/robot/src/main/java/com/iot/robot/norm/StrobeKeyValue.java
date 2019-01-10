package com.iot.robot.norm;

public class StrobeKeyValue extends KeyValue<String> {

    private String key = STROBE;
    private String fixedValue;
    private String deltaValue;

    @Override
    public String getFixedValue() {
        return fixedValue;
    }

    @Override
    public void setFixedValue(String fixedValue) {
        if (!(!"on".equals(fixedValue) && !"off".equals(fixedValue))) {
            throw new IllegalStateException("illegal fixedValue, only support on or off");
        }
        this.fixedValue = fixedValue;
    }

    @Override
    public String getDeltaValue() {
        return null;
    }

    @Override
    public void setDeltaValue(String deltaValue) {
        
    }

    @Override
    public String getKey() {
        return key;
    }

}
