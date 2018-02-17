package pl.gzhk.messagebus.middleware;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import pl.gzhk.messagebus.MessageHandlingException;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

class FinishHandlingMessageBeforeNextMessageBusMiddlewareTest {

    @Test
    void finishesHandlingMessageBeforeHandlingNext() throws Exception {
        ExecutorService executorService = Executors.newFixedThreadPool(2);
        ConcurrentLinkedQueue<String> messages = new ConcurrentLinkedQueue<>();

        MessageBusMiddleware middleware = new FinishHandlingMessageBeforeNextMessageBusMiddleware();

        executorService.submit(callMiddleware(middleware, "first", messages, 200));
        Thread.sleep(100);
        executorService.submit(callMiddleware(middleware, "second", messages, 100));
        executorService.shutdown();
        executorService.awaitTermination(1, TimeUnit.SECONDS);

        Assertions
            .assertThat(messages.toArray())
            .isEqualTo(new String[]{"first", "second"});
    }

    private static Runnable callMiddleware(
        MessageBusMiddleware middleware,
        String message,
        ConcurrentLinkedQueue<String> messages,
        int sleep
    ) {
        return () -> {
            try {
                middleware.next(message, m -> {
                    try {
                        Thread.sleep(sleep);
                        messages.add(m);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                });
            } catch (MessageHandlingException e) {
                throw new RuntimeException(e.getMessage(), e);
            }
        };
    }

}
