package com.hegp.controller.common;

import com.hegp.core.model.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.ResourceAccessException;

import java.io.IOException;
import java.io.Serializable;

@RestController
@ControllerAdvice
public class ErrorControllerAdvice implements Serializable {

    private Logger logger = LoggerFactory.getLogger(ErrorControllerAdvice.class);

    @ExceptionHandler(Exception.class)
    public Result handleException(Exception e) {
        e.printStackTrace();
        return Result.fail(499, e.getMessage());
    }

    // RuntimeException
    @ExceptionHandler(RuntimeException.class)
    public Result handleRuntimeException(Exception e) {
        e.printStackTrace();
        return Result.fail(499, e.getMessage());
    }

    @ResponseStatus(value = HttpStatus.OK)
    @ExceptionHandler(ResourceAccessException.class)
    public Result handleResourceAccessException(Exception e) {
        e.printStackTrace();
        return Result.fail(499, e.getMessage());
    }

    @ResponseStatus(value = HttpStatus.OK)
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public Result handleMethodNotSupportedException(Exception e) {
        e.printStackTrace();
        return Result.fail(499, e.getMessage());
    }

    @ResponseStatus(value = HttpStatus.OK)
    @ExceptionHandler(ServletRequestBindingException.class)
    public Result handleServletRequestBindingException(Exception e) {
        e.printStackTrace();
        return Result.fail(499, e.getMessage());
    }

    @ResponseStatus(value = HttpStatus.OK)
    @ExceptionHandler(IOException.class)
    public Result handleIOException(Exception e) {
        e.printStackTrace();
        return Result.fail(499, e.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public Result handleIllegalArgumentException(IllegalArgumentException e) {
        e.printStackTrace();
        return Result.fail(499, e.getMessage());
    }
}
