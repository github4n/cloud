package com.iot.shcs.security.service;

import com.iot.mqttsdk.common.MqttMsg;
import com.iot.shcs.security.vo.SecurityRule;
import org.apache.ibatis.annotations.*;
import java.util.List;

public interface SecurityRuleService {

    int deleteByPrimaryKey(@Param("tenantId") Long tenantId,@Param("securityId") Long securityId, @Param("type")String type);

    SecurityRule selectBySecurityIdAndType(@Param("tenantId") Long tenantId,@Param("securityId") Long securityId,@Param("type") String type);

    Long saveSecurityRule(SecurityRule rule);

    Long saveSecurityRuleForMoveData(SecurityRule rule,boolean isNoData);

    SecurityRule getRuleBean(MqttMsg message);

    List<SecurityRule> list(SecurityRule securityRule);

}
