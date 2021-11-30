package com.nutrymaco.gateway.dto;

import com.nutrymaco.gateway.model.Status;

/**
 * PaymentDto
 */
public class PaymentDto {
	
	private final Status status;
	private final int price;

	public PaymentDto(Status status, int price) {
		this.status = status;
		this.price = price;
	}

	public Status getStatus() {
		return status;
	}

	public int getPrice() {
		return price;
	}
	
}
