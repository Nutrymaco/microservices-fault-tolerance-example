package com.nutrymaco.payment.resource;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PATCH;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import java.util.UUID;

import com.nutrymaco.payment.model.Payment;
import com.nutrymaco.payment.repository.PaymentRepository;

@Path("/internal/api/v1/payments")
public class PaymentResource {

	@Inject
	PaymentRepository paymentRepository;
	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public Payment createPayment(Payment payment) {
		System.out.println("createPayment()");
		return paymentRepository.createPayment(payment);
	}

	@Path("/{paymentUid}/cancel")
	@PATCH
	public void cancelPayment(@PathParam("paymentUid") UUID paymentUid) {
		System.out.println("cancelPayment()");
		paymentRepository.cancelPayment(paymentUid);
	}

	@GET
	@Path("/{paymentUid}")
	@Produces(MediaType.APPLICATION_JSON)
	public Payment getPayment(@PathParam("paymentUid") UUID paymentUid) {
		return paymentRepository.getPayment(paymentUid);
	}
	
}
