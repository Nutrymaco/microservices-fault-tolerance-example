package com.nutrymaco.loaylty.resource;

import com.nutrymaco.loaylty.model.LoyaltyStatus;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;

/**
 * LoyaltyTest
 */
@QuarkusTest
public class LoyaltyTest {

	@Test
	public void testBronzeLoyaltyStatusResolving() {
		Assertions.assertEquals(LoyaltyStatus.BRONZE, LoyaltyStatus.resolveByReservationCount(0));
		Assertions.assertEquals(LoyaltyStatus.BRONZE, LoyaltyStatus.resolveByReservationCount(4));
	}

	@Test
	public void testSilverLoyaltyStatusResolving() {
		Assertions.assertEquals(LoyaltyStatus.SILVER, LoyaltyStatus.resolveByReservationCount(10));
		Assertions.assertEquals(LoyaltyStatus.SILVER, LoyaltyStatus.resolveByReservationCount(19));
	}


	@Test
	public void testGoldLoyaltyStatusResolving() {
		Assertions.assertEquals(LoyaltyStatus.GOLD, LoyaltyStatus.resolveByReservationCount(20));
		Assertions.assertEquals(LoyaltyStatus.GOLD, LoyaltyStatus.resolveByReservationCount(128));
	}
}
