package com.iot.device.repository;

import com.iot.device.model.DailyElectricityStatistics;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface DailyElectricityStatisticsRepository extends MongoRepository<DailyElectricityStatistics,Long> {
}
