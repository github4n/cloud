package com.iot.boss.controller;

import com.iot.boss.service.uuid.IftttService;
import com.iot.boss.vo.TemplateVoResp;
import com.iot.common.annotation.Action;
import com.iot.common.annotation.LoginRequired;
import com.iot.common.beans.CommonResponse;
import com.iot.common.exception.BusinessException;
import com.iot.control.ifttt.exception.IftttExceptionEnum;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(description = "模板管理",value = "模板管理")
@RequestMapping("/ifttt")
public class IftttController {
    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(IftttController.class);

    @Autowired
    private IftttService iftttService;

    @ApiOperation("获取模板")
    @LoginRequired(value = Action.Normal)
    @RequestMapping(value = "/getTempaltes", method = RequestMethod.GET)
    public CommonResponse<TemplateVoResp> getTempaltes(@RequestParam("productModel") String productModel) {
        LOGGER.info("收到获取模板的请求，产品的model：" + productModel);
        try {
            TemplateVoResp tempaltes = iftttService.getTempaltes(productModel);
            if (tempaltes == null) {
                throw new BusinessException(IftttExceptionEnum.TEMPLATE_IS_NULL);
            }
            return CommonResponse.success(tempaltes);
        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            LOGGER.info("getTempaltes.error：" + e);
            throw e;
        }
    }

}
