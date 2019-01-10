package com.iot.control.ifttt.controller;

import com.iot.common.enums.APIType;
import com.iot.common.helper.Page;
import com.iot.control.ifttt.api.IftttApi;
import com.iot.control.ifttt.constant.IftttConstants;
import com.iot.control.ifttt.entity.Sensor;
import com.iot.control.ifttt.service.*;
import com.iot.control.ifttt.vo.SensorVo;
import com.iot.control.ifttt.vo.req.*;
import com.iot.control.ifttt.vo.res.RuleResp;
import com.iot.control.ifttt.vo.res.SensorResp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


@RestController
public class IftttController implements IftttApi {
    private static final Logger LOGGER = LoggerFactory.getLogger(IftttController.class);
    @Autowired
    private IftttService iftttService;


    @Autowired
    private RuleService ruleService;

    @Autowired
    private SensorService sensorService;

    @Autowired
    private ActuatorService actuatorService;

    @Autowired
    private RelationService relationService;

    @Autowired
    private TriggerService triggerService;


    @Override
    public Page<RuleResp> list(@RequestBody RuleListReq req) {
        return ruleService.list(req);
    }

    @Override
    public RuleResp get(@PathVariable("id") Long id) {
        return iftttService.get(id);
    }
}
