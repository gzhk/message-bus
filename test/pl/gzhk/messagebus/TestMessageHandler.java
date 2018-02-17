package pl.gzhk.messagebus;

import pl.gzhk.messagebus.handler.MessageHandler;

import java.util.ArrayList;
import java.util.List;

public final class TestMessageHandler implements MessageHandler<String> {

    public final List<String> handledValues = new ArrayList<>();

    @Override
    public void handle(String message) {
        handledValues.add(message);
    }

}
