package pl.gzhk.messagebus.handler;

@FunctionalInterface
public interface MessageHandler<T> {

    void handle(T message);

}
