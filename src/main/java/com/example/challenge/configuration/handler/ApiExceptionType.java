package com.example.challenge.configuration.handler;

import lombok.Data;
import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import static org.springframework.http.HttpStatus.*;

@Data
public final class ApiExceptionType {
    private static final Map<Integer, ApiExceptionType> REGISTRY = new HashMap<>();

    public static final ApiExceptionType SYSTEM_ERROR = new ApiExceptionType(INTERNAL_SERVER_ERROR, "SYSTEM_ERROR", 0);
    public static final ApiExceptionType NOT_IMPLEMENTED = new ApiExceptionType(INTERNAL_SERVER_ERROR, "NOT_IMPLEMENTED", 1);

    public static final ApiExceptionType INVALID_DATA = new ApiExceptionType(BAD_REQUEST, "INVALID_DATA", 1000);
    public static final ApiExceptionType MALFORMED_JSON = new ApiExceptionType(BAD_REQUEST, "INVALID_DATA", 1001);
    public static final ApiExceptionType INVALID_JSON = new ApiExceptionType(BAD_REQUEST, "INVALID_DATA", 1002);
    public static final ApiExceptionType MISSING_PARAM = new ApiExceptionType(BAD_REQUEST, "INVALID_DATA", 1003);
    public static final ApiExceptionType PARAM_TYPE_MISMATCH = new ApiExceptionType(BAD_REQUEST, "INVALID_DATA", 1004);

    public static final ApiExceptionType GATEWAY_ERROR = new ApiExceptionType(BAD_GATEWAY, "GATEWAY_ERROR", 2000);
    public static final ApiExceptionType GATEWAY_TIMEOUT = new ApiExceptionType(BAD_GATEWAY, "GATEWAY_ERROR", 2001);

    public static final ApiExceptionType NOT_FOUND = new ApiExceptionType(HttpStatus.NOT_FOUND, "NOT_FOUND", 3000);

    public static final ApiExceptionType FORBIDDEN = new ApiExceptionType(HttpStatus.FORBIDDEN, "FORBIDDEN", 4000);

    public static final ApiExceptionType CANT_DELETE = new ApiExceptionType(HttpStatus.CONFLICT, "NOT_DONE", 5000);
    public static final ApiExceptionType ALREADY_EXISTS = new ApiExceptionType(HttpStatus.CONFLICT, "NOT_DONE", 5001);
    public static final ApiExceptionType UNAVAILABLE = new ApiExceptionType(HttpStatus.CONFLICT, "NOT_DONE", 5002);
    public static final ApiExceptionType CANT_INSERT = new ApiExceptionType(HttpStatus.CONFLICT, "NOT_DONE", 5003);
    public static final ApiExceptionType CANT_MODIFY = new ApiExceptionType(HttpStatus.CONFLICT, "NOT_DONE", 5004);

    public static final ApiExceptionType WRONG_CREDENTIALS = new ApiExceptionType(HttpStatus.UNAUTHORIZED, "WRONG_CREDENTIALS", 6000);
    public static final ApiExceptionType AUTH_EXPIRED = new ApiExceptionType(HttpStatus.UNAUTHORIZED, "AUTH_EXPIRED", 6001);
    public static final ApiExceptionType AUTH_REJECTED = new ApiExceptionType(HttpStatus.UNAUTHORIZED, "AUTH_REJECTED", 6002);
    public static final ApiExceptionType UNAUTHORIZED = new ApiExceptionType(HttpStatus.UNAUTHORIZED, "UNAUTHORIZED", 6003);

    private final HttpStatus status;
    private final String code;
    private final int subcode;

    public ApiExceptionType(final HttpStatus status, final String code, final int subcode) {
        this.status = status;
        this.code = code;
        this.subcode = subcode;
        REGISTRY.put(subcode, this);
    }

    public String getMessage() {
        ResourceBundle bundle = ResourceBundle.getBundle("errors");
        return bundle.getString(Integer.toString(this.subcode));
    }
}