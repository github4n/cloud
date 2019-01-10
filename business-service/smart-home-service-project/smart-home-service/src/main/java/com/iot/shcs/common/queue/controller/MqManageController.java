package com.iot.shcs.common.queue.controller;

import com.iot.common.helper.ApplicationContextHelper;
import com.iot.shcs.common.queue.bean.DemoMessage;
import com.iot.shcs.common.queue.sender.DemoSender;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: lucky
 * @Descrpiton: https://blog.csdn.net/u011424653/article/details/79824538
 * https://www.jianshu.com/p/4112d78a8753
 * @Date: 15:09 2018/8/15
 * @Modify by:
 */
@RestController
@RequestMapping("/mqManage")
public class MqManageController {

    @GetMapping("/demoSend")
    public Object demoSend() {
        DemoMessage demoMessage = new DemoMessage();
        demoMessage.setContent("xxxxxxxxxxxxx");
        demoMessage.setName("lucky");
        demoMessage.setCreateBy("lucky");
        ApplicationContextHelper.getBean(DemoSender.class).send(demoMessage);
        return "success";
    }
}
