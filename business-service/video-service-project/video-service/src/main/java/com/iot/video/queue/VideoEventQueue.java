package com.iot.video.queue;

import com.iot.video.mongo.entity.VideoEventEntity;
import com.iot.video.mongo.entity.VideoFileEntity;

import java.util.List;

/**
 * 项目名称：cloud
 * 功能描述：videoEvent存入队列
 * 创建人： yeshiyuan
 * 创建时间：2018/8/3 15:36
 * 修改人： yeshiyuan
 * 修改时间：2018/8/3 15:36
 * 修改描述：
 */
public class VideoEventQueue {

    private static ArrayListBlockingQueue<VideoEventEntity> queue = null ;

    /**
      * @despriction：队列大小初始化
      * @author  yeshiyuan
      * @created 2018/8/6 9:45
      * @param null
      * @return 
      */
    public static void init(Integer maxSingleSize, Integer maxGroupSize) {
        if (queue == null) {
            synchronized(VideoEventQueue.class) {
                if (queue == null) {
                    queue = new ArrayListBlockingQueue<>(maxSingleSize, maxGroupSize, "videoEventQueue");
                }
            }
        }
    }
    
    /**
     * 把videoFile推送至缓存里
     * @param videoEvent
     */
    public static void put(VideoEventEntity videoEvent){
        queue.add(videoEvent);
    }

    /**
     * 批量把videoFile推送至缓存里
     * @param videoEvent
     */
    public static void batchPut(List<VideoEventEntity> videoEvent){
        queue.add(videoEvent);
    }

    /**
     * 从缓存里拿出数据
     * @return
     */
    public static List<VideoEventEntity> take(){
        return queue.poll();
    }


    /**
     * 从缓存里拿出数据(最长等待时间)
     * @return
     */
    public static List<VideoEventEntity> take(Long waitMillTime){
        return queue.poll(waitMillTime);
    }
}
