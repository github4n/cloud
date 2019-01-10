package com.iot.control.activity.mongo;


import com.iot.control.activity.domain.ActivityRecord;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ActivityRecordMongo extends MongoRepository<ActivityRecord, Long> {




}
