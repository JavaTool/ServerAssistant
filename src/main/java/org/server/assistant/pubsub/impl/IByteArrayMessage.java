package org.server.assistant.pubsub.impl;

public interface IByteArrayMessage {

  String getIp();

  int getMessageId();

  byte[] getBytes();

}
