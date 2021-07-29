package com.tanhua.server.exception;

import com.tanhua.commons.exception.TanHuaException;
import com.tanhua.domain.vo.ErrorResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class ExceptionAdvice {

    @ExceptionHandler(TanHuaException.class)
    public ResponseEntity handleTanHuaException(TanHuaException ex) {
        if (null != ex.getErrData()) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getErrData());
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                ErrorResult.error("000009",ex.getMessage())
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity handlerException(Exception ex){
        ex.printStackTrace();
        log.error("发生未知异常",ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ErrorResult.error());
    }

}
