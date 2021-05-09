package com.example.demo.advice;
import com.example.demo.exception.NotEnoughRightException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.LinkedHashMap;
import java.util.Map;

@ControllerAdvice
public class NotEnoughRight {

    @ResponseBody
    @ExceptionHandler(NotEnoughRightException.class)
    ResponseEntity<Object> NotEnoughRightHandler (NotEnoughRightException ex) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("message", ex.getMessage());

        return new ResponseEntity<Object>(body, HttpStatus.FORBIDDEN);
    }
}
