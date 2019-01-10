package com.iot.cloud.controller;

import com.iot.cloud.service.impl.SpaceService;
import com.iot.cloud.util.ConstantUtil;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 创建人:chenweida
 * 创建时间:2018/9/10
 */
@RestController
@RequestMapping(ConstantUtil.commonPath+"/space")
@Api(tags = "space模块")
public class SpaceController {
    @Autowired
    private SpaceService spaceService;


}
