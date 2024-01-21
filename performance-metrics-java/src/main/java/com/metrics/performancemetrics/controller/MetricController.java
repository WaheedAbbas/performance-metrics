package com.metrics.performancemetrics.controller;

import com.metrics.performancemetrics.dto.NewMetricValue;
import com.metrics.performancemetrics.entity.MetricValue;
import com.metrics.performancemetrics.util.ResponseMessages;
import com.metrics.performancemetrics.util.Result;
import com.metrics.performancemetrics.dto.NewMetric;
import com.metrics.performancemetrics.entity.Metric;
import com.metrics.performancemetrics.service.MetricService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api.endpoint.base-url}/metrics")
public class MetricController {

    private final MetricService metricService;

    public MetricController(MetricService metricService) {
        this.metricService = metricService;
    }

    @PostMapping()
    public Result addNewMetric(@RequestBody @Valid NewMetric newMetric)
    {
        Metric metric = this.metricService.addNewMetric(newMetric);
        return new Result(true, HttpStatus.CREATED.value(), ResponseMessages.NEW_METRIC_SUCCESS, metric);
    }

    @PostMapping("/{metric_id}")
    public Result addNewMetricValue(@PathVariable("metric_id") Integer metricId,  @RequestBody @Valid NewMetricValue newMetricValue)
    {
        MetricValue metricValue = this.metricService.addNewMetricValue(newMetricValue, metricId);
        return new Result(true, HttpStatus.OK.value(), ResponseMessages.ADD_NEW_METRICS_VALUE_SUCCESS, metricValue);
    }

    @GetMapping()
    public Result getAllMetrics()
    {
        List<Metric> metrics = this.metricService.getAllMetrics();
        return new Result(true, HttpStatus.OK.value(), ResponseMessages.GET_ALL_METRICS_SUCCESS, metrics);
    }
}
