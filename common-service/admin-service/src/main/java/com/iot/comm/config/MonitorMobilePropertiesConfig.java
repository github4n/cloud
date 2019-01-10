
package com.iot.comm.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lucky
 * @date 2018/8/8
 */
@ConditionalOnExpression("!'${mobiles}'.isEmpty()")
public class MonitorMobilePropertiesConfig {
    private Boolean enabled;
    private List<String> mobiles = new ArrayList<>();

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public List<String> getMobiles() {
        return mobiles;
    }

    public void setMobiles(List<String> mobiles) {
        this.mobiles = mobiles;
    }
}
