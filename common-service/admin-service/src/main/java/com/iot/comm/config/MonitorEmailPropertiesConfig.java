
package com.iot.comm.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lucky
 * @date 2018/8/8
 */
@ConditionalOnExpression("!'${emails}'.isEmpty()")
public class MonitorEmailPropertiesConfig {
    private Boolean enabled;
    private List<String> emails = new ArrayList<>();

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public List<String> getEmails() {
        return emails;
    }

    public void setEmails(List<String> emails) {
        this.emails = emails;
    }
}
