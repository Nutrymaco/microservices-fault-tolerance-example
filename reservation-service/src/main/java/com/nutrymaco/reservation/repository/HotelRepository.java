package com.nutrymaco.reservation.repository;

import java.util.List;
import java.util.UUID;

import com.nutrymaco.reservation.model.Hotel;

/**
 * HotelRepository
 */
public interface HotelRepository {
	
	List<Hotel> getHotels();

	Hotel getHotel(UUID hotelUid);
	
}
