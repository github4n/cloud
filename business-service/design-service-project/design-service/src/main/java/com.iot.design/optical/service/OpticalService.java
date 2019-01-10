package com.iot.design.optical.service;

import java.util.Map;

public interface OpticalService {

    void readFile(String fileId);

    void addWZZS2Redis(Map<String, Float> wzzsMap);


}
