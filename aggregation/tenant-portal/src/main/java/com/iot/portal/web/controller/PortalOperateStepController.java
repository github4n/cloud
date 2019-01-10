package com.iot.portal.web.controller;

import com.iot.common.annotation.Action;
import com.iot.common.annotation.LoginRequired;
import com.iot.common.beans.BeanUtil;
import com.iot.common.beans.CommonResponse;
import com.iot.common.exception.BusinessException;
import com.iot.device.api.OperateStepRecordApi;
import com.iot.device.vo.req.OperateStepRecordReq;
import com.iot.portal.exception.BusinessExceptionEnum;
import com.iot.portal.web.vo.req.SaveOperateStepRecordReq;
import com.iot.saas.SaaSContextHolder;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * 项目名称：cloud
 * 功能描述：portal操作步骤记录
 * 创建人： yeshiyuan
 * 创建时间：2018/9/12 10:06
 * 修改人： yeshiyuan
 * 修改时间：2018/9/12 10:06
 * 修改描述：
 */
@Api(value = "操作步骤记录", description = "操作步骤记录")
@RestController
@RequestMapping(value = "/portal/operateStep")
public class PortalOperateStepController {

    @Autowired
    private OperateStepRecordApi stepRecordApi;

    /**
      * @despriction：查询当前所处步骤
      * @author  yeshiyuan
      * @created 2018/9/12 10:16
      * @return
      */
    @LoginRequired(Action.Normal)
    @ApiOperation(value = "查询当前所处步骤", notes = "查询当前所处步骤")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "operateId", value = "操作对象id", dataType = "Long", paramType = "query"),
            @ApiImplicitParam(name = "operateType", value = "操作类型", dataType = "String", paramType = "query"),
    })
    @RequestMapping(value = "/currentStep", method = RequestMethod.GET)
    public CommonResponse currentStep(Long operateId, String operateType) {
        Map<String, Object> map = new HashMap<>();
        map.put("currentStep", stepRecordApi.currentStep(operateId, SaaSContextHolder.currentTenantId(), operateType));
        return CommonResponse.success(map);
    }

    /**
      * @despriction：保存操作步骤
      * @author  yeshiyuan
      * @created 2018/9/12 10:29
      * @return
      */
    @LoginRequired(Action.Normal)
    @ApiOperation(value = "保存操作步骤", notes = "保存操作步骤")
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public CommonResponse save(@RequestBody SaveOperateStepRecordReq recordReq) {
        if (recordReq == null) {
            throw new BusinessException(BusinessExceptionEnum.PARAM_IS_ERROR);
        }
        OperateStepRecordReq operateStepRecordReq = new OperateStepRecordReq();
        BeanUtil.copyProperties(recordReq, operateStepRecordReq);
        operateStepRecordReq.setTenantId(SaaSContextHolder.currentTenantId());
        operateStepRecordReq.setUserId(SaaSContextHolder.getCurrentUserId());
        stepRecordApi.save(operateStepRecordReq);
        return CommonResponse.success();
    }

}
