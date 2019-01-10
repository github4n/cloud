package com.iot.pack.service;

import com.iot.pack.controller.AppPackController;
import com.iot.pack.util.PackUtil;
import com.iot.pack.vo.PackReq;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 描述：app打包服务
 * 创建人： LaiGuiMing
 * 创建时间： 2018/6/1 11:18
 */
@Service
public class AppPackService {

    private final static Logger LOGGER = LoggerFactory.getLogger(AppPackController.class);
    private static ExecutorService executorService = Executors.newFixedThreadPool(20);

    /**
     * app打包
     *
     * @param req
     * @return
     */
    public Boolean pack(PackReq req) {
        LOGGER.info("receive app pack message," + req.toString());
        Boolean flag = true;
        PackUtil packUtil = new PackUtil();
        //这些操作做异步处理，加快响应速度
        executorService.submit(new Runnable() {
            @Override
            public void run() {
                try{
                    //异步处理
                    packUtil.pack(req);
                }catch (Exception e){
                    LOGGER.error("app pack failed",e);
                }
            }
        });
        return flag;
    }

    public Boolean test(){
        LOGGER.info("app pack test api!");
        //拉取svn代码
        PackUtil packUtil = new PackUtil();

        LOGGER.info("app pack test api end!");
        return true;
    }


    public Boolean validation(Map map){
        PackUtil packUtil = new PackUtil();
        return packUtil.validation(map);
    }


}
