package com.iot.design.optical.service;

import com.iot.design.optical.vo.Rectangle;

import java.util.List;
import java.util.Map;

public interface CoordinateService {

    List<Map<String, Float>> calculator(Rectangle rectangle);

    void glareCalculator(Float[] eyePoint, List<Float[]> lightPoint, Float[] lightParams, String ies,List<Map<String, Object>> coordinateList);
}
