package com.nutrymaco.gateway.exception;

public class ServiceNotAvailableException extends RuntimeException {

    public ServiceNotAvailableException(String serviceName) {
        super("service : %s not available".formatted(serviceName));
    }
}
