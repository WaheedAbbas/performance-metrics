package com.metrics.performancemetrics.repository;

import com.metrics.performancemetrics.entity.MetricValue;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MetricValueRepository extends JpaRepository<MetricValue, Long> {
}
