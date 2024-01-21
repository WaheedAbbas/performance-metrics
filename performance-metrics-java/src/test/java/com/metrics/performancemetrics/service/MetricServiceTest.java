package com.metrics.performancemetrics.service;

import com.metrics.performancemetrics.dto.NewMetric;
import com.metrics.performancemetrics.entity.Metric;
import com.metrics.performancemetrics.repository.MetricRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MetricServiceTest {

    @Mock
    MetricRepository metricRepository;
    @InjectMocks
    MetricService metricService;
    List<Metric> mockMetrics = new ArrayList<>();
    @BeforeEach
    void setUp() {

        Metric remoteEmployees = new Metric();
        remoteEmployees.setId(1);
        remoteEmployees.setName("Remote Employees");
        remoteEmployees.setCreated_at(Instant.now());

        Metric onSiteEmployees = new Metric();
        onSiteEmployees.setId(2);
        onSiteEmployees.setName("Onsite Employees");
        onSiteEmployees.setCreated_at(Instant.now());

        mockMetrics.add(remoteEmployees);
        mockMetrics.add(onSiteEmployees);
    }
    @Test
    void addNewMetric_ShouldReturnSuccess() {

        //Given
        Metric remoteEmployees = new Metric();
        remoteEmployees.setId(1);
        remoteEmployees.setName("Remote Employees");
        remoteEmployees.setCreated_at(Instant.now());

        //When
        when(metricRepository.save(any(Metric.class))).thenReturn(remoteEmployees);
        Metric savedMetric = this.metricService.addNewMetric(new NewMetric(remoteEmployees.getName()));

        //Then
        assertThat(savedMetric.getId()).isEqualTo(remoteEmployees.getId());
        assertThat(savedMetric.getName()).isEqualTo(remoteEmployees.getName());
        assertThat(savedMetric.getCreated_at()).isEqualTo(remoteEmployees.getCreated_at());

        //verify
        verify(metricRepository, times(1)).save(any(Metric.class));
    }

    @Test
    void getAllMetrics_ShouldReturnMetricsList() {

        when(metricService.getAllMetrics()).thenReturn(mockMetrics);

        List<Metric> receivedMetrics = this.metricService.getAllMetrics();

        // Assert
        assertThat(receivedMetrics.size()).isEqualTo(mockMetrics.size());
        assertThat(receivedMetrics.get(0).getId()).isEqualTo(mockMetrics.get(0).getId());
        assertThat(receivedMetrics.get(0).getName()).isEqualTo(mockMetrics.get(0).getName());
        assertThat(receivedMetrics.get(0).getCreated_at()).isEqualTo(mockMetrics.get(0).getCreated_at());

        verify(metricRepository, times(1)).findAll();
    }

    @Test
    void getAllMetrics_ShouldReturnEmptyList() {
        when(metricService.getAllMetrics()).thenReturn(Collections.emptyList());

        List<Metric> receivedMetrics = this.metricService.getAllMetrics();

        assertThat(receivedMetrics).isEmpty();
        verify(metricRepository, times(1)).findAll();
    }

}