package org.server.assistant.thread;

import java.util.function.Consumer;

public final class SynchronizedMessageProcessorFactory implements IMessageProcessorFactory<IMessagePackage> {

  private final IMessagePackageHandler handler;

  public SynchronizedMessageProcessorFactory(IMessagePackageHandler handler) {
    this.handler = handler;
  }

  @Override
  public Consumer<IMessagePackage> create(String name) {
    return new SynchronizedMessageProcessor(handler);
  }

}
