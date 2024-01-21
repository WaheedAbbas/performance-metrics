package com.metrics.performancemetrics.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.metrics.performancemetrics.dto.NewMetric;
import com.metrics.performancemetrics.dto.NewMetricValue;
import com.metrics.performancemetrics.entity.Metric;
import com.metrics.performancemetrics.entity.MetricValue;
import com.metrics.performancemetrics.exception.MetricNotFoundException;
import com.metrics.performancemetrics.service.MetricService;
import com.metrics.performancemetrics.util.ResponseMessages;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;


@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
class MetricControllerTest {

    @Autowired
    MockMvc mockMvc;
    @MockBean
    MetricService metricService;
    @Autowired
    ObjectMapper objectMapper;
    @Value("${api.endpoint.base-url}")
    String baseUrl;
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
    void testFindAllUsersSuccess() throws Exception
    {
        given(this.metricService.getAllMetrics()).willReturn(this.mockMetrics);

        this.mockMvc.perform(MockMvcRequestBuilders.get(this.baseUrl + "/metrics").accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.code").value(HttpStatus.OK.value()))
                .andExpect(jsonPath("$.message").value(ResponseMessages.GET_ALL_METRICS_SUCCESS))
                .andExpect(jsonPath("$.data[0].id").value(mockMetrics.get(0).getId()))
                .andExpect(jsonPath("$.data[0].name").value(mockMetrics.get(0).getName()))
                .andExpect(jsonPath("$.data[0].created_at").value(mockMetrics.get(0).getCreated_at().toString()));

    }
    @Test
    void testGetAllMetricsEmptyList() throws Exception {
        given(metricService.getAllMetrics()).willReturn(Collections.emptyList());

        mockMvc.perform(MockMvcRequestBuilders.get(baseUrl + "/metrics").accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.code").value(HttpStatus.OK.value()))
                .andExpect(jsonPath("$.message").value(ResponseMessages.GET_ALL_METRICS_SUCCESS))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    void testAddNewMetricSuccess() throws Exception {
        // Arrange
        NewMetric newMetric = new NewMetric("New Metric");

        Metric addedMetric = new Metric();
        addedMetric.setId(1);
        addedMetric.setName("New Metric");
        addedMetric.setCreated_at(Instant.now());

        given(metricService.addNewMetric(eq(newMetric))).willReturn(addedMetric);

        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.post(baseUrl + "/metrics")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newMetric))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.code").value(HttpStatus.CREATED.value()))
                .andExpect(jsonPath("$.message").value(ResponseMessages.NEW_METRIC_SUCCESS))
                .andExpect(jsonPath("$.data.id").value(addedMetric.getId()))
                .andExpect(jsonPath("$.data.name").value(addedMetric.getName()))
                .andExpect(jsonPath("$.data.created_at").value(addedMetric.getCreated_at().toString()));

        // Verify that the service method was called
        verify(metricService, times(1)).addNewMetric(eq(newMetric));
    }

    @Test
    void test_AddNewMetricValue_CorrectID_Success() throws Exception {
        // Arrange
        NewMetricValue newMetricValue = new NewMetricValue(42.0);

        MetricValue addedMetricValue = new MetricValue();
        addedMetricValue.setId(1);
        addedMetricValue.setValue(42.0);
        addedMetricValue.setCreated_at(Instant.now());

        given(metricService.addNewMetricValue(eq(newMetricValue), eq(1))).willReturn(addedMetricValue);

        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.post(baseUrl + "/metrics/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newMetricValue))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.code").value(HttpStatus.OK.value()))
                .andExpect(jsonPath("$.message").value(ResponseMessages.ADD_NEW_METRICS_VALUE_SUCCESS))
                .andExpect(jsonPath("$.data.id").value(addedMetricValue.getId()))
                .andExpect(jsonPath("$.data.value").value(addedMetricValue.getValue()))
                .andExpect(jsonPath("$.data.created_at").value(addedMetricValue.getCreated_at().toString()));

        // Verify that the service method was called
        verify(metricService, times(1)).addNewMetricValue(eq(newMetricValue), eq(1));
    }
    @Test
    void test_AddNewMetricValue_IncorrectId_Failure() throws Exception {
        // Arrange
        NewMetricValue newMetricValue = new NewMetricValue(42.0);

        given(metricService.addNewMetricValue(eq(newMetricValue), any(Integer.class))).willThrow(new MetricNotFoundException(1));

        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.post(baseUrl + "/metrics/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newMetricValue))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.code").value(HttpStatus.NOT_FOUND.value()))
                .andExpect(jsonPath("$.message").value(ResponseMessages.INVALID_METRIC_ID + " " + 1));
        // Verify that the service method was called
        verify(metricService, times(1)).addNewMetricValue(eq(newMetricValue), eq(1));
    }

}