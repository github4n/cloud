
package com.iot.shcs.common.config;

import lombok.Data;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lucky
 * @date 2018/8/8
 */
@Data
@ConditionalOnExpression("!'${emails}'.isEmpty()")
public class MonitorEmailPropertiesConfig {
    private Boolean enabled;
    private List<String> emails = new ArrayList<>();

}
