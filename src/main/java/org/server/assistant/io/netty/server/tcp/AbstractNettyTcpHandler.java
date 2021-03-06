package org.server.assistant.io.netty.server.tcp;

import java.util.UUID;

import org.server.assistant.io.dispatch.ISender;
import org.server.assistant.io.message.IConnectHandler;
import org.server.assistant.io.netty.NettyTcpSender;
import org.server.assistant.secret.IEncrypt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.Attribute;
import io.netty.util.AttributeKey;

/**
 * TCP处理器
 * @author 	fuhuiyuan
 */
public abstract class AbstractNettyTcpHandler extends SimpleChannelInboundHandler<ByteBuf> {

  protected static final AttributeKey<ISender> SENDER_KEY = AttributeKey.valueOf("SENDER_KEY");

  protected static final AttributeKey<String> SESSION_ID_KEY = AttributeKey.valueOf(ISender.SESSION_ID);

  protected static final Logger log = LoggerFactory.getLogger(AbstractNettyTcpHandler.class);

  protected static final String LOG_ACTIVE = "[Coming Active]IP:{}";

  protected static final String LOG_INACTIVE = "[Coming Out]IP:{}, session{}";
  /**消息处理器*/
  protected final IConnectHandler messageHandler;

  protected final IEncrypt encrypt;

  public AbstractNettyTcpHandler(IConnectHandler messageHandler, IEncrypt encrypt) {
    this.messageHandler = messageHandler;
    this.encrypt = encrypt;
  }

  @Override
  public void channelActive(final ChannelHandlerContext ctx) {
    log.info(LOG_ACTIVE, ctx.channel().remoteAddress());
  }

  @Override
  public void channelInactive(ChannelHandlerContext ctx) throws Exception {
    Channel channel = ctx.channel();
    String address = channel.remoteAddress().toString();
    String sessionId = channel.attr(SESSION_ID_KEY).get();
    log.info(LOG_INACTIVE, address, sessionId);
    channel.close();
    ctx.close();
    Attribute<ISender> attribute = channel.attr(SENDER_KEY);
    ISender sender = attribute.get();
    if (sessionId != null && sender != null) {
      messageHandler.discontected(sender);
    }
  }

  @Override
  public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
    log.info("[Coming Out Error]IP:{} ; ERROR:{}", ctx.channel().remoteAddress(), cause.getMessage());
    ctx.channel().close();
    ctx.close();
  }

  @Override
  protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
    if (check(msg)) {
      Channel channel = ctx.channel();
      Attribute<ISender> attribute = channel.attr(SENDER_KEY);
      ISender sender = attribute.get();
      if (sender == null) {
        sender = new NettyTcpSender(channel, encrypt);
        attribute.set(sender);
        Attribute<String> session = channel.attr(SESSION_ID_KEY);
        session.set(UUID.randomUUID().toString());
      }

      byte[] data = new byte[msg.readableBytes()];
      msg.readBytes(data);
      if (data.length > 0) {
        messageHandler.handle(encrypt.deEncrypt(data), sender);
      }
    }
  }

  protected abstract boolean check(ByteBuf msg);

}
