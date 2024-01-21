package com.metrics.performancemetrics.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity(name = "metric")
@Data
public class Metric {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private Instant created_at;
    @OneToMany(mappedBy = "metric", cascade = CascadeType.ALL)
    private List<MetricValue> metricValues = new ArrayList<>();
}
