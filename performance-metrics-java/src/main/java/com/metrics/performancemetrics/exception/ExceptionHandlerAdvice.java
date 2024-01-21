package com.metrics.performancemetrics.exception;

import com.metrics.performancemetrics.util.Result;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class ExceptionHandlerAdvice {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    Result handleValidationException(MethodArgumentNotValidException ex) {
        List<ObjectError> errors = ex.getBindingResult().getAllErrors();
        Map<String, String> map = new HashMap<>(errors.size());
        errors.forEach((error) -> {
            String key = ((FieldError) error).getField();
            String val = error.getDefaultMessage();
            map.put(key, val);
        });
        return new Result(false, HttpStatus.BAD_REQUEST.value(), "Provided arguments are invalid, see data for details.", map);
    }
    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    Result handleMetricIdNotFound(MetricNotFoundException metricNotFoundException){
        return new Result(false, HttpStatus.NOT_FOUND.value(), metricNotFoundException.getMessage(), null);
    }
    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    Result handleHttpMessageNotWritableException(HttpMessageNotWritableException httpMessageNotWritableException){
        return new Result(false, HttpStatus.INTERNAL_SERVER_ERROR.value(), httpMessageNotWritableException.getMessage(), null);
    }
}
