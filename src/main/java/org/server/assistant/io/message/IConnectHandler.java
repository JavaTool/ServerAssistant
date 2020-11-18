package org.server.assistant.io.message;

import org.server.assistant.io.dispatch.ISender;

public interface IConnectHandler extends IMessageHandler {

  void discontected(ISender sender) throws Exception;

}
