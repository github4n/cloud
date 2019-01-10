package com.iot.shcs.security.api;

import com.iot.common.exception.BusinessException;
import com.iot.shcs.device.vo.ControlReq;
import com.iot.shcs.security.vo.rsp.IntentResultResp;
import com.iot.shcs.security.vo.rsp.SecurityResp;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Created with IntelliJ IDEA.
 * User: 余昌兴
 * Date: 2018/5/22
 * Time: 16:53
 * To change this template use File | Settings | File Templates.
 */

@Api("安防接口")
@FeignClient(value = "smart-home-service")
@RequestMapping("/security")
public interface SecurityApi {


    /**
     * 根据 spaceId 获取 SecurityResp
     *
     * @param spaceId
     * @return
     */
    @ApiOperation("根据 spaceId 获取 SecurityResp")
    @RequestMapping(value = "/getBySpaceId", method = RequestMethod.GET)
    public SecurityResp getBySpaceId(@RequestParam("spaceId") Long spaceId);
    
    @RequestMapping(value = "/arm", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public IntentResultResp arm(@RequestBody ControlReq controlVo) throws BusinessException;
    
    @RequestMapping(value = "/panic", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public IntentResultResp panic(@RequestBody ControlReq controlVo) throws BusinessException;
    
    @RequestMapping(value = "/statusQuery", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public IntentResultResp statusQuery(@RequestBody ControlReq controlVo) throws BusinessException;

    @ApiOperation("数据迁移")
    @RequestMapping(value = "/moveData",method = RequestMethod.GET)
    public String doMoveData(@RequestParam("isMove") int doMove);
}
