package org.server.assistant.io.message;

public interface IMessage {

  int getMessageId();

  int getSerial();

  IMessage setSerial(int serial);

  byte[] toByteArray();

  long getReceiveTime();

  int getThreadId();

  IMessage setThreadId(int threadId);

  IMessage setReceiveTime(long receiveTime);

}
