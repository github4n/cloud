package com.iot.video.mongo.entity;

import com.iot.video.entity.VideoFile;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.Date;

/**
 * 项目名称：cloud
 * 功能描述：Mongodb存储的VideoFile对象
 * 创建人： yeshiyuan
 * 创建时间：2018/8/3 11:45
 * 修改人： yeshiyuan
 * 修改时间：2018/8/3 11:45
 * 修改描述：
 */
@Document(collection = "video_file")
@CompoundIndexes({
		@CompoundIndex(name = "planId_1_videoStartTime_1", def = "{'planId': 1, 'videoStartTime': 1}")
})
public class VideoFileEntity extends VideoFile implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	private String id;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public VideoFileEntity() {
	}

	public VideoFileEntity(Date videoStartTime, Date videoEndTime, Integer fileSize, Float videoLength, String fileId, String filePath ) {
		super(videoStartTime, videoEndTime, fileSize, videoLength, fileId, filePath);
	}
}
