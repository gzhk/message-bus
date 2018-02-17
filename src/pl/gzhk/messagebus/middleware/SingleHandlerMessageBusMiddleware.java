package pl.gzhk.messagebus.middleware;

import pl.gzhk.messagebus.MessageHandlerNotFound;
import pl.gzhk.messagebus.MessageHandlingException;
import pl.gzhk.messagebus.handler.MessageHandler;

import java.util.Map;

public final class SingleHandlerMessageBusMiddleware implements MessageBusMiddleware {

    private final Map<Class<?>, MessageHandler<?>> messageHandlers;

    public SingleHandlerMessageBusMiddleware(Map<Class<?>, MessageHandler<?>> messageHandlers) {
        this.messageHandlers = messageHandlers;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <M> void next(M message, Callable<M> nextMiddlewareCallable) throws MessageHandlingException {
        if (!messageHandlers.containsKey(message.getClass())) {
            throw new MessageHandlerNotFound(message.getClass());
        }

        MessageHandler<M> messageHandler = (MessageHandler<M>) messageHandlers.get(message.getClass());
        messageHandler.handle(message);
    }

}
