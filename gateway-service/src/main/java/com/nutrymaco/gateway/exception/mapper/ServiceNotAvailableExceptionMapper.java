package com.nutrymaco.gateway.exception.mapper;

import com.nutrymaco.gateway.exception.ServiceNotAvailableException;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.util.Map;

@Provider
public class ServiceNotAvailableExceptionMapper implements ExceptionMapper<ServiceNotAvailableException> {
    @Override
    public Response toResponse(ServiceNotAvailableException exception) {
        return Response.status(503)
                .entity(Map.of("message", exception.getMessage()))
                .build();
    }
}
