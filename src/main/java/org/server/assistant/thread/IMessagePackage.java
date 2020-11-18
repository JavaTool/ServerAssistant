package org.server.assistant.thread;

import org.server.assistant.io.message.IMessageSender;

public interface IMessagePackage {

  int getMessageId();

  int getSerial();

  byte[] getData();

  IMessageSender getMessageSender();

}
