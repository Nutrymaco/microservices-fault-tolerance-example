package com.nutrymaco.reservation.resource;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.nutrymaco.reservation.repository.HotelRepository;
import com.nutrymaco.reservation.model.Hotel;

/**
 * HotelResource
 */
@Path("/internal/api/v1/hotels")
public class HotelResource {

	@Inject
	HotelRepository hotelRepository;

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<Hotel> getHotels() {
		System.out.println("getHotels()");
		return hotelRepository.getHotels();
	}
	
}
