package org.server.assistant.io.netty.server;

import org.server.assistant.io.message.IConnectHandler;
import org.server.assistant.io.message.IMessageIdTransform;
import org.server.assistant.secret.IEncrypt;

public interface INettyServerConfig {

  IConnectHandler getConnectHandler();

  IEncrypt getEncrypt();

  IMessageIdTransform getMessageIdTransform();

  int getParentThreadNum();

  int getChildThreadNum();

  int getPort();

  String getIp();

  long getReaderIdleTime();

  long getWriterIdleTime();

  long getAllIdleTime();

  int getSoBacklog();

}
