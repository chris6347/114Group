package com.tanhua.commons.exception;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TanhuaException extends RuntimeException{

    private Object err;

    public TanhuaException(Object err) {
        this.err=err;
    }

    public TanhuaException(String message) {
        super(message);
    }

}
