package org.server.assistant.sequence.impl;

import java.util.Map;

import org.server.assistant.sequence.IInstanceIdMaker;
import org.server.assistant.sequence.IInstanceIdManager;

import com.google.common.collect.Maps;
import redis.clients.jedis.Jedis;

public class RedisPoolIdManager implements IInstanceIdManager {

  /**id生成器集合*/
  protected Map<String, IInstanceIdMaker> idMakers;

  protected Jedis jedis;

  public RedisPoolIdManager(Jedis jedis) {
    this.jedis = jedis;
    idMakers = Maps.newHashMap();
  }

  @Override
  public void create(String name, long baseValue) {
    if (!idMakers.containsKey(name)) {
      idMakers.put(name, new RedisIdMaker(name, jedis, baseValue));
    }
  }

  @Override
  public long next(String name) {
    if (idMakers.containsKey(name)) {
      return idMakers.get(name).nextInstanceId();
    } else {
      throw new NullPointerException("Do not have " + name + " id maker.");
    }
  }

}
