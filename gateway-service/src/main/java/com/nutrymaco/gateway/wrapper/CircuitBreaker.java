package com.nutrymaco.gateway.wrapper;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;

public class CircuitBreaker {

    private final int tryCount;
    private int currentTry = 0;
    private final int timeout;
    private final Supplier<Status> healthCheck;
    private final RequestScheduler requestScheduler;

    private final AtomicBoolean serviceIsReady = new AtomicBoolean(true);


    public CircuitBreaker(int tryCount, int timeout, Supplier<Status> healthCheck, RequestScheduler requestScheduler) {
        this.tryCount = tryCount;
        this.timeout = timeout;
        this.healthCheck = healthCheck;
        this.requestScheduler = requestScheduler;
    }

    public <T> T tryOrGetDefault(Supplier<T> request, Supplier<T> defaultValue) {
        T answer = null;
        if (currentTry == tryCount) {
            if (!serviceIsReady.get()) {
                System.out.println("service not available, return fallback");
                return defaultValue.get();
            } else {
                currentTry = 0;
            }
        }
        try {
            answer = request.get();
            currentTry = 0;
        } catch (Exception ignored) {
            currentTry++;
        }

        if (answer == null) {
            serviceIsReady.set(false);
            scheduleHealthCheck();
            answer = defaultValue.get();
        }

        return answer;
    }

    public <T> Optional<T> tryOrGetEmpty(Supplier<T> request) {
        return tryOrGetDefault(() -> Optional.of(request.get()), Optional::empty);
    }

    public <T> List<T> tryOrGetEmptyList(Supplier<List<T>> request) {
        return tryOrGetDefault(request, List::of);
    }

    public Status tryAndGetStatus(Runnable voidRequest) {
        return tryOrGetDefault(
                () -> {
                    voidRequest.run();
                    return Status.OK;
                    },
                () -> Status.BAD);
    }

    private void scheduleHealthCheck() {
        requestScheduler.tryExecuteUntilSuccess(() -> {
            System.out.println("execute health check");
            return healthCheck.get();
        }, () -> serviceIsReady.set(true));
    }


}
