package com.nutrymaco.loaylty.repository;

import com.nutrymaco.loaylty.model.Loyalty;
import com.nutrymaco.loaylty.model.LoyaltyStatus;

/**
 * LoyaltyRepository
 */
public interface LoyaltyRepository {

	LoyaltyStatus getLoyaltyStatus(String username);

	void increaseReservationCount(String username);

	void decreaseReservationCount(String username);

	Loyalty getLoyalty(String username);
	
}
