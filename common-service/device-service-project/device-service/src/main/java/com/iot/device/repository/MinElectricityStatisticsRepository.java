package com.iot.device.repository;

import com.iot.device.model.MinElectricityStatistics;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MinElectricityStatisticsRepository extends MongoRepository<MinElectricityStatistics,Long> {
}
