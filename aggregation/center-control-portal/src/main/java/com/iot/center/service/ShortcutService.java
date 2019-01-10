package com.iot.center.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.iot.building.shortcut.api.ShortcutApi;
import com.iot.building.shortcut.vo.ShortcutVo;

@Service
public class ShortcutService {

	private static final Logger logger = LoggerFactory.getLogger(ShortcutService.class);

	@Autowired
	private ShortcutApi shortcutApi;

	public List<ShortcutVo> findShortcutList(ShortcutVo vo) {
		return shortcutApi.getShortcutList(vo);
	}
	
	public void excute(Long id,String type,Integer flag,Long tenantId,Long locationId) {
		ShortcutVo shortcutVo =new ShortcutVo();
		shortcutVo.setId(id);
		shortcutVo.setType(type);
		shortcutVo.setFlag(flag);
		shortcutVo.setTenantId(tenantId);
		shortcutVo.setLocationId(locationId);
		shortcutApi.excute(shortcutVo);
	}
}