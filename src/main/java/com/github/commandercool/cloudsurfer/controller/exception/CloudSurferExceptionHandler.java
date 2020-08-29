package com.github.commandercool.cloudsurfer.controller.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice(annotations = Controller.class)
public class CloudSurferExceptionHandler {

    @ResponseBody
    @ExceptionHandler(value = {Exception.class})
    public ResponseEntity<String> handleException(Exception e) {
        ResponseEntity.BodyBuilder responseBuilder;
        if (e instanceof IllegalArgumentException) {
            responseBuilder = ResponseEntity.status(400);
        } else {
            responseBuilder = ResponseEntity.status(500);
        }
        return responseBuilder.body(getMessage(e));
    }

    private String getMessage(Throwable th) {
        while (StringUtils.isEmpty(th.getMessage()) && th.getCause() != null) {
            th = th.getCause();
        }
        return th.getMessage();
    }

}
