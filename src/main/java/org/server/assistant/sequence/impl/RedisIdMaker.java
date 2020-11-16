package org.server.assistant.sequence.impl;

import org.server.assistant.sequence.IInstanceIdMaker;
import redis.clients.jedis.Jedis;

/**
 * Redis-id生成器
 * @author	fuhuiyuan
 */
public class RedisIdMaker implements IInstanceIdMaker {

  private final Jedis jedis;
  /**名称*/
  private final String name;

  public RedisIdMaker(String name, Jedis jedis, int baseValue) {
    this.jedis = jedis;
    this.name = name;

    int currentValue = jedis.exists(name) ? nextInstanceId() : 0;
    for (int i = currentValue;i < baseValue;i++) {
      nextInstanceId();
    }
  }

  @Override
  public int nextInstanceId() {
    return jedis.incr(name).intValue();
  }

  @Override
  public int currentInstanceId() {
    return Integer.parseInt(jedis.get(name));
  }

}
