package com.nutrymaco.gateway.wrapper;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

public class RequestScheduler {

    private final int timeout;
    private final ScheduledThreadPoolExecutor requestExecutor = new ScheduledThreadPoolExecutor(1);

    public RequestScheduler(int timeout) {
        this.timeout = timeout;
    }

    public void tryExecuteUntilSuccess(Supplier<Status> request, Runnable successAction) {
        System.out.println("schedule request");
        requestExecutor.schedule(
                () -> {
                    System.out.println("try execute scheduled request");
                    switch (request.get()) {
                        case OK -> successAction.run();
                        case BAD -> tryExecuteUntilSuccess(request, successAction);
                    }
                },
                timeout,
                TimeUnit.SECONDS
        );
    }

}
