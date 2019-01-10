package com.iot.center.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.iot.center.bean.PeopleSteam;
import com.iot.center.cache.PeopleSteamCache;
import com.iot.center.helper.BusinessExceptionEnum;
import com.iot.common.beans.CommonResponse;
import com.iot.common.exception.BusinessException;
import com.iot.common.exception.ResultMsg;

@Controller()
@RequestMapping("/peopleSteam")
public class PeopleSteamController {
	@ResponseBody
	@RequestMapping("/uploadSteam")
	public CommonResponse<ResultMsg> uploadSteam(String deviceId, long datetime, Integer comeIn, Integer comeOut) {
		PeopleSteam peopleSteam = new PeopleSteam();
		try {
			PeopleSteamCache.get(deviceId);
		} catch (Exception e) {
			e.printStackTrace();
			throw new BusinessException(BusinessExceptionEnum.COMMOMN_EXCEPTION);
		}
		peopleSteam.setDateTime(datetime);
		peopleSteam.setNumber(peopleSteam.getNumber() + comeIn - comeOut);
		PeopleSteamCache.put(deviceId, peopleSteam);
		return new CommonResponse<ResultMsg>(ResultMsg.SUCCESS);
	}
}
