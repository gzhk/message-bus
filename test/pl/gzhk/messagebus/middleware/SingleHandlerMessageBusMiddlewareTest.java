package pl.gzhk.messagebus.middleware;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import pl.gzhk.messagebus.MessageHandlerNotFound;
import pl.gzhk.messagebus.MessageHandlingException;
import pl.gzhk.messagebus.TestMessageHandler;
import pl.gzhk.messagebus.handler.MessageHandler;

import java.util.Collections;
import java.util.HashMap;

class SingleHandlerMessageBusMiddlewareTest {

    @Test
    void delegatesHandlingOfMessageToProperHandler() throws MessageHandlingException {
        HashMap<Class<?>, MessageHandler<?>> messageHandlers = new HashMap<>();
        TestMessageHandler testMessageHandler = new TestMessageHandler();
        messageHandlers.put(String.class, testMessageHandler);

        new SingleHandlerMessageBusMiddleware(messageHandlers).next("text message", m -> {});

        Assertions.assertThat(testMessageHandler.handledValues).isEqualTo(Collections.singletonList("text message"));
    }

    @Test
    void throwsExceptionWhenMessageHandlerDoesNotExists() {
        Assertions
            .assertThatExceptionOfType(MessageHandlerNotFound.class)
            .isThrownBy(
                () -> new SingleHandlerMessageBusMiddleware(new HashMap<>()).next("text message", m -> {})
            );
    }
}
