package com.metrics.performancemetrics.service;

import com.metrics.performancemetrics.dto.NewMetric;
import com.metrics.performancemetrics.dto.NewMetricValue;
import com.metrics.performancemetrics.entity.Metric;
import com.metrics.performancemetrics.entity.MetricValue;
import com.metrics.performancemetrics.exception.MetricNotFoundException;
import com.metrics.performancemetrics.repository.MetricRepository;
import com.metrics.performancemetrics.repository.MetricValueRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;

@Transactional
@Service
public class MetricService {

    private static final Logger logger = LoggerFactory.getLogger(MetricService.class);

    private final MetricRepository metricRepository;
    private final MetricValueRepository metricValueRepository;

    @Autowired
    public MetricService(MetricRepository metricRepository, MetricValueRepository metricValueRepository) {
        this.metricRepository = metricRepository;
        this.metricValueRepository = metricValueRepository;
    }
    public Metric addNewMetric(NewMetric newMetric)
    {
        Metric metric = new Metric();
        metric.setCreated_at(Instant.now());
        metric.setName(newMetric.name());
        return this.metricRepository.save(metric);
    }
    public List<Metric> getAllMetrics()
    {
        return this.metricRepository.findAll();
    }
    public MetricValue addNewMetricValue(NewMetricValue newMetricValue, Integer metricId)
    {
        MetricValue metricValue = new MetricValue();
        metricValue.setValue(newMetricValue.value());
        metricValue.setCreated_at(Instant.now());
        metricValue.setMetric(getMetricByID(metricId));
        return this.metricValueRepository.save(metricValue);
    }
    public Metric getMetricByID(Integer metricId)
    {
        return this.metricRepository.findById(metricId).orElseThrow(() -> new MetricNotFoundException(metricId));
    }
}
