package org.server.assistant.io.netty.server.tcp;

import static java.util.concurrent.TimeUnit.SECONDS;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.timeout.IdleStateHandler;
import org.server.assistant.io.message.IConnectHandler;
import org.server.assistant.io.netty.server.INettyServerConfig;
import org.server.assistant.secret.IEncrypt;

/**
 * TCP协议接收器
 * @author 	fuhuiyuan
 */
public final class NettyTcpServer extends AbstractNettyTcpServer<INettyServerConfig> {

  public NettyTcpServer(INettyServerConfig config) {
    super(config);
  }

  @Override
  protected ChannelHandler createChildHandler(INettyServerConfig config) {
    final long readerIdleTime = config.getReaderIdleTime();
    final long writerIdleTime = config.getWriterIdleTime();
    final long allIdleTime = config.getAllIdleTime();
    final IConnectHandler messageHandler = config.getConnectHandler();
    final IEncrypt encrypt = config.getEncrypt();
    return new ChannelInitializer<SocketChannel>() {

      @Override
      protected void initChannel(SocketChannel ch) {
        ChannelPipeline pipeline = ch.pipeline();
        // 读信道空闲,写信道空闲,读，写信道空闲
        pipeline.addLast("idleStateHandler", new IdleStateHandler(readerIdleTime, writerIdleTime, allIdleTime, SECONDS));
        // 粘包处理
        pipeline.addLast("Decoder", new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0, 4, 0, 4));
        // 业务逻辑处理
        pipeline.addLast("Handler", new NettyTcpHandler(messageHandler, encrypt));
        log.info("Init channel done.");
      }

    };
  }

}
