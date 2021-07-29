package com.tanhua.server.exception;

import com.tanhua.commons.exception.TanhuaException;
import com.tanhua.domain.vo.ErrorResult;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionAdvice {

    @ExceptionHandler(TanhuaException.class)
    public ResponseEntity tanhuaExceptionAdvice(TanhuaException e){
        if (e.getErr()!=null) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getErr());
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity exceptionHandler(Exception e) {
        e.printStackTrace();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ErrorResult.error());
    }

}
