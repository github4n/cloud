package com.iot.building.shortcut.api;

import java.util.List;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.iot.building.reservation.api.callback.ReservationApiFallbackFactory;
import com.iot.building.shortcut.vo.ShortcutVo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * @Author: Xieby
 * @Date: 2018/9/3
 * @Description: *
 */
@Api("会议预约接口")
@FeignClient(value = "building-control-service")
@RequestMapping("/shortcut")
public interface ShortcutApi {

   
    @ApiOperation("快捷列表")
    @RequestMapping(value = "/getShortcutList", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public List<ShortcutVo> getShortcutList(@RequestBody ShortcutVo vo);
	
    @ApiOperation("执行快捷")
    @RequestMapping(value = "/excute",method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public void excute(@RequestBody ShortcutVo shortcutVo);
    
    @ApiOperation("schedule执行")
    @RequestMapping(value = "/scheduleExcute",method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public void scheduleExcute(@RequestBody ShortcutVo vo);
    
}
