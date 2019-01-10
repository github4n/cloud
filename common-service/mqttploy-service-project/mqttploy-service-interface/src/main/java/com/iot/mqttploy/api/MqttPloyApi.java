package com.iot.mqttploy.api;

import com.iot.mqttploy.entity.PloyInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;


import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.iot.common.exception.BusinessException;

import java.util.List;

@Api(tags = "MQTT策略服务接口")
@FeignClient(value="mqttploy-service")
@RequestMapping("/api/mqttploy/service")
public interface MqttPloyApi {

	/**
	 *
	 * 描述：新增策略
	 * @author mao2080@sina.com
	 * @created 2018年3月13日 下午5:09:07
	 * @since
	 * @param type 策略类型
	 * @param clientId clientId
	 * @param uuid 用户或设备UUID
	 * @param password 加密后的密码
	 * @throws BusinessException
	 */
	@ApiOperation(value = "新增策略" ,  notes="新增策略")
	@ApiImplicitParams({@ApiImplicitParam(name = "type", value = "类型（user表示用户，device表示设备）", required = true, paramType = "query", dataType = "String"),
						@ApiImplicitParam(name = "clientId", value = "用户或设备clientId", required = true, paramType = "query", dataType = "String"),
						@ApiImplicitParam(name = "uuid", value = "用户或设备UUID", required = true, paramType = "query", dataType = "String"),
						@ApiImplicitParam(name = "password", value = "加密后的密码", required = true, paramType = "query", dataType = "String") })
	@RequestMapping(value="/saveAcls",method=RequestMethod.POST)
	public int saveAcls(@RequestParam("type")String type, @RequestParam("clientId")String clientId, @RequestParam("uuid")String uuid, @RequestParam("password")String password) throws BusinessException;

	/**
	 *
	 * 描述：批量新增
	 * @author mao2080@sina.com
	 * @created 2018年3月13日 下午5:09:07
	 * @since
	 * @param ployInfos 策略
	 * @throws BusinessException
	 */
	@ApiOperation(value = "批量新增",  notes="批量新增", response=PloyInfo.class)
	@ApiImplicitParam(name = "ployInfos", value = "策略详细实体PloyInfo", required = true, dataType = "PloyInfo")
	@RequestMapping(value="/batchSaveAcls",method=RequestMethod.POST,consumes=MediaType.APPLICATION_JSON_VALUE)
	public int batchSaveAcls(@RequestBody List<PloyInfo> ployInfos) throws BusinessException;

	/**
	 *
	 * 描述：附加策略
	 * @author mao2080@sina.com
	 * @created 2018年3月13日 下午5:09:07
	 * @since
	 * @param userId 用户id
	 * @param deviceId 设备id
	 * @throws BusinessException
	 */
	@ApiOperation(value = "附加策略",  notes="附加策略")
	@ApiImplicitParams({@ApiImplicitParam(name = "userId", value = "用户id", required = true, paramType = "query", dataType = "String"),
						@ApiImplicitParam(name = "deviceId", value = "设备id", required = true, paramType = "query", dataType = "String")})
	@RequestMapping(value="/addAcls",method=RequestMethod.POST)
	public int addAcls(@RequestParam("userId") String userId, @RequestParam("deviceId") String deviceId) throws BusinessException;

	/**
	 *
	 * 描述：ToB附加策略
	 * @author mao2080@sina.com
	 * @created 2018年3月13日 下午5:09:07
	 * @since
	 * @param userId 用户id
	 * @param deviceId 设备id
	 * @throws BusinessException
	 */
	@ApiOperation(value = "ToB附加策略",  notes="ToB附加策略")
	@ApiImplicitParams({@ApiImplicitParam(name = "userId", value = "用户id", required = true, paramType = "query", dataType = "String"),
			@ApiImplicitParam(name = "deviceId", value = "设备id", required = true, paramType = "query", dataType = "String")})
	@RequestMapping(value="/addAclsToB",method=RequestMethod.POST)
	public int addAclsToB(@RequestParam("userId") String userId, @RequestParam("deviceId") String deviceId) throws BusinessException;


	/**
	 *
	 * 描述：分离策略
	 * @author mao2080@sina.com
	 * @created 2018年3月13日 下午5:09:07
	 * @since
	 * @param userId 用户
	 * @param deviceId 设备id
	 * @throws BusinessException
	 */
	@ApiOperation(value = "分离策略",  notes="分离策略")
	@ApiImplicitParams({@ApiImplicitParam(name = "userId", value = "用户id", required = true, paramType = "query", dataType = "String"),
						@ApiImplicitParam(name = "deviceId", value = "设备id", required = true, paramType = "query", dataType = "String")})
	@RequestMapping(value="/separationAcls",method=RequestMethod.POST)
	public int separationAcls(@RequestParam("userId")String userId, @RequestParam("deviceId")String deviceId) throws BusinessException;

	/**
	 *
	 * 描述：修改密码
	 * @author mao2080@sina.com
	 * @created 2018年3月13日 下午5:09:07
	 * @since
	 * @param type 类型（0表示web,1表示app）
	 * @param userId 用户ID
	 * @param password 加密后的密码
	 * @throws BusinessException
	 */
	@ApiOperation(value =  "修改密码",  notes="修改密码")
	@ApiImplicitParams({@ApiImplicitParam(name = "type", value = "类型（web/app/alexa/googlehome）", required = true, paramType = "query", dataType = "String"),
						@ApiImplicitParam(name = "userId", value = "用户ID", required = true, paramType = "query", dataType = "String"),
			 			@ApiImplicitParam(name = "password", value = "加密后的密码", required = true, paramType = "query", dataType = "String") })
	@RequestMapping(value="/changePassword",method=RequestMethod.POST)
	public int changePassword(@RequestParam("type")String type, @RequestParam("userId")String userId, @RequestParam("password")String password) throws BusinessException;

}