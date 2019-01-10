package com.iot.device.repository;

import com.iot.device.model.WeeklyElectricityStatistics;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface WeeklyElectricityStatisticsRepository extends MongoRepository<WeeklyElectricityStatistics,Long> {
}
