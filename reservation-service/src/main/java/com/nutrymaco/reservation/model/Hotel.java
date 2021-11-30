package com.nutrymaco.reservation.model;

import java.util.UUID;

/**
 * Hotel
 */
public class Hotel {
	
	private int id;
	private UUID hotelUid;
	private String name;
	private String country;
	private String city;
	private String address;
	private int stars;
	private int price;
	
	public Hotel(int id, UUID hotelUid, String name, String country, String city, String address, int stars,
			int price) {
		this.id = id;
		this.hotelUid = hotelUid;
		this.name = name;
		this.country = country;
		this.city = city;
		this.address = address;
		this.stars = stars;
		this.price = price;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public UUID getHotelUid() {
		return hotelUid;
	}
	public void setHotelUid(UUID hotelUid) {
		this.hotelUid = hotelUid;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public int getStars() {
		return stars;
	}
	public void setStars(int stars) {
		this.stars = stars;
	}
	public int getPrice() {
		return price;
	}
	public void setPrice(int price) {
		this.price = price;
	}

	
}
