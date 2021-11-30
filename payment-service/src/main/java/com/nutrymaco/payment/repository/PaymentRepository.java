package com.nutrymaco.payment.repository;

import java.util.UUID;

import com.nutrymaco.payment.model.Payment;

/**
 * PaymentRepository
 */
public interface PaymentRepository {

	Payment createPayment(Payment payment);

	void cancelPayment(UUID paymentUid);

	Payment getPayment(UUID paymentUid);
	
}
