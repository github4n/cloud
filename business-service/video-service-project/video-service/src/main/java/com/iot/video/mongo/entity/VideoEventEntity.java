package com.iot.video.mongo.entity;

import com.iot.video.entity.VideoEvent;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * 项目名称：cloud
 * 功能描述：Mongodb存储的VideoEvent对象
 * 创建人： yeshiyuan
 * 创建时间：2018/8/6 16:25
 * 修改人： yeshiyuan
 * 修改时间：2018/8/6 16:25
 * 修改描述：
 */
@Document(collection = "video_event")
@CompoundIndexes({
        @CompoundIndex(name = "planId_1_eventOddurTime_1", def = "{'planId': 1, 'eventOddurTime': 1}")
})
public class VideoEventEntity extends VideoEvent{

    @Id
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

}
