package org.server.assistant.shutdown;

public interface ShutdownExecutor extends IShutdown {

  void addShutdown(IShutdown shutdown);

}
