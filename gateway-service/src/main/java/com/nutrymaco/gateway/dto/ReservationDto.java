package com.nutrymaco.gateway.dto;

import java.time.OffsetDateTime;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * ReservationDto
 */
public class ReservationDto {
	
	private UUID hotelUid;
	
	private String startDate;
	
	private String endDate;

	public ReservationDto(UUID hotelUid, String startDate, String endDate) {
		this.hotelUid = hotelUid;
		this.startDate = startDate;
		this.endDate = endDate;
	}
	public UUID getHotelUid() {
		return hotelUid;
	}
	public void setHotelUid(UUID hotelUid) {
		this.hotelUid = hotelUid;
	}
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	
}
