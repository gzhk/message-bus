package pl.gzhk.messagebus.middleware;

import pl.gzhk.messagebus.MessageHandlingException;

public interface MessageBusMiddleware {

    <M> void next(M message, Callable<M> nextMiddlewareCallable) throws MessageHandlingException;

}
