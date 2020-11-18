package org.server.assistant.io.netty.server.tcp;

import io.netty.buffer.ByteBuf;
import org.server.assistant.io.message.IConnectHandler;
import org.server.assistant.secret.IEncrypt;

final class NettyTcpHandler extends AbstractNettyTcpHandler {

  public NettyTcpHandler(IConnectHandler messageHandler, IEncrypt encrypt) {
    super(messageHandler, encrypt);
  }

  @Override
  protected boolean check(ByteBuf msg) {
    return true;
  }

}
