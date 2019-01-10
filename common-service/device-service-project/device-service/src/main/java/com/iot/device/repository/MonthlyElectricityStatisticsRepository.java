package com.iot.device.repository;

import com.iot.device.model.MonthlyElectricityStatistics;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MonthlyElectricityStatisticsRepository extends MongoRepository<MonthlyElectricityStatistics,Long> {
}
