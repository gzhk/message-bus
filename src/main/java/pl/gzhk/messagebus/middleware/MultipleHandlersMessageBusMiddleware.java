package pl.gzhk.messagebus.middleware;

import pl.gzhk.messagebus.MessageHandlingException;
import pl.gzhk.messagebus.handler.MessageHandler;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public final class MultipleHandlersMessageBusMiddleware implements MessageBusMiddleware {

    private final Map<Class<?>, Set<MessageHandler<?>>> handlers;

    public MultipleHandlersMessageBusMiddleware(Map<Class<?>, Set<MessageHandler<?>>> handlers) {
        this.handlers = handlers;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <M> void next(M message, Callable<M> nextMiddlewareCallable) throws MessageHandlingException {
        handlers
            .getOrDefault(message.getClass(), new HashSet<>())
            .forEach(messageHandler -> {
                MessageHandler<M> handler = (MessageHandler<M>) messageHandler;
                handler.handle(message);
            });
    }

}
