package org.server.assistant.pubsub.impl;

import java.util.concurrent.Executors;

import org.server.assistant.pubsub.IPubsub;
import org.server.assistant.system.SystemUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.eventbus.AsyncEventBus;
import com.google.common.eventbus.EventBus;

public class EventBusPubsub implements IPubsub<Object> {

  public static final String CHANNEL_ASYNC = "async";

  public static final String CHANNEL_ASYNC_ONLY = "async_only";

  private static final Logger log = LoggerFactory.getLogger(EventBusPubsub.class);

  private final EventBus eventBus;

  private final EventBus asyncEventBus;

  public EventBusPubsub() {
    eventBus = new EventBus();
    asyncEventBus = new AsyncEventBus("EventBusPubsub", Executors.newFixedThreadPool(SystemUtil.CPU_AMOUNT));
  }

  @Override
  public void publish(String channel, Object message) {
    try {
      if (CHANNEL_ASYNC.equals(channel) || CHANNEL_ASYNC_ONLY.equals(channel)) {
        asyncEventBus.post(message);
      }
      if (!CHANNEL_ASYNC_ONLY.equals(channel)) {
        eventBus.post(message);
      }
    } catch (Exception e) {
      log.error("", e);
    }
  }

  @Override
  public void subscribe(Object subscribe, String... channel) {
    if(channel == null || channel.length == 0 || !CHANNEL_ASYNC_ONLY.equals(channel[0])){
      eventBus.register(subscribe);
    }
    asyncEventBus.register(subscribe);
  }

  @Override
  public void unsubscribe(Object subscribe, String... channel) {
    if(channel == null || channel.length == 0 || !CHANNEL_ASYNC_ONLY.equals(channel[0])){
      eventBus.unregister(subscribe);
    }
    asyncEventBus.unregister(subscribe);
  }

}
