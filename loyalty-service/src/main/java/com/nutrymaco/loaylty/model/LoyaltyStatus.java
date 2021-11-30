package com.nutrymaco.loaylty.model;

/**
 * LoyaltyStatus
 */
public enum LoyaltyStatus {
	
	BRONZE(5), SILVER(7), GOLD(10);

	public final int discount;

	private LoyaltyStatus(int discount) {
		this.discount = discount;
	}

	public static LoyaltyStatus resolveByReservationCount(int reservationCount) {
		if (reservationCount < 10) {
			return BRONZE;
		} else if (reservationCount < 20) {
			return SILVER;
		} else {
			return GOLD;
		}
	}
	
}
