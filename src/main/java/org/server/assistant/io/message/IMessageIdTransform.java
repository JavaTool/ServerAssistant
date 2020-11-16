package org.server.assistant.io.message;

public interface IMessageIdTransform {

  int transform(String messageId);

  String transform(int messageId);

}
