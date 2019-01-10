package com.iot.device.web;

import com.iot.device.service.core.AsyncTasks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.Future;


@RestController
@RequestMapping("/async")
public class AsyncTaskController {

    @Autowired
    AsyncTasks asyncTasks;


    /**
     * 测试异步任务
     *
     * @param
     * @return
     * @author lucky
     * @date 2018/7/6 10:14
     */
    @GetMapping("/task")
    public String task() throws Exception {
        long start = System.currentTimeMillis();

        Future<String> task1 = asyncTasks.doTaskOne();
        Future<String> task2 = asyncTasks.doTaskTwo();
        Future<String> task3 = asyncTasks.doTaskThree();

        while (true) {
            if (task1.isDone() && task2.isDone() && task3.isDone()) {
                // 三个任务都调用完成，退出循环等待
                break;
            }
            Thread.sleep(1000);
        }

        long end = System.currentTimeMillis();

        String result = "任务全部完成，总耗时：" + (end - start) + "毫秒";
        return result;
    }
}