package com.iot.shcs.space.service;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Map;

import static org.junit.Assert.*;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class SpaceCoreServiceTest {

    @Autowired
    private SpaceCoreService spaceCoreService;

    @Test
    public void findSpaceDeviceList() {
        Map<String, Object> result =
                spaceCoreService.findSpaceDeviceList(2l, 1197l, "1506", "1399");
        System.out.println("ddd");
    }
}