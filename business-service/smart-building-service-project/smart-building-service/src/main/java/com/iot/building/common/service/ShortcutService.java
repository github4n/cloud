package com.iot.building.common.service;

import java.util.List;

import com.iot.building.scene.vo.resp.LocationSceneResp;
import com.iot.building.shortcut.vo.ShortcutVo;

public interface ShortcutService {

	public List<ShortcutVo> getShortcutList(ShortcutVo vo);
	
	public void excute(ShortcutVo vo);
	
	public void scheduleExcute(ShortcutVo vo);
	
	List<LocationSceneResp> getLocalListSecneByBuildOrFloor(Long orgId, Long spaceId,Long tenantId,Long locationId);
	
}
