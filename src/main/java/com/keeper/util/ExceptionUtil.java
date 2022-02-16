package com.keeper.util;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class ExceptionUtil {

    private ExceptionUtil() {

    }

    public static ResponseStatusException badRequestException(String message) {
        return new ResponseStatusException(HttpStatus.BAD_REQUEST, message);
    }

    public static ResponseStatusException notFoundException(String message) {
        return new ResponseStatusException(HttpStatus.NOT_FOUND, message);
    }
}
