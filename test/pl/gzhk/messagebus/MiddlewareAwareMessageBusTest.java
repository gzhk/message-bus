package pl.gzhk.messagebus;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import pl.gzhk.messagebus.middleware.Callable;
import pl.gzhk.messagebus.middleware.MessageBusMiddleware;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class MiddlewareAwareMessageBusTest {

    @Test
    void executesMiddlewaresInOrder() throws MessageHandlingException {
        List<String> executionOrder = new ArrayList<>();
        List<MessageBusMiddleware> middlewares = new ArrayList<>();
        middlewares.add(
            new MessageBusMiddleware() {
                @Override
                public <M> void next(M message, Callable<M> nextMiddlewareCallable) throws MessageHandlingException {
                    executionOrder.add("first " + message);
                    nextMiddlewareCallable.call(message);
                }
            }
        );
        middlewares.add(
            new MessageBusMiddleware() {
                @Override
                public <M> void next(M message, Callable<M> nextMiddlewareCallable) throws MessageHandlingException {
                    executionOrder.add("second " + message);
                    nextMiddlewareCallable.call(message);
                }
            }
        );

        MiddlewareAwareMessageBus messageBus = new MiddlewareAwareMessageBus(middlewares);
        messageBus.handle("message");

        Assertions.assertThat(executionOrder).isEqualTo(Arrays.asList("first message", "second message"));
    }

}
