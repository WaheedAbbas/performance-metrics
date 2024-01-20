package com.metrics.performancemetrics.util;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Result {
    private boolean success;
    private Integer code;
    private String message;
    private Object data;
}
