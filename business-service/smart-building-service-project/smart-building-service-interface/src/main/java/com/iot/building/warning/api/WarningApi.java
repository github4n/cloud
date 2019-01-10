package com.iot.building.warning.api;

import java.util.List;

import com.iot.building.warning.api.callback.WarningApiFallbackFactory;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.iot.building.warning.vo.WarningReq;
import com.iot.building.warning.vo.WarningResp;
import com.iot.common.exception.BusinessException;
import com.iot.common.helper.Page;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * @Author: wl
 * @Date: 2018/9/3
 * @Description: *
 */
@Api("告警接口")
@FeignClient(value = "building-control-service" , fallbackFactory = WarningApiFallbackFactory.class)
@RequestMapping("/warning")
public interface WarningApi {

    @ApiOperation("查询告警")
    @RequestMapping(value = "/countWarningById", method = RequestMethod.GET)
    int countWarningById(@RequestParam("id") Long id) throws Exception;
	
    @ApiOperation("历史告警")
    @RequestMapping(value = "/findHistoryWarningList", method = RequestMethod.GET)
    public Page<WarningResp> findHistoryWarningList(@RequestParam("pageNum") String pageNum,@RequestParam("pageSize") String pageSize,
    		@RequestParam(value = "eventType", required = false) String eventType,
    		@RequestParam(value = "timeType", required = false) String timeType,
    		@RequestParam("tenantId")Long tenantId,@RequestParam("orgId")Long orgId,
			@RequestParam("locationId")Long locationId) throws BusinessException;
	
    /**
	 * 描述：查询未读告警数据
	 *
	 * @return
	 * @throws BusinessException
	 * @since
	 */
    @ApiOperation("查询未读告警数据")
    @RequestMapping(value = "/findUnreadWarningList", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public List<WarningResp> findUnreadWarningList(@RequestParam("tenantId")Long tenantId,@RequestParam("orgId")Long orgId,@RequestParam("locationId")Long locationId) throws BusinessException;

    /**
     * 描述：插入告警数据
     * @param warning
     * @throws BusinessException
     * @author zhouzongwei
     * @created 2017年11月30日 下午3:00:00
     * @since
     */
    @ApiOperation("插入告警数据")
    @RequestMapping(value = "/addWarning", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public WarningResp addWarning(@RequestBody WarningReq warning) throws BusinessException;

    @ApiOperation("跟新状态")
    @RequestMapping(value = "/updateWarningStatus", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public int updateWarningStatus(@RequestBody WarningReq warning) throws BusinessException;

    @ApiOperation("查询历史")
    @RequestMapping(value = "/findHistoryWarningListNoPage", method = RequestMethod.GET)
    List<WarningResp> findHistoryWarningListNoPage(@RequestParam("eventType")String eventType,@RequestParam("count") String count,
    		@RequestParam("tenantId")Long tenantId,@RequestParam("orgId")Long orgId,@RequestParam("locationId")Long locationId);


    @ApiOperation("综合查询的接口")
    @RequestMapping(value = "/findWarningList", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    List<WarningResp> findWarningList(@RequestBody WarningReq warning) throws BusinessException;
}
