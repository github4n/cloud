package com.iot.video.queue;

import com.iot.video.mongo.entity.VideoFileEntity;

import java.util.List;

/**
 * 项目名称：cloud
 * 功能描述：videoFile存入队列
 * 创建人： yeshiyuan
 * 创建时间：2018/8/3 15:36
 * 修改人： yeshiyuan
 * 修改时间：2018/8/3 15:36
 * 修改描述：
 */
public class VideoFileQueue {

    private static ArrayListBlockingQueue<VideoFileEntity> queue = null ;

    /**
      * @despriction：队列大小初始化
      * @author  yeshiyuan
      * @created 2018/8/6 9:45
      * @param null
      * @return 
      */
    public static void init(Integer maxSingleSize, Integer maxGroupSize) {
        if (queue == null) {
            synchronized(VideoFileQueue.class) {
                if (queue == null) {
                    queue = new ArrayListBlockingQueue<>(maxSingleSize,maxGroupSize, "videoFileQueue");
                }
            }
        }
    }
    
    /**
     * 把videoFile推送至缓存里
     * @param videoFile
     */
    public static void put(VideoFileEntity videoFile){
        queue.add(videoFile);
    }

    /**
     * 批量把videoFile推送至缓存里
     * @param videoFile
     */
    public static void batchPut(List<VideoFileEntity> videoFile){
        queue.add(videoFile);
    }

    /**
     * 从缓存里拿出数据
     * @return
     */
    public static List<VideoFileEntity> take(){
        return queue.poll();
    }


    /**
     * 从缓存里拿出数据(最长等待时间)
     * @return
     */
    public static List<VideoFileEntity> take(Long waitMillTime){
        return queue.poll(waitMillTime);
    }
}
