package com.iot.device.repository;

import com.iot.device.model.MinRuntime;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MinRuntimeRepository extends MongoRepository<MinRuntime,Long>{
}
