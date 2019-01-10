package com.iot.building.ifttt.calculatro;

import com.iot.building.space.service.IBuildingSpaceService;
import com.iot.common.helper.ApplicationContextHelper;
import org.springframework.stereotype.Component;
import java.util.Map;

@Component
public class SpaceActuatorExecutor implements IActuatorExecutor {

    @Override
    public void execute(Map<String, Object> params) {
        IBuildingSpaceService spaceService = ApplicationContextHelper.getBean(IBuildingSpaceService.class);
        Long spaceId = Long.valueOf(params.get("spaceId")+"");
        Long tenantId = Long.valueOf(params.get("tenantId")+"");
        Long orgId = Long.valueOf(params.get("orgId")+"");
        Long templateId = null;
        if (params.get("templateId") != null) {
            templateId = (Long) params.get("templateId");
        }
        params.remove("spaceId");
        params.remove("templateId");
        try {
            spaceService.publicGroupOrSceneControl(spaceId, params, templateId,orgId,tenantId);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
