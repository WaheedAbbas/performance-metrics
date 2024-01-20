package com.metrics.performancemetrics.entity;

import jakarta.annotation.Nonnull;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity(name = "metric_value")
@Data
public class MetricValue {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Integer id;
    @Column(nullable = false)
    Double value;
    @Column(nullable = false)
    LocalDateTime created_at;
    @ManyToOne
    @JoinColumn(name = "metric_id", nullable = false)
    private Metric metric;
}
