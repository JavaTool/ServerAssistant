package org.server.assistant.io.proto;

import org.server.assistant.io.message.IMessage;

public interface IErrorHandler {

  IMessage createErrorResponse(int messageId, int serial, String error);

}
