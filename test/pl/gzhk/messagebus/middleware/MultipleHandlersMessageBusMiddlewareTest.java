package pl.gzhk.messagebus.middleware;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import pl.gzhk.messagebus.MessageHandlingException;
import pl.gzhk.messagebus.TestMessageHandler;
import pl.gzhk.messagebus.handler.MessageHandler;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

class MultipleHandlersMessageBusMiddlewareTest {

    @Test
    void notifiesAllSubscribedHandlers() throws MessageHandlingException {
        HashMap<Class<?>, Set<MessageHandler<?>>> handlersMap = new HashMap<>();
        HashSet<MessageHandler<?>> stringHandlers = new HashSet<>();
        TestMessageHandler firstHandler = new TestMessageHandler();
        TestMessageHandler secondHandler = new TestMessageHandler();
        stringHandlers.add(firstHandler);
        stringHandlers.add(secondHandler);

        handlersMap.put(String.class, stringHandlers);

        new MultipleHandlersMessageBusMiddleware(handlersMap).next("handle me", m -> {});

        Assertions.assertThat(firstHandler.handledValues).isNotEmpty();
        Assertions.assertThat(firstHandler.handledValues.get(0)).isEqualTo("handle me");

        Assertions.assertThat(secondHandler.handledValues).isNotEmpty();
        Assertions.assertThat(secondHandler.handledValues.get(0)).isEqualTo("handle me");
    }

    @Test
    void doesNotThrowsExceptionIfThereIsNoHandler() throws MessageHandlingException {
        new MultipleHandlersMessageBusMiddleware(new HashMap<>()).next("handle me", m -> {});
    }
}
