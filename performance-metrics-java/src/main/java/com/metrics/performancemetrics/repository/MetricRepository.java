package com.metrics.performancemetrics.repository;

import com.metrics.performancemetrics.entity.Metric;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MetricRepository extends JpaRepository<Metric, Integer> {
}
