package com.iot.building.common.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.iot.building.common.service.ShortcutService;
import com.iot.building.shortcut.api.ShortcutApi;
import com.iot.building.shortcut.vo.ShortcutVo;

@RestController
public class ShortcutController implements  ShortcutApi{
	
	@Autowired
	private ShortcutService shortcutService;

	@Override
	public List<ShortcutVo> getShortcutList(@RequestBody ShortcutVo vo) {
		return shortcutService.getShortcutList(vo);
	}

	@Override
	public void excute(@RequestBody ShortcutVo shortcutVo) {
		shortcutService.excute(shortcutVo);
	}

	@Override
	public void scheduleExcute(@RequestBody ShortcutVo vo) {
		shortcutService.scheduleExcute(vo);
	}

}
