package com.iot.video.queue;

import com.iot.common.util.JsonUtil;
import com.iot.video.mongo.entity.VideoEventEntity;
import com.iot.video.mongo.entity.VideoFileEntity;
import com.iot.video.mongo.service.VideoEventMongoService;
import com.iot.video.mongo.service.VideoFileMongoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 项目名称：cloud
 * 功能描述：videoEvent队列消费，插入到mongodb
 * 创建人： yeshiyuan
 * 创建时间：2018/8/3 15:25
 * 修改人： yeshiyuan
 * 修改时间：2018/8/3 15:25
 * 修改描述：
 */
@Component
public class VideoEventQueueConsumer {

    private final static Logger logger = LoggerFactory.getLogger(VideoEventQueueConsumer.class);

    private ExecutorService executorService ;

    @Value("${queue.videoEvent.maxSingleSize}")
    private Integer maxSingleSize;

    @Value("${queue.videoEvent.maxGroupSize}")
    private Integer maxGroupSize;

    @Value("${queue.videoEvent.consumerThreadNum}")
    private Integer threadNum;


    @Autowired
    private VideoEventMongoService videoEventMongoService;

    @PostConstruct
    public void init(){
        if (executorService == null){
            executorService = Executors.newFixedThreadPool(threadNum);
        }
        logger.info("***********************************");
        VideoEventQueue.init(maxSingleSize, maxGroupSize);
        logger.info("videoEvent queue init complete.....");
        initConsumerThread();
        logger.info("***********************************");
        logger.info("videoEvent queue consumer start.....");
        logger.info("***********************************");
    }

    public VideoEventQueueConsumer() {

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
                        List<VideoEventEntity> list = VideoEventQueue.take(1000L);
                        if (list !=null && !list.isEmpty()){
                            try {
                                videoEventMongoService.bacthInsert(list);
                            }catch (Exception e) {
                                logger.error("videoEventQueue consumer fail : " + JsonUtil.toJson(list), e);
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
