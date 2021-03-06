package org.server.assistant.pubsub.impl;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.server.assistant.cache.redis.IJedisReources;
import org.server.assistant.coder.stream.IStreamCoder;
import org.server.assistant.coder.stream.StreamCoders;
import org.server.assistant.pubsub.IPubsub;
import org.server.assistant.pubsub.ISubscribe;

public class RedisBlockQueue implements IPubsub<ISubscribe> {

  private static final int THREAD_COUNT = 5;

  private final IJedisReources cache;

  private final ExecutorService executorService;

  private final IStreamCoder coder;

  public RedisBlockQueue(IJedisReources cache, IStreamCoder coder) {
    this.cache = cache;
    this.coder = coder;
    executorService = Executors.newFixedThreadPool(THREAD_COUNT);
  }

  public RedisBlockQueue(IJedisReources cache) {
    this(cache, StreamCoders.newProtoStuffCoder());
  }

  @Override
  public void publish(String channel, Object message) {
    cache.exec((jedis) -> jedis.lpush(coder.write(channel), coder.write(message)));
  }

  @Override
  public void subscribe(ISubscribe subscribe, String... channel) {
    executorService.execute(new SubscribeThread(subscribe, channel));
  }

  protected class SubscribeThread implements Runnable {

    private final ISubscribe subscribe;

    private final String[] channels;

    public SubscribeThread(ISubscribe subscribe, String... channels) {
      this.subscribe = subscribe;
      this.channels = channels;
    }

    @Override
    public void run() {
      cache.exec((jedis) -> {
        List<byte[]> list = jedis.blpop(subscribe.getTimeout(), coder.write(channels));
        while (list.size() > 0) {
          subscribe.onMessage(coder.read(list.remove(0)).toString(), coder.read(list.remove(0)));
        }
        subscribe(subscribe, channels);
      });
    }

  }

  @Override
  public void unsubscribe(ISubscribe subscribe, String... channel) {
    throw new UnsupportedOperationException();
  }

}
