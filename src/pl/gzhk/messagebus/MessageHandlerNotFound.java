package pl.gzhk.messagebus;

public final class MessageHandlerNotFound extends MessageHandlingException {

    public MessageHandlerNotFound(Class<?> clazz) {
        super(String.format("Message handler for class %s not found.", clazz.toString()));
    }
}
