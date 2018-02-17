package pl.gzhk.messagebus;

import pl.gzhk.messagebus.middleware.Callable;
import pl.gzhk.messagebus.middleware.MessageBusMiddleware;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public final class MiddlewareAwareMessageBus implements MessageBus {

    private final List<MessageBusMiddleware> middlewares;

    public MiddlewareAwareMessageBus(List<MessageBusMiddleware> middlewares) {
        this.middlewares = middlewares;
    }

    public <T> void handle(T message) throws MessageHandlingException {
        createCallableForMiddleware(new LinkedList<>(middlewares)).call(message);
    }

    private static <T> Callable<T> createCallableForMiddleware(Queue<MessageBusMiddleware> middlewares) {
        if (middlewares.isEmpty()) {
            return m -> {};
        }

        return m -> middlewares.remove().next(m, createCallableForMiddleware(new LinkedList<>(middlewares)));
    }

}
