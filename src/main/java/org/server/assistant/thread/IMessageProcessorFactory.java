package org.server.assistant.thread;

import java.util.function.Consumer;

public interface IMessageProcessorFactory<T> {

  Consumer<T> create(String name);

}
