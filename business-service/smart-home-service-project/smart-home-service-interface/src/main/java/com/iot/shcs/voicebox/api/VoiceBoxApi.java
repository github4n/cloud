package com.iot.shcs.voicebox.api;

import com.iot.shcs.voicebox.vo.SetDevAttrDTO;
import com.iot.shcs.voicebox.vo.VoiceBoxMqttMsg;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @Descrpiton:
 * @Author: yuChangXing
 * @Date: 2018/10/8 13:51
 * @Modify by:
 */

@Api("音箱接口")
@FeignClient(value = "smart-home-service")
@RequestMapping("/voicebox")
public interface VoiceBoxApi {


    /**
     *  执行场景请求
     *
     * @param voiceBoxMqttMsg   mqtt消息内容
     */
    @ApiOperation("执行场景请求")
    @RequestMapping(value = "/excSceneReq", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public void excSceneReq(@RequestBody VoiceBoxMqttMsg voiceBoxMqttMsg);

    /**
     *  布置安防请求
     *
     * @param voiceBoxMqttMsg   mqtt消息内容
     */
    @ApiOperation("布置安防请求")
    @RequestMapping(value = "/setArmModeReq", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public void setArmModeReq(@RequestBody VoiceBoxMqttMsg voiceBoxMqttMsg);

    /**
     *  获取安防状态请求
     *
     * @param voiceBoxMqttMsg   mqtt消息内容
     */
    @ApiOperation("获取安防状态请求")
    @RequestMapping(value = "/getStatusReq", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public void getStatusReq(@RequestBody VoiceBoxMqttMsg voiceBoxMqttMsg);

    /**
     *  设备控制(设置设备属性)
     *
     */
    @ApiOperation("设备控制(设置设备属性)")
    @RequestMapping(value = "/setDevAttrReq", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public void setDevAttrReq(@RequestBody SetDevAttrDTO setDevAttrDTO);
}
