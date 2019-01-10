package com.iot.video.queue;

import java.util.List;

/**
 * 项目名称：cloud
 * 功能描述：基于List的自定义的队列批量处理接口
 * 创建人： yeshiyuan & shengxiang
 * 创建时间：2018/8/3 11:29
 * 修改人： yeshiyuan & shengxiang
 * 修改时间：2018/8/3 11:29
 * 修改描述：
 */
public interface IListBlockingQueue<T> {

    /**
      * @despriction：添加数据到缓存
      * @author  yeshiyuan
      * @created 2018/8/3 11:31
      * @param T 存储对象
      * @return
      */
    void add(T o);


    /**
      * @despriction：批量添加数据到缓存
      * @author  yeshiyuan
      * @created 2018/8/3 11:32
      * @param List
      * @return
      */
    void add(List<T> os);

    /**
      * @despriction：缓存是否为空
      * @author  yeshiyuan
      * @created 2018/8/3 11:33
      * @return
      */
    boolean isEmpty();

    /**
      * @despriction：缓存是否已满
      * @author  yeshiyuan
      * @created 2018/8/3 11:33
      * @return
      */
    boolean isFull();


    /**
      * @despriction：获取并删除数据，有最长等待时间
      * @author  yeshiyuan
      * @created 2018/8/3 11:34
      * @param mills 最长等待时间（单位：ms）
      * @return
      */
    List<T> poll(long mills);

    /**
      * @despriction：获取并删除数据
      * @author  yeshiyuan
      * @created 2018/8/3 11:35
      * @param null
      * @return
      */
    List<T> poll();

    /**
      * @despriction：获取缓冲区中所有元素
      * @author  yeshiyuan
      * @created 2018/8/3 11:36
      * @return
      */
    List<T> getAll();

    /**
      * @despriction：删除缓冲区中所有元素
      * @author  yeshiyuan
      * @created 2018/8/3 11:31
      * @return
      */
    List<T> removeAll();

    /**
      * @despriction：获取队列大小
      * @author  yeshiyuan
      * @created 2018/8/3 11:30
      * @return
      */
    long getSizeInByte();

    /**
      * @despriction：清空队列
      * @author  yeshiyuan
      * @created 2018/8/3 11:30
      * @return
      */
    void clear();
}
