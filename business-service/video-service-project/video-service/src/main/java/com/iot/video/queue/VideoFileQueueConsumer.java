package com.iot.video.queue;

import com.iot.common.util.JsonUtil;
import com.iot.video.contants.ModuleConstants;
import com.iot.video.mongo.entity.VideoFileEntity;
import com.iot.video.mongo.service.VideoFileMongoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 项目名称：cloud
 * 功能描述：videoFile队列消费，插入到mongodb
 * 创建人： yeshiyuan
 * 创建时间：2018/8/3 15:25
 * 修改人： yeshiyuan
 * 修改时间：2018/8/3 15:25
 * 修改描述：
 */
@Component
public class VideoFileQueueConsumer {

    private final static Logger logger = LoggerFactory.getLogger(VideoFileQueueConsumer.class);

    private ExecutorService executorService ;

    @Value("${queue.videoFile.maxSingleSize}")
    private Integer maxSingleSize;

    @Value("${queue.videoFile.maxGroupSize}")
    private Integer maxGroupSize;

    @Value("${queue.videoFile.consumerThreadNum}")
    private Integer threadNum;


    @Autowired
    private VideoFileMongoService videoFileMongoService;

    @PostConstruct
    public void init(){
        if (executorService == null){
            executorService = Executors.newFixedThreadPool(threadNum);
        }
        logger.info("***********************************");
        VideoFileQueue.init(maxSingleSize, maxGroupSize);
        logger.info("videoFile queue init complete.....");
        initConsumerThread();
        logger.info("***********************************");
        logger.info("videoFile queue consumer start.....");
        logger.info("***********************************");
    }

    public VideoFileQueueConsumer() {

    }

    /**
      * @despriction：初始化线程
      * @author  yeshiyuan
      * @created 2018/8/3 15:56
      * @return
      */
    private void initConsumerThread(){
        for (int i = 0; i < threadNum; i++){
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    while (true){
                        List<VideoFileEntity> list = VideoFileQueue.take(1000L);
                        if (list !=null && !list.isEmpty()){
                            try {
                                videoFileMongoService.bacthInsert(list);
                            }catch (Exception e) {
                                logger.error("videoFileQueue consumer fail : " + JsonUtil.toJson(list), e);
                            }
                        }
                    }
                }
            });
            executorService.execute(thread);
        }
    }

    public Integer getMaxSingleSize() {
        return maxSingleSize;
    }

    public void setMaxSingleSize(Integer maxSingleSize) {
        this.maxSingleSize = maxSingleSize;
    }

    public Integer getMaxGroupSize() {
        return maxGroupSize;
    }

    public void setMaxGroupSize(Integer maxGroupSize) {
        this.maxGroupSize = maxGroupSize;
    }
}
