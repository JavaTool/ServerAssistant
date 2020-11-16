package org.server.assistant.thread;

import java.util.function.Consumer;

final class SynchronizedMessageProcessor implements Consumer<IMessagePackage> {

  private final IMessagePackageHandler handler;

  public SynchronizedMessageProcessor(IMessagePackageHandler handler) {
    this.handler = handler;
  }

  @Override
  public synchronized void accept(IMessagePackage msg) {
    handler.handle(msg);
  }

}
