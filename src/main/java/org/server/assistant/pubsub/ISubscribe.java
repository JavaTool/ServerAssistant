package org.server.assistant.pubsub;

public interface ISubscribe {

  void onMessage(String channel, Object message);

  int getTimeout();

}
