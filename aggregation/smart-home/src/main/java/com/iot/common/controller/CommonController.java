package com.iot.common.controller;

import com.iot.common.annotation.Action;
import com.iot.common.annotation.LoginRequired;
import com.iot.common.beans.CommonResponse;
import com.iot.common.exception.ResultMsg;
import com.iot.common.util.CalendarUtil;
import com.iot.common.util.StringUtil;
import com.iot.common.vo.ServerConfig;
import com.iot.common.vo.SynTimeResp;
import com.iot.file.api.FileApi;
import com.iot.report.api.DeviceActivateApi;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Api(description = "通用接口")
@RestController
@RequestMapping("/commonController")
public class CommonController {
    /**日志*/
    private static final Logger loger = LoggerFactory.getLogger(CommonController.class);

    /**
     * 同步服务器时间的 时间格式
     */
    private static final String SYN_TIME_DATE_PATTERN = "yyyy-MM-dd HH:mm:ss SSS";

    @Autowired
    private FileApi fileApi;

    @Autowired
    private DeviceActivateApi deviceActivateApi;

    /**
     * NTP服务器地址
     */
    @Value("${serverUrlconfig.ntpServerUrl}")
    private String ntpServerUrl;

    /**
     * 设备MQTT服务器地址
     */
    @Value("${serverUrlconfig.devMqttServerUrl}")
    private String devMqttServerUrl;

    @Value("${serverUrlconfig.newDevMqttServerUrl}")
    private String newDevMqttServerUrl;
    /**
     * 用户MQTT服务器地址
     */
    @Value("${serverUrlconfig.usrMqttServerUrl}")
    private String usrMqttServerUrl;

    @Value("${serverUrlconfig.newUsrMqttServerUrl}")
    private String newUsrMqttServerUrl;
    /**
     * 云服务请求头
     */
    @Value("${serverUrlconfig.header}")
    private String httpHeader;

    /**
     * MQTT心跳周期
     */
    @Value("${serverUrlconfig.heartbeat}")
    private byte heartbeat;

    /**
     * 描述：获取服务器配置（暂时读取配置文件，后期可能会改为依据ClientId按照特定算法分配MQTT服务器URL）
     *
     * @return com.lds.iot.common.beans.newBeans.CommonResponse
     * @author mao2080@sina.com
     * @created 2018/5/11 9:45
     */
    @LoginRequired(value = Action.Skip)
    @ApiOperation(value = "获取服务器配置", notes = "请求NTP服务器URL,MQTT服务器URL")
    @ApiImplicitParams({@ApiImplicitParam(name = "clientId", value = "MQTT客户端ID，设备传设备UUID，用户传app-UUID或web-UUID", required = true, paramType = "query", dataType = "String")})
    @RequestMapping(value = "/getServerUrlConfig", method = RequestMethod.GET)
    public CommonResponse getServerUrlConfig(@RequestParam("clientId") String clientId, @RequestParam(value = "version", required = false) String version) {
        ServerConfig config = new ServerConfig(new Date().getTime());
        //如果版本传递为空，此时使用旧的地址,否则使用新的地址
        if(StringUtil.isEmpty(version)) {
            if (StringUtil.isEmpty(clientId) || !clientId.contains("-")) {
                config.setMqttServerUrl(this.devMqttServerUrl); //设备配置
            } else {
                config.setMqttServerUrl(this.usrMqttServerUrl); //用户配置
            }
        }else {
            if (StringUtil.isEmpty(clientId) || !clientId.contains("-")) {
                config.setMqttServerUrl(this.newDevMqttServerUrl); //设备配置
            } else {
                config.setMqttServerUrl(this.newUsrMqttServerUrl); //用户配置
            }
        }
        config.setHeartbeat(this.heartbeat);
        config.setNtpServerUrl(this.ntpServerUrl);
        config.setResTimestamp(new Date().getTime());
        loger.info("currentTime: "+ CalendarUtil.format(new Date(),CalendarUtil.YYYYMMDDHHMMSS));
        config.setHttpHeader(httpHeader);
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String ip = getIpAddr(request);
        config.setIp(ip);
        if(StringUtil.isNotBlank(ip) && StringUtil.isNotBlank(clientId) && !clientId.contains("-")){
            deviceActivateApi.cacheDevIp(clientId,ip);
        }
        return ResultMsg.SUCCESS.info(config);
    }

    public String getIpAddr(HttpServletRequest request){
        try{
            String ip = request.getHeader("X-Real-IP");
            if (!StringUtils.isBlank(ip) && !"unknown".equalsIgnoreCase(ip)) {
                return ip;
            }
            ip = request.getHeader("X-Forwarded-For");
            if (!StringUtils.isBlank(ip) && !"unknown".equalsIgnoreCase(ip)) {
                // 多次反向代理后会有多个IP值，第一个为真实IP。
                int index = ip.indexOf(',');
                if (index != -1) {
                    return ip.substring(0, index);
                } else {
                    return ip;
                }
            } else {
                return request.getRemoteAddr();
            }
        }catch (Exception e){
            loger.error("",e);
            return null;
        }
    }

    /**
     * 请求服务器获取对应时区的夏令时
     *
     * @param area
     * @param t1
     * @return
     */
    @LoginRequired(value = Action.Skip)
    @ApiOperation(value = "同步服务器时间", notes = "请求服务器获取对应时区的夏令时")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "area", value = "设备的地区信息（时区）", required = true, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "t1", value = "设备端记录请求发出的时间", required = true, paramType = "query", dataType = "String")
    })
    @RequestMapping(value = "/synTime", method = RequestMethod.GET)
    public CommonResponse synTime(@RequestParam("area") String area, @RequestParam("t1") String t1) {
        ZoneId zoneId = TimeZone.getTimeZone(area).toZoneId();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern(SYN_TIME_DATE_PATTERN);

        // 服务器接收到的时间
        LocalDateTime timeReceive = LocalDateTime.now(zoneId);
        String t2 = timeReceive.format(dtf);

        if (StringUtil.isBlank(area)) {
            return ResultMsg.HTTP_PARAM_ERROR.info("param 'area' is null.");
        }

        if (StringUtil.isBlank(t1)) {
            return ResultMsg.HTTP_PARAM_ERROR.info("param 't1' is null.");
        }

        SynTimeResp synTimeResp = new SynTimeResp();
        synTimeResp.setT1(t1);
        synTimeResp.setT2(t2);
        synTimeResp.setTimestamp(System.currentTimeMillis());

        // 服务器发出的时间
        LocalDateTime timeSend = LocalDateTime.now(zoneId);
        String t3 = timeSend.format(dtf);
        synTimeResp.setT3(t3);

        return ResultMsg.SUCCESS.info(synTimeResp);
    }

    @LoginRequired(Action.Normal)
    @ApiOperation("获取资源文件")
    @RequestMapping(value = "/resourceLink", method = RequestMethod.POST)
    public CommonResponse resourceLink(@RequestBody List<String> resourceIds){
        Map map = fileApi.getGetUrl(resourceIds);
        Map result = new HashMap();
        resourceIds.forEach(s->{
            result.put(s,map.get(s));
        });
        return CommonResponse.success(result);
    }

}
