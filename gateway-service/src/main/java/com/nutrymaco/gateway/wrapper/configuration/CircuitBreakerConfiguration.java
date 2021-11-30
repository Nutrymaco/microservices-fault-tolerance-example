package com.nutrymaco.gateway.wrapper.configuration;

import com.nutrymaco.gateway.service.LoyaltyServiceClient;
import com.nutrymaco.gateway.service.PaymentServiceClient;
import com.nutrymaco.gateway.service.ReservationServiceClient;
import com.nutrymaco.gateway.service.annotation.LoyaltyService;
import com.nutrymaco.gateway.service.annotation.PaymentService;
import com.nutrymaco.gateway.service.annotation.ReservationService;
import com.nutrymaco.gateway.wrapper.CircuitBreaker;
import com.nutrymaco.gateway.wrapper.RequestScheduler;
import com.nutrymaco.gateway.wrapper.Status;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.ws.rs.Produces;
import java.util.UUID;
import java.util.function.Supplier;

@ApplicationScoped
public class CircuitBreakerConfiguration {

    @ConfigProperty(name = "circuit-breaker.try-count", defaultValue = "3")
    int tryCount;

    @ConfigProperty(name = "circuit-breaker.timeout", defaultValue = "10")
    int timeout;

    @Inject
    @RestClient
    ReservationServiceClient reservationService;

    @Inject
    @RestClient
    PaymentServiceClient paymentService;

    @Inject
    @RestClient
    LoyaltyServiceClient loyaltyService;

    @Singleton
    @Produces
    @LoyaltyService
    public CircuitBreaker loyaltyCircuitBreaker(RequestScheduler requestScheduler) {
        return new CircuitBreaker(tryCount, timeout, statusWrapper(() -> loyaltyService.getLoyalty("Test Max")), requestScheduler);
    }

    @Singleton
    @Produces
    @PaymentService
    public CircuitBreaker paymentCircuitBreaker(RequestScheduler requestScheduler) {
        return new CircuitBreaker(tryCount, timeout, statusWrapper(() -> paymentService.getPayment(UUID.randomUUID())), requestScheduler);
    }

    @Singleton
    @Produces
    @ReservationService
    public CircuitBreaker reservationCircuitBreaker(RequestScheduler requestScheduler) {
        return new CircuitBreaker(tryCount, timeout, statusWrapper(() -> reservationService.getHotels()), requestScheduler);
    }

    @Singleton
    @Produces
    public RequestScheduler requestScheduler() {
        return new RequestScheduler(timeout);
    }

    private Supplier<Status> statusWrapper(Runnable request) {
        return () -> {
            try {
                request.run();
                return Status.OK;
            } catch (Exception ignored) {
                return Status.BAD;
            }
        };
    }
}
