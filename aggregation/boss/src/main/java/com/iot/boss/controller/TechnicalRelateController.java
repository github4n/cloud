package com.iot.boss.controller;

import com.iot.boss.service.technicalrelate.TechnicalRelateService;
import com.iot.common.annotation.Action;
import com.iot.common.annotation.LoginRequired;
import com.iot.common.beans.CommonResponse;
import com.iot.common.exception.ResultMsg;
import com.iot.device.vo.NetworkTypeVo;
import com.iot.device.vo.req.NetworkTypePageReq;
import com.iot.device.vo.req.technical.SaveDeviceTechnicalReq;
import com.iot.saas.SaaSContextHolder;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Api(description = "技术方案相关功能",value = "技术方案相关功能")
@RequestMapping("api/technicalRelate")
public class TechnicalRelateController {

	@Autowired
	private TechnicalRelateService technicalRelateService;

    @ApiOperation(value = "查询配网模式信息", notes = "查询配网模式信息")
    @LoginRequired(value = Action.Normal)
    @ApiImplicitParams({@ApiImplicitParam(name = "networkTypeId", value = "配网模式id", required = true, paramType = "query", dataType = "Integer")})
    @RequestMapping(value = "getNetworkInfo", method = RequestMethod.GET)
    public CommonResponse getNetworkInfo(@RequestParam("networkTypeId") Long networkTypeId) {
        return ResultMsg.SUCCESS.info(this.technicalRelateService.getNetworkInfo(networkTypeId));
    }
    
    @ApiOperation(value = "更新配网模式信息", notes = "更新配网模式信息")
    @LoginRequired(value = Action.Normal)
    @RequestMapping(value = "updateNetworkInfo", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse updateNetworkInfo(@RequestBody NetworkTypeVo networkTypeVo) {
    	this.technicalRelateService.updateNetworkInfo(networkTypeVo);
        return ResultMsg.SUCCESS.info();
    }

    /**
      * @despriction：分页查询配网类型
      * @author  yeshiyuan
      * @created 2018/10/15 17:30
      * @return
      */
    @LoginRequired(value = Action.Normal)
    @ApiOperation(value = "分页查询配网类型", notes = "分页查询配网类型")
    @RequestMapping(value = "/queryNetworkType", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse queryNetworkType(@RequestBody NetworkTypePageReq pageReq) {
        return ResultMsg.SUCCESS.info(technicalRelateService.queryNetworkType(pageReq));
    }

    /**
      * @despriction：加载所有的技术文案
      * @author  yeshiyuan
      * @created 2018/10/15 17:54
      * @return
      */
    @LoginRequired(Action.Normal)
    @ApiOperation(value = "加载所有的技术文案", notes = "加载所有的技术文案")
    @RequestMapping(value = "/getAllTechnical", method = RequestMethod.GET)
    public CommonResponse getAllTechnical(){
        return ResultMsg.SUCCESS.info(technicalRelateService.getAllTechnical());
    }

    /**
     * 描述：加载所有配网code
     * @author maochengyuan
     * @created 2018/11/22 19:55
     * @param
     * @return com.iot.common.beans.CommonResponse
     */
    @LoginRequired(Action.Normal)
    @ApiOperation(value = "加载所有配网code", notes = "加载所有配网code")
    @RequestMapping(value = "/getAllTypeCode", method = RequestMethod.GET)
    public CommonResponse getAllTypeCode(){
        return ResultMsg.SUCCESS.info(this.technicalRelateService.getAllTypeCode());
    }

    /**
      * @despriction：保存设备支持的技术方案
      * @author  yeshiyuan
      * @created 2018/10/15 19:41
      * @return
      */
    @LoginRequired(Action.Normal)
    @ApiOperation(value = "保存设备支持的技术方案", notes = "保存设备支持的技术方案")
    @RequestMapping(value = "/saveDeviceTechnical", method = RequestMethod.POST)
    public CommonResponse saveDeviceTechnical(@RequestBody SaveDeviceTechnicalReq req) {
        req.setCreateBy(SaaSContextHolder.getCurrentUserId());
        technicalRelateService.saveDeviceTechnical(req);
        return ResultMsg.SUCCESS.info();
    }

    /**
      * @despriction：查询设备支持的技术方案和配网类型
      * @author  yeshiyuan
      * @created 2018/10/15 20:10
      * @return
      */
    @LoginRequired(Action.Normal)
    @ApiOperation(value = "查询设备支持的技术方案和配网类型", notes = "查询设备支持的技术方案和配网类型")
    @RequestMapping(value = "/queryTechnicalAndNetwork", method = RequestMethod.GET)
    public CommonResponse queryTechnicalAndNetwork(@RequestParam("deviceTypeId") Long deviceTypeId) {
        return ResultMsg.SUCCESS.info(technicalRelateService.queryTechnicalAndNetwork(deviceTypeId));
    }

    /**
      * @despriction：查询设备类型支持的配网模式
      * @author  yeshiyuan
      * @created 2018/10/16 10:40
      * @return
      */
    @LoginRequired(Action.Normal)
    @ApiOperation(value = "查询设备类型支持的配网模式", notes = "查询设备类型支持的配网模式")
    @RequestMapping(value = "/deviceSupportNetworkType", method = RequestMethod.GET)
    public CommonResponse deviceSupportNetwork(@RequestParam("deviceTypeId") Long deviceTypeId) {
        return ResultMsg.SUCCESS.info(technicalRelateService.deviceSupportNetwork(deviceTypeId));
    }

    /**
      * @despriction：查询技术方案支持的配网模式
      * @author  yeshiyuan
      * @created 2018/10/16 16:21
      * @return
      */
    @LoginRequired(Action.Normal)
    @ApiOperation(value = "查询技术方案支持的配网模式", notes = "查询技术方案支持的配网模式")
    @RequestMapping(value = "/findNetworkByTechnicalIds", method = RequestMethod.GET)
    public CommonResponse findNetworkByTechnicalIds(@RequestParam("technicalIds") List<Long> technicalIds) {
        return ResultMsg.SUCCESS.info(technicalRelateService.findNetworkByTechnicalIds(technicalIds));
    }

    /**
     * @despriction：加载设备类型支持的技术方案
     * @author  yeshiyuan
     * @created 2018/10/16 11:41
     * @return
     */
    @LoginRequired(value = Action.Normal)
    @ApiOperation(value = "加载设备类型支持的技术方案", notes = "加载设备类型支持的技术方案")
    @RequestMapping(value = "/getDeviceTechnical", method = RequestMethod.GET)
    public CommonResponse getDeviceTechnical(@RequestParam("deviceTypeId") Long deviceTypeId) {
        return ResultMsg.SUCCESS.info(this.technicalRelateService.getDeviceTechnical(deviceTypeId));
    }
}
