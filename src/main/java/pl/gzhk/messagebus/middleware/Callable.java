package pl.gzhk.messagebus.middleware;

import pl.gzhk.messagebus.MessageHandlingException;

@FunctionalInterface
public interface Callable<T> {

    void call(T message) throws MessageHandlingException;

}
