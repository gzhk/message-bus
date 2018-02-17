package pl.gzhk.messagebus;

public interface MessageBus {

    <T> void handle(T message) throws MessageHandlingException;

}
