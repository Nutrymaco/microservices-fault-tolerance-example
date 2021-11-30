package com.nutrymaco.gateway.exception;

import java.util.Arrays;

import static java.util.stream.Collectors.joining;

public class ServiceNotAvailableException extends RuntimeException {

    public ServiceNotAvailableException(String serviceName) {
        super("%s unavailable".formatted(Arrays.stream(serviceName.split("-"))
                .map(ServiceNotAvailableException::capitalize)
                .collect(joining(" "))));
    }

    private static String capitalize(String s) {
        return String.valueOf(s.charAt(0)).toUpperCase() + s.substring(1);
    }
}
