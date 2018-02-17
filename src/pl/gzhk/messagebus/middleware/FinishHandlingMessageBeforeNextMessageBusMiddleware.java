package pl.gzhk.messagebus.middleware;

import pl.gzhk.messagebus.MessageHandlingException;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;

public final class FinishHandlingMessageBeforeNextMessageBusMiddleware implements MessageBusMiddleware {

    private final ConcurrentLinkedQueue<Object> messages;
    private final AtomicBoolean isHandling;

    public FinishHandlingMessageBeforeNextMessageBusMiddleware() {
        this.messages = new ConcurrentLinkedQueue<>();
        this.isHandling = new AtomicBoolean(false);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <M> void next(M message, Callable<M> nextMiddlewareCallable) throws MessageHandlingException {
        messages.add(message);

        if (isHandling.compareAndSet(false, true)) {
            while (!messages.isEmpty()) {
                try {
                    nextMiddlewareCallable.call((M) messages.poll());
                } catch (MessageHandlingException e) {
                    isHandling.set(false);
                    throw e;
                }
            }

            isHandling.set(false);
        }
    }

}
