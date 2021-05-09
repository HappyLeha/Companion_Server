package com.example.demo.advice;
import com.example.demo.exception.ValidationFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import javax.validation.ConstraintViolationException;
import java.util.LinkedHashMap;
import java.util.Map;

@ControllerAdvice
public class ValidationFailure {

    @ResponseBody
    @ExceptionHandler(MethodArgumentNotValidException.class)
    ResponseEntity<Object> validationFailureHandler (MethodArgumentNotValidException ex) {
        String resultMessage = "";

        for (ObjectError error: ex.getAllErrors()) {
            resultMessage += error.getDefaultMessage() + "\n";
        }
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("message", resultMessage);

        return new ResponseEntity<Object>(body, HttpStatus.BAD_REQUEST);
    }

    @ResponseBody
    @ExceptionHandler(ConstraintViolationException.class)
    ResponseEntity<Object> validationFailureHandler (ConstraintViolationException ex) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("message", ex.getMessage());

        return new ResponseEntity<Object>(body, HttpStatus.BAD_REQUEST);
    }

    @ResponseBody
    @ExceptionHandler(ValidationFailureException.class)
    ResponseEntity<Object> validationFailureHandler (ValidationFailureException ex) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("message", ex.getMessage());

        return new ResponseEntity<Object>(body, HttpStatus.BAD_REQUEST);
    }
}
