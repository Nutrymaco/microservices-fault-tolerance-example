package com.nutrymaco.gateway.service;

import java.util.UUID;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.GET;
import javax.ws.rs.PATCH;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.nutrymaco.gateway.model.Payment;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

/**
 * PaymentService - http client to Payments microservice
 */
@Path("/internal/api/v1/payments")
@RegisterRestClient(configKey = "payment-service")
@ApplicationScoped
public interface PaymentServiceClient {

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	Payment createPayment(Payment payment);

	@Path("/{paymentUid}/cancel")
	@PATCH
	void cancelPayment(@PathParam("paymentUid") UUID paymentUid);


	@GET
	@Path("/{paymentUid}")
	@Produces(MediaType.APPLICATION_JSON)
	Payment getPayment(@PathParam("paymentUid") UUID paymentUid);
}
