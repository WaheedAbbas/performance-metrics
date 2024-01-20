package com.metrics.performancemetrics.controller;

import com.metrics.performancemetrics.util.ResponseMessages;
import com.metrics.performancemetrics.util.Result;
import com.metrics.performancemetrics.dto.NewMetric;
import com.metrics.performancemetrics.entity.Metric;
import com.metrics.performancemetrics.service.MetricService;
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
    public Result addNewMetric(@RequestBody NewMetric newMetric)
    {
        Metric metric = this.metricService.addNewMetric(newMetric);
        return new Result(true, HttpStatus.CREATED.value(), ResponseMessages.NEW_METRIC_SUCCESS, metric);
    }
    @GetMapping()
    public Result getAllMetrics()
    {
        List<Metric> metrics = this.metricService.getAllMetrics();
        return new Result(true, HttpStatus.OK.value(), ResponseMessages.GET_ALL_METRICS_SUCCESS, metrics);
    }
}
