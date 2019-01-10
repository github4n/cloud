package com.iot.design.optical.controller;

import com.iot.design.optical.CalculatorApi;
import com.iot.design.optical.service.CoordinateService;
import com.iot.design.optical.req.GlareCalculatorReq;
import com.iot.design.optical.vo.Rectangle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * 坐标计算
 *
 * @author fenglijian
 */
@RestController
public class CoordinateController implements CalculatorApi {

    @Autowired
    private CoordinateService coordinateService;

    /**
     * 长方体坐标计算
     */
    @Override
    public List<Map<String, Float>> calculator(@RequestBody Rectangle rectangle) {
        List<Map<String, Float>> result = coordinateService.calculator(rectangle);
        return result;
    }


    @Override
    public void glareCalculator(@RequestBody GlareCalculatorReq calculatorReq) {
        coordinateService.glareCalculator(
                calculatorReq.getTestPoint(),
                calculatorReq.getLightPoint(),
                calculatorReq.getLightParams(),
                calculatorReq.getIes(),
                calculatorReq.getCoordinateList());
    }

}
