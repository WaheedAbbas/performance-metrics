package com.metrics.performancemetrics.service;

import com.metrics.performancemetrics.dto.NewMetric;
import com.metrics.performancemetrics.entity.Metric;
import com.metrics.performancemetrics.repository.MetricRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Transactional
@Service
public class MetricService {

    private static final Logger logger = LoggerFactory.getLogger(MetricService.class);

    private final MetricRepository metricRepository;

    @Autowired
    public MetricService(MetricRepository metricRepository) {
        this.metricRepository = metricRepository;
    }
    public Metric addNewMetric(NewMetric newMetric)
    {
        Metric metric = new Metric();
        metric.setCreated_at(LocalDateTime.now());
        metric.setName(newMetric.name());
        return this.metricRepository.save(metric);
    }
    public List<Metric> getAllMetrics()
    {
        return this.metricRepository.findAll();
    }
}
