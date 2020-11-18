package org.server.assistant.io.message;

public interface ISynchronizedMessageSender {

  void send(IMessage message, IMessageHandler messageHandler);

  void send(int messageId, int serial, long receiveTime, IMessageHandler messageHandler);

}
