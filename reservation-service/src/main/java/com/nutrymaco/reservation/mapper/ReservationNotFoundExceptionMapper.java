package com.nutrymaco.reservation.mapper;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import com.nutrymaco.reservation.exception.ReservationNotFoundException;

/**
 * ReservationNotFoundExceptionMapper
 */
@Provider
public class ReservationNotFoundExceptionMapper implements ExceptionMapper<ReservationNotFoundException> {

	@Override
	public Response toResponse(ReservationNotFoundException exception) {
		return Response.status(404)
			    .entity(exception.getMessage())
				.build();
	}
	
}
