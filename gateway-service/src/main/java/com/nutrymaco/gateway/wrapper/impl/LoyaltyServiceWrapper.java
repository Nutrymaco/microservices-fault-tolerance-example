package com.nutrymaco.gateway.wrapper.impl;

import com.nutrymaco.gateway.model.Loyalty;
import com.nutrymaco.gateway.service.LoyaltyServiceClient;
import com.nutrymaco.gateway.service.annotation.LoyaltyService;
import com.nutrymaco.gateway.wrapper.CircuitBreaker;
import com.nutrymaco.gateway.wrapper.Status;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.Optional;

@ApplicationScoped
public class LoyaltyServiceWrapper {

    @Inject
    @RestClient
    LoyaltyServiceClient loyaltyServiceClient;

    @LoyaltyService
    @Inject
    CircuitBreaker circuitBreaker;

    public Optional<Loyalty> getLoyalty(String username) {
        return circuitBreaker.tryOrGetEmpty(() -> loyaltyServiceClient.getLoyalty(username));
    }

    public Status increaseReservationCount(String username) {
        return circuitBreaker.tryAndGetStatus(() -> loyaltyServiceClient.increaseReservationCount(username));
    }

    public Status decreaseReservationCount(String username) {
        return circuitBreaker.tryAndGetStatus(() -> loyaltyServiceClient.decreaseReservationCount(username));
    }

}
