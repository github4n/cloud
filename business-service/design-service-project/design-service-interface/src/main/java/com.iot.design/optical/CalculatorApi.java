package com.iot.design.optical;

import com.iot.design.optical.req.GlareCalculatorReq;
import com.iot.design.optical.vo.Rectangle;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;
import java.util.Map;

@Api("计算接口")
@FeignClient(value = "design-service")
@RequestMapping("/calculator")
public interface CalculatorApi {
    /**
     * 长方体坐标计算
     *
     * @param rectangle
     * @return
     */
    @ApiOperation("长方体各个坐标点照度")
    @RequestMapping(value = "/rectangle", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    List<Map<String, Float>> calculator(@RequestBody Rectangle rectangle);

    @ApiOperation("眩光值计算")
    @RequestMapping(value = "/glareCalculator", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    void glareCalculator(@RequestBody GlareCalculatorReq calculatorReq);
}
