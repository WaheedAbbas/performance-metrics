package com.metrics.performancemetrics.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.annotation.Nonnull;
import jakarta.persistence.*;
import lombok.Data;

import java.time.Instant;
import java.time.LocalDateTime;

@Entity(name = "metric_value")
@Data
public class MetricValue {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    @Column(nullable = false)
    private Double value;
    @Column(nullable = false)
    private Instant created_at;
    @ManyToOne
    @JoinColumn(name = "metric_id", nullable = false)
    @JsonIgnore
    private Metric metric;
}
