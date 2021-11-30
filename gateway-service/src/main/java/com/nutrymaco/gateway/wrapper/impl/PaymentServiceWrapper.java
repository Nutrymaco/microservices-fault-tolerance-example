package com.nutrymaco.gateway.wrapper.impl;

import com.nutrymaco.gateway.model.Payment;
import com.nutrymaco.gateway.service.PaymentServiceClient;
import com.nutrymaco.gateway.service.annotation.PaymentService;
import com.nutrymaco.gateway.wrapper.CircuitBreaker;
import com.nutrymaco.gateway.wrapper.Status;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
public class PaymentServiceWrapper {

    @PaymentService
    @Inject
    CircuitBreaker circuitBreaker;

    @Inject
    @RestClient
    PaymentServiceClient paymentServiceClient;

    public Optional<Payment> createPayment(Payment payment) {
        return circuitBreaker.tryOrGetEmpty(() -> paymentServiceClient.createPayment(payment));
    }

    public Status cancelPayment(UUID paymentUid) {
        return circuitBreaker.tryAndGetStatus(() -> paymentServiceClient.cancelPayment(paymentUid));
    }

    public Optional<Payment> getPayment(UUID paymentUid) {
        return circuitBreaker.tryOrGetEmpty(() -> paymentServiceClient.getPayment(paymentUid));
    }

}
